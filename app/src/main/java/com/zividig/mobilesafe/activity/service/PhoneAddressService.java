package com.zividig.mobilesafe.activity.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView view;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

    public void showToast(String text){

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        view = new TextView(this);
        view.setText(text);
        view.setTextColor(Color.BLUE);
        mWM.addView(view,params);
    }
}
