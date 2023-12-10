package com.example.controller;



import com.alibaba.fastjson.JSONObject;
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
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.ArrayList;

@WebServlet(name = "SignStatic", value = "/SignStatic")
public class SignStatic extends HttpServlet {
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

        JSONObject rjson = (JSONObject) JSONObject.parse(s.toString());
        System.out.println("rjson"+rjson);
        String groupid =rjson.getString("groupid");

        String sql="select signid from signinfo where groupid =?";
        ResultSet resultSet =JDBCUtil.executeQuery(sql,groupid);
        ArrayList<Integer>singid =new ArrayList<>();
        ArrayList<SignRecord>signRecords =new ArrayList<>();
        try {
            while (resultSet.next()){
                singid.add(resultSet.getInt("signid"));
            }
            System.out.println(singid);
            for (int i = 0; i < singid.size(); i++) {
                sql="select * from signrecord where signid=?";
                ResultSet resultSet2 =JDBCUtil.executeQuery(sql,singid.get(i));
                System.out.println(resultSet2);
                while (resultSet2.next()){
                    System.out.println(778899);
                    SignRecord signRecord =new SignRecord();
                    signRecord.setUsername(resultSet2.getString("username"));
                    signRecord.setSigntime(resultSet2.getString("signtime"));
                    signRecord.setSignsuccess(resultSet2.getString("signsuccess"));
                    signRecord.setId(resultSet2.getString("id"));
                    signRecord.setJingdu(resultSet2.getString("jindu"));
                    signRecord.setSignid(resultSet2.getString("signid"));
                    signRecord.setWeidu(resultSet2.getString("weidu"));
                    signRecord.setUsernumber(resultSet2.getString("usernumber"));
                    signRecord.setText(resultSet2.getString("text"));
                    System.out.println(signRecord);
                    signRecords.add(signRecord);
                }
            }
        }catch (Exception e){e.printStackTrace();}

        Gson gson = new Gson();
        String json = gson.toJson(signRecords);
        response.getOutputStream().write(json.toString().getBytes("UTF-8"));
        System.out.println("签到服务结束");
        System.out.println(signRecords);
    }
}

