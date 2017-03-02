package com.grantsome.zhihudaily.Model;

import java.util.List;

/**
 * Created by tom on 2017/3/1.
 */

public class StartImageInfo {
    private List<CreativesBean> creatives;

    public List<CreativesBean> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<CreativesBean> creatives) {
        this.creatives = creatives;
    }

    public static class CreativesBean {
        /**
         * url : https://pic4.zhimg.com/v2-9f030a1d7bcfa8e8a7992d13d3592dbb.jpg
         * text : David Marcu
         * start_time : 1488351771
         * impression_tracks : ["https://sugar.zhihu.com/track?vs=1&ai=3297&ut=&cg=2&ts=1488351771.78&si=68e4f69cdd454aa5912ee37feb5a5de1&lu=0&hn=ad-engine.ad-engine.9672d469&at=impression&pf=PC&az=11&sg=79d9faa6656baf72a199e94e76959177"]
         * type : 0
         * id : 3297
         */

        private String url;
        private String text;
        private int start_time;
        private int type;
        private String id;
        private List<String> impression_tracks;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getImpression_tracks() {
            return impression_tracks;
        }

        public void setImpression_tracks(List<String> impression_tracks) {
            this.impression_tracks = impression_tracks;
        }
    }
}
