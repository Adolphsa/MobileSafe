package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

/**
 * 手机防盗
 * Created by Administrator on 2016-04-19.
 */
public class MobileSafe extends Activity {

    private SharedPreferences mPref;
    private TextView safePhone;
    private ImageView ivProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirst = mPref.getBoolean("isFirst", false);
        if (isFirst){
            setContentView(R.layout.activity_mobile_safe);

            //根据sp更新安全号码
            safePhone = (TextView) findViewById(R.id.tv_safe_phone); //安全号码
            String safe_phone = mPref.getString("safe_phone", "");
            safePhone.setText(safe_phone);

            //根据sp更新保护锁
            ivProtect = (ImageView) findViewById(R.id.iv_protect); //图片
            boolean protect = mPref.getBoolean("protect",false);
            if (protect){
                ivProtect.setImageResource(R.mipmap.lock);
            }else{
                ivProtect.setImageResource(R.mipmap.unlock);
            }

            RelativeLayout rlAgainSetGuide = (RelativeLayout) findViewById(R.id.rl_ms_again_guide);
            rlAgainSetGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MobileSafe.this,GuideActivity1.class));
                    finish();
                }
            });
        }else {
            startActivity(new Intent(MobileSafe.this,GuideActivity1.class));
            finish();
        }



    }

}
