package com.rookiefly.commons.pattern;

import java.util.Date;

public class Singleton2 {

    private Singleton2() {

    }

    private static volatile Singleton2 singleton = null;

    public static Singleton2 getInstance() {
        if (singleton == null) {
            synchronized (Singleton2.class) {
                if (singleton == null) {
                    singleton = new Singleton2();
                }
            }
        }
        return singleton;
    }

    public Date getDate() {
        return new Date();
    }
}
