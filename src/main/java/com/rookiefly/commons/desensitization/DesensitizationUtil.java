package com.rookiefly.commons.desensitization;

import org.apache.commons.lang.StringUtils;

public class DesensitizationUtil {

    public static String email(String email) {
        if (StringUtils.isNotBlank(email)) {
            int index = StringUtils.indexOf(email, "@");
            if (index > 1) {
                return StringUtils.rightPad(StringUtils.left(email, 1),
                        index - 1, "*").concat(StringUtils.mid(email, index - 1, StringUtils.length(email)));
            }
        }
        return email;
    }

    public static void main(String[] args) {
        System.out.println(DesensitizationUtil.email("rookiefly@163.com"));
    }
}
