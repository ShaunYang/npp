package com.zhuoyi.fauction.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.mine.fragment.TXYHKListFragment;
import com.zhuoyi.fauction.ui.mine.fragment.TXZFBListFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.to_about)
    RelativeLayout toAbout;
    @Bind(R.id.version) TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @OnClick(R.id.to_about) void toAboutClick() {
       Intent intent=new Intent(SettingActivity.this,AboutActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.to_contact) void toContactClick() {
        Intent intent=new Intent(SettingActivity.this,ContactUsActivity.class);
        startActivity(intent);
    }



    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.setting);
        ButterKnife.bind(this);

        version.setText("V"+Util.getAppVersionName(SettingActivity.this,"com.zhuoyi.fauction"));




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
