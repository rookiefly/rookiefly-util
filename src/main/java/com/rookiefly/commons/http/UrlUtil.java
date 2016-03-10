package com.rookiefly.commons.http;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rookiefly on 2015/7/28.
 * http请求url解析工具
 */
public class UrlUtil {

    private static String getQuery(String strURL) {
        if (StringUtils.isBlank(strURL)) {
            throw new IllegalArgumentException("url can't be null");
        }
        String queryString = null;
        strURL = strURL.trim();
        if (strURL.length() > 1) {
            String[] urlSplit = strURL.split("[?]");
            if (urlSplit.length > 1) {
                if (urlSplit[1] != null) {
                    queryString = urlSplit[1];
                }
            }
        }
        return queryString;
    }

    public static Map<String, String> getQueryMap(String url) {
        Map<String, String> mapRequest = new HashMap<String, String>();
        String[] arrSplit = null;
        String strUrlParam = getQuery(url);
        if (strUrlParam == null) {
            return mapRequest;
        }
        arrSplit = strUrlParam.split("[&]");
        for (String strSplit : arrSplit) {
            String[] arrSplitEqual = null;
            arrSplitEqual = strSplit.split("[=]");
            if (arrSplitEqual.length > 1) {
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);
            } else {
                if (arrSplitEqual[0] != "") {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        return mapRequest;
    }

    public static void main(String[] args) throws Exception {
        //String requestUrl = "http://10.0.53.73:8089/crm/home/index.htm?jsessionid=7799D1D46D9CF1E5155DA7BCD8E64CA6#";
        String requestUrl = "dubbo%3A%2F%2F192.168.56.1%3A20880%2Fcom.alibaba.dubbo.demo.DemoService%3Fanyhost%3Dtrue%26application%3Ddemo-provider%26dubbo%3D2.5.3%26interface%3Dcom.alibaba.dubbo.demo.DemoService%26loadbalance%3Droundrobin%26methods%3DsayHello%26pid%3D5764%26revision%3D2.5.3%26side%3Dprovider%26timestamp%3D1438137947891";
        System.out.println(new URL(requestUrl).toString());
        requestUrl = URLDecoder.decode(requestUrl,"utf-8");
        System.out.println(requestUrl);
        String query = UrlUtil.getQuery(requestUrl);
        System.out.println(query);
        System.out.println(getQueryMap(requestUrl));
    }
}
