package com.zividig.mobilesafe.activity.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

/**
 * 小火箭的服务
 * Created by Administrator on 2016-05-24.
 */
public class RocketService extends Service {

    private WindowManager mWM;
    private View view;

    private int startX;
    private int startY;
    private int mWidth;
    private int mHeight;
    private WindowManager.LayoutParams params;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();

        //获取屏幕的宽度和高度
        mWidth = mWM.getDefaultDisplay().getWidth();
        mHeight = mWM.getDefaultDisplay().getHeight();

        params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;


        params.gravity = Gravity.LEFT + Gravity.TOP; //重新设置显示窗口的中心为左上角


        view = View.inflate(this, R.layout.service_rocket,null);


        mWM.addView(view, params);

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
                        mWM.updateViewLayout(view, params);

                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (params.x > 300 && params.x < 450 && params.y > mHeight-150){
                            System.out.println("发射火箭");
                            showRocket();
                        }
                        break;
                }
                return true;
            }
        });

    }

    public void showRocket(){

        int pos = 450;
        for (int i=0; i<=10; i++){

            params.y = pos - 45*i;

            mWM.updateViewLayout(view,params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWM != null && view != null){
            mWM.removeView(view);
            view = null;
        }
    }
}
