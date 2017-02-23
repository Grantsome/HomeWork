package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.grantsome.zhihudaily.Fragment.CollectFragment;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.LogUtil;

public class CollectActivity extends AppCompatActivity {

    private Stories stories;

    private boolean isFromLatestContentActivity;

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (requestCode){
            case 0:
                stories = (Stories) intent.getSerializableExtra("stories");
                isFromLatestContentActivity = true;
                setIsFromLatestContentActivity(isFromLatestContentActivity);
                LogUtil.d("CollectActivity","onActivityResult 里面的 requestCode == 0" + stories.getTitle());
                break;
            case 1:
                stories = (Stories) intent.getSerializableExtra("stories");
                isFromLatestContentActivity = false;
                setIsFromLatestContentActivity(isFromLatestContentActivity);
                LogUtil.d("CollectActivity","onActivityResult 里面的 requestCode == 1" + stories.getTitle());
                break;
            default:
                break;
        }
    }


    public void setIsFromLatestContentActivity(boolean isFromLatestContentActivity){
        this.isFromLatestContentActivity = isFromLatestContentActivity;
    }

    public boolean getIsFromLatestContentActivity() {
        return isFromLatestContentActivity;
    }
}
