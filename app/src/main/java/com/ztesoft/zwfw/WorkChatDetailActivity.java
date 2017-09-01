package com.ztesoft.zwfw;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
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
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.WorkChatBean;
import com.ztesoft.zwfw.utils.SoftKeyBoardListener;

import java.util.List;

public class WorkChatDetailActivity extends BaseActivity {


    private WorkChatBean mChat;

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
                if(EditorInfo.IME_ACTION_SEND == actionId){
                    return true;
                }
                return false;
            }
        });

        mChat = (WorkChatBean) getIntent().getSerializableExtra("chat");
        titleTv.setText(mChat.title);
        contentTv.setText(mChat.content);
        creatorTv.setText(mChat.creator);
        imgGv.setAdapter(new ImageAdapter());
    }


       class ImageAdapter extends BaseAdapter {


        public ImageAdapter() {

        }

        @Override
        public int getCount() {
            return mChat.imgs == null ? 0 : mChat.imgs.size();
        }

        @Override
        public Object getItem(int position) {
            return mChat.imgs == null ? null:mChat.imgs.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

           @Override
        public View getView(int position, View view, ViewGroup parent) {
            if(view == null){
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false);
            }
               ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
               //ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, mChat.imgs.get(position), iv_img, 0, 0);
               ImageLoader.getInstance().displayImage(mChat.imgs.get(position),iv_img);

            return view;
        }


       }

    public void reply(View view) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            mReplyEdt.setFocusable(true);
            mReplyEdt.setFocusableInTouchMode(true);
            mReplyEdt.requestFocus();
            imm.showSoftInput(mReplyEdt,0);

    }
}