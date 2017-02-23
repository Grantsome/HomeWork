package com.grantsome.zhihudaily.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grantsome.zhihudaily.Model.ExtraInfo;
import com.grantsome.zhihudaily.Model.LongComments;
import com.grantsome.zhihudaily.R;
import com.grantsome.zhihudaily.Util.ApiUtil;
import com.grantsome.zhihudaily.Util.HttpUtil;
import com.grantsome.zhihudaily.Util.LogUtil;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CommentsActivity extends AppCompatActivity {

    private ImageButton commentBack;

    private TextView commentTitle;

    private ListView listViewComment;

    private TextView longCommentNumberText;

    private TextView shortCommentNumberText;

    private int storiesId;

    private LongComments longComments;

    private List<LongComments.CommentsBean> longCommentsList = new ArrayList<LongComments.CommentsBean>();

    private List<LongComments.CommentsBean> shortCommentsList = new ArrayList<LongComments.CommentsBean>();

    private ExtraInfo extraInfo;

    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

    private ImageLoader imageLoader = ImageLoader.getInstance();

    private CommentsAdapter commentsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        commentBack = (ImageButton) findViewById(R.id.comment_back);
        commentBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        commentTitle = (TextView) findViewById(R.id.comment_title);
        listViewComment = (ListView) findViewById(R.id.list_view_comment);
        storiesId = getIntent().getIntExtra("stories.getId()",520);
        LogUtil.d("CommentActivity","stories.getId()"+ storiesId);
        extraInfo = (ExtraInfo) getIntent().getSerializableExtra("extraInfo");
        LogUtil.d("CommentActivity","extraInfo"+ extraInfo.getShort_comments() + " " + extraInfo.getPopularity());
        initLongCommentNumberText();
        initShortCommentNumberText();
        commentsAdapter = new CommentsAdapter();
        listViewComment.setAdapter(commentsAdapter);
    }

    private void initLongCommentNumberText(){
       longCommentNumberText = new TextView(CommentsActivity.this);
       longCommentNumberText.setPadding(8,8,8,8);
       listViewComment.addHeaderView(longCommentNumberText);
       String shortCommentUrl = ApiUtil.STORY + storiesId + ApiUtil.LONG_COMMENTS;
        if(HttpUtil.isNetWorkConntected(getApplicationContext())){
            HttpUtil.get(shortCommentUrl, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    String response = responseString.toString();
                    LogUtil.d("CommentActivity", "response的内容为" + response);
                    parseToJson(response);
                }
            });
        }
    }

    private void initShortCommentNumberText(){
       shortCommentNumberText = new TextView(CommentsActivity.this);
       shortCommentNumberText.setPadding(8,8,8,8);
       listViewComment.addFooterView(shortCommentNumberText);
       shortCommentNumberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shortCommentsList.size() == 0){
                    LogUtil.d("CommentActivity","ShortComments开始加载") ;
                    String shortCommentUrl = ApiUtil.STORY + storiesId + ApiUtil.SHORT_COMMENTS;
                    if(HttpUtil.isNetWorkConntected(getApplicationContext())){
                        HttpUtil.get(shortCommentUrl, new TextHttpResponseHandler() {
                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                                String response = responseString.toString();
                                LogUtil.d("CommentActivity","ShortComments:response的内容为" + response);
                                Gson gson = new Gson();
                                longComments = gson.fromJson(response,LongComments.class);
                                LogUtil.d("CommentActivity","longComments" + longComments);
                                {
                                    setListViewData();
                                    setCommentNumberText();
                                    setCommentTitle();

                                    shortCommentsList = longComments.getComments();
                                    longCommentsList.addAll(shortCommentsList);
                                    commentsAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                }

            }
        });
    }

    private void parseToJson(String jsonData){
        Gson gson = new Gson();
        longComments = gson.fromJson(jsonData,LongComments.class);
        LogUtil.d("CommentActivity","longComments" + longComments);
        {
            setListViewData();
            setCommentNumberText();
            setCommentTitle();
        }

    }

    private void setCommentTitle(){
        int number = extraInfo.getLong_comments()+extraInfo.getShort_comments();
        commentTitle.setText(number+ "条评论");
    }

    private void setCommentNumberText(){
        longCommentNumberText.setText(extraInfo.getLong_comments() + "条长评");
        shortCommentNumberText.setText(extraInfo.getShort_comments()+"条短评");
    }

    private void setListViewData(){
        longCommentsList = longComments.getComments();
        commentsAdapter.notifyDataSetChanged();
    }

    private class CommentsAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return longCommentsList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            MyViewHolder viewHolder = null;
            if(view == null){
                view = View.inflate(CommentsActivity.this,R.layout.long_comments,null);
                viewHolder = new MyViewHolder();
                viewHolder.imageViewUserImage = (ImageView) view.findViewById(R.id.comment_user_image);
                viewHolder.textViewUserName = (TextView) view.findViewById(R.id.comment_user_name);
                viewHolder.textViewContent = (TextView) view.findViewById(R.id.comment_user_content);
                viewHolder.textViewDate = (TextView) view.findViewById(R.id.comment_user_time);
                viewHolder.textViewLikeNumber = (TextView) view.findViewById(R.id.comment_like);
                view.setTag(viewHolder);
            }else {
                viewHolder = (MyViewHolder) view.getTag();
                imageLoader.displayImage(longCommentsList.get(i).getAvatar(),viewHolder.imageViewUserImage,displayImageOptions);
                LogUtil.d("CommentActivity","CommentActivity地址为" + longCommentsList.get(i).getAuthor());
                viewHolder.textViewUserName.setText(longCommentsList.get(i).getAuthor());
                viewHolder.textViewContent.setText(longCommentsList.get(i).getContent());
                LogUtil.d("CommentsActivity","longCommentsList.get(i).getLikes()" + longCommentsList.get(i).getLikes());
                if(longCommentsList.get(i).getLikes() == 0) {
                   //viewHolder.textViewLikeNumber.setText(0);
                }else {
                    //viewHolder.textViewLikeNumber.setText(longCommentsList.get(i).getLikes());
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
                long data = Long.parseLong(String.valueOf(longCommentsList.get(i).getTime()));
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(data*1000);
                String commentTime = simpleDateFormat.format(calendar.getTime());
                viewHolder.textViewDate.setText(commentTime);
            }
            return view;
        }
    }

    public static class MyViewHolder{
        ImageView imageViewUserImage;
        TextView textViewUserName;
        TextView textViewLikeNumber;
        TextView textViewContent;
        TextView textViewDate;
    }

}
