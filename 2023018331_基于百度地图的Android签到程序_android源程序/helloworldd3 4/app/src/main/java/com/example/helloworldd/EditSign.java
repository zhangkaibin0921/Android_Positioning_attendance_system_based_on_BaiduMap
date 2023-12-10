package com.example.helloworldd;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworldd.adapter.SignListAdapter2;
import com.example.helloworldd.pojo.SignItem;
import com.example.helloworldd.pojo.SignQueue;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditSign extends AppCompatActivity {


    Button btnConfirm;

    SignQueue signQueue=new SignQueue();
    RadioGroup radioGroup      ;
    RadioButton radio_daily    ;
    RadioButton radio_weekly   ;
    RadioButton radio_monthly  ;
    RadioGroup  radio_group_text      ;
    RadioButton radio_date    ;
    RadioButton radio_times   ;
    RadioButton radio_no  ;
    EditText    edit_text_times;
    EditText    edit_text_time;
    Button set_start         ;
    TextView set_start_text    ;
    Button set_end           ;
    TextView set_end_text      ;
    TextView btn_confirm      ;
    EditText edit_text_title   ;
    ListView listView;
    Context mContext;
    List<SignItem> items;
    SignListAdapter2 adapter;
    Button post;
    boolean click=false;
    MyHander myHander=new MyHander();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sign);
        mContext=getApplicationContext();
        init_ui();
        adapter = new SignListAdapter2(this, items);
    }
    public void init_ui(){
        radioGroup       =findViewById(R.id.radio_group_type);
        radio_daily     =findViewById(R.id.radio_daily);
        radio_weekly    =findViewById(R.id.radio_weekly);
        radio_monthly   =findViewById(R.id.radio_monthly);
        radio_group_text   =findViewById(R.id.radio_group_text);
        radio_date    =findViewById(R.id.radio_date);
        radio_times   =findViewById(R.id.radio_times);
        radio_no  =findViewById(R.id.radio_no);
        edit_text_times =findViewById(R.id.edit_text_times);
        set_start          =findViewById(R.id.set_start);
        set_start_text     =findViewById(R.id.set_start_text);
        set_end            =findViewById(R.id.set_end);
        set_end_text       =findViewById(R.id.set_end_text);
        edit_text_title    =findViewById(R.id.edit_text_title);
        btn_confirm         =findViewById(R.id.btn_confirm);
        edit_text_time=findViewById(R.id.edit_text_time);
        listView=findViewById(R.id.listvv);
        post=findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(items);

                for (int i = 0; i < items.size(); i++) {
                    String str1= items.get(i).getSignTime();
                    String str2 =items.get(i).getType();
                    System.out.println("str2 "+str2);
                    if (signQueue.getTimeHour()!=null)
                    {
                        signQueue.setTimeHour(signQueue.getTimeHour()+str1+",");
                        signQueue.setTimetype(signQueue.getTimetype()+str2+",");
                    }else {
                        signQueue.setTimeHour(str1+",");
                        signQueue.setTimetype(str2+",");
                    }
                }
                System.out.println(signQueue);
                items =new ArrayList<>();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        httpUrlConnPost();
                    }
                }).start();
            }
        });

        set_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(1);
                showDatePickerDialog(1);
            }
        });
        set_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(2);
                showDatePickerDialog(2);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 根据选中的RadioButton ID 来执行相应的操作
                switch (checkedId) {
                    case R.id.radio_daily:
                        signQueue.setSignTextsType("DAILY");
                        break;
                    case R.id.radio_weekly:
                        signQueue.setSignTextsType("WEEKLY");
                        break;
                    case R.id.radio_monthly:
                        signQueue.setSignTextsType("MONTHLY");
                        break;
                }
            }
        });
        radio_group_text.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // 根据选中的RadioButton ID 来执行相应的操作
                switch (checkedId) {
                    case R.id.radio_date:
                        signQueue.setType("DATE");
                        break;
                    case R.id.radio_times:
                        signQueue.setType("TIMES");
                        break;
                    case R.id.radio_no:
                        signQueue.setType("NO");
                        break;
                }
            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signQueue.setSignTexts(edit_text_title.getText().toString());
                signQueue.setNums(edit_text_times.getText().toString());
                signQueue.setHowLong(edit_text_time.getText().toString());

                System.out.println(signQueue);
                click=true;
                items =new ArrayList<>();
                System.out.println(signQueue.getNums());
                int j = Integer.parseInt(signQueue.getNums().toString());
                for (int i = 1; i < j+1; i++) {
                    SignItem signItem = new SignItem();
                    signItem.setSignCount(i+"");
                    signItem.setType(signQueue.getSignTextsType());
                    items.add(signItem);
                    System.out.println(items);}
                adapter.items=items;
                adapter.notifyDataSetChanged();
                ViewGroup.LayoutParams params = listView.getLayoutParams();
                System.out.println(j);
                params.height = j*130;
                listView.setLayoutParams(params);
                listView.setAdapter(adapter);}});}

    private void showDatePickerDialog(int requestCode) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                System.out.println(signQueue);
                LocalDate date = LocalDate.of(year, month + 1, dayOfMonth); // month参数从0开始，需要加1
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // 定义日期格式
                String dateString = date.format(formatter); // 将LocalDate对象格式化为字符串
                if (requestCode == 1) {
                    // 如果requestCode为1，将日期赋值给dateA
                    signQueue.setStartTime(dateString);
                } else if (requestCode == 2) {
                    // 如果requestCode为2，将日期赋值给dateB
                    signQueue.setEndTime(dateString);
                }

            }
        }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog(int requestCode) {

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                System.out.println(signQueue);

                if (requestCode == 1) {
                    LocalDateTime dateTime;
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateA = LocalDate.parse(signQueue.getStartTime(), formatter1);
                    dateTime = LocalDateTime.of(dateA, LocalTime.of(hourOfDay, minute));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义日期时间格式
                    String dateTimeString = dateTime.format(formatter);
                    signQueue.setStartTime(dateTimeString);
                    set_start_text.setText(dateTimeString);

                } else if (requestCode == 2) {
                    LocalDateTime dateTime;
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    LocalDate dateA = LocalDate.parse(signQueue.getEndTime(), formatter1);
                    dateTime = LocalDateTime.of(dateA, LocalTime.of(hourOfDay, minute));
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // 定义日期时间格式
                    String dateTimeString = dateTime.format(formatter);
                    signQueue.setEndTime(dateTimeString);
                    set_end_text.setText(dateTimeString);
                }
            }
        }, hour, minute, true);
        timePickerDialog.show();
    }

     class MyHander extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 2:
                    Toast.makeText(mContext,"操作成功2",Toast.LENGTH_SHORT);

            }
        }
    }

    public void httpUrlConnPost() {
        HttpURLConnection urlConnection = null;
        URL url;
        try {
            url = new URL("http://180.76.136.248:8080/AndroidServer-1.0-SNAPSHOT/EditSign");
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
            signQueue.setGroupId(Integer.parseInt(WeixinFragment.weixinItem.getGroupid()));
            String jsonstr = new Gson().toJson(signQueue);
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
                System.out.println(rjson);
                if (i==1){

                    Handler handler =new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mContext, "操作完成", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        } catch (Exception r) {
            r.printStackTrace();
        }
    }
}
