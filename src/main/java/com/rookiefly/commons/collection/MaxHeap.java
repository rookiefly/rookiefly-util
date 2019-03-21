package com.rookiefly.commons.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * 大顶堆
 */
public class MaxHeap<T extends Comparable<T>> {
    private List<T> mHeap; // 存放元素的动态数组

    public MaxHeap() {

        this.mHeap = new ArrayList<>();
    }

    /**
     * 大顶堆的向上调整算法(添加节点的时候调用) 注：数组实现的堆中，第N个节点的左孩子的索引值是(2N+1)，右孩子的索引是(2N+2)。
     *
     * @param start -- 被上调节点的起始位置(一般为数组中最后一个元素的索引)
     */
    protected void filterup(int start) {

        int c = start; // 需要调整的节点的初始位置
        int p = (c - 1) / 2; // 当前节点的父节点的位置
        T tmp = mHeap.get(c); // 被调整节点的值

        while (c > 0) {
            // 父节点的值和被调整节点的值进行比较
            int cmp = mHeap.get(p).compareTo(tmp);
            if (cmp >= 0) {
                // 父节点大
                break;
            } else {
                // 被调整节点的值大，交换
                mHeap.set(c, mHeap.get(p));
                c = p;
                p = (c - 1) / 2;
            }
        }
        // 找到被调整节点的最终位置了
        mHeap.set(c, tmp);
    }

    /**
     * 大顶堆的向下调整算法(删除节点的时候需要调用来调整大顶堆)
     * 注：数组实现的堆中，第N个节点的左孩子的索引值是(2N+1)，右孩子的索引是(2N+2)。
     *
     * @param start -- 被下调节点的起始位置(一般为0，表示从第1个开始)
     * @param end   -- 截至范围(一般为数组中最后一个元素的索引)
     */
    protected void filterdown(int start, int end) {

        int c = start; // 被下调节点的初始位置
        int l = 2 * c + 1; // 左孩子节点的位置
        T tmp = mHeap.get(c); // 当前节点的值(大小)

        while (l <= end) {
            // 当前节点的左右节点进行比较
            int cmp = mHeap.get(l).compareTo(mHeap.get(l + 1));
            // 取大的
            if (l < end && cmp < 0) {
                l++;
            }
            // 当前节点和大的那个再比较一下
            cmp = tmp.compareTo(mHeap.get(l));
            if (cmp >= 0) {
                // 当前节点大,不用动
                break;
            } else {
                // 当前节点小,交换
                mHeap.set(c, mHeap.get(l));
                c = l; // 更新当前节点的位置
                l = 2 * c + 1; // 更新当前节点的左孩子位置
            }
        }
        mHeap.set(c, tmp);
    }

    /**
     * 向大顶堆中插入新元素
     *
     * @param data
     */
    public void insert(T data) {

        int insertIndex = mHeap.size(); // 获取插入的位置
        // 将新元素插入到数组尾部
        mHeap.add(data);
        // 调用filterup函数，调整大顶堆
        filterup(insertIndex);
    }

    /**
     * 删除大顶堆中的data节点
     *
     * @param data
     * @return 返回-1表示出错, 返回0表示删除成功
     */
    public int remove(T data) {

        // 大顶堆空
        if (mHeap.isEmpty()) {
            return -1;
        }

        // 获取data在数组中的索引
        int index = mHeap.indexOf(data);
        if (index == -1) {
            return -1;
        }

        // 堆中元素的个数
        int size = mHeap.size();
        // 删除了data元素，需要用最后一个元素填补，然后调用filterdown算法进行调整
        mHeap.set(index, mHeap.get(size - 1)); // 用最后一个元素填补
        mHeap.remove(size - 1); // 删除最后一个元素

        if (mHeap.size() > 1 && index < mHeap.size()) {
            // 调整成大顶堆
            filterdown(index, mHeap.size() - 1);
        }
        return 0;
    }


    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mHeap.size(); i++) {
            sb.append(mHeap.get(i) + " ");
        }
        return sb.toString();
    }

    public static void main(String[] args) {

        int a[] = {10, 40, 30, 60, 90, 70, 20, 50, 80};

        //大顶堆
        MaxHeap<Integer> maxHeap = new MaxHeap<>();

        //添加元素
        System.out.println("=== 依次添加元素：");
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
            maxHeap.insert(a[i]);
        }

        //生成的大顶堆
        System.out.println("=== 生成的大顶堆：");
        System.out.println(maxHeap);

        //添加新元素85
        int data = 85;
        maxHeap.insert(data);
        System.out.println("=== 添加新元素" + data + "之后的大顶堆：");
        System.out.println(maxHeap);

        //删除元素90
        data = 90;
        maxHeap.remove(data);
        System.out.println("=== 删除元素" + data + "之后的大顶堆：");
        System.out.println(maxHeap);

    }
}