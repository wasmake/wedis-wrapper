package com.wasmake.wedis;

import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author Andrew R.
 */
@Getter
public class RedisPool {
    private JedisPool jedisPool;
    private boolean connected;
    private RedisConfig redisConfig;

    public RedisPool(RedisConfig redisConfig){
        this.redisConfig = redisConfig;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(128);
        jedisPoolConfig.setMaxTotal(128);

        String password = redisConfig.getPassword();
        String ip = redisConfig.getHost();
        int port = redisConfig.getPort();

        try {
            jedisPool = password == null || password.isEmpty() ? new JedisPool(jedisPoolConfig, ip, port) : new JedisPool(jedisPoolConfig, ip, port, redisConfig.getTimeout(), password);
            if(jedisPool != null && jedisPool.getResource().isConnected()) {
                connected = true;
            }

        } catch (Exception e) {
            System.out.println("Error! Failed to connect to redis -> " + e.getLocalizedMessage() + "\nPlease check redis configuration");
            throw new RuntimeException("Failed to connect to redis");
        }
    }

    public void publish(String key, String message){
        try (Jedis jedis = getResource()) {
            jedis.publish(redisConfig.getPrefix(), key + "|" + message);
        }
    }

    public Jedis getResource(){
        return this.jedisPool.getResource();
    }

}
