package com.ztesoft.zwfw.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.domain.WorkChatBean;

import java.util.List;

/**
 * Created by Baochengchen on 2017/8/27.
 */

public class WorkChatAdapter extends BaseAdapter {
    private Context context;
    private List<WorkChatBean> datas;

    public WorkChatAdapter(Context context, List<WorkChatBean> datas) {
        this.context = context;
        this.datas = datas;
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

        WorkChatBean workChatBean = datas.get(position);
        titleTv.setText(workChatBean.title);
        contentTv.setText(workChatBean.content);
        creatorTv.setText(workChatBean.creator);
        return convertView;
    }


}
