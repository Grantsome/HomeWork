package com.grantsome.zhihudaily.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * Created by tom on 2017/2/14.
 */

public abstract class BaseFragment extends Fragment {

    public Activity activity;

    public Fragment fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        activity = getActivity();
        return initView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        activity = null;
    }

    protected void initData(){

    }

    protected abstract View initView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState);

}
