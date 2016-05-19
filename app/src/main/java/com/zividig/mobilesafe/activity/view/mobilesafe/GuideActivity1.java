package com.zividig.mobilesafe.activity.view.mobilesafe;

import android.content.Intent;
import android.os.Bundle;

import com.zividig.mobilesafe.R;

/**引导页1
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity1 extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide1);

    }

    @Override
    public void showNextPage() {
        startActivity(new Intent(GuideActivity1.this,GuideActivity2.class));
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
        finish();
    }

    @Override
    public void showPreviousPage() {

    }
}
