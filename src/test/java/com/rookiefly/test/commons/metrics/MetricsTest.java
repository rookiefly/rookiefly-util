package com.rookiefly.test.commons.metrics;

import com.alibaba.metrics.Counter;
import com.alibaba.metrics.MetricManager;
import com.alibaba.metrics.MetricName;
import org.junit.Test;

public class MetricsTest {

    @Test
    public void testCounter() {
        Counter hello = MetricManager.getCounter("test", MetricName.build("test.my.counter"));
        hello.inc();
        hello.inc();
        hello.inc();
        System.out.println(hello.getCount());
    }
}
