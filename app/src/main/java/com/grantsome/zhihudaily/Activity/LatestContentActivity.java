package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;


/**
 * Created by tom on 2017/2/18.
 */

public class LatestContentActivity extends AppCompatActivity implements BackgroundFragment.OnStateChangeListener{

    private WebView webView;

    private WebCacheDbHelper webCacheDbHelper;

    private boolean isLight;

    private CoordinatorLayout coordinatorLayout;

    private BackgroundFragment backgroundFragment;

    private Stories stories;

    private ImageView imageView;

    private NewsContent newsContent;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    private boolean isNeedCollected = false;

    private String sharedTitle;

    private String sharedUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
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
        imageView = (ImageView) findViewById(R.id.image_view_title);
        stories = (Stories) getIntent().getSerializableExtra("stories");
        //toolbar.setBackgroundColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_dark: android.R.color.background_dark));
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        if(stories!=null) {
            collapsingToolbarLayout.setTitle(stories.getTitle());
            sharedTitle = stories.getTitle();
        }else {
            collapsingToolbarLayout.setTitle(getIntent().getStringExtra("title"));
            LogUtil.d("LatestContentActivity","title" + getIntent().getStringExtra("title"));
            sharedTitle = getIntent().getStringExtra("title");
        }
        collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_dark:android.R.color.black));
        webView = (WebView) findViewById(R.id.web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        if(HttpUtil.isNetWorkConntected(this)){
                if(stories!=null) {
                    sharedUrl = ApiUtil.BASEURL+ApiUtil.CONTENT + stories.getId();
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
                }
               if(stories == null) {
                   sharedUrl = ApiUtil.BASEURL+ApiUtil.CONTENT + getIntent().getIntExtra("code", 520);
                   LogUtil.d("LatestContentActivity", "stories为空，所以getIntent为" + getIntent().getIntExtra("code", 520));
                   HttpUtil.get(ApiUtil.CONTENT + getIntent().getIntExtra("code", 520), new TextHttpResponseHandler() {
                       @Override
                       public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                       }

                       @Override
                       public void onSuccess(int statusCode, Header[] headers, String responseString) {
                           SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getWritableDatabase();
                           responseString = responseString.replaceAll("'", "''");
                           sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + getIntent().getIntExtra("code", 520) + ",'" + responseString + "')");
                           sqLiteDatabase.close();
                           parseJson(responseString);
                       }
                   });
               }
        }else {
            if(stories!=null) {
                SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Cache where newsId = " + stories.getId(), null);
                if (cursor.moveToFirst()) {
                    String json = cursor.getString(cursor.getColumnIndex("json"));
                    parseJson(json);
                }
                cursor.close();
                sqLiteDatabase.close();
            }
            if(stories == null){
                SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getReadableDatabase();
                Cursor cursor = sqLiteDatabase.rawQuery("select * from Cache where newsId = " + getIntent().getIntExtra("code", 520), null);
                if (cursor.moveToFirst()) {
                    String json = cursor.getString(cursor.getColumnIndex("json"));
                    parseJson(json);
                }
                cursor.close();
                sqLiteDatabase.close();
            }
        }

        setBackgroundFragment(savedInstanceState);
    }

    private void parseJson(String responseString){
        Gson gson = new Gson();
        newsContent = gson.fromJson(responseString,NewsContent.class);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageLoader.displayImage(newsContent.getImage(),imageView,displayImageOptions);
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
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu){
        //实例化菜单
        getMenuInflater().inflate(R.menu.content_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(LatestContentActivity.this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.action_share:
                Toast.makeText(this, "正在处理后台,请稍后", Toast.LENGTH_SHORT).show();
                showShare();
                break;
            case R.id.action_collect:
                if(menuItem.getTitle().equals("收藏")) {
                    isNeedCollected = true;
                    menuItem.setTitle("取消收藏");
                    Toast.makeText(this, "已收藏", Toast.LENGTH_SHORT).show();
                    if(isNeedCollected){
                        Intent intentOne = new Intent(LatestContentActivity.this,CollectActivity.class);
                        intentOne.putExtra("stories",stories);
                        startActivity(intentOne);
                        //intent.putExtra("ApiUtil.CONTENT + stories.getId()",ApiUtil.CONTENT + stories.getId());
                    }
                }else {
                    isNeedCollected = false;
                    menuItem.setTitle("收藏");
                    Toast.makeText(this, "已取消收藏", Toast.LENGTH_SHORT).show();
                }
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

    private void showShare(){
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(sharedTitle);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(sharedUrl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText("分享链接" + sharedUrl + sharedTitle);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(sharedUrl);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(null);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(sharedUrl);
        oks.setImageUrl(null);
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("QZone".equals(platform.getName())) {
                    paramsToShare.setTitle(sharedTitle);
                    paramsToShare.setTitleUrl(sharedUrl);
                }
                if ("SinaWeibo".equals(platform.getName())) {
                    paramsToShare.setUrl(sharedUrl);
                    paramsToShare.setText("分享链接" + sharedTitle + sharedUrl);
                }
                if ("Wechat".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(),R.drawable.ssdk_logo);
                    paramsToShare.setImageData(imageData);
                    //paramsToShare.setTitle("分享链接" + sharedTitle + sharedUrl);
                }
                if ("WechatMoments".equals(platform.getName())) {
                    Bitmap imageData = BitmapFactory.decodeResource(getResources(),R.drawable.ssdk_logo);
                    paramsToShare.setImageData(imageData);
                    //paramsToShare.setTitle("分享链接" + sharedTitle + sharedUrl);
                }

            }
        });

// 启动分享GUI
        oks.show(this);
    }

}
