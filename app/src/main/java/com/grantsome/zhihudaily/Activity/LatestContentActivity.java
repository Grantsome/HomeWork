package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Database.WebCacheDbHelper;
import com.grantsome.zhihudaily.Fragment.BackgroundFragment;
import com.grantsome.zhihudaily.Model.ExtraInfo;
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

public class LatestContentActivity extends AppCompatActivity {

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

    private boolean isStoriesEmpty = false;

    private ExtraInfo extraInfo;

    private ImageButton imageButtonBack;

    private ImageButton imageButtonShare;

    private ImageButton imageButtonCollect;

    private TextView textViewComments;

    private TextView textViewParise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
        webCacheDbHelper = new WebCacheDbHelper(this,1);
        isLight = getIntent().getBooleanExtra("isLight",true);
        stories = (Stories) getIntent().getSerializableExtra("stories");
        //toolbar.setBackgroundColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_dark: android.R.color.background_dark));
        if(stories!=null) {
            //collapsingToolbarLayout.setTitle(stories.getTitle());
            sharedTitle = stories.getTitle();
        }else {
            isStoriesEmpty = true;
            //collapsingToolbarLayout.setTitle(getIntent().getStringExtra("title"));
            LogUtil.d("LatestContentActivity","title" + getIntent().getStringExtra("title"));
            sharedTitle = getIntent().getStringExtra("title");
        }
        //collapsingToolbarLayout.setContentScrimColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_dark:android.R.color.black));
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
                    HttpUtil.get(ApiUtil.STORY_EXTRA + stories.getId(), new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getWritableDatabase();
                            responseString = responseString.replaceAll("'", "''");
                            sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + stories.getId() + ",'" + responseString + "')");
                            sqLiteDatabase.close();
                            Gson gson = new Gson();
                            extraInfo = gson.fromJson(responseString,ExtraInfo.class);
                            LogUtil.d("LatestActivity","extraInfo的评论数量" + extraInfo.getComments());
                            initView(extraInfo);
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

        //setBackgroundFragment(savedInstanceState);
    }

    private void initView(ExtraInfo extraInfo){
        imageButtonBack = (ImageButton) findViewById(R.id.image_view_back);
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageButtonCollect = (ImageButton) findViewById(R.id.image_button_collect);
        imageButtonCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNeedCollected = true;
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                if(isNeedCollected){
                    Intent intentOne = new Intent(LatestContentActivity.this,CollectActivity.class);
                    intentOne.putExtra("stories",stories);
                    startActivityForResult(intentOne,0);
                }
            }
        });
        imageButtonShare = (ImageButton) findViewById(R.id.image_button_share);
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "正在处理后台,请稍后", Toast.LENGTH_SHORT).show();
                showShare();

                }
        });
        textViewComments = (TextView) findViewById(R.id.text_view_comment);
        if(extraInfo==null){
            LogUtil.d("LatestContentActivity","extraInfo内容为空");
        }
        LogUtil.d("LatestContentActivity","extraInfo.getShort_comments() " + extraInfo.getShort_comments());
        final ExtraInfo extraInfoOne = extraInfo;
        int number = extraInfoOne.getShort_comments() + extraInfoOne.getLong_comments();
        textViewComments.setText(Integer.toString(number));
        textViewComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentTwo = new Intent(LatestContentActivity.this,CommentsActivity.class);
                intentTwo.putExtra("stories.getId()",isStoriesEmpty? getIntent().getIntExtra("code", 520):stories.getId());
                intentTwo.putExtra("extraInfo", extraInfoOne);
                LogUtil.d("LatestActivity","extraInfo里面的内容为" + extraInfoOne.getComments()+ "长评论"+extraInfoOne.getLong_comments()+"短评论"+extraInfoOne.getPopularity()+extraInfoOne.getShort_comments());
                startActivity(intentTwo);
            }
        });
        textViewParise = (TextView) findViewById(R.id.text_view_praise);
        int num = extraInfoOne.getPopularity();
        textViewParise.setText(Integer.toString(num));
        textViewParise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOne = extraInfoOne.getPopularity() + 1;
                textViewParise.setText(Integer.toString(numOne));
            }
        });
    }


    private void parseJson(String responseString){
        Gson gson = new Gson();
        newsContent = gson.fromJson(responseString,NewsContent.class);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageView = (ImageView) findViewById(R.id.image_view_title);
        imageLoader.displayImage(newsContent.getImage(),imageView,displayImageOptions);
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + newsContent.getBody() + "</body></html>";
        html = html.replace("<div class=\"ima-place-holder\">","");
        webView.loadDataWithBaseURL("x-data://base",html,"text/html","UTF-8",null);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
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
