package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.os.Bundle;
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
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.resp.QueryConsultListResp;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/30.
 */

public class ConsultFragment extends BaseFragment {

    private static final String TAG = ConsultFragment.class.getSimpleName();

    View rootView;

    PullToRefreshListView mConsultLv;

    private List<Consult> mConsults = new ArrayList<>();
    private ConsultAdapter mConsultAdapter;

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
        rootView = inflater.inflate(R.layout.fragment_consult,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mConsultLv = (PullToRefreshListView) rootView.findViewById(R.id.consult_lv);
        mConsultLv.setMode(PullToRefreshBase.Mode.BOTH);
        mConsultLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mConsultLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mConsultAdapter = new ConsultAdapter();
        mConsultLv.setAdapter(mConsultAdapter);

        mConsultLv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QRYINTERACTION + "?page=" + curPage + "&size=20","{}", new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

                Log.d(TAG, "onRequest:" + url);
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                mConsultLv.onRefreshComplete();
                QueryConsultListResp resp = JSON.parseObject(response, QueryConsultListResp.class);
                if (resp.getContent() != null) {
                    if (resp.isFirst()) {
                        mConsults.clear();
                        mConsults.addAll(resp.getContent());
                        mConsultAdapter.notifyDataSetChanged();
                    } else {
                        if (resp.getContent().size() == 0) {
                            curPage--;
                            Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
                        } else {
                            mConsults.addAll(resp.getContent());
                            mConsultAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                Log.d(TAG, errorMsg);
                mConsultLv.onRefreshComplete();
            }
        }, curPage);
    }

    public static ConsultFragment newInstance() {

        Bundle args = new Bundle();
        ConsultFragment fragment = new ConsultFragment();
        fragment.setArguments(args);
        return fragment;
    }

    class ConsultAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mConsults.size();
        }

        @Override
        public Object getItem(int position) {
            return mConsults.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (null == convertView) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_consult, null);
            }
            TextView applyTimeTv = (TextView) convertView.findViewById(R.id.apply_time_tv);
            TextView surNameTv = (TextView) convertView.findViewById(R.id.surname_tv);
            TextView nameTv = (TextView) convertView.findViewById(R.id.name_tv);
            TextView taskTitleTv = (TextView) convertView.findViewById(R.id.title_tv);
            TextView taskStatusTv = (TextView) convertView.findViewById(R.id.status_tv);

            Consult consult = mConsults.get(position);
            applyTimeTv.setText("申请时间：" + consult.getCreateDate());
            if (position % 3 == 0) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.red_corner_text_bg));
            } else if (position % 3 == 1) {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.pink_corner_text_bg));
            } else {
                surNameTv.setBackground(getResources().getDrawable(R.drawable.blue_corner_text_bg));
            }
            surNameTv.setText(consult.getWebUserName().substring(0, 1));
            nameTv.setText(consult.getWebUserName());
            taskTitleTv.setText(consult.getTitle());
            taskStatusTv.setText(consult.getTaskname());

            return convertView;
        }


    }
}
