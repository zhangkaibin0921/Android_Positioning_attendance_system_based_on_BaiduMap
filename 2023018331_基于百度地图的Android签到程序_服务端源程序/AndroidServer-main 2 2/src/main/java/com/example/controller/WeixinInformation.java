package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.pojo.WeixinList;
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

@WebServlet(name = "WeixinInformation", value = "/WeixinInformation")
public class WeixinInformation extends HttpServlet {
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
        System.out.println("  info 服务开始");

        BufferedReader br = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);
        }

        br.close();
        System.out.println(s.toString());//
        //JSON：这是json解析包，IDEA是没有，要我们自己导入
        WeixinList weixinList = JSON.parseObject(s.toString(), WeixinList.class);//是用了反射机制來完成对象的封闭

        String number = URLDecoder.decode(String.valueOf(weixinList.getNumber()), "utf-8");
        System.out.println("执行 info操作");
        System.out.println(weixinList);
        System.out.println(number);

        UserServiceImpl us = new UserServiceImpl();

        List<WeixinList> weixinList1 = us.informationUser(number);
        System.out.println("conrtroller拿到"+weixinList1);
        if(weixinList1 != null) {
            System.out.println("进入");
            Gson gson = new Gson();
            String s1 =gson.toJson(weixinList1);
            System.out.println(s1);
            response.getOutputStream().write(
                   s1.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
        }
        System.out.println("  info 服务结束");
    }
}
