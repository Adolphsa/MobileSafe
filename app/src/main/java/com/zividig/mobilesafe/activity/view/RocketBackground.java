package com.zividig.mobilesafe.activity.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.zividig.mobilesafe.R;

/**
 * 火箭发射的背景
 * Created by Administrator on 2016-05-25.
 */
public class RocketBackground extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket_background);

        ImageView iv1 = (ImageView) findViewById(R.id.imageView);
        ImageView iv2 = (ImageView) findViewById(R.id.imageView2);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true); //动画结束后保持状态

        iv1.setAnimation(alphaAnimation);
        iv2.setAnimation(alphaAnimation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },1000);
    }
}
