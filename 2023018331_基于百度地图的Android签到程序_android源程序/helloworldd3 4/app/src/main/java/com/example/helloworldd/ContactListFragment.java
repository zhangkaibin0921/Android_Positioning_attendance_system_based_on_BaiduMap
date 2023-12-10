package com.example.helloworldd;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.helloworldd.adapter.ContactListAdapter;
import com.example.helloworldd.pojo.User;
import com.example.helloworldd.tools.UserPin;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ContactListFragment extends Fragment {
    public static int flag;
    public static ContactListAdapter adapter;




    public static List<User> listfriend=new ArrayList<>();

    public static List<UserPin> listpin=new ArrayList<>();


    /* 声明组件*/
    private ListView listView;
    private SideBar sideBar;
    private MyHander myhander = new MyHander();
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //获取fragment布局
        View view = inflater.inflate(R.layout.contactlist_fragment, container, false);
        /*初始化组件*/
        listView = (ListView) view.findViewById(R.id.listView);
        sideBar = (SideBar) view.findViewById(R.id.side_bar);


        System.out.println("STARTING INIT UI");
        initData();



        /*创建自定义适配器，并设置给listview*/
        adapter = new ContactListAdapter(getActivity().getApplicationContext(), listpin);
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
        return view;
    }

    // 初始化了 ids  list用户表 name
    private void initData() {
        //把从服务器获取解析的数据添加到map中，方便处理

        listpin = new ArrayList<>();
        System.out.println(listpin);
        System.out.println(listfriend);
        for(int i=0;i<listfriend.size();i++){
            UserPin temp=new UserPin(listfriend.get(i).getName());
            listpin.add(temp);
        }


            Collections.sort(listpin); // 对list进行排序，需要让UserPin实现Comparable接口重写compareTo方法

    }


    // 1.编写一个发送请求的方法
    // 发送请求的主要方法 获取到了Datas
    public static void httpUrlConnPost(String number) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Contact");
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
            //json.put("title", URLEncoder.encode(title, "UTF-8"));
            json.put("number", URLEncoder.encode(number, "UTF-8"));
            String jsonstr = json.toString();
             OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("aa", urlConnection.getResponseCode()+"");

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

                listpin =new ArrayList<>();

                Type listOfMyClassObject = new TypeToken<ArrayList<User>>() {}.getType();
                Gson gson = new Gson();
                listfriend = gson.fromJson(buffer.toString(), listOfMyClassObject);
                System.out.print(listfriend);
                for(int i=0;i<listfriend.size();i++){
                    UserPin temp=new UserPin(listfriend.get(i).getName());
                    listpin.add(temp);
                }
                flag=0;

                adapter.setData(listpin);



                if (true) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
                    //myhander.sendEmptyMessage(1);
                    Log.i("用户：", "联系人列表初始化成功");
                    System.out.println("联系人列表初始化成功");
                } else {
                    // myhander.sendEmptyMessage(2);
                    System.out.println("联系人列表初始化失败");
                    Log.i("用户：", "联系人列表初始化失败");
                }
            } else {
                //myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("aa", e.toString());
            System.out.println("联系人列表异常");
            //myhander.sendEmptyMessage(2);
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

                    adapter.setData(listpin);

                    break;
                case 2:
                    Log.i("aa", msg.what + "");
            }
        }
    }




}
