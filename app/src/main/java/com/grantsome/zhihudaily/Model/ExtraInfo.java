package com.grantsome.zhihudaily.Model;

import java.io.Serializable;

/**
 * Created by tom on 2017/2/21.
 */

public class ExtraInfo implements Serializable{
    /**
     * long_comments : 48
     * popularity : 2853
     * short_comments : 251
     * comments : 299
     */

    private int long_comments;
    private int popularity;
    private int short_comments;
    private int comments;

    public int getLong_comments() {
        return long_comments;
    }

    public void setLong_comments(int long_comments) {
        this.long_comments = long_comments;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getShort_comments() {
        return short_comments;
    }

    public void setShort_comments(int short_comments) {
        this.short_comments = short_comments;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

}
