package com.grantsome.zhihudaily.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 继承与SQLiteOpenHelper,一般是重写onCreate和Upgrade方法
 * 当需要创建或者打开一个数据库对象的时候,得到一个WebCacheDbHelper的实例，然后在调用它的getReadable()获取gwtWritable()方法
 * Created by tom on 2017/2/15.
 */

public class CacheDbHelper extends SQLiteOpenHelper {

    //调用父类构造器
    public CacheDbHelper(Context context,int version){
        super(context,"cache.db",null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //数据库里面的三种元素,自动增长的id,时间data,以及json
        sqLiteDatabase.execSQL("create table if not exists CacheList (id INTEGER primary key autoincrement,date INTEGER unique,json text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
