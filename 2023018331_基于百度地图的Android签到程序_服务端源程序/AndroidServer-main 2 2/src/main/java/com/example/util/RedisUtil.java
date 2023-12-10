package com.example.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class RedisUtil {
    // Redis实例对象
    private static Jedis jedis;
    // Redis服务器地址
    private static final String redisHost = "localhost";
    // Redis服务器端口号
    private static final int redisPort = 6379;

    // 获取Redis连接对象
    public static Jedis getJedis() {
        try {
            jedis = new Jedis(redisHost, redisPort);
            return jedis;
        } catch (JedisConnectionException e) {
            System.out.println("连接Redis服务器失败！");
            e.printStackTrace();
            return null;
        }
    }

    // 关闭Redis连接
    public static void closeJedis() {
        if (jedis != null) {
            jedis.close();
        }
    }
}

