# wedis-wrapper

A Redis helper based on Jedis for Spigot plugins.

# What is Wedis-wrapper?

Wedis is a Redis library designed to make easier the messenger of jedis pub/sub channel and the redis cache.

## Installing

You can sahde this repository into your plugin, as simple as copying 4 classes.

# Using wedis in your plugin

Wedis is designed to be simple and easy to use.

You just need to make a new instance unto your main class:

`Redis redis = new Redis(this, "skywars", yourJedisPool); // Assuming 'this' is your JavaPlugin instance`

## Sending a message

You can send a message to redis easily.

            redis.publish("island-tp", 1, "wasmake", "a-uu-id");

### Registering a listener

You can register listeners in a lambda way so it is easier.

            redis.registerListener("island-tp", message -> {
                System.out.println("Msg island-tp received!");
                String[] msg = message.split(";"); //0 Num;Username;AKAR-UUID
                int num = Integer.parseInt(msg[0].trim());
                String username = msg[1].trim();
                ObjectId uuid = new ObjectId(msg[2].trim()); // AlarCore UUID
                redis.runSyncEvent(new IslandTeleportEvent(num, uuid, username, IslandTeleportEvent.Teleport.PREPARE));
            });
            
Do you like the project? Contribute or give a star!