package com.zividig.mobilesafe.activity.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.view.RocketBackground;

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
    AnimationDrawable rocketAnimation;

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
        ImageView rocketImage = (ImageView) view.findViewById(R.id.iv_rocket);
        rocketImage.setBackgroundResource(R.drawable.smoke);
        rocketAnimation = (AnimationDrawable) rocketImage.getBackground();
        rocketAnimation.start();

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
                        if (params.x > (mWidth/2-150) && params.x < (mWidth/2+150) && params.y > mHeight-150){
                            System.out.println("发射火箭");

                            showRocket();
                            Intent intent = new Intent(RocketService.this, RocketBackground.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                        break;
                }
                return true;
            }
        });

    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int y = msg.arg1;
            params.y = y;
            mWM.updateViewLayout(view,params);
        }
    };

    //展示火箭
    public void showRocket(){
        //火箭居中
        params.x = mWidth/2 - view.getWidth()/2;
        mWM.updateViewLayout(view,params);

        new Thread(){
            @Override
            public void run() {
                super.run();
                int pos = 450;
                for (int i=0; i<=10; i++){
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int y = pos - 45*i;
                    Message ms = Message.obtain();
                    ms.arg1 = y;
                    handler.sendMessage(ms);
                }
            }
        }.start();

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
