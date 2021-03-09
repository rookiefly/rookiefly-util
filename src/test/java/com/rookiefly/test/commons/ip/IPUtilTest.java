package com.rookiefly.test.commons.ip;

import com.rookiefly.commons.ip.IPUtils;
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
        String ipAddress = "115.236.20.7";
        String city = IPUtils.getCityName(ipAddress);
        Assert.assertEquals("", "杭州", city);
    }

    @Test
    public void test02() {
        String ip = "115.236.20.7";
        Assert.assertTrue(IPUtils.ipIsInNet("115.236.20.7-115.236.20.7", "115.236.20.7"));
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
}
