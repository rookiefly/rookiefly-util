package com.rookiefly.commons.mybatis.plugin;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Mybatis sql日志记录插件
 * @author rookiefly
 */
@Intercepts({
             @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
             @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                                                                         RowBounds.class, ResultHandler.class }),
             @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                                                                         RowBounds.class, ResultHandler.class,
                                                                         CacheKey.class, BoundSql.class }) })
public class SqlMonitorManager implements Interceptor {
    private static final Logger sqlStatLogger = Logger.getLogger("mysqlStatLogger");
    private boolean           showSql       = true;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (!showSql) {
            return invocation.proceed();
        }

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        if (mappedStatement == null) {
            return invocation.proceed();
        }

        String sqlId = mappedStatement.getId();
        Object returnValue = null;
        int resultCode = 0;
        long start = System.currentTimeMillis();
        try {
            returnValue = invocation.proceed();
        } catch (Exception e) {
            resultCode = 1;
            throw e;
        } finally {
            long end = System.currentTimeMillis();
            long time = end - start;
            sqlStatLogger.info(sqlId + "," + resultCode + "," + time);
        }
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        if (properties.containsKey("show_sql")) {
            String value = properties.getProperty("show_sql");
            if (Boolean.TRUE.toString().equals(value)) {
                this.showSql = true;
            }
        }
    }

}
