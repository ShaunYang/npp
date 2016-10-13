package com.zhuoyi.fauction.ui.home.activity;

import android.content.Intent;
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
import com.zhuoyi.fauction.ui.home.fragment.HRJXFragment;
import com.zhuoyi.fauction.ui.home.fragment.JRZQFragment;
import com.zhuoyi.fauction.ui.home.fragment.PMYZFragment;
import com.zhuoyi.fauction.ui.home.fragment.ZXCJFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionAllFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionFinishFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnGetFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnPayFragment;
import com.zhuoyi.fauction.ui.myfauction.fragment.FauctionUnSendFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeTabActivity extends BaseActivity {


    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    String[] title = { "捡漏专区", "拍卖预展", "火热进行","最新成交" };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_home_tab);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        int tab = intent.getIntExtra("tab", 0);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setDividerColor(0xFFFFFF);
        viewPager.setCurrentItem(tab);



    }


    class MyPagerAdapter extends FragmentPagerAdapter {

       HRJXFragment hrjxFragment;
        JRZQFragment jrzqFragment;
        PMYZFragment pmyzFragment;
        ZXCJFragment zxcjFragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }





        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 2:
                    hrjxFragment = new HRJXFragment();
                   // mainCategory.setTitle(title[0]);
                    return hrjxFragment;
                case 0:
                    jrzqFragment = new JRZQFragment();
                   // mainCategory.setTitle(title[1]);
                    return jrzqFragment;
                case 1:
                    pmyzFragment = new PMYZFragment();
                    //mainCategory.setTitle(title[2]);
                    return pmyzFragment;
                case 3:
                    zxcjFragment = new ZXCJFragment();
                   // mainCategory.setTitle(title[3]);
                    return zxcjFragment;


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
