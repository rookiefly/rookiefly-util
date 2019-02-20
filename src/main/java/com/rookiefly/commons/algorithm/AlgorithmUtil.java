package com.rookiefly.commons.algorithm;

import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2019-02-20
 **/
public class AlgorithmUtil {

    public static void main(String[] args) {
        System.out.println(getLCSString("acbcbcef", "abcbced"));
        System.out.println(getLengthOfLongestSubstring("abcabcbb"));
    }

    /**
     * 最长公共子字符串
     * @param s1
     * @param s2
     * @return
     */
    public static String getLCSString(String s1, String s2) {
        char[] s1Array = s1.toCharArray();
        char[] s2Array = s2.toCharArray();
        int length = 0;
        int pos = 0;
        int[][] temp = new int[s1Array.length][s2Array.length];
        for (int i = 0; i < s1Array.length; i++) {
            for (int j = 0; j < s2Array.length; j++) {
                if (s1Array[i] == s2Array[j]) {
                    if (i > 0 && j > 0) {
                        temp[i][j] = temp[i - 1][j - 1] + 1;
                    } else {
                        temp[i][j] = 0;
                    }
                    if (temp[i][j] > length) {
                        length = temp[i][j];
                        pos = j;
                    }
                } else {
                    temp[i][j] = 0;
                }
            }
        }

        return s2.substring(pos - length + 1, pos + 1);
    }

    /**
     * 无重复字符的最长子串
     * @return
     */
    public static String getLengthOfLongestSubstring(String s){
        if (s.length() == 0 || s.length() == 1) {
            return s;
        }
        Map<Character, Integer> map = new HashMap<>();
        int pre = -1;
        int length = 0;
        int pos = -1;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            Integer index = map.get(ch);
            if (index != null) {
                pre = Math.max(pre, index);
            }
            map.put(ch, i);
            if (length < i - pre) {
                length = i - pre;
                pos = i;
            }
        }
        return s.substring(pos + 1 - length, pos + 1);
    }
}