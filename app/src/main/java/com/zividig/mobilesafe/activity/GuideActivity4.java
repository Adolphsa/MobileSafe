package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.zividig.mobilesafe.R;

/**引导页4
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity4 extends BaseActivity {


    private CheckBox cbProtect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide4);
        cbProtect = (CheckBox) findViewById(R.id.cb_guard);

        Boolean protect = sp.getBoolean("protect",false);
        if (protect){
            cbProtect.setText("防盗保护已经开启");
            cbProtect.setChecked(true);
        }else{
            cbProtect.setText("防盗保护没有开启");
            cbProtect.setChecked(false);
        }

        cbProtect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    cbProtect.setText("防盗保护已经开启");
                    sp.edit().putBoolean("protect",true).apply();
                }else {
                    cbProtect.setText("防盗保护没有开启");
                    sp.edit().putBoolean("protect",false).apply();
                }
            }
        });

    }

    //完成
    @Override
    public void showNextPage() {
        startActivity(new Intent(GuideActivity4.this,MobileSafe.class));
        sp.edit().putBoolean("isFirst",true).apply();
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
        finish();
    }

    //上一步
    @Override
    public void showPreviousPage() {
        startActivity(new Intent(GuideActivity4.this,GuideActivity3.class));
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
        finish();
    }
}
