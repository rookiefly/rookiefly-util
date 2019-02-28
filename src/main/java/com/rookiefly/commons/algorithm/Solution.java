package com.rookiefly.commons.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * @create: 2019-02-20
 **/
public class Solution {

    public static void main(String[] args) {
        System.out.println(LCS_length("acbcbcef", "abcbced"));
        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring2("abcabcbb"));
        System.out.println(longestCommonPrefix(new String[]{"leets", "leetcode", "leet", "leets"}));
        int[] arr = {5, 4, 3, 6, 7, 4, 9};
        quickSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        int[][] test = new int[][]{
                {1, 1, 2, 2, 3, 4, 5},
                {1, 1, 1, 1, 1, 1, 1},
                {1, 2, 3, 4, 5, 6, 7},
                {1, 2, 1, 1, 1, 1, 1},};

        for (int[] input : test) {
            System.out.println("Array with Duplicates       : " + Arrays.toString(input));
            System.out.println("After removing duplicates   : " + Arrays.toString(removeDuplicates2(input)));
        }

        System.out.println(restoreIpAddresses2("25525511135"));
        System.out.println(sqrt2(4));
        System.out.println(sqrt2(8));

        boolean isBlack = filterIp("10.168.1.2", "10.168.0.224/23");
        if (isBlack) {
            System.out.println("是黑名单");
        } else {
            System.out.println("不是黑名单");
        }

        System.out.println(threeSumClosest(new int[]{-1, 2, 1, -4}, 1));
        System.out.println(reverseInt(453));
        System.out.println(isPalindrome(-121));
        System.out.println(longestPalindrome("abaaba"));

        ListNode head1 = new ListNode(1);
        head1.next = new ListNode(3);
        head1.next.next = new ListNode(5);

        ListNode head2 = new ListNode(2);
        head2.next = new ListNode(5);
        head2.next.next = new ListNode(6);
        head2.next.next.next = new ListNode(7);

