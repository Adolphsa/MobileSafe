package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zividig.mobilesafe.R;

/**引导页2
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity2 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);

        //上一步
        Button guide2PreBtn = (Button) findViewById(R.id.bt_guide2_pre);
        guide2PreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity2.this,GuideActivity1.class));
                overridePendingTransition(R.anim.left_in,R.anim.right_out);
                finish();
            }
        });

        //下一步
        Button guide2NextBtn = (Button) findViewById(R.id.bt_guide2_next);
        guide2NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity2.this,GuideActivity3.class));
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                finish();
            }
        });
    }
}
