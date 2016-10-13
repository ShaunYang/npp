package com.zhuoyi.fauction.ui.mine.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.view.CommonTopBar;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineRemindActivity extends BaseActivity {


    @Bind(R.id.tabs) PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager) ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initComponent(Bundle bundle) {
        setContentView(R.layout.activity_mine_remind);
        ButterKnife.bind(this);
        tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPager);
        tabs.setDividerColor(0xFFFFFF);
    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "即将开始", "进行中", "已结束"};
        RemindPreStartFragment fauctionAllFragment;
        RemindDoFragment fauctionUnPayFragment;
        RemindEndFragment fauctionUnSendFragment;



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fauctionAllFragment = new RemindPreStartFragment();
                    return fauctionAllFragment;
                case 1:
                    fauctionUnPayFragment = new RemindDoFragment();
                    return fauctionUnPayFragment;
                case 2:
                    fauctionUnSendFragment = new RemindEndFragment();
                    return fauctionUnSendFragment;


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


    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void pause() {

    }

    @Override
    protected void resume() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected void onNetWorkReConnected() {

    }

    @Override
    protected void onNetWorkConnectFail() {

    }
}
