package com.grantsome.zhihudaily.Fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.grantsome.zhihudaily.Model.LatestNews;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2017/2/16.
 * 首页轮播图
 */

public class ViewFragment extends FrameLayout implements View.OnClickListener{

    private List<LatestNews.TopStoriesBean> latestTopStoriesBean;

    private ImageLoader imageLoader;

    private DisplayImageOptions displayImageOptions;

    private Context context;

    private List<View> viewList;

    private List<ImageView> imageViewDotsList;

    private LinearLayout linearLayoutDots;

    private int waitTime;

    private ViewPager viewPager;

    private int currentItem;

    private boolean isAutoPlay;

    private Handler handler = new Handler();

    private OnItemClickListener ItemClickListener;

    //里面的三个参数说明
    //1、Context是在程序内实例化的时候需要
    //2、AttributeSet用于文件的实例化,会把xml里面的参数带入到view里面
    //3、defStyle主题的style信息,也是从xml里面带入
    public ViewFragment(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        //实例化imageLoader
        imageLoader = ImageLoader.getInstance();
        //显示图片的选项,分别是设置缓存到内存和SD卡之中
        displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        this.context = context;
        this.latestTopStoriesBean = new ArrayList<>();
        //初始化视图的函数
        initView();
    }

    public ViewFragment(Context context,AttributeSet attrs){
        this(context, attrs,0);
    }

    public ViewFragment(Context context){
        this(context,null);
    }

    private void initView(){
        viewList = new ArrayList<View>();
        imageViewDotsList = new ArrayList<ImageView>();
        waitTime = 2000;
    }

    public void setLatestTopStories(List<LatestNews.TopStoriesBean> latestTopStoriesBean){
        this.latestTopStoriesBean = latestTopStoriesBean;
        reset();
    }

    private void reset(){
        //初始化的时候clear
        viewList.clear();
        initUI();
    }

    private void initUI(){
        //从一个context之中获取布局填充器,把xml对象转换为view对象
        View view = LayoutInflater.from(context).inflate(R.layout.top_news_view_pager_item,this,true);
        //利用view对象来找到布局之中的组件
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        linearLayoutDots = (LinearLayout) view.findViewById(R.id.view_dots);
        //初始化的时候移除掉所有的子视图
        linearLayoutDots.removeAllViews();
        //有多少张首页里面的轮播图,就加入多少应的ImageView(也就是首页轮播图里面的图片)
        int len = latestTopStoriesBean.size();
        for(int i = 0; i < len;i++){
            //ImageView的函数构造式
            ImageView imageView = new ImageView(context);
            //LinearLayout.LayoutParams/简介
            //主要功能是从子视图向新视图里面传递信息,两个参数的意义分别是设置长和宽
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //本元素/视图距离左元素/视图的距离
            layoutParams.leftMargin = 5;
            //本元素/视图距离右元素/视图的距离
            layoutParams.rightMargin = 5;
            //用addView的方法把ImageView添加到layoutParams里面
            linearLayoutDots.addView(imageView,layoutParams);
            //想ImageView的list集合里面添加ImageView
            imageViewDotsList.add(imageView);
        }

        for(int i=0;i<=len+1;i++){
           //从一个context之中获取布局填充器,把xml对象转换为view对象
           View v = LayoutInflater.from(context).inflate(R.layout.news_content_layout,null);
           ImageView imageView = (ImageView) v.findViewById(R.id.image_view_title);
           TextView textView = (TextView) v.findViewById(R.id.text_view_title);
            //均衡放缩图像的模式设置(剪去了中间部分以适应设置)
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           if(i == 0){
               //轮播图的最后一张图片
               imageLoader.displayImage(latestTopStoriesBean.get(len-1).getImage(),imageView,displayImageOptions);
               LogUtil.d("ViewFragment","当i=0时，latestTopStoriesBean.get(len-1).gteTitle()"+latestTopStoriesBean.get(len-1).getTitle());
               textView.setText(latestTopStoriesBean.get(len-1).getTitle());
           } else if(i ==len+1) {
               //轮播图的第一张图片
               imageLoader.displayImage(latestTopStoriesBean.get(0).getImage(),imageView,displayImageOptions);
               textView.setText(latestTopStoriesBean.get(0).getTitle());
               LogUtil.d("ViewFragment","当i=len+1时，latestTopStoriesBean.get(0).gteTitle()"+latestTopStoriesBean.get(0).getTitle());
           } else {
               //轮播图的五张图片全部都会显示
               imageLoader.displayImage(latestTopStoriesBean.get(i-1).getImage(),imageView,displayImageOptions);
               textView.setText(latestTopStoriesBean.get(i-1).getTitle());LogUtil.d("ViewFragment","当i不等于前两者的时候latestTopStoriesBean.get(0).gteTitle()"+latestTopStoriesBean.get(i-1).getTitle());
           }
            //设置监听器事件
            v.setOnClickListener(this);
            //把v加入到viewList里面
            viewList.add(v);
        }
        //设置可获取焦点状态，这样才能获取点击事件
        viewPager.setFocusable(true);
        //设置初始显示的界面
        viewPager.setCurrentItem(1);
        //设置adapter
        viewPager.setAdapter(new MyPagerAdapter());
        //设置滚动监听器事件
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        currentItem = 1;
        startPlay();
    }

