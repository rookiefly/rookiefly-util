package com.rookiefly.test.commons.redis;

import com.rookiefly.commons.redis.RedisServiceImpl;
import org.junit.Test;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

public class RedisTest {

    @Test
    public void testJedisClient() {
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        //Jedis Cluster will attempt to discover cluster nodes automatically
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 7379));
        JedisCluster jc = new JedisCluster(jedisClusterNodes);
        jc.set("foo", "bar");
        String value = jc.get("foo");
    }

    @Test
    public void testGetSet() {
        RedisServiceImpl redisService = new RedisServiceImpl();
        redisService.setRedisAddr("127.0.0.1:19000");
        redisService.init();
        redisService.set("test", "hello");
        redisService.set("test2", "hello");
        redisService.set("test3", "hello");
        System.out.println(redisService.get("test"));
        System.out.println(redisService.get("test"));
        System.out.println(redisService.get("test1"));
    }
}
