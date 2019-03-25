package com.rookiefly.commons.algorithm;

import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;

/**
 * KMP算法参考 https://blog.csdn.net/v_july_v/article/details/7041827
 */
public class KMPSearch {

    public int indexOf(String source, String pattern) {
        int i = 0, j = 0;
        char[] src = source.toCharArray();
        char[] ptn = pattern.toCharArray();
        int sLen = src.length;
        int pLen = ptn.length;
        int[] next = getNext(ptn);
        while (i < sLen && j < pLen) {
            // 如果j = -1,或者当前字符匹配成功(src[i] = ptn[j]),都让i++,j++
            if (j == -1 || src[i] == ptn[j]) {
                i++;
                j++;
            } else {
                // 如果j!=-1且当前字符匹配失败,则令i不变,j=next[j],即让pattern模式串右移j-next[j]个单位
                j = next[j];
            }
        }
        if (j == pLen)
            return i - j;
        return -1;
    }

    public int[] getNext(char[] p) {
        // 已知next[j] = k,利用递归的思想求出next[j+1]的值
        // 如果已知next[j] = k,如何求出next[j+1]呢?具体算法如下:
        // 1. 如果p[j] = p[k], 则next[j+1] = next[k] + 1;
        // 2. 如果p[j] != p[k], 则令k=next[k],如果此时p[j]==p[k],则next[j+1]=k+1,
        // 如果不相等,则继续递归前缀索引,令 k=next[k],继续判断,直至k=-1(即k=next[0])或者p[j]=p[k]为止
        int pLen = p.length;
        int[] next = new int[pLen];
        int k = -1;
        int j = 0;
        next[0] = -1; // next数组中next[0]为-1
        while (j < pLen - 1) {
            if (k == -1 || p[j] == p[k]) {
                k++;
                j++;
                next[j] = k;
            } else {
                k = next[k];
            }
        }
        return next;
    }

    public static void main(String[] args) {
        String a = "QWERQWR";
        String b = "WWE QWERQW QWERQWERQWRT";
        KMPSearch kmp = new KMPSearch();
        int[] next = kmp.getNext(a.toCharArray());
        for (int i = 0; i < next.length; i++) {
            System.out.println(a.charAt(i) + "    " + next[i]);
        }
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        stopwatch.start();
        System.out.println(kmp.indexOf(b, a));
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.NANOSECONDS));

        /**
         * JDK String indexOf
         * JDK为什么不使用KMP https://stackoverflow.com/questions/19543547/why-does-string-indexof-not-use-kmp
         */
        stopwatch.reset();
        stopwatch.start();
        System.out.println(b.indexOf(a));
        stopwatch.stop();
        System.out.println(stopwatch.elapsed(TimeUnit.NANOSECONDS));
    }
}