package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.client;
import static com.example.helloworldd.LoginActivity.nowUser;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Map;

public class AddNewFreind extends AppCompatActivity {
    private TextView Username;
    private Button ADD;
    private MyHander myhander = new MyHander();

    void initviews()
    {
        ADD=findViewById(R.id.add);
        Username=findViewById(R.id.username);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_freind);
        initviews();
        ADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username1 = (String) Username.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            System.out.println("ACEESS DATA "+nowUser.getPhone());
                            httpUrlConnPost(nowUser.getPhone(),Username1);
                        }catch (Exception e ){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }
    public void httpUrlConnPost(String Host, String Guest) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/AddNewFriend");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
             urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(16000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            urlConnection.connect();
            JSONObject json = new JSONObject();
            json.put("host", URLEncoder.encode(Host, "UTF-8"));
            json.put("guest", URLEncoder.encode(Guest, "UTF-8"));
             String jsonstr = json.toString();
             OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("添加好友", urlConnection.getResponseCode() + "");

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
                Log.i("添加好友", "rjson=" + rjson);
                boolean result = rjson.getBoolean("json");
                System.out.println("json:===" + result);

                String name=rjson.getString("name");
                String id =rjson.getString("id");

                Map<String, String> map = new HashMap<String, String>();
                map.put(name, name);
                if (result) {
                    myhander.sendEmptyMessage(1);
                    System.out.println("添加成功");
                    Log.i("用户：", "添加成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("添加失败");
                    Log.i("用户：", "添加失败");
                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("添加好友", e.toString());
            System.out.println("添加异常");
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
    }

     class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             switch (msg.what) {
                case 1:
                    Log.i("添加好友", msg.what + "");
                    Toast.makeText(getApplicationContext(), "添加成功",
                            Toast.LENGTH_SHORT).show();
                    String str =Username.getText().toString();
                    com.example.helloworldd.pojo.Message message =new com.example.helloworldd.pojo.Message(com.example.helloworldd.pojo.Message.ADD_FRIEND,str,nowUser.getId()+"",str);
                    String str2 =Json2Str.toJsonString(message, com.example.helloworldd.pojo.Message.class);
                    client.send(str2);
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ContactListFragment.httpUrlConnPost(nowUser.getPhone());
                        }
                    });
                    thread1.start();
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:

                    Log.i("添加好友", msg.what + "");
                    new AlertDialog.Builder(AddNewFreind.this)
                            .setTitle("                   添加失败")
                            .setMessage("    用户名错误，请重新填写")
                            .setPositiveButton("确定", null)
                            .show();
            }
        }
    }


}