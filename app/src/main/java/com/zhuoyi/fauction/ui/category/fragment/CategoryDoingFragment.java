package com.zhuoyi.fauction.ui.category.fragment;

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
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionAllFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionFinishFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnGetFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnPayFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnSendFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;


import butterknife.ButterKnife;


public class CategoryDoingFragment extends Fragment {
    ViewPager viewPager;
    PagerSlidingTabStrip tabs;
    private View viewHolder;
    public static CategoryDoingFragment newInstance(String param1, String param2) {
        CategoryDoingFragment fragment = new CategoryDoingFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_category_doing, container, false);
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
        String[] title = { "全部", "玉米", "辣椒","四季豆","毛豆" };
        CategoryDoListFragment categoryDoListFragment1;
        CategoryDoListFragment categoryDoListFragment2;
        CategoryDoListFragment categoryDoListFragment3;
        CategoryDoListFragment categoryDoListFragment4;
        CategoryDoListFragment categoryDoListFragment5;



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    categoryDoListFragment1 = new CategoryDoListFragment();
                    return categoryDoListFragment1;
                case 1:
                    categoryDoListFragment2 = new CategoryDoListFragment();
                    return categoryDoListFragment2;
                case 2:
                    categoryDoListFragment3 = new CategoryDoListFragment();
                    return categoryDoListFragment3;
                case 3:
                    categoryDoListFragment4 = new CategoryDoListFragment();
                    return categoryDoListFragment4;
                case 4:
                    categoryDoListFragment5 = new CategoryDoListFragment();
                    return categoryDoListFragment5;

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
