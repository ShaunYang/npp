package com.zhuoyi.fauction.ui.mine.activity;



import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
public class AddAcountZFBaoActivity extends BaseActivity {

	@Bind(R.id.title) TextView title;
	@Bind(R.id.username) EditText username;
	@Bind(R.id.zfb_account) EditText zfbAccount;






	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_add_zfbao);
		ButterKnife.bind(this);

		initView();



	}

	@OnClick(R.id.back) void onBackClick() {


			onBackPressed();

	}



	@OnClick(R.id.confirm_ok) void onConfirmOkClick() {
		increaseZFBaoPost();
	}


		//添加地址接口
	private void increaseZFBaoPost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddAcountZFBaoActivity.this, timestamp, "Wallet", "addAccount");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.WALLET_ACCOUNT)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddAcountZFBaoActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddAcountZFBaoActivity.this))
				.addParams("user_id", ConfigUserManager.getUserId(AddAcountZFBaoActivity.this))
				.addParams("type",1+"")
				.addParams("uname", username.getText().toString())
				.addParams("account", zfbAccount.getText().toString())
				.addParams("bank", "支付宝")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "add", response);
						//Gson gson = new Gson();
						//Order order = gson.fromJson(response, Order.class);
						//跳转收货地址列表
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code == 0) {
							//跳转收货地址列表
							ToastUtil.showLongToast(AddAcountZFBaoActivity.this, "添加成功");
							Intent intent = new Intent(AddAcountZFBaoActivity.this, TXActivity.class);
							startActivity(intent);
							finish();
						} else if (code == -1) {
							ToastUtil.showLongToast(AddAcountZFBaoActivity.this, "登陆超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(AddAcountZFBaoActivity.this, "添加失败");
						}


					}
				});
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




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {
		//productId = getIntent().getIntExtra("productId", -1);

	}












}
