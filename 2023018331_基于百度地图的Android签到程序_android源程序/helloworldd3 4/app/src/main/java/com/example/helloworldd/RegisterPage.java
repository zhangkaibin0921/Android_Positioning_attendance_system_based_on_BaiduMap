package com.example.helloworldd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPage extends AppCompatActivity {

    private MyHander myhander = new MyHander();
    EditText username;
    EditText email;
    EditText password;
    EditText confirm;
    Button verify;
    EditText sex;
    EditText qianming;

    String username1;
    String email1;
    String password1;
    String confirm1;
    String sex1;
    String qianming1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        ImageView imageView=findViewById(R.id.signupp);
        Glide.with(this).load(R.drawable.forget).into(imageView);
        username =findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirm = findViewById(R.id.confirm);
        verify = findViewById(R.id.Signup);
        sex = findViewById(R.id.sex);
        qianming = findViewById(R.id.qianming);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username1=username.getText().toString();
                email1 = email.getText().toString();
                password1 = password.getText().toString();
                confirm1 =confirm.getText().toString();
                sex1 =sex.getText().toString();
                qianming1 =qianming.getText().toString();

                if (!confirm1.equals(password1))
                {
                    System.out.println(confirm1);
                    System.out.println(password1);
                    Toast.makeText(RegisterPage.this, "twice password are not the same, pleases try again", Toast.LENGTH_SHORT).show();
                }

                else {
                    Pattern pattern = Pattern
                            .compile("^(13[0-9]|15[0-9]|153|15[6-9]|180|18[23]|18[5-9])\\d{8}$");
                    Matcher matcher = pattern.matcher(email.getText());
                    if (matcher.matches()) {
                        // 开一个线程完成网络请求操作
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPost(username1+ "",
                                        email1 + "", password1+ "", sex1+ "", qianming1+ "");
                            }
                        }).start();
                    } else {
                        Toast.makeText(getApplicationContext(), "手机格式错误", Toast.LENGTH_LONG).show();
                    }




                }

            }
        });


    }

    class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    Toast.makeText(getApplicationContext(), "注册成功",
                            Toast.LENGTH_SHORT).show();
                    /*跳转到登录页面并把180.76.136.248号也传过去*/
                    Intent intent = new Intent();
                    intent.setClass(com.example.helloworldd.RegisterPage.this, LoginActivity.class);
                    startActivity(intent);
                    com.example.helloworldd.RegisterPage.this.finish(); //结束当前activity
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
                    //這是一個提示消息
                    Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    public void httpUrlConnPost(String name, String phone, String password,String sex,String qianming) {
        /*使用工具类生成随机的180.76.136.248号*/

        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Reigister");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
             urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(9000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            urlConnection.connect();
            JSONObject json = new JSONObject();

            json.put("name", URLEncoder.encode(name, "UTF-8"));
            json.put("phone", URLEncoder.encode(phone, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));
            json.put("sex", URLEncoder.encode(sex, "UTF-8"));
            json.put("qianming", URLEncoder.encode(qianming, "UTF-8"));
            String jsonstr = json.toString();
             OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("aa", urlConnection.getResponseCode() + "");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                Log.i("aa", "rjson=" + rjson);
                boolean result = rjson.getBoolean("json");
                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明注册成功，否则注册失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    myhander.sendEmptyMessage(1);
                    Log.i("用户：", "注册成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    Log.i("用户：", "注册失败");
                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
    }




}



