package com.zividig.mobilesafe.activity.view.callsafe;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.zividig.mobilesafe.R;

/**
 * 通讯卫士
 */
public class CallSmsSafeActivity extends Activity {

    private ListView lvBlackNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);

        lvBlackNum = (ListView) findViewById(R.id.lv_black_number);
    }

}
