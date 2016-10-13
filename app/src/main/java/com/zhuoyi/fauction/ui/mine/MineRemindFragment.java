package com.zhuoyi.fauction.ui.mine;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.ui.mine.fragment.RemindDoFragment;
import com.zhuoyi.fauction.ui.mine.fragment.RemindEndFragment;
import com.zhuoyi.fauction.ui.mine.fragment.RemindPreStartFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionAllFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionFinishFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnGetFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnPayFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnSendFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;

import butterknife.ButterKnife;


public class MineRemindFragment extends Fragment {
    ViewPager viewPager;
    PagerSlidingTabStrip tabs;
    private View viewHolder;
    public static MineRemindFragment newInstance(String param1, String param2) {
        MineRemindFragment fragment = new MineRemindFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_my_fauction, container, false);
            ButterKnife.bind(this, viewHolder);
            BusProvider.getInstance().register(this);
            initComponent();
        }
        ViewGroup parent = (ViewGroup) viewHolder.getParent();
        if (parent != null) {
            parent.removeView(viewHolder);
        }
        ButterKnife.bind(this, viewHolder);
        return viewHolder;
    }

    private void initComponent(){
        viewPager = (ViewPager) viewHolder.findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        tabs = (PagerSlidingTabStrip) viewHolder.findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setDividerColor(0xFFFFFF);

        //tabs.setTextSize(20);
        //tabs.setSelectedTextColor(R.color.txt_color_green);


    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "即将开始", "进行中", "已结束" };
        RemindPreStartFragment remindPreStartFragment;
        RemindDoFragment remindDoFragment;
        RemindEndFragment remindEndFragment;



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    remindPreStartFragment = new RemindPreStartFragment();
                    return remindPreStartFragment;
                case 1:
                    remindDoFragment = new RemindDoFragment();
                    return remindDoFragment;
                case 2:
                    remindEndFragment = new RemindEndFragment();
                    return remindEndFragment;


                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
        }

    }


    //在恢复里及时更新数据
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
