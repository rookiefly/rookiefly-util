package com.rookiefly.test.commons.json;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by rookiefly on 2015/12/1.
 */
public class Account {

    private String id;

    @JSONField(name="username")
    private String name;

    private int age;

    private Address address;

    public Account(String id, String name, int age, Address address) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JSONField(name="name")
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
