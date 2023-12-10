package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworldd.adapter.SignListAdapter;
import com.example.helloworldd.pojo.SignInfo;
import com.example.helloworldd.pojo.SignRecord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignGroup extends AppCompatActivity {

    String ownerphone;
    boolean isOwner;
    String title;
    TextView biaoti;
    Button faqiqiandao;
    Button bianjiqiandao;
    Button tongjiqiandao;
    TextView qiandaobiaoti;
    TextView qishishijian;
    TextView jiezhishijian;
    Button qiandao;
    Button history;
    public  static ListView listView;
    String groupid;
    public  static List<SignInfo>nowSignInfos;
    public  static List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
    public  static SignListAdapter signListAdapter;
    public  static Context mContext;
    public  static SignInfo nowSignInfo;
    public  static SignInfo signInfo2;
    public  static SignInfo signInfoNum = new SignInfo();
public static int NUM=0;
    public static boolean isClickAble;
    public static MyHander myHander ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_group);
        initData();
        initview();
        mContext=SignGroup.this;
        if (isOwner){
            initOwnerview();
            faqiqiandao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignGroup.this,AddASign.class));
                    AddASign.shown=true;
                    System.out.println("SGP"+AddASign.shown);
                }
            });
            tongjiqiandao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignGroup.this,SignStatic.class));
                }
            });
            bianjiqiandao.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(SignGroup.this,EditSign.class));
                }
            });
            signListAdapter = new SignListAdapter(list2,SignGroup.this);
            listView.setAdapter(signListAdapter);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Looper.prepare();
//                        while (true)
                        {
                            String signidd =signInfoNum.getSignid();
                            httpUrlConnPost4(signidd);
                            Thread.sleep(10000);
                            System.out.println("GET NUM "+NUM);
                            nowSignInfos.get(nowSignInfos.size()-1).setSignnums(NUM+"");
                            list2.get(list2.size()-1).put("SIGNNUM",NUM+"");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    signListAdapter.notifyDataSetChanged();
                                }
                            });

                        }

                    }catch (Exception e){e.printStackTrace();}

                }
            }).start();
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String signid =nowSignInfos.get(i).getSignid();

                    signInfo2=nowSignInfos.get(i);
                    System.out.println("S I G N G R OUP"+signInfo2);
                    DetailSign.signRecord.setSignid(signid);
                    DetailSign.I=i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUrlConnPost3(signid);
                        }
                    }).start();

                    startActivity(new Intent(SignGroup.this,WaitFile2.class));
                    System.out.println("S I G N G R O U P "+"++++++++++++++++++++++++++++++++");}});}
        else{

            initUserview();


        }


    }
    void initData()
    {
        System.out.println("S I G---"+WeixinFragment.weixinItem.getCreateusernumber());
        ownerphone = WeixinFragment.weixinItem.getCreateusernumber();
        System.out.print(nowUser.getPhone());
        isOwner =ownerphone.equals(nowUser.getPhone());
        title=WeixinFragment.weixinItem.getTitle();
        groupid=WeixinFragment.weixinItem.getGroupid();
        AddASign.groupid=groupid;
        System.out.println("IN SIGN GROUP"+"groupid");

    }
    void initview(){
        biaoti=findViewById(R.id.title1);
        faqiqiandao  =findViewById(R.id.signButton);
        bianjiqiandao  =findViewById(R.id.Editsign);
        tongjiqiandao  =findViewById(R.id.signStatic);
        qiandaobiaoti=findViewById(R.id.signtitle);
        qishishijian =findViewById(R.id.signStartTime);
        jiezhishijian=findViewById(R.id.signEndTime);
        qiandao      =findViewById(R.id.signIt);
        listView=findViewById(R.id.qiandaolist);
        biaoti.setText(WeixinFragment.weixinItem.getTitle());
//        history=findViewById(R.id.history);

    }
    void initOwnerview(){
        qiandaobiaoti.setVisibility(View.GONE);
        qishishijian.setVisibility(View.GONE);
        jiezhishijian.setVisibility(View.GONE);
        qiandao.setVisibility(View.GONE);
//        history.setVisibility(View.GONE);
    }
    void initUserview(){
        isOut();
        listView.setVisibility(View.GONE);
        faqiqiandao.setVisibility(View.GONE);
        tongjiqiandao.setVisibility(View.GONE);
        bianjiqiandao.setVisibility(View.GONE);
        qiandaobiaoti.setText(nowSignInfo.getSigntext());
        qishishijian.setText(nowSignInfo.getSignstarttime());
        jiezhishijian.setText(nowSignInfo.getSignmins());
        nowSignInfo.getSignstarttime();
        qiandao.setEnabled(isClickAble);
        qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignGroup.mContext,BaiduActivity.class));
            }
        });
