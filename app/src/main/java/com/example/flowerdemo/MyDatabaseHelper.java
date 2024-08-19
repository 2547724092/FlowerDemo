package com.example.flowerdemo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Run on 2021/3/26.
 */
public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_FLOWERS = "create table flowers("
            + "id integer primary key autoincrement,"
            + "name text,"
            + "kind text,"
            + "price text,"
            + "address text,"
            + "pic BLOB)";
    public static final String CREATE_USERS = "create table users ("
            + "id integer primary key autoincrement,"
            + "user_name text,"
            + "user_code text,"
            + "avatar BLOB)";
    public static final String CREATE_LIST = "create table list("
            + "id integer primary key autoincrement,"
            + "user_name text,"
            + "name text,"
            + "num text,"
            + "total text,"
            + "time text)";

    public static final String CREATE_COMMENT = "create table comment("
            + "id integer primary key autoincrement,"
            + "user_name text,"
            + "name text,"
            + "content text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FLOWERS);
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_LIST);
        db.execSQL(CREATE_COMMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Flowers");
        db.execSQL("drop table if exists users");
        db.execSQL("drop table if exists list");
        db.execSQL("drop table if exists comment");
        onCreate(db);
    }
}