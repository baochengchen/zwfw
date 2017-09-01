package com.ztesoft.zwfw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class WorkChatActivity extends BaseActivity {

    private PagerSlidingTabStrip mPagerStrip;
    private ViewPager mViewPager;
    private List<Fragment> mFragments;
    private String[] mPagerTitles = {"我问的","问我的","公开的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_chat);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.work_chat));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mFragments = new ArrayList<>();
        WorkChatMineFragment  workChatMineFragment = WorkChatMineFragment.newInstance();
        WorkChatToMeFragment  workChatToMeFragment = WorkChatToMeFragment.newInstance();
        WorkChatPublicFragment workChatPublicFragment = WorkChatPublicFragment.newInstance();
        mFragments.add(workChatMineFragment);
        mFragments.add(workChatToMeFragment);
        mFragments.add(workChatPublicFragment);


        mPagerStrip = (PagerSlidingTabStrip) findViewById(R.id.pager_strip);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new WorkChatFragmentPagerAdapter(getSupportFragmentManager()));

        mPagerStrip.setViewPager(mViewPager);


    }


    class WorkChatFragmentPagerAdapter extends FragmentPagerAdapter {

        public WorkChatFragmentPagerAdapter(FragmentManager fm) {
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

    public void onChat(View v){
        startActivity(new Intent(mContext,PerformChatActivity.class));
    }
}
