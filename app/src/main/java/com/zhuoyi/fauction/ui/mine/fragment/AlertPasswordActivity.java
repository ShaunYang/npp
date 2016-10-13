package com.zhuoyi.fauction.ui.mine.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class AlertPasswordActivity extends Activity {
    private View viewHolder;
    @Bind(R.id.user_phone)
    EditText user_phone;

    @Bind(R.id.comfirm_psd)
    EditText comfirm_psd;
    @Bind(R.id.get_code)
    Button get_code;

    @Bind(R.id.title) TextView title;


    @Bind(R.id.code) EditText code;
    @Bind(R.id.password) EditText password;



    private String TAG="AlertPasswordActivity";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_password);
        ButterKnife.bind(this);
        String titleName = getIntent().getStringExtra("title");
        if(titleName!=null&&!"".equals(titleName)){
            title.setText(titleName);
        }
    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }


    public void initComponent(){

    }



    @OnClick(R.id.get_code)
    public void getPhoneCode(){
        sendCodePost();
    }

    private void sendCodePost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(AlertPasswordActivity.this,timestamp,"Member","sendCode");

            OkHttpUtils.post()
                    .url(Constant.MEMBER_SENDCODE)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AlertPasswordActivity.this))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(AlertPasswordActivity.this))
                    .addParams("user_id",ConfigUserManager.getUserId(AlertPasswordActivity.this))
                    .addParams("mobile",user_phone.getText().toString().trim())
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
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "成功");
                                //跳到登录页面重新登录

                            } else if (code == -1) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "手机号错误");
                            } else if (code == -2) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "发送失败  \n" +
                                        "短信验证码有效时间为3分钟（180秒）");
                            }


                        }
                    });




    }

    @OnClick(R.id.confirm_ok)
    public void alertOk(){
        alertPsdPost();
    }

    private void alertPsdPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(AlertPasswordActivity.this,timestamp,"Member","forgotPassword");

            OkHttpUtils.post()
                    .url(Constant.MEMBER_FORGOTPASSWORD)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AlertPasswordActivity.this))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(AlertPasswordActivity.this))
                    .addParams("user_id", ConfigUserManager.getUserId(AlertPasswordActivity.this))
                    .addParams("mobile", user_phone.getText().toString().trim())
                    .addParams("code",code.getText().toString().trim())
                    .addParams("pwd",password.getText().toString().trim())
                    .addParams("pwd2",comfirm_psd.getText().toString().trim())

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
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "修改成功");
                                memberLoginOutPost();

                            } else if (code == -1) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "手机号错误");

                            } else if (code == -2) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "密码字符只能由字母和数字组成");

                            } else if (code == -3) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "密码长度须在6-16个任意字母或数字组合");

                            } else if (code == -4) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "两次输入的密码不一致");

                            } else if (code == -5) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "密码找回失败");

                            } else if (code == -6) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "验证码错误");

                            } else if (code == -7) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "用户不存在");

                            } else if (code == -8) {
                                ToastUtil.showLongToast(AlertPasswordActivity.this, "新密码不能和旧密码一致");

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

    private void memberLoginOutPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(AlertPasswordActivity.this, timestamp, "Member", "loginOut");

        OkHttpUtils.post()
                .url(Constant.MEMBER_LOGINOUT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AlertPasswordActivity.this))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(AlertPasswordActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(AlertPasswordActivity.this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "333333333333=", response);
                        Logger.e("mgc", "退出登录！！！");

                        //取消token
                        ConfigUserManager.setToken(AlertPasswordActivity.this, null);
                        ConfigUserManager.setAlreadyLogin(AlertPasswordActivity.this, false);
                        //ConfigUserManager.isAlreadyLogin(getContext()) = false;
                        Common.categories=null;
                        Intent intent = new Intent(AlertPasswordActivity.this, MainActivity.class);
                        startActivity(intent);


                    }
                });
    }
}
