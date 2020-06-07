package com.wasmake.wedis;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.logging.Level;

/**
 * @author Andrew R.
 */
public class Redis {
    private String prefix;
    private JavaPlugin plugin;

    private JedisPool jedisPool;
    private RedisListener redisListener;
    private RedisCache redisCache;

    public Redis(JavaPlugin plugin, String prefix, JedisPool jedisPool){
        this.plugin = plugin;
        this.prefix = prefix;
        this.jedisPool = jedisPool;
        try {
            if(jedisPool != null && jedisPool.getResource().isConnected()) plugin.getLogger().log(Level.INFO, prefix + " is now connected to redis!");
            if(jedisPool != null && jedisPool.getResource().isConnected()) getConsumer(jedisPool);
            redisCache = new RedisCache(jedisPool, prefix);
        } catch (JedisConnectionException e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to redis, disabling plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void publish(String key, Object... objects){
        String s = "";
        for(Object obj : objects){
            s += ";" + obj.toString();
        }
        publish(key, s.substring(1));
    }

    public void publish(String key, String msg){
        try (Jedis jedis = getResource()) {
            System.out.println("Publishing msg " + msg + " to channel " + key);
            jedis.publish(prefix, key + "|" + msg);
        }
    }

    public void registerListener(String channel, Callback callback){
        redisListener.addListener(channel, callback);
    }

    public void runSyncEvent(Event event) {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(event));
    }

    public void getConsumer(JedisPool jedis){
        redisListener = new RedisListener(plugin, prefix, jedis);
    }

    public RedisCache getCache(){
        return this.redisCache;
    }

    public Jedis getResource(){
        return jedisPool.getResource();
    }

}
