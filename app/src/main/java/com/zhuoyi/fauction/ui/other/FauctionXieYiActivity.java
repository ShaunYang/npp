package com.zhuoyi.fauction.ui.other;



import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.PayPo;
import com.zhuoyi.fauction.model.ReminderPo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.ui.other.adapter.MyGridAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class FauctionXieYiActivity extends BaseActivity {




	@Bind(R.id.webview)
	WebView webview;



	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.xieyi_fauction);
		ButterKnife.bind(this);

		initView();



    }

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
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

	private String TAG="FauctionXieYiActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {
		webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());
		// webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// 允许JavaScript执行
		webview.getSettings().setJavaScriptEnabled(true);
		// webView.addJavascriptInterface(new Contact(), "contact");
		webview.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview.getSettings().setAllowFileAccess(true);

		// 加载index.html
		webview.loadUrl("http://api.nongpaipai.cn/v1/new/index/id/2");

	}












}
