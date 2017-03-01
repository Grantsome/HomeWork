package com.grantsome.zhihudaily.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.grantsome.zhihudaily.Database.CacheDbHelper;
import com.grantsome.zhihudaily.Fragment.HomeFragment;
import com.grantsome.zhihudaily.Fragment.MainFragment;
import com.grantsome.zhihudaily.Fragment.NewsFragment;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.LogUtil;

/*
* 首页的activity,主要是fragment
*/
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private boolean isLight;

    private CacheDbHelper cacheDbHelper;

    private SwipeRefreshLayout swipeRefreshLayout;

    private String currentId;

    private DrawerLayout drawerLayout;

    private HomeFragment homeFeagment;

    private FrameLayout frameLayoutContent;

    private SharedPreferences sharedPreferences;

    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //PreferencesManager类里面的getDefaultSharedPreferences(context),接受的参数为context。
        //原理：每一个应用都有一个默认的配置文件preference.xml,使用getDefaultSharedPreference()方法获取。
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        //同上,使用getBoolean()方法获取到isLight的boolean值,默认值为true。
        isLight = sharedPreferences.getBoolean("isLight",true);
        //同上,使用getBoolean()方法获取到isRead的boolean值,默认值为false。

        //获取cacheDbHelper的实例,CacheDBHelper在database里面。
        cacheDbHelper = new CacheDbHelper(this,1);
        //调用加载最新新闻的方法,该方法LoadLatestNews在本类里面定义。
        loadLatestNews();
        //调用初始化界面的方法,该方法initView在本类里面定义
        initView();
        isLogin = getIntent().getBooleanExtra("isLogin", false);
    }


    //初始化界面方法
    private void initView(){
        //得到swipeRefreshLayout的实例化,通过findViewById实现
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        //setColorSchemeResource方法是给加载的时候的进度条设置的四种颜色
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        //设置下拉刷新的监听器事件
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //调用替换碎片的方法,该方法在本类中定义
                replaceFragment();
                //替换碎片之后即完成刷新,设置参数值为false不再刷新
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //首先要注意的就是去res/values/styles目录下面去设置无toolbar
        //得到toolbar的实例化,通过findViewById实现
        toolbar = (Toolbar) findViewById(R.id.toobar);
        //为toolbar设置背景颜色,通过判断isLight的值来设置颜色,不过这里有两个坑。第一个是toolbar导包的时候，一定要导入import android.support.v7.widget.Toolbar;导入另外一个toolbar的包会发生错误。另外一个是getResource().getColor()方法不能用了,必须要换成ContextCompat(context,color id)
        toolbar.setBackgroundColor(ContextCompat.getColor(this,isLight?android.R.color.holo_blue_light: android.R.color.background_dark));
        //设置toolbar
        setSupportActionBar(toolbar);
        //得到FrameLayout的实例,通过findViewById实现
        frameLayoutContent = (FrameLayout) findViewById(R.id.frame_layout_content);
        //得到DrawerLayout的实例,通过findViewById实现
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //获取ActionBarDrawerToggle的实例
        //传入的参数有五个。第一个:拥有滑动菜单的activity(这里传入的是this),第二个:drawerLayout对象,第三个:导航工具(开关),即被点击的对象,第四个:打开drawerLayout时候的字符串描述,第五个:关闭drawerLayout时候的字符串描述.
        final ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.app_name,R.string.app_name);
        //setDrawerListener已经过时了,要使用addDrawerListener才可以
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //关联actionbar(toolbar),以实现将导航工具(开关)的图片显示在actionbar(toolbar)上面。默认是三杠。
        actionBarDrawerToggle.syncState();

    }

    //定义的替换碎片的方法
    public void replaceFragment(){
        //如果是最新消息,就从左侧至右侧滑出。
        if(currentId.equals("latestNews")){
            //setCustomAnimations()里面的两个参数。第一个:进入动画时候的资源文件id,第二个:退出动画的时候的资源文件id
            //replace()方法里面的参数。第一个：容器的资源id,第二个:待添加碎片的实例化,第三个:标记的TAG;
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left).replace(R.id.frame_layout_content,new MainFragment(),"latestNews").commit();
        } else {
            ((NewsFragment) getSupportFragmentManager().findFragmentByTag("themeNews")).updateTheme();
        }
    }

    //定义加载最新消息的方法
    public void loadLatestNews(){
        //同上
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_from_right,R.anim.slide_out_to_left).replace(R.id.frame_layout_content,new MainFragment(),"latestNews").commit();
        currentId = "latestNews";
        LogUtil.d("MainActivity","loadLatestNews已经执行");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        finish();
    }

    //实例化菜单栏的必要函数
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //实例化菜单
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    //菜单栏的点击事件的必备函数
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            //如果选择夜间模式
            case R.id.action_mode:
                isLight = !isLight;
                //设置背景颜色,通过判断isLight的值来确定
                frameLayoutContent.setBackgroundColor(ContextCompat.getColor(this,isLight?android.R.color.white:android.R.color.black));
                toolbar.setBackgroundColor(ContextCompat.getColor(this,isLight ?android.R.color.holo_blue_light:R.color.dark));
                if (currentId.equals("latest")) {
                     //通过getSupportFragmentManager()的findFragmentByTag()来找到对应的fragment，MainFragment。并调用该MainFragment里面的updateTheme函数
                     ((MainFragment) getSupportFragmentManager().findFragmentByTag("latestNews")).updateTheme();
                } else {

            }
            //同上
            ((HomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment)).updateTheme();
            //编辑sharedPreference,传入isLight的Boolean值
            sharedPreferences.edit().putBoolean("isLight", isLight).commit();
                break;
            //如果选择登录
            case R.id.action_login:
                if(isLogin == false) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MainActivity.this, UserUIActivity.class);
                    startActivity(intent);
                }
                finish();
                break;
            //如果选择设置
            case R.id.action_setting:
                Toast.makeText(this, "该功能暂未实现", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(menuItem);
    }

    //给toolbar设置标题的函数,以在进入主题日报的时候将主题日报的名字显示在toolbar上面
    public void setToolBarTitle(String text){
        toolbar.setTitle(text);
    }

    //传入isLight的boolean值的函数
    public boolean isLight(){
        return isLight;
    }


    //设置刷新函数
    public void setSwipeRefreshLayout(boolean isEnable){
        swipeRefreshLayout.setEnabled(isEnable);
        return;
    }

    //返回CacheDbHelper的函数
    public CacheDbHelper getCacheDbHelper(){
        return cacheDbHelper;
    }

    //设置currentId的函数
    public void setCurrentId(String id){
        currentId = id;
    }

    //设置关闭菜单栏的函数
    public void closeMenu(){
        drawerLayout.closeDrawers();
    }

}

