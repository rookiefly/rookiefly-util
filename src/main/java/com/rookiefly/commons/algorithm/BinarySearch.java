package com.rookiefly.commons.algorithm;

import java.util.Arrays;

/**
 * 插入数据到有序数组，保证数组有序
 */
public class BinarySearch {

    public static int binarySearch(int[] arr, int target) {
        if (arr != null) {
            int min, mid, max;
            min = 0; // the minimum index
            max = arr.length - 1; // the maximum index
            while (min <= max) {
                mid = (min + max) / 2; // the middle index
                if (arr[mid] < target) {
                    min = mid + 1;
                } else if (arr[mid] > target) {
                    max = mid - 1;
                } else {
                    return mid;
                }
            }
            return min;
        }
        return -1;
    }

    public static int[] insertOrderArray(int[] arr, int target) {
        int index = binarySearch(arr, target);
        if (index != -1) {
            int[] newArr = Arrays.copyOf(arr, arr.length + 1);
            System.arraycopy(arr, index, newArr, index + 1, arr.length - index);
            newArr[index] = target;
            return newArr;
        }
        return null;
    }

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 5, 6, 7, 8, 9, 23, 34};
        System.out.println(binarySearch(arr, 0));
        System.out.println(binarySearch(arr, 4));
        System.out.println(binarySearch(arr, 23));
        System.out.println(binarySearch(arr, 35));
        int[] insertOrderArray = insertOrderArray(arr, 21);
        for (int i = 0; i < insertOrderArray.length; i++) {
            System.out.print(insertOrderArray[i] + " ");
        }
    }
}