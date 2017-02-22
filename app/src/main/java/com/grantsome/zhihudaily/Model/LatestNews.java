package com.grantsome.zhihudaily.Model;

import java.util.List;

/**
 * 使用了GsonFormat插件
 * 最新消息的类
 * Created by tom on 2017/2/12.
 */

public class LatestNews {

    /**
     * date : 20170214
     * stories : [{"images":["http://pic3.zhimg.com/9756419a0031b20e027baafde0d26f16.jpg"],"type":0,"id":9221655,"ga_prefix":"021415","title":"身为清华博士，我现在专门说相声，还上了春晚"},{"images":["http://pic3.zhimg.com/885025ea06de5560236a4c0d2d1e81f6.jpg"],"type":0,"id":9221195,"ga_prefix":"021414","title":"没订到餐厅，现在开始准备情人节晚餐还来得及"},{"images":["http://pic3.zhimg.com/bb90c9679737dcb947b6413efc5f4d16.jpg"],"type":0,"id":9125499,"ga_prefix":"021413","title":"狗说：不要自称「单身狗」，我比你强多了"},{"images":["http://pic3.zhimg.com/f05da6c9e5acae492d275375db71d7a2.jpg"],"type":0,"id":9221210,"ga_prefix":"021412","title":"大误 · 大哥，稳"},{"images":["http://pic2.zhimg.com/a3415a3bff66d14b158ed573331b1041.jpg"],"type":0,"id":9221149,"ga_prefix":"021411","title":"趁着情人节，我们来说清楚为什么你买的玫瑰其实是月季"},{"images":["http://pic2.zhimg.com/77a7c65cc2bb2cb7e2f44cbf7babf855.jpg"],"type":0,"id":9131279,"ga_prefix":"021410","title":"大家更喜欢假的，这是心理学科普面临的根本问题"},{"images":["http://pic4.zhimg.com/4a615ddef2ec9915ff904278d91934db.jpg"],"type":0,"id":9217392,"ga_prefix":"021409","title":"科学领域中目前有意义的最大数字是多少？"},{"images":["http://pic3.zhimg.com/63bf374e8cf0503052c133ab982ab7fe.jpg"],"type":0,"id":9219409,"ga_prefix":"021408","title":"爱情啊，就是在不嗑药的情况下，让自己上瘾"},{"images":["http://pic4.zhimg.com/5ed66170f19b8e7312f575b99f3b7fd3.jpg"],"type":0,"id":9216709,"ga_prefix":"021407","title":"为什么美国的主要税源是所得税，中国则是增值税？"},{"images":["http://pic2.zhimg.com/dac9848ebbd36e97578adb4acccf8285.jpg"],"type":0,"id":9220325,"ga_prefix":"021407","title":"《歌手》第四期：从难度、曲风到改编，大家都有点保守"},{"images":["http://pic1.zhimg.com/52968ae3e266d6a724bfae3ccc2775f8.jpg"],"type":0,"id":9219765,"ga_prefix":"021407","title":"小米开始用「直供模式」买手机，怎么看起来有点像安利？"},{"images":["http://pic4.zhimg.com/05f39ebe846d2f8a3e7ec7f59f9fafbf.jpg"],"type":0,"id":8420176,"ga_prefix":"021406","title":"瞎扯 · 如何正确地吐槽"},{"images":["http://pic3.zhimg.com/dd9ebd7529baba9c9176ff67fd233796.jpg"],"type":0,"id":9218620,"ga_prefix":"021406","title":"这里是广告 · 有个喜欢玩英雄联盟的男友是怎样的体验？"}]
     * top_stories : [{"image":"http://pic4.zhimg.com/8dba1649e35cbe06bfb29c9c3f7ea76f.jpg","type":0,"id":9125499,"ga_prefix":"021413","title":"狗说：不要自称「单身狗」，我比你强多了"},{"image":"http://pic2.zhimg.com/f6b6b22de8dc84ffa8d917497aef8775.jpg","type":0,"id":9221149,"ga_prefix":"021411","title":"趁着情人节，我们来说清楚为什么你买的玫瑰其实是月季"},{"image":"http://pic4.zhimg.com/6dfefd92d4fd334c666d39b919c48527.jpg","type":0,"id":9220325,"ga_prefix":"021407","title":"《歌手》第四期：从难度、曲风到改编，大家都有点保守"},{"image":"http://pic4.zhimg.com/32073159df1dee9ba9cd4a31b8c0373f.jpg","type":0,"id":9219765,"ga_prefix":"021407","title":"小米开始用「直供模式」买手机，怎么看起来有点像安利？"},{"image":"http://pic2.zhimg.com/710a31e0b3890d384256c66aa84d3aa9.jpg","type":0,"id":9219379,"ga_prefix":"021318","title":"连 7.8 亿股权都能拍，网上的司法拍卖是怎么回事？"}]
     */

    private String date;
    private List<Stories> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }


    public static class TopStoriesBean {
        /**
         * image : http://pic4.zhimg.com/8dba1649e35cbe06bfb29c9c3f7ea76f.jpg
         * type : 0
         * id : 9125499
         * ga_prefix : 021413
         * title : 狗说：不要自称「单身狗」，我比你强多了
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return "TopStoriesBean{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", ga_prefix='" + ga_prefix + '\'' +
                    ", image='" + image + '\'' +
                    ", type=" + type +
                    '}';
        }
    }


    @Override
    public String toString() {
        return "LatestNews{" +
                "top_stories=" + top_stories +
                ", stories=" + stories +
                ", date='" + date + '\'' +
                '}';
    }


}