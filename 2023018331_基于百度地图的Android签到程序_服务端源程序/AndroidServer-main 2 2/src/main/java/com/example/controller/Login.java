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

@WebServlet(name = "Login", value = "/Login")
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置字符编码，防止中文乱码
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        //以json数据完成操作
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("登录服务开始"+request.getContentType());// 得到客户端发送过来内容的类型，application/json;charset=UTF-8
        System.out.println(request.getRemoteAddr());// 得到客户端的ip地址，
        BufferedReader br = new BufferedReader(new InputStreamReader(// 使用字符流读取客户端发过来的数据
                request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();

        while ((line = br.readLine()) != null) {
            s.append(line);
        }
        // 关闭io流
        br.close();
        System.out.println(s.toString());
        //JSON：这是json解析包，IDEA是没有，要我们自己导入
        User user = JSON.parseObject(s.toString(), User.class);//是用了反射机制來完成对象的封闭
        //以utf-8解码操作
        String number = URLDecoder.decode(user.getNumber(), "utf-8");
        String password = URLDecoder.decode(user.getPassword(), "utf-8");
        System.out.println("手机号:" + number + ", 密码;" + password);
        // 去数据库完成用户登录功能
        UserServiceImpl us = new UserServiceImpl();
        //调用登录的方法
        User user1 = us.login(number, password);
        boolean loginInfo = false;
        if (user1 != null) {
            //登录成功
            System.out.println("登陆成功");
            loginInfo = true;
        }
        else{
            System.out.println("登陆失败");
        }
        //将结果返回给客户端，将結果构建成json数据返回给客戶端
        //
        System.out.println("查询到的用户为"+user1);
        JSONObject rjson = new JSONObject();
        rjson.put("json", loginInfo);
        rjson.put("qianming",user1.getQianming());
        rjson.put("sex",user1.getSex());
        rjson.put("name",user1.getName());
        rjson.put("id",user1.getId());
        response.getOutputStream().write(
                rjson.toString().getBytes("UTF-8"));// 向客户端发送一个带有json对象内容的响应
        System.out.println("登录服务结束");
    }
}
