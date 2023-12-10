package com.example.helloworldd;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.helloworldd.adapter.SignRecordAdapter;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StudentSign extends AppCompatActivity {
    ListView listView ;
    List<SignRecord>signRecords;
    MyHander myHander=new MyHander();
    SignRecordAdapter adapter;
    Context mContext;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_sign);

        listView=findViewById(R.id.listView);
        listView.setVisibility(View.GONE);
        mContext=getApplicationContext();
        imageView=findViewById(R.id.imagee);

        Glide.with(this)
                .asGif()
                .load(R.drawable.stu2)
                .listener(new RequestListener<GifDrawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {return false;}
                    @Override
                    public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                        resource.setLoopCount(1);
                        resource.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                            @Override
                            public void onAnimationEnd(Drawable drawable) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        httpUrlConnPost(WeixinFragment.weixinItem.getGroupid().toString(),LoginActivity.nowUser.getPhone().toString());
                                        myHander.sendEmptyMessage(1);
                                        for (int i = 0; i < signRecords.size(); i++) {
                                            signRecords.get(i).getText();
                                            signRecords.get(i).getSignsuccess();
                                        }
                                    }
                                }).start();

                            }
                        });
                        return false;
                    }
                })
                .into(imageView);


    }

    public void httpUrlConnPost(String groupid,String usernumber) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/StudentSign");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(9000);//
            urlConnection.setUseCaches(false);
            urlConnection.setInstanceFollowRedirects(true);
            urlConnection.setReadTimeout(290000);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.connect();

            JSONObject json = new JSONObject();
            System.out.println("XCS"+groupid);
            json.put("groupid", URLEncoder.encode(groupid, "UTF-8"));
            json.put("usernumber", URLEncoder.encode(groupid, "UTF-8"));

            String jsonstr = json.toString();
            System.out.println(jsonstr);
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
                Gson gson = new Gson();
                signRecords = gson.fromJson(buffer.toString(), new TypeToken<ArrayList<SignRecord>>() {}.getType());
                Collections.sort(signRecords,idComparator);
                System.out.println(signRecords);
            }
        } catch (Exception r) {r.printStackTrace();}
    }

    Comparator<SignRecord> idComparator = new Comparator<SignRecord>() {
        @Override
        public int compare(SignRecord s1, SignRecord s2) {
            return s1.getId().compareTo(s2.getId());
        }
    };
    class MyHander extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    listView.setVisibility(View.VISIBLE);
                    adapter = new SignRecordAdapter(mContext, signRecords);
                    listView.setAdapter(adapter);
                    System.out.println(778899000);

            }
        }
    }


}