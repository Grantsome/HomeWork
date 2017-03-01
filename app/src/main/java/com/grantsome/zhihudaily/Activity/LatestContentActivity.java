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

    private boolean isRead = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_latest_content);
        //最开始的定义里面传入的两个参数，第一个是context参数，第二个是版本号
        //获取webCacheDbHelper的实例，该类在Database里面
        webCacheDbHelper = new WebCacheDbHelper(this,1);
        //得到传进来的isLight的Boolean值，判断是否为夜间模式
        isLight = getIntent().getBooleanExtra("isLight",true);
        //得到传进来的stories
        stories = (Stories) getIntent().getSerializableExtra("stories");
        //判断是否为空，原因:当从收藏的界面里面传入的时候，stories为空，但会传入title和id;
        if(stories!=null) {
            //如果不为空就设置分享的题目为stories.getTitle();
            sharedTitle = stories.getTitle();
        }else {
            //令isStoriesEmpty=true,在初始化的时候;
            isStoriesEmpty = true;
            //调试的时候的打印日志
            LogUtil.d("LatestContentActivity","title" + getIntent().getStringExtra("title"));
            //如果为空就设置分享的题目为getIntent传入的title
            sharedTitle = getIntent().getStringExtra("title");
        }
        webView = (WebView) findViewById(R.id.web_view);
        //设置webView的支持JavaScript,默认为False
        webView.getSettings().setJavaScriptEnabled(true);
        //一个普通的网页加载 cache会被检查,内容也会被重新效验。第一次加载网页的时候,内容会被存储到cache本地，设置cache的模式,LOAD_CACHE_ELSE_NETWORK的意思是使用cache里面的资源,不管过期与否。原因：根据知乎日报API接口的特性，每次的都是一样的
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //开启DOM Storage API功能，即设置本地的DOM存储，用于持久化存储，默认为false;
        webView.getSettings().setDomStorageEnabled(true);
        //开启Database Storage API功能；
        webView.getSettings().setDatabaseEnabled(true);
        //开启Application Cache功能
        webView.getSettings().setAppCacheEnabled(true);
        //判断网络是否可用
        if(HttpUtil.isNetWorkConntected(this)){
                if(stories!=null) {
                    //设置分享的时候的链接
                    sharedUrl = ApiUtil.BASEURL+ApiUtil.CONTENT + stories.getId();
                    //调试打印日志
                    LogUtil.d("LatestContentActivity","stories.getTitle()的内容为"+stories.getTitle());
                    //发起网络请求,TextHttpResponseHandler继承与AsyncHttpResponseHandler,重写了了onSuccess和onFailure,将请求结果由byte数组转化为String
                    HttpUtil.get(ApiUtil.CONTENT + stories.getId(), new TextHttpResponseHandler() {
                        @Override
                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, String responseString) {
                            //使用该webCacheDbHelper里面的数据库,写入数据
                            SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getWritableDatabase();
                            //把得到的String里面的  '  全部替换成 ''
                            responseString = responseString.replaceAll("'", "''");
                            //replace方法，把newsId换成了stories.getId(),把json换成了responseString
                            sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + stories.getId() + ",'" + responseString + "')");
                            //关闭数据库
                            sqLiteDatabase.close();
                            //解析数据
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
                            //解析新闻额外信息的json
                            Gson gson = new Gson();
                            extraInfo = gson.fromJson(responseString,ExtraInfo.class);
                            LogUtil.d("LatestActivity","extraInfo的评论数量" + extraInfo.getComments());
                            //初始化额外信息的界面
                            initView(extraInfo);
                        }
                    });
                }
               if(stories == null) {
                   //stories==null的时候得到getIntent里面的code也就是ID
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
                           LogUtil.d("LatestActivity","执行");
                           sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + getIntent().getIntExtra("code", 520) + ",'" + responseString + "')");
                           sqLiteDatabase.close();
                           parseJson(responseString);
                       }
                   });
                   LogUtil.d("LatestContentActivity", "加载新闻内容函数执行完毕");
                   HttpUtil.get(ApiUtil.STORY_EXTRA + getIntent().getIntExtra("code", 520), new TextHttpResponseHandler() {
                       @Override
                       public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                       }

                       @Override
                       public void onSuccess(int statusCode, Header[] headers, String responseString) {
                           LogUtil.d("LatestActivity","当stories==null的时候，加载评论开始执行");
                           SQLiteDatabase sqLiteDatabase = webCacheDbHelper.getWritableDatabase();
                           responseString = responseString.replaceAll("'", "''");
                           sqLiteDatabase.execSQL("replace into Cache(newsId,json) values(" + getIntent().getIntExtra("code", 520) + ",'" + responseString + "')");
                           sqLiteDatabase.close();
                           Gson gson = new Gson();
                           extraInfo = gson.fromJson(responseString,ExtraInfo.class);
                           LogUtil.d("LatestActivity","当stories==null的时候extraInfo的评论数量" + extraInfo.getComments());
                           initView(extraInfo);
                       }
                   });
                   LogUtil.d("LatestContentActivity","加载评论函数已经执行完毕");
               }
        }else {
            if(stories!=null) {
                //在数据库里面读数据
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
    }

    private void initView(ExtraInfo extraInfo){
        imageButtonBack = (ImageButton) findViewById(R.id.image_view_back);
        //点击back的时候返回
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageButtonCollect = (ImageButton) findViewById(R.id.image_button_collect);
        //点击Collect的时候的点击事件
        imageButtonCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置isNeedCollect为true;
                isNeedCollected = true;
                //弹出Toast
                Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                if(isNeedCollected){
                    //实例化Intent并且传入stories
                    Intent intentOne = new Intent(LatestContentActivity.this,CollectActivity.class);
                    intentOne.putExtra("stories",stories);
                    startActivityForResult(intentOne,0);
                }
            }
        });
        imageButtonShare = (ImageButton) findViewById(R.id.image_button_share);
        //点击share的时候的点击事件
        imageButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "正在处理后台,请稍后", Toast.LENGTH_SHORT).show();
                showShare();

                }
        });
        //设置评论的数量
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
        //设置点赞的数量
        textViewParise = (TextView) findViewById(R.id.text_view_praise);
        int num = extraInfoOne.getPopularity();
        //要注意必须使用Integer.toString,不然会报错
        textViewParise.setText(Integer.toString(num));
        textViewParise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numOne = extraInfoOne.getPopularity() + 1;
                textViewParise.setText(Integer.toString(numOne));
            }
        });
    }


    //解析NewsContent的json
    private void parseJson(String responseString){
        Gson gson = new Gson();
        newsContent = gson.fromJson(responseString,NewsContent.class);
        final ImageLoader imageLoader = ImageLoader.getInstance();
        //cacheInMemory,设置下载的图片是否在缓存中CacheOnDisk，设置下载的图片是否在SDcard的内存中
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageView = (ImageView) findViewById(R.id.image_view_title);
        imageLoader.displayImage(newsContent.getImage(),imageView,displayImageOptions);
        //css: rel描述了当前页面与href的关系，这里为引用式样式表/链接外部样式表  styleSheet样式表 href为地址
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        //在html的头部引用css的文件
        String html = "<html><head>" + css + "</head><body>" + newsContent.getBody() + "</body></html>";
        html = html.replace("<div class=\"ima-place-holder\">","");
        webView.loadDataWithBaseURL("x-data://base",html,"text/html","UTF-8",null);
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    //分享
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
