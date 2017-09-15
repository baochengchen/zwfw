package com.ztesoft.zwfw.moudle.workchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.WorkChatAdapter;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.WorkChatBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/25.
 */

public class WorkChatToMeFragment extends BaseFragment {

    View rootView;
    PullToRefreshListView mLv;
    private WorkChatAdapter mWorkChatAdapter;
    private List<Chat> mChats = new ArrayList<>();

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
        rootView = inflater.inflate(R.layout.fragment_work_chat_to_me,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLv = (PullToRefreshListView) rootView.findViewById(R.id.work_chat_lv);
        mLv.setMode(PullToRefreshBase.Mode.BOTH);
        mLv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多");
        mLv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开以加载");
        mWorkChatAdapter = new WorkChatAdapter(getActivity(),mChats);
        mLv.setAdapter(mWorkChatAdapter);

        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WorkChatDetailActivity.class);
                intent.putExtra("chat",mChats.get(position-1));
                startActivity(intent);
            }
        });

      //  requestData();

    }


    public static WorkChatToMeFragment newInstance() {

        Bundle args = new Bundle();
        WorkChatToMeFragment fragment = new WorkChatToMeFragment();
        fragment.setArguments(args);
        return fragment;
    }



}
