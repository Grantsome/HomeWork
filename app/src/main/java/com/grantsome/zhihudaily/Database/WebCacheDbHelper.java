package com.grantsome.zhihudaily.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 继承与SQLiteOpenHelper,一般是重写onCreate和Upgrade方法
 * 当需要创建或者打开一个数据库对象的时候,得到一个WebCacheDbHelper的实例，然后在调用它的getReadable()获取gwtWritable()方法
 * Created by tom on 2017/2/17.
 */

public class WebCacheDbHelper extends SQLiteOpenHelper {

    //调用父类构造器
    public WebCacheDbHelper(Context context, int version) {
        super(context, "webCache.db", null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        //数据库里面的三种元素，第一个是自动增长的id,第二个是newsId，第三个是json的text
        database.execSQL("create table if not exists Cache (id INTEGER primary key autoincrement,newsId INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

    }
}