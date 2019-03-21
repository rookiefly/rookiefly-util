package com.rookiefly.commons.sql;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;
import org.apache.tools.ant.types.EnumeratedAttribute;

import java.io.File;

/**
 * 读取SQL脚本调用ant包执行
 *
 * @author rookiefly
 */
public class RunSqlScript {

    public void runSqlScript() {
        SQLExec sqlExec = new SQLExec();
        String driverClass = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1/test?useUnicode=true&characterEncoding=utf-8";
        String username = "root";
        String password = "password";
        sqlExec.setDriver(driverClass);
        sqlExec.setUrl(url);
        sqlExec.setUserid(username);
        sqlExec.setPassword(password);
        sqlExec.setSrc(new File("src/main/resources/user.sql"));
        //如果有出错的语句继续执行
        sqlExec.setOnerror((SQLExec.OnError) (EnumeratedAttribute.getInstance(SQLExec.OnError.class, "continue")));
        sqlExec.setPrint(true);
        sqlExec.setOutput(new File("src/main/resources/userinfo.txt"));
        sqlExec.setProject(new Project());
        sqlExec.execute();
    }

    public static void main(String[] args) {
        RunSqlScript rsc = new RunSqlScript();
        rsc.runSqlScript();
    }
}