//        history.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(mContext,HistorySign.class));
//            }
//        });

    }
    static boolean isOut(){
        System.out.println(nowSignInfo);
        String str1 =nowSignInfo.getSignstarttime();
        String str2 =nowSignInfo.getSignmins();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(str1);
        System.out.println(str2);
        Date date = null;
        try {
            date = df.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long timee =date.getTime();
        System.out.println("S I G N G R OUP"+timee);
        timee+=Integer.parseInt(str2)*1000*60;
        isClickAble = timee>System.currentTimeMillis();
        System.out.println("S I G N G R OUP"+"获取到的"+timee);
        System.out.println("S I G N G R OUP"+"现在的"+System.currentTimeMillis());
        return isClickAble;
    }

// 获取历史签到信息
    public static void httpUrlConnPost(String groupid) {
        HttpURLConnection urlConnection = null;
        URL url;
        try { 
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/SignGroup");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(9000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();//

            JSONObject json = new JSONObject();
            System.out.println("S I G N G R OUP"+groupid+"778899");
            json.put("groupid", URLEncoder.encode(groupid, "UTF-8"));
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
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
                Type listOfMyClassObject = new TypeToken<ArrayList<SignInfo>>() {
                }.getType();
                Gson gson = new Gson();
                nowSignInfos = gson.fromJson(buffer.toString(), listOfMyClassObject);
                System.out.print(nowSignInfos);
                list2=new ArrayList<>();
                for (int i = 0; i < nowSignInfos.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("SIGNNAME", nowSignInfos.get(i).getSigntext());
                    map.put("SIGNTIME", nowSignInfos.get(i).getSignstarttime());
                    map.put("SIGNLIMIT", nowSignInfos.get(i).getSignmins());
                    map.put("SIGNNUM", nowSignInfos.get(i).getSignnums());
                    if (i==nowSignInfos.size()-1){signInfoNum=nowSignInfos.get(i);}
                    list2.add(map);
                }
                System.out.println("S I G N G R OUP"+"获取成功");
            }
        }catch (Exception r){r.printStackTrace();}
    }
// 监控是否有新的签到
    public static void httpUrlConnPost2(String groupid) {
        HttpURLConnection urlConnection = null;
        URL url;
        try { 
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/IsANewSign");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(5000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();//

            JSONObject json = new JSONObject();
            System.out.println("groupid "+groupid+"");
            json.put("groupid", URLEncoder.encode(groupid, "UTF-8"));
           // json.put("userid", URLEncoder.encode(userid, "UTF-8"));
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
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();
               nowSignInfo =new Gson().fromJson(buffer.toString(),SignInfo.class);
                System.out.print("监控是否有新的签到"+nowSignInfo);
                System.out.println(buffer.toString());
                System.out.println("监控是否有新的签到"+"获取成功");
            }
        }catch (Exception r){r.printStackTrace();}
    }
    // 监控签到的详细情况
    public static void httpUrlConnPost3(String signid) {
        HttpURLConnection urlConnection = null;
        URL url;
        try { 
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/DetailSign");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(9000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();//

            JSONObject json = new JSONObject();
            System.out.println("S I G N G R OUP"+signid+"");
            json.put("signid", URLEncoder.encode(signid, "UTF-8"));
            // json.put("userid", URLEncoder.encode(userid, "UTF-8"));
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
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();

                Type listOfMyClassObject = new TypeToken<ArrayList<SignRecord>>() {}.getType();
                Gson gson = new Gson();
                DetailSign.signRecords = gson.fromJson(buffer.toString(), listOfMyClassObject);
                //System.out.print(DetailSign.signRecords);
                DetailSign.list=new ArrayList<>();
                for (int i = 0; i < DetailSign.signRecords.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("signer", DetailSign.signRecords.get(i).getUsername());
                    map.put("signsu", DetailSign.signRecords.get(i).getSignsuccess());
                    map.put("signtimeee", DetailSign.signRecords.get(i).getSigntime());
                    DetailSign.list.add(map);
                    System.out.println("S I G N G R OUP"+map+"---");
                }
                System.out.println("S I G N G R OUP"+"获取成功");}
        }catch (Exception r){r.printStackTrace();}
    }

    public void httpUrlConnPost4(String signid) {
        HttpURLConnection urlConnection = null;
        URL url;
        try { 
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/GetSignNum");
            urlConnection = (HttpURLConnection) url.openConnection(); 
            urlConnection.setConnectTimeout(9000);//  
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(290000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();//

            JSONObject json = new JSONObject();
            System.out.println("GET NUM"+signid);
            json.put("signid", URLEncoder.encode(signid, "UTF-8"));

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
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                in.close();
                br.close();

                JSONObject rjson = new JSONObject(buffer.toString());
                int i = rjson.getInt("json");
                NUM=rjson.getInt("signnums");
                System.out.println("GET NUM"+"获取成功 " + NUM);

            }
        } catch (Exception r) {
            r.printStackTrace();
        }
    }

    static class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "123111");
                    signListAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    Log.i("aabb", msg.what + "177777");
            }
        }

    }
}