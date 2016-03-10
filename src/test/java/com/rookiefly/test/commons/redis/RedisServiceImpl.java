package com.rookiefly.test.commons.redis;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.Pool;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class RedisServiceImpl implements RedisService {
    private static final Logger logger = Logger.getLogger(RedisServiceImpl.class);
    // redis lua scripts(supportted since redis 2.6)
    private static final String CAS_CMD = "local v=redis.call('get',KEYS[1]);local r=v;local n=#ARGV-1;local tb=ARGV[#ARGV];local succ='F';for i=1, n do if(v==ARGV[i]) then redis.call('set',KEYS[1],tb); r=tb; succ='T' break;end end;return {succ,r}";
    private static final String SUBS_CMD = "local v=redis.call('incrby',KEYS[1],0);local r=ARGV[1]-v;redis.call('set',KEYS[1],ARGV[1]);return r;";
    private volatile String CAS_KEY;
    private volatile String SUBS_KEY;
    private String redisAddr;
    private String[] addrInfo;
    private Pool<Jedis> jedisPool;
    private String sentinels;
    private long redisSubMaintainInterval = 10000;
    private int maxIdle = 5;

    public void setRedisAddr(String redisAddr) {
        this.redisAddr = redisAddr;
    }

    public void setRedisSubMaintainInterval(long redisSubMaintainInterval) {
        this.redisSubMaintainInterval = redisSubMaintainInterval;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setSentinels(String sentinels) {
        this.sentinels = sentinels;
    }

    public void init() {
        if (redisAddr == null || "".equals(redisAddr.trim()) || "NULL".equals(redisAddr.trim())) {
            logger.warn("no redis addr was configured,this redis service will be unavaliable");
            return;
        }
        JedisPoolConfig cfg = new JedisPoolConfig();
        cfg.setMaxIdle(maxIdle);
        if (sentinels != null) {
            // addr info is a name rather than ip:port
            String[] sentinelsInfo = sentinels.split(",");
            Set<String> sentinelSet = new HashSet<String>();
            for (String s : sentinelsInfo) {
                sentinelSet.add(s.trim());
            }
            jedisPool = new JedisSentinelPool(redisAddr, sentinelSet, cfg);
        } else {
            addrInfo = redisAddr.split(":");
            jedisPool = new JedisPool(cfg, addrInfo[0], Integer.valueOf(addrInfo[1]));
        }
        // load cas scripts
        Jedis jedis = jedisPool.getResource();
        try {
            CAS_KEY = jedis.scriptLoad(CAS_CMD);
            SUBS_KEY = jedis.scriptLoad(SUBS_CMD);
        } finally {
            jedisPool.returnResource(jedis);
        }
    }

    public void stop() {
        if (jedisPool != null) {
            jedisPool.destroy();
        }
    }

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.set(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 以当前时间的毫秒数位score
     */
    @Override
    public void addSortedSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.zadd(key, System.currentTimeMillis(), value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String set(String key, String value, int expireSeconds) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.setex(key, expireSeconds, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long addToSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.sadd(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long removeFormSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.srem(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long addToSet(String key, String... value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.sadd(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public int getSetCount(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.scard(key).intValue();
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean setIfNotExist(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.setnx(key, value);
            return result > 0;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public List<String> gets(String... keys) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.mget(keys);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.exists(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Double getSetScore(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zscore(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public int del(String... keys) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.del(keys);
            return (int) result;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.expire(key, seconds);
            return result > 0;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean expireAt(String key, long unixTime) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.expireAt(key, unixTime);
            return result > 0;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String getSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.getSet(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Set<String> getSetMembers(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.smembers(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String getRandomMember(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.srandmember(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.incr(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long incrBy(String key, int increment) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.incrBy(key, increment);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Double incrByFloat(String key, double increment) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.incrByFloat(key, increment);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long decr(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.decr(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long decrBy(String key, int decrement) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.decrBy(key, decrement);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public int hset(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        int result = 0;
        boolean broken = false;
        try {
            result = jedis.hset(key, field, value).intValue();
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return result;
    }

    @Override
    public CASResult<String> cas(String key, Set<String> expectingOriginVals, String toBe) {
        List<String> args = new ArrayList<String>(expectingOriginVals.size() + 2);
        args.add(key);
        args.addAll(expectingOriginVals);
        args.add(toBe);
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            @SuppressWarnings("unchecked")
            List<String> response = (List<String>) jedis.evalsha(CAS_KEY, 1, args.toArray(new String[0]));
            String succStr = response.get(0);
            String val = response.get(1);
            CASResult<String> result = new CASResult<String>("T".equals(succStr), val);
            return result;
        } catch (JedisDataException e) {
            // redis实例重启
            CAS_KEY = jedis.scriptLoad(CAS_CMD);
            throw e;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long substractedAndSet(long minuend, String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            Long response = (Long) jedis.evalsha(SUBS_KEY, 1, key, String.valueOf(minuend));
            return response;
        } catch (JedisDataException e) {
            // redis实例恢复
            SUBS_KEY = jedis.scriptLoad(SUBS_CMD);
            throw e;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void subscribe(final JedisPubSub jedisPubSub, final String... channels) {
        new RedisSubscribeTask(jedisPubSub, channels).subscribe();
    }

    @Override
    public void publish(String channel, String message) {
        String msg = JSON.toJSONString(new PublishMessage(message));
        if (logger.isDebugEnabled()) {
            logger.debug("Message have been published channel is " + channel + ",message is " + msg);
        }

        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.publish(channel, msg);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void originalPublish(String channel, String message) {
        if (logger.isDebugEnabled()) {
            logger.debug("Message have been published channel is " + channel + ",message is " + message);
        }
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.publish(channel, message);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    /**
     * 订阅reids通道的task,负责执行redis通道的订阅,并在必要(断线)时重新订阅
     *
     * @author kyo.ou 2013-7-18
     */
    private class RedisSubscribeTask {
        private JedisPubSub pubSub;
        private final String[] channels;
        private volatile AtomicBoolean reSubFail = new AtomicBoolean(true);
        private volatile Jedis jedis;
        private final String channelStr;

        public RedisSubscribeTask(JedisPubSub pubSub, String[] channels) {
            this.pubSub = pubSub;
            this.channels = channels;
            StringBuilder builder = new StringBuilder();
            for (String channel : channels) {
                builder.append(channel).append(' ');
            }
            channelStr = builder.toString();
        }

        public void subscribe() {
            doSubScribe(true);
        }

        private void doSubScribe(boolean firstTime) {
            if (channels == null || channels.length == 0) {
                return;
            }
            jedis = new Jedis(addrInfo[0], Integer.valueOf(addrInfo[1]));
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (reSubFail.compareAndSet(true, false) == false)
                            ;
                        jedis.subscribe(pubSub, channels);
                    } catch (Throwable e) {
                        reSubFail.set(true);
                        logger.error("redis subscribe " + channelStr + "@" + redisAddr
                                + " has failed will try to resubscribe after " + redisSubMaintainInterval
                                + " millis", e);
                    }
                }
            }, "RedisSubPub-" + channelStr);
            t1.start();
            if (firstTime) {// 启动维护线程
                Thread t2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            boolean metException = false;
                            try {
                                publish("RedisServiceImpl:test", "test");
                            } catch (Throwable e) {
                                logger.warn("Redis pubsub has broken:" + e.getMessage());
                                metException = true;
                            }
                            if (metException || reSubFail.get() == true) {
                                try {
                                    jedis.disconnect();
                                } catch (Throwable e2) {
                                    logger.warn("fail to close old jedis subscribe", e2);
                                }
                                doSubScribe(false);
                            }
                            try {
                                Thread.sleep(redisSubMaintainInterval);
                            } catch (InterruptedException e) {
                                // impossiable
                            }
                        }

                    }
                }, "RediSubPubMaintain-" + channelStr);
                t2.start();
            }
        }
    }

    @Override
    public void hsets(String key, Map<String, String> fields) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.hmset(key, fields);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean hsetIfNotExists(String key, String field, String value) {
        Jedis jedis = jedisPool.getResource();
        long result = 0;
        boolean broken = false;
        try {
            result = jedis.hsetnx(key, field, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return result > 0;
    }

    @Override
    public String hget(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        String val = null;
        boolean broken = false;
        try {
            val = jedis.hget(key, field);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public List<String> hgets(String key, String... fields) {
        Jedis jedis = jedisPool.getResource();
        List<String> val = null;
        boolean broken = false;
        try {
            val = jedis.hmget(key, fields);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String, String> val = null;
        boolean broken = false;
        try {
            val = jedis.hgetAll(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public void hdel(String key, String field) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.hdel(key, field);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public void hdel(String key, String... fields) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            jedis.hdel(key, fields);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String type(String key) {
        Jedis jedis = jedisPool.getResource();
        String val = null;
        boolean broken = false;
        try {
            val = jedis.type(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;

    }

    @Override
    public Set<String> hkeys(String key) {
        Jedis jedis = jedisPool.getResource();
        Set<String> val = null;
        boolean broken = false;
        try {
            val = jedis.hkeys(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        Jedis jedis = jedisPool.getResource();
        String val = null;
        boolean broken = false;
        try {
            val = jedis.hmset(key, hash);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        long val = -99;
        boolean broken = false;
        try {
            val = jedis.ttl(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
        return val;
    }

    @Override
    public Long zcount(String key, double min, double max) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zcount(key, min, max);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long zadd(String key, String member, double score) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zadd(key, score, member);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long zadd(String key, Map<String, Double> members) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zadd(key, members);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zrange(key, start, end);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Set<String> zrangeAndDel(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            Transaction tx = jedis.multi();
            tx.zrange(key, start, end);
            tx.del(key);
            List<Object> resultList = tx.exec();
            return (Set<String>) resultList.get(0);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Set<String> zreverage(String key, long start, long end) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zrevrange(key, start, end);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long zrem(String key, String... member) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zrem(key, member);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long zRemrangeByScore(String key, double min, double max) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zremrangeByScore(key, min, max);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String toString() {
        return "redis addr :" + redisAddr;
    }

    @Override
    public long hincrBy(String key, String member) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.hincrBy(key, member, 1);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long hincrBy(String key, String member, int dlt) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.hincrBy(key, member, dlt);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long zcard(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zcard(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean setIfAbsent(String key, String value, int expireMilliSeconds) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            String result = jedis.set(key, value, "nx", "px", expireMilliSeconds);
            if (result != null && result.equalsIgnoreCase("OK")) {
                return true;
            }
            return false;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public long removeSortedSet(String key, String... members) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.zrem(key, members);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public String getRedisAddr() {
        return redisAddr;
    }

    @Override
    public long llen(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.llen(key);

        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public List<String> lrange(String key, final long start, final long end) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.lrange(key, start, end);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String rpop(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.rpop(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long lpush(String key, String string) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.lpush(key, string);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String lpop(String key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.lpop(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long rpush(String key, String string) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.rpush(key, string);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean isMember(String key, String value) {

        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.sismember(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Long addSet(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.sadd(key, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public Set<String> keys(String pattern) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.keys(pattern);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }

    }

    @Override
    public int batchLpush(String key, List<String> valueList) {
        if (key == null || valueList == null)
            return 0;

        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            Pipeline p = jedis.pipelined();
            for (String value : valueList) {
                p.lpush(key, value);
            }
            p.sync();
            return valueList.size();
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Set<String>> batchZRevrangeByScore(Set<String> keys, double max, double min, int offset,
                                                          int count) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            Pipeline p = jedis.pipelined();
            List<String> keyList = new ArrayList<String>(keys);
            for (String key : keyList) {
                p.zrevrangeByScore(key, max, min, offset, count);
            }
            List<Object> response = p.syncAndReturnAll();
            Map<String, Set<String>> resultMap = new HashMap<String, Set<String>>();
            if (response != null && response.size() != 0) {
                for (int i = 0; i < response.size(); ++i) {
                    resultMap.put(keyList.get(i), (Set<String>) response.get(i));
                }
            }
            return resultMap;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Set<String>> batchZRrangeByScore(Set<String> keys, double min, double max, int offset, int count) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            Pipeline p = jedis.pipelined();
            List<String> keyList = new ArrayList<String>(keys);
            for (String key : keyList) {
                p.zrangeByScore(key, min, max, offset, count);
            }
            List<Object> response = p.syncAndReturnAll();
            Map<String, Set<String>> resultMap = new HashMap<String, Set<String>>();
            if (response != null && response.size() != 0) {
                for (int i = 0; i < response.size(); ++i) {
                    resultMap.put(keyList.get(i), (Set<String>) response.get(i));
                }
            }
            return resultMap;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public String set(byte[] key, byte[] value, int seconds) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.setex(key, seconds, value);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public byte[] get(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.get(key);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public boolean expire(final byte[] key, final int seconds) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.expire(key, seconds);
            return result > 0;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public int del(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            long result = jedis.del(key);
            return (int) result;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public Long rpush(byte[] key, byte[]... strings) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.rpush(key, strings);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    @Override
    public byte[] lindex(byte[] key, long index) {
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            return jedis.lindex(key, index);
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }

    public int batchHsets(Map<String, Map<String, String>> valuesMap) {
        if (valuesMap == null || valuesMap.size() == 0) {
            return 0;
        }
        Jedis jedis = jedisPool.getResource();
        boolean broken = false;
        try {
            int count = 0;
            Pipeline p = jedis.pipelined();
            for (Map.Entry<String, Map<String, String>> entry : valuesMap.entrySet()) {
                if (entry.getValue() != null && entry.getValue().size() != 0) {
                    p.hmset(entry.getKey(), entry.getValue());
                    count += 1;
                }
            }
            p.sync();
            return count;
        } catch (JedisConnectionException e) {
            jedisPool.returnBrokenResource(jedis);
            broken = true;
            throw e;
        } finally {
            if (jedis != null && !broken) {
                jedisPool.returnResource(jedis);
            }
        }
    }
}
