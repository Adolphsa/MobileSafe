package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zividig.mobilesafe.R;

/**
 * 手机防盗
 * Created by Administrator on 2016-04-19.
 */
public class MobileSafe extends Activity {

    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirst = mPref.getBoolean("isFirst", false);
        if (isFirst){
            setContentView(R.layout.activity_mobile_safe);
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
