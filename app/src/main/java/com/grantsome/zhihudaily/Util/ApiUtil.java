package com.grantsome.zhihudaily.Util;

/**
 * 这个类是Api接口
 *
 * Created by tom on 2017/2/14.
 */

public class ApiUtil {

    //基础地址，每一个接口都会有，每一个API具体地址必须使用的string
    public static final String BASEURL = "http://news-at.zhihu.com/api/4/";
    //开始的时候出现的图片，貌似不能用了
    public static final String START = "http://news-at.zhihu.com/api/7/prefetch-launch-images/1080*1668";
    //
    public static final String THEMES = "themes";
    //最新消息
    public static final String LATESTNEWS = "news/latest";
    //历史消息
    public static final String BEFORE = "news/before/";
    //主题消息
    public static final String THEMENEWS = "theme/";
    //新闻内容
    public static final String CONTENT = "news/";
    //新闻额外信息
    public static final String STORY_EXTRA = "story-extra/";
    //接在BASEURL后面，加上STORY +id+LONG_COMMENT/SHORT_COMMENT;
    public static final String STORY = "story/";
    //新闻长评论
    public static final String LONG_COMMENTS = "/long-comments";
    //新闻短评论
    public static final String SHORT_COMMENTS = "/short-comments";

    public static final int TOPIC = 131;

    public static final String START_LOCATION = "start_location";

    public static final String CACHE = "cache";

    public static final int LATEST_COLUMN = Integer.MAX_VALUE;

    public static final int BASE_COLUMN = 100000000;
}
