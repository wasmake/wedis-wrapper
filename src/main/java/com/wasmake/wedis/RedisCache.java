package com.wasmake.wedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author Andrew R.
 */
public class RedisCache {
    private Jedis jedis;
    private String prefix;

    public RedisCache(JedisPool redis, String prefix){
        this.jedis = redis.getResource();
        Set<String> keys = jedis.keys(prefix);
        if(!keys.isEmpty()){
            System.out.println("Wedis found old keys over RedisCache, removing them...");
            for (String key : keys) {
                System.out.println("Key " + key);
                jedis.del(key);
            }
        }

    }

    public String get(String key){
        return jedis.get(prefix + "."+key);
    }

    public void set(String key, String value){
        jedis.set(prefix + "."+key, value);
    }

    public void remove(String key){
        jedis.del(prefix + "."+key);
    }

}