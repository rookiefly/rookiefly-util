package com.rookiefly.commons.ip;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.rookiefly.commons.json.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class IPUtils {

    private static final String TAOBAO_IP_REST_API = "http://ip.taobao.com/service/getIpInfo.php?ip=%s";

    public static String getCityString(String ipAddress) {
        String result = null;
        try {
            URL url = new URL(String.format(TAOBAO_IP_REST_API, ipAddress));
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            result = IOUtils.toString(inputStream);
            inputStream.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, String> getCityMapResult(String ipAddress) {
        Map<String, String> dataMap = null;
        String result = getCityString(ipAddress);
        if (StringUtils.isNotBlank(result)) {
            Map<String, Object> mapResult = JsonUtil.readMap(result);
            dataMap = (Map<String, String>) mapResult.get("data");
        }
        return dataMap;
    }

    public static String getCityName(String ipAddress) {
        Map<String, String> dataMap = getCityMapResult(ipAddress);
        if (dataMap != null) {
            return dataMap.get("city");
        }
        return null;
    }

    public static boolean ipIsInNet(String iparea, String ip) {
        if (iparea == null)
            throw new NullPointerException("IP段不能为空！");
        if (ip == null)
            throw new NullPointerException("IP不能为空！");
        iparea = iparea.trim();
        ip = ip.trim();
        final String REGX_IP = "((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]\\d|\\d)";
        final String REGX_IPB = REGX_IP + "\\-" + REGX_IP;
        if (!iparea.matches(REGX_IPB) || !ip.matches(REGX_IP))
            return false;
        int idx = iparea.indexOf('-');
        String[] sips = iparea.substring(0, idx).split("\\.");
        String[] sipe = iparea.substring(idx + 1).split("\\.");
        String[] sipt = ip.split("\\.");
        long ips = 0L, ipe = 0L, ipt = 0L;
        for (int i = 0; i < 4; ++i) {
            ips = ips << 8 | Integer.parseInt(sips[i]);
            ipe = ipe << 8 | Integer.parseInt(sipe[i]);
            ipt = ipt << 8 | Integer.parseInt(sipt[i]);
        }
        if (ips > ipe) {
            long t = ips;
            ips = ipe;
            ipe = t;
        }
        return ips <= ipt && ipt <= ipe;
    }

    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    public static String getIpaddr(HttpServletRequest request) {
        String ipaddr = request.getHeader("X-Forwarded-For");
        if (StringUtils.isBlank(ipaddr) || ipaddr.equalsIgnoreCase("unknown")) {
            ipaddr = request.getHeader("Proxy-Client-ipaddr");
        }

        if (StringUtils.isBlank(ipaddr) || ipaddr.equalsIgnoreCase("unknown")) {
            ipaddr = request.getHeader("WL-Proxy-Client-ipaddr");
        }

        if (StringUtils.isBlank(ipaddr) || ipaddr.equalsIgnoreCase("unknown")) {
            ipaddr = request.getHeader("HTTP_CLIENT_ipaddr");
        }

        if (StringUtils.isBlank(ipaddr) || ipaddr.equalsIgnoreCase("unknown")) {
            ipaddr = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isBlank(ipaddr) || ipaddr.equalsIgnoreCase("unknown")) {
            ipaddr = request.getRemoteAddr();
        }

        if (!StringUtils.isBlank(ipaddr) && ipaddr.indexOf(",") > 0) {
            String[] ipaddrs = ipaddr.split(",");
            ipaddr = ipaddrs[0];
        }

        return ipaddr;
    }

    public static void main(String[] args) {
        String ipStr = "192.168.0.1";
        long longIp = ipToLong(ipStr);
        System.out.println("192.168.0.1 的整数形式为：" + longIp);
        System.out.println("整数" + longIp + "转化成字符串IP地址："
                + longToIP(longIp));
        //ip地址转化成二进制形式输出
        System.out.println("192.168.0.1 的二进制形式为：" + Long.toBinaryString(longIp));

    }
}
