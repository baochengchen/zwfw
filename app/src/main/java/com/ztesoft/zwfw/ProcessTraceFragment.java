package com.ztesoft.zwfw;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.DynamicDatas;
import com.ztesoft.zwfw.domain.Supervise;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.TaskProcess;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.widget.NoScrollListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BaoChengchen on 2017/8/16.
 */

public class ProcessTraceFragment extends BaseFragment {

    View rootView;
    NoScrollListView mProcessLv;
    List<TaskProcess> mTaskProcesses = new ArrayList<>();
    private ProcessAdapter mProcessAdapter;

    private Task mTask;
    private Consult mConsult;
    private Supervise mSupervise;
    private String mType;

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
        rootView = inflater.inflate(R.layout.fragment_task_process, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTask = (Task) getArguments().getSerializable("task");
        mConsult = (Consult) getArguments().getSerializable("consult");
        mSupervise = (Supervise) getArguments().getSerializable("supervise");
        mType = getArguments().getString("type");

        mProcessLv = (NoScrollListView) rootView.findViewById(R.id.process_lv);
        mProcessAdapter = new ProcessAdapter();
        mProcessLv.setAdapter(mProcessAdapter);

        requestData();

    }

    private void requestData() {

        Map<String, Object> map = new HashMap<>();
        map.put("servicePath", "WorkProcessService");
        if(mType.equals(TaskDetailActivity.TYPE_TASK)){
            map.put("dynamicDatas", new DynamicDatas(mTask.getHolderNo()));
            map.put("keyId", mTask.getId());
        }else if(mType.equals(TaskDetailActivity.TYPE_CONSULT)){
            map.put("dynamicDatas", new DynamicDatas(mConsult.getHolderNo()));
            map.put("keyId", mConsult.getId());
        }else if(mType.equals(TaskDetailActivity.TYPE_SUPERVISE)){
            map.put("dynamicDatas", new DynamicDatas(mSupervise.getSupervisionHolderNo()));
            map.put("keyId", mConsult.getId());
        }

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_SEARCHFRONTEXCUTELIST, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                List<TaskProcess> taskProcesses = JSON.parseArray(response, TaskProcess.class);
                if (null != taskProcesses) {
                    mTaskProcesses.clear();
                    mTaskProcesses.addAll(taskProcesses);
                    mProcessAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, 0);
    }


    public static ProcessTraceFragment newInstance(Object object) {

        Bundle args = new Bundle();
        if(object instanceof Task){
            args.putSerializable("task", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_TASK);
        }else if(object instanceof Consult){
            args.putSerializable("consult", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_CONSULT);
        }else if(object instanceof Supervise) {
            args.putSerializable("supervise", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_SUPERVISE);
        }
        ProcessTraceFragment fragment = new ProcessTraceFragment();
        fragment.setArguments(args);
        return fragment;
    }


    class ProcessAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTaskProcesses.size();
        }

        @Override
        public Object getItem(int position) {
            return mTaskProcesses.size() == 0 ? null : mTaskProcesses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.task_process_item_undo, null);
            }

            TextView auditTimeTv = (TextView) convertView.findViewById(R.id.auditTime_tv);
            TextView taskNameTv = (TextView) convertView.findViewById(R.id.taskName_tv);
            TextView stateCodeNameTv = (TextView) convertView.findViewById(R.id.stateCodeName_tv);
            TextView taskResultTv = (TextView) convertView.findViewById(R.id.taskResult_tv);
            TextView userNameTv = (TextView) convertView.findViewById(R.id.userName_tv);

            TaskProcess taskProcess = mTaskProcesses.get(position);

            auditTimeTv.setText(taskProcess.getAuditTime());
            taskNameTv.setText("环节名称：" + taskProcess.getTaskName());
            stateCodeNameTv.setText("处理结果：" + taskProcess.getStateCodeName());
            taskResultTv.setText("处理意见：" + taskProcess.getTaskResult());
            userNameTv.setText("审批人：" + taskProcess.getUserName());
            return convertView;
        }
    }
}
