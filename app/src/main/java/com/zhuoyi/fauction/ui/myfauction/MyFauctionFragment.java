package com.zhuoyi.fauction.ui.myfauction;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.mine.fragment.LoginFragment;
import com.zhuoyi.fauction.ui.mine.fragment.RegisterFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionAllFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionFinishFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnGetFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnPayFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnSendFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;


import butterknife.ButterKnife;


public class MyFauctionFragment extends Fragment {
    ViewPager viewPager;
    PagerSlidingTabStrip tabs;
    private View viewHolder;
    MyPagerAdapter adapter;
    public static MyFauctionFragment newInstance(String param1, String param2) {
        MyFauctionFragment fragment = new MyFauctionFragment();
        return fragment;

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(ConfigUserManager.isAlreadyLogin(getContext())){
            if (viewHolder == null) {
                viewHolder = inflater.inflate(R.layout.fragment_my_fauction, container, false);
                ButterKnife.bind(this, viewHolder);
                BusProvider.getInstance().register(this);
                Common.home_tab=3;
                initComponent();
            }
            ViewGroup parent = (ViewGroup) viewHolder.getParent();
            if (parent != null) {
                parent.removeView(viewHolder);
            }
            ButterKnife.bind(this, viewHolder);

        }else{
            ((MainActivity)getActivity()).toFragment(new LoginFragment());
        }
        return viewHolder;
    }


    @Override
    public void onStart() {
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Common.home_tab=3;
            initComponent();
        }else{
            ((MainActivity)getActivity()).toFragment(new LoginFragment());
        }
        super.onStart();
    }


    private void initComponent(){
        Common.whichActivity=0;
        viewPager = (ViewPager) viewHolder.findViewById(R.id.viewPager);
       // viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));

        PagerAdapter adapter = viewPager.getAdapter();
        if(adapter==null){
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    MyFauctionFragment.this.adapter = new MyPagerAdapter(getFragmentManager());
                    viewPager.setAdapter(MyFauctionFragment.this.adapter);
                    tabs = (PagerSlidingTabStrip) viewHolder.findViewById(R.id.tabs);
                    tabs.setViewPager(viewPager);
                    tabs.setDividerColor(0xFFFFFF);


                }
            }, 100);
        }else{
            tabs.setScrollOffset(0);
            viewPager.setCurrentItem(0);

        }



        //tabs.setTextSize(20);
        //tabs.setSelectedTextColor(R.color.txt_color_green);


    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "全部", "待支付", "待发货","待收货","交易完成" };
        FauctionAllFragment fauctionAllFragment;
        FauctionUnPayFragment fauctionUnPayFragment;
        FauctionUnSendFragment fauctionUnSendFragment;
        FauctionUnGetFragment fauctionUnGetFragment;
        FauctionFinishFragment fauctionFinishFragment;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fauctionAllFragment = new FauctionAllFragment();
                    return fauctionAllFragment;
                case 1:
                    fauctionUnPayFragment = new FauctionUnPayFragment();
                    return fauctionUnPayFragment;
                case 2:
                    fauctionUnSendFragment = new FauctionUnSendFragment();
                    return fauctionUnSendFragment;
                case 3:
                    fauctionUnGetFragment = new FauctionUnGetFragment();
                    return fauctionUnGetFragment;
                case 4:
                    fauctionFinishFragment = new FauctionFinishFragment();
                    return fauctionFinishFragment;

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
