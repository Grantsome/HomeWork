package com.grantsome.zhihudaily.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tom on 2017/2/17.
 */

public class WebCacheDbHelper extends SQLiteOpenHelper {

    public WebCacheDbHelper(Context context, int version) {
        super(context, "webCache.db", null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL("create table if not exists Cache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}