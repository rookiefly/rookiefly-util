package com.rookiefly.test.commons.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;

@ExcelTarget("studentEntity")
public class Student {

    @Excel(name = "id", orderNum = "1")
    private long id;

    @Excel(name = "名称", orderNum = "2")
    private String name;

    @Excel(name = "年龄", orderNum = "3")
    private int age;

    @Excel(name = "性别", orderNum = "4")
    private boolean sex;

    @Excel(name = "生日", orderNum = "5", exportFormat="yyyy-MM-dd")
    private Date birthday;

    public Student() {
        super();
    }

    public Student(long id, String name, int age, boolean sex, Date birthday) {
        super();
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.birthday = birthday;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}