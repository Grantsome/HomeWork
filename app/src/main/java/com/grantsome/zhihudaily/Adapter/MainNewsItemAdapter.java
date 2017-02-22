package com.grantsome.zhihudaily.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grantsome.zhihudaily.Activity.MainActivity;
import com.grantsome.zhihudaily.Model.Stories;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2017/2/16.
 */

public class MainNewsItemAdapter extends BaseAdapter{

    private List<Stories> storiesList;

    private Context context;

    private ImageLoader imageLoader;

    private DisplayImageOptions displayImageOptions;

    private boolean isLight = true;

    private boolean isRead = false;

    private Fragment fragment;

    public MainNewsItemAdapter(Context context){
        this.context = context;
        this.storiesList = new ArrayList<>();
       // this.isLight = ((MainActivity) context).isLight();
        this.displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageLoader = ImageLoader.getInstance();
    }

    public void addList(List<Stories> items){
        this.storiesList.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return storiesList.size();
    }

    @Override
    public Object getItem(int i) {
        return storiesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.today_hot_news_item,viewGroup,false);
            viewHolder.textViewTopic = (TextView) view.findViewById(R.id.text_view_topic);
            viewHolder.imageViewTitle = (ImageView) view.findViewById(R.id.today_hot_news_item_image);
            viewHolder.textViewTitle = (TextView)  view.findViewById(R.id.today_hot_news_item_title);
            view.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) view.getTag();
        }
        String readSeq = PreUtil.getStringFromDefault(context, "read", "");
        if(readSeq.contains(storiesList.get(i).getId()+"")){
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.click));
        } else {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,isLight? android.R.color.black:android.R.color.white));
        }

        ((FrameLayout) viewHolder.imageViewTitle.getParent().getParent().getParent()).setBackgroundColor(ContextCompat.getColor(context,isLight? R.color.white:R.color.dark));
        viewHolder.textViewTopic.setTextColor(ContextCompat.getColor(context,isLight? android.R.color.darker_gray:R.color.white));
        Stories stories = storiesList.get(i);
        if(stories.getType() == ApiUtil.TOPIC){
            ((LinearLayout) viewHolder.textViewTopic.getParent()).setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
            viewHolder.textViewTitle.setVisibility(View.GONE);
            viewHolder.imageViewTitle.setVisibility(View.GONE);
            viewHolder.textViewTopic.setVisibility(View.VISIBLE);
            viewHolder.textViewTopic.setText(stories.getTitle());

        } else {
            ((LinearLayout) viewHolder.textViewTopic.getParent()).setBackgroundResource(isLight?R.drawable.light:R.drawable.dark);
            viewHolder.textViewTopic.setVisibility(View.GONE);
            viewHolder.textViewTitle.setVisibility(View.VISIBLE);
            viewHolder.imageViewTitle.setVisibility(View.VISIBLE);
            viewHolder.textViewTitle.setText(stories.getTitle());
            imageLoader.displayImage(stories.getImages().get(0),viewHolder.imageViewTitle,displayImageOptions);
        }
        return view;
    }

    public static class ViewHolder{
        TextView textViewTitle;
        ImageView imageViewTitle;
        TextView textViewTopic;
    }

    public void updateTheme(){
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }
}
