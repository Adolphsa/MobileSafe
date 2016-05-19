package com.zividig.mobilesafe.activity.view.atools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

/**
 * 电话归属地查询
 * Created by Administrator on 2016-05-19.
 */
public class PhoneQueryTool extends Activity {

    private EditText etPhone;
    private Button btQuery;
    private TextView tvDestinationPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_query_tool);

        etPhone = (EditText) findViewById(R.id.et_phone);
        btQuery = (Button) findViewById(R.id.bt_query);
        tvDestinationPhone = (TextView) findViewById(R.id.tv_destination_phone);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String address = AddressDao.getAddress(s.toString());
                tvDestinationPhone.setText(address);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    //开始查询
    public void query(View view){
        String number = etPhone.getText().toString().trim();
        if (!TextUtils.isEmpty(number)){
            String address = AddressDao.getAddress(number);
            tvDestinationPhone.setText(address);

        }else {
            vibrator();
        }

    }

    //震动
    public void vibrator(){

        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{1000,2000,1000,2000},-1);
    }
}
