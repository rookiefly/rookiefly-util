package com.rookiefly.commons.sql;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 读取SQL脚本并解析执行
 *
 * @author rookiefly
 */
public class SqlFileExecutor {

    /**
     * 读取 SQL 文件，获取 SQL 语句
     *
     * @param sqlFile SQL 脚本文件
     * @return List<sql> 返回所有 SQL 语句的 List
     */
    private List<String> loadSql(String sqlFile) {
        List<String> sqlList = new ArrayList<>();

        try {
            InputStream sqlFileIn = new FileInputStream(sqlFile);

            StringBuilder sqlSb = new StringBuilder();
            byte[] buff = new byte[1024];
            int byteRead = 0;
            while ((byteRead = sqlFileIn.read(buff)) != -1) {
                sqlSb.append(new String(buff, 0, byteRead));
            }

            // Windows 下换行是 \r\n, Linux 下是 \n
            String[] sqlArr = sqlSb.toString().split("(;\\s*\\r\\n)|(;\\s*\\n)");
            for (int i = 0; i < sqlArr.length; i++) {
                String sql = sqlArr[i].replaceAll("--.*", "").trim();
                if (!sql.equals("")) {
                    sqlList.add(sql);
                }
            }
            return sqlList;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 传入连接来执行 SQL 脚本文件，这样可与其外的数据库操作同处一个事物中
     *
     * @param conn    传入数据库连接
     * @param sqlFile SQL 脚本文件
     * @throws Exception
     */
    public void execute(Connection conn, String sqlFile) throws Exception {
        Statement stmt = null;
        List<String> sqlList = loadSql(sqlFile);
        stmt = conn.createStatement();
        for (String sql : sqlList) {
            stmt.addBatch(sql);
        }
        int[] rows = stmt.executeBatch();
        System.out.println("Row count:" + Arrays.toString(rows));
    }

    /**
     * 自建连接，独立事物中执行 SQL 文件
     *
     * @param sqlFile SQL 脚本文件
     * @throws Exception
     */
    public void execute(String sqlFile) throws Exception {
        Connection conn = DBCenter.getConnection();
        Statement stmt = null;
        List<String> sqlList = loadSql(sqlFile);
        try {
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            for (String sql : sqlList) {
                stmt.addBatch(sql);
            }
            int[] rows = stmt.executeBatch();
            System.out.println("Row count:" + Arrays.toString(rows));
            DBCenter.commit(conn);
        } catch (Exception ex) {
            DBCenter.rollback(conn);
            throw ex;
        } finally {
            DBCenter.close(null, stmt, conn);
        }
    }

    public static void main(String[] args) {
        List<String> sqlList = new SqlFileExecutor().loadSql(args[0]);
        System.out.println("size:" + sqlList.size());
        for (String sql : sqlList) {
            System.out.println(sql);
        }
    }
}