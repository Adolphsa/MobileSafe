package com.zividig.mobilesafe.activity.view.mobilesafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zividig.mobilesafe.R;

/**引导页3
 * Created by Administrator on 2016-04-20.
 */
public class GuideActivity3 extends BaseActivity {

    private EditText safePhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide3);

        safePhone = (EditText) findViewById(R.id.et_safe_phone);

        String safe_phone = sp.getString("safe_phone", ""); //先从配置文件中获取安全号码的值
        safePhone.setText(safe_phone);
    }


    //选择联系人
    public void chosePhoneNumber(View view) {
        Intent intent = new Intent(this,ChoseContact.class);
        startActivityForResult(intent,1);
    }

    //下一页
    @Override
    public void showNextPage() {
        String phone = safePhone.getText().toString().trim(); //注意过滤空格

        if (TextUtils.isEmpty(phone)){
            Toast.makeText(this,"安全号码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

        sp.edit().putString("safe_phone",phone).apply(); //保存安全号码

        startActivity(new Intent(GuideActivity3.this,GuideActivity4.class));
        overridePendingTransition(R.anim.right_in,R.anim.left_out);
        finish();
    }

    //上一页
    @Override
    public void showPreviousPage() {
        startActivity(new Intent(GuideActivity3.this,GuideActivity2.class));
        overridePendingTransition(R.anim.left_in,R.anim.right_out);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            String phone = data.getStringExtra("phone");
            phone = phone.replaceAll("-", "").replaceAll(" ", "");// 替换-和空格

            safePhone.setText(phone);
        }
    }
}
