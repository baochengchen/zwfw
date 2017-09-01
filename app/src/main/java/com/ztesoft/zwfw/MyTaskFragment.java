package com.ztesoft.zwfw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/7.
 */

public class MyTaskFragment extends BaseFragment{

    View mView;
    PullToRefreshListView mTaskLv;
    private List<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_layout_my_task,container,false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mView.findViewById(R.id.tt_layout).setBackgroundResource(R.mipmap.sy_top);
        TextView csTitile = (TextView) mView.findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.approve_center));
        csTitile.setTextColor(getResources().getColorStateList(R.color.white));
        mView.findViewById(R.id.cs_back).setVisibility(View.GONE);
        mTaskLv = (PullToRefreshListView) mView.findViewById(R.id.task_lv);
        mTaskLv.setMode(PullToRefreshBase.Mode.BOTH);

        mTaskLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mTaskLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mTaskAdapter = new TaskAdapter();
        mTaskLv.setAdapter(mTaskAdapter);

        mTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),TaskDetailActivity.class);
                intent.putExtra("task",mTasks.get(position-1));
                startActivity(intent);
            }
        });


        requestData();


    }


     public static MyTaskFragment newInstance() {

        Bundle args = new Bundle();
        MyTaskFragment fragment = new MyTaskFragment();
        fragment.setArguments(args);
        return fragment;
    }
    private void requestData() {

        String[] surNames = new String[]{"刘","关","张"};
        String[] names = new String[]{"刘备","关羽","张飞"};
        for(int i=0 ; i< 10;i++){
            Task task = new Task();
            task.setWorkNo("0000"+(i+1));
            task.setPromiseDate("2017/9/30");
            task.setApplicantName(names[i%surNames.length]);
            task.setItemOrThemeName("关于讨伐大魏的计划");
           // task.content = "东汉末年天下大乱， 各路诸侯割据，又有人崛起， 互相争地盘,打来打去,就剩下三个，最牛的是同一北方,有背景,有才干的曹操";
            task.setTaskname("已审批");
            mTasks.add(task);
        }

        mTaskAdapter.notifyDataSetChanged();
    }


    class TaskAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTasks.size();
        }

        @Override
        public Object getItem(int position) {
            return mTasks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_my_todo, null);
            }
            TextView taskNoTv = (TextView) convertView.findViewById(R.id.task_no_tv);
            TextView taskTimelineTv = (TextView) convertView.findViewById(R.id.task_time_line_tv);
            TextView surNameTv = (TextView) convertView.findViewById(R.id.surname_tv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView taskTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Task task = mTasks.get(position);
            taskNoTv.setText("办件编号：" + task.getWorkNo());
            taskTimelineTv.setText("办结时限：" + task.getPromiseDate());
            if (position % 3 == 0) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.red_corner_text_bg));
            }else if(position % 3 == 1){
                surNameTv.setBackground(getResources().getDrawable(R.drawable.pink_corner_text_bg));
            }else {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            surNameTv.setText(task.getApplicantName().substring(0,1));
            nameTv.setText(task.getApplicantName());
            taskTitleTv.setText(task.getItemOrThemeName());
            taskStatusTv.setText(task.getTaskname());

            return convertView;
        }


    }

}
