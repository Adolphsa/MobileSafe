package com.zividig.mobilesafe.activity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 开机自启动的广播
 * Created by Administrator on 2016-05-13.
 */
public class BootComplete extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String sim = sp.getString("sim", null);//获取系统保存的sim卡号

        if (!TextUtils.isEmpty(sim)){
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String simSerialNumber = tm.getSimSerialNumber(); //获取当前手机的sim卡号

            if (sim.equals(simSerialNumber)){
                System.out.println("序列号相等，手机安全");
            }else {
                System.out.println("序列号不相等，手机危险");
            }
        }
    }
}
