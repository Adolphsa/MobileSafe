package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zividig.mobilesafe.R;

/**引导页4
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity4 extends Activity {

    private SharedPreferences config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide4);

        config = getSharedPreferences("config", MODE_PRIVATE);
        //上一步
        Button guide2PreBtn = (Button) findViewById(R.id.bt_guide4_pre);
        guide2PreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity4.this,GuideActivity3.class));
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                finish();
            }
        });

        //完成
        Button guide2NextBtn = (Button) findViewById(R.id.bt_guide4_complete);
        guide2NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity4.this,MobileSafe.class));
                config.edit().putBoolean("isFirst",true).commit();
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                finish();
            }
        });
    }
}
