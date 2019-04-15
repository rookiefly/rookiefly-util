package com.rookiefly.test.commons.qlexpress;

import com.ql.util.express.Operator;

import java.util.ArrayList;
import java.util.List;

public class JoinFunction extends Operator {

    private static final long serialVersionUID = 1L;

    public Object executeInner(Object[] list) {
        List result = new ArrayList();
        for (Object o : list) {
            result.add(o);
        }
        return result;
    }
}