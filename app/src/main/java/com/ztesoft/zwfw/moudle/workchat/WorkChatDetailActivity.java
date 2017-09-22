package com.ztesoft.zwfw.moudle.workchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.Comment;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.SoftKeyBoardListener;
import com.ztesoft.zwfw.utils.UnicodeUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.widget.NoScrollListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkChatDetailActivity extends BaseActivity implements View.OnClickListener{


    public static final int ACTION_ADD_COMMENT = 0;
    public static final int ACTION_GET_COMMENT = 1;
    public static final int ACTION_UPDATECHAT = 2;

    private Chat mChat;
    private int mType;
    LinearLayout mReplyLayout;
    EditText mReplyEdt;
    Button mSendBt;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mComments = new ArrayList<>();
    private Comment mComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_chat_detail);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText("问题详情");
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView rightView = (ImageView) findViewById(R.id.cs_right_view);
        rightView.setVisibility(View.VISIBLE);
        rightView.setImageResource(R.mipmap.ic_edit_reply);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply();
            }
        });

        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        TextView contentTv = (TextView) findViewById(R.id.content_tv);
        TextView creatorTv = (TextView) findViewById(R.id.creator_tv);
        TextView creatTimeTv = (TextView) findViewById(R.id.time_tv);
        GridView imgGv = (GridView) findViewById(R.id.img_gv);
        NoScrollListView commentLv = (NoScrollListView) findViewById(R.id.commentLv);
        mSendBt = (Button) findViewById(R.id.send_bt);
        mSendBt.setOnClickListener(this);


        mReplyLayout = (LinearLayout) findViewById(R.id.reply_layout);
        mReplyEdt = (EditText) findViewById(R.id.reply_edt);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mReplyLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                mReplyLayout.setVisibility(View.GONE);
            }
        });

        mReplyEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_SEND == actionId) {
                    return true;
                }
                return false;
            }
        });

        mChat = (Chat) getIntent().getSerializableExtra("chat");
        mType = getIntent().getIntExtra("type",0);

        if(mType == Config.TYPE_PUBLIC)
            rightView.setVisibility(View.GONE);

        titleTv.setText(mChat.getTitle());
        contentTv.setText(UnicodeUtils.unicode2String(mChat.getContent()));
        creatorTv.setText("来自 " + mChat.getByUserName());
        creatTimeTv.setText(mChat.getCreateDate());
        String attachments = mChat.getAttachments();
        if (!TextUtils.isEmpty(attachments)) {
            final String[] imgIds = attachments.split(",");
            imgGv.setAdapter(new ImageAdapter(imgIds));
            imgGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(WorkChatDetailActivity.this,PhotoViewActivity.class);
                    intent.putExtra("imgs",imgIds);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
        }

        mCommentAdapter = new CommentAdapter();
        commentLv.setAdapter(mCommentAdapter);

        updateChat();
        requestComment();
        
        
    }

    private void updateChat() {
        Map<String,String> map = new HashMap<>();
        map.put("id",mChat.getId());
        RequestManager.getInstance().postHeader(Config.BASE_URL+Config.URL_TALK_UPDATECHAT,JSON.toJSONString(map),mListener,ACTION_UPDATECHAT);
    }


    private void requestComment() {
        Map<String,String> map = new HashMap<>();
        map.put("chatId",mChat.getId());
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_GETCOMMENTS, JSON.toJSONString(map), mListener,ACTION_GET_COMMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_bt:
                addComment();
                break;
        }
    }

    RequestManager.RequestListener mListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest(String url, int actionId) {

        }

        @Override
        public void onSuccess(String response, String url, int actionId) {

            switch (actionId){
                case ACTION_ADD_COMMENT:
                    mComments.add(mComment);
                    mCommentAdapter.notifyDataSetChanged();
                    break;
                case ACTION_GET_COMMENT:
                    mComments = JSON.parseArray(response,Comment.class);
                    mCommentAdapter.notifyDataSetChanged();
                    break;
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {

            switch (actionId){
                case ACTION_ADD_COMMENT:
                    Toast.makeText(mContext,"回复失败",Toast.LENGTH_SHORT).show();
                    break;

                case ACTION_GET_COMMENT:
                    break;

                case ACTION_UPDATECHAT:
                    
                    break;
            }

        }
    };

    private void addComment() {
        mComment = new Comment();
        mComment.setChatId(Long.parseLong(mChat.getId()));
        mComment.setByUserId(Long.parseLong(getmUser().getUserId()));
        mComment.setToUserId(mType==Config.TYPE_MINE?mChat.getToUserId():mChat.getByUserId());
        mComment.setByUserName(getmUser().getUserName());
        mComment.setToUserName(mType==Config.TYPE_MINE?mChat.getToUserName():mChat.getByUserName());
        String replyStr = mReplyEdt.getText().toString().trim();
        if(TextUtils.isEmpty(replyStr)){
            Toast.makeText(mContext,"请输入回复内容",Toast.LENGTH_SHORT).show();
            return;
        }
        mComment.setContent(UnicodeUtils.string2Unicode(replyStr));
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_ADDCOMMENT, JSON.toJSONString(mComment),mListener,ACTION_ADD_COMMENT);

        hideRelyEdt();
    }


    class ImageAdapter extends BaseAdapter {

        private String[] imgIds;

        public ImageAdapter(String[] imgIds) {
            this.imgIds = imgIds;
        }

        @Override
        public int getCount() {
            return imgIds == null ? 0 : imgIds.length;
        }

        @Override
        public Object getItem(int position) {
            return imgIds == null ? null : imgIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false);
            }
            ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
            //ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, mChat.imgs.get(position), iv_img, 0, 0);
            ImageLoader.getInstance().displayImage(Config.BASE_URL + Config.URL_ATTACHMENT + "/" + imgIds[position], iv_img);

            return view;
        }


    }


    class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mComments.size();
        }

        @Override
        public Object getItem(int position) {
            return mComments.size() == 0 ? null : mComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comment comment = mComments.get(position);
            if(null == convertView){
              convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item,parent,false);
            }
            TextView commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            String byUserName = comment.getByUserName();

            SpannableStringBuilder ssb = new SpannableStringBuilder(byUserName+"："+UnicodeUtils.unicode2String(comment.getContent()));
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#708090")),0,byUserName.length()+1,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            commentTv.setText(ssb);
            return convertView;
        }
    }


    private void reply() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mReplyEdt.setFocusable(true);
        mReplyEdt.setFocusableInTouchMode(true);
        mReplyEdt.requestFocus();
        imm.showSoftInput(mReplyEdt, 0);

    }


    private void hideRelyEdt(){
        mReplyEdt.setText("");
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mReplyEdt.getWindowToken(),0);

    }
}