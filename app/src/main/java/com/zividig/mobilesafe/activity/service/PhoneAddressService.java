package com.zividig.mobilesafe.activity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.view.atools.AddressDao;

/**
 * 查询电话归属地的服务
 * Created by Administrator on 2016-05-20.
 */
public class PhoneAddressService extends Service {

    private TelephonyManager tm;
    private MyListener listener;
    private CallReceiver callReceiver;
    private WindowManager mWM;
    private View view;
    private SharedPreferences mPref;

    private int startX;
    private int startY;
    private int mWidth;
    private int mHeight;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mPref = getSharedPreferences("config",MODE_PRIVATE);

        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        listener = new MyListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);

        //动态注册广播
        callReceiver = new CallReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
        registerReceiver(callReceiver,filter);
    }

    class MyListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);

            switch (state){
                case TelephonyManager.CALL_STATE_RINGING: //来电状态
                    System.out.println("来电");
                    String address = AddressDao.getAddress(incomingNumber);
                    showToast(address);
                    break;

                case TelephonyManager.CALL_STATE_IDLE: //空闲状态  移除显示的Toast
                    if (mWM != null && view != null){
                        mWM.removeView(view);
                    }
                default:
                    break;
            }

        }
    }

    //动态注册去电广播
    //需要权限 android.permission.PROCESS_OUTGOING_CALLS
    class CallReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("去电的广播");
            String number = getResultData();
            String address = AddressDao.getAddress(number);
            showToast(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(listener,TelephonyManager.PHONE_TYPE_NONE); //移除监听

        unregisterReceiver(callReceiver); //移除广播
    }

    //来电显示的窗口风格
    public void showToast(String text){

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        //获取屏幕的宽度和高度
        mWidth = mWM.getDefaultDisplay().getWidth();
        mHeight = mWM.getDefaultDisplay().getHeight();

        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        params.gravity = Gravity.LEFT + Gravity.TOP; //重新设置显示窗口的中心为左上角

        //设置showToast的位置
        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);
        params.x = lastX;
        params.y = lastY;

        view = View.inflate(this, R.layout.layout_phone_address_style,null);
        int[] bgStyle = new int[]{R.drawable.call_locate_white,
                R.drawable.call_locate_orange, R.drawable.call_locate_blue,
                R.drawable.call_locate_gray, R.drawable.call_locate_green

        };
        int style = mPref.getInt("phone_style",0);
        view.setBackgroundResource(bgStyle[style]);
        TextView textView = (TextView) view.findViewById(R.id.tv_address);
        textView.setText(text);
        mWM.addView(view,params);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        //获取起点的X,Y坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //获取终点的X,Y坐标
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        //计算x,y的偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        params.x += dx;
                        params.y += dy;

                        //检查显示框位置的偏差
                        if (params.x < 0){
                            params.x = 0;
                        }
                        if (params.y < 0){
                            params.y = 0;
                        }
                        if (params.x > mWidth-view.getWidth()){
                            params.x = mWidth-view.getWidth();
                        }
                        if (params.y > mHeight-view.getHeight()){
                            params.y = mHeight-view.getHeight();
                        }
                        mWM.updateViewLayout(view,params);

                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //鼠标抬起后记录当前的位置
                        SharedPreferences.Editor edit = mPref.edit();
                        edit.putInt("lastX",params.x);
                        edit.putInt("lastY",params.y);
                        edit.apply();
                        break;
                }
                return false;
            }
        });
    }
}
