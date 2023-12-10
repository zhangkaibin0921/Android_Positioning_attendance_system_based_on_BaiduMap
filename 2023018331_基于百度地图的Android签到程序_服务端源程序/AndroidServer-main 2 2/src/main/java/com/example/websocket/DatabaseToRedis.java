package com.example.websocket;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class DatabaseToRedis {
    // Redis连接池
    public static JedisPool jedisPool = null;

    // MySQL连接信息
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    private static final String MYSQL_URL = "jdbc:mysql://localhost:3306/android?useUnicode=true&characterEncoding=utf-8";
    private static final String MYSQL_USER = "root";
    private static final String MYSQL_PASSWORD = "root";
    // Redis连接信息
    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
//    private static final String REDIS_PASSWORD = "123456";
    private static final int REDIS_MAX_TOTAL = 1024;
    private static final int REDIS_MAX_IDLE = 200;
    private static final int REDIS_MAX_WAIT_MILLIS = 10000;

    /**
     * 获取Redis连接池
     */
    public static JedisPool getJedisPool() {
        System.out.println(11111);
        if (jedisPool == null) {
            synchronized (DatabaseToRedis.class) {
                if (jedisPool == null) {
                    JedisPoolConfig poolConfig = new JedisPoolConfig();
                    poolConfig.setMaxTotal(REDIS_MAX_TOTAL);
                    poolConfig.setMaxIdle(REDIS_MAX_IDLE);
                    poolConfig.setMaxWaitMillis(REDIS_MAX_WAIT_MILLIS);
                    jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT, 0);
                }
            }
        }
        return jedisPool;
    }

    /**
     * 获取MySQL连接
     */
    private static Connection getMysqlConnection() throws ClassNotFoundException, SQLException {
        Class.forName(MYSQL_DRIVER);
        return DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASSWORD);
    }

    /**
     * 将数据从MySQL导入到Redis
     */
    public static void importData() {
        // 获取Redis连接池
        JedisPool jedisPool = getJedisPool();
        Jedis jedis = jedisPool.getResource();

        try {
            // 获取MySQL连接
            Connection conn = getMysqlConnection();

            // 读取表名
            PreparedStatement tableStmt = conn.prepareStatement("show tables");
            ResultSet tableRs = tableStmt.executeQuery();
            while (tableRs.next()) {
                String tableName = tableRs.getString(1);

                // 读取表中数据
                PreparedStatement dataStmt = conn.prepareStatement("select * from " + tableName);
                ResultSet dataRs = dataStmt.executeQuery();
                while (dataRs.next()) {
                    // 将数据写入Redis
                    Map<String, String> map = new HashMap<>();
                    for (int i = 1; i <= dataRs.getMetaData().getColumnCount(); i++) {
                        String columnLabel = dataRs.getMetaData().getColumnLabel(i);
                        String columnValue = dataRs.getString(i);
                        map.put(columnLabel, columnValue);
                    }
                    jedis.hmset(tableName + ":" + dataRs.getString(1), map);
                    System.out.println(tableName);
                    System.out.println(dataRs.getString(1));
                    System.out.println(map);
                }
                dataRs.close();
                dataStmt.close();
            }
            tableRs.close();
            tableStmt.close();

            // 关闭MySQL连接
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭Redis连接
            jedis.close();
        }
    }

    public static void main(String[] args) {
        importData();
    }
}

