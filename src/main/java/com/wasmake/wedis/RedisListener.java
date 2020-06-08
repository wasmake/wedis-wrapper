package com.wasmake.wedis;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * @author Andrew R.
 */
public class RedisListener {

    private JavaPlugin plugin;
    HashMap<String, Callback> callbacks = new HashMap<>();

    private String prefix;

    public RedisListener(JavaPlugin plugin, String prefix, Jedis jedis){
        this.plugin = plugin;
        this.prefix = prefix;
        System.out.println(prefix + " trying to subscribe to redis");
        new BukkitRunnable() {
            @Override
            public void run() {
                jedis.subscribe(new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if (channel.equals(prefix)) {
                            String[] args = message.split(Pattern.quote("|"));
                            if(callbacks.containsKey(args[0])){
                                String[] messages = Arrays.copyOfRange(args, 1, args.length);
                                String msg = Arrays.toString(messages);
                                callbacks.get(args[0]).onMessage(msg.substring(1).split(";"));
                            }
                        }
                        super.onMessage(channel, message);
                    }

                    @Override
                    public void onSubscribe(String channel, int subscribedChannels) {
                        System.out.println(prefix + " successfully subscribed to redis channel.");
                    }

                }, prefix);
            }
        }.runTaskAsynchronously(plugin);

    }

    public void addListener(String channel, Callback callback){
        System.out.println("Registering a new redis listener for " + prefix + " " + channel);
        callbacks.putIfAbsent(channel, callback);
    }


}

