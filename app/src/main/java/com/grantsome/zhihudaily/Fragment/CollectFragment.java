package com.grantsome.zhihudaily.Fragment;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grantsome.zhihudaily.Activity.CollectActivity;
import com.grantsome.zhihudaily.Activity.LatestContentActivity;
import com.grantsome.zhihudaily.Activity.NewsContentActivity;
import com.grantsome.zhihudaily.Adapter.MainNewsItemAdapter;
import com.grantsome.zhihudaily.Adapter.NewsItemAdapter;
import com.grantsome.zhihudaily.Database.MyDataBaseHelper;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by tom on 2017/2/21.
 */

public class CollectFragment extends BaseFragment {

    private ImageLoader imageLoader;

    private ViewFragment viewFagment;

    private MainNewsItemAdapter mainNewsItemAdapter;

    private Stories stories;

    private ListView listViewNews;

    private ImageView imgaeViewTitle;

    private TextView textViewTitle;

    private NewsItemAdapter newsItemAdapter;

    private MyDataBaseHelper myDataBaseHelper;



    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collect_item,container,false);
        listViewNews = (ListView) view.findViewById(R.id.list_view_item);
        Stories stories = ((CollectActivity)getActivity()).getStories();
        final boolean isFromLatestActivity = ((CollectActivity) getActivity()).getIsFromLatestContentActivity();
        myDataBaseHelper =  new MyDataBaseHelper(getActivity(),"Collect.db",null,2);
        if(stories == null){
            SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.query("Collect",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do{
                    final View top = LayoutInflater.from(activity).inflate(R.layout.collect_news,listViewNews,false);
                    final String title = cursor.getString(cursor.getColumnIndex("title"));
                    final int code = cursor.getInt(cursor.getColumnIndex("code"));
                    textViewTitle = (TextView) top.findViewById(R.id.collect_title);
                    textViewTitle.setText(title);
                    listViewNews.addHeaderView(top);
                    textViewTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isFromLatestActivity) {
                                Intent intent = new Intent(activity, LatestContentActivity.class);
                                int[] startingLocation = new int[2];
                                view.getLocationOnScreen(startingLocation);
                                startingLocation[0] += view.getWidth() / 2;
                                intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                intent.putExtra("code", code);
                                intent.putExtra("title", title);
                                startActivity(intent);
                            }
                            if (!isFromLatestActivity){
                                Intent intent = new Intent(activity, NewsContentActivity.class);
                                int[] startingLocation = new int[2];
                                view.getLocationOnScreen(startingLocation);
                                startingLocation[0] += view.getWidth() / 2;
                                intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                intent.putExtra("code", code);
                                intent.putExtra("title", title);
                                startActivity(intent);
                            }
                        }
                    });
                    mainNewsItemAdapter = new MainNewsItemAdapter(activity);
                    LogUtil.d("CollectFragment","title 是 " + title);
                    listViewNews.setAdapter(mainNewsItemAdapter);
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        if(stories != null){
            SQLiteDatabase sqLiteDatabase = myDataBaseHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title",stories.getTitle());
            contentValues.put("code",stories.getId());
            LogUtil.d("CollectFragment","contentValue.put(code)" + stories.getId());
            sqLiteDatabase.insert("Collect",null,contentValues);
            Cursor cursor = sqLiteDatabase.query("Collect",null,null,null,null,null,null);
            if (cursor.moveToFirst()){
                do{
                    final View top = LayoutInflater.from(activity).inflate(R.layout.collect_news,listViewNews,false);
                    final String title = cursor.getString(cursor.getColumnIndex("title"));
                    final int code = cursor.getInt(cursor.getColumnIndex("code"));
                    LogUtil.d("CollectFragment","cursor.getInt" + code);
                    textViewTitle = (TextView) top.findViewById(R.id.collect_title);
                    textViewTitle.setText(title);
                    textViewTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(isFromLatestActivity) {
                                LogUtil.d("CollectFragment", "textView点击事件已经开始执行");
                                Intent intent = new Intent(activity, LatestContentActivity.class);
                                int[] startingLocation = new int[2];
                                view.getLocationOnScreen(startingLocation);
                                startingLocation[0] += view.getWidth() / 2;
                                intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                intent.putExtra("title", title);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }
                            if(!isFromLatestActivity){
                                Intent intent = new Intent(activity, NewsContentActivity.class);
                                int[] startingLocation = new int[2];
                                view.getLocationOnScreen(startingLocation);
                                startingLocation[0] += view.getWidth() / 2;
                                intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                intent.putExtra("title", title);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }
                        }
                    });
                    listViewNews.addHeaderView(top);
                    listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            int[] startingLocation = new int[2];
                            view.getLocationOnScreen(startingLocation);
                            startingLocation[0] += view.getWidth()/2;
                            if(isFromLatestActivity) {
                                Intent intent = new Intent(activity, LatestContentActivity.class);
                                Stories stories = ((CollectActivity) getActivity()).getStories();
                                intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                intent.putExtra("stories", stories);
                                intent.putExtra("title", title);
                                intent.putExtra("code", code);
                                startActivity(intent);
                            }
                            if(!isFromLatestActivity){
                                    Intent intent = new Intent(activity, NewsContentActivity.class);
                                    Stories stories = ((CollectActivity) getActivity()).getStories();
                                    intent.putExtra(ApiUtil.START_LOCATION, startingLocation);
                                    intent.putExtra("stories", stories);
                                    intent.putExtra("title", title);
                                    intent.putExtra("code", code);
                                    startActivity(intent);
                            }
                        }
                    });
                    mainNewsItemAdapter = new MainNewsItemAdapter(activity);
                    listViewNews.setAdapter(mainNewsItemAdapter);

                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        imageLoader = ImageLoader.getInstance();

        listViewNews.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        return view;
    }


}
