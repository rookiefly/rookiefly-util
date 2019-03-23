package com.rookiefly.commons.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * LFUCache Cache
 *
 * @param <K> 键值
 * @param <V> value值
 */
public class LFUCache<K, V> {

    private final int capcity;

    private Map<K, V> cache = new HashMap<>();

    private Map<K, HitRate> count = new HashMap<>();

    public LFUCache(int capcity) {
        this.capcity = capcity;
    }

    public void put(K key, V value) {
        V v = cache.get(key);
        if (v == null) {
            if (cache.size() == capcity) {
                removeElement();
            }
            count.put(key, new HitRate(key, 1, System.nanoTime()));
        } else {
            addHitCount(key);
        }
        cache.put(key, value);
    }

    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            addHitCount(key);
            return value;
        }
        return null;
    }

    /**
     * 移除元素
     */
    private void removeElement() {
        HitRate hr = Collections.min(count.values());
        cache.remove(hr.key);
        count.remove(hr.key);
    }

    /**
     * 更新访问元素状态
     *
     * @param key
     */
    private void addHitCount(K key) {
        HitRate hitRate = count.get(key);
        hitRate.hitCount = hitRate.hitCount + 1;
        hitRate.lastTime = System.nanoTime();
    }

    /**
     * 命中率
     */
    class HitRate implements Comparable<HitRate> {
        private K key;
        private int hitCount;
        private long lastTime;

        private HitRate(K key, int hitCount, long lastTime) {
            this.key = key;
            this.hitCount = hitCount;
            this.lastTime = lastTime;
        }

        /**
         * 先比较命中次数，在比较更新时间
         * @param o
         * @return
         */
        @Override
        public int compareTo(HitRate o) {
            int compare = Integer.compare(this.hitCount, o.hitCount);
            return compare == 0 ? Long.compare(this.lastTime, o.lastTime) : compare;
        }
    }


    public static void main(String[] args) {
        LFUCache<Integer, Integer> cache = new LFUCache<>(3);
        cache.put(2, 2);
        cache.put(1, 1);

        System.out.println(cache.get(2));
        System.out.println(cache.get(1));
        System.out.println(cache.get(2));

        cache.put(3, 3);
        cache.put(4, 4);

        //1、2元素都有访问次数，放入3后缓存满，加入4时淘汰3
        System.out.println(cache.get(3));
        System.out.println(cache.get(2));
        //System.out.println(cache.get(1));
        System.out.println(cache.get(4));

        cache.put(5, 5);
        //目前2访问2次，1访问一次，4访问一次，由于4的时间比较新，放入5的时候移除1元素。
        System.out.println("现有缓存数据：");
        cache.cache.entrySet().forEach(entry -> {
            System.out.println(entry.getValue());
        });

    }
}