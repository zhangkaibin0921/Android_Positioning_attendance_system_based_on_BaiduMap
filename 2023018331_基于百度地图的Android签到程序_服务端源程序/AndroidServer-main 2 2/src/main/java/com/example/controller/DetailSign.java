package com.example.controller;

import com.example.pojo.SignRecord;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "DetailSign", value = "/DetailSign")
public class DetailSign extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
       
        response.setContentType("application/json;charset=UTF-8");
        System.out.println(" 获得详细签到 开始");
////
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;

        StringBuffer s = new StringBuffer();

        line = br.readLine();
        {
            s.append(line);

        }
        br.close();
        HashMap hashMap = new Gson().fromJson(s.toString(),HashMap.class);
        String signid= hashMap.get("signid").toString();
        System.out.println("执行  获得详细签到 操作");
        System.out.println(signid);
        UserServiceImpl us = new UserServiceImpl();


        List<SignRecord>signRecords=new ArrayList<>();
        signRecords =us.getDetailSign(signid);
        if(signRecords!=null) {
            System.out.println("进入");
            String str = new Gson().toJson(signRecords);
            response.getOutputStream().write(
                    str.toString().getBytes("UTF-8"));
        }
        System.out.println("   获得详细签到 服务结束");
    }
}
