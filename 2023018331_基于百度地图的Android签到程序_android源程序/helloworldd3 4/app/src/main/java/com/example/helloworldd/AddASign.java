package com.example.helloworldd;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.helloworldd.adapter.SignListAdapter;
import com.example.helloworldd.pojo.SignInfo;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddASign extends AppCompatActivity {
    EditText mins;
    EditText textt;
    Button signnow;
    MyHander myHander = new MyHander();
    static String min;
    static String text;
    static String timee;
    static String groupid;
    static String signidA;
    private static Context mContext = null;
    static boolean x=false;
    static boolean shown=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_asign);
        initview();
//        if (!shown){startActivity(new Intent(AddASign.this,SignGroup.class));
//            System.out.println("QQX");}
        System.out.println(signnow);

        mContext = AddASign.this;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//不能new一个，得系统服务生成，需要这个语句。
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        shown=false;
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,//指定GPS定位的提供者
                1000,//间隔时间
                1,//位置间隔1米
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {

                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                }
        );

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);//获取最新的定位信息
        locationUpdates(location);//将获取到的定位信息传递到Location当中
        shown=false;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(1000);
                        if (x){startActivity(new Intent(AddASign.this,SignGroup.class));break;}
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }


    public void initview() {
        mins = findViewById(R.id.mins);
        signnow = findViewById(R.id.signnows);
        textt = findViewById(R.id.textt);
        System.out.println("111111111111111");
        signnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                min = mins.getText().toString();
                text = textt.getText().toString();
                System.out.println(text);
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                timee = formatter.format(date);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpUrlConnPost(groupid, text, timee, min);
                        System.out.println("SIGNNOW");
                        System.out.println("SIGNNOW");
                        System.out.println("SIGNNOW");
                        System.out.println("SIGNNOW");
                    }
                }).start();

            }
        });
    }
    public void httpUrlConnPost(String groupid, String signtext, String signtime, String limit) {
        HttpURLConnection urlConnection = null;
        URL url;
        try { 
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/AddASign");
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
            System.out.println("ADD A SIGN"+groupid);
            json.put("groupid", URLEncoder.encode(groupid, "UTF-8"));
            json.put("signtext", URLEncoder.encode(signtext, "UTF-8"));
            json.put("signstarttime", URLEncoder.encode(signtime, "UTF-8"));
            json.put("signmins", URLEncoder.encode(limit, "UTF-8"));
            json.put("jingdu",URLEncoder.encode(String.valueOf(BaiduActivity.desLongitude), "UTF-8"));
            json.put("weidu",URLEncoder.encode(String.valueOf(BaiduActivity.desLatitude), "UTF-8"));

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
                signidA=rjson.getInt("signid")+"";
                System.out.println("ADD A SIGN"+"获取成功 " + i);
                myHander.sendEmptyMessage(1);
                x=true;
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
                    Log.i("aa", msg.what + "");
                    Toast.makeText(mContext, "添加成功", Toast.LENGTH_SHORT).show();
                    SignInfo signInfo= new SignInfo();
                    signInfo.setSigntext(text);
                    signInfo.setSignmins(min);
                    signInfo.setSignstarttime(timee);
                    signInfo.setSignnums("0");
                    signInfo.setSignid(signidA);
                    System.out.println("THERE "+signidA);
                    System.out.println("THERE "+signInfo);
                    SignGroup.nowSignInfos.add(signInfo);
                    SignGroup.signInfoNum=signInfo;
                    Map map = new HashMap();
                    map.put("SIGNNAME", text);
                    map.put("SIGNTIME", timee);
                    map.put("SIGNLIMIT", min);
                    map.put("SIGNNUM", 0);
                    if (!SignGroup.list2.contains(map)){
                    SignGroup.list2.add(map);}
                    SignGroup.signListAdapter = new SignListAdapter(SignGroup.list2,SignGroup.mContext);
                    SignGroup.listView.setAdapter(SignGroup.signListAdapter);break;

                case 2:
                    Log.i("aa", msg.what + "");
            }
        }


    }
    public void locationUpdates(Location location){
        if(location!=null){
            BaiduActivity.desLatitude=location.getLatitude();
            BaiduActivity.desLongitude=location.getLongitude();
            System.out.println("ADD A SIGN"+BaiduActivity.desLatitude);
            System.out.println("ADD A SIGN"+ BaiduActivity.desLongitude);

        }else{
            System.out.println("ADD A SIGN"+"没有获取到gps信息");//如果你经常显示，请把代码在手机上跑一下然后去室外去跑，建议关闭流量WiFi，经实证，是会得到经纬度信息的。
        }
    }
}