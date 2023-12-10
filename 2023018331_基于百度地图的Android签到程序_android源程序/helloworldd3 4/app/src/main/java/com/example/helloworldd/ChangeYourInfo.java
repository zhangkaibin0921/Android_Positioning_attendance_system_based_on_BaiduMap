package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangeYourInfo extends AppCompatActivity {
    EditText Username;
    EditText UserSex;
    EditText UserSign;
    EditText UserOldPass;
    EditText UserNewPass;
    EditText UserNewPassA;

    Button    ok;
    Button    ok2;
   Button quit;
    TextView cancel;

    String Username1;
    String UserSex1;
    String UserSign1;
    String UserOldPass1;
    String UserNewPass1;
    String UserNewPassA1;
    MyHander myhander = new MyHander();
    public static boolean hasShownLogin=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_your_info);
        initView();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTexts();
                if (!nowUser.getPassword().toString().equals(UserOldPass1)){
                    Toast.makeText(ChangeYourInfo.this,"旧密码错误",Toast.LENGTH_SHORT).show();
                }
                else if (!UserNewPass1.equals(UserNewPassA1))
                {
                    Toast.makeText(ChangeYourInfo.this,"新密码输入不一致",Toast.LENGTH_SHORT).show();

                }else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            User user = new User();
                            user.setPassword(UserNewPass1);
                            user.setPhone(nowUser.getPhone());
                            boolean result = httpUrlConnPost(user);
                            System.out.println("个人密码修改结果是 "+result);
                            if (result){nowUser.setPassword(UserNewPass1);}

                        }
                    }).start();
                }
            }
        });
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeYourInfo.hasShownLogin=false;
                Intent intent=new Intent(ChangeYourInfo.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        ok2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTexts();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User user = new User();
                        user.setName(Username1);
                        System.out.print("当前用户ID"+nowUser.getId());
                        user.setPhone(nowUser.getPhone());
                        user.setQianming(UserSign1);
                        user.setSex(UserSex1);
                        boolean result = httpUrlConnPost(user);
                        System.out.println("个人信息修改结果是 "+result);
                        if (result){nowUser.setName(Username1);nowUser.setQianming(UserSign1);nowUser.setSex(UserSex1);}
                    }
                }).start();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangeYourInfo.this,MainActivity.class));
            }
        });

    }
    void initView()
    {

        Username = findViewById(R.id.username);
        UserSex = findViewById(R.id.usersex);
        UserSign = findViewById(R.id.usersign);
        UserOldPass = findViewById(R.id.useroldpass);
        UserNewPass = findViewById(R.id.usernewpass);
        UserNewPassA = findViewById(R.id.usernewpassA);
        ok=findViewById(R.id.ok);
        cancel=findViewById(R.id.back);
        ok2=findViewById(R.id.ok2);
        setText();
        quit=findViewById(R.id.quit);


    }
    void getTexts(){
        Username1=Username.getText().toString();
        UserSex1=UserSex.getText().toString();
        UserSign1=UserSign.getText().toString();
        UserOldPass1=UserOldPass.getText().toString();
        UserNewPass1=UserNewPass.getText().toString();
        UserNewPassA1=UserNewPassA.getText().toString();

    }
    void setText(){
        Username.setText(nowUser.getName().toString());
        if(nowUser.getSex()!=null){
            UserSex.setText(nowUser.getSex().toString());
        }
        else{
            UserSex.setText("");
        }

        if(nowUser.getQianming()!=null){
            UserSign.setText(nowUser.getQianming().toString());
        }
        else{
            UserSign.setText("");
        }


    }
    public boolean httpUrlConnPost(User user) {
        HttpURLConnection urlConnection = null;
        URL url;
        boolean result=false;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/ChangeInfo");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
             urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(19000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/json;charset=UTF-8");
            urlConnection.connect();



            JSONObject json = new JSONObject();
            Gson gson =new Gson();
            String JsonStr = gson.toJson(user,User.class);

            json.put("user", URLEncoder.encode(JsonStr, "UTF-8"));


            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(json.toString());
            bw.flush();
            out.close();
            bw.close();
            Log.i("aa", urlConnection.getResponseCode() + "");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());
                Log.i("aa", "rjson=" + rjson);
                result = rjson.getBoolean("json");
                System.out.println("json:===" + result);


                if (result) {
                    System.out.println("修改成功");myhander.sendEmptyMessage(1);} else {myhander.sendEmptyMessage(2);System.out.println("修改失败");}} else {myhander.sendEmptyMessage(2);}
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            System.out.println();
            myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
        return result;
    }

     class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
             switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    Toast.makeText(getApplicationContext(), "修改成功",
                            Toast.LENGTH_SHORT).show();
                    nowUser.setName(Username1);
                    nowUser.setSex(UserSex1);
                    nowUser.setQianming(UserSign1);
                    SelfFragment.mSignName.setRightDesc(nowUser.getQianming());
                    SelfFragment.mSex.setRightDesc(nowUser.getSex());
                    SelfFragment.mNickName.setRightDesc(nowUser.getName());
                    SelfFragment.mUserName.setText(nowUser.getName());
                    Intent intent = new Intent (com.example.helloworldd.ChangeYourInfo.this, com.example.helloworldd.MainActivity.class);
                    startActivity(intent);
                    com.example.helloworldd.ChangeYourInfo.this.finish();
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
                    new AlertDialog.Builder(com.example.helloworldd.ChangeYourInfo.this)
                            .setTitle("                   修改失败")
                            .setMessage("           修改失败")
                            .setPositiveButton("确定", null)
                            .show();
            }
        }
    }
}