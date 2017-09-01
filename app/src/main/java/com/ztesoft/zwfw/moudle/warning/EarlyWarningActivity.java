package com.ztesoft.zwfw.moudle.warning;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.TaskDetailActivity;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.moudle.todo.MyTodoActivity;

import java.util.ArrayList;
import java.util.List;

public class EarlyWarningActivity extends BaseActivity {

    PullToRefreshListView mTaskLv;
    private List<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_early_warning);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        switch (getIntent().getIntExtra("type",2)){

            case 2:
                csTitile.setText(getString(R.string.alarm_task));
                break;
            case 3:
                csTitile.setText(getString(R.string.time_out_task));
                break;
        }
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTaskLv = (PullToRefreshListView) findViewById(R.id.task_lv);
        mTaskAdapter = new TaskAdapter();
        mTaskLv.setAdapter(mTaskAdapter);

        mTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext,TaskDetailActivity.class);
                intent.putExtra("task",mTasks.get(position-1));
                startActivity(intent);
            }
        });


       // requestData();
    }

 /*   private void requestData() {

        String[] surNames = new String[]{"孙","猪","沙"};
        String[] names = new String[]{"孙悟空","猪八戒","沙僧"};
        for(int i=0 ; i< 10;i++){
            Task task = new Task();
            task.taskNumber = "0000"+(i+1);
            task.timeLine ="2017/9/30";
            task.surName =surNames[i%surNames.length];
            task.name =names[i%surNames.length];
            task.title = "对于销售假冒伪劣产品的处罚";
            task.content = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX";
            task.status = "审批中";
            mTasks.add(task);
        }

        mTaskAdapter.notifyDataSetChanged();
    }*/


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
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_my_todo, null);
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
            surNameTv.setText(task.getApplicantName());
            nameTv.setText(task.getApplicantName().substring(0,1));
            taskTitleTv.setText(task.getItemOrThemeName());
            taskStatusTv.setText(task.getTaskname());

            return convertView;
        }


    }
}
