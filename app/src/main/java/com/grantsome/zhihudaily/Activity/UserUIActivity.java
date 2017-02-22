package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.LogUtil;

public class UserUIActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ui);
        toolbar = (Toolbar) findViewById(R.id.toobar);
        toolbar.setBackgroundColor(ContextCompat.getColor(this,android.R.color.holo_blue_dark));
        setSupportActionBar(toolbar);
        setupActionBar();

    }

    private void setupActionBar(){
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home_ui);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.usermenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        int id = menuItem.getItemId();
        if(id == android.R.id.home) {
            Intent intent = new Intent(UserUIActivity.this,MainActivity.class);
            intent.putExtra("isLogin",isLogin);
            startActivityForResult(intent,520);
            LogUtil.d("UserUIActivity","传值isLogin成功");
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

}
