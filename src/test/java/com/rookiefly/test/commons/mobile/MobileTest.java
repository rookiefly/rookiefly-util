package com.rookiefly.test.commons.mobile;

import org.junit.Assert;
import org.junit.Test;

import com.rookiefly.commons.mobile.MobileUtil;

public class MobileTest {

    @Test
    public void test01() {
        Assert.assertTrue(MobileUtil.isRegualTel("0571-8342343"));
        Assert.assertTrue(MobileUtil.isRegualMob("13185028561"));
    }

    @Test
    public void test02() {
        Assert.assertEquals("城市归属地","杭州",MobileUtil.getMobieZone("13185028561").getCity());
    }
}
