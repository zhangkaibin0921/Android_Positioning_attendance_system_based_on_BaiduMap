package com.example.websocket;

import redis.clients.jedis.Jedis;

public class RedisExample {
    public static void main(String[] args) {
        // Connect to Redis server
        Jedis jedis = new Jedis("localhost");

        // Perform Redis operations
        jedis.set("mykey", "myvalue");
        String value = jedis.get("mykey");
        System.out.println(value);

        // Disconnect from Redis server
        jedis.close();
    }
}
