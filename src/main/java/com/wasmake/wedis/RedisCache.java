package com.wasmake.wedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * @author Andrew R.
 */
public class RedisCache {
    private JedisPool jedisPool;
    private String prefix;

    public RedisCache(JedisPool jedisPool, String prefix){
        this.jedisPool = jedisPool;
        try (Jedis jedis = getResource()) {
            Set<String> keys = jedis.keys(prefix);
            if(!keys.isEmpty()){
                System.out.println("Wedis found old keys over RedisCache, removing them...");
                for (String key : keys) {
                    System.out.println("Key " + key);
                    jedis.del(key);
                }
            }
        }
    }

    public Jedis getResource(){
        return this.jedisPool.getResource();
    }

    public String get(String key){
        try (Jedis jedis = getResource()) {
            return jedis.get(prefix + "."+key);
        }
    }

    public void set(String key, String value){
        try (Jedis jedis = getResource()) {
            jedis.set(prefix + "."+key, value);
        }
    }

    public void remove(String key){
        try (Jedis jedis = getResource()) {
            jedis.del(prefix + "."+key);
        }
    }

}