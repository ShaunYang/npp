package com.zhuoyi.fauction.ui.mine.bond;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.zhuoyi.fauction.ui.BaseActivity;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.ui.mine.bond.fragment.BondAllFragment;
import com.zhuoyi.fauction.ui.mine.bond.fragment.BondFinishFragment;
import com.zhuoyi.fauction.ui.mine.bond.fragment.BondUnReturnFragment;
import com.zhuoyi.fauction.ui.mine.bond.fragment.BondReturnFragment;
import com.zhuoyi.fauction.ui.mine.bond.fragment.BondDiyaFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BondDetailActivity extends BaseActivity {

    @Bind(R.id.tabs) PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager) ViewPager viewPager;
    MyPagerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void initComponent(Bundle bundle) {
        initComponent();
    }


    private void initComponent(){
        setContentView(R.layout.activity_my_bond);
        ButterKnife.bind(this);
       /* viewPager = (ViewPager) viewHolder.findViewById(R.id.viewPager);*/
       // viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                adapter = new MyPagerAdapter(getSupportFragmentManager());
                viewPager.setAdapter(adapter);
                //tabs = (PagerSlidingTabStrip) viewHolder.findViewById(R.id.tabs);
                tabs.setViewPager(viewPager);
                tabs.setDividerColor(0xFFFFFF);
            }
        }, 100);

        //tabs.setTextSize(20);
        //tabs.setSelectedTextColor(R.color.txt_color_green);


    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        String[] title = { "全部", "已缴纳", "待退还","已退还","已抵押" };
        BondAllFragment fauctionAllFragment;
        BondReturnFragment fauctionUnPayFragment;
        BondDiyaFragment fauctionUnSendFragment;
        BondUnReturnFragment fauctionUnGetFragment;
        BondFinishFragment fauctionFinishFragment;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fauctionAllFragment = new BondAllFragment();
                    return fauctionAllFragment;
                case 1:
                    fauctionFinishFragment = new BondFinishFragment();
                    return fauctionFinishFragment;
                case 2:

                fauctionUnGetFragment = new BondUnReturnFragment();
                return fauctionUnGetFragment;
                case 3:
                    fauctionUnPayFragment = new BondReturnFragment();
                    return fauctionUnPayFragment;

                case 4:

                fauctionUnSendFragment = new BondDiyaFragment();
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


    //在恢复里及时更新数据
    @Override
    public void onResume() {

        super.onResume();
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
