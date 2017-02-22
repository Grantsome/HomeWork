package com.grantsome.zhihudaily.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.grantsome.zhihudaily.Activity.CollectActivity;
import com.grantsome.zhihudaily.Activity.MainActivity;
import com.grantsome.zhihudaily.Database.NewsListItem;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2017/2/14.
 */

public class HomeFragment extends BaseFragment {

    private TextView loginTextView;

    private TextView mainTextView;

    private TextView offlineDownloadTextView;

    private ListView listViewItem;

    private List<NewsListItem> newsListItems;

    private boolean isLight;

    private NewsTypeAdapter newsTypeAdapter;

    private LinearLayout linearLayoutMenu;

    private LinearLayout allMenuLayout;

    private TextView collectTextView;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.home_header,container,false);
        allMenuLayout = (LinearLayout) view.findViewById(R.id.all_home_layout);
        linearLayoutMenu = (LinearLayout) view.findViewById(R.id.menu_linear_layout);
        loginTextView = (TextView) view.findViewById(R.id.login_text_view);
        mainTextView = (TextView) view.findViewById(R.id.main_text_view);
        mainTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               switch (view.getId()){
                   case R.id.main_text_view:
                       ((MainActivity) activity).loadLatestNews();
                       ((MainActivity) activity).closeMenu();
                       break;
               }
            }
        });
        offlineDownloadTextView = (TextView) view.findViewById(R.id.offline_download_text_view);
        offlineDownloadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.offline_download_text_view:
                        ((MainActivity) activity).loadLatestNews();
                        ((MainActivity) activity).closeMenu();
                        break;
                }
            }
        });
        collectTextView = (TextView) view.findViewById(R.id.collect_text_view);
        collectTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Intent intent = new Intent(getActivity(),CollectActivity.class);
                 startActivity(intent);
                ((MainActivity) activity).closeMenu();
            }
        });

        listViewItem = (ListView) view.findViewById(R.id.list_view_item);
        listViewItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left).replace(R.id.frame_layout_content,new NewsFragment(newsListItems.get(i).getId(),newsListItems.get(i).getTitle()),"themeNews").commit();
                ((MainActivity) activity).setCurrentId(newsListItems.get(i).getId());
                ((MainActivity) activity).closeMenu();
            }
        });

        return view;
    }

    @Override
    protected void initData(){
        super.initData();
        isLight = ((MainActivity) activity).isLight();
        newsListItems = new ArrayList<NewsListItem>();
        if (HttpUtil.isNetWorkConntected(activity)) {
            LogUtil.d("HomeFragment","if已执行");
            HttpUtil.get(ApiUtil.THEMES, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode,  headers, response);
                    String json = response.toString();
                    PreUtil.putStringToDefault(activity, ApiUtil.THEMES, json);
                    parseJson(response);
                    LogUtil.d("HomeFragment","JsonHttpResponseHandle已执行");
                }
            });
        }else {
            String json = PreUtil.getStringFromDefault(activity,ApiUtil.THEMES,"");
            try{
                JSONObject jsonObject = new JSONObject(json);
                parseJson(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJson(JSONObject response){
        try{
            JSONArray jsonArray = response.getJSONArray("others");
            for(int i = 0; i < jsonArray.length();i++){
                NewsListItem newsListItem = new NewsListItem();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                newsListItem.setTitle(jsonObject.getString("name"));
                newsListItem.setId(jsonObject.getString("id"));
                newsListItems.add(newsListItem);
            }
            newsTypeAdapter = new NewsTypeAdapter();
            listViewItem.setAdapter(newsTypeAdapter);
            updateTheme();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class NewsTypeAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return newsListItems.size();
        }

        @Override
        public Object getItem(int i) {
            return newsListItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null){
                view = LayoutInflater.from(activity).inflate(R.layout.menu_item,viewGroup,false);
            }
            TextView textViewItem = (TextView) view.findViewById(R.id.text_view_item);
            textViewItem.setTextColor(ContextCompat.getColor(activity,isLight?R.color.dark:R.color.gray));
            textViewItem.setText(newsListItems.get(i).getTitle());
            return view;
        }
    }

    public void updateTheme(){
        isLight = ((MainActivity) activity).isLight();
        linearLayoutMenu.setBackgroundColor(ContextCompat.getColor(activity,isLight? android.R.color.holo_blue_dark:android.R.color.black));
        loginTextView.setTextColor(ContextCompat.getColor(activity,isLight? android.R.color.white:android.R.color.secondary_text_dark));
        allMenuLayout.setBackgroundColor(ContextCompat.getColor(activity,isLight? android.R.color.holo_blue_dark:android.R.color.black));
        offlineDownloadTextView.setTextColor(ContextCompat.getColor(activity,isLight? android.R.color.white:android.R.color.secondary_text_dark));
        mainTextView.setTextColor(ContextCompat.getColor(activity,isLight? android.R.color.holo_blue_dark:android.R.color.black));
        listViewItem.setBackgroundColor(ContextCompat.getColor(activity,isLight? android.R.color.white:android.R.color.black));
        newsTypeAdapter.notifyDataSetChanged();

    }

}

