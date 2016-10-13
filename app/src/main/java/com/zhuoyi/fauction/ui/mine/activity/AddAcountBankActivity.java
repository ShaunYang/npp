package com.zhuoyi.fauction.ui.mine.activity;



import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.ui.other.AreaActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
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
public class AddAcountBankActivity extends BaseActivity {

	@Bind(R.id.title) TextView title;
	@Bind(R.id.bank_name) EditText bankName;
	@Bind(R.id.bank_account) EditText bankAccount;
	@Bind(R.id.bank_username) EditText bankUsername;
	@Bind(R.id.bank_city) EditText bankCity;





	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_add_bank);
		ButterKnife.bind(this);

		initView();



	}

	@OnClick(R.id.back) void onBackClick() {


			onBackPressed();

	}



	@OnClick(R.id.confirm_ok) void onConfirmOkClick() {
		increaseBankPost();
	}


		//添加地址接口
	private void increaseBankPost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddAcountBankActivity.this, timestamp, "Wallet", "addAccount");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.WALLET_ACCOUNT)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddAcountBankActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddAcountBankActivity.this))
				.addParams("user_id", ConfigUserManager.getUserId(AddAcountBankActivity.this))
				.addParams("type",2+"")
				.addParams("uname", bankUsername.getText().toString())
				.addParams("account", bankAccount.getText().toString())
				.addParams("bank", bankName.getText().toString())
				.addParams("deposit", bankCity.getText().toString())
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
							ToastUtil.showLongToast(AddAcountBankActivity.this, "添加成功");
							Intent intent = new Intent(AddAcountBankActivity.this, TXActivity.class);
							startActivity(intent);
							finish();
						} else if (code == -1) {
							ToastUtil.showLongToast(AddAcountBankActivity.this, "登陆超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(AddAcountBankActivity.this, "添加失败");
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
