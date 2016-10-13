package com.zhuoyi.fauction.ui.mine.activity;


import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.ui.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class PicketHelpActivity extends BaseActivity {




	@Bind(R.id.webview)
	WebView webview;



	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.pickethelp_fauction);
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
		webview.loadUrl("http://api.nongpaipai.cn/v2/new/index/id/6");

	}












}
