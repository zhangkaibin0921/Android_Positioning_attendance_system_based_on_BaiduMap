package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.UserDao;
import com.example.dao.UserDaoImpl;
import com.example.pojo.User;
import com.example.service.UserService;
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

@WebServlet(name = "Forget", value = "/Forget")
public class Forget extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       
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
        System.out.println(s.toString());

        User user = JSON.parseObject(s.toString(), User.class);

        String phone = URLDecoder.decode(user.getPhone(), "utf-8");
        String password = URLDecoder.decode(user.getPassword(), "utf-8");
        System.out.println(user);

        UserService us = new UserServiceImpl();
        UserDao ud = new UserDaoImpl();

        //调用注册的方法
        int i = us.forgetPassword(phone, password);
        boolean rs = false;
        //判断是否注册成功
        if (i > 0) {
            System.out.println("修改密码成功");
            rs = true;
        }
        else{
            System.out.println("修改密码失败");
        }
        //将结果返回给客户端	，將结果构建成json数据返回給客戶端
        JSONObject rjson = new JSONObject();
        rjson.put("json", rs);
        response.getOutputStream().write(
                rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
    }
}
