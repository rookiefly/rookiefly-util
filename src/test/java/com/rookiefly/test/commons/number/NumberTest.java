package com.rookiefly.test.commons.number;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/8.
 */
public class NumberTest {

    @Test
    public void testBigDecimal() {
        BigDecimal bigDecimal = BigDecimal.valueOf(0.000);
        System.out.println(bigDecimal);
        Assert.assertTrue(bigDecimal.doubleValue() == 0);
        Assert.assertTrue(bigDecimal.intValue() == 0);
    }

    @Test
    public void testNumberFormat() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        DecimalFormat decimalFormat = new DecimalFormat("0.0#");
        Assert.assertEquals("", "1.01", decimalFormat.format(1.009));
        Assert.assertEquals("", "1.00", decimalFormat.format(1.0039));
        Assert.assertEquals("", "1.00", decimalFormat.format(1.0));
        Assert.assertEquals("", "21.00", decimalFormat.format(21.0));
    }

    @Test
    public void testIntegerEqual() {
/*        Integer a = null;
        Integer b = 6;
        Assert.assertTrue(a.equals(b));*/
        Assert.assertTrue(Runtime.getRuntime().availableProcessors() == 4);
    }

    @Test
    public void testRound() {
        System.out.println("舍掉小数取整:Math.floor(2)=" + (int)Math.floor(2));
        System.out.println("舍掉小数取整:Math.floor(2.1)=" + (int)Math.floor(2.1));
        System.out.println("舍掉小数取整:Math.floor(2.5)=" + (int)Math.floor(2.5));
        System.out.println("舍掉小数取整:Math.floor(2.9)=" + (int)Math.floor(2.9));
        System.out.println("四舍五入取整:(2)=" + new BigDecimal("2").setScale(1, BigDecimal.ROUND_HALF_UP));
        System.out.println("四舍五入取整:(2.15)=" + new BigDecimal("2.15").setScale(1, BigDecimal.ROUND_HALF_UP));
        System.out.println("四舍五入取整:(2.56)=" + new BigDecimal("2.56").setScale(1, BigDecimal.ROUND_HALF_UP));
        System.out.println("四舍五入取整:(2.92)=" + new BigDecimal("2.92").setScale(1, BigDecimal.ROUND_HALF_UP));
    }

    @Test
    public void testCharLen(){
        String ts = "abc汉子";
        Assert.assertEquals(ts.length(), 5);
        Assert.assertEquals(ts.getBytes().length, 7);
    }

    @Test
    public void testRemove() {

        List<Integer> priceList = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            priceList.add(i+3);
        }
        Integer priceTotal = priceList.get(0);
        for (int i = 1; i < priceList.size(); i++) {
            priceTotal = priceTotal + priceList.get(i);
        }
        List<Integer> subList = priceList.subList(1, priceList.size());

        priceList.clear();

        priceList.addAll(subList);

        System.out.println(priceList);
    }

    @Test
    public void testArgs() {
        double a = 1.00000000000001;
        double c = 1.00000000000001;
        int b = 1;
        System.out.println(c == a);
        System.out.println(Runtime.getRuntime().maxMemory());
    }

    @Test
    public void testConvertString() {
        Double aDouble = Double.valueOf("9");
        BigDecimal amount = BigDecimal.valueOf(aDouble);
        String format = new DecimalFormat("#.##").format(BigDecimal.valueOf(0.10));
        System.out.println(format);
    }

    @Test
    public void testArray(){
        int[] a = new int[2];
        System.out.println(a);
        System.out.println(String.format("percent:%X%%", 5));
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(processName);
    }

    @Test
    public void testConstact() {
        Constant c = new Constant();
        System.out.println(c.size);;
        System.out.println(c.size);;
    }
}
