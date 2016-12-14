package com.zhuoyi.fauction.ui.other;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Order;
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
public class AddressAddActivity extends BaseActivity {



	@Bind(R.id.back) ImageView back;
	@Bind(R.id.add_finish) TextView addFinish;
	@Bind(R.id.name) EditText name;
	@Bind(R.id.mobile) EditText mobile;
	@Bind(R.id.area)
	RelativeLayout area;
	@Bind(R.id.address_detail)
	EditText addressDetail;

	@Bind(R.id.sel_area) TextView sel_area;

	int productId=-1;


	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.add_address);
		ButterKnife.bind(this);

		initView();



	}

	@OnClick(R.id.back) void onBackClick() {

		if(productId!=-1){
				Intent intent=new Intent(AddressAddActivity.this,OrderSubmitActivity.class);
			    intent.putExtra("productId",productId);
			    startActivity(intent);
			    finish();
		}else{
			onBackPressed();
		}

	}
		//添加地址接口
	private void increaseAddressPost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddressAddActivity.this, timestamp, "Member", "increaseAddress");
		String areaId="";
		if(Common.area==null){
			if(Common.city==null){
				areaId=Common.province.getId();
			}else{
				areaId=Common.city.getId();
			}
		}else{
			areaId=Common.area.getId();
		}
		Logger.i("areaId","areaId="+ areaId);

		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MEMBER_INCREASEADDRESS)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddressAddActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddressAddActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(AddressAddActivity.this))
				.addParams("uname",name.getText().toString())
				.addParams("mobile",mobile.getText().toString())
				.addParams("zip","22222")
				.addParams("area", areaId)
				.addParams("address",addressDetail.getText().toString())
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
							String id = jsonObject.getString("id");
							setAddressDefaultPost(id);
						} else if (code == -1) {
							ToastUtil.showLongToast(AddressAddActivity.this, "登陆超时");
						} else if (code==-2) {
							ToastUtil.showLongToast(AddressAddActivity.this,"收货人姓名不能为空  \n" +
									"短信验证码有效时间为3分钟（180秒）");
						}else if (code == -3) {
							ToastUtil.showLongToast(AddressAddActivity.this,"收货人手机不能为空");
						}else if (code == -4) {
							ToastUtil.showLongToast(AddressAddActivity.this,"收获地址不能为空");
						}else if (code == -5) {
							ToastUtil.showLongToast(AddressAddActivity.this,"请选择收货所在地");
						}else if (code == -6) {
							ToastUtil.showLongToast(AddressAddActivity.this,"收货信息修改失败");
						}else if (code == -7) {
							ToastUtil.showLongToast(AddressAddActivity.this,"非法操作");
						}


					}
				});
	}


	private void setAddressDefaultPost(String addressID) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddressAddActivity.this, timestamp, "Member", "setAddress");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MEMBER_SETADDRESS)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddressAddActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddressAddActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(AddressAddActivity.this))
				.addParams("id",addressID)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "list221231===", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code == 0) {

							ToastUtil.showLongToast(AddressAddActivity.this, "添加设置成功");
							Intent intent = new Intent(AddressAddActivity.this, AddressActivity.class);
							startActivity(intent);
							finish();
//							if(recordPreAddress!=null){
//								recordPreAddress.findViewById(R.id.address_sel).setVisibility(View.GONE);
//							}
//							//设置成功
//							defaultAddressView.findViewById(R.id.address_sel).setVisibility(View.VISIBLE);
//							recordPreAddress=defaultAddressView;
						} else if (code == -1) {
							ToastUtil.showLongToast(AddressAddActivity.this, "登陆超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(AddressAddActivity.this, "非法操作");
						} else if (code == -3) {
							ToastUtil.showLongToast(AddressAddActivity.this, "设置失败");
						}

					}
				});
	}




	@OnClick(R.id.add_finish) void onFinishAddClick() {
		increaseAddressPost();

	}
	@OnClick(R.id.area) void toAeraSelectClick() {
			Intent intent=new Intent();
			intent.setClass(AddressAddActivity.this,AreaActivity.class);
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
				//sel_area.setTextSize(12);
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
		productId = getIntent().getIntExtra("productId", -1);

	}












}
