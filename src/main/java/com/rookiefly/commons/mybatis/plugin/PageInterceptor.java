package com.rookiefly.commons.mybatis.plugin;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Mybatis分页拦截器
 *
 * <p>
 * 对<code>StatementHandler</code>对象的prepare(Connection)方法进行拦截,以实现mybatis自动分页功能
 * </P>
 *
 * @author rookiefly
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class PageInterceptor implements Interceptor {

    private static final Log logger = LogFactory.getLog(PageInterceptor.class);

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();
    private static final ReflectorFactory DEFAULT_REFLECTOR_FACTORY = new DefaultReflectorFactory();

    private static String defaultDialect = "mysql";                                 // 数据库类型(默认为mysql)
    private static String defaultPageSqlId = "pageQuery";                             // 需要拦截的id(支持正则匹配)
    private static String dialect = "";                                      // 数据库类型
    private static String pageSqlId = "";                                      // 需要拦截的id(支持正则匹配)

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        // 进行包装
        MetaObject metaStatementHandler = MetaObject.forObject(statementHandler, DEFAULT_OBJECT_FACTORY,
                DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);

        // 分离代理对象链(由于目标类可能被多个拦截器拦截,从而形成多次代理,通过下面的两次循环可以分离出最原始的的目标类)
        while (metaStatementHandler.hasGetter("h")) {
            Object object = metaStatementHandler.getValue("h");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }
        // 分离最后一个代理对象的目标类
        while (metaStatementHandler.hasGetter("target")) {
            Object object = metaStatementHandler.getValue("target");
            metaStatementHandler = MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY, DEFAULT_REFLECTOR_FACTORY);
        }

        MappedStatement mappedStatement = (MappedStatement) metaStatementHandler.getValue("delegate.mappedStatement");

        // 重写需要分页的sql语句. 通过MappedStatement的id匹配
        if (StringUtils.isBlank(pageSqlId)) {
            pageSqlId = defaultPageSqlId;
            logger.warn("Property pageSqlId is not setted, use default:" + defaultPageSqlId);
        }

        // 分页查询
        if (mappedStatement.getId().matches(".*" + pageSqlId + ".*")) {
            BoundSql boundSql = (BoundSql) metaStatementHandler.getValue("delegate.boundSql");
            Object parameterObject = boundSql.getParameterObject();
            if (parameterObject == null) {
                throw new NullPointerException("parameterObject is null!");
            }

            Object pageParam = metaStatementHandler.getValue("delegate.boundSql.parameterObject.page");

            Object queryTotalCount = metaStatementHandler.getValue("delegate.boundSql.parameterObject.queryTotalCount");

            if (queryTotalCount != null && !Boolean.valueOf(queryTotalCount.toString())) { // 没有分页查询参数, 不需要分页查询所有
                return invocation.proceed();
            }

            if (pageParam == null) { // 没有分页查询参数, 不需要分页查询所有
                return invocation.proceed();
            }

            Page<?> page = (Page<?>) pageParam;
            String sql = boundSql.getSql();

            // 重写sql
            String pageSql = buildPageSql(sql, page);
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);

            // 采用物理分页后, 就不需要mybatis的内存分页了
            metaStatementHandler.setValue("delegate.rowBounds.offset", RowBounds.NO_ROW_OFFSET);
            metaStatementHandler.setValue("delegate.rowBounds.limit", RowBounds.NO_ROW_LIMIT);

            // 重设分页参数里的总页数等
            Connection connection = (Connection) invocation.getArgs()[0];
            setPageParameter(sql, connection, mappedStatement, boundSql, page);
        }

        // 将执行权交给下一个拦截器
        return invocation.proceed();
    }

    /**
     * 获取总记录数
     *
     * @param sql
     * @param connection
     * @param mappedStatement
     * @param boundSql
     * @param page
     */
    private void setPageParameter(String sql, Connection connection, MappedStatement mappedStatement,
                                  BoundSql boundSql, Page<?> page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") as total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);

            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql,
                    boundSql.getParameterMappings(), boundSql.getParameterObject());
            try {
                // 利用反射为countBS添加额外参数
                Field f = boundSql.getClass().getDeclaredField("additionalParameters");
                f.setAccessible(true);
                @SuppressWarnings("unchecked")
                Map<String, Object> additionalParam = (Map<String, Object>) f.get(boundSql);

                for (Entry<String, Object> e : additionalParam.entrySet()) {
                    countBS.setAdditionalParameter(e.getKey(), e.getValue());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // 设置参数
            setParameters(countStmt, mappedStatement, countBS);
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotalCount(totalCount);

        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                if (null != rs) {
                    rs.close();
                }
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                if (null != countStmt) {
                    countStmt.close();
                }
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }

    }

    /**
     * 对SQL参数(?)设值
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @throws SQLException
     */
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql)
            throws SQLException {
        ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(),
                boundSql);
        parameterHandler.setParameters(ps);
    }

    /**
     * 根据数据库类型，生成特定的分页sql
     *
     * @param sql
     * @param page
     * @return
     */
    private String buildPageSql(String sql, Page<?> page) {
        if (page != null) {
            StringBuilder pageSql = new StringBuilder();
            if (StringUtils.isBlank(dialect)) {
                dialect = defaultDialect;
            }
            if ("mysql".equalsIgnoreCase(dialect)) {
                pageSql = buildPageSqlForMysql(sql, page);
            } else if ("oracle".equals(dialect)) {
                throw new UnsupportedOperationException("Un supported for Oracle");
            } else {
                return sql;
            }
            return pageSql.toString();
        } else {
            return sql;
        }
    }

    /**
     * mysql的分页语句
     *
     * @param sql
     * @param page
     * @return String
     */
    private StringBuilder buildPageSqlForMysql(String sql, Page<?> page) {
        StringBuilder pageSql = new StringBuilder();
        String beginrow = String.valueOf((page.getCurrentPage() - 1) * page.getPageSize());
        pageSql.append(sql);
        pageSql.append(" limit " + beginrow + "," + page.getPageSize());
        return pageSql;
    }

    @Override
    public Object plugin(Object target) {
        // 当目标类是StatementHandler才进行包装, 否则直接返回目标对象
        // 减少目标对象被代理的次数
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
        pageSqlId = properties.getProperty("pageSqlId");
        dialect = properties.getProperty("dialect");
    }
}
