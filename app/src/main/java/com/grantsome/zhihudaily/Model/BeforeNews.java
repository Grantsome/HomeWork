package com.grantsome.zhihudaily.Model;

import java.util.List;

/**
 * Created by tom on 2017/2/16.
 */

public class BeforeNews {

    /**
     * date : 20161110
     * stories : [{"images":["http://pic1.zhimg.com/1bb294cd4dad57eee8af66ca75086fb8.jpg"],"type":0,"id":8967488,"ga_prefix":"111022","title":"小事 · 被性侵之后"},{"images":["http://pic3.zhimg.com/703ad2ae4ff91c7ae0a78598cecf010e.jpg"],"type":0,"id":8966783,"ga_prefix":"111021","title":"一个男人为救活妻子，找到了永生之树"},{"title":"瓷器有七种彩，可别再只认得青花瓷了","ga_prefix":"111020","images":["http://pic3.zhimg.com/28a29f1bd69f6c6342778a46bf5af7b2.jpg"],"multipic":true,"type":0,"id":8963560},{"images":["http://pic3.zhimg.com/ad315e8eb3a2c66a7e05ccc55e6058c2.jpg"],"type":0,"id":8967028,"ga_prefix":"111020","title":"他是产品，我是运营，互联网的大潮里我们都有光明的前途"},{"images":["http://pic1.zhimg.com/a26340666020fbdbe42f158b3e033a98.jpg"],"type":0,"id":8967161,"ga_prefix":"111019","title":"双十一 · 「大家都别买，商家库存大，后面肯定更便宜」"},{"images":["http://pic3.zhimg.com/3ca005e7cb5c8833f1d36049bcab1b02.jpg"],"type":0,"id":8966379,"ga_prefix":"111018","title":"「不就是透明玻璃和大白墙嘛，极简建筑我也能设计」"},{"images":["http://pic2.zhimg.com/e97b08d1177ebc57bd9e9d8ddf17e055.jpg"],"type":0,"id":8966833,"ga_prefix":"111017","title":"知乎好问题 · 时间管理最常见的误区有哪些？"},{"images":["http://pic3.zhimg.com/fc30413e70df02ecbcc13fed7b35c32e.jpg"],"type":0,"id":8964409,"ga_prefix":"111015","title":"宁泽涛和国家游泳队的冲突从何而来？他会退役吗？"},{"images":["http://pic4.zhimg.com/4ced184e0a84464d93dd62f207228abb.jpg"],"type":0,"id":8961167,"ga_prefix":"111014","title":"二手车挑来挑去，各家交易平台上价格怎么差好多"},{"images":["http://pic3.zhimg.com/20ac6fe024d134c8234df0c134150dc6.jpg"],"type":0,"id":8965861,"ga_prefix":"111013","title":"预测美国大选，有一项数据还挺准的，而且越来越明显"},{"images":["http://pic1.zhimg.com/73de922af044dd70beae4f6d3c113d14.jpg"],"type":0,"id":8965513,"ga_prefix":"111012","title":"大误 · 樱木花道，我可是在帮你啊"},{"images":["http://pic4.zhimg.com/e76156d3f4b1932e3cf02bf7a23b8a03.jpg"],"type":0,"id":8964264,"ga_prefix":"111011","title":"苹果公司是怎样培养消费者忠诚度的？"},{"images":["http://pic1.zhimg.com/522632f1bc771bf91743af4574b6aa94.jpg"],"type":0,"id":8962937,"ga_prefix":"111010","title":"「就知道你一定会投给我」：两党「亲妈州」是这么来的"},{"images":["http://pic2.zhimg.com/b1bf21f4b6a7b4b5851279dc517a613d.jpg"],"type":0,"id":8963864,"ga_prefix":"111009","title":"家庭里的「仪式感」多一点，孩子感受到的爱就多一点"},{"title":"打开上帝视角，重新发现一座城市","ga_prefix":"111008","images":["http://pic3.zhimg.com/c959c6353d0adb61c053757a8b1d8052.jpg"],"multipic":true,"type":0,"id":8963763},{"images":["http://pic1.zhimg.com/47ac4b0e2cfe37cf1b40c8d16a60f9c8.jpg"],"type":0,"id":8964773,"ga_prefix":"111007","title":"特朗普「逆袭」取胜，为什么所有预测机构都出错了？"},{"images":["http://pic3.zhimg.com/7c187dbb9c060748e162d234280168d2.jpg"],"type":0,"id":8958248,"ga_prefix":"111007","title":"说真的，看电影还有个好处是，可以止痛"},{"images":["http://pic3.zhimg.com/9c3c775781a74373023f55bca724cee2.jpg"],"type":0,"id":8964898,"ga_prefix":"111007","title":"年轻人独自一人居住，如何有效地保持自律？"},{"images":["http://pic4.zhimg.com/d1b96f8e289674d972f57c14a422a4db.jpg"],"type":0,"id":8964643,"ga_prefix":"111007","title":"读读日报 24 小时热门 TOP 5 · 特朗普总统的第一个任期"},{"images":["http://pic2.zhimg.com/80473eae77df078154818643ec3b7bdd.jpg"],"type":0,"id":8961174,"ga_prefix":"111006","title":"瞎扯 · 如何正确地吐槽"}]
     */

    private String date;
    private List<Stories> stories;

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


}
