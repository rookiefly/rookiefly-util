package com.rookiefly.commons.idcard;

import cn.hutool.core.date.DateUtil;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.console.ConsoleLog;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class IdentityUtil {

    private static Log log = new ConsoleLog(IdentityUtil.class);
    // wi =2(n-1)(mod 11)
    private static final int[] wi =
            {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    // verify digit
    private static final int[] vi =
            {1, 0, 'X', 9, 8, 7, 6, 5, 4, 3, 2};

    private static Map<String, String> provinceMap = new HashMap<>();

    static {
        provinceMap.put("11", "北京");
        provinceMap.put("12", "天津");
        provinceMap.put("13", "河北");
        provinceMap.put("14", "山西");
        provinceMap.put("15", "内蒙古");
        provinceMap.put("21", "辽宁");
        provinceMap.put("22", "吉林");
        provinceMap.put("23", "黑龙江");
        provinceMap.put("31", "上海");
        provinceMap.put("32", "江苏");
        provinceMap.put("33", "浙江");
        provinceMap.put("34", "安徽");
        provinceMap.put("35", "福建");
        provinceMap.put("36", "江西");
        provinceMap.put("37", "山东");
        provinceMap.put("41", "河南");
        provinceMap.put("42", "湖北");
        provinceMap.put("43", "湖南");
        provinceMap.put("44", "广东");
        provinceMap.put("45", "广西");
        provinceMap.put("46", "海南");
        provinceMap.put("50", "重庆");
        provinceMap.put("51", "四川");
        provinceMap.put("52", "贵州");
        provinceMap.put("53", "云南");
        provinceMap.put("54", "西藏");
        provinceMap.put("61", "陕西");
        provinceMap.put("62", "甘肃");
        provinceMap.put("63", "青海");
        provinceMap.put("64", "宁夏");
        provinceMap.put("65", "新疆");
        provinceMap.put("71", "台湾");
        provinceMap.put("81", "香港");
        provinceMap.put("82", "澳门");
        provinceMap.put("91", "国外");
    }

    private IdentityUtil() {
    }

    //verify
    public static boolean verify(String idcard) {
        if (StringUtils.isBlank(idcard)) {
            return false;
        }
        try {
            if (idcard.length() == 15) {
                idcard = uptoeighteen(idcard);
            }
            return verify18(idcard);
        } catch (Exception e) {
            log.debug(e, "身份证格式错误{}", idcard);
            return false;
        }
    }

    /**
     * 只支持18位身份证
     *
     * @param idcard
     * @return boolean
     */
    public static boolean verify18(String idcard) {
        if (StringUtils.isBlank(idcard)) {
            return false;
        }
        if (idcard.length() != 18) {
            return false;
        }
        //判断前两位省份是否正确
        if (provinceMap.get(idcard.substring(0, 2)) == null || "".equals(provinceMap.get(idcard.substring(0, 2)))) {
            return false;
        }
        if (!verifyBirthDay(idcard)) {
            return false;
        }
        String verify = idcard.substring(17, 18);
        try {
            if (verify.equalsIgnoreCase(getVerify(idcard))) {
                return true;
            }
        } catch (Exception e) {
            log.debug(e, "身份证格式错误{}", idcard);
            return false;
        }
        return false;
    }

    //get verify
    private static String getVerify(String eightcardid) {
        int[] ai = new int[18];
        int remaining = 0;
        if (eightcardid.length() == 18) {
            eightcardid = eightcardid.substring(0, 17);
        }
        if (eightcardid.length() == 17) {
            int sum = 0;
            for (int i = 0; i < 17; i++) {
                String k = eightcardid.substring(i, i + 1);
                ai[i] = Integer.parseInt(k);
            }
            for (int i = 0; i < 17; i++) {
                sum = sum + wi[i] * ai[i];
            }
            remaining = sum % 11;
        }
        return remaining == 2 ? "X" : String.valueOf(vi[remaining]);
    }

    //15 update to 18
    public static String uptoeighteen(String fifteencardid) {
        String eightcardid = fifteencardid.substring(0, 6);
        eightcardid = eightcardid + "19";
        eightcardid = eightcardid + fifteencardid.substring(6, 15);
        eightcardid = eightcardid + getVerify(eightcardid);
        return eightcardid;
    }

    /**
     * 判断两个身份证是否符合升位规律
     *
     * @return
     */
    public static boolean isMatch(String oldIdentityNo, String newIdentityNo) {
        if (StringUtils.isEmpty(oldIdentityNo) || StringUtils.isEmpty(newIdentityNo))
            return false;
        if (StringUtils.equalsIgnoreCase(oldIdentityNo, newIdentityNo))
            return true;
        if (oldIdentityNo.length() > newIdentityNo.length()) {
            return isMatch(newIdentityNo, oldIdentityNo);
        }
        if (oldIdentityNo.length() == 15
                && newIdentityNo.length() == 18
                && StringUtils.equalsIgnoreCase(uptoeighteen(oldIdentityNo).substring(0, 14),
                newIdentityNo.substring(0, 14))) {
            return true;
        }
        return false;
    }

    /**
     * 从身份证获取性别
     *
     * @param identityNo
     * @return 1：男，0：女
     */
    public static int getSex(String identityNo) {
        try {
            if (identityNo.length() == 15) {
                identityNo = uptoeighteen(identityNo);
            }
            return Integer.valueOf(identityNo.substring(16, 17)) % 2;
        } catch (Exception e) {
            log.warn(e, "身份证获取性别异常。cardNo:{}", identityNo);
        }
        return 1;
    }

    /**
     * 身份证验证出生日期
     */
    private static boolean verifyBirthDay(String idcard) {
        if (idcard.length() == 18) {
            String birthDay = idcard.substring(6, 14);
            long date = DateUtil.parse(birthDay, "yyyyMMdd").getDate();
            if (date != -1 && date < System.currentTimeMillis()) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        String idcard = "41272619901005053X";
        System.out.println(IdentityUtil.verify18(idcard));
        System.out.println(IdentityUtil.verifyBirthDay(idcard));
    }
}
