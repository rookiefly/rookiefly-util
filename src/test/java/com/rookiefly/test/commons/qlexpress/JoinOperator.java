package com.rookiefly.test.commons.qlexpress;

import com.ql.util.express.Operator;

import java.util.ArrayList;
import java.util.List;

public class JoinOperator extends Operator {

    private static final long serialVersionUID = 1L;

    public Object executeInner(Object[] list) {
        Object opdata1 = list[0];
        Object opdata2 = list[1];
        if (opdata1 instanceof List) {
            ((List) opdata1).add(opdata2);
            return opdata1;
        } else {
            List result = new ArrayList();
            result.add(opdata1);
            result.add(opdata2);
            return result;
        }
    }
}