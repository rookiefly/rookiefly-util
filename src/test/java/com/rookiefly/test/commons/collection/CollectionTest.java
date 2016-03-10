package com.rookiefly.test.commons.collection;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by rookiefly on 2015/10/12.
 */
public class CollectionTest {

    @Test
    public void test01() {

        List<Student> list = new ArrayList<>();
        Student student = new Student(1, "rokiefly", 25);
        list.add(student);
        list.add(student);
        for (Student stu : list) {
            System.out.println(stu);
        }
        System.out.println("--------");
        List<Student> listRm = new ArrayList<>();
        Student studentRm = new Student(1, "datouxiangzi", 25);
        listRm.add(student);
        listRm.add(studentRm);
        list.removeAll(listRm);
        for (Student stu : list) {
            System.out.println(stu);
        }
    }

    @Test
    public void test02() throws IOException {
        Properties properties = new Properties();
        properties.load(this.getClass().getResourceAsStream("/autoconfig.properties"));
        properties.load(this.getClass().getResourceAsStream("/autoconfig2.properties"));
        System.out.println(properties);
    }
}
