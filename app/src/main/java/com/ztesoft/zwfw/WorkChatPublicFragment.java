package com.ztesoft.zwfw;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ztesoft.zwfw.base.BaseFragment;

/**
 * Created by BaoChengchen on 2017/8/25.
 */

public class WorkChatPublicFragment extends BaseFragment {

    View rootView;

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
        rootView = inflater.inflate(R.layout.fragment_work_chat_public,container,false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    public static WorkChatPublicFragment newInstance() {

        Bundle args = new Bundle();
        WorkChatPublicFragment fragment = new WorkChatPublicFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
