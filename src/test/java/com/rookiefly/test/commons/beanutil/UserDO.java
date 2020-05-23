package com.rookiefly.test.commons.beanutil;

import lombok.Data;

import java.util.Date;

@Data
public class UserDO {

    private Long id;

    private String userName;

    private String password;

    private Integer age;

    private Date registerTime;
}