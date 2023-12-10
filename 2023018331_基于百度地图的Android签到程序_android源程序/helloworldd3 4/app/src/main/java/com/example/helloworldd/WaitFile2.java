package com.example.helloworldd;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class WaitFile2 extends AppCompatActivity {
Handler handler = new Handler();
    private boolean shouldSkip = false; // 标记变量，判断是否需要跳过跳转
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_file2);
        TextView wait =findViewById(R.id.wait);
        ImageView imageView=findViewById(R.id.waitingiamge);
        new Thread(new Runnable() {

            @Override
            public void run() {
                System.out.println("------------------------"+SignGroup.signInfo2.getSignid());

                SignGroup.httpUrlConnPost3(SignGroup.signInfo2.getSignid());
            }
        }).start();
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
                                if (!shouldSkip) {
                                    wait.getContext().startActivity(new Intent(WaitFile2.this, DetailSign.class));
                                    finish();
                                }

                            }
                        });
                        return false;
                    }
                })
                .into(imageView);}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shouldSkip = true; // 在页面销毁时设置shouldSkip为true
    }
}