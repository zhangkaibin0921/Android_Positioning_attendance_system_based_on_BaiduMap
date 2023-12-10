package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.SignQueue;
import com.example.pojo.SignRecord;
import com.example.service.UserServiceImpl;
import com.example.util.JDBCUtil;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@WebServlet(name = "EditSign", value = "/EditSign")
public class EditSign extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {


        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        System.out.println("EditSign");
        BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String line = null;
        StringBuffer s = new StringBuffer();
        line = br.readLine();{s.append(line);}
        br.close();
        SignQueue signQueue = new Gson().fromJson(s.toString(), SignQueue.class);
        System.out.println(signQueue);

         int groupId=signQueue.getGroupId();
         String startTime=signQueue.getStartTime();
         String endTime=signQueue.getEndTime();
         String type=signQueue.getType();
         String timetype=signQueue.getTimetype();
         String howLong=signQueue.getHowLong();
         String nums=signQueue.getNums();
         String signTexts=signQueue.getSignTexts();
         String signTextsType=signQueue.getSignTextsType();
         String timeHour=signQueue.getTimeHour();

         String sql="SELECT * FROM   signqueue  WHERE groupid =?";
         int i;
            ResultSet resultSet=JDBCUtil.executeQuery(sql,signQueue.getGroupId());
        if (resultSet.next()){
            sql ="UPDATE signqueue SET starttime=?, endtime=?, type=?, timetype=?, howlong=?, nums=?, signtexts=?, signtextstype=?, timehour=? WHERE groupid=?;";
             i =JDBCUtil.executeUpdate(sql,startTime,endTime,type,timetype,howLong,nums,signTexts,signTextsType,timeHour,groupId);
            System.out.println("0="+i);
        }else { sql ="insert into signqueue( groupid ,starttime, endtime, type, timetype, howlong, nums, signtexts, signtextstype,timehour) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?,?) ;";
            i =JDBCUtil.executeUpdate(sql,groupId,startTime,endTime,type,timetype,howLong,nums,signTexts,signTextsType,timeHour);
            }
            System.out.println(i+" 0");
        JSONObject rjson = new JSONObject();
        if (i==1){
            rjson.put("json", i);
        }else {rjson.put("json",0);}
            System.out.println(rjson);
        response.getOutputStream().write(rjson.toString().getBytes("UTF-8"));
        }catch (Exception e){e.printStackTrace();}


    }
}

