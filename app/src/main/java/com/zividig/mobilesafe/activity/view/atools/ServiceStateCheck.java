package com.zividig.mobilesafe.activity.view.atools;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * 检查服务是否存在
 * Created by Administrator on 2016-05-20.
 */
public class ServiceStateCheck {

    public static boolean isServiceRunning(Context context,String serviceName ){

       ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(100);

        for (ActivityManager.RunningServiceInfo runningServiceInfo: runningServices) {
            String className = runningServiceInfo.service.getClassName(); //获取服务名称
            if (className.equals(serviceName)){
                return true;
            }
        }
        return false;
    }
}
