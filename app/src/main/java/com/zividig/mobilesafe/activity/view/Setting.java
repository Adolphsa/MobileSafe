package com.zividig.mobilesafe.activity.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.customView.SettingItemView;

/**
 * 设置
 * Created by Administrator on 2016-04-21.
 */
public class Setting extends Activity {

    private SettingItemView sivAutoUpdate;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

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
                    mPref.edit().putBoolean("auto_update",false).commit();
                }else {
                    sivAutoUpdate.setCbItem(true);
                    mPref.edit().putBoolean("auto_update",false).commit();
                }
            }
        });
    }
}
