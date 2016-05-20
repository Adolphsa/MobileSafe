package com.zividig.mobilesafe.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.customView.SettingItemClickView;
import com.zividig.mobilesafe.activity.customView.SettingItemView;
import com.zividig.mobilesafe.activity.service.PhoneAddressService;
import com.zividig.mobilesafe.activity.view.atools.ServiceStateCheck;

/**
 * 设置
 * Created by Administrator on 2016-04-21.
 */
public class Setting extends Activity {

    private SettingItemView sivAutoUpdate;
    private SharedPreferences mPref;
    private SettingItemView sivPhoneAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        initAutoUpdate();
        initPhoneAddress();
        initPhoneAddressStyle();
    }

    //自动更新开关
    private void initAutoUpdate(){
        sivAutoUpdate = (SettingItemView) findViewById(R.id.siv_setting_update);

        //判断自动更新的状态
        boolean autoUpdate = mPref.getBoolean("auto_update", true);
        if (autoUpdate){
            sivAutoUpdate.setCbItem(true);
        }else {
            sivAutoUpdate.setCbItem(false);
        }

        sivAutoUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sivAutoUpdate.isCheck()){
                    sivAutoUpdate.setCbItem(false);
                    mPref.edit().putBoolean("auto_update",false).apply();
                }else {
                    sivAutoUpdate.setCbItem(true);
                    mPref.edit().putBoolean("auto_update",false).apply();
                }
            }
        });
    }

    //电话归属地查询开关
    private void initPhoneAddress(){
        sivPhoneAddress = (SettingItemView) findViewById(R.id.siv_setting_phone_address);
        //检查服务是否存在
        boolean serviceRunning = ServiceStateCheck.isServiceRunning(this,
                "com.zividig.mobilesafe.activity.service.PhoneAddressService");
        if (serviceRunning){
            sivPhoneAddress.setCbItem(true); //服务存在就设置为真
        }else {
            sivPhoneAddress.setCbItem(false); //服务不存在就设置为假
        }
        sivPhoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, PhoneAddressService.class);
                if (sivPhoneAddress.isCheck()){
                    sivPhoneAddress.setCbItem(false);
                    stopService(intent); //停止服务
                    mPref.edit().putBoolean("phone_address_service",false).apply();
                }else {
                    sivPhoneAddress.setCbItem(true);
                    startService(intent); //开始服务
                    mPref.edit().putBoolean("phone_address_service",true).apply();
                }
            }
        });
    }

    //电话归属地的风格
    private void initPhoneAddressStyle(){
        SettingItemClickView sicv = (SettingItemClickView) findViewById(R.id.siv_setting_phone_address_style);
        sicv.setTvItemDesrc("苹果绿");
        sicv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("电话归属地被点击了");
            }
        });
    }
}
