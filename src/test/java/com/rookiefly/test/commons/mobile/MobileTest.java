package com.rookiefly.test.commons.mobile;

import com.rookiefly.commons.mobile.MobileUtil;
import org.junit.Assert;
import org.junit.Test;

public class MobileTest {

    @Test
    public void test01() {
        Assert.assertTrue(MobileUtil.isRegualTel("0571-8342343"));
        Assert.assertTrue(MobileUtil.isRegualMob("13185028561"));
        System.out.println(MobileUtil.formatMobile("13185028561", 4,3));
    }
}
