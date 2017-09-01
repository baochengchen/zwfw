package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.ztesoft.zwfw.domain.Supervise;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.resp.QuerySuperviseListResp;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/30.
 */

public class SuperviseFragment extends BaseFragment {
    private static final String TAG = TaskFragment.class.getSimpleName();

    View rootView;

    PullToRefreshListView mSuperviseLv;

    private List<Supervise> mSupervises = new ArrayList<>();
    private SuperviseAdapter mSuperviseAdapter;

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
        rootView = inflater.inflate(R.layout.fragment_supervise,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSuperviseLv = (PullToRefreshListView) rootView.findViewById(R.id.supervise_lv);
        mSuperviseLv.setMode(PullToRefreshBase.Mode.BOTH);
        mSuperviseLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mSuperviseLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mSuperviseAdapter = new SuperviseAdapter();
        mSuperviseLv.setAdapter(mSuperviseAdapter);

        mSuperviseLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYSUPERVISE + "?page=" + curPage + "&size=20", "{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

                Log.d(TAG, "onRequest:" + url);
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mSuperviseLv.onRefreshComplete();
                QuerySuperviseListResp resp = JSON.parseObject(response, QuerySuperviseListResp.class);
                if (resp.getContent() != null) {
                    if (resp.isFirst()) {
                        mSupervises.clear();
                        mSupervises.addAll(resp.getContent());
                        mSuperviseAdapter.notifyDataSetChanged();
                    } else {
                        if (resp.getContent().size() == 0) {
                            curPage--;
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mSupervises.addAll(resp.getContent());
                            mSuperviseAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                Log.d(TAG, errorMsg);
                mSuperviseLv.onRefreshComplete();
            }
        }, curPage);
    }

    public static SuperviseFragment newInstance() {

        Bundle args = new Bundle();
        SuperviseFragment fragment = new SuperviseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class SuperviseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mSupervises.size();
        }

        @Override
        public Object getItem(int position) {
            return mSupervises.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_supervise, null);
            }

            TextView superviseTimeTv = (TextView) convertView.findViewById(R.id.supervise_time_tv);
            TextView typeTv = (TextView) convertView.findViewById(R.id.type_tv);
            TextView codeTv = (TextView) convertView.findViewById(R.id.code_tv);
            TextView contentTv = (TextView) convertView.findViewById(R.id.content_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Supervise supervise = mSupervises.get(position);
            superviseTimeTv.setText("崔督办时间：" + supervise.getSuperviseTime());
            if (TextUtils.equals("R",supervise.getFunctionType().getCode())) {
                typeTv.setBackground(getResources().getDrawable(R.drawable.red_corner_text_bg));
            } else {
                typeTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            typeTv.setText(supervise.getFunctionType().getTitle());
            codeTv.setText("编码："+supervise.getSuperviseNo());
            contentTv.setText(supervise.getSuperviseContent());
            taskStatusTv.setText(supervise.getSupervisionTaskName());

            return convertView;
        }


    }
}
