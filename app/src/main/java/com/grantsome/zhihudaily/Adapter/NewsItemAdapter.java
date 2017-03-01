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
import com.grantsome.zhihudaily.Util.PreUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by tom on 2017/2/15.
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
 */

public class NewsItemAdapter extends BaseAdapter {

    private List<Stories> storiesList;

    private Context context;

    private ImageLoader imageLoader;

    private DisplayImageOptions displayImageOptions;

    private boolean isLight;

    private boolean isRead = false;

    private Fragment fragment;

    //调用父类构造器
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

    //每一个界面生成一个Item的时候,就会调用此方法,第一个参数表示的是item在adapter里面的位置
    //第二个参数表示的是item的view对象,第三个参数是加载xml视图的
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){//当滑动list的时候，如果item没有消失，这时候view是没有任何指向的，view==null
            viewHolder = new ViewHolder();
            //用LayoutInflater动态加载实例化布局
            view = LayoutInflater.from(context).inflate(R.layout.today_hot_news_item,viewGroup,false);
            viewHolder.imageViewTitle = (ImageView) view.findViewById(R.id.today_hot_news_item_image);
            viewHolder.textViewTitle = (TextView)  view.findViewById(R.id.today_hot_news_item_title);
            //把刚刚实例化的textView和ImageView的数据信息添加到view里面
            view.setTag(viewHolder);
        } else{//当滑动list的时候,如果item消失了，这时候从该view之中提取已经创建的ImageView和textView信息数据,以循环使用
            viewHolder = (ViewHolder) view.getTag();
        }
        //是否为已读新闻
        String readSeq = PreUtil.getStringFromDefault(context, "read", "");
        if (readSeq.contains(storiesList.get(i).getId() + "")) {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,R.color.click));
        } else {
            viewHolder.textViewTitle.setTextColor(ContextCompat.getColor(context,isLight ? android.R.color.black : android.R.color.white));
        }

        ((FrameLayout) viewHolder.imageViewTitle.getParent().getParent().getParent()).setBackgroundResource(isLight? R.color.white:R.color.dark);
        ((LinearLayout) viewHolder.textViewTitle.getParent()).setBackgroundResource(isLight ? R.drawable.light:R.drawable.dark);
        //得到对应的stories的实例化，给textView和ImageView赋值
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
