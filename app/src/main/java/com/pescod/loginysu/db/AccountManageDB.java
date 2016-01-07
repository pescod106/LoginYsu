package com.pescod.loginysu.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.pescod.loginysu.model.AccountInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 1/1/2016.
 */
public class AccountManageDB {
    //数据库名字
    public static final String DB_NAME = "AccountDB";
    //数据库版本
    public static final int VERSION =1;

    private static AccountManageDB accountManageDB;
    private SQLiteDatabase db;

    public AccountManageDB(Context context) {
        LoginYsuOpenHelper dbHelper = new LoginYsuOpenHelper(context,DB_NAME,null,VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public synchronized static AccountManageDB getInstance(Context context){
        if (accountManageDB==null){
            accountManageDB = new AccountManageDB(context);
        }
        return accountManageDB;
    }

    public void saveAccount(AccountInfo accountInfo){
        if(accountInfo!=null){
            ContentValues values = new ContentValues();
            values.put("account",accountInfo.getStrAccount());
            values.put("password",accountInfo.getStrPassword());
            values.put("isTest",accountInfo.isTest());
            values.put("isAvailable", accountInfo.isAvailable());
            db.insert("AccountTable",null,values);
        }
    }

    public List<AccountInfo> loadAccountInfo(){
        List<AccountInfo> list = new ArrayList<AccountInfo>();
        Cursor cursor = db.query("AccountTable",new String[]{"account","password","isTest","isAvailable"},null,null,"account",null,null);
        if (cursor.moveToFirst()){
            do{
                AccountInfo accountInfo = new AccountInfo();
                accountInfo.setStrAccount(cursor.getString(cursor.getColumnIndex("account")));
                accountInfo.setStrPassword(cursor.getString(cursor.getColumnIndex("password")));
                accountInfo.setIsTest(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isTest"))));
                accountInfo.setIsAvailable(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex("isAvailable"))));
                list.add(accountInfo);
            }while(cursor.moveToNext());
        }
        return list;
    }

    /**
     * 修改密码
     * @param account帐号
     * @param password密码
     */
    public void changePassword(String account,String password){
        String update = "UPDATE AccountTable SET password = "+password+" WHERE account = "+account;
        db.execSQL(update);
    }

    /**
     * 修改该帐号测试状态
     * @param account帐号
     */
    public void changeTestState(String account){
        String update = "UPDATE AccountTable SET isTest = 1 WHERE account = "+account;
        db.execSQL(update);
    }
}
