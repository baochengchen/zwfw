package com.ztesoft.zwfw.moudle.todo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.FloatButton;
import com.ztesoft.zwfw.domain.ReportDynamicDatas;
import com.ztesoft.zwfw.domain.Supervise;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.Type;
import com.ztesoft.zwfw.domain.req.ReplyReq;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.widget.CustomReplyDialog;
import com.ztesoft.zwfw.widget.SegmentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDetailActivity extends BaseActivity implements SegmentView.OnSegmentViewClickListener, View.OnClickListener, CustomReplyDialog.OnCustomReplyClickListener {

    public static final String TYPE_TASK = "type_task";
    public static final String TYPE_CONSULT = "type_consult";
    public static final String TYPE_SUPERVISE = "type_supervise";

    public static final int REQUEST_REFRESH = 0;
    public static final int RESULET_CUSTOM_REPLY = 1;

    SegmentView mSegView;
    ViewPager mViewPager;
    LinearLayout mFloatBtnContainer;
    EditText mReplyEdt;
    private List<Fragment> mFragments;
    private Object mData;

    private CustomReplyDialog mCustomReplyDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.task_detail));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mData = getIntent().getSerializableExtra("data");

        mFloatBtnContainer = (LinearLayout) findViewById(R.id.floatBtnContainer);
        mCustomReplyDialog = new CustomReplyDialog(mContext, R.style.customDialog);
        mCustomReplyDialog.setOnConsultDialogClickListener(this);

        mReplyEdt = (EditText) findViewById(R.id.reply_edt);

        initFloatBtn();

        mSegView = (SegmentView) findViewById(R.id.seg_task_detail);
        mSegView.setSegmentText("申请信息", "流转痕迹");
        mSegView.setOnSegmentViewClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragments = new ArrayList<>();

        ApplyInfoFragment applyInfoFragment = ApplyInfoFragment.newInstance(mData);
        ProcessTraceFragment processTraceFragment = ProcessTraceFragment.newInstance(mData);
        mFragments.add(applyInfoFragment);
        mFragments.add(processTraceFragment);
        mViewPager.setAdapter(new TaskDetailFragmentPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                
            }

            @Override
            public void onPageSelected(int position) {
                mSegView.setSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(0);

    }

    private void initFloatBtn() {

        Map<String, String> map = new HashMap<>();
        if (mData instanceof Task) {
            map.put("templateId", ((Task) mData).getTemplateId());
        } else if (mData instanceof Consult) {
            map.put("templateId", ((Consult) mData).getTemplateId());
        } else if (mData instanceof Supervise) {
            map.put("templateId", ((Supervise) mData).getSupervisionTemplateId());
        }

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_SEARCHFLOWBUTTONDTO, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                List<FloatButton> floatButtons = JSON.parseArray(response, FloatButton.class);
                for (FloatButton bt : floatButtons) {
                    if(TextUtils.equals(bt.getBtnName(),"回复")||TextUtils.equals(bt.getBtnName(),"预审通过")||TextUtils.equals(bt.getBtnName(),"预审不通过")){
                        Button button = new Button(mContext);
                        button.setTag(bt);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.px2dp_76));
                        lp.setMargins(getResources().getDimensionPixelSize(R.dimen.px2dp_10), 0, 0, 0);
                        button.setLayoutParams(lp);
                        button.setTextColor(getResources().getColorStateList(R.color.white));
                        button.setBackgroundResource(R.drawable.blue_edit_corner_bg);
                        button.setText(bt.getBtnName());
                        button.setOnClickListener(TaskDetailActivity.this);
                        mFloatBtnContainer.addView(button);
                    }
                }

            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {

            }
        }, 0);
    }

    @Override
    public void onSegmentViewClick(View view, int position) {
        mViewPager.setCurrentItem(position);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        FloatButton floatButton = (FloatButton) v.getTag();
        if (TextUtils.equals("回复", floatButton.getBtnName())) {
            mCustomReplyDialog.setStateCode(floatButton.getStateCode());
            mCustomReplyDialog.show();
            mCustomReplyDialog.setTiTleText(floatButton.getBtnName());
        } else if (TextUtils.equals("预审通过", floatButton.getBtnName())||TextUtils.equals("预审不通过", floatButton.getBtnName())) {
            mCustomReplyDialog.setStateCode(floatButton.getStateCode());
            mCustomReplyDialog.show();
            mCustomReplyDialog.setTiTleText(floatButton.getBtnName());
        }else if (TextUtils.equals("转发", floatButton.getBtnName())) {

        } else if (TextUtils.equals("提交", floatButton.getBtnName())) {
            mCustomReplyDialog.setStateCode(floatButton.getStateCode());
            mCustomReplyDialog.show();
            mCustomReplyDialog.setTiTleText(floatButton.getBtnName());
        }

    }

    private boolean excuteBizProcess(CustomReplyDialog dialog) {
        ReplyReq replyReq = new ReplyReq();
        ReportDynamicDatas reportDynamicDatas = new ReportDynamicDatas();
        if(mData instanceof Task){
            Task task = (Task) mData;
            replyReq.setKeyId(task.getId());
            replyReq.setTaskListId(task.getTaskListId());
        }else if (mData instanceof Consult) {
            Consult consult = (Consult) mData;
            replyReq.setKeyId(consult.getId());
            replyReq.setTaskListId(consult.getTaskListId());
        }else if(mData instanceof Supervise){
            Supervise supervise = (Supervise) mData;
            replyReq.setKeyId(supervise.getId());
            replyReq.setTaskListId(supervise.getSupervisionTaskListId());
            reportDynamicDatas.setSupervise(supervise);
        }
        replyReq.setStateCode(dialog.getStateCode());
        replyReq.setDynamicDatas(reportDynamicDatas);
        String reply = dialog.getReplyText();
        if (TextUtils.isEmpty(reply)) {
            Toast.makeText(mContext, "请输入意见", Toast.LENGTH_SHORT).show();
            return false;
        }
        replyReq.setTaskResult(reply);

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_EXCUTEBIZPROCESS, JSON.toJSONString(replyReq), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {
                showProgressDialog("正在处理");
            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                hideProgressDialog();
                Type type = JSON.parseObject(response, Type.class);
                if (null != type && TextUtils.equals("1", type.getCode())) {
                    Toast.makeText(mContext, "处理成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULET_CUSTOM_REPLY,null);
                    finish();

                } else {
                    Toast.makeText(mContext, "处理失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                hideProgressDialog();
                Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
            }
        },0);


        return true;
    }

    @Override
    public void onCustomReplyClick(boolean confirm, CustomReplyDialog dialog) {
        if (confirm) {
            if (excuteBizProcess(dialog))
                dialog.cancel();
        } else {
            dialog.cancel();
        }

    }


    class TaskDetailFragmentPagerAdapter extends FragmentPagerAdapter {

        public TaskDetailFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
