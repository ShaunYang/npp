package com.zhuoyi.fauction.ui.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yintai.common.util.Logger;
import com.yintai.common.view.CommonTopBar;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.ui.mine.fragment.AlertPasswordActivity;
import com.zhuoyi.fauction.ui.mine.fragment.AlertUserNameActivity;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MineSelectCatetoryActivity extends Activity  {

    private static final String TAG = "MineSelectCatetoryActivity";
    private MyGridView gridview;
    private MyGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_category);
        ButterKnife.bind(this);

        if(Common.categories!=null){
            gridview=(MyGridView) findViewById(R.id.gridview);
            mAdapter=new MyGridAdapter(MineSelectCatetoryActivity.this);
            mAdapter.setCategories(Common.categories);
            gridview.setAdapter(mAdapter);
        }
    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }








}
