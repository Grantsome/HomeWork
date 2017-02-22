package com.grantsome.zhihudaily.Util;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 加载图片的工具类
 * Created by tom on 2017/2/14.
 */

public class UILUtil extends Application{

    @Override
    public void onCreate(){
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    //加载图片
    private void initImageLoader(Context context){
        ImageLoaderConfiguration imageLoaderConfiguration = ImageLoaderConfiguration.createDefault(context);
        ImageLoader.getInstance().init(imageLoaderConfiguration);
    }

}
