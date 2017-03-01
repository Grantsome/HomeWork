package com.grantsome.zhihudaily.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Activity.MainActivity;
import com.grantsome.zhihudaily.Activity.NewsContentActivity;
import com.grantsome.zhihudaily.Adapter.NewsItemAdapter;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.Model.ThemeNews;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;


/**
 * Created by tom on 2017/2/15.
 * 这一段代码和MainFragment里面的差不多,注释也就不写了
 */
@SuppressLint("ValidFragment")
public class NewsFragment extends BaseFragment {

    private ImageLoader imageLoader;

    private ListView listViewNews;

    private ImageView imgaeViewTitle;

    private TextView textViewTitle;

    private String urlId;

    private String title;

    private ThemeNews themeNews;

    private NewsItemAdapter newsItemAdapter;

    private boolean isRead;

    public NewsFragment(String urlId, String title){
        this.urlId = urlId;
        this.title = title;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((MainActivity) activity).setToolBarTitle(title);
        View view = inflater.inflate(R.layout.news_item,container,false);
        imageLoader = ImageLoader.getInstance();
        listViewNews = (ListView) view.findViewById(R.id.list_view_item);
        final View top = LayoutInflater.from(activity).inflate(R.layout.top_news_item,listViewNews,false);
        textViewTitle = (TextView) top.findViewById(R.id.top_news_item_title);
        imgaeViewTitle = (ImageView) top.findViewById(R.id.top_news__item_image);
        listViewNews.addHeaderView(top);
        listViewNews.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Stories stories = (Stories) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(activity, NewsContentActivity.class);
                isRead = true;
                intent.putExtra("stories",stories);
                intent.putExtra("isLight",((MainActivity) activity).isLight());
                String readSequence = PreUtil.getStringFromDefault(activity, "read", "");
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                if (splits.length >= 70) {
                    for (int j = 35; j < splits.length; j++) {
                        sb.append(splits[j] + ",");
                    }
                    readSequence = sb.toString();
                }

                if(stories!=null) {

                    if (!readSequence.contains(stories.getId() + "")) {
                        readSequence = readSequence + stories.getId() + ",";
                    }
                    PreUtil.putStringToDefault(activity, "read", readSequence);

                    textViewTitle.setTextColor(ContextCompat.getColor(activity, R.color.click));

                    startActivity(intent);
                    activity.overridePendingTransition(0, 0);
                }
                if(stories == null){
                    Toast.makeText(getContext(), "点击此图片或文字无其他内容，请勿重试", Toast.LENGTH_SHORT).show();
                }
            }

        });
        listViewNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int i, int i1, int i2) {
              if(listViewNews!=null&& listViewNews.getChildCount()>0){
                  boolean isEnable = (i==0) &&(view.getChildAt(i).getTop()==0);
                  ((MainActivity) activity).setSwipeRefreshLayout(isEnable);
              }
            }
        });
        return view;
    }

    @Override
    protected void initData(){
        super.initData();
        if(HttpUtil.isNetWorkConntected(activity)){
            HttpUtil.get(ApiUtil.THEMENEWS + urlId, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                  try {
                      SQLiteDatabase sqliteDatabase = ((MainActivity) activity).getCacheDbHelper().getWritableDatabase();
                      sqliteDatabase.execSQL("replace into CacheList(date,json) values(" + (ApiUtil.BASE_COLUMN + Integer.parseInt(urlId)) + ",' " + responseString + "')");
                      sqliteDatabase.close();
                      parseJson(responseString);
                  } catch (Exception e){
                      e.printStackTrace();
                  }
                }
            });
        } else {
            SQLiteDatabase sqliteDatabase = ((MainActivity) activity).getCacheDbHelper().getReadableDatabase();
            Cursor cursor = sqliteDatabase.rawQuery("select * from CacheList where date = " + (ApiUtil.BASE_COLUMN +Integer.parseInt(urlId)),null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            sqliteDatabase.close();
        }
    }

    private void parseJson(String responseString){
        Gson gson = new Gson();
        themeNews = gson.fromJson(responseString,ThemeNews.class);
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        textViewTitle.setText(themeNews.getDescription());
        imageLoader.displayImage(themeNews.getImage(),imgaeViewTitle,displayImageOptions);
        newsItemAdapter = new NewsItemAdapter(activity,themeNews.getStories());
        listViewNews.setAdapter(newsItemAdapter);
    }

    public void updateTheme(){
        newsItemAdapter.updateTheme();
    }

}
