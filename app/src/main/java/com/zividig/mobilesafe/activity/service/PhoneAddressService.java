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

        WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

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
    }
}
