package com.example.helloworldd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class WelcomeFlie extends AppCompatActivity {

    private static boolean hasShownWelcome = false; // 静态变量

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_flie);

        // 如果已经显示过欢迎页面，则直接跳转到登录页面
        if (hasShownWelcome) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        ImageView welcome = findViewById(R.id.waitingiamge);
        Glide.with(this).load(R.drawable.welocome).into(welcome);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 保存已经显示过欢迎页面的标记
                hasShownWelcome = true;

                startActivity(new Intent(WelcomeFlie.this, LoginActivity.class));
                finish();
            }
        }, 1400);
    }
}
