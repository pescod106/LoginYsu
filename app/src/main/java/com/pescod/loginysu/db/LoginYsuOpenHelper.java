package com.pescod.loginysu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 1/1/2016.
 */
public class LoginYsuOpenHelper extends SQLiteOpenHelper {

    public static final String CREATE_ACCOUNT = "CREATE TABLE AccountTable(" +
            "account character(12) primary key," +
            "password varchar(20)," +
            "isTest boolean default 0," +
            "isAvailable boolean default 0)";

    public LoginYsuOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
