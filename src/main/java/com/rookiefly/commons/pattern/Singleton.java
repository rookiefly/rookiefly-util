package com.rookiefly.commons.pattern;

import java.util.Date;

public class Singleton {

    private Singleton() {

    }

    private static Singleton singleton = new Singleton();

    public static Singleton getInstance() {
        return singleton;
    }

    public Date getDate() {
        return new Date();
    }
}
