package com.grantsome.zhihudaily.Model;

import java.util.List;

/**
 * Created by tom on 2017/2/15.
 */

public class ThemeNews {

    /**
     * stories : [{"type":0,"id":8362197,"title":"更多设计内容，都在读读日报里"},{"images":["http://pic4.zhimg.com/b22d557844dc326b09ed8ccf4c7633b3.jpg"],"type":0,"id":7809160,"title":"如果你有个朋友想在几小时内学会用 AI，你可以把这转给他"},{"images":["http://pic1.zhimg.com/7715929756d5dbc4969d75e216dcd73c.jpg"],"type":2,"id":8346038,"title":"Coldplay的新单曲MV没有看过瘾怎么办？"},{"images":["http://pic1.zhimg.com/c4087e649670ea00f17e07b79bc53e8c.jpg"],"type":0,"id":8292706,"title":"一觉醒来 Instagram 变了样，第一眼有点接受不了"},{"images":["http://pic2.zhimg.com/d61103a6b81c78650e260d7b0c59dee5.jpg"],"type":2,"id":8270506,"title":"回顾无印良品12年之间做过的广告 |  无印良品vol.2"},{"images":["http://pic4.zhimg.com/e61cfe33bb9a099b5a9faed710ff0fef.jpg"],"type":2,"id":8270497,"title":"那些你在中国无印良品商店买不到的商品 |  无印良品vol.1"},{"images":["https://pic2.zhimg.com/633b7811055c3906d87c13124694ddb9_r.png"],"type":0,"id":8265861,"title":"「光之画家」Turner 取代经济学家，成为新版 20 英镑头像"},{"type":1,"id":8264539,"title":"身为一位高中生，我是这样完成校园海报的"},{"images":["http://pic1.zhimg.com/63a900d65e384902edd2345f0963d7dc.jpg"],"type":2,"id":8258189,"title":"一个卖手机壳的创业团队，为何要经历百般刁难后入驻 Apple Store？"},{"images":["http://pic4.zhimg.com/4450a81faa92d6013c3aed4d1a19ed73.jpg"],"type":2,"id":8254137,"title":"去过IKEA千百次的你，一定不知道在包装背后还有这些故事"},{"images":["http://pic1.zhimg.com/b59dd90d97f275ee3554666a811c0a48.jpg"],"type":1,"id":8231029,"title":"好作品好在何处？2016 iF 平面设计金奖精选赏析"},{"images":["http://pic3.zhimg.com/479265a0313e2f0b9e0e9693d712d202.jpg"],"type":2,"id":8216566,"title":"三宅一生没让他火，2020东京奥运会Logo让他笑到了最后"},{"images":["http://pic4.zhimg.com/880bca1181e9e685a7acd83f6b0813bb.jpg"],"type":2,"id":8211397,"title":"为什么在这个连手机都能做设计的时代，我们还推荐这部关于手工平面设计的纪录片？"},{"images":["https://pic1.zhimg.com/76af59c95f6530ebfe568964cece26a8_r.png"],"type":0,"id":8207739,"title":"当二次元遇上性冷淡：Nendo 的不惊叹无设计"},{"images":["http://pic4.zhimg.com/59a2c31285853571c547b704b7575acb.jpg"],"type":2,"id":8192372,"title":"我怎么给 Apple 写文案？（三）"},{"images":["http://pic4.zhimg.com/a19d993850cf1e000a2c279a755709eb.jpg"],"type":2,"id":8192367,"title":"我怎么给 Apple 写文案？（二）"},{"images":["http://pic2.zhimg.com/363e75a5cdee8e8ed6ad225b4de6c90d.jpg"],"type":2,"id":8186252,"title":"我怎么给 Apple 写文案？（一）"},{"images":["http://pic2.zhimg.com/13974285495b8a66f52ce2f8c9efa3a9.jpg"],"type":2,"id":8186233,"title":"当建筑师为喵星人设计豪宅"}]
     * description : 好设计需要打磨和研习，我们分享灵感和路径
     * background : http://p3.zhimg.com/ff/15/ff150eef63a48f0d1dafb77e62610a9f.jpg
     * color : 62140
     * name : 设计日报
     * image : http://p2.zhimg.com/98/dd/98dd8dcec0186ffba8d8e298255765e7.jpg
     * editors : [{"url":"http://www.zhihu.com/people/starose","bio":"产品设计师 @华兴资本","id":56,"avatar":"http://pic4.zhimg.com/de2ab67cf_m.jpg","name":"星玫"},{"url":"http://www.zhihu.com/people/fanxtastic","bio":"PhD Researcher in Design","id":22,"avatar":"http://pic2.zhimg.com/e19f362d5_m.jpg","name":"Fanxtastic"}]
     * image_source :
     */

    private String description;
    private String background;
    private int color;
    private String name;
    private String image;
    private String image_source;
    private List<Stories> stories;
    private List<EditorsBean> editors;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_source() {
        return image_source;
    }

    public void setImage_source(String image_source) {
        this.image_source = image_source;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    public List<EditorsBean> getEditors() {
        return editors;
    }

    public void setEditors(List<EditorsBean> editors) {
        this.editors = editors;
    }

    public static class StoriesBean {
        /**
         * type : 0
         * id : 8362197
         * title : 更多设计内容，都在读读日报里
         * images : ["http://pic4.zhimg.com/b22d557844dc326b09ed8ccf4c7633b3.jpg"]
         */

        private int type;
        private int id;
        private String title;
        private List<String> images;

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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class EditorsBean {
        /**
         * url : http://www.zhihu.com/people/starose
         * bio : 产品设计师 @华兴资本
         * id : 56
         * avatar : http://pic4.zhimg.com/de2ab67cf_m.jpg
         * name : 星玫
         */

        private String url;
        private String bio;
        private int id;
        private String avatar;
        private String name;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
