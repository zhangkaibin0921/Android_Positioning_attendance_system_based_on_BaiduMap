package com.example.controller;

import com.alibaba.fastjson.JSON;
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
import java.net.URLDecoder;
import java.util.List;

@WebServlet(name = "SignGroup", value = "/SignGroup")
public class SignGroup extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符编码，防止中文乱码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
       
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("  获取签到列表 服务开始");

        BufferedReader br = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = null;

        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);}


        br.close();
        System.out.println(s.toString());//

       SignInfo signInfo = JSON.parseObject(s.toString(), SignInfo.class);//是用了反射机制來完成对象的封闭

        String groupid = URLDecoder.decode(signInfo.getGroupid(), "utf-8");
        System.out.println("执行  获取签到列表 操作");
        System.out.println(signInfo);
        System.out.println(groupid);

        UserServiceImpl us = new UserServiceImpl();
        List<SignInfo> signInfos = us.getSignInfo(groupid);



        if(signInfos != null) {
            System.out.println("进入");


            Gson gson = new Gson();
            String s1 =gson.toJson(signInfos);
            System.out.println(s1);

            response.getOutputStream().write(
                    s1.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
        }
        System.out.println("   获取签到列表 服务结束");
    }
}
