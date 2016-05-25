package com.zividig.mobilesafe.activity.view.callsafe;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 黑名单数据库帮助打开类
 * Created by Administrator on 2016-05-25.
 */
public class BlackNumberOpenHelper extends SQLiteOpenHelper{

    public BlackNumberOpenHelper(Context context) {
        super(context, "blackNumber.db", null, 1);
    }

    /**
     * 创建表
     * @param db
     * id 自增长的ID
     * number 电话号码
     * mode 模式
     *
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table blacknumber (_id integer primary key autoincrement," +
                "number varchar(20)," +
                "mode varchar(2))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
