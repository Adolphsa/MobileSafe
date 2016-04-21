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
public class SettingItemView extends RelativeLayout {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

    private TextView tvItemTitle;
    private TextView tvItemDesrc;
    private CheckBox cbItem;
    private String mTitle;
    private String mDescOn;
    private String mDescOff;

    public SettingItemView(Context context) {
        super(context);
        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTitle = attrs.getAttributeValue(NAMESPACE, "itemTitle"); //获取自定义属性的值
        mDescOn = attrs.getAttributeValue(NAMESPACE, "itemDesc_on");
        mDescOff = attrs.getAttributeValue(NAMESPACE, "itemDesc_off");

        initView();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView(){
        SettingItemView.inflate(getContext(), R.layout.layout_setting_item,this);
        tvItemTitle = (TextView) findViewById(R.id.tv_setting_item_title);
        tvItemDesrc = (TextView) findViewById(R.id.tv_setting_item_desrc);
        cbItem = (CheckBox) findViewById(R.id.cb_setting_item);

        tvItemTitle.setText(mTitle);
    }

    public boolean isCheck(){
        return cbItem.isChecked();
    }
    //设置是否选中
    public void setCbItem(boolean check){
        cbItem.setChecked(check);

        if (check){
            tvItemDesrc.setText(mDescOn);
        }else {
            tvItemDesrc.setText(mDescOff);
        }
    }
}
