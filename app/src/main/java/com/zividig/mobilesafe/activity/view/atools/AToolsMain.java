package com.zividig.mobilesafe.activity.view.atools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.zividig.mobilesafe.R;

/**
 * 高级工具
 * Created by Administrator on 2016-05-19.
 */
public class AToolsMain extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
    }

    public void phoneQuery(View view){
        Intent intent = new Intent(this,PhoneQueryTool.class);
        startActivity(intent);
    }
}
