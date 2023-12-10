package com.example.util;

import java.sql.*;


public class JDBCUtil {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/android?useUnicode=true&characterEncoding=utf-8";
    private static final String USER = "root";
    private static final String PASSWORD = "root";
    private static Connection ct;
    private static PreparedStatement ps;
    private static ResultSet rs;


    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() {

        try {

            ct = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ct;
    }

    public static ResultSet executeQuery(String sql, Object... obj) {
        // 1.寰楀埌杩炴帴
        ct = getConnection();
        // 2.鍒涢敭鍙戦�佸璞�
        try {
            ps = ct.prepareStatement(sql);
            // 澶勭悊鍗犱綅绗﹂棶棰�
            if (obj != null) {

                for (int i = 0; i < obj.length; i++) {
                    ps.setObject(i + 1, obj[i]);
                }
            }
            rs = ps.executeQuery();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return rs;
    }


    public static int executeUpdate(String sql, Object... obj) {
        // 1.寰楀埌杩炴帴
        ct = getConnection();
        // 2.鍒涢敭鍙戦�佸璞�
        try {
            ps = ct.prepareStatement(sql);
            // 澶勭悊鍗犱綅绗﹂棶棰�
            if (obj != null) {
                for (int i = 0; i < obj.length; i++) {
                    ps.setObject(i + 1, obj[i]);
                }
            }
            int in = ps.executeUpdate();
            close(ct, ps, null);
            return in;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }


    public static void close(Connection ct, PreparedStatement ps, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (ct != null) {
            try {
                ct.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    // 缁欏閮ㄤ竴涓闂甤t,鍜宲s鐨勬柟娉�
    public static Connection getCt() {
        return ct;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

}
