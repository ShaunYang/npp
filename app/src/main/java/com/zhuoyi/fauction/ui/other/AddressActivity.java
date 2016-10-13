package com.zhuoyi.fauction.ui.other;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyAddress;
import com.zhuoyi.fauction.model.Order;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.adapter.AddressListAdapter;
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
public class AddressActivity extends BaseActivity {

	@Bind(R.id.back) ImageView back;
	@Bind(R.id.edit) TextView edit;
	@Bind(R.id.address_list)
	ListView addressList;

	View defaultAddressView;

	View recordPreAddress;



	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.address_null);
		ButterKnife.bind(this);

		initView();
		getListAddressPost();

	}

	@OnClick(R.id.add_address) void onAddAddressClick() {
		Intent intent=new Intent(AddressActivity.this,AddressAddActivity.class);
		startActivity(intent);
		finish();

	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
	}



	//地址列表接口
	private void getListAddressPost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddressActivity.this, timestamp, "Member", "getListData");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MEMBER_GETLISTDATA)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddressActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddressActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(AddressActivity.this))

				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "list221231===", response);
						//显示列表
						Gson gson = new Gson();
						final MyAddress myAddress = gson.fromJson(response, MyAddress.class);
						addressList.setAdapter(new AddressListAdapter(AddressActivity.this, myAddress.getData(), addressList));
						addressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
								final View mView=view;
								final String[] items = getResources().getStringArray(
										R.array.addressitem);
								new AlertDialog.Builder(AddressActivity.this)
										.setTitle("请点击选择")
										.setItems(items, new DialogInterface.OnClickListener() {
											@Override
											public void onClick(DialogInterface dialog, int which) {
												if (which == 0) {
													//设置为默认地址
													defaultAddressView=mView;

													setAddressDefaultPost(myAddress.getData().get(position).getId());
												} else if (which == 1) {
													//修改地址
													Intent intent = new Intent(AddressActivity.this, AddressAlertActivity.class);
													intent.putExtra("address_id", myAddress.getData().get(position).getId());
													startActivity(intent);
													finish();
												}
											}
										})
										.show();
							}
						});

					}
				});
	}


	private void setAddressDefaultPost(String addressID) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(AddressActivity.this, timestamp, "Member", "setAddress");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MEMBER_SETADDRESS)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AddressActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(AddressActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(AddressActivity.this))
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
							if(recordPreAddress!=null){
								recordPreAddress.findViewById(R.id.address_sel).setVisibility(View.GONE);
							}
							//设置成功
							defaultAddressView.findViewById(R.id.address_sel).setVisibility(View.VISIBLE);
							recordPreAddress=defaultAddressView;
							if(Common.whichActivity==3){
								//跳回订单提交页面
								onBackPressed();
							}
						} else if (code == -1) {
							ToastUtil.showLongToast(AddressActivity.this, "登陆超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(AddressActivity.this, "非法操作");
						} else if (code == -3) {
							ToastUtil.showLongToast(AddressActivity.this, "设置失败");
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


	}












}
