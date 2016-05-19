package com.zividig.mobilesafe.activity.view.atools;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 数据库查询
 * Created by Administrator on 2016-05-19.
 */
public class AddressDao {

    private static final String path = "data/data/com.zividig.mobilesafe/files/address.db";

    public static String getAddress(String number){

        String address = "未知号码";

        //获取数据库对象
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        if (number.matches("^1[3-8]\\d{9}$")){ //匹配手机号码

            Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where id = (select outkey from data1 where id = ?)",
                    new String[]{number.substring(0, 7)});
            if (cursor.moveToNext()){
                address = cursor.getString(0);
            }
            cursor.close();
        }else if (number.matches("^\\d+$")){ //匹配数字
            switch (number.length()){
                case 3:
                    address = "报警电话";
                    break;
                case 4:
                    address = "模拟器";
                    break;
                case 5:
                    address = "客户电话";
                    break;
                case 7:
                case 8:
                    address = "本地电话";
                    break;
                default:
                    if (number.startsWith("0") && number.length()>10){
                        //先查询4位区号
                        Cursor cursor = sqLiteDatabase.rawQuery("select location from data2 where area = ?",
                                new String[]{number.substring(1, 4)});
                        if (cursor.moveToNext()){
                            address = cursor.getString(0);
                        }else {
                            cursor.close();
                            //再查询3位区号
                            cursor = sqLiteDatabase.rawQuery("select location from data2 where area = ?",
                                    new String[]{number.substring(1, 3)});
                            if (cursor.moveToNext()){
                                address = cursor.getString(0);
                            }
                            cursor.close();
                        }
                    }
                    break;
            }

        }
        sqLiteDatabase.close();
        return address;
    }
}
