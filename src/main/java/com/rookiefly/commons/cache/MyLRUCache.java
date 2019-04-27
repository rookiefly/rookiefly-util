package com.rookiefly.commons.cache;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
public class MyLRUCache<K, V> {

    public static void main(String[] args) {
        MyLRUCache<Integer, Integer> cache = new MyLRUCache<>(2 /* 缓存容量 */);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        System.out.println(cache.get(2));       // 返回 null (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        System.out.println(cache.get(1));       // 返回 null (未找到)
        System.out.println(cache.get(3));       // 返回  3
        System.out.println(cache.get(4));       // 返回  4
    }

    private LinkedHashMap<K, V> cache;

    private final int capacity;

    public MyLRUCache(int capacity) {
        this.capacity = capacity;
        cache = new LinkedHashMap<K, V>(16, 0.75f, true) {

            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                return size() > MyLRUCache.this.capacity;
            }
        };
    }

    public V get(K key) {
        V value = cache.get(key);
        return value;
    }

    public void put(K key, V value) {
        cache.put(key, value);
    }
}