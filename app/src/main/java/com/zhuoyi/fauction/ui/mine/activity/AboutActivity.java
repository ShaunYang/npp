package com.zhuoyi.fauction.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuoyi.fauction.ui.BaseActivity;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.utils.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.about_version)
    TextView about_version;

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


        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        about_version.setText("侬拍拍"+Util.getAppVersionName(AboutActivity.this,"com.zhuoyi.fauction"));




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
