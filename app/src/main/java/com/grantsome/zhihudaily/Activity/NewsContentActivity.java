package com.grantsome.zhihudaily.Activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Database.WebCacheDbHelper;
import com.grantsome.zhihudaily.Fragment.BackgroundFragment;
import com.grantsome.zhihudaily.Model.NewsContent;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class NewsContentActivity extends AppCompatActivity implements BackgroundFragment.OnStateChangeListener {

    private WebView webView;

    private WebCacheDbHelper webCacheDbHelper;

    private boolean isLight;

    private CoordinatorLayout coordinatorLayout;

    private BackgroundFragment backgroundFragment;

    private Stories stories;

    private Context context;

    private NewsContent newsContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        webCacheDbHelper = new WebCacheDbHelper(this,1);
        isLight = getIntent().getBooleanExtra("isLight",true);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        coordinatorLayout.setVisibility(View.INVISIBLE);
        backgroundFragment = (BackgroundFragment) findViewById(R.id.background_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toobar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_dark: android.R.color.background_dark));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_ui);
        stories = (Stories) getIntent().getSerializableExtra("stories");
        if(stories!=null){
            LogUtil.d("NewsContentActivity","stories不为空但是你还是看不见");
        }
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        if(HttpUtil.isNetWorkConntected(this)){
            try {
                HttpUtil.get(ApiUtil.CONTENT + stories.getId(), new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getWritableDatabase();
                        responseString = responseString.replaceAll("'", "''");
                        sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + stories.getId() + ",'" + responseString + "')");
                        sqLiteDatabase.close();
                        parseJson(responseString);
                    }
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }else {
            SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getReadableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery("select * from Cache Where newsId = " + stories.getId() ,null);
            if(cursor.moveToFirst()){
                String json = cursor.getString(cursor.getColumnIndex("json"));
                parseJson(json);
            }
            cursor.close();
            sqLiteDatabase.close();
        }
        setBackgroundFragment(savedInstanceState);
    }

    private void parseJson(String responseString){
        Gson gson = new Gson();
        newsContent = gson.fromJson(responseString,NewsContent.class);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsContent.getBody() + "</body></html>";
        html = html.replace("<div class=\"ima-place-holder\">","");
        webView.loadDataWithBaseURL("x-data://base",html,"text/html","UTF-8",null);
    }

    private void setBackgroundFragment(Bundle savedInstanceState){
        backgroundFragment.setOnStateChangeListener(this);
        if(savedInstanceState == null){
            final int[] startingLocation = getIntent().getIntArrayExtra(ApiUtil.START_LOCATION);
            backgroundFragment.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    backgroundFragment.getViewTreeObserver().removeOnPreDrawListener(this);
                    backgroundFragment.startFromLocation(startingLocation);
                    return true;
                }
            });
        } else {
            backgroundFragment.setToFinishedFrame();
        }
    }

    @Override
    public void onStateChange(int state){
        if(BackgroundFragment.STATE_FINISHED == state){
            coordinatorLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //实例化菜单
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(NewsContentActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_share:
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_collect:
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_comment:
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_parse:
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
       return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }
}
