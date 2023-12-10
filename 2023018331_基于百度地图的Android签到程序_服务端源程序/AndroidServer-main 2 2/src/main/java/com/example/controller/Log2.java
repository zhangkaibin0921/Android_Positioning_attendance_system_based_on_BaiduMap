package com.example.controller;

import com.example.websocket.DatabaseToRedis;
import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@WebServlet(name = "Log2", value = "/Log2")
public class Log2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();
        while ((line = br.readLine()) != null) {s.append(line);}
        br.close();System.out.println(s.toString());
        DatabaseToRedis databaseToRedis = new DatabaseToRedis();
        Jedis jedis= new Jedis("loaclhost",6397);
        System.out.println(jedis);
    }
}
