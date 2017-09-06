package com.ztesoft.zwfw.moudle.todo;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.ztesoft.zwfw.widget.ConsultReplyDialog;
import com.ztesoft.zwfw.widget.SegmentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDetailActivity extends BaseActivity implements SegmentView.OnSegmentViewClickListener, View.OnClickListener, ConsultReplyDialog.OnConsultReplyClickListener {

    public static final String TYPE_TASK = "type_task";
    public static final String TYPE_CONSULT = "type_consult";
    public static final String TYPE_SUPERVISE = "type_supervise";

    SegmentView mSegView;
    ViewPager mViewPager;
    LinearLayout mFloatBtnContainer;
    EditText mReplyEdt;
    private List<Fragment> mFragments;
    private Object mData;

    private ConsultReplyDialog mConsultReplyDialog;


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
        mConsultReplyDialog = new ConsultReplyDialog(mContext, R.style.customDialog);
        mConsultReplyDialog.setOnConsultDialogClickListener(this);

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
        if(mData instanceof Task){
            map.put("templateId", ((Task)mData).getTemplateId());
        }else if(mData instanceof Consult){
            map.put("templateId", ((Consult)mData).getTemplateId());
        }else if(mData instanceof Supervise){
            map.put("templateId", ((Supervise)mData).getSupervisionTemplateId());
        }

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_SEARCHFLOWBUTTONDTO, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {
                List<FloatButton> floatButtons = JSON.parseArray(response, FloatButton.class);
                for (FloatButton bt : floatButtons) {
                    Button button = new Button(mContext);
                    button.setTag(bt);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, getResources().getDimensionPixelSize(R.dimen.px2dp_76));
                    lp.setMargins(getResources().getDimensionPixelSize(R.dimen.px2dp_10), 0, 0, 0);
                    button.setLayoutParams(lp);
                    button.setBackgroundResource(R.drawable.custom_edit_corner_bg);
                    button.setText(bt.getBtnName());
                    button.setOnClickListener(TaskDetailActivity.this);
                    mFloatBtnContainer.addView(button);
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
            mConsultReplyDialog.setStateCode(floatButton.getStateCode());
            mConsultReplyDialog.show();
        } else if (TextUtils.equals("忽略", floatButton.getBtnName())) {

        } else if (TextUtils.equals("转发", floatButton.getBtnName())) {

        }

    }

    private boolean relpy(ConsultReplyDialog dialog) {

        if (mData instanceof Consult) {
            final Consult consult = (Consult) mData;
            ReplyReq replyReq = new ReplyReq();
            replyReq.setKeyId(consult.getId());
            replyReq.setStateCode(dialog.getStateCode());
            replyReq.setTaskListId(consult.getTaskListId());
            replyReq.setDynamicDatas(new ReportDynamicDatas());
            String reply = dialog.getReplyText();
            if (TextUtils.isEmpty(reply)) {
                Toast.makeText(mContext, "请输入意见", Toast.LENGTH_SHORT).show();
                return false;
            }
            replyReq.setTaskResult(reply);

            RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_EXCUTEBIZPROCESS, JSON.toJSONString(replyReq), new RequestManager.RequestListener() {
                @Override
                public void onRequest(String url, int actionId) {

                }

                @Override
                public void onSuccess(String response, String url, int actionId) {
                    Type type = JSON.parseObject(response, Type.class);
                    if (null != type && TextUtils.equals("1", type.getCode())) {
                        Toast.makeText(mContext, "回复成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(mContext, "回复失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String errorMsg, String url, int actionId) {
                    Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }, 0);

        }
        return true;
    }

    @Override
    public void onConsultReplyClick(boolean confirm, ConsultReplyDialog dialog) {
        if (confirm) {
            if (relpy(dialog))
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
