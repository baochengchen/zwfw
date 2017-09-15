package com.ztesoft.zwfw.moudle.workchat;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.imagepicker.ImagePicker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.WorkChatBean;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.SoftKeyBoardListener;

import java.util.List;

public class WorkChatDetailActivity extends BaseActivity {


    private Chat mChat;

    LinearLayout mReplyLayout;
    EditText mReplyEdt;


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
        GridView imgGv = (GridView) findViewById(R.id.img_gv);

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
        titleTv.setText(mChat.getTitle());
        contentTv.setText(mChat.getContent());
        creatorTv.setText(mChat.getByUserName());
        String attachments = mChat.getAttachments();
        if(!TextUtils.isEmpty(attachments)){
            imgGv.setAdapter(new ImageAdapter(attachments.split(",")));
        }

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
            ImageLoader.getInstance().displayImage(Config.BASE_URL+Config.URL_ATTACHMENT+"/"+imgIds[position], iv_img);

            return view;
        }


    }

    private void reply() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mReplyEdt.setFocusable(true);
        mReplyEdt.setFocusableInTouchMode(true);
        mReplyEdt.requestFocus();
        imm.showSoftInput(mReplyEdt, 0);

    }
}