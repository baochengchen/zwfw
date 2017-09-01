package com.ztesoft.zwfw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.moudle.MainActivity;
import com.ztesoft.zwfw.moudle.todo.MyTodoActivity;
import com.ztesoft.zwfw.moudle.warning.EarlyWarningActivity;
import com.ztesoft.zwfw.widget.NoScrollGridView;

/**
 * Created by BaoChengchen on 2017/8/7.
 */

public class MainFragment extends BaseFragment implements MainActivity.UpdateMsgCountListener{

    View mView;
    NoScrollGridView mGridView;
    private static final int[] portalNames = {R.string.msg_center,R.string.my_todo,R.string.alarm_task, R.string.time_out_task, R.string.task_query, R.string.work_chat};
    private static final int[] portalbgs = {R.mipmap.sy_1, R.mipmap.sy_2, R.mipmap.sy_3, R.mipmap.sy_4, R.mipmap.sy_5, R.mipmap.sy_6};
   // private static final int[] portalColors = {R.color.light_blue, R.color.dark_orange, R.color.limegreen, R.color.light_blue, R.color.dark_orange, R.color.mediumvioletred};

    private int msgCount = 0;
    private HomeSquareAdapter mHomeSquareAdapter;
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
        mView = inflater.inflate(R.layout.fragment_layout_main, container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //CycleViewPager cycleViewPager = new CycleViewPager();
        // getChildFragmentManager().beginTransaction().replace(R.id.banner_container, cycleViewPager).commit();

        ((MainActivity)getActivity()).setUpdateMsgCountListener(this);

        mGridView = (NoScrollGridView) mView.findViewById(R.id.gridView);
        mHomeSquareAdapter = new HomeSquareAdapter();
        mGridView.setAdapter(mHomeSquareAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(new Intent(getActivity(),MessageCenterActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(),MyTodoActivity.class));
                        break;
                    case 2:
                    case 3:
                        Intent intent = new Intent(getActivity(),EarlyWarningActivity.class);
                        intent.putExtra("type",position);
                        startActivity(intent);
                        break;
                    case 4:
                        startActivity(new Intent(getActivity(),TaskQueryActivity.class));
                        break;
                    case 5:
                        msgCount = 0;
                        startActivity(new Intent(getActivity(),WorkChatActivity.class));
                        break;

                }
            }
        });
    }


    public static MainFragment newInstance() {

        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onResume() {
        mHomeSquareAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void onUpdate() {
        msgCount++;
        mHomeSquareAdapter.notifyDataSetChanged();
    }


    class HomeSquareAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return portalNames.length;
        }

        @Override
        public Object getItem(int position) {
            return portalNames.length == 0 ? null : portalNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(getActivity()).inflate(R.layout.home_square_item, parent, false);
            TextView countView = (TextView) convertView.findViewById(R.id.count_tv);
            switch (position){
                case 5:
                    if(msgCount == 0){
                        countView.setVisibility(View.GONE);
                    }else{
                        countView.setVisibility(View.VISIBLE);
                        countView.setText(""+msgCount);
                    }

                    break;
                default:
                    countView.setVisibility(View.GONE);
                    break;
            }
            TextView squareTitle = (TextView) convertView.findViewById(R.id.square_title);
            convertView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(getScreenWidth()-getResources().getDimensionPixelSize(R.dimen.px2dp_128))/2));

            convertView.setBackgroundResource(portalbgs[position]);
            squareTitle.setText(portalNames[position]);

            return convertView;
        }
    }

    public int getScreenWidth(){
        return getActivity().getWindowManager().getDefaultDisplay().getWidth();
    }


}
