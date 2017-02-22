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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tom on 2017/2/16.
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

    public ViewFragment(Context context, AttributeSet attrs, int defStyle) {
        super(context,attrs,defStyle);
        imageLoader = ImageLoader.getInstance();
        displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();
        this.context = context;
        this.latestTopStoriesBean = new ArrayList<>();
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
        viewList.clear();
        initUI();
    }

    private void initUI(){
        View view = LayoutInflater.from(context).inflate(R.layout.top_news_view_pager_item,this,true);
        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        linearLayoutDots = (LinearLayout) view.findViewById(R.id.view_dots);
        linearLayoutDots.removeAllViews();

        int len = latestTopStoriesBean.size();
        for(int i = 0; i < len;i++){
            ImageView imageView = new ImageView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            linearLayoutDots.addView(imageView,layoutParams);
            imageViewDotsList.add(imageView);
        }

        for(int i=0;i<len;i++){
           View v = LayoutInflater.from(context).inflate(R.layout.news_content_layout,null);
           ImageView imageView = (ImageView) v.findViewById(R.id.image_view_title);
           TextView textView = (TextView) v.findViewById(R.id.text_view_title);
           imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
           if(i == 0){
               imageLoader.displayImage(latestTopStoriesBean.get(len-1).getImage(),imageView,displayImageOptions);
               textView.setText(latestTopStoriesBean.get(len-1).getTitle());
           } else if(i ==len+1) {
               imageLoader.displayImage(latestTopStoriesBean.get(0).getImage(),imageView,displayImageOptions);
               textView.setText(latestTopStoriesBean.get(0).getTitle());
           } else {
               imageLoader.displayImage(latestTopStoriesBean.get(i-1).getImage(),imageView,displayImageOptions);
               textView.setText(latestTopStoriesBean.get(i-1).getTitle());
           }
            v.setOnClickListener(this);
            viewList.add(v);
        }
        viewPager.setFocusable(true);
        viewPager.setCurrentItem(1);
        viewPager.setAdapter(new MyPagerAdapter());
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        currentItem = 1;
        startPlay();
    }

    private void startPlay(){
        isAutoPlay = true;
        handler.postDelayed(task ,3000);
    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            if(isAutoPlay){
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

    @Override
    public void onClick(View view) {
        if(ItemClickListener != null){
            LatestNews.TopStoriesBean topStoriesBean= latestTopStoriesBean.get(viewPager.getCurrentItem());
            ItemClickListener.click(view,topStoriesBean);
        }
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup viewGroup,int i){
            viewGroup.addView(viewList.get(i));
            return viewList.get(i);
        }

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
               case 1:
                   isAutoPlay = false;
                   break;
               case 2:
                   isAutoPlay = true;
                   break;
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

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.ItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void click(View v, LatestNews.TopStoriesBean topStoriesBean);
    }
}
