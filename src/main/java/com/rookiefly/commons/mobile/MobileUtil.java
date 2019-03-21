package com.rookiefly.commons.mobile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机工具类
 */
public class MobileUtil {

    /**
     * 判断是否规则的手机号码 正常的手机号码 +86 11位手机号码 /11位手机号码
     *
     * @param number
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
