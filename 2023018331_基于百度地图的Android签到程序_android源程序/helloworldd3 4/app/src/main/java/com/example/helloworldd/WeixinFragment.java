package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.helloworldd.adapter.InformationAdapter;
import com.example.helloworldd.pojo.WeixinList;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressLint("ValidFragment")
public class WeixinFragment extends Fragment {
    Button button;

    public static Map ItemMap;
    public static List<WeixinList> weixinlist;
    public static WeixinList weixinItem;
    //public static MyHander myHander= new MyHander();

    private static MyHander myhander = new MyHander();

    //180.76.136.248号，用于查找180.76.136.248消息列表
    private String number;
    // 声明组件
    private ListView listView;
    // 创建集合用于存储服务器发来的显示180.76.136.248消息列表的一些信息
    public static List<Map<String, Object>> shouyelist = new ArrayList<Map<String, Object>>();

    public static InformationAdapter shouyeadapter;
    //自定义的一个Hander消息机制

    /*有参构造方法，参数为180.76.136.248号*/
    @SuppressLint("ValidFragment")
    WeixinFragment(String number) {
        this.number = number;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        number=nowUser.getPhone();

        // 开一个线程完成网络请求操作
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                httpUrlConnPost(String.valueOf(number));
            }
        });
        thread1.start();
        /*等待网络请求线程完成*/
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //获取fragment布局
        View view = inflater.inflate(R.layout.weixin_fragment, container, false);

        //初始化组件
        listView = view.findViewById(R.id.listView);
        //创建自定义的适配器，用于把数据显示在组件上
        shouyeadapter = new InformationAdapter(getActivity().getApplicationContext(), shouyelist);
        //设置适配器
        listView.setAdapter(shouyeadapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
//                    while (true)
                    {httpUrlConnPost(String.valueOf(number));
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                shouyeadapter = new InformationAdapter(getActivity().getApplicationContext(), shouyelist);
                                //设置适配器
                                listView.setAdapter(shouyeadapter);
                            }
                        });
                        Thread.sleep(10000);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ItemMap= (Map) shouyeadapter.getItem(i);
                weixinItem=weixinlist.get(i);

                System.out.print("选中   "+weixinItem);

                startActivity(new Intent(getContext(),WaitFile.class));


            }
        });




//        button = view.findViewById(R.id.baidu);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent (getActivity(), com.example.helloworldd.BaiduActivity.class);
//                startActivity(intent);
//            }
//        });

        return view;
    }

    // 1.编写一个发送请求的方法
    public static void httpUrlConnPost(String number) {

        HttpURLConnection urlConnection = null;
        URL url;
        try {
             
            url = new URL(
                    "http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/WeixinInformation");
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
            Log.i("180.76.136.248消息", urlConnection.getResponseCode() + "");

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

                Type listOfMyClassObject = new TypeToken<ArrayList<WeixinList>>() {}.getType();

                Gson gson = new Gson();
                weixinlist = gson.fromJson(buffer.toString(), listOfMyClassObject);

                System.out.print(weixinlist);


                List<String> title=new ArrayList<>();
                List<String> content=new ArrayList<>();
                List<String> time=new ArrayList<>();





                for (int i = 0; i < weixinlist.size(); i++) {
                    title.add(weixinlist.get(i).getTitle());
                    content.add(weixinlist.get(i).getContent());
                    time.add(weixinlist.get(i).getTime());
                }

//                String str2 = rjson.getJSONObject("json").get("title").toString();
//                String[] title = str2.split("\r\n");
//                String str3 = rjson.getJSONObject("json").get("content").toString();
//                String[] content = str3.split("\r\n");
//                String str4 = rjson.getJSONObject("json").get("time").toString();
//                String[] time = str4.split("\r\n");

                shouyelist=new ArrayList<>();

                for (int i = 0; i < title.size(); i++) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("title", title.get(i));
                    //System.out.println("网址:" + title.get(i));
                    if(content.get(i)!=null){
                        map.put("content", content.get(i));
                    }
                    else{
                        map.put("content", "");
                    }

                    map.put("time", time.get(i));
                    shouyelist.add(map);//将map放到list集合中
                }


                //shouyeadapter.setData(shouyelist);
                //shouyeadapter.notifyDataSetChanged();



                boolean result = true;//rjson.getBoolean("json");
                System.out.println("json:===" + result);

                //如果服务器端返回的是true，则说明跳转180.76.136.248页成功，跳转180.76.136.248页失败
                if (result) {// 判断结果是否正确
                    //在Android中http请求，必须放到线程中去作请求，但是在线程中不可以直接修改UI，只能通过hander机制来完成对UI的操作
//                    Handler myhander=new Handler();
//                    myhander.sendEmptyMessage(1);


                    Log.i("用户：", "跳转180.76.136.248页成功");
                    System.out.println("跳转180.76.136.248页成功");
                } else {
                    //myhander.sendEmptyMessage(2);
                    System.out.println("跳转180.76.136.248页失败");
                    Log.i("用户：", "跳转180.76.136.248页失败");
                }
            } else {
                //myhander.sendEmptyMessage(2);
            }
        } catch (Exception e) {
            e.printStackTrace();

            Log.i("aa", e.toString());
            System.out.println("跳转180.76.136.248页异常");
            //myhander.sendEmptyMessage(2);
        } finally {
            urlConnection.disconnect();
        }
    }

     static class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断hander的内容是什么，如果是1则说明跳转180.76.136.248页成功，如果是2说明跳转180.76.136.248页失败
            switch (msg.what) {
                case 1:
                    Log.i("aa", msg.what + "SPE");
                    shouyeadapter.notifyDataSetChanged();

                    break;
                case 2:
                    Log.i("aa", msg.what + "");
            }

        }
    }


}
