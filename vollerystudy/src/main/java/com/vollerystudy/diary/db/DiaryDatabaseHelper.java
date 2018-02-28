package com.vollerystudy.diary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/6/30.
 */

public class DiaryDatabaseHelper extends SQLiteOpenHelper {

    private Context mContext;

    private static final String CRETA_DIARY = "create table Diary("
            +"id integer primary key autoincrement, "
            +"date text ,"
            +"title text, "
            +"tag text, "
            +"content text)";

    public DiaryDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CRETA_DIARY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("drop table if exists Diary");
        onCreate(sqLiteDatabase);
    }
}
