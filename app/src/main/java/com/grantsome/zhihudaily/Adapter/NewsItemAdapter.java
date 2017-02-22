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
import com.grantsome.zhihudaily.Util.LogUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by tom on 2017/2/15.
 */

public class NewsItemAdapter extends BaseAdapter {

    private List<Stories> storiesList;

    private Context context;

    private ImageLoader imageLoader;

    private DisplayImageOptions displayImageOptions;

    private boolean isLight;

    private boolean isRead = false;

    private Fragment fragment;

    public NewsItemAdapter(Context context, List<Stories> storiesList){
          this.context = context;
          this.storiesList = storiesList;
          this.imageLoader = ImageLoader.getInstance();
          this.isLight = ((MainActivity) context).isLight();
          this.displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
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
            viewHolder.imageViewTitle = (ImageView) view.findViewById(R.id.today_hot_news_item_image);
            viewHolder.textViewTitle = (TextView)  view.findViewById(R.id.today_hot_news_item_title);
            view.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder) view.getTag();
            LogUtil.d("NewsItemAdapter","else里面getTag()是这个东西：" + viewGroup);
        }

        String readSeq = PreUtil.getStringFromDefault(context, "read", "");
        if (readSeq.contains(storiesList.get(i).getId() + "")) {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.click));
        } else {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,isLight ? android.R.color.black : android.R.color.white));
        }

        ((FrameLayout) viewHolder.imageViewTitle.getParent().getParent().getParent()).setBackgroundResource(isLight? R.color.white:R.color.dark);
        ((LinearLayout) viewHolder.textViewTitle.getParent()).setBackgroundResource(isLight ? R.drawable.light:R.drawable.dark);
        Stories stories = storiesList.get(i);
        viewHolder.textViewTitle.setText(stories.getTitle());
        if(stories.getImages()!=null){
            viewHolder.imageViewTitle.setVisibility(View.VISIBLE);
            imageLoader.displayImage(stories.getImages().get(0),viewHolder.imageViewTitle,displayImageOptions);
        } else {
            viewHolder.imageViewTitle.setVisibility(View.GONE);
        }
        return view;
    }

    public static class ViewHolder{
        TextView textViewTitle;
        ImageView imageViewTitle;
    }

    public void updateTheme(){
        isLight = ((MainActivity) context).isLight();
        notifyDataSetChanged();
    }
}
