package com.rookiefly.commons.codec;

/**
 * Created by rookiefly on 2015/7/30.
 * unicode转化工具
 */
public class UnicodeUtil {

    public static String string2Unicode(String str) {
        StringBuffer out = new StringBuffer("");
        char[] utfBytes = str.toCharArray();
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            out.append("\\u");
            out.append(hexB);
        }
        return out.toString();
    }

    public static String unicode2String(String unicodeStr) {
        StringBuffer sb = new StringBuffer();
        String str[] = unicodeStr.toLowerCase().split("\\\\u");
        for (int i = 0; i < str.length; i++) {
            if (str[i].equals(""))
                continue;
            char c = (char) Integer.parseInt(str[i].trim(), 16);
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(UnicodeUtil.string2Unicode("中国"));
        System.out.println(UnicodeUtil.unicode2String("\\u4e2d\\u56fd"));
    }

}

