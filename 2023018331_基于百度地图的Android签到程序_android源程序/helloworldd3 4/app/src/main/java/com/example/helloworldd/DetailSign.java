package com.example.helloworldd;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworldd.adapter.DetailSignAdapter;
import com.example.helloworldd.adapter.SignListAdapter;
import com.example.helloworldd.pojo.SignRecord;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailSign extends AppCompatActivity {
    public static List<Map<String, Object>> list ;
    public static DetailSignAdapter detailSignAdapter;
    public static Context mContext;
    public static SignRecord signRecord=new SignRecord();
    public static List<SignRecord> signRecords;
    public static TextView textView;
    public static ListView listView;
    public static MyHander myHander = new MyHander();
    public static Button yanshi;
    public static Button xiugai;
    public static Button qingjia;
    public static Button jieshu;
    public static Button shanchu;
    public static boolean x=false;
    public static int I=0;
Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        },3550);
        setContentView(R.layout.activity_detail_sign);
        mContext = DetailSign.this;
        initview();
        initData();
        detailSignAdapter = new DetailSignAdapter(DetailSign.list, mContext);
        listView.setAdapter(detailSignAdapter);
        yanshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSign.this);
                builder.setTitle("延时多长时间？");
                builder.setTitle(Html.fromHtml("<font color='#FF6200EE'>延时多长时间？</font>"));

                final EditText input = new EditText(DetailSign.this);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userInput = input.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPostOP(0,signRecord.getSignid(),userInput);
                            }}).start();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();







            }
        });
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSign.this);
                builder.setTitle("将签到名称修改为？");
                builder.setTitle(Html.fromHtml("<font color='#FF6200EE'>将签到名称修改为？</font>"));

                final EditText input = new EditText(DetailSign.this);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userInput = input.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPostOP(1,signRecord.getSignid(),userInput);
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        qingjia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSign.this);
                builder.setTitle("输入请假学生的手机号码");
                builder.setTitle(Html.fromHtml("<font color='#FF6200EE'>输入请假学生的手机号码？</font>"));

                final EditText input = new EditText(DetailSign.this);
                builder.setView(input);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String userInput = input.getText().toString();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPostOP(2,signRecord.getSignid(),userInput);
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        jieshu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSign.this);
                builder.setTitle("确定要立即结束该签到吗？");
                builder.setTitle(Html.fromHtml("<font color='#FF6200EE'>确定要立即结束该签到吗？</font>"));

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPostOP(3,signRecord.getSignid(),"1");
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailSign.this);
                builder.setTitle("确定要删除该签到吗？");
                builder.setTitle(Html.fromHtml("<font color='#FF6200EE'>确定要删除该签到吗？</font>"));

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                httpUrlConnPostOP(4,signRecord.getSignid(),"1");
                            }
                        }).start();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.show();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true)
                    {
                    Thread.sleep(1000);
                    System.out.println("qqq"+x);
                    if (x){
                        myHander.sendEmptyMessage(3);
                        startActivity(new Intent(DetailSign.this,SignGroup.class));
                        x=false;
                    break;}
                } }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        SignGroup.httpUrlConnPost3(SignGroup.signInfo2.getSignid());
                        Thread.sleep(10000);
                        myHander.sendEmptyMessage(1);
                    }catch (Exception r){r.printStackTrace();}
                }
            }
        }).start();
    }

    static void initData() {
        list=new ArrayList<Map<String, Object>>();
        for (int i = 0; i < signRecords.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("signer", signRecords.get(i).getUsername());
            map.put("signsu", signRecords.get(i).getSignsuccess());
            map.put("signtimeee", signRecords.get(i).getSigntime());
            list.add(map);
            //System.out.println(map);
        }
        textView.setText(SignGroup.signInfo2.getSigntext());
    }

    void initview() {
        textView = findViewById(R.id.signnamee);
        listView = findViewById(R.id.listviewnames);
        yanshi=findViewById(R.id.yanshi);
        xiugai=findViewById(R.id.xiugai);
        qingjia=findViewById(R.id.qingjia);
        jieshu=findViewById(R.id.jieshu);
        shanchu=findViewById(R.id.shanchu);

    }
    public static void httpUrlConnPostOP(int i ,String signid,String content) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Signop");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(9000);
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();

            JSONObject json = new JSONObject();

            System.out.println("signid"+signid+"");
            if (i==0)
            {
                json.put("type",i);
                json.put("signid", URLEncoder.encode(signid, "UTF-8"));
                json.put("time", URLEncoder.encode(content, "UTF-8"));
//                SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
            }
            if (i==1)
            {
                json.put("type",i);
                json.put("signid", URLEncoder.encode(signid, "UTF-8"));
                json.put("title", URLEncoder.encode(content, "UTF-8"));
//                SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
            }
            if (i==2)
            {
                json.put("type",i);
                json.put("signid", URLEncoder.encode(signid, "UTF-8"));
                json.put("phone", URLEncoder.encode(content, "UTF-8"));
//                SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
            }
            if (i==3)
            {
                json.put("type",i);
                json.put("signid", URLEncoder.encode(signid, "UTF-8"));
                SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
//                json.put("time", URLEncoder.encode(time, "UTF-8"));
            }
            if (i==4)
            {
                json.put("type",i);
                json.put("signid", URLEncoder.encode(signid, "UTF-8"));
//                SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
//                json.put("time", URLEncoder.encode(time, "UTF-8"));


            }

            String jsonstr = json.toString();
            System.out.println(jsonstr+"xxxcv");
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("DSOP", urlConnection.getResponseCode() + "");

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
                Log.i("DSOP", "rjson=" + rjson);
                int i1 = rjson.getInt("json");
                if (i==4&&i1!=0){x=true;}
                System.out.println("x="+x);
               if (i1!=0){myHander.sendEmptyMessage(1);}else {myHander.sendEmptyMessage(2);} }}catch (Exception r){r.printStackTrace();}}

    static class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);


            switch (msg.what) {
                case 1:
                    Log.i("DSOP", msg.what + "  ");
                    detailSignAdapter = new DetailSignAdapter(DetailSign.list, mContext);
                    listView.setAdapter(DetailSign.detailSignAdapter);
//                    Toast.makeText(mContext,"操作成功1",Toast.LENGTH_SHORT).show();
//                    SignGroup.httpUrlConnPost(SignGroup.nowSignInfo.getGroupid());
                    break;
                case 2:
                    Log.i("DSOP", msg.what + "  ");
                    Toast.makeText(mContext,"操作失败",Toast.LENGTH_SHORT).show();
                case 3:
                    SignGroup.list2.remove(I);
                    SignGroup.signListAdapter = new SignListAdapter(SignGroup.list2,SignGroup.mContext);
                    SignGroup.listView.setAdapter(SignGroup.signListAdapter);

            }
        }

    }
}