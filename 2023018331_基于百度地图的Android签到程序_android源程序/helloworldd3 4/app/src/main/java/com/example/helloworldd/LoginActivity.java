package com.example.helloworldd;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworldd.pojo.User;
import com.google.gson.Gson;

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
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity {
    public static User nowUser=new User();
    public static MyWebSocketClient client;
    public static boolean hasShownWelcome = false; // 静态变量

    private MyHander myhander = new MyHander();
    TextView Username ;
    TextView Password ;
    TextView Forgetpassword ;
    Button   Sign_in;
    Button   Sign_up;
    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ChangeYourInfo.hasShownLogin){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_login);

        Username =findViewById(R.id.Username);
        Password =findViewById(R.id.Password);
        Forgetpassword=findViewById(R.id.Forgetpassword);
        Sign_in=findViewById(R.id.Sign_in);
        Sign_up=findViewById(R.id.Sign_up);



        Forgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ResetPwdPage.class));
                finish();
            }
        });
        Sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = Username.getText().toString();
                password = Password.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            httpUrlConnPost(username + "",
                                    password + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        Sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,RegisterPage.class));
            }
        });
    }
    public void httpUrlConnPost(String phone, String password) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Login");urlConnection = (HttpURLConnection) url.openConnection();urlConnection.setConnectTimeout(9000);urlConnection.setUseCaches(false);urlConnection.setReadTimeout(9000);urlConnection.setDoInput(true);urlConnection.setDoOutput(true);urlConnection.setRequestMethod("POST");urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");urlConnection.connect();JSONObject json = new JSONObject();
            json.put("number", URLEncoder.encode(phone, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));
            String jsonstr = json.toString();
            OutputStream out = urlConnection.getOutputStream();BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));bw.write(jsonstr);bw.flush();out.close();bw.close();
            Log.i("登陆", urlConnection.getResponseCode() + "bb");
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {buffer.append(str);}in.close();br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                Log.i("登陆", "rjson=" + rjson);
                boolean result = rjson.getBoolean("json");
                System.out.println("json:===" + result);
                if (result) {
                    myhander.sendEmptyMessage(1);
                    nowUser.setPhone(phone);
                    nowUser.setPassword(password);
                    nowUser.setName(rjson.getString("name"));
                    nowUser.setQianming(rjson.getString("qianming"));
                    nowUser.setSex(rjson.getString("sex"));
                    nowUser.setId(rjson.getInt("id"));}
                 else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("登录失败");
                    Log.i("登陆：", "登录失败");
                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aabb", e.toString());
            System.out.println();
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
        }

         class MyHander extends Handler {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {


             switch (msg.what) {
                case 1:
                    Log.i("登陆", msg.what + "");
                    Toast.makeText(getApplicationContext(), "登录成功",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent (com.example.helloworldd.LoginActivity.this, com.example.helloworldd.MainActivity.class);
                    String url="ws://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/websocket/"+nowUser.getPhone();
                    System.out.println(url);


                    client = new MyWebSocketClient(url);
//                    com.example.helloworldd.pojo.Message message =new com.example.helloworldd.pojo.Message(com.example.helloworldd.pojo.Message.LOGIN,"123",nowUser.getId()+"","0");
//                    System.out.println("Message: " + message);
//                    String str2 = JSON.toJSONString(message);
//                    System.out.println(str2);
//                    client.send(str2);

                    HashMap<String,String> hashMap =new HashMap();
                    hashMap.put("type","LOGIN");
                    hashMap.put("user","137");
                    String str=new Gson().toJson(hashMap).toString();
                    JSONObject json = new JSONObject();
                    json.put("HASHMAP", URLEncoder.encode(str, "UTF-8"));
                    client.send(str);
                    System.out.println(str+"----");
                    ChangeYourInfo.hasShownLogin=true;
                    System.out.println("7777777777");

                    startActivity(intent);
                    com.example.helloworldd.LoginActivity.this.finish();
                    break;
                case 2:
                    Log.i("登陆", msg.what + "");
                    new AlertDialog.Builder(com.example.helloworldd.LoginActivity.this)
                            .setTitle("                   登录失败")
                            .setMessage("    用户名或密码错误，请重新填写")
                            .setPositiveButton("确定", null)
                            .show();
            }
        }catch (Exception e){e.printStackTrace();}
    }




}}