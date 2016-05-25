package com.zividig.mobilesafe.activity.view.callsafe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放黑名单的数据库
 * Created by Administrator on 2016-05-25.
 */
public class BlackNumberDao {

    private final BlackNumberOpenHelper helper;

    public BlackNumberDao(Context context) {
        helper = new BlackNumberOpenHelper(context);
    }

    /**
     * 添加号码
     * @param number 黑名单号码
     * @param mode 拦截模式
     * @return
     */
    public boolean add(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number",number);
        values.put("node",mode);
        long rowID = db.insert("blacknumber", null, values);
        if (rowID == -1){
            return false;
        }else {
            return true;
        }

    }

    /**
     * 删除
     * @param number 电话号码
     * @return
     */
    public Boolean delete(String number){
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowNumber = db.delete("blacknumber", "number=?", new String[]{number});
        if (rowNumber == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 通过电话号码修改拦截模式
     * @param number 电话号码
     * @param mode  模式
     * @return
     */
    public Boolean update(String number,String mode){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("mode",mode);
        int rowNumber = db.update("blacknumber", values, "number=?", new String[]{number});
        if (rowNumber == 0){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 返回一个黑名单号码的拦截模式
     * @param number 电话号码
     * @return  拦截模式
     */
    public String find(String number){
        String mode = "";
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("blacknumber",
                new String[]{"mode"},
                "number=?",
                new String[]{number},
                null, null, null);
        if (cursor.moveToNext()){
           mode = cursor.getString(0);
        }

        cursor.close();
        db.close();

        return mode;
    }

    /**
     * 查询所有的黑名单
     */
    public List<BlackNumberInfo> findAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        List<BlackNumberInfo> blackNumberInfos = new ArrayList<>();
        Cursor cursor = db.query("blacknumber",
                new String[]{"number", "mode"},
                null, null, null, null, null);
        while (cursor.moveToNext()){
            BlackNumberInfo blackNumberInfo = new BlackNumberInfo();
            blackNumberInfo.setNumber(cursor.getString(0));
            blackNumberInfo.setMode(cursor.getString(1));
            blackNumberInfos.add(blackNumberInfo);
        }
        cursor.close();
        db.close();

        return blackNumberInfos;
    }
}

