package com.rookiefly.commons.javaproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogHandler implements InvocationHandler {

    private Service service;

    public LogHandler(Service service) {
        this.service = service;
    }

    @Override

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("log start");
        Object invoke = method.invoke(service, args);
        System.out.println("log end");
        return invoke;
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloService();
        LogHandler logHandler = new LogHandler(helloService);
        Service service = (Service) Proxy.newProxyInstance(logHandler.getClass().getClassLoader(), helloService.getClass().getInterfaces(), logHandler);
        service.doSomething("rookiefly");
    }
}
