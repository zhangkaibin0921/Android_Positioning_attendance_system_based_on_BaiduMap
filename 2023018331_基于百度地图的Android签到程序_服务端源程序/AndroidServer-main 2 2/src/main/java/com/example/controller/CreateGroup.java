


package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.User;
import com.example.service.UserServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CreateGroup", value = "/CreateGroup")
public class CreateGroup  extends HttpServlet {
    public static int allgroupid=10;

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
        System.out.println("创建群聊 开始");

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

        JSONObject rjson = (JSONObject) JSONObject.parse(s.toString());

        System.out.println("创建群聊  rjson"+rjson);
        String groupname=URLDecoder.decode(rjson.getString("groupname"), "utf-8");
        System.out.println("创建群聊"+groupname);
        Type listOfMyClassObject = new TypeToken<ArrayList<User>>() {}.getType();
        Gson gson = new Gson();
        List<User> chooses = gson.fromJson(URLDecoder.decode(rjson.getString("chooses"), "utf-8"), listOfMyClassObject);
        System.out.println("创建群聊 选择"+chooses);
        User createuser = gson.fromJson(URLDecoder.decode(rjson.getString("createUser"), "utf-8"), User.class);
        System.out.println("创建群聊 创建者"+createuser);
        chooses.add(createuser);


        UserServiceImpl us = new UserServiceImpl();

       boolean flag = us.createGroup(groupname,chooses,createuser);


        if(flag) {
            System.out.println("创建群聊成功");


           rjson.put("json", true);


            response.getOutputStream().write(
                    rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
        }
        System.out.println("创建群聊 结束");
    }
}
