package com.ztesoft.zwfw;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.bigkoo.pickerview.TimePickerView;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskQueryActivity extends BaseActivity implements View.OnClickListener{


    Spinner mSpinner;
    EditText mCodeEdt;
    LinearLayout mMutiLayout;
    Button mStartTimeBt, mEndTimeBt;

    //TimeSelectDialog mTimeSelectDialog;
    TimePickerView mTimePickerView;

    private int curTimeViewId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_query);
        mSpinner = (Spinner) findViewById(R.id.spinner);

        mCodeEdt = (EditText) findViewById(R.id.code_edit);
        mMutiLayout = (LinearLayout) findViewById(R.id.multiple_layout);

        mStartTimeBt = (Button) findViewById(R.id.startTime_bt);
        mEndTimeBt = (Button) findViewById(R.id.endTime_bt);

        mStartTimeBt.setOnClickListener(this);
        mEndTimeBt.setOnClickListener(this);

        ArrayList<String> queryType = new ArrayList<String>();
        queryType.add("按编号查询");
        queryType.add("组合查询");

        ArrayAdapter queryAdapter = new ArrayAdapter(mContext, android.R.layout.simple_spinner_item, queryType);
        queryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(queryAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mCodeEdt.setVisibility(View.VISIBLE);
                        mMutiLayout.setVisibility(View.GONE);
                        break;
                    case 1:
                        mCodeEdt.setVisibility(View.GONE);
                        mMutiLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void timeSelect(int id) {
        curTimeViewId = id;
        if(null == mTimePickerView){
            mTimePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if(curTimeViewId== R.id.startTime_bt){
                        mStartTimeBt.setText(DateUtils.Date2String(date));
                    }else{
                        mEndTimeBt.setText(DateUtils.Date2String(date));
                    }

                }
            }).setType(new boolean[]{true,true,true,false,false,false}).build();
        }
        mTimePickerView.setDate(Calendar.getInstance());
        mTimePickerView.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.startTime_bt:
            case R.id.endTime_bt:
                timeSelect(v.getId());
                break;
        }
    }
}
