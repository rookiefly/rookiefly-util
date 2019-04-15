package com.rookiefly.test.commons.qlexpress;

import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import org.junit.Assert;
import org.junit.Test;

public class QLExpressTest {

    @Test
    public void testSimple() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("a", 1);
        context.put("b", 2);
        context.put("c", 3);
        String express = "a+b*c";
        Object r = runner.execute(express, context, null, true, false);
        Assert.assertEquals(7, r);
    }

    @Test
    public void testOperator() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        runner.addOperatorWithAlias("如果", "if", null);
        runner.addOperatorWithAlias("则", "then", null);
        runner.addOperatorWithAlias("否则", "else", null);

        String express = "如果  (语文+数学+英语>270) 则 {return 1;} 否则 {return 0;}";
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        context.put("语文", 80);
        context.put("数学", 90);
        context.put("英语", 95);
        Object r = runner.execute(express, context, null, false, false, null);
        Assert.assertEquals(0, r);
    }

    @Test
    public void testDefineOperator() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        runner.addOperator("join", new JoinOperator());
        Object r = runner.execute("1 join 2 join 3", context, null, false, false);
        System.out.println(r);
    }

    @Test
    public void testFunction() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<String, Object>();
        runner.addFunction("join",new JoinFunction());
        Object r = runner.execute("join(1,2,3)", context, null, false, false);
        System.out.println(r);
    }


}

