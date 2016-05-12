package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zividig.mobilesafe.R;
import com.zividig.mobilesafe.activity.utils.MD5Utils;

public class MainActivity extends Activity {

    private String[] itemText = {"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
    private int[] itemImage = {R.mipmap.home_safe,R.mipmap.home_callmsgsafe,R.mipmap.home_apps,
                                R.mipmap.home_taskmanager,R.mipmap.home_netmanager,R.mipmap.home_trojan,
                                R.mipmap.home_sysoptimize,R.mipmap.home_tools,R.mipmap.home_settings};
    private GridView glMainMenu;
    private SharedPreferences mPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPref = getSharedPreferences("config", MODE_PRIVATE);

        glMainMenu = (GridView) findViewById(R.id.gl_main_menu);
        glMainMenu.setAdapter(new GridViewAdapter());

        //item的点击事件
        glMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + "被点击了");
                switch (position){
                    case 0: //手机安全
                        showDialog();
                        break;
                    case 8: //设置
                        Intent intent8 = new Intent(MainActivity.this,Setting.class);
                        startActivity(intent8);
                        break;
                }
            }
        });

    }
    //显示对话框
    private void showDialog(){

        String isSave = mPref.getString("password",null);

        if (!TextUtils.isEmpty(isSave)){ //设置过密码
            setInputPwdDialog();
        }else { //没有设置过密码
            setSettingPwdDialog();
        }
    }

    //输入密码对话框
    private void setInputPwdDialog(){
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this,R.layout.layout_input_pwd_dialog,null);
        dialog.setView(view,0,0,0,0);

        final EditText etPwd = (EditText) view.findViewById(R.id.et_input_dialog_pwd); //密码

        //确定按钮
        Button inputSureBtn = (Button) view.findViewById(R.id.bt_input_dialog_ensure);
        inputSureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("确定");
                String password = etPwd.getText().toString();
                String savedPassword = mPref.getString("password",null);

                //判断密码输入框是否为空
                if (!TextUtils.isEmpty(password)){
                    if (MD5Utils.encode(password).equals(savedPassword)){ //相等
                        Toast.makeText(MainActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(MainActivity.this,MobileSafe.class));
                    }
                    else { //不相等
                        Toast.makeText(MainActivity.this,"密码输入不正确",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"密码不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //取消按钮
        Button inputCancelBtn = (Button) view.findViewById(R.id.bt_input_dialog_cancel);
        inputCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("取消");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //设置密码对话框
    private void setSettingPwdDialog(){
        AlertDialog.Builder builder = new  AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this,R.layout.layout_setting_pwd_dialog,null);
        dialog.setView(view,0,0,0,0);

        final EditText etPwd = (EditText) view.findViewById(R.id.et_setting_dialog_pwd); //密码
        final EditText etAgainPwd = (EditText) view.findViewById(R.id.et_setting_dialog_again_pwd); //确认密码

        //确定按钮
        Button sureBtn = (Button) view.findViewById(R.id.bt_setting_dialog_ensure);
        sureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("确定");
                String password = etPwd.getText().toString();
                String password2 = etAgainPwd.getText().toString();

                //判断密码输入框是否为空
                if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(password2)){
                    if (password.equals(password2)){ //相等
                        //保存密码
                        mPref.edit().putString("password", MD5Utils.encode(password)).commit();
                        Toast.makeText(MainActivity.this,"设置密码成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        startActivity(new Intent(MainActivity.this,MobileSafe.class));
                    }
                    else { //不相等
                        Toast.makeText(MainActivity.this,"两次密码不一样",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"密码不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                }

            }
        });

        //取消按钮
        Button cancelBtn = (Button) view.findViewById(R.id.bt_setting_dialog_cancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("取消");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //网格布局的数据适配器
    class GridViewAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return itemText.length;
        }

        @Override
        public Object getItem(int position) {
            return itemText[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(MainActivity.this,R.layout.item_mian_menu,null);
            ImageView ivItem = (ImageView) view.findViewById(R.id.iv_item);
            TextView tvItem = (TextView) view.findViewById(R.id.tv_item);

            ivItem.setImageResource(itemImage[position]);
            tvItem.setText(itemText[position]);
            return view;
        }
    }


}
