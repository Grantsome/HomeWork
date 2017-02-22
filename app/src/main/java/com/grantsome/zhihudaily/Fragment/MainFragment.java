package com.grantsome.zhihudaily.Fragment;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Activity.LatestContentActivity;
import com.grantsome.zhihudaily.Activity.MainActivity;
import com.grantsome.zhihudaily.Adapter.MainNewsItemAdapter;
import com.grantsome.zhihudaily.Model.BeforeNews;
import com.grantsome.zhihudaily.Model.LatestNews;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.List;

/**
 * Created by tom on 2017/2/16.
 */

public class MainFragment extends BaseFragment{

    private ListView listViewNews;

    private ViewFragment viewFagment;

    private MainNewsItemAdapter mainNewsItemAdapter;

    private boolean isloading = false;

    private LatestNews latestNews;

    private String date;

    private Handler handler = new Handler();

    private BeforeNews beforeNews;

    private boolean isRead;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) activity).setToolBarTitle("首页");
        View view = inflater.inflate(R.layout.news_item, container, false);
        listViewNews = (ListView) view.findViewById(R.id.list_view_item);
        View v = inflater.inflate(R.layout.view_fragment,listViewNews,false);
        viewFagment = (ViewFragment) v.findViewById(R.id.view_fragment);
        viewFagment.setOnItemClickListener(new ViewFragment.OnItemClickListener() {
            @Override
            public void click(View v, LatestNews.TopStoriesBean topStoriesBean) {
                int[] startingLocation = new int[2];
                v.getLocationOnScreen(startingLocation);
                startingLocation[0] += v.getWidth()/2;
                Stories stories = new Stories();
                stories.setId(topStoriesBean.getId());
                stories.setTitle(topStoriesBean.getTitle());
                Intent intent = new Intent(activity, LatestContentActivity.class);
                intent.putExtra(ApiUtil.START_LOCATION,startingLocation);
                intent.putExtra("isLight",((MainActivity) activity).isLight());
                intent.putExtra("stories",stories);

                startActivity(intent);
                activity.overridePendingTransition(0,0);
            }
        });
        listViewNews.addHeaderView(v);
        mainNewsItemAdapter = new MainNewsItemAdapter(activity);

        listViewNews.setAdapter(mainNewsItemAdapter);
        listViewNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(listViewNews!=null&&listViewNews.getChildCount()>0){
                    boolean enable = (i ==0)&&(absListView.getChildAt(i).getTop() == 0);
                    ((MainActivity) activity).setSwipeRefreshLayout(enable);
                    if(i + i1 == i2&&!isloading){
                       loadMore(ApiUtil.BEFORE+date);
                    }
                }
            }
        });
        listViewNews.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int[] startingLocation = new int[2];
                view.getLocationOnScreen(startingLocation);
                startingLocation[0] += view.getWidth()/2;
                Stories stories = (Stories) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(activity, LatestContentActivity.class);
                intent.putExtra(ApiUtil.START_LOCATION,startingLocation);
                intent.putExtra("stories",stories);
                intent.putExtra("isLight",((MainActivity) activity).isLight());
                isRead = true;
                String readSequence = PreUtil.getStringFromDefault(activity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length >= 200) {
                    for (int j = 100; j < splits.length; j++) {
                        sb.append(splits[j] + ",");
                    }
                    readSequence = sb.toString();
                }

                if (!readSequence.contains(stories.getId() + "")) {
                    readSequence = readSequence + stories.getId() + ",";
                }
                PreUtil.putStringToDefault(activity, "read", readSequence);

                startActivity(intent);
                activity.overridePendingTransition(0,0);
            }
        });
        return view;
    }

    private void loadFirst(){
        isloading = true;
        if(HttpUtil.isNetWorkConntected(activity)){
            HttpUtil.get(ApiUtil.LATESTNEWS, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    try {
                      SQLiteDatabase db = ((MainActivity) activity).getCacheDbHelper().getWritableDatabase();
                      db.execSQL("replace into CacheList(date,json) values(" + ApiUtil.LATEST_COLUMN + ",' " + responseString + "')");
                      db.close();
                    parseLatestNewsJson(responseString);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
        }else {
            SQLiteDatabase sqliteDatabase = ((MainActivity) activity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = sqliteDatabase.rawQuery("select * from CacheList where date = " + ApiUtil.LATEST_COLUMN,null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseLatestNewsJson(json);
            }else {
                isloading = false;
            }
            cursor.close();
            sqliteDatabase.close();
        }
    }

    private void parseLatestNewsJson(String responseString){
        Gson gson = new Gson();
        latestNews = gson.fromJson(responseString,LatestNews.class);
        date = latestNews.getDate();
        viewFagment.setLatestTopStories(latestNews.getTop_stories());
        handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Stories> storiesList = latestNews.getStories();
                Stories stories = new Stories();
                stories.setType(ApiUtil.TOPIC);
                stories.setTitle("今日热闻");
                storiesList.add(0,stories);
                mainNewsItemAdapter.addList(storiesList);
                isloading = false;
            }
        });
    }

    private void loadMore(final String url){
        isloading = true;
        if(HttpUtil.isNetWorkConntected(activity)){
            HttpUtil.get(url, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    SQLiteDatabase sqLiteDatabase = ((MainActivity) activity).getCacheDbHelper().getWritableDatabase();
                    sqLiteDatabase.execSQL("replace into CacheList(date,json) values(" + date + ",' " + responseString + "')");
                    sqLiteDatabase.close();
                    parseBeforeJson(responseString);
                }
            });
        } else {
            SQLiteDatabase sqLiteDatabase = ((MainActivity) activity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from CacheList where date = " + date, null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseBeforeJson(json);
            }else {
                sqLiteDatabase.delete("CacheList", "date < " + date, null);
                isloading = false;
                Snackbar snackbar = Snackbar.make(listViewNews,"已经加载所有的",Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(activity,((MainActivity) activity).isLight()? android.R.color.holo_blue_dark :R.color.dark));
                snackbar.show();
            }
            cursor.close();
            sqLiteDatabase.close();
        }
    }

    private void parseBeforeJson(String responseString){
        Gson gson = new Gson();
        beforeNews = gson.fromJson(responseString,BeforeNews.class);
        if( beforeNews == null){
            isloading = false;
        }
        date = beforeNews.getDate();
        handler.post(new Runnable() {
            @Override
            public void run() {
                List<Stories> storiesList = beforeNews.getStories();
                Stories stories = new Stories();
                stories.setType(ApiUtil.TOPIC);
                stories.setTitle(convertDate(date));
                storiesList.add(0,stories);
                mainNewsItemAdapter.addList(storiesList);
                isloading = false;
            }
        });
    }

    @Override
    protected void initData(){
        super.initData();
        loadFirst();
    }

    private String convertDate(String date){
        String result = date.substring(0,4);
        result += "年";
        result += date.substring(4,6);
        result += "月";
        result += date.substring(6,8);
        result += "日";
        return result;
    }

    public boolean isRead(){
        return isRead;
    }

    public void updateTheme(){
        mainNewsItemAdapter.updateTheme();
    }
}