        printCommonPart(head1, head2);
        System.out.println(findMid(head2));
        printListNode(addTwoLists(head1, head2));
        printListNode(mergeTwoLists(head1, head2));
        //printListNode(mergeTwoLists2(head1, head2));
        System.out.println(isPalindrome1(head1));
    }

    public static class ListNode {
        public int val;
        public ListNode next;

        public ListNode(int val) {
            this.val = val;
        }

        public int val() {
            return this.val;
        }
    }

    public static void printListNode(ListNode node) {
        System.out.print("Linked List: ");
        while (node != null) {
            System.out.print(node.val + " ");
            node = node.next;
        }
        System.out.println();
    }

    public static class TreeNode {

        private int value;

        private TreeNode left;

        private TreeNode right;

        public TreeNode(int value) {
            this.value = value;
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
     * 无重复字符的最长子串
     *
     * @return
     */
    public static int lengthOfLongestSubstring2(String s) {
        int ans = 0;
        int pre = 0;
        int i = 0;
        Set<Character> set = new HashSet<>();
        while (i < s.length() && pre < s.length()) {
            if (!set.contains(s.charAt(i))) {
                set.add(s.charAt(i++));
                ans = Math.max(ans, i - pre);
            } else {
                set.remove(s.charAt(pre++));
            }
        }
        return ans;
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

    /**
     * 字符串中的全排列
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean checkInclusion(String s1, String s2) {
        if (s1 == null || s2 == null || s1.length() > s2.length()) {
            return false;
        }
        int[] count1 = new int[26]; // s1每个字符出现的次数
        int[] count2 = new int[26]; // s2每个字符出现的次数
        // 1. 进行统计
        for (int i = 0; i < s1.length(); i++) {
            count1[s1.charAt(i) - 'a']++;
            count2[s2.charAt(i) - 'a']++;
        }
        // 2. 滑动窗口，滑块长度始终为 s1.length()
        for (int i = s1.length(); i < s2.length(); i++) {
            if (isSame(count1, count2)) {
                return true;
            }
            count2[s2.charAt(i - s1.length()) - 'a']--; // 去掉滑块当前的首个字符
            count2[s2.charAt(i) - 'a']++; // 添加最新的字符到滑块中
        }
        return isSame(count1, count2);
    }

    // 有且仅当 count1 中所有值都等于 count2 中对应值时满足条件
    public static boolean isSame(int[] count1, int[] count2) {
        for (int i = 0; i < count1.length; i++) {
            if (count1[i] != count2[i]) {
                return false;
            }
        }
        return true;
    }

    public static List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<String>();
        helper(s, 0, "", res);
        return res;
    }

    public static void helper(String s, int n, String out, List<String> res) {
        if (n == 4) {
            if (s.isEmpty()) res.add(out);
            return;
        }
        for (int k = 1; k < 4; ++k) {
            if (s.length() < k) break;
            int val = Integer.parseInt(s.substring(0, k));
            if (val > 255 || k != String.valueOf(val).length()) continue;
            helper(s.substring(k), n + 1, out + s.substring(0, k) + (n == 3 ? "" : "."), res);
        }
    }

    public static int[] removeDuplicates(int[] numbersWithDuplicates) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < numbersWithDuplicates.length; i++) {
            if (map.get(numbersWithDuplicates[i]) != null && map.get(numbersWithDuplicates[i]) > 0) {
                map.put(numbersWithDuplicates[i], map.get(numbersWithDuplicates[i]) + 1);
                numbersWithDuplicates[i] = 0;
            } else {
                map.put(numbersWithDuplicates[i], 1);
            }
        }
        return numbersWithDuplicates;
    }

    public static int[] removeDuplicates2(int[] numbersWithDuplicates) {
        Arrays.sort(numbersWithDuplicates);

        int[] result = new int[numbersWithDuplicates.length];
        int pre = numbersWithDuplicates[0];
        result[0] = numbersWithDuplicates[0];

        for (int i = 1; i < numbersWithDuplicates.length; i++) {
            if (pre != numbersWithDuplicates[i]) {
                result[i] = numbersWithDuplicates[i];
            }
            pre = numbersWithDuplicates[i];
        }
        return result;
    }

    public static List<String> restoreIpAddresses2(String s) {
        List<String> res = new ArrayList<String>();
        for (int a = 1; a < 4; ++a)
            for (int b = 1; b < 4; ++b)
                for (int c = 1; c < 4; ++c)
                    for (int d = 1; d < 4; ++d)
                        if (a + b + c + d == s.length()) {
                            int A = Integer.parseInt(s.substring(0, a));
                            int B = Integer.parseInt(s.substring(a, a + b));
                            int C = Integer.parseInt(s.substring(a + b, a + b + c));
                            int D = Integer.parseInt(s.substring(a + b + c));
                            if (A <= 255 && B <= 255 && C <= 255 && D <= 255) {
                                String t = String.valueOf(A) + "." + String.valueOf(B) + "." + String.valueOf(C) + "." + String.valueOf(D);
                                if (t.length() == s.length() + 3) res.add(t);
                            }
                        }
        return res;
    }

    /**
     * @param numbers : Give an array numbers of n integer
     * @return : Find all unique triplets in the array which gives the sum of zero.
     */
    public ArrayList<ArrayList<Integer>> threeSum(int[] numbers) {

        ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
        if (numbers == null || numbers.length < 3)
            return result;
        Arrays.sort(numbers);
        for (int i = 0; i < numbers.length; i++) {
            int left = i + 1;
            int right = numbers.length - 1;
            while (left < right) {
                int sum = numbers[i] + numbers[left] + numbers[right];
                ArrayList<Integer> path = new ArrayList<Integer>();
                if (sum == 0) {
                    path.add(numbers[i]);
                    path.add(numbers[left]);
                    path.add(numbers[right]);
                    if (result.contains(path) == false)
                        result.add(path);
                    left++;
                    right--;
                } else if (sum > 0) {
                    right--;
                } else {
                    left++;
                }
            }
        }

        return result;
    }

    public static int sqrt(int x) {
        int r = x;
        while (r * r > x) {
            r = (r + x / r) / 2;
        }
        return r;
    }

    public static int sqrt2(int x) {
        if (x == 0) {
            return 0;
        }
        int left = 1, right = x, ans = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (mid <= x / mid) {
                ans = mid;
                left = left + 1;
            } else {
                right = mid - 1;
            }
        }
        return ans;
    }

    public static ListNode addTwoLists(ListNode l1, ListNode l2) {
        if (l1 == null)
            return l2;
        if (l2 == null)
            return l1;

        int carry = 0;
        ListNode newhead = new ListNode(-1);
        ListNode l3 = newhead;

        while (l1 != null || l2 != null) {
            if (l1 != null) {
                carry += l1.val;
                l1 = l1.next;
            }
            if (l2 != null) {
                carry += l2.val;
                l2 = l2.next;
            }

            l3.next = new ListNode(carry % 10);
            carry = carry / 10;
            l3 = l3.next;
        }

        if (carry == 1)
            l3.next = new ListNode(1);
        return newhead.next;
    }

    /**
     * @param network 黑名单网段
     * @param maskIp  扫描ip
     * @return
     */
    public static boolean filterIp(String network, String maskIp) {
        //首先将网段转换为10进制数
        String[] networks = network.split("\\.");
        long networkIp = Integer.parseInt(networks[0]) << 24 |
                Integer.parseInt(networks[1]) << 16 |
                Integer.parseInt(networks[2]) << 8 |
                Integer.parseInt(networks[3]);

        //取出网络位数
        int netCount = Integer.parseInt(maskIp.replaceAll(".*/", ""));
        //这里实际上通过CIDR的网络号转换为子网掩码
        int mask = 0xFFFFFFFF << (32 - netCount);

        //再将验证的ip转换为10进制数
        String testIp = maskIp.replaceAll("/.*", "");
        String[] ips = testIp.split("\\.");
        long ip = Integer.parseInt(ips[0]) << 24 |
                Integer.parseInt(ips[1]) << 16 |
                Integer.parseInt(ips[2]) << 8 |
                Integer.parseInt(ips[3]);

        //将网段ip和验证ip分别和子网号进行&运算之后，得到的是网络号，如果相同，说明是同一个网段的
        return (networkIp & mask) == (ip & mask);
    }

    public static int threeSumClosest(int[] nums, int target) {
        if (nums.length == 2) {
            return nums[0] + nums[1];
        }
        int sum = nums[0] + nums[1] + nums[2];
        int n = nums.length;
        for (int i = 0; i < n - 2; i++) {
            int left = i + 1;
            int right = n - 1;
            while (left < right) {
                int tempSum = nums[i] + nums[left] + nums[right];
                if (Math.abs(target - tempSum) < Math.abs(target - sum)) {
                    sum = tempSum;
                    if (sum == target) {
                        return sum;
                    }
                } else if (tempSum > target) {
                    right--;
                } else {
                    left++;
                }

            }

        }
        return sum;
    }

    public static int reverseInt(int x) {
        int num = 0;
        while (x != 0) {
            num = num * 10 + (x % 10);
            x = x / 10;
        }

        if (x < Integer.MIN_VALUE || x > Integer.MAX_VALUE) {
            return 0;
        }

        return num;
    }

    /**
     * 回文整数判断
     *
     * @param x
     * @return
     */
    public static boolean isPalindrome(int x) {
        if (x == 0)
            return true;
        if (x < 0)
            return false;
        int rev = 0;
        int temp = x;
        while (temp != 0) {
            rev = rev * 10 + temp % 10;
            temp = temp / 10;
        }
        return rev == x;
    }

    /**
     * 最长回文字符串
     *
     * @param s
     * @return
     */
    public static int longestPalindrome(String s) {
        int length = s.length();
        if (length == 1) {
            return 1;
        }
        boolean[][] pal = new boolean[length][length];
        int maxLength = 0;
        for (int i = 0; i < length; i++) {
            int j = i;
            while (j >= 0) {
                if (s.charAt(i) == s.charAt(j) && (i - j < 2 || pal[j + 1][i - 1])) {
                    pal[j][i] = true;
                    maxLength = Math.max(maxLength, i - j + 1);
                }
                j--;
            }
        }
        return maxLength;
    }

    public static int removeElement(int[] a, int t) {

        int i = 0;
        for (int j = 0; j < a.length; j++) {
            if (a[j] != t) {
                a[i++] = a[j];
            }
        }
        return i;
    }

    /**
     * 合并有序链表，非递归
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode newHead = new ListNode(0);
        ListNode l3 = newHead;
        while (l1 != null && l2 != null) {
            if (l1.val > l2.val) {
                l3.next = l2;
                l2 = l2.next;
            } else {
                l3.next = l1;
                l1 = l1.next;
            }
            l3 = l3.next;
        }

        if (l1 != null) {
            l3.next = l1;
        } else if (l2 != null) {
            l3.next = l2;
        }
        return newHead.next;
    }

    /**
     * 合并有序链表，递归
     *
     * @param l1
     * @param l2
     * @return
     */
    public static ListNode mergeTwoLists2(ListNode l1, ListNode l2) {

        if (l1 == null)
            return l2;
        if (l2 == null)
            return l1;
        if (l1.val < l2.val) {
            l1.next = mergeTwoLists2(l1.next, l2);
            return l1;
        } else {
            l2.next = mergeTwoLists2(l1, l2.next);
            return l2;
        }
    }

    public static int findMid(ListNode l1) {
        ListNode fast = l1;
        ListNode slow = l1;
        while (fast != null && fast.next != null) {
            fast = fast.next.next;
            slow = slow.next;
        }
        return slow.val;
    }

    public static void printCommonPart(ListNode head1, ListNode head2) {
        System.out.print("Common Part: ");
        while (head1 != null && head2 != null) {
            if (head1.val < head2.val) {
                head1 = head1.next;
            } else if (head1.val > head2.val) {
                head2 = head2.next;
            } else {
                System.out.print(head1.val + " ");
                head1 = head1.next;
                head2 = head2.next;
            }
        }
        System.out.println();
    }

    public static boolean isPalindrome1(ListNode node) {
        if (node == null)
            return false;
        Stack<ListNode> stack = new Stack();
        ListNode cur = node;
        while (cur != null) {
            stack.push(cur);
            cur = cur.next;
        }
        while (node != null) {
            if (node.val != stack.pop().val) {
                return false;
            }
        }
        return true;
    }

    // need O(1) extra space
    public static boolean isPalindrome3(ListNode head) {
        if (head == null || head.next == null) {
            return true;
        }
        ListNode n1 = head;
        ListNode n2 = head;
        while (n2.next != null && n2.next.next != null) { // find mid node
            n1 = n1.next; // n1 -> mid
            n2 = n2.next.next; // n2 -> end
        }
        n2 = n1.next; // n2 -> right part first node
        n1.next = null; // mid.next -> null
        ListNode n3 = null;
        while (n2 != null) { // right part convert
            n3 = n2.next; // n3 -> save next node 操作前先保存下一个节点
            n2.next = n1; // next of right node convert 反转指向
            n1 = n2; // n1 move 保存当前节点，作为后一个的前节点
            n2 = n3; // n2 move 向前推进
        }
        n3 = n1; // n3 -> save last node
        n2 = head;// n2 -> left first node
        boolean res = true;
        while (n1 != null && n2 != null) { // check palindrome
            if (n1.val != n2.val) {
                res = false;
                break;
            }
            n1 = n1.next; // left to mid
            n2 = n2.next; // right to mid
        }
        n1 = n3.next;
        n3.next = null;
        while (n1 != null) { // recover list
            n2 = n1.next;
            n1.next = n3;
            n3 = n1;
            n1 = n2;
        }
        return res;
    }
}