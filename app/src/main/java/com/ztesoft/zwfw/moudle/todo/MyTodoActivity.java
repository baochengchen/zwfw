package com.ztesoft.zwfw.moudle.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.TaskDetailActivity;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class MyTodoActivity extends BaseActivity {



    private PagerSlidingTabStrip mPagerStrip;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private String[] mPagerTitles = {"办件待处理","咨询投诉待回复","催/督办件待处理"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_todo);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        csTitile.setText(getString(R.string.my_todo));
        initViews();

    }

    private void initViews() {

        mFragments = new ArrayList<>();
        TaskFragment taskFragment = TaskFragment.newInstance();
        ConsultFragment consultFragment = ConsultFragment.newInstance();
        SuperviseFragment superviseFragment = SuperviseFragment.newInstance();
        mFragments.add(taskFragment);
        mFragments.add(consultFragment);
        mFragments.add(superviseFragment);

        mPagerStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_strip);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(new TaskFragmentPagerAdapter(getSupportFragmentManager()));
        mPagerStrip.setViewPager(mViewPager);


    }


    class TaskFragmentPagerAdapter extends FragmentPagerAdapter {

        public TaskFragmentPagerAdapter(FragmentManager fm) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            return mPagerTitles[position];
        }
    }



}
