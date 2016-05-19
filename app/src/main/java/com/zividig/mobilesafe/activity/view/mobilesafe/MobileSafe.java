package com.zividig.mobilesafe.activity.view.mobilesafe;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.receiver.AdminReceiver;

/**
 * 手机防盗
 * Created by Administrator on 2016-04-19.
 */
public class MobileSafe extends Activity {

    private SharedPreferences mPref;
    private TextView safePhone;
    private ImageView ivProtect;
    private ComponentName mDeviceAdminSample;
    public static DevicePolicyManager mDPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPref = getSharedPreferences("config", MODE_PRIVATE);
        boolean isFirst = mPref.getBoolean("isFirst", false);

        //获取设备策略服务
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mDeviceAdminSample = new ComponentName(this, AdminReceiver.class);
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

            //重新设置向导
            RelativeLayout rlAgainSetGuide = (RelativeLayout) findViewById(R.id.rl_ms_again_guide);
            rlAgainSetGuide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MobileSafe.this,GuideActivity1.class));
                    finish();
                }
            });

            //开启设备管理器
            final TextView tvAdmin = (TextView) findViewById(R.id.tv_admin);
            RelativeLayout rlAdmin = (RelativeLayout) findViewById(R.id.rl_admin);
            boolean isAdmin = mPref.getBoolean("admin",false);
            if (isAdmin){
                tvAdmin.setText("已激活");
            }else {
                tvAdmin.setText("已关闭");
            }
            rlAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDPM.isAdminActive(mDeviceAdminSample)){
                        System.out.println("设备管理器已激活");
                        mDPM.removeActiveAdmin(mDeviceAdminSample); //关闭设备管理器
                        tvAdmin.setText("已关闭");
                        mPref.edit().putBoolean("admin",false).apply();

                    }else {
                        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                                "哈哈哈哈");
                        startActivity(intent);
                        tvAdmin.setText("已激活");
                        mPref.edit().putBoolean("admin",true).apply();
                    }


                }
            });
        }else {
            startActivity(new Intent(MobileSafe.this,GuideActivity1.class));
            finish();
        }



    }

}
