package com.example.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.service.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLDecoder;

@WebServlet(name = "Signin", value = "/Signin")
public class Signin extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符编码，防止中文乱码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
       
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("签到服务开始"+request.getContentType());// 得到客户端发送过来内容的类型，application/json;charset=UTF-8

        BufferedReader br = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);
        }

        br.close();
        System.out.println(s.toString());

        JSONObject rjson = (JSONObject) JSONObject.parse(s.toString());
        System.out.println("rjson"+rjson);

        String name=URLDecoder.decode(rjson.getString("name"), "utf-8");
        String number=URLDecoder.decode(rjson.getString("number"), "utf-8");
        String signinfoid=URLDecoder.decode(rjson.getString("signinfoid"), "utf-8");
        String signsuccess=URLDecoder.decode(rjson.getString("signsuccess"), "utf-8");


        UserServiceImpl us = new UserServiceImpl();
        us.insertsign(name,number,signinfoid,signsuccess);
        System.out.println("签到服务结束");
    }
}
