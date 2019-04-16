package com.rookiefly.commons.qlexpress;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;

public class QLExpressDemo {

    public static void main(String[] args) throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
//        context.put("a", 1);
//        context.put("b", 2);
//        context.put("c", 3);
        String express = "function add(int a,int b){\n" +
                "  return a+b;\n" +
                "};\n" +
                "\n" +
                "function sub(int a,int b){\n" +
                "  return a - b;\n" +
                "};\n" +
                "\n" +
                "a=10;\n" +
                "return add(a,4) + sub(a,9);";
        Object r = runner.execute(express, context, null, true, false);
        System.out.println(r);
    }
}
