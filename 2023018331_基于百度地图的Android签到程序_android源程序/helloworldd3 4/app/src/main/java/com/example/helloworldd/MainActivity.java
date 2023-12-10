package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static Context context;

    PopupWindow popupWindow;


    private WeixinFragment firstFragment = null;// 用于显示180.76.136.248界面
    private ContactListFragment secondFragment = null;// 用于显示通讯录界面
    private SelfFragment fourthFragment = null;// 用于显示我界面

    private View firstLayout = null;// 180.76.136.248显示布局
    private View secondLayout = null;// 通讯录显示布局
    private View fourthLayout = null;// 我显示布局

    /*声明组件变量*/
    private ImageView weixinImg = null;
    private ImageView contactImg = null;
    private ImageView selfImg = null;

    private TextView weixinText = null;
    private TextView contactText = null;
    private TextView selfText = null;

    private FragmentManager fragmentManager = null;// 用于对Fragment进行管理

    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        fragmentManager = getFragmentManager();//用于对Fragment进行管理
        // 初始化布局元素
        initViews();
        // 设置默认的显示界面
        setTabSelection(0);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                ContactListFragment.httpUrlConnPost(nowUser.getPhone());
            }
        }).start();

    }

    public void initViews() {
        fragmentManager = getFragmentManager();
        firstLayout = findViewById(R.id.weixin_layout);
        secondLayout = findViewById(R.id.contacts_layout);
        fourthLayout = findViewById(R.id.self_layout);

        weixinImg = (ImageView) findViewById(R.id.weixin_img);
        contactImg = (ImageView) findViewById(R.id.contact_img);
        selfImg = (ImageView) findViewById(R.id.self_img);

        weixinText = (TextView) findViewById(R.id.weixin_text);
        contactText = (TextView) findViewById(R.id.contact_text);
        selfText = (TextView) findViewById(R.id.self_text);

        //处理点击事件
        firstLayout.setOnClickListener(this);
        secondLayout.setOnClickListener(this);
        fourthLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.weixin_layout:
                setTabSelection(0);// 当点击了180.76.136.248时，选中第1个tab
                break;
            case R.id.contacts_layout:
                setTabSelection(1);// 当点击了通讯录时，选中第2个tab
                break;
            case R.id.self_layout:
                setTabSelection(3);// 当点击了我时，选中第4个tab
                break;
            default:
                break;
        }

    }


    private void setTabSelection(int index) {
        clearSelection();// 每次选中之前先清除掉上次的选中状态
        FragmentTransaction transaction = fragmentManager.beginTransaction();// 开启一个Fragment事务
        hideFragments(transaction);// 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        switch (index) {
            case 0:
                // 当点击了我的180.76.136.248tab时改变控件的图片和文字颜色
                weixinImg.setImageResource(R.drawable.tab_weixin_pressed);//修改布局中的图片
                weixinText.setTextColor(Color.parseColor("#0090ff"));//修改字体颜色

                if (firstFragment == null) {

                    // 如果FirstFragment为空，则创建一个并添加到界面上
                    firstFragment = new WeixinFragment(phone);
                    transaction.add(R.id.fragment, firstFragment);

                } else {
                    // 如果FirstFragment不为空，则直接将它显示出来
                    transaction.show(firstFragment);//显示的动作
                }
                break;
            // 以下和firstFragment类同
            case 1:
                contactImg.setImageResource(R.drawable.tab_address_pressed);
                contactText.setTextColor(Color.parseColor("#0090ff"));
                if (true) {
                    secondFragment = new ContactListFragment();
                    transaction.add(R.id.fragment, secondFragment);
                } else {
                    transaction.show(secondFragment);
                }
                break;
            case 3:
                selfImg.setImageResource(R.drawable.tab_settings_pressed);
                selfText.setTextColor(Color.parseColor("#0090ff"));
                if (true) {
                    fourthFragment = new SelfFragment();
                    transaction.add(R.id.fragment, fourthFragment);
                } else {
                    transaction.show(fourthFragment);
                }
                break;
        }
        transaction.commit();

    }


    /**
     * 清除掉所有的选中状态
     */
    private void clearSelection() {
        weixinImg.setImageResource(R.drawable.tab_weixin_normal);
        weixinText.setTextColor(Color.parseColor("#82858b"));

        contactImg.setImageResource(R.drawable.tab_address_normal);
        contactText.setTextColor(Color.parseColor("#82858b"));


        selfImg.setImageResource(R.drawable.tab_settings_normal);
        selfText.setTextColor(Color.parseColor("#82858b"));
    }

    /**
     * 将所有的Fragment都设置为隐藏状态 用于对Fragment执行操作的事务
     */
    @SuppressLint("NewApi")
    private void hideFragments(FragmentTransaction transaction) {
        if (firstFragment != null) {
            transaction.hide(firstFragment);
        }
        if (secondFragment != null) {
            transaction.hide(secondFragment);
        }

        if (fourthFragment != null) {
            transaction.hide(fourthFragment);
        }
    }



    //搜索按钮处理事件
    public void search(View v) {
        /*跳转到180.76.136.248启动页*/
        Intent intent = new Intent();
        intent.setClass(com.example.helloworldd.MainActivity.this,
                com.example.helloworldd.AddNewFreind.class);
        startActivity(intent);
    }


    //菜单按钮的单机事件
    public void OnMenu(View v){

        //获取自定义菜单的布局文件
        View popupWindow_view=getLayoutInflater().inflate(R.layout.menu,null,false);
        //创建popupWindow实例，设置菜单的宽度和高度
        popupWindow=new PopupWindow(popupWindow_view, ActionBar.LayoutParams.WRAP_CONTENT,ActionBar.LayoutParams.WRAP_CONTENT,true);
        //设置菜单显示在按钮的下面
        popupWindow.showAsDropDown(findViewById(R.id.plus),0,0);
        //单机其它位置隐藏菜单
        popupWindow_view.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                //如果菜单存在并且处于显示状态
                if (popupWindow!=null&&popupWindow.isShowing()){
                    popupWindow.dismiss();//关闭菜单
                    popupWindow=null;
                }
                return false;
            }
        });

        Button create=popupWindow_view.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (com.example.helloworldd.MainActivity.this, com.example.helloworldd.CreateGroup.class);
                startActivity(intent);
            }
        });


        Button addfri=popupWindow_view.findViewById(R.id.addfri);
        addfri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (com.example.helloworldd.MainActivity.this, com.example.helloworldd.AddNewFreind.class);
                startActivity(intent);
            }
        });
    }
    public void createGroup(){

        Intent intent = new Intent (com.example.helloworldd.MainActivity.this, com.example.helloworldd.CreateGroup.class);
        startActivity(intent);

    }
    public void baiduClick(){
        Intent intent = new Intent (com.example.helloworldd.MainActivity.this, com.example.helloworldd.SelfFragment.class);
        startActivity(intent);
    }
}