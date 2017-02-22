package com.grantsome.zhihudaily.Model;

import java.util.List;

/**
 * Created by tom on 2017/2/21.
 */

public class LongComments {

    private List<CommentsBean> comments;

    public List<CommentsBean> getComments() {
        return comments;
    }

    public void setComments(List<CommentsBean> comments) {
        this.comments = comments;
    }

    public static class CommentsBean {
        /**
         * author : 棍棍_風暴之灵
         * content : 不敢苟同作者的观点，在你眼中可能四线城市甚至是县城更多的是无知、愚昧、落后。但是我只能说你就像带着有色眼镜一样去否定一个国家最基本的组成单元，北京就真的能摒除这所谓的世俗了么？其实哪里都是一样的，只是外在的表象在你心中的印象不同罢了。
         * avatar : http://pic3.zhimg.com/b06dca0a55ed7b263449b013c1bef5fa_im.jpg
         * time : 1480996207
         * id : 27431257
         * likes : 3
         * reply_to : {"content":"我觉得题主只是想分享他自己的心路历程而已。大家都说的对，谁都没有权利指责谁，每个人都有自己的活法，你可以醉生梦死，你也可以每日拼搏，甚至还可以终日乞讨，只要不伤害到别人，没有谁比谁更高尚。但是站在题主自己的角度上，经历过这么多事以后，他只是对自己的过去做出总结，并对自己的未来做出判断，仅此而已，你有权利反对，有权利认同，这就是言论自由。","status":0,"id":27376545,"author":"tom Lau"}
         */

        private String author;
        private String content;
        private String avatar;
        private int time;
        private int id;
        private int likes;
        private ReplyToBean reply_to;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public ReplyToBean getReply_to() {
            return reply_to;
        }

        public void setReply_to(ReplyToBean reply_to) {
            this.reply_to = reply_to;
        }

        public static class ReplyToBean {
            /**
             * content : 我觉得题主只是想分享他自己的心路历程而已。大家都说的对，谁都没有权利指责谁，每个人都有自己的活法，你可以醉生梦死，你也可以每日拼搏，甚至还可以终日乞讨，只要不伤害到别人，没有谁比谁更高尚。但是站在题主自己的角度上，经历过这么多事以后，他只是对自己的过去做出总结，并对自己的未来做出判断，仅此而已，你有权利反对，有权利认同，这就是言论自由。
             * status : 0
             * id : 27376545
             * author : tom Lau
             */

            private String content;
            private int status;
            private int id;
            private String author;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getAuthor() {
                return author;
            }

            public void setAuthor(String author) {
                this.author = author;
            }
        }
    }
}
