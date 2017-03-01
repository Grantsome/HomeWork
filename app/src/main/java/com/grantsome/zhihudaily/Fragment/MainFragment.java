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
import com.grantsome.zhihudaily.Util.LogUtil;
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
        //设置toolbar的标题
        ((MainActivity) activity).setToolBarTitle("首页");
        View view = inflater.inflate(R.layout.news_item, container, false);
        listViewNews = (ListView) view.findViewById(R.id.list_view_item);
        View v = inflater.inflate(R.layout.view_fragment,listViewNews,false);
        viewFagment = (ViewFragment) v.findViewById(R.id.view_fragment);
        viewFagment.setOnItemClickListener(new ViewFragment.OnItemClickListener() {
            @Override
            public void click(View v, LatestNews.TopStoriesBean topStoriesBean) {
                Stories stories = new Stories();
                stories.setId(topStoriesBean.getId());
                stories.setTitle(topStoriesBean.getTitle());
                Intent intent = new Intent(activity, LatestContentActivity.class);
                isRead = true;
                intent.putExtra("isRead",isRead);
                intent.putExtra("isLight",((MainActivity) activity).isLight());
                intent.putExtra("stories",stories);
                LogUtil.d("MainFragment","isread"+isRead);
                LogUtil.d("MainFragment","传递过去的stories.getTitle内容为"+stories.getTitle());
                startActivity(intent);
                //activity之间的动画切换方式,第一个是进入的动画,第二个是退出的动画
                activity.overridePendingTransition(0,0);
            }
        });
        //把listView的Header设置为v
        listViewNews.addHeaderView(v);
        mainNewsItemAdapter = new MainNewsItemAdapter(activity);
        //设置adapter
        listViewNews.setAdapter(mainNewsItemAdapter);
        //listView的滑动事件
        listViewNews.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            //第一个参数：第一个可见项是listView的第几项
            //第二个参数: 可见项的总数
            //第三个参数: 总项数
            //listViewNews.getChildCount是显示的所包含的子项的个数
            //指定的某一条新闻即：view.getChildAt(i)
            //获取listView顶部的距离Y
            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                if(listViewNews!=null&&listViewNews.getChildCount()>0){
                    boolean enable = (i ==0)&&(absListView.getChildAt(i).getTop() == 0);
                    ((MainActivity) activity).setSwipeRefreshLayout(enable);
                    if(i + i1 == i2&&!isloading){
                        //加载历史数据
                       loadMore(ApiUtil.BEFORE+date);
                    }
                }
            }
        });
        listViewNews.setBackgroundColor(ContextCompat.getColor(activity,R.color.white));
        //给listViewNews里面的点击事件
        listViewNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //实例化stories,目的是后面的传值
                Stories stories = (Stories) adapterView.getAdapter().getItem(i);
                Intent intent = new Intent(activity, LatestContentActivity.class);
                intent.putExtra("stories",stories);
                intent.putExtra("isLight",((MainActivity) activity).isLight());
                isRead = true;
                intent.putExtra("isRead",isRead);
                //自己定义的getStringFromDefault方法,在util文件下的PreUtil里面
                //主要是利用了SharedPreference实现已读和未读的区分
                //首先加载以前的读过的新闻的id;
                String readSequence = PreUtil.getStringFromDefault(activity, "read", "");
                //给readSequence里面的id用逗号,隔开
                String[] splits = readSequence.split(",");
                StringBuffer sb = new StringBuffer();
                //如果当读过的记录有了70条以上,那么就清空前35条读过的标记,并且还是以逗号隔开
                if (splits.length >= 70) {
                    for (int j = 35; j < splits.length; j++) {
                        sb.append(splits[j] + ",");
                    }
                    readSequence = sb.toString();
                }
                //当读过的记录里面没有刚刚点开的id的时候,加入id
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

    //初始化的ThemeNews
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

    //加载最新消息
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
                //向storiesList里面加入数据
                storiesList.add(0,stories);
                //把storiesList里面的数据加入到Adapter的方法里面。
                mainNewsItemAdapter.addList(storiesList);
                isloading = false;
            }
        });
    }

    //加载更多
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

    //加载历史消息
    private void parseBeforeJson(String responseString){
        Gson gson = new Gson();
        beforeNews = gson.fromJson(responseString,BeforeNews.class);
        if( beforeNews == null){
            isloading = false;
        }
        date = beforeNews.getDate();
        //使用handler.post执行在UI线程更新时候的代码
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
        //前四位为年份 2017 + “年”
        String result = date.substring(0,4);
        result += "年";
        //后面两位为 02 + “月”
        result += date.substring(4,6);
        result += "月";
        //最后两位为 15 + “日”
        result += date.substring(6,8);
        result += "日";
        return result;
    }

    public void updateTheme(){
        mainNewsItemAdapter.updateTheme();
    }
}
