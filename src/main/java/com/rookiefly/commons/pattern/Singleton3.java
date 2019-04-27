package com.rookiefly.commons.pattern;

import java.util.Date;

public class Singleton3 {

    private Singleton3() {

    }

    public static class Holder {
        public static Singleton3 singleton3 = new Singleton3();
    }

    public static Singleton3 getInstance() {
        return Holder.singleton3;
    }

    public Date getDate() {
        return new Date();
    }
}
