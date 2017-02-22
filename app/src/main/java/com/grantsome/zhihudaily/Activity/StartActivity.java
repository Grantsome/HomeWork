package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.grantsome.zhihudaily.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by tom on 2017/2/11.
 */

public class StartActivity extends AppCompatActivity {

    //开机的进度条
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //实例化progressBar
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
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

    @Override
    public void onDestroy(){
        super.onDestroy();
        finish();
    }

}


