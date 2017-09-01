package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.resp.BasePageResp;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.moudle.warning.EarlyWarningActivity;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BaoChengchen on 2017/8/30.
 */

public class TaskFragment extends BaseFragment {

    private static final String TAG = TaskFragment.class.getSimpleName();

    View rootView;
    PullToRefreshListView mTaskLv;

    private List<Task> mTasks = new ArrayList<>();
    private TaskAdapter mTaskAdapter;

    private int curPage = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_task, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTaskLv = (PullToRefreshListView) rootView.findViewById(R.id.task_lv);
        mTaskLv.setMode(PullToRefreshBase.Mode.BOTH);
        mTaskLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mTaskLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mTaskAdapter = new TaskAdapter();
        mTaskLv.setAdapter(mTaskAdapter);

        mTaskLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage = 0;
                requestData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                curPage++;
                requestData();
            }
        });

        requestData();

    }

    private void requestData() {
        //Map<String, Object> map = new HashMap<String, Object>();
       /* map.put("itemOrThemeName","");
        map.put("itemType","");
        map.put("applySource","");
        map.put("workType","");
        map.put("applicantName","");
        map.put("workNo","");
        map.put("applyStartTime","");
        map.put("applyEndTime","");
        map.put("itemOrgName","");*/

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYWORKLIST + "?page=" + curPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

                Log.d(TAG, "onRequest:" + url);
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mTaskLv.onRefreshComplete();
                QueryTaskListResp resp = JSON.parseObject(response, QueryTaskListResp.class);
                if (resp.getContent() != null) {
                    if (resp.isFirst()) {
                        mTasks.clear();
                        mTasks.addAll(resp.getContent());
                        mTaskAdapter.notifyDataSetChanged();
                    } else {
                        if (resp.getContent().size() == 0) {
                            curPage--;
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mTasks.addAll(resp.getContent());
                            mTaskAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                Log.d(TAG, errorMsg);
                mTaskLv.onRefreshComplete();
            }
        }, curPage);
    }

    public static TaskFragment newInstance() {

        Bundle args = new Bundle();
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
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
            } else if (position % 3 == 1) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.pink_corner_text_bg));
            } else {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            surNameTv.setText(task.getApplicantName().substring(0, 1));
            nameTv.setText(task.getApplicantName());
            taskTitleTv.setText(task.getItemOrThemeName());
            taskStatusTv.setText(task.getTaskname());

            return convertView;
        }


    }
}
