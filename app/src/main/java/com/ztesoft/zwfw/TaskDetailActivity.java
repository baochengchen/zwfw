package com.ztesoft.zwfw;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.widget.SegmentView;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity implements SegmentView.OnSegmentViewClickListener{

    SegmentView mSegView;
    ViewPager mViewPager;
    private List<Fragment> mFragments;
    private Task mtask;

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


        mtask = (Task) getIntent().getSerializableExtra("task");
        mSegView = (SegmentView) findViewById(R.id.seg_task_detail);
        mSegView.setSegmentText("申请信息", "流程痕迹");
        mSegView.setOnSegmentViewClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mFragments = new ArrayList<>();
        ApplyInfoFragment applyInfoFragment = ApplyInfoFragment.newInstance(mtask);
        ProcessTraceFragment processTraceFragment = ProcessTraceFragment.newInstance();
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

    @Override
    public void onSegmentViewClick(View view, int position) {
        mViewPager.setCurrentItem(position);
    }


    class TaskDetailFragmentPagerAdapter extends FragmentPagerAdapter{

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
