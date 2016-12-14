package com.zhuoyi.fauction.ui.mine.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.net.Constant;
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
public class ContactUsActivity extends BaseActivity {




	@Bind(R.id.webview)
	WebView webview;



	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.contact_us);
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
		/*webview.setWebChromeClient(new WebChromeClient());
		webview.setWebViewClient(new WebViewClient());*/
		// webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// 允许JavaScript执行
		webview.getSettings().setJavaScriptEnabled(true);
		// webView.addJavascriptInterface(new Contact(), "contact");
		webview.getSettings().setPluginState(WebSettings.PluginState.ON);
		webview.getSettings().setAllowFileAccess(true);

		// 加载index.html
		webview.loadUrl(Constant.CONTACTS_US);
		webview.setWebViewClient(new WebViewClient(){
			@Override
			public void onLoadResource(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onLoadResource(view, url);
			}
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.contains("tel:")) {
					Uri uri = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
					return true;
				}
				return super.shouldOverrideUrlLoading(view, url);
			}
		});

	}












}
