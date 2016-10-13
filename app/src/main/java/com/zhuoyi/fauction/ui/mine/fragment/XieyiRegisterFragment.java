package com.zhuoyi.fauction.ui.mine.fragment;

import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
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

public class XieyiRegisterFragment extends Fragment  {

    private static final String TAG ="XieyiRegisterFragment" ;


    @Bind(R.id.webview)
    WebView webview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.xieyi_register, null);
    }

    @OnClick(R.id.back) void onBackClick() {
        ((MainActivity)getActivity()).toFragment(new RegisterFragment());
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());
        // webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 允许JavaScript执行
        webview.getSettings().setJavaScriptEnabled(true);
        // webView.addJavascriptInterface(new Contact(), "contact");
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setAllowFileAccess(true);

        // 加载index.html
        webview.loadUrl("http://api.nongpaipai.cn/v1/new/index/id/1");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

//    @OnClick(R.id.agree_xieyi) void onAgreeXieyiClick() {
//        newAgreementPost();
//    }

    private void newAgreementPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(), timestamp, "New", "agreement");

        OkHttpUtils.post()
                .url(Constant.NEW_AGREEMENT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
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
                            String data=jsonObject.getJSONObject("data").getString("data");
                            //xieyiContent.setText(data);
                        } else if (code == -1) {
                            ToastUtil.showLongToast(getActivity(),"没有数据");
                        }


                    }
                });




    }
}
