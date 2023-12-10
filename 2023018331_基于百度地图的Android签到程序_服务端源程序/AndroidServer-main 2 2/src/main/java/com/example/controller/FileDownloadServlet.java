package com.example.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@WebServlet(name = "FileDownloadServlet", value = "/download/*")
public class FileDownloadServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取要下载的文件路径
        String fileName = request.getPathInfo().substring(1);
        String filePath = "" + fileName;

        // 设置响应的内容类型和头信息
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        // 读取要下载的文件内容，并写入响应输出流中
        FileInputStream in = new FileInputStream(filePath);
        OutputStream out = response.getOutputStream();
        byte[] buffer = new byte[4096];
        int length = 0;
        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        in.close();
        out.flush();
    }
}
