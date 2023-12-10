package com.example.controller;

import com.example.tool.MD5;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

public class main {

    public static String insertUser(String name, String phone, String password, String sex, String qianming, Jedis jedis) {
        String key = "user:" + phone;
        Map<String, String> user = new HashMap<>();
        String id =jedis.incr("user_id").toString();
        user.put("id",id);
        user.put("name", name);
        user.put("phone", phone);
        user.put("password", MD5.getMD5(password));
        user.put("sex", sex);
        user.put("qianming", qianming);
        return jedis.hmset(key, user);
    }
    Jedis jedis =new Jedis("localhost",6379);
//public static int insertUser(String name, String phone, String password, String sex, String qianming) {
//    String id = jedis.incr("user:id").toString();
//    Map<String, String> userData = new HashMap<>();
//    userData.put("id", id);
//    userData.put("name", name);
//    userData.put("phone", phone);
//    userData.put("password", password);
//    userData.put("sex", sex);
//    userData.put("qianming", qianming);
//    jedis.hmset("user:" + id, userData);
//    return 1;
//}


    public static void main(String[] args) {
        insertUser("zz","123","123","1","1",new Jedis("localhost",6379));
    }

}