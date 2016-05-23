package com.zividig.mobilesafe.activity.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

/**
 * 归属地显示控件的拖动
 * Created by Administrator on 2016-05-23.
 */
public class DragView extends Activity{

    private TextView tvTop;
    private ImageView ivDrag;
    private TextView tvBottom;
    private int startX;
    private int startY;
    private SharedPreferences mPref;
    private int mWidth;
    private int mHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        mPref = getSharedPreferences("config",MODE_PRIVATE);

        //获取屏幕的宽度和高度
        mWidth = getWindowManager().getDefaultDisplay().getWidth();
        mHeight = getWindowManager().getDefaultDisplay().getHeight();

        tvTop = (TextView) findViewById(R.id.tv_top);
        ivDrag = (ImageView) findViewById(R.id.iv_drag);
        tvBottom = (TextView) findViewById(R.id.tv_bottom);

        int lastX = mPref.getInt("lastX", 0);
        int lastY = mPref.getInt("lastY", 0);

        //判断该显示上面的文本还是下面的文本
        if (lastY > mHeight/2){
            tvTop.setVisibility(View.INVISIBLE);
            tvBottom.setVisibility(View.VISIBLE);
        }else {
            tvTop.setVisibility(View.VISIBLE);
            tvBottom.setVisibility(View.INVISIBLE);
        }

        //获取布局对象
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivDrag.getLayoutParams();
        layoutParams.leftMargin = lastX;
        layoutParams.topMargin = lastY;
        ivDrag.setLayoutParams(layoutParams);

        ivDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN: //按下
                        //获取起点的X,Y坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE: //移动
                        //获取终点的X,Y坐标
                        int endX = (int) event.getRawX();
                        int endY = (int) event.getRawY();

                        //计算x,y的偏移量
                        int dx = endX - startX;
                        int dy = endY - startY;

                        //绘制控件位置
                        int l = ivDrag.getLeft() + dx; //左
                        int t = ivDrag.getTop() + dy; //上
                        int r = ivDrag.getRight() + dx; //右
                        int b = ivDrag.getBottom() + dy; //下

                        if (l<0 || t<0 || r>mWidth || b>mHeight-20){ //超出屏幕宽度就break
                            break;
                        }

                        if (event.getRawY() > mHeight/2){
                            tvTop.setVisibility(View.INVISIBLE);
                            tvBottom.setVisibility(View.VISIBLE);
                        }else {
                            tvTop.setVisibility(View.VISIBLE);
                            tvBottom.setVisibility(View.INVISIBLE);
                        }
                        ivDrag.layout(l,t,r,b);

                        //重新初始化起点坐标
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP: //抬起
                        //鼠标抬起后记录当前的位置
                        SharedPreferences.Editor edit = mPref.edit();
                        edit.putInt("lastX",ivDrag.getLeft());
                        edit.putInt("lastY",ivDrag.getTop());
                        edit.apply();
                        break;
                }
                return true;
            }
        });
    }
}
