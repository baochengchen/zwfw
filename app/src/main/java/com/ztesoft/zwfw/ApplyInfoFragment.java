package com.ztesoft.zwfw;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Task;

/**
 * Created by BaoChengchen on 2017/8/16.
 */

public class ApplyInfoFragment extends BaseFragment {

    View rootView;
    TextView mApplyPeopleType, mCertificateType, mCertificateNumber, mApplyer,
            mSex, mBirthday, mAddress;
    EditText mContentDescribe;

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
        rootView = inflater.inflate(R.layout.fragment_apply_info, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Task task = (Task) getArguments().getSerializable("task");

        mApplyer = (TextView) rootView.findViewById(R.id.applyer_tv);
        mApplyer.setText(task.getApplicantName());
        mContentDescribe = (EditText) rootView.findViewById(R.id.content_edt);
        mContentDescribe.setText("");


    }


    public static ApplyInfoFragment newInstance(Task task) {

        Bundle args = new Bundle();
        args.putSerializable("task",task);
        ApplyInfoFragment fragment = new ApplyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
