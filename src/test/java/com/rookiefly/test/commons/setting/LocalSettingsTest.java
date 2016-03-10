package com.rookiefly.test.commons.setting;

import com.rookiefly.commons.setting.LocalSettings;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;

/**
 * Created by rookiefly on 2015/7/30.
 */
public class LocalSettingsTest {

    @Test
    public void test01() throws InterruptedException {
        String zk = LocalSettings.getString("zk");
        Assert.assertTrue(zk.equals("192.168.1.1:2181"));
        Thread.sleep(20000);
        zk = LocalSettings.getString("zk");
        Assert.assertTrue(zk.equals("192.168.1.101:2181"));
    }

    @Test
    public void test02(){
        String classpath = this.getClass().getResource("/").getPath();
        System.out.println(classpath);
        File classDir = new File(classpath);
/*        String[] files = classDir.list();
        System.out.println(files);
        for (String file : files) {
            System.out.println(file);
        }*/

        File[] files = classDir.listFiles();
        for (File file : files) {
            System.out.println(file.getName());
        }
    }

    @Test
    public void test03(){
        System.out.println(System.getProperty("user.name"));
    }

}
