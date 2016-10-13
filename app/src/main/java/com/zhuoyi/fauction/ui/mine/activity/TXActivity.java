package com.zhuoyi.fauction.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.zhuoyi.fauction.ui.home.fragment.HRJXFragment;
import com.zhuoyi.fauction.ui.home.fragment.JRZQFragment;
import com.zhuoyi.fauction.ui.home.fragment.PMYZFragment;
import com.zhuoyi.fauction.ui.home.fragment.ZXCJFragment;
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

public class TXActivity extends BaseActivity {


    @Bind(R.id.new_username)
    LinearLayout newUsername;
    @Bind(R.id.canuse_money)
    TextView canuseMoney;
    @Bind(R.id.tx_money)
    EditText txMoney;
    @Bind(R.id.tabs) PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPagers) ViewPager viewPagers;
    String[] title = { "提现到支付宝", "提现到银行卡"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @Override
    protected void onRestart() {
        viewPagers.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPagers);
        tabs.setDividerColor(0xFFFFFF);
        viewPagers.setCurrentItem(0);
        super.onRestart();
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_tx);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        int tab = intent.getIntExtra("tab", 0);
       // String canUseMoney=intent.getStringExtra("canUseMoney");
        canuseMoney.setText("￥" + Common.yuer);
        viewPagers.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        tabs.setViewPager(viewPagers);
        tabs.setDividerColor(0xFFFFFF);
        viewPagers.setCurrentItem(0);



    }

    @OnClick(R.id.confirm_ok) void onConfirmOkClick() {
        txPost();
    }

    private void txPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(TXActivity.this, timestamp, "Wallet", "withdrawals");

        String money=txMoney.getText().toString().trim();

        Logger.i(TAG + "money=", money);

        if(money.equals("")||money==null){
            ToastUtil.showLongToast(TXActivity.this, "请输入提现金额");
            return;
        }

        if(Common.accountId!=null){
            OkHttpUtils.post()
                    .url(Constant.WALLET_WITHDRAWALS)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(TXActivity.this))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(TXActivity.this))
                    .addParams("user_id",ConfigUserManager.getUserId(TXActivity.this))
                    .addParams("price",txMoney.getText().toString().trim())
                    .addParams("id",Common.accountId)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {

                            Logger.i(TAG + "333333333333=", response);
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            int code = jsonObject.getIntValue("code");
                            if(code==0){
                                ToastUtil.showLongToast(TXActivity.this, "提现申请成功，您提醒的金额将在48小时内到达您的账户中");
                                Intent intent=new Intent(TXActivity.this, MineWalletActivity.class);
                                startActivity(intent);
                                finish();
                            }else if(code==-1){
                                ToastUtil.showLongToast(TXActivity.this, "登录超时");
                            }else if(code==-2){
                                ToastUtil.showLongToast(TXActivity.this, "余额不足");
                            }else if(code==-3){
                                ToastUtil.showLongToast(TXActivity.this, "系统忙，请重新提交");
                            }else if(code==-4){
                                ToastUtil.showLongToast(TXActivity.this, "提现账户不存在");
                            }


                        }
                    });
        }else{
            ToastUtil.showLongToast(TXActivity.this, "请选择提现账户");
        }


    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        TXYHKListFragment txyhkListFragment;
        TXZFBListFragment txzfbListFragment;


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }





        @Override
        public Fragment getItem(int position) {
            switch (position) {

                case 0:
                    txzfbListFragment = new TXZFBListFragment();
                   // mainCategory.setTitle(title[1]);
                    return txzfbListFragment;
                case 1:
                    txyhkListFragment = new TXYHKListFragment();
                    //mainCategory.setTitle(title[2]);
                    return txyhkListFragment;


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
