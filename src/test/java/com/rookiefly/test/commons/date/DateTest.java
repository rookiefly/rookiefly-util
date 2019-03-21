package com.rookiefly.test.commons.date;

import org.apache.commons.lang.time.FastDateFormat;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Created by Administrator on 2015/7/8.
 */
public class DateTest {

    @Test
    public void test01() {

        FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        Assert.assertEquals("", "2015-07-08", date);
    }

    @Test
    public void test02() {

    }
}
