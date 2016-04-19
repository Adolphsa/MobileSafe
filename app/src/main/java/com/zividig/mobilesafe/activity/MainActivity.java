package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.zividig.mobilesafe.R;

public class MainActivity extends Activity {

    private String[] itemText = {"手机防盗","通讯卫士","软件管理","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"};
    private int[] itemImage = {R.mipmap.home_safe,R.mipmap.home_callmsgsafe,R.mipmap.home_apps,
                                R.mipmap.home_taskmanager,R.mipmap.home_netmanager,R.mipmap.home_trojan,
                                R.mipmap.home_sysoptimize,R.mipmap.home_tools,R.mipmap.home_settings};
    private GridView glMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        glMainMenu = (GridView) findViewById(R.id.gl_main_menu);
        glMainMenu.setAdapter(new GridViewAdapter());

        //item的点击事件
        glMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(position + "被点击了");
                switch (position){
                    case 0:
                        Intent intent = new Intent(MainActivity.this,MobileSafe.class);
                        startActivity(intent);
                        break;
                }
            }
        });
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