    private void startPlay(){
        isAutoPlay = true;
        //定时器,设置为3秒的延迟
        handler.postDelayed(task ,3000);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isAutoPlay){
                //currentItem = current%6  +1;
                currentItem = currentItem%(latestTopStoriesBean.size()+1)+1;
                if(currentItem == 1){
                    viewPager.setCurrentItem(currentItem,false);
                    handler.post(task);
                }else {
                    viewPager.setCurrentItem(currentItem);
                    handler.postDelayed(task,4000);
                }
            } else {
                handler.postDelayed(task,4000);
            }
        }
    };

    //接入的接口的原因,写的onClick方法
    @Override
    public void onClick(View view) {
        if(ItemClickListener != null){
            //由于设置的currentItem的原因,所以这里必须-1才是可以
            LatestNews.TopStoriesBean topStoriesBean= latestTopStoriesBean.get(viewPager.getCurrentItem()-1);
            ItemClickListener.click(view,topStoriesBean);
        }
    }

    class MyPagerAdapter extends PagerAdapter{

        //返回当前有效视图的个数
        @Override
        public int getCount() {
            return viewList.size();
        }

        //判断传过来的唯一的标识码是否来源于同一个视图
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //创建指定位置的页面视图
        //将指定的位置的视图add到viewgroup里面
        //返回的是一个任意的能够唯一标识这个view的码
        @Override
        public Object instantiateItem(ViewGroup viewGroup,int i){
            viewGroup.addView(viewList.get(i));
            return viewList.get(i);
        }

        //移除一个给定位置的界面
        @Override
        public void destroyItem(ViewGroup viewGroup,int i,Object object){
            viewGroup.removeView((View) object);
        }

    }

    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {


        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }


        @Override
        public void onPageSelected(int position) {
            for(int i =0 ;i < imageViewDotsList.size();i++){
                if( i == position -1){
                    imageViewDotsList.get(i).setImageResource(R.drawable.dot_full);
                } else {
                    imageViewDotsList.get(i).setImageResource(R.drawable.dot_empty);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
           switch (state){
               //手指滑动的时候
               //正在滑动
               case 1:
                   isAutoPlay = false;
                   break;
               //默认滑动完毕
               case 2:
                   isAutoPlay = true;
                   break;
               //什么都没有
               case 0:
                   if(viewPager.getCurrentItem() ==0 ){
                       viewPager.setCurrentItem(latestTopStoriesBean.size(),false);
                   } else if(viewPager.getCurrentItem() == latestTopStoriesBean.size()+1){
                       viewPager.setCurrentItem(1,false);
                   }
                   currentItem =viewPager.getCurrentItem();
                   isAutoPlay = true;
                   break;
           }
        }

    }

    //写了监听器事件的设置,主要是为了MainFragment里面引用这个方法
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.ItemClickListener = onItemClickListener;
    }

    //还是为了MainFragment里面的调用
    public interface OnItemClickListener{
        void click(View v, LatestNews.TopStoriesBean topStoriesBean);
    }
}
