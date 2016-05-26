package com.zividig.mobilesafe.activity.view.callsafe;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

import java.util.List;

/**
 * 通讯卫士
 */
public class CallSmsSafeActivity extends Activity {

    private ListView lvBlackNum;
    private List<BlackNumberInfo> numberInfos;
    private LinearLayout llPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_sms_safe);


        initUI();
        initDate();
    }

    //初始化UI
    public void initUI(){
        lvBlackNum = (ListView) findViewById(R.id.lv_black_number);
        llPb = (LinearLayout) findViewById(R.id.ll_pb);
        llPb.setVisibility(View.VISIBLE);
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) { //收到消息后设置数据
            llPb.setVisibility(View.INVISIBLE);
            MyListAdapter adapter = new MyListAdapter();
            lvBlackNum.setAdapter(adapter);
        }
    };
    //初始化数据
    public void initDate(){

        new Thread(){
            @Override
            public void run() {
                BlackNumberDao numberDao = new BlackNumberDao(CallSmsSafeActivity.this);
                numberInfos = numberDao.findAll();
                handler.sendEmptyMessage(0); //发送消息
            }
        }.start();


    }

    class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return numberInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return numberInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null){
                convertView = View.inflate(CallSmsSafeActivity.this,R.layout.item_black_number,null);
                holder = new ViewHolder();
                holder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                holder.tvMode = (TextView) convertView.findViewById(R.id.tv_mode);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvNumber.setText(numberInfos.get(position).getNumber());
            String mode = numberInfos.get(position).getMode();
            if (mode.equals("1")){
                holder.tvMode.setText("来电拦截 + 短信");
            }else if (mode.equals("2")){
                holder.tvMode.setText("来电拦截");
            }else if (mode.equals("3")){
                holder.tvMode.setText("短信拦截");
            }
            return convertView;
        }
    }

    static class ViewHolder{
        TextView tvNumber;
        TextView tvMode;
    }

}
