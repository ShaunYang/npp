package com.zhuoyi.fauction.ui.other;



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
public class AddressAlertActivity extends BaseActivity {



	@Bind(R.id.back) ImageView back;
	@Bind(R.id.add_finish) TextView addFinish;
	@Bind(R.id.name) EditText name;
	@Bind(R.id.mobile) EditText mobile;
	@Bind(R.id.area)
	RelativeLayout area;
	@Bind(R.id.address_detail)
	EditText addressDetail;

	@Bind(R.id.sel_area) TextView sel_area;


	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.add_address);
		ButterKnife.bind(this);

		initView();



	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
	}
		//添加地址接口
	private void increaseAddressPost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddressAlertActivity.this, timestamp, "Member", "editAddress");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MEMBER_EDITADDRESS)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddressAlertActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddressAlertActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(AddressAlertActivity.this))
				.addParams("uname",name.getText().toString())
				.addParams("mobile",mobile.getText().toString())
				.addParams("zip","22222")
				.addParams("area", Common.area.getId())
				.addParams("address", addressDetail.getText().toString())
				.addParams("id", getIntent().getStringExtra("address_id"))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "add", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code == 0) {
							//跳转收货地址列表
							Intent intent = new Intent(AddressAlertActivity.this, AddressActivity.class);
							startActivity(intent);
							finish();
						} else if (code == -1) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "登陆超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "收货人姓名不能为空  \n" +
									"短信验证码有效时间为3分钟（180秒）");
						} else if (code == -3) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "收货人手机不能为空");
						} else if (code == -4) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "收获地址不能为空");
						} else if (code == -5) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "请选择收货所在地");
						} else if (code == -6) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "收货信息修改失败");
						} else if (code == -7) {
							ToastUtil.showLongToast(AddressAlertActivity.this, "非法操作");
						}
						//Gson gson = new Gson();
						//Order order = gson.fromJson(response, Order.class);



					}
				});
	}





	@OnClick(R.id.add_finish) void onFinishAddClick() {
		increaseAddressPost();

	}
	@OnClick(R.id.area) void toAeraSelectClick() {
			Intent intent=new Intent();
			intent.setClass(AddressAlertActivity.this,AreaActivity.class);
			startActivityForResult(intent, 0);

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
			case RESULT_OK:
				Bundle b=data.getExtras(); //data为B中回传的Intent
				String  mCurrentProviceName=data.getStringExtra("mCurrentProviceName");
				String mCurrentCityName=data.getStringExtra("mCurrentCityName");
				String mCurrentDistrictName=data.getStringExtra("mCurrentDistrictName");
				sel_area.setText(mCurrentProviceName+" "+mCurrentCityName+" "+mCurrentDistrictName);
				break;
			default:
				break;
		}
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


	}












}
