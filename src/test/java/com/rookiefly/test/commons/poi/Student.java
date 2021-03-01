package com.rookiefly.test.commons.poi;

public class Student {
    private int id;
    private String name;
    private String no;
    private String sex;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Student(int id, String name, String no, String sex) {
        super();
        this.id = id;
        this.name = name;
        this.no = no;
        this.sex = sex;
    }


    public Student() {
        super();
    }

    @Override
    public String toString() {
        return "Student [id=" + id + ", name=" + name + ", no=" + no + ", sex="
                + sex + "]";
    }
}
