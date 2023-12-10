package com.example.helloworldd;

import static com.example.helloworldd.ContactListFragment.listfriend;
import static com.example.helloworldd.ContactListFragment.listpin;
import static com.example.helloworldd.LoginActivity.nowUser;
import static com.example.helloworldd.WeixinFragment.shouyeadapter;
import static com.example.helloworldd.WeixinFragment.shouyelist;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworldd.adapter.CreateGrouplistAdapter;
import com.example.helloworldd.pojo.User;
import com.example.helloworldd.tools.UserPin;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateGroup  extends AppCompatActivity {

    private MyHander myhander = new MyHander();


    public List<User> chooses=new ArrayList<>();

    private ListView listView;
    private SideBar sideBar;
    private ImageButton confirm;
    private EditText groupname;
    CreateGrouplistAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creategroup);

        listView = (ListView) findViewById(R.id.listView);
        sideBar = (SideBar) findViewById(R.id.side_bar);
        confirm=findViewById(R.id.confirm);
        groupname=findViewById(R.id.groupname);

        List<UserPin> listpin1=listpin;




        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooses=new ArrayList<>();
                List<Boolean> choosesIndex=adapter.getmChecked();
                for(int i=0;i<choosesIndex.size();i++){
                    if(choosesIndex.get(i))
                    {
                        System.out.println(listfriend.get(i));
                        chooses.add(listfriend.get(i));
                    }
                }
                System.out.println("选择:"+chooses+"bbbbbb");

                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpUrlConnPost(chooses);
                    }
                });
                thread1.start();
                /*等待网络请求线程完成*/
                try {
                    thread1.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        adapter = new CreateGrouplistAdapter (this.getApplicationContext(), listpin);
        listView.setAdapter(adapter);
        sideBar.setOnStrSelectCallBack(new SideBar.ISideBarSelectCallBack() {
            @Override
            public void onSelectStr(int index, String selectStr) {
                for (int i = 0; i < listpin.size(); i++) {
                    if (listpin.get(i).getName() == "新的朋友" || listpin.get(i).getName() == "群聊" ||
                            listpin.get(i).getName() == "标签" || listpin.get(i).getName() == "公众号"  )
                        continue;
                    if (selectStr.equalsIgnoreCase(listpin.get(i).getFirstLetter())) {
                        listView.setSelection(i); // 选择到首字母出现的位置
                        return;
                    }
                }
            }
        });
    }

    public void httpUrlConnPost(List<User> chooses) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/CreateGroup");
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

            Gson gson = new Gson();

            String s1 =gson.toJson(chooses);

            String nowuser=gson.toJson(nowUser);
            System.out.println("json的choose"+s1.toString());
            JSONObject json = new JSONObject();
            json.put("chooses", URLEncoder.encode(s1, "UTF-8"));
            json.put("groupname", URLEncoder.encode(groupname.getText().toString(), "UTF-8"));
            json.put("createUser", URLEncoder.encode(nowuser, "UTF-8"));
            String jsonstr = json.toString();
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr.toString());




            bw.flush();
            out.close();
            bw.close();
            Log.i("创建群聊", urlConnection.getResponseCode() + "");

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                InputStream in = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in));
                String str = null;
                StringBuffer buffer = new StringBuffer();
                while ((str = br.readLine()) != null) {
                    System.out.println("测试：" + str);
                    buffer.append(str);
                }
                in.close();
                br.close();
                JSONObject rjson = new JSONObject(buffer.toString());


                boolean result =rjson.getBoolean("json");
                System.out.println("json:===" + result);
                //如果服务器端返回的是true，则说明跳转180.76.136.248页成功，跳转180.76.136.248页失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    myhander.sendEmptyMessage(1);

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", groupname.getText().toString());

                    map.put("content", "");
                    map.put("time", System.currentTimeMillis());
//                    shouyeadapter.add(map);
                    Log.i("用户：", "跳转180.76.136.248页成功");
                    System.out.println("跳转180.76.136.248页成功");


                } else {
                    myhander.sendEmptyMessage(2);
                    System.out.println("跳转180.76.136.248页失败");
                    Log.i("用户：", "跳转180.76.136.248页失败");


                }
            } else {
                myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("aa", e.toString());
            System.out.println("创建群聊跳转180.76.136.248页异常");
            myhander.sendEmptyMessage(1);
        } finally {
            urlConnection.disconnect();
        }
    }

     class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，如果是1则说明跳转180.76.136.248页成功，如果是2说明跳转180.76.136.248页失败
            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "");
                    Toast.makeText(CreateGroup.this, "创建成功", Toast.LENGTH_SHORT).show();
                    shouyeadapter.setData(shouyelist);
                    Thread thread1 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            WeixinFragment.httpUrlConnPost(nowUser.getPhone());
                        }
                    });
                    thread1.start();
                    /*等待网络请求线程完成*/
                    try {
                        thread1.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    shouyeadapter.setData(shouyelist);



                    break;
                case 2:
                    Log.i("aa", msg.what + "");
            }

        }
    }



}


