package com.example.controller;

import com.example.pojo.SignInfo;
import com.example.service.UserServiceImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

@WebServlet(name = "IsANewSign", value = "/IsANewSign")

public class IsANewSign extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
       
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("  获得签到 服务开始");

        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;

        StringBuffer s = new StringBuffer();

         line = br.readLine();
        {
            s.append(line);

        }
        br.close();
        HashMap hashMap = new Gson().fromJson(s.toString(),HashMap.class);
        String groupid= hashMap.get("groupid").toString();
        //String userid= hashMap.get("userid").toString();

        System.out.println(groupid);

        UserServiceImpl us = new UserServiceImpl();


        SignInfo signInfo =us.getASign(groupid);
        //System.out.println("获得签到拿到"+signInfo);


        if(signInfo!=null) {
            String str = new Gson().toJson(signInfo);
            response.getOutputStream().write(
                    str.toString().getBytes("UTF-8"));
        }
        System.out.println("   获得签到 服务结束");
    }

}
