package com.zividig.mobilesafe.activity.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

/**
 * 设置菜单自定义的布局
 * Created by Administrator on 2016-04-21.
 */
public class SettingItemClickView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView tvItemTitle;
    private TextView tvItemDesrc;


    public SettingItemClickView(Context context) {
        super(context);
        initView();
    }

    public SettingItemClickView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initView();
    }

    public SettingItemClickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        SettingItemClickView.inflate(getContext(), R.layout.layout_setting_item_click,this);
        tvItemTitle = (TextView) findViewById(R.id.tv_setting_item_title);
        tvItemDesrc = (TextView) findViewById(R.id.tv_setting_item_desrc);
    }


    public void setTvItemTitle(String text){
        tvItemTitle.setText(text);
    }

    public void setTvItemDesrc(String text){
        tvItemDesrc.setText(text);
    }
}
