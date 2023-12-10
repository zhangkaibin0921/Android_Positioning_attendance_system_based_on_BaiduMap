package com.example.dao;

import com.example.tool.MD5;
import com.example.util.RedisUtil;
import redis.clients.jedis.Jedis;

public class RedisUserDao {
    // Redis键值对中用户信息存储的键前缀
    private static final String USER_PREFIX = "user_";

    // 将用户信息存储到Redis中
    public int insertUser(String name, String phone, String password,String sex,String qianming) {
        try (Jedis jedis = RedisUtil.getJedis()) {
            String userKey = USER_PREFIX + phone;
            // 如果该用户已经存在，返回0
            if (jedis.exists(userKey)) {
                return 0;
            }
            // 存储用户信息到Redis中
            jedis.hset(userKey, "name", name);
            jedis.hset(userKey, "phone", phone);
            jedis.hset(userKey, "password", MD5.getMD5(password));
            jedis.hset(userKey, "sex", sex);
            jedis.hset(userKey, "qianming", qianming);
            // 设置用户信息在Redis中的过期时间
            jedis.expire(userKey, 3600);
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            RedisUtil.closeJedis();
        }
    }
}

