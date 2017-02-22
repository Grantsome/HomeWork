package com.grantsome.zhihudaily.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grantsome.zhihudaily.Fragment.CollectFragment;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.LogUtil;

public class CollectActivity extends AppCompatActivity {

    private Stories stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        stories = (Stories) getIntent().getSerializableExtra("stories");
        LogUtil.d("CollectActivity","stories的内容为" + stories);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left).replace(R.id.frame_layout_content,new CollectFragment()).commit();

    }

    public Stories getStories(){
        return stories;
    }


}
