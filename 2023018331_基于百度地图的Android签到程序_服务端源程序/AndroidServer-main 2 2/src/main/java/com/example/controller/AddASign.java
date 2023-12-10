package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pojo.SignInfo;
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

@WebServlet(name = "AddASign", value = "/AddASign")
public class AddASign extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符编码，防止中文乱码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
       
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("  发起签到 服务开始");

        BufferedReader br = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = null;

        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);

        }

        br.close();
        SignInfo signInfo = JSON.parseObject(s.toString(), SignInfo.class);//是用了反射机制來完成对象的封闭

        String groupid = URLDecoder.decode(signInfo.getGroupid(), "utf-8");
        String signtext=URLDecoder.decode(signInfo.getSigntext(), "utf-8");;
        String signstarttime=URLDecoder.decode(signInfo.getSignstarttime(), "utf-8");;
        String signmins=URLDecoder.decode(signInfo.getSignmins(), "utf-8");;
        String jingdu=URLDecoder.decode(signInfo.getJingdu(), "utf-8");;
        String weidu=URLDecoder.decode(signInfo.getWeidu(), "utf-8");;
        System.out.println("执行  发起签到 操作");
        System.out.println(signInfo);
        System.out.println(groupid);

        UserServiceImpl us = new UserServiceImpl();

        int i =us.addASign(groupid,signtext,signstarttime,signmins,jingdu,weidu);
        //System.out.println("发起签到 拿到"+i);
            JSONObject rjson = new JSONObject();
           rjson.put("json", 1);
            rjson.put("signid",i);


            response.getOutputStream().write(
                    rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应

        System.out.println("   发起签到 服务结束");
    }
}
