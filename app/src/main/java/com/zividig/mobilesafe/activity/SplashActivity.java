package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zividig.mobilesafe.MainActivity;
import com.zividig.mobilesafe.R;

/**
 * 闪屏页面
 * Created by Administrator on 2016-04-15.
 */
public class SplashActivity extends Activity{
    private final static int CODE_ENTER_MAIN_ACTIVITY = 0;

    private RelativeLayout rl;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case CODE_ENTER_MAIN_ACTIVITY:
                    enterMainActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        rl = (RelativeLayout) findViewById(R.id.rl_splash);
        TextView versionName = (TextView) findViewById(R.id.tv_version_code);
        versionName.setText("版本号" + getVersionName());
//        AlphaAnimation();
        //延时2S进入到主界面
        mHandler.sendEmptyMessageDelayed(CODE_ENTER_MAIN_ACTIVITY,2000);
    }

    private void enterMainActivity(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private String getVersionName(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            int versionCode = packageInfo.versionCode;
            System.out.println("版本号为" + versionCode);
            String versionName = packageInfo.versionName;
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    private void AlphaAnimation(){
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(500);
        rl.setAnimation(alpha);
    }


}
