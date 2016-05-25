package com.zividig.mobilesafe.activity.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.service.RocketService;
import com.zividig.mobilesafe.activity.utils.ExitAppUtils;

/**
 * 清理缓存
 * Created by Administrator on 2016-05-24.
 */
public class CleanCache extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean_cache);

        ExitAppUtils.getInstance().addActivity(this);

    }

    public void startRocket(View view){
        startService(new Intent(CleanCache.this, RocketService.class));
        ExitAppUtils.getInstance().exit();
    }

    public void stopRocket(View view){
        stopService(new Intent(CleanCache.this, RocketService.class));
    }
}
