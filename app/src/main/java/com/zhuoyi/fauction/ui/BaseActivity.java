package com.zhuoyi.fauction.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yintai.app_common.ui.common.view.CommonSimpleTitleDialog;
import com.yintai.common.util.NetUtil;

/**
 * Created by Apple on 16/8/25.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private BroadcastReceiver connectionReceiver;
    public String TAG = this.getClass().getSimpleName();
    private ProgressDialog dialog;

    public BaseActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        this.initWindow();
        super.onCreate(savedInstanceState);
        this.initComponent(savedInstanceState);
        this.initEvent();
        this.initData();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        this.connectionReceiver = new BaseActivity.AppNetWorkConnectionReceiver();
        this.registerReceiver(this.connectionReceiver, intentFilter);
    }

    public void showDialog(String msg) {
        this.dialog = new ProgressDialog(this, com.yintai.app_common.R.style.common_dialog_style);
        this.dialog.setCanceledOnTouchOutside(false);
        this.dialog.setMessage(msg);
        if(!this.dialog.isShowing()) {
            this.dialog.show();
        }

    }

    public void dismiss() {
        if(this.dialog != null && this.dialog.isShowing()) {
            this.dialog.dismiss();
        }

    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, String title, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        showCustomerSimpleTitleDialog(context, cancel, title, context.getString(com.yintai.app_common.R.string.submit), context.getString(com.yintai.app_common.R.string.cancle), submitClickListener, negativeClickListener);
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, String title, String submitText, String cancelText, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        showCustomerSimpleTitleDialog(context, cancel, 0, title, submitText, cancelText, submitClickListener, negativeClickListener);
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, int gravity, String title, String submitText, String cancelText, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        CommonSimpleTitleDialog simpleTitleDialog = new CommonSimpleTitleDialog(context, com.yintai.app_common.R.style.common_dialog_style, gravity);
        simpleTitleDialog.setCanceledOnTouchOutside(cancel);
        simpleTitleDialog.setTitle(title);
        simpleTitleDialog.setSubmitText(submitText);
        simpleTitleDialog.setCancelText(cancelText);
        simpleTitleDialog.setSubmitClickListener(submitClickListener);
        simpleTitleDialog.setNegativeClickListener(negativeClickListener);
        if(!simpleTitleDialog.isShowing()) {
            simpleTitleDialog.show();
        }

    }

    protected abstract void initComponent(Bundle var1);

    protected abstract void initEvent();

    protected abstract void initData();

    protected abstract void pause();

    protected abstract void resume();

    protected abstract void destory();

    protected void initWindow() {
    }

    protected abstract void onNetWorkReConnected();

    protected abstract void onNetWorkConnectFail();

    protected void onResume() {
        super.onResume();
        this.resume();
    }

    protected void onPause() {
        super.onPause();
        this.pause();
    }

    protected void onDestroy() {
        this.unregisterReceiver(this.connectionReceiver);
        this.destory();
        super.onDestroy();
    }

    private class AppNetWorkConnectionReceiver extends BroadcastReceiver {
        private AppNetWorkConnectionReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if(NetUtil.hasNetwork(context)) {
                BaseActivity.this.onNetWorkReConnected();
            } else {
                BaseActivity.this.onNetWorkConnectFail();
            }

            ConnectivityManager connectMgr = (ConnectivityManager)BaseActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(0);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(1);
            if(mobNetInfo==null){
                //没有移动网络
                if(!wifiNetInfo.isConnected()) {
                    BaseActivity.this.onNetWorkConnectFail();
                } else {
                    BaseActivity.this.onNetWorkReConnected();
                }

            }else{
                if(!mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                    BaseActivity.this.onNetWorkConnectFail();
                } else {
                    BaseActivity.this.onNetWorkReConnected();
                }
            }


        }
    }
}
