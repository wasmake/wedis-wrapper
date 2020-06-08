package com.wasmake.wedis;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import java.util.logging.Level;

/**
 * @author Andrew R.
 */
public class Redis {
    private String prefix;
    private JavaPlugin plugin;

    private Jedis jedis;
    private RedisListener redisListener;
    private RedisCache redisCache;
    private RedisConfig redisConfig;
    private RedisPool redisPool;

    public Redis(JavaPlugin plugin, String prefix, RedisConfig redisConfig){
        this.plugin = plugin;
        this.prefix = prefix;
        this.redisConfig = redisConfig;
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getMaxTotal());

        this.jedis = new Jedis(redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout());
        this.redisPool = new RedisPool(redisConfig);
        if(!redisPool.isConnected()){
            plugin.getLogger().log(Level.SEVERE, "Failed to connect to redis, disabling plugin!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        } else {
            this.redisListener = new RedisListener(plugin, prefix, jedis);
            this.redisCache = new RedisCache(redisPool.getJedisPool(), prefix);
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
        this.redisPool.publish(key, msg);
    }

    public void registerListener(String channel, Callback callback){
        redisListener.addListener(channel, callback);
    }

    public void runSyncEvent(Event event) {
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getServer().getPluginManager().callEvent(event));
    }

    public RedisCache getCache(){
        return this.redisCache;
    }

}
