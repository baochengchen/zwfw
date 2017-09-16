package com.ztesoft.zwfw.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.WorkChatBean;

import java.util.List;

/**
 * Created by Baochengchen on 2017/8/27.
 */

public class WorkChatAdapter extends BaseAdapter {

    public static final int TYPE_MINE = 0;
    public static final int TYPE_TOME = 1;

    private Context context;
    private List<Chat> datas;
    private int type;

    public WorkChatAdapter(Context context, List<Chat> datas,int type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.work_chat_item, parent, false);
        }
        TextView titleTv = (TextView) convertView.findViewById(R.id.title_tv);
        TextView contentTv = (TextView) convertView.findViewById(R.id.content_tv);
        TextView creatorTv = (TextView) convertView.findViewById(R.id.creator_tv);
        TextView readStateTv = (TextView) convertView.findViewById(R.id.read_state_tv);

        Chat chat = datas.get(position);
        if(type == TYPE_MINE){
            if(TextUtils.equals("0",chat.getByReadState())){
                readStateTv.setVisibility(View.VISIBLE);
            }else{
                readStateTv.setVisibility(View.GONE);
            }
        }else if(type == TYPE_TOME){
            if(TextUtils.equals("0",chat.getToReadState())){
                readStateTv.setVisibility(View.VISIBLE);
            }else{
                readStateTv.setVisibility(View.GONE);
            }
        }
        titleTv.setText(chat.getTitle());
        contentTv.setText(chat.getContent());
        creatorTv.setText(chat.getByUserName());
        return convertView;
    }


}
