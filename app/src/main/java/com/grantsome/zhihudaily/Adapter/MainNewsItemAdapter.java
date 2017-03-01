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
import com.grantsome.zhihudaily.Util.LogUtil;
import com.grantsome.zhihudaily.Util.PreUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2017/2/16.
 *
 * BaseAdapter里面实现步骤：
 * 1、GridView准备绘制
 * 2、调用getCount()获取数据个数,设置为n
 * 3、初始化position=0
 * 4、使用getView()返回的当前的position对应的View
 * 5、position +=1
 * 6、判断position<n?，是的话返回步骤4，否的话执行步骤7
 * 7、结束
 *
 * 各个函数的主要功能:
 * 1、getCount()决定了我们要绘制的资源个数(必须小于等于最大值)
 * 2、getView()通过传入的参数的position,加工成为我们想要的View并返回,供GridView使用
 * 3、getItemId()决定了第position处的列表项的id,在某些方法里面需要ID这个参数
 * (比如在监听器事件里面会用到: public void onItemClick(AdapterView<?>) parent,View view,int position,long id)
 * 4、getItem()返回当前IItem里面的数据,方便在Activity里面的onItemClick方法中调用,点击事件的时候在AdapterView里面的getItemAtPosition()调用的
 * */

public class MainNewsItemAdapter extends BaseAdapter{

    private List<Stories> storiesList;

    private Context context;

    private ImageLoader imageLoader;

    private DisplayImageOptions displayImageOptions;

    private boolean isLight = true;

    private boolean isRead = false;

    private Fragment fragment;

    //调用父类的构造器
    public MainNewsItemAdapter(Context context){
        this.context = context;
        this.storiesList = new ArrayList<>();
       // this.isLight = ((MainActivity) context).isLight();
        this.displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        imageLoader = ImageLoader.getInstance();
    }

    //让其他的stories add到这上面来
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

    //每当界面显示出来一个item的时候,就会调用该方法,第一个参数表示item在adapter里面的位置
    //第二个参数是item的View的对象,第三个参数加载xml视图
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){//党list滑动的时候,如果item没有消失这时候的参数对象view是没有任何指向的,所以view==null
            viewHolder = new ViewHolder();
            //利用layoutInflater类的布局加载器动态加载实例化对象
            view = LayoutInflater.from(context).inflate(R.layout.today_hot_news_item,viewGroup,false);
            viewHolder.textViewTopic = (TextView) view.findViewById(R.id.text_view_topic);
            viewHolder.imageViewTitle = (ImageView) view.findViewById(R.id.today_hot_news_item_image);
            viewHolder.textViewTitle = (TextView)  view.findViewById(R.id.today_hot_news_item_title);
            //向view之中添加数据信息,也就是两个textView对象和一个ImageView对象
            view.setTag(viewHolder);
        } else{
            //如果有之前的view的对象返回,即滑动list的时候有Item消失,从该view之中提取已经创建的两个textView对象,达到view对象数据的循环使用
            viewHolder = (ViewHolder) view.getTag();
        }
        //是否为读过的新闻
        String readSeq = PreUtil.getStringFromDefault(context, "read", "");
        //加上一个stories.getId()后面的"",是因为这里需要的是Char，一个int是不合符的
        if(readSeq.contains(storiesList.get(i).getId() + "")){
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.click));
        } else {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,isLight? android.R.color.black:android.R.color.white));
        }

        ((FrameLayout) viewHolder.imageViewTitle.getParent().getParent().getParent()).setBackgroundColor(ContextCompat.getColor(context,isLight? R.color.white:R.color.dark));
        viewHolder.textViewTopic.setTextColor(ContextCompat.getColor(context,isLight? android.R.color.darker_gray:R.color.white));
        //实例化stories,目的是给对应的textView和imageView赋值
        Stories stories = storiesList.get(i);
        if(stories.getType() == ApiUtil.TOPIC){
            ((LinearLayout) viewHolder.textViewTopic.getParent()).setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
            viewHolder.textViewTitle.setVisibility(View.GONE);
            viewHolder.imageViewTitle.setVisibility(View.GONE);
            viewHolder.textViewTopic.setVisibility(View.VISIBLE);
            viewHolder.textViewTopic.setText(stories.getTitle());
            LogUtil.d("MainNewsAdapter","type==topic");
        } else {
            ((LinearLayout) viewHolder.textViewTopic.getParent()).setBackgroundResource(isLight?R.drawable.light:R.drawable.dark);
            viewHolder.textViewTopic.setVisibility(View.GONE);
            viewHolder.textViewTitle.setVisibility(View.VISIBLE);
            viewHolder.imageViewTitle.setVisibility(View.VISIBLE);
            viewHolder.textViewTitle.setText(stories.getTitle());
            imageLoader.displayImage(stories.getImages().get(0),viewHolder.imageViewTitle,displayImageOptions);
            LogUtil.d("MainNewsAdapter","type!=topic");
        }
        return view;
    }

    //暂存textView和ImageView的实例化对象,达到循环使用
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
