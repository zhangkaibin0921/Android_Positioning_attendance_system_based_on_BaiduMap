package com.example.helloworldd;

import static com.example.helloworldd.LoginActivity.nowUser;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;


@SuppressLint("ValidFragment")
public class SelfFragment extends Fragment {
    public ImageView mHBack;
    public ImageView mHHead;
    public ImageView mUserLine;
    public static TextView mUserName;
    public static TextView mUserVal;

    public static ItemView mNickName;
    public static ItemView mSex;
    public static ItemView mSignName;
    public ItemView mPass;
    public ItemView mPhone;
    public ItemView mAbout;
    public ItemView button;
    Context context =getActivity();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.self_fragment,container,false);
        mHBack = (ImageView) view.findViewById(R.id.h_back);
        mHHead = (ImageView) view.findViewById(R.id.h_head);
        mUserLine = (ImageView) view.findViewById(R.id.user_line);
        mUserName = (TextView) view.findViewById(R.id.user_name);
        mUserVal = (TextView) view.findViewById(R.id.user_val);
        //下面item控件
        mNickName = (ItemView) view.findViewById(R.id.nickName);
        mSex = (ItemView) view.findViewById(R.id.sex);
        mSignName = (ItemView) view.findViewById(R.id.signName);
        mPass = (ItemView) view.findViewById(R.id.pass);
        mPhone = (ItemView) view.findViewById(R.id.phone);
        mAbout = (ItemView) view.findViewById(R.id.about);
        button=view.findViewById(R.id.abouts);

        //setData();

        mSex.setRightDesc(nowUser.getSex());
        mNickName.setRightDesc(nowUser.getName());
        mSignName.setRightDesc(nowUser.getQianming());
        mPhone.setRightDesc(nowUser.getPhone());
        mUserName.setText(nowUser.getName());
        mUserVal.setText(nowUser.getPhone());


        Bitmap bitmap1= BitmapFactory.decodeResource(getResources(), R.drawable.touxiang2);
        Bitmap bitmap2=makeBitmapSquare(bitmap1,120);
        RoundedBitmapDrawable roundImg1= RoundedBitmapDrawableFactory.create(getResources(),bitmap2);
        roundImg1.setAntiAlias(true);
        roundImg1.setCornerRadius(bitmap2.getWidth()/2);
        mHHead.setImageDrawable(roundImg1);


        System.out.println(getContext());
//        System.out.println(getContext());

        mNickName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
               // Toast.makeText(SelfFragment.this,Toast.LENGTH_SHORT,"用户名"+text).show();
                Toast.makeText(getContext(), "用户名:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });

        mSex.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                // Toast.makeText(SelfFragment.this,Toast.LENGTH_SHORT,"用户名"+text).show();
                Toast.makeText(getContext(), "性别:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mSignName.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                // Toast.makeText(SelfFragment.this,Toast.LENGTH_SHORT,"用户名"+text).show();
                Toast.makeText(getContext(), "个性签名:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mPhone.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                // Toast.makeText(SelfFragment.this,Toast.LENGTH_SHORT,"用户名"+text).show();
                Toast.makeText(getContext(), "电话:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mAbout.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getContext(), "版本:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });
        mPass.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                Toast.makeText(getContext(), "密码不可见:"+text,
                        Toast.LENGTH_SHORT).show();
            }
        });

        button.setItemClickListener(new ItemView.itemClickListener() {
            @Override
            public void itemClick(String text) {
                startActivity(new Intent(getContext(), ChangeYourInfo.class));
            }
        });



        System.out.println(nowUser);




        return view;
    }
    public static Bitmap makeBitmapSquare(Bitmap oldbitmap, int newWidth){
        Bitmap newbitmap=null;
        if (oldbitmap.getWidth()>oldbitmap.getHeight()){
            newbitmap=Bitmap.createBitmap(oldbitmap,oldbitmap.getWidth()/2-oldbitmap.getHeight()/2,0,oldbitmap.getHeight(),oldbitmap.getHeight());
        }else{
            newbitmap=Bitmap.createBitmap(oldbitmap,0,oldbitmap.getHeight()/2-oldbitmap.getWidth()/2,oldbitmap.getWidth(),oldbitmap.getWidth());
        }
        int width=newbitmap.getWidth();
        float scaleWidth=((float)newWidth)/width;
        Matrix matrix=new Matrix();
        matrix.postScale(scaleWidth,scaleWidth);
        newbitmap= Bitmap.createBitmap(newbitmap,0,0,width,width,matrix,true);
        return newbitmap;
    }



}
