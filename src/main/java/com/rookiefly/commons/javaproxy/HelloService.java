package com.rookiefly.commons.javaproxy;

public class HelloService implements Service {

    @Override
    public void doSomething(String name) {
        System.out.println("hello " + name);
    }
}
