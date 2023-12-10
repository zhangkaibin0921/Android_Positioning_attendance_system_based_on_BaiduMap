package com.example.helloworldd;

import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.helloworldd.pojo.SignRecord;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignStatic extends AppCompatActivity implements OnChartValueSelectedListener {
    public static MyHander myHander =new MyHander();
    static ImageView imageView;
    static LinearLayout image;
    static LinearLayout content;
    static TextView title_text_view;
    TextView pingjun;
    TextView zuihao;
    TextView zuicha;
    static  BarChart chart;
    static PieChart piechart;
    static ListView listView;
    static ListView listView2;
    static double maxRate;
    static double minRate;
    static Context mContext;
    static List<String> studentsBelow75;
    static HashMap<String, int[]> userSignRates;
    Button Export;
    String downloadUrl;

    public static ArrayList<SignRecord>signRecords=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_static);
        mContext=getApplicationContext();

        init_ui();
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpUrlConnPost(WeixinFragment.weixinItem.getGroupid());
                myHander.sendEmptyMessage(2);
            }
        }).start();

        Glide.with(this)
                .asGif()
                .load(R.drawable.tea)
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

                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void run() {
                                        myHander.sendEmptyMessage(1);
                                        pingjun.setText(calculateAverageSignRate(signRecords)+"");
                                        zuihao.setText(bestSignRecord(signRecords).getText());
                                        zuicha.setText(worstSignRecord(signRecords).getText());
                                        HashMap<String, int[]> signRates =calculateSignRates(signRecords);
                                        {
                                            List<BarEntry> entries = getSignRatesEntries(signRecords);
                                            BarDataSet dataSet = new BarDataSet(entries, " HHK");
                                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                                            // 设置 X 轴属性
                                            XAxis xAxis = chart.getXAxis();
                                            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                                            xAxis.setDrawGridLines(false);
                                            xAxis.setGranularity(1f);
                                            xAxis.setLabelCount(7);
                                            xAxis.setTextSize(12f);

                                            // 设置 Y 轴属性
                                            YAxis yAxisLeft = chart.getAxisLeft();
                                            yAxisLeft.setDrawGridLines(false);
                                            yAxisLeft.setTextSize(12f);

                                            YAxis yAxisRight = chart.getAxisRight();
                                            yAxisRight.setEnabled(false);

                                            // Create a list of BarDataSet objects to hold the BarData objects
                                            List<IBarDataSet> dataSets = new ArrayList<>();
                                            dataSets.add(dataSet);

                                            // Create a BarData object to hold the BarDataSet objects
                                            BarData data = new BarData(dataSets);
                                            data.setBarWidth(0.9f);
                                            data.setValueTextSize(16f);
                                            data.setValueTextColor(Color.BLACK);

                                            // Set the data on the chart
                                            chart.setData(data);
                                            // Set the listener for chart value selection events
                                            System.out.println(data);
                                            System.out.println(entries);
                                        }
                                        {
                                            userSignRates = calculateUserSignRates(signRecords);
                                            HashMap<String, Integer> signRateGroups = new HashMap<>();
                                            for (Map.Entry<String, int[]> entry : userSignRates.entrySet()) {
                                                String username = entry.getKey();
                                                int[] stats = entry.getValue();
                                                double signRate = (double) stats[0] / stats[1];
                                                String group;
                                                if (signRate < 0.6) {
                                                    group = "60%以下";
                                                } else if (signRate < 0.8) {
                                                    group = "60%-80%";
                                                } else {
                                                    group = "80%-100%";
                                                }
                                                signRateGroups.put(group, signRateGroups.getOrDefault(group, 0) + 1);
                                            }
                                            System.out.println(signRateGroups);
                                            ArrayList<PieEntry> entries = new ArrayList<>();
                                            for (Map.Entry<String, Integer> entry : signRateGroups.entrySet()) {

                                                entries.add(new PieEntry(entry.getValue(), entry.getKey()));
                                                PieDataSet dataSet = new PieDataSet(entries, "");
                                                dataSet.setColors(new int[]{Color.RED, Color.GREEN, Color.BLUE});
                                                dataSet.setValueTextColor(Color.WHITE);
                                                dataSet.setValueTextSize(16f);

                                                // 创建一个 PieData 对象，将 PieDataSet 对象传入其中
                                                PieData data = new PieData(dataSet);

                                                // 设置饼图的样式和数据
                                                piechart.setData(data);
                                                piechart.setHoleRadius(50f);
                                                piechart.setTransparentCircleRadius(55f);
                                                piechart.setDrawEntryLabels(true);
                                                piechart.setEntryLabelTextSize(14f);
                                                piechart.setEntryLabelColor(Color.WHITE);
                                                piechart.getDescription().setEnabled(false);
                                                piechart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                                                piechart.getLegend().setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                                                piechart.getLegend().setOrientation(Legend.LegendOrientation.HORIZONTAL);
                                                piechart.getLegend().setTextSize(14f);
                                                piechart.getLegend().setTextColor(Color.BLACK);
//                        piechart.animateY(1000);
                                                piechart.invalidate();
                                            }
                                        }
                                        {
                                            myHander.sendEmptyMessage(3);

                                        }
                                        {
                                            studentsBelow75 = new ArrayList<>();
                                            for (String username : userSignRates.keySet()) {
                                                int[] stats = userSignRates.get(username);
                                                double attendanceRate = (double) stats[0] / stats[1];

                                                if (attendanceRate < 0.75) {
                                                    studentsBelow75.add(username);
                                                }
                                            }
                                            myHander.sendEmptyMessage(4);
                                        }

                                    }
                                }).start();


                            }
                        });
                        return false;
                    }
                })
                .into(imageView);


        chart.setOnChartValueSelectedListener(this);
        
    }
    void init_ui()
    {
        pingjun=findViewById(R.id.pingjun);
        zuihao=findViewById(R.id.zuihao);
        zuicha=findViewById(R.id.zuicha);
        imageView = findViewById(R.id.phot);
        image=findViewById(R.id.image);
        content=findViewById(R.id.content);
        chart=findViewById(R.id.chartbar);
        piechart=findViewById(R.id.pie_chart);
        title_text_view=findViewById(R.id.title_text_view);
        listView =findViewById(R.id.list_view2);
        listView2 =findViewById(R.id.list_view3);
        Export=findViewById(R.id.export);
        Export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(mContext,"正在为您生成报告",Toast.LENGTH_LONG);
                        httpUrlConnPost2();

                    }
                }).start();
            }
        });
    }


    //首先，我将为您提供前四个问题的方法。请注意，这些方法需要您导入Java所需的包，例如java.util.ArrayList、java.util.HashMap等。以下是四个方法：
    //计算平均签到率：
    public static double calculateAverageSignRate(ArrayList<SignRecord> signRecords) {
        int totalCount = signRecords.size();
        int successCount = 0;

        for (SignRecord signRecord : signRecords) {
            if (signRecord.getSignsuccess().equals("签到成功") || signRecord.getSignsuccess().equals("学生请假")) {
                successCount++;
            }
        }

        return (double) successCount / totalCount;
    }

    //最好的签到（签到率最高）：
    public static SignRecord bestSignRecord(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> signRates = calculateSignRates(signRecords);
        SignRecord bestSignRecord = null;
        maxRate = Double.MIN_VALUE;

        for (SignRecord signRecord : signRecords) {
            String signId = signRecord.getSignid();
            int[] stats = signRates.get(signId);
            double rate = (double) stats[0] / stats[1];

            if (rate > maxRate) {
                maxRate = rate;
                bestSignRecord = signRecord;
            }
        }

        return bestSignRecord;
    }
    //最差的签到（签到率最低）：
    public static SignRecord worstSignRecord(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> signRates = calculateSignRates(signRecords);
        SignRecord worstSignRecord = null;
        minRate = Double.MAX_VALUE;

        for (SignRecord signRecord : signRecords) {
            String signId = signRecord.getSignid();
            int[] stats = signRates.get(signId);
            double rate = (double) stats[0] / stats[1];

            if (rate < minRate) {
                minRate = rate;
                worstSignRecord = signRecord;
            }
        }

        return worstSignRecord;
    }
    //不经常签到的学生（签到率低于75%）：
    public static ArrayList<String> infrequentSigners(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> userSignRates = calculateUserSignRates(signRecords);
        ArrayList<String> infrequentSigners = new ArrayList<>();

        for (String username : userSignRates.keySet()) {
            int[] stats = userSignRates.get(username);
            double rate = (double) stats[0] / stats[1];

            if (rate < 0.75) {
                infrequentSigners.add(username);
            }
        }

        return infrequentSigners;
    }
    //以下是您需要为这些方法使用的辅助方法：
    public static HashMap<String, int[]> calculateSignRates(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> signRates = new HashMap<>();

        for (SignRecord signRecord : signRecords) {
            String signId = signRecord.getSignid();
            int[] stats = new int[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                stats = signRates.getOrDefault(signId, new int[]{0, 0});
            }

            if (signRecord.getSignsuccess().equals("签到成功") || signRecord.getSignsuccess().equals("学生请假")) {
                stats[0]++;
            }
            stats[1]++;

            signRates.put(signId, stats);
        }

        return signRates;
    }

    public static HashMap<String, int[]> calculateUserSignRates(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> userSignRates = new HashMap<>();

        for (SignRecord signRecord : signRecords) {
            String username = signRecord.getUsername();
            int[] stats = new int[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                stats = userSignRates.getOrDefault(username, new int[]{0, 0});
            }

            if (signRecord.getSignsuccess().equals("签到成功") || signRecord.getSignsuccess().equals("学生请假")) {
                stats[0]++;
            }
            stats[1]++;

            userSignRates.put(username, stats);
        }

        return userSignRates;
    }

    public static HashMap<String, int[]> calculateUserSignRates2(ArrayList<SignRecord> signRecords) {
        HashMap<String, int[]> userSignRates = new HashMap<>();

        for (SignRecord signRecord : signRecords) {
            String username = signRecord.getUsername();
            int[] stats = new int[0];
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                stats = userSignRates.getOrDefault(username, new int[]{0, 0,0,0,0,0});
            }

            if (signRecord.getSignsuccess().equals("签到成功") || signRecord.getSignsuccess().equals("学生请假")) {
                stats[0]++;
            }
            stats[1]++;
            if (signRecord.getSignsuccess().equals("签到成功")){stats[2]++;}
            if (signRecord.getSignsuccess().equals("学生请假")){stats[3]++;}
            if (signRecord.getSignsuccess().equals("签到失败")){stats[4]++;}
            if (signRecord.getSignsuccess().equals("未签到")){stats[5]++;}

            userSignRates.put(username, stats);
        }

        return userSignRates;
    }


    Comparator<SignRecord> idComparator = new Comparator<SignRecord>() {
        @Override
        public int compare(SignRecord s1, SignRecord s2) {
            return s1.getId().compareTo(s2.getId());
        }
    };



    //对于第五个问题，您可以使用ListView控件，并将学生的用户名和签到率添加到列表中。然而，这需要您使用Java的图形用户界面框架，例如JavaFX或Swing。
    //第六个问题涉及到数据可视化，您可以使用Java的第三方库，如JFreeChart实现柱状图和饼图。您可以根据ArrayList<SignRecord>中的数据生成图表。这将需要
    public List<BarEntry> getSignRatesEntries(ArrayList<SignRecord> signRecords) {
        List<BarEntry> entries = new ArrayList<>();

        // 计算每个 signid 的签到率
        HashMap<String, int[]> signStats = calculateSignRates(signRecords);
        HashMap<String, Double> signRates = new HashMap<>();

        for (Map.Entry<String, int[]> entry : signStats.entrySet()) {
            String signId = entry.getKey();
            int[] stats = entry.getValue();

            if (stats[1] > 0) {
                double signRate = (double) stats[0] / stats[1];
                signRates.put(signId, signRate);
            }
        }

        // 将每个 signid 和其对应的签到率添加到 BarEntry 中
        int i = 0;
        for (Map.Entry<String, Double> entry : signRates.entrySet()) {
            String signId = entry.getKey();
            Double signRate = entry.getValue();
            String label = "";

            // 在这里获取 signrecord 的 text 作为条目的标签
            for (SignRecord signRecord : signRecords) {
                if (signRecord.getSignid().equals(signId)) {
                    label = signRecord.getText();
                    break;
                }
            }

            entries.add(new BarEntry(i++, signRate.floatValue(), label));
        }

        return entries;
    }

    public void httpUrlConnPost(String groupid) {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/SignStatic");
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
            System.out.println("GET NUM"+groupid);
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
                Gson gson = new Gson();
                signRecords = gson.fromJson(buffer.toString(), new TypeToken<ArrayList<SignRecord>>() {}.getType());
                Collections.sort(signRecords,idComparator);
                System.out.println(signRecords);
            }
        } catch (Exception r) {r.printStackTrace();}
    }
    public void httpUrlConnPost2() {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/Download");
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


            String jsonstr = new Gson().toJson(calculateUserSignRates2(signRecords));
            OutputStream out = urlConnection.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            bw.write(jsonstr);
            bw.flush();
            out.close();
            bw.close();
            Log.i("SIGN STATIC", urlConnection.getResponseCode() + "");

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
                downloadUrl=buffer.toString();
                Toast.makeText(mContext,"下载链接已经复制到粘贴板中",Toast.LENGTH_SHORT);
                downloadUrl="http://180.76.136.248:8080"+downloadUrl;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setText(downloadUrl);
                System.out.println("kkkx "+downloadUrl);

            }
        } catch (Exception r) {r.printStackTrace();}
    }
    static class MyHander extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    image.setVisibility(View.GONE);
                    content.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    content.setVisibility(View.GONE);
                case 3:
                    if (userSignRates!=null){

                    SignRecordListAdapter adapter = new SignRecordListAdapter(mContext,userSignRates);
                    listView.setAdapter(adapter);


                        listView.setAdapter(adapter);
                    }
                case 4:
                    if (studentsBelow75!=null){
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, studentsBelow75);
                    listView2.setAdapter(adapter2);
                        ViewGroup.LayoutParams params = listView2.getLayoutParams();
                        listView2.setAdapter(adapter2);}

            }
        }

    }
    @Override
    public void onValueSelected(Entry e, Highlight h) {
        System.out.println(h);
        System.out.println(e);

        // Get the label of the selected bar entry

        String label = chart.getData().getDataSetByIndex(h.getDataSetIndex()).getEntryForXValue(e.getX(), e.getY()).getData().toString();

        // Set the title text to the selected label
        title_text_view.setText(label);
    }

    @Override
    public void onNothingSelected() {
        // Do nothing when no bar is selected
    }

    private static class SignRecordListAdapter extends BaseAdapter {

        private Context mContext;
        private HashMap<String, int[]> mData;

        public SignRecordListAdapter(Context context, HashMap<String, int[]> data) {
            mContext = context;
            mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public Object getItem(int position) {
            String[] keys = mData.keySet().toArray(new String[0]);
            return keys[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_student, parent, false);
            }

            // 获取当前学生的姓名、签到次数、请假次数和签到率
            String username = (String) getItem(position);
            int[] stats = mData.get(username);
            int attendanceCount = stats[0];
            int absenceCount = stats[1] - stats[0];
            double attendanceRate = (double) attendanceCount / stats[1];

            // 在视图中显示学生姓名、签到次数、请假次数和签到率
            TextView usernameTextView = view.findViewById(R.id.text_name);
            TextView attendanceCountTextView = view.findViewById(R.id.text_attendance);
            TextView absenceCountTextView = view.findViewById(R.id.text_absence);
            TextView attendanceRateTextView = view.findViewById(R.id.text_attendance_rate);

            usernameTextView.setText(username);
            attendanceCountTextView.setText(String.valueOf(attendanceCount));
            absenceCountTextView.setText(String.valueOf(absenceCount));
            attendanceRateTextView.setText(String.format("%.2f", attendanceRate * 100) + "%");

            return view;
        }
    }
}