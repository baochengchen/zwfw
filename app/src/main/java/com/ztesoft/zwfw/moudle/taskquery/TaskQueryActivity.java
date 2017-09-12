package com.ztesoft.zwfw.moudle.taskquery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.req.QueryTaskReq;
import com.ztesoft.zwfw.domain.resp.QueryTaskListResp;
import com.ztesoft.zwfw.utils.DateUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TaskQueryActivity extends BaseActivity implements View.OnClickListener {


    EditText mCodeEdt, mTaskNameEdt, mApplyUserEdt;
    Button mStartTimeBt, mEndTimeBt, mSearchBt;
    TimePickerView mTimePickerView;

    private int curTimeViewId;
    private String mStartTime = "";
    private String mEndTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_query);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.task_query));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mCodeEdt = (EditText) findViewById(R.id.code_edit);
        mTaskNameEdt = (EditText) findViewById(R.id.taskname_edt);
        mApplyUserEdt = (EditText) findViewById(R.id.apply_user_edt);

        mStartTimeBt = (Button) findViewById(R.id.startTime_bt);
        mEndTimeBt = (Button) findViewById(R.id.endTime_bt);
        mSearchBt = (Button) findViewById(R.id.search_bt);

        mStartTimeBt.setOnClickListener(this);
        mEndTimeBt.setOnClickListener(this);
        mSearchBt.setOnClickListener(this);


    }


    private void timeSelect(int id) {
        curTimeViewId = id;
        if (null == mTimePickerView) {
            mTimePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                @Override
                public void onTimeSelect(Date date, View v) {
                    if (curTimeViewId == R.id.startTime_bt) {
                        mStartTimeBt.setText(DateUtils.Date2String(date));
                        mStartTime = DateUtils.Date2String(date);
                    } else {
                        mEndTimeBt.setText(DateUtils.Date2String(date));
                        mEndTime = DateUtils.Date2String(date);
                    }

                }
            }).setType(new boolean[]{true, true, true, false, false, false}).build();
        }
        mTimePickerView.setDate(Calendar.getInstance());
        mTimePickerView.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.startTime_bt:
            case R.id.endTime_bt:
                timeSelect(v.getId());
                break;
            case R.id.search_bt:
                searchTask();
                break;
        }
    }


    private void searchTask() {
        StringBuffer sb = new StringBuffer();
        sb.append(mCodeEdt.getText().toString().trim()).append(mTaskNameEdt.getText().toString().trim()).append(mApplyUserEdt.getText().toString().trim()).append(mStartTime).append(mEndTime);
        if(TextUtils.isEmpty(sb)){
            Toast.makeText(mContext,"请至少输入一个条件",Toast.LENGTH_SHORT).show();
            return;
        }
        final QueryTaskReq queryTaskReq = new QueryTaskReq();
        queryTaskReq.setQueryNo(mCodeEdt.getText().toString().trim());
        queryTaskReq.setItemName(mTaskNameEdt.getText().toString().trim());
        queryTaskReq.setUserName(mApplyUserEdt.getText().toString().trim());
        queryTaskReq.setBeginDate(mStartTime);
        queryTaskReq.setEndDate(mEndTime);
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_QUERYAPPWORKS+ "?page=0&size=20", JSON.toJSONString(queryTaskReq), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
                showProgressDialog("查询中..");
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                hideProgressDialog();
                QueryTaskListResp resp = JSON.parseObject(response, QueryTaskListResp.class);
                if (resp.getContent() != null) {
                    Intent intent = new Intent(mContext, QueryTaskListActivity.class);
                    intent.putExtra("list", (Serializable) resp.getContent());
                    intent.putExtra("queryTaskReq", (Serializable) queryTaskReq);
                    startActivity(intent);
                }else{
                    Toast.makeText(mContext, "content = null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                hideProgressDialog();
                Toast.makeText(mContext, "查询失败", Toast.LENGTH_SHORT).show();
            }
        }, 0);

    }


}
