package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zividig.mobilesafe.R;

/**引导页1
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide1);

        //下一步
        Button guide1NextBtn = (Button) findViewById(R.id.bt_guide1_next);
        guide1NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity1.this,GuideActivity2.class));
                overridePendingTransition(R.anim.right_in,R.anim.left_out);
                finish();
            }
        });
    }

}
