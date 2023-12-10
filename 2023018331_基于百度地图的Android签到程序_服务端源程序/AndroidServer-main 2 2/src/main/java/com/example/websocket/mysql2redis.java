package com.example.websocket;

import redis.clients.jedis.Jedis;

import java.sql.*;

public class mysql2redis {
    public static void main(String[] args) {
        // Connect to Redis server
        Jedis jedis = new Jedis("localhost");

        // Connect to MySQL database
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost/android","root","root");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Retrieve data from MySQL database and insert into Redis
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement("SELECT * FROM ");
            rs = ps.executeQuery();
            while (rs.next()) {
                jedis.hset("myhash", rs.getString("key"), rs.getString("value"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // Disconnect from Redis server
        jedis.disconnect();
    }
}
