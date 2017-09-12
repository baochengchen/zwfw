package com.ztesoft.zwfw.moudle.taskquery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.TaskAdapter;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.req.QueryTaskReq;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.moudle.todo.TaskDetailActivity;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class QueryTaskListActivity extends BaseActivity {



    PullToRefreshListView mTaskLv;
    private List<Task> mTasks = new ArrayList<>();
    private QueryTaskReq mQueryTaskReq;
    private TaskAdapter mTaskAdapter;
    private int curPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_task_list);

        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.query_result));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mTasks = (List<Task>) getIntent().getSerializableExtra("list");
        mQueryTaskReq = (QueryTaskReq) getIntent().getSerializableExtra("queryTaskReq");

        mTaskLv = (PullToRefreshListView) findViewById(R.id.task_lv);
        mTaskLv.setMode(PullToRefreshBase.Mode.BOTH);
        mTaskLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mTaskLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mTaskAdapter = new TaskAdapter(mContext,mTasks);
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


        mTaskLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, TaskDetailActivity.class);;
                intent.putExtra("data",mTasks.get(position-1));
                startActivity(intent);
            }
        });
    }


    private void requestData() {

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QUERYAPPWORKS + "?page=" + curPage + "&size=20", JSON.toJSONString(mQueryTaskReq), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

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
                            Toast.makeText(mContext, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mTasks.addAll(resp.getContent());
                            mTaskAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                mTaskLv.onRefreshComplete();
            }
        }, curPage);
    }
}
