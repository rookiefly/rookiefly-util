package com.rookiefly.commons.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @create: 2019-02-20
 **/
public class AlgorithmUtil {

    public static void main(String[] args) {
        System.out.println(LCS_length("acbcbcef", "abcbced"));
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(longestCommonPrefix(new String[]{"leets", "leetcode", "leet", "leets"}));
        int[] arr = {5, 4, 3, 6, 7, 4, 9};
        quickSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

    /**
     * 最长公共子字符串
     * https://blog.csdn.net/u010397369/article/details/38979077
     *
     * @param s1
     * @param s2
     * @return
     */
    public static String LCS_length(String s1, String s2) {
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
     *
     * @return
     */
    public static String lengthOfLongestSubstring(String s) {
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

    /**
     * 数据最长公共前缀
     * https://blog.csdn.net/biezhihua/article/details/79859576
     *
     * @param strs
     * @return
     */
    public static String longestCommonPrefix(String[] strs) {
        if (strs == null || strs.length == 0) {
            return "";
        }
        for (int i = 0; i < strs[0].length(); i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (strs[j].length() == i || strs[j].charAt(i) != c) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    public static void quickSort(int[] arr) {
        qsort(arr, 0, arr.length - 1);
    }

    private static void qsort(int[] arr, int low, int high) {
        if (low < high) {
            int pivot = partition(arr, low, high);        //将数组分为两部分
            qsort(arr, low, pivot - 1);                   //递归排序左子数组
            qsort(arr, pivot + 1, high);                  //递归排序右子数组
        }
    }

    private static int partition(int[] arr, int low, int high) {
        int pivot = arr[low];     //枢轴记录
        while (low < high) {
            while (low < high && arr[high] >= pivot) --high;
            arr[low] = arr[high];             //交换比枢轴大的记录到左端
            while (low < high && arr[low] <= pivot) ++low;
            arr[high] = arr[low];           //交换比枢轴小的记录到右端
        }
        //扫描完成，枢轴到位
        arr[low] = pivot;
        //返回的是枢轴的位置
        return low;
    }

    public boolean checkInclusion(String s1, String s2) {
        int l1 = s1.length();
        int l2 = s2.length();
        int[] c1 = new int[26];
        int[] c2 = new int[26];
        for (char c : s1.toCharArray())
            c1[c - 'a']++;

        for (int i = 0; i < l2; i++) {
            if (i >= l1)
                --c2[s2.charAt(i - l1) - 'a'];//先把坐标查过的
            c2[s2.charAt(i) - 'a']++;
            if (Arrays.equals(c1, c2))
                return true;
        }
        return false;
    }
}