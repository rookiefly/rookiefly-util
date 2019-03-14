package com.rookiefly.commons.collection;

import com.github.jsonzou.jmockdata.JMockData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TopK<T> {

    /**
     * 堆的边界，Top K 问题中的 K
     */
    private final int boundary;


    /**
     * 优先队列，用来构造一个有界的堆
     */
    private final PriorityQueue<T> boundaryHeap;


    /**
     * 通过自定义边界 boundary 可以求解 top K 问题
     * 通过自定义比较器 comparator 可以控制求解 top K 大 还是 top K 小
     *
     * @param boundary   边界 K
     * @param comparator 数据比较器
     */
    public TopK(int boundary, Comparator<T> comparator) {
        this.boundary = boundary;
        boundaryHeap = new PriorityQueue<>(boundary, comparator);
    }

    /**
     * 通过自定义边界 boundary 可以求解 top K 问题
     * 对象实现Comparable接口
     *
     * @param boundary 边界 K
     */
    public TopK(int boundary) {
        this.boundary = boundary;
        boundaryHeap = new PriorityQueue<>(boundary);
    }

    /**
     * 求解数据流中的top K， 将结果写入List中
     *
     * @param dataStream 数据流
     * @param results    top K 结果
     */
    public void topK(Stream<T> dataStream, List<T> results) {
        dataStream.forEach(this::add);

        while (!boundaryHeap.isEmpty()) {
            results.add(boundaryHeap.poll());
        }
    }


    /**
     * 向有界堆中添加元素的帮助方法
     *
     * @param t 待添加数据
     */
    public void add(T t) {
        boundaryHeap.add(t);
        if (boundaryHeap.size() > boundary) {
            boundaryHeap.poll();
        }
    }

    private static class PersonSupplier implements Supplier<UserActivity> {
        @Override
        public UserActivity get() {
            return JMockData.mock(UserActivity.class);
        }
    }

    public static void main(String[] args) {
        TopK<UserActivity> userActivityTopK = new TopK<>(3);

        Stream<UserActivity> stream = Stream.generate(new PersonSupplier()).limit(10000);

        //stream.forEach(System.out::println);

        ArrayList<UserActivity> results = new ArrayList<>();
        userActivityTopK.topK(stream, results);
        for (UserActivity result : results) {
            System.out.println(result);
        }
    }
}
