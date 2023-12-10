package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.pojo.User;
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

@WebServlet(name = "ChangeInfo", value = "/ChangeInfo")
public class ChangeInfo extends HttpServlet {
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

        String temp=URLDecoder.decode(rjson.getString("user"), "utf-8");
        System.out.println(temp);

        User user = JSON.parseObject(temp, User.class);//是用了反射机制來完成对象的封闭

        String password=user.getPassword();
        String Sex=user.getSex();
        String Username=user.getName();
        String Sign=user.getQianming();
        UserServiceImpl us = new UserServiceImpl();
        int i;
        boolean loginInfo = false;

        if (password==null)
        {//修改个人信息
            System.out.println("修改的是个人信息");
            i= us.ModifyInfo(user);
            System.out.println(user);
        }
        else {
            i=us.ModifyPassword(user);
            System.out.println("修改的是个人密码");
        }

        if (i>0) {
            //登录成功
            System.out.println("修改个人信息添加成功");
            loginInfo = true;
        }
        else{
            System.out.println("修改个人信息失败");
        }
        //将结果返回给客户端，将結果构建成json数据返回给客戶端
        rjson = new JSONObject();
        rjson.put("json", loginInfo);
        response.getOutputStream().write(
                rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
    }
}
