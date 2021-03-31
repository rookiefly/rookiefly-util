package com.rookiefly.test.commons.ip;

import com.rookiefly.commons.ip.IpUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Scanner;

public class IPUtilTest {

    @Test
    public void test01() throws Exception {
        String ipAddress = "50.236.19.10";
        String city = IpUtils.getCityName(ipAddress);
        Assert.assertEquals("", "杭州", city);
    }

    @Test
    public void test02() {
        String ip = "115.236.20.7";
        Assert.assertTrue(IpUtils.ipIsInNet("115.236.20.7-115.236.20.7", "115.236.20.7"));
    }

    @Test
    public void test03() throws IOException {
        System.out.println(execReadToString("hostname"));
        System.out.println(InetAddress.getLocalHost().getHostName());
    }

    public static String execReadToString(String execCommand) throws IOException {
        Process proc = Runtime.getRuntime().exec(execCommand);
        try (InputStream stream = proc.getInputStream()) {
            try (Scanner s = new Scanner(stream).useDelimiter("\\A")) {
                return s.hasNext() ? s.next() : "";
            }
        }
    }

    @Test
    public void test04() throws SocketException {
        StringBuilder sb = new StringBuilder();
        Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
        while (e.hasMoreElements()) {
            NetworkInterface ni = e.nextElement();
            sb.append(ni.toString());
        }

        System.out.println(sb);
    }

    @Test
    public void test05() {
        String ipStr = "192.168.0.1";
        long longIp = IpUtils.ipToLong(ipStr);
        System.out.println("192.168.0.1 的整数形式为：" + longIp);
        System.out.println("整数" + longIp + "转化成字符串IP地址："
                + IpUtils.longToIP(longIp));
        //ip地址转化成二进制形式输出
        System.out.println("192.168.0.1 的二进制形式为：" + Long.toBinaryString(longIp));
    }

    @Test
    public void test06() {
        System.out.println(IpUtils.getExternalIp());
    }
}
