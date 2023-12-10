package com.example.controller;

import com.example.pojo.UserStat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.ResultSet;
import java.util.*;

@WebServlet(name = "Download", value = "/Download")
public class Download extends HttpServlet {

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
            System.out.println("Download");
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String line = null;
            StringBuffer s = new StringBuffer();
            line = br.readLine();{s.append(line);}
            br.close();
            HashMap<String, int[]> hashMap = new Gson().fromJson(s.toString(), new TypeToken<HashMap<String, int[]>>(){}.getType());
            System.out.println(hashMap);

            String fileName=new Random().nextInt(999999999)+".csv";
            CSVWriter writer = new CSVWriter(new FileWriter(fileName));
            String[] columnHeaders = {"姓名", "签到成功", "学生请假", "签到失败", "未签到", "签到率"};
            writer.writeNext(columnHeaders);
            for (Map.Entry<String, int[]> entry : hashMap.entrySet()) {
                int[] stat = entry.getValue();
                //假如下面的数据才是你要填写的数据
                String username = entry.getKey();
                int a1=stat[2];
                int a2=stat[3];
                int a3=stat[4];
                int a4=stat[5];
                double rate = (double) stat[0] / stat[1];
                String[] data={username,String.valueOf(a1),String.valueOf(a2),String.valueOf(a3),String.valueOf(a4),String.format("%.2f%%", rate * 100)};
                writer.writeNext(data);
            }

            String downloadUrl = request.getContextPath() + "/download/" + fileName;
            System.out.println(downloadUrl);
            writer.close();
            response.getOutputStream().write(
                    downloadUrl.toString().getBytes("UTF-8"));
            System.out.println("tttt");
        }catch (Exception e){e.printStackTrace();}


    }


}

