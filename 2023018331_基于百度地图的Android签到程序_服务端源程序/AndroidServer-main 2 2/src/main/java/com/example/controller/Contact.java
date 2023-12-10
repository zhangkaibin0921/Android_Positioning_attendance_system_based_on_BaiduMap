package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.pojo.ContactList;
import com.example.pojo.User;
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

@WebServlet(name = "Contact", value = "/Contact")
public class Contact extends HttpServlet {
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

        BufferedReader br = new BufferedReader(new InputStreamReader(
                request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);
        }

        br.close();
        System.out.println(s.toString());// {"password":"123456","name":"admin"}
        //JSON：这是json解析包，IDEA是没有，要我们自己导入 StringName

        ContactList contactList = JSON.parseObject(s.toString(), ContactList.class);//是用了反射机制來完成对象的封闭

        String number = URLDecoder.decode(contactList.getNumber(), "utf-8");
        System.out.println(contactList);
        // 去数据库完成用户登录功能
        UserServiceImpl us = new UserServiceImpl();
        //调用登录的方法

        System.out.println("返回联系人");


        List<User> listfriend = us.selectPhoneNumber(number);
        if(listfriend  != null) {


            Gson gson = new Gson();
            String s1 =gson.toJson(listfriend);//


            System.out.println("返回联系人"+s1);

            response.getOutputStream().write(
                    s1.getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
        }
    }
}
