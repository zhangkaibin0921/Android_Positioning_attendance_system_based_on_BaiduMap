package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.UserDaoImpl;
import com.example.pojo.HostAndGuest;
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

@WebServlet(name = "AddNewFriend", value = "/AddNewFriend")
public class AddNewFriend extends HttpServlet {
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
        //JSON：这是json解析包，IDEA是没有，要我们自己导入
        HostAndGuest user = JSON.parseObject(s.toString(), HostAndGuest.class);//是用了反射机制來完成对象的封闭

        String Host = URLDecoder.decode(user.getHost(), "utf-8");
        String Guest = URLDecoder.decode(user.getGuest(), "utf-8");
        System.out.println("Guset:"+Guest +  ", Host;" +Host );
        System.out.println(user);
        // 去数据库完成用户登录功能
        UserServiceImpl us = new UserServiceImpl();
        UserDaoImpl ud=new UserDaoImpl();

        //调用登录的方法
        int i = us.addFriend(Host,Guest);


        System.out.println(i);
        boolean loginInfo = false;

        User name=ud.findByUsername(Guest);

        System.out.println(name.getName());

        if (i ==2) {
            //登录成功
            System.out.println("添加 朋友 成功");
            loginInfo = true;
        }
        else{
            System.out.println("添加 朋友 失败");
        }
        //将结果返回给客户端，将結果构建成json数据返回给客戶端
        JSONObject rjson = new JSONObject();
        rjson.put("json", loginInfo);
        rjson.put("name",name.getName());
        rjson.put("id",name.getId());
        response.getOutputStream().write(
                rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
    }
}
