package com.zhuoyi.fauction.ui.mine.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class AlertUserNameActivity extends Activity {

    @Bind(R.id.new_username)
    LinearLayout newUsername;
    @Bind(R.id.ed_username) EditText edUsername;



    private String TAG="AlertUserNameActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_username);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }


    public void initComponent(){

    }


    private Context getActivity(){
        Activity activity=AlertUserNameActivity.this;
        return activity;
    }

    @OnClick(R.id.confirm_ok)
    public void alertOk(){
        alertUsernamePost();
    }

    private void alertUsernamePost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Member","editUserName");

            OkHttpUtils.post()
                    .url(Constant.MEMBE_EDITUSERNAME)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getActivity()))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                    .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                    .addParams("user_name", edUsername.getText().toString().trim())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {

                            Logger.i(TAG, response);
                            JSONObject jsonObject = JSONObject.parseObject(response);
                            int code = jsonObject.getIntValue("code");
                            if (code == 0) {
                                ToastUtil.showLongToast(getActivity(), "修改成功");

                            } else if (code == -1) {
                                ToastUtil.showLongToast(getActivity(), "登录超时");

                            } else if (code == -2) {
                                ToastUtil.showLongToast(getActivity(), "用户名不符合规范");

                            } else if (code == -3) {
                                ToastUtil.showLongToast(getActivity(), "用户名修改失败");

                            }


                        }
                    });




    }





    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
