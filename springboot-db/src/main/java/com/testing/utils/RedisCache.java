package com.testing.utils;

import redis.clients.jedis.Jedis;

public class RedisCache {

    private static Jedis jedis;

    public RedisCache(String hostname, Integer port) {
        jedis = new Jedis(hostname, port);
    }

    public String get(String key) {
        return jedis.get(key);
    }

    public void put(String key, String value) {
        jedis.set(key, value);
    }

    public void delete(String key) {
        jedis.del(key);
    }
}
