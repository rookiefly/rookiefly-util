package com.rookiefly.commons.mobile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * 手机工具类
 * 
 * @author kyo.ou 2013-7-9
 * 
 */
public class MobileUtil {
    // 财付通
    private static final String TENPAY = "http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi";

    /**
     * 财付通接口
     * 
     * @param mobile
     * @return 手机归属地信息
     * @throws MalformedURLException
     * @throws IOException
     */
    public static MobileZone getMobieZone(String mobile) {
        HttpURLConnection conn = null;
        InputStream in = null;
        try {
            conn = (HttpURLConnection) new URL(TENPAY).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            out.write(("chgmobile=" + mobile).getBytes());
            out.close();

            if (conn.getResponseCode() == 200) {
                in = conn.getInputStream();
                byte[] data = read(in);
                return parseXml(new String(data, "GBK"));
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null)
                try {
                    in.close();
                } catch (IOException e) {
                }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private static byte[] read(InputStream in) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
        }
        return out.toByteArray();
    }

    private static MobileZone parseXml(String xml) throws DocumentException, UnsupportedEncodingException {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(new ByteArrayInputStream(xml.getBytes("gb2312")));
        Element root = document.getRootElement();
        @SuppressWarnings("unchecked")
        Iterator<Element> it = root.elementIterator();

        MobileZone mobileZone = new MobileZone();
        while (it.hasNext()) {
            Element e = it.next();
            if (e.getName() == "province") {
                mobileZone.setProvince(e.getText());
            }
            if (e.getName() == "city") {
                mobileZone.setCity(e.getText().trim());
            }
            if (e.getName() == "chgmobile") {
                mobileZone.setMobile(e.getText());
            }
            if (e.getName() == "supplier") {
                mobileZone.setSupplier(e.getText());
            }
        }
        return mobileZone;
    }

    public static class MobileZone {

        private String province;
        private String city;
        private String supplier; // 运营商
        private String mobile;

        public MobileZone() {
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getSupplier() {
            return supplier;
        }

        public void setSupplier(String supplier) {
            this.supplier = supplier;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        @Override
        public String toString() {
            return "MobileZone [province=" + province + ", city=" + city + ", supplier=" + supplier + ", mobile="
                   + mobile + "]";
        }
        
    }

    /**
     * 判断是否规则的手机号码 正常的手机号码 +86 11位手机号码 /11位手机号码
     * 
     * @param mobiles
     * @return
     */
    public static boolean isRegualMob(String number) {
        Pattern p = Pattern
            .compile("^((13[0-9])|(14[57])|(15[^4,\\D])|(17[0678])|(18[0-9])|(\\+8613[0-9])|(\\+8614[57])|(\\+8615[^4,\\D])|(\\+8617[0678])|(\\+8618[0-9]))\\d{8}$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public static boolean isRegualTel(String number) {
        Pattern p = Pattern.compile("^(0[0-9]{2,3}(\\-)?)?([2-9][0-9]{6,7})$");
        Matcher m = p.matcher(number);
        return m.matches();
    }

    public static String formatMobile(String mobile, int size, int prefixSize, int profixSize) {

        if (mobile != null)
            return "";
        if (mobile.length() < 11)
            return mobile;
        StringBuilder builder = new StringBuilder();

        String mobilePre = mobile.substring(0, prefixSize);
        builder.append(mobilePre);
        for (int i = 0; i < size; i++) {
            builder.append('*');
        }
        String mobileEnd = mobile.substring(mobile.length() - 4, mobile.length());
        builder.append(mobileEnd);
        return builder.toString();
    }
}
