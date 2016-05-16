package com.zividig.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.zividig.mobilesafe.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 联系人界面
 * Created by Administrator on 2016-05-16.
 */
public class ChoseContact extends Activity {

    private ArrayList<HashMap<String, String>> readContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chose_contact);
        ListView lv = (ListView) findViewById(R.id.lv_list);

        readContact = readContact();
//        System.out.println(readContact);

        //设置每行的点击事件
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String phone = readContact.get(position).get("phone"); // 读取当前item的电话号码
                Intent intent = new Intent();
                intent.putExtra("phone",phone);
                setResult(Activity.RESULT_OK,intent); // 将数据放在intent中返回给上一个页面
                finish();
            }
        });

        //给listView设置适配器
        lv.setAdapter(new SimpleAdapter(this, readContact,
                R.layout.layout_contact_list_item, new String[] { "name", "phone" },
                new int[] { R.id.tv_name, R.id.tv_phone }));
    }

    private ArrayList<HashMap<String, String>> readContact(){
        // 首先,从raw_contacts中读取联系人的id("contact_id")
        // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
        // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码

        Uri rawContactUri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri dataUri = Uri.parse("content://com.android.contacts/data");

        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        // 从raw_contacts中读取联系人的id("contact_id")
        Cursor rawContactCursor = getContentResolver().query(rawContactUri,new String[]{"contact_id"},null,null,null);
        if (rawContactCursor != null){
            while (rawContactCursor.moveToNext()){
                String contactId = rawContactCursor.getString(0);
                System.out.println(contactId);

                // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                Cursor dataCursor = getContentResolver().query(dataUri,
                        new String[]{"data1","mimetype"},
                        "contact_id=?",
                        new String[]{contactId},
                        null);

                if (dataCursor != null){
                    HashMap<String, String> map = new HashMap<String, String>();
                    while (dataCursor.moveToNext()){
                        String data1 = dataCursor.getString(0);
                        String mimetype = dataCursor.getString(1);

                        if ("vnd.android.cursor.item/phone_v2".equals(mimetype)) {
                            map.put("phone", data1);
                        } else if ("vnd.android.cursor.item/name"
                                .equals(mimetype)) {
                            map.put("name", data1);
                        }
                    }

                    list.add(map);
                    dataCursor.close();
                }
            }

            rawContactCursor.close();

        }
        return list;
    }
}
