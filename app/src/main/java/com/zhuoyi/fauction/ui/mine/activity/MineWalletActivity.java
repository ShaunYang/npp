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
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.mine.fragment.AlertPasswordActivity;
import com.zhuoyi.fauction.ui.mine.fragment.AlertUserNameActivity;
import com.zhuoyi.fauction.ui.other.AccountDetailActivity;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.ui.other.ChongPayActivity;
import com.zhuoyi.fauction.util.Toast;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import okhttp3.Call;

public class MineWalletActivity extends Activity  {

    private static final String TAG = "MineWalletActivity";

    @Bind(R.id.to_account) TextView toAccount;
    @Bind(R.id.all_money) TextView allMoney;
    @Bind(R.id.djbond) TextView djbond;
    @Bind(R.id.yuer) TextView yuer;

    String canUseMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        walletIndetPost();
    }

    @Override
    protected void onRestart() {
        walletIndetPost();
        super.onRestart();
    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @OnClick(R.id.to_account) void toAccountClick() {
       Intent intent=new Intent(MineWalletActivity.this, AccountDetailActivity.class);
        startActivity(intent);
    }



    private void walletIndetPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(MineWalletActivity.this, timestamp, "Wallet", "index");

        OkHttpUtils.post()
                .url(Constant.WALLET_INDEX)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(MineWalletActivity.this))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(MineWalletActivity.this))
                .addParams("user_id", ConfigUserManager.getUserId(MineWalletActivity.this))
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
                        if (code == 0) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            allMoney.setText("￥" + data.getString("total"));
                            djbond.setText("￥" + data.getString("frozen"));
                            yuer.setText("￥" + data.getString("wallet"));
                            canUseMoney = data.getString("wallet");
                            Common.yuer = canUseMoney;
                        } else if (code == -1) {
                            ToastUtil.showLongToast(MineWalletActivity.this, "登录超时");
                        } else if (code == -2) {
                            ToastUtil.showLongToast(MineWalletActivity.this, "数据不存在");
                        }


                    }
                });
    }




    @OnClick(R.id.cz) void onCzClick() {
        Intent intent=new Intent(MineWalletActivity.this,ChongPayActivity.class);
        //intent.putExtra("canUseMoney",canUseMoney);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tx) void onTxClick() {
       Intent intent=new Intent(MineWalletActivity.this,TXActivity.class);
        intent.putExtra("canUseMoney",canUseMoney);
        startActivity(intent);
        finish();
    }
}
