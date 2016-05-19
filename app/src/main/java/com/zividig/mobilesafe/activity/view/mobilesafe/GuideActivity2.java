package com.zividig.mobilesafe.activity.view.mobilesafe;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.customView.SettingItemView;

/**引导页2
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity2 extends BaseActivity {

    private SettingItemView bindSim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide2);

        bindSim = (SettingItemView) findViewById(R.id.bind_sim);

        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)){
            bindSim.setCbItem(false);
        }else {
            bindSim.setCbItem(true);
        }

        bindSim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bindSim.isCheck()){ //如果点击之前是选中的状态，则把选中状态编程非选中状态
                    System.out.println("取消勾选");
                    bindSim.setCbItem(false); //不选中
                    sp.edit().remove("sim").apply(); //移除sim卡号
                }else {
                    System.out.println("勾选");
                    bindSim.setCbItem(true);  //选中
                    TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String simSerialNumber = tm.getSimSerialNumber(); //获取sim卡序列号
                    System.out.println("手机卡的序列号是：" + simSerialNumber) ;

                    //保存序列号
                    sp.edit().putString("sim",simSerialNumber).apply();
                }
            }
        });
    }

    //下一步
    @Override
    public void showNextPage() {
        String sim = sp.getString("sim", null);
        if (TextUtils.isEmpty(sim)){
            Toast.makeText(this,"必须绑定sim卡!",Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(new Intent(GuideActivity2.this,GuideActivity3.class));
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
        finish();
    }

    //上一步
    @Override
    public void showPreviousPage() {
        startActivity(new Intent(GuideActivity2.this,GuideActivity1.class));
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
        finish();
    }
}
