package com.example.helloworldd;

import static cn.smssdk.SMSSDK.getSupportedCountries;
import static cn.smssdk.SMSSDK.getVerificationCode;
import static cn.smssdk.SMSSDK.submitVerificationCode;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.mob.MobSDK;

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

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ResetPwdPage extends AppCompatActivity {
    private MyHander myhander = new MyHander();
    EditText verifyphone;
    EditText verifycode;
    EditText Fpassword;
    EditText Fretype;
    Button   verify;
    Button   verifyButton;

    String phone;
    String code ;
    String password ;
    String retype ;

    private boolean tag = true;
    //每次验证请求需要间隔60S
    private int i=60;


    private final String TAG="--ResetPwdPage--";
    //app key和app secret 需要填自己应用的对应的！这里只是我自己创建的应用。
    private final String appKey="37062e645e81e";
    private final String appSecret="37d69d433ca58486b3d75ace0fff8470";
    private EventHandler eh;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    //客户端验证成功，可以进行注册,返回校验的手机和国家代码phone/country

                    Toast.makeText(ResetPwdPage.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUrlConnPost(verifyphone.getText().toString(), Fpassword.getText().toString());
                        }
                    }).start();
                    break;
                case 1:
                    //获取验证码成功
                    Toast.makeText(ResetPwdPage.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //返回支持发送验证码的国家列表
                    Toast.makeText(ResetPwdPage.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public void initView()
    {
        verifyphone=findViewById(R.id.verifyphone);
        verifycode=findViewById(R.id.verifycode);
        Fpassword=findViewById(R.id.Fpassword);
        Fretype=findViewById(R.id.Fretype);
        verify=findViewById(R.id.verify);
        verifyButton=findViewById(R.id.verifyButton);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd_page);
        initView();
        ImageView imageView=findViewById(R.id.registerpic);
        Glide.with(this).load(R.drawable.register).into(imageView);
        MobSDK.submitPolicyGrantResult(true);
        MobSDK.init(this,appKey,appSecret);
        eh=new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        Message msg = new Message();
                        msg.arg1 = 0;
                        msg.obj = data;
                        handler.sendMessage(msg);
                        Log.d(TAG, "提交验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Message msg = new Message();
                        //获取验证码成功
                        msg.arg1 = 1;
                        msg.obj = "获取验证码成功";
                        handler.sendMessage(msg);
                        Log.d(TAG, "获取验证码成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        Message msg = new Message();
                        //返回支持发送验证码的国家列表
                        msg.arg1 = 2;
                        msg.obj = "返回支持发送验证码的国家列表";
                        handler.sendMessage(msg);
                        Log.d(TAG, "返回支持发送验证码的国家列表");
                    }
                } else {
                    Message msg = new Message();
                    //返回支持发送验证码的国家列表
                    msg.arg1 = 3;
                    msg.obj = "验证失败";
                    handler.sendMessage(msg);
                    Log.d(TAG, "验证失败");
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

        verifyButton.setClickable(false);


        verify.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                phone =verifyphone.getText().toString();
                code  =verifycode.getText().toString();
                password = Fpassword.getText().toString();
                retype = Fretype.getText().toString();


                if (password .equals(retype)) {
                    if (code.equals("")){
                        Toast.makeText(ResetPwdPage.this,"验证码不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                        //填写了验证码，进行验证
                        submitVerificationCode("86", phone, code);
                    }
                }

                else  {
                    Toast.makeText(ResetPwdPage.this, "password not the same ", Toast.LENGTH_SHORT).show();
                }
            }
        });
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone =  verifyphone.getText().toString();


                if(phone.equals("")){
                    Toast.makeText(ResetPwdPage.this,"手机号不能为空",Toast.LENGTH_SHORT).show();
                }else{
                    //填写了手机号码
                    if(isMobileNO(phone)){
                        //如果手机号码无误，则发送验证请求
                        verifyButton.setClickable(true);
                        changeBtnGetCode();
                        getSupportedCountries();
                        getVerificationCode("86", phone);
                    }else{
                        //手机号格式有误
                        Toast.makeText(ResetPwdPage.this,"手机号格式错误，请检查",Toast.LENGTH_SHORT).show();
                    }
                }}
        });

    }


    private boolean isMobileNO(String phone) {
       /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(phone)) return false;
        else return phone.matches(telRegex);
    }
    private void changeBtnGetCode() {

        Thread thread = new Thread() {
            @Override
            public void run() {
                if (tag) {
                    while (i > 0) {
                        i--;
                        //如果活动为空
                        if (ResetPwdPage.this == null) {
                            break;
                        }

                        ResetPwdPage.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                verifyButton.setText("获取验证码(" + i + ")");
                                verifyButton.setClickable(false);
                            }
                        });

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tag = false;
                }
                i = 60;
                tag = true;
                if (ResetPwdPage.this != null) {
                    ResetPwdPage.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            verifyButton.setText("获取验证码");
                            verifyButton.setClickable(true);
                        }
                    });
                }
            }
        };
        thread.start();
    }



    public void httpUrlConnPost(String phone, String password) {
        /*使用工具类生成随机的180.76.136.248号*/

        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Forget");
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

            json.put("phone", URLEncoder.encode(phone, "UTF-8"));
            json.put("password", URLEncoder.encode(password, "UTF-8"));
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
                    Log.i("用户：", "修改密码成功");
                } else {
                    myhander.sendEmptyMessage(2);
                    Log.i("用户：", "修改密码失败");
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


    class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    Toast.makeText(getApplicationContext(), "修改密码成功",
                            Toast.LENGTH_SHORT).show();
                    /*跳转到登录页面并把180.76.136.248号也传过去*/
                    Intent intent = new Intent();
                    intent.setClass(com.example.helloworldd.ResetPwdPage.this, LoginActivity.class);
                    startActivity(intent);
                    com.example.helloworldd.ResetPwdPage.this.finish(); //结束当前activity
                    break;
                case 2:
                    Log.i("aa", msg.what + "");
                    //這是一個提示消息
                    Toast.makeText(getApplicationContext(), "修改密码失败", Toast.LENGTH_LONG).show();
            }
        }
    }

}