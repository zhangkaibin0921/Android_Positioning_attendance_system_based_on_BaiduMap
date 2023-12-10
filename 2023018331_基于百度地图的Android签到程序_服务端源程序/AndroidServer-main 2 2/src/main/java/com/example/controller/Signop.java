package com.example.controller;



import com.alibaba.fastjson.JSONObject;
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

@WebServlet(name = "Signop", value = "/Signop")
public class Signop extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");

        response.setContentType("application/json;charset=UTF-8");
        System.out.println("OP服务开始"+request.getContentType());
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
        System.out.println("rjson"+rjson);
        String signid =rjson.getString("signid");
        int type = Integer.parseInt(URLDecoder.decode(String.valueOf(rjson.getInteger("type")), "utf-8"));
        if (type==0){
            // modify the sign time to add this time
            int  time = Integer.parseInt(rjson.getString("time"));
            System.out.println("time"+time);
            UserServiceImpl userService =new UserServiceImpl();
            int i =userService.addsigntime(signid,time);
            JSONObject rjson2 = new JSONObject();
            rjson2.put("json", i);
            response.getOutputStream().write(rjson2.toString().getBytes("UTF-8"));
            System.out.println(rjson2);
        }
        if (type==1){
            // modify the sign title
            String title =URLDecoder.decode(rjson.getString("title"), "utf-8");
            System.out.println("title"+title);
            UserServiceImpl userService =new UserServiceImpl();
            int i =userService.signtitle(signid,title);
            JSONObject rjson2 = new JSONObject();
            rjson2.put("json", i);
            response.getOutputStream().write(rjson2.toString().getBytes("UTF-8"));
            System.out.println(rjson2);

            // service here sql...
        }
        if (type==2){
            // modify a signrecord
            String phone =rjson.getString("phone");
            System.out.println("phone"+phone);
            UserServiceImpl userService =new UserServiceImpl();
            int i =userService.signteacher(signid,phone);
            JSONObject rjson2 = new JSONObject();
            rjson2.put("json", i);
            response.getOutputStream().write(rjson2.toString().getBytes("UTF-8"));
            System.out.println(rjson2);
        }
        if (type==3){
            //end thr sign
            // service here sql...
            UserServiceImpl userService =new UserServiceImpl();
            int i =userService.signend(signid);
            JSONObject rjson2 = new JSONObject();
            rjson2.put("json", i);
            response.getOutputStream().write(rjson2.toString().getBytes("UTF-8"));
            System.out.println(rjson2);

        }
        if (type==4){
            // delete the sign
            // service here sql...
            UserServiceImpl userService =new UserServiceImpl();
            int i =userService.signdelete(signid);
            JSONObject rjson2 = new JSONObject();
            rjson2.put("json", i);
            response.getOutputStream().write(rjson2.toString().getBytes("UTF-8"));
            System.out.println(rjson2);
        }



        System.out.println("OP服务结束");
    }
}

