package com.rookiefly.test.commons.beanutil;

import com.github.jsonzou.jmockdata.JMockData;
import com.rookiefly.commons.beanutil.WrappedBeanCopier;
import org.junit.Test;

public class BeanCopierTest {

    @Test
    public void test01() {
        UserDO userDO = JMockData.mock(UserDO.class);
        System.out.println(userDO.toString());
        UserVO userVO = WrappedBeanCopier.copyProperties(userDO, UserVO.class);
        System.out.println(userVO);
    }

}
