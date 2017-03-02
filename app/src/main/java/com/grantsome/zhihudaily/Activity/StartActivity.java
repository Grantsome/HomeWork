package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Model.StartImageInfo;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tom on 2017/2/11.
 */

public class StartActivity extends AppCompatActivity {

    //开机的进度条
    private ProgressBar progressBar;

    private ImageView imageViewStart;

    private StartImageInfo startImageInfo;

    private ImageLoader imageLoader;

    private TextView textViewStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //实例化progressBar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        imageViewStart = (ImageView) findViewById(R.id.image_view_start);
        imageLoader = ImageLoader.getInstance();
        textViewStart  = (TextView) findViewById(R.id.text_view_author);
        initImage();
        //跳转到MainActivity
        final Intent intent = new Intent(StartActivity.this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //定时跳转
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //销毁StartActivity，使得按住BACK键的时候直接退出程序
                finish();
                startActivity(intent);
            }
        };
        timer.schedule(timerTask,1000*2);//定时两秒
    }

    private void initImage(){
        if(HttpUtil.isNetWorkConntected(this)){
            HttpUtil.getImage(ApiUtil.START, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                   String response = responseString.toString();
                   parseToImage(response);
                }
            });
        }else {
            //imageViewStart.setImageResource(R.drawable.start_ui);
        }
    }

    private void parseToImage(String response){
        Gson gson = new Gson();
        startImageInfo = gson.fromJson(response, StartImageInfo.class);
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        int i=0;
        imageViewStart = (ImageView) findViewById(R.id.image_view_start);
        textViewStart = (TextView) findViewById(R.id.text_view_author);
        LogUtil.d("StartActivity","url"+startImageInfo.getCreatives().get(i).getUrl());
        imageLoader.displayImage(startImageInfo.getCreatives().get(i).getUrl(),imageViewStart,displayImageOptions);
        textViewStart.setText(startImageInfo.getCreatives().get(i).getText());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
    }

}


