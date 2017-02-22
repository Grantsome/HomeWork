package com.grantsome.zhihudaily.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tom on 2017/2/21.
 */

public class MyDataBaseHelper extends SQLiteOpenHelper{

   public static final String CREATE_COLLECT = "create table Collect (" + "id integer primary key autoincrement," + "title text," + "code integer)";

    private Context mContext;

    public MyDataBaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_COLLECT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
         sqLiteDatabase.execSQL("drop table if exists Collect");
         onCreate(sqLiteDatabase);
    }

}
