package com.grantsome.zhihudaily.Gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by tom on 2017/2/12.
 */

public class Latest {

    @SerializedName("date")
    public String newsDate;

    @SerializedName("stories")
    public List<LatestStories> latestStoriesList;

    public class LatestStories{

        @SerializedName("images")
        public String newsImagesUrl;

        @SerializedName("id")
        public int newsId;

        @SerializedName("title")
        public String newsTitle;
    }

    @SerializedName("top_stories")
    public List<LatestTopStories> latestTopStoriesList;

    public class LatestTopStories{

        @SerializedName("image")
        public String newsImageUrl;

        @SerializedName("id")
        public int newsId;

        @SerializedName("title")
        public String newsTitle;
    }
}
