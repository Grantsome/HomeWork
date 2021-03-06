package com.grantsome.zhihudaily.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * 依赖了android-async-http框架，以发起网络请求，请求网络数据
 * Created by tom on 2017/2/12.
 */

public class HttpUtil {

    //官方建议使用的一个静态的AsyncHttpClient();
    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandlerInterface){
        asyncHttpClient.get(ApiUtil.BASEURL+url,responseHandlerInterface);
    }

    public static void getImage(String url,ResponseHandlerInterface responseHandlerInterface){
        asyncHttpClient.get(url,responseHandlerInterface);
    }

    //判断网络是否可用
    public static boolean isNetWorkConntected(Context context){
        if(context!=null){
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                return networkInfo.isAvailable();
            }
        }
        return false;
    }
}
