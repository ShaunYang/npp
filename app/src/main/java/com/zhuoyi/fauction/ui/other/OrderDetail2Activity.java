package com.zhuoyi.fauction.ui.other;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yintai.app_common.ui.common.view.CommonSimpleTitleDialog;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.OrderDetail;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.DialogUtil;
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


public class OrderDetail2Activity extends BaseActivity {
	@Bind(R.id.username) TextView username;
	@Bind(R.id.phone) TextView phone;
	@Bind(R.id.address) TextView address;
	@Bind(R.id.title) TextView title;
	@Bind(R.id.cj_price) TextView cjPrice;
	@Bind(R.id.cj_num) TextView cjNum;
	@Bind(R.id.cj_all_price) TextView cjAllPrice;
	/*@Bind(R.id.express) TextView express;*/
	/*@Bind(R.id.insurance) TextView insurance;*/
	@Bind(R.id.commission) TextView commission;
	@Bind(R.id.bond) TextView bond;
	@Bind(R.id.total) TextView total;
	@Bind(R.id.order_num) TextView orderNum;
	@Bind(R.id.title_img)
	ImageView titleImg;
	@Bind(R.id.top_title) TextView topTitle;
	@Bind(R.id.to_pay)
	Button toPay;
	@Bind(R.id.packing) TextView packing;


	@Bind(R.id.pay_time_blank)
	RelativeLayout payTimeBlank;
	@Bind(R.id.pay_time) TextView payTime;
	@Bind(R.id.send_time_blank) RelativeLayout sendTimeBlank;
	@Bind(R.id.send_time) TextView sendTime;
	@Bind(R.id.order_time_blank) RelativeLayout orderTimeBlank;
	@Bind(R.id.order_pay_type) RelativeLayout order_pay_type;
	@Bind(R.id.order_time) TextView orderTime;
	@Bind(R.id.diyong) TextView diyong;
	@Bind(R.id.pay_type) TextView pay_type;


	List<Area> list;

	String id;


	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_order_detail);
		ButterKnife.bind(this);
		list=initJsonData();
		initView();
		Intent intent = getIntent();
		id=intent.getStringExtra("id");
		orderOrderInfoPost(id);
	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		Intent intent=new Intent();
		intent.setClass(OrderDetail2Activity.this,MainActivity.class);
		intent.putExtra("tab",3);
		startActivity(intent);
		finish();
	}
		//
	private void orderOrderInfoPost(final String id) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OrderDetail2Activity.this, timestamp, "Order", "viewOrder");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.ORDER_VIEWORDER)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderDetail2Activity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OrderDetail2Activity.this))
				.addParams("id",id+"")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG+"order_detail=", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							Gson gson=new Gson();
							final OrderDetail orderDetail = gson.fromJson(response, OrderDetail.class);
							title.setText(orderDetail.getData().getTitle());
							cjPrice.setText("￥"+orderDetail.getData().getPrice());
							cjNum.setText(orderDetail.getData().getNum()+"/"+orderDetail.getData().getUnit());
							cjAllPrice.setText("￥"+orderDetail.getData().getTotal_price());
							//express.setText("￥"+orderDetail.getData().getExpress());
							commission.setText("￥"+orderDetail.getData().getCommission());
							//insurance.setText("￥"+orderDetail.getData().getInsurance());
							bond.setText("￥"+orderDetail.getData().getBond());
							orderNum.setText(orderDetail.getData().getOrder_num());
							packing.setText(orderDetail.getData().getPacking());
							//curState.setText(orderDetail.getData().getState());
							diyong.setText("￥"+orderDetail.getData().getVoucher_price());
							total.setText("￥" + orderDetail.getData().getTotal());
							Picasso.with(OrderDetail2Activity.this).load(orderDetail.getData().getPic()).resize(titleImg.getWidth(), titleImg.getHeight()).into(titleImg);
							String status = orderDetail.getData().getStatus();
							if(status.equals("0")){
								//未付款
								topTitle.setText("你还未付款，请立即付款");
								toPay.setVisibility(View.VISIBLE);
								orderTimeBlank.setVisibility(View.VISIBLE);
								orderTime.setText(orderDetail.getData().getAdd_time());
								toPay.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(OrderDetail2Activity.this, OrderSubmitActivity.class);
										intent.putExtra("productId",Integer.parseInt(orderDetail.getData().getAid()));
										startActivity(intent);
									}
								});
							}else if(status.equals("1")){
								//等待发货
								payTimeBlank.setVisibility(View.VISIBLE);
								payTime.setText(orderDetail.getData().getPay_time());
							}else if(status.equals("2")){
								//待收货
								if(orderDetail.getData().getLogistics()!=null){
									topTitle.setText("供应商已发货，物流信息："+orderDetail.getData().getLogistics());
								}else{
									topTitle.setText("供应商已发货，物流信息：");
								}
								payTimeBlank.setVisibility(View.VISIBLE);
								payTime.setText(orderDetail.getData().getPay_time());
								sendTimeBlank.setVisibility(View.VISIBLE);
								sendTime.setText(orderDetail.getData().getDeliver_time());
								order_pay_type.setVisibility(View.VISIBLE);
								pay_type.setText(orderDetail.getData().getPay_type_cn());

								toPay.setVisibility(View.VISIBLE);
								toPay.setText("确认收货");
								toPay.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										DialogUtil.showCustomerSimpleTitleDialog(OrderDetail2Activity.this, true, "是否确定收货？", "确认", "取消", new CommonSimpleTitleDialog.OnSubmitClickListener() {
											@Override
											public void onSubmit(Dialog dialog) {

												String timestamp = DateUtil.getStringDate();

												String sign = Util.createSign(OrderDetail2Activity.this, timestamp, "Order", "confirmReceipt");

												OkHttpUtils.post()
														.url(Constant.ORDER_CONFIRMRECEIPT)
														.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderDetail2Activity.this))
														.addParams("timestamp", timestamp)
														.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderDetail2Activity.this))
														.addParams("user_id", ConfigUserManager.getUserId(OrderDetail2Activity.this))
														.addParams("id", id)
														.build()
														.execute(new StringCallback() {
															@Override
															public void onError(Call call, Exception e) {

															}

															@Override
															public void onResponse(String response) {

																Logger.i("JDItemAdapter" + "45646456456====", response);
																JSONObject jsonObject = JSONObject.parseObject(response);
																int code = jsonObject.getIntValue("code");
																if (code == 0) {
																	//成功
																	DialogUtil.commonDialogDismiss();
																	toPay.setVisibility(View.GONE);
																	//ToastUtil.showLongToast(mContext, "成功");
																	//跳转订单详情
																	Intent intent = new Intent(OrderDetail2Activity.this, OrderDetail2Activity.class);
																	//intent.putExtra("productId", Integer.parseInt(id));
																	intent.putExtra("id",id);
																	startActivity(intent);
																	finish();

																	//orderOrderInfoPost(id);


																} else if (code == -1) {
																	ToastUtil.showLongToast(OrderDetail2Activity.this, "登陆超时");
																	DialogUtil.dismiss();
																} else if (code == -2) {
																	ToastUtil.showLongToast(OrderDetail2Activity.this, "提交失败");
																	DialogUtil.dismiss();
																}


															}
														});
											}
										}, new CommonSimpleTitleDialog.OnNegativeClickListener() {
											@Override
											public void onNegative(Dialog dialog) {

											}
										});
									}
								});

							}else if(status.equals("3")){
								//交易完成
								if(orderDetail.getData().getLogistics()!=null){
									topTitle.setText("交易完成，物流信息："+orderDetail.getData().getLogistics());
								}else{
									topTitle.setText("交易完成，物流信息：");
								}
								payTimeBlank.setVisibility(View.VISIBLE);
								payTime.setText(orderDetail.getData().getPay_time());
								sendTimeBlank.setVisibility(View.VISIBLE);
								sendTime.setText(orderDetail.getData().getDeliver_time());

							}else if(status.equals("4")){
								//交易关闭
								topTitle.setText("因你超时未付款，交易已关闭");
								orderTimeBlank.setVisibility(View.VISIBLE);
								orderTime.setText(orderDetail.getData().getAdd_time());
							}

							int provinceId = orderDetail.getData().getReceipt().getProvince();
							int cityId = orderDetail.getData().getReceipt().getCity();
							String areaId = orderDetail.getData().getReceipt().getArea();
							StringBuilder sb = new StringBuilder();
							for (Area area : list) {
								String pid = area.getProvince().getId();
								if (pid.equals(provinceId + "")) {
									Area.ProvinceBean province = area.getProvince();
									Logger.i("province.getName()=",province.getName()+"");
									sb.append(province.getName());
									List<Area.ProvinceBean.CityBean> citys = province.getCity();
									for (Area.ProvinceBean.CityBean city : citys) {
										if (city.getId().equals(cityId+"")) {
											sb.append(city.getName());
											List<Area.ProvinceBean.CityBean.AreaBean> areas = city.getArea();
											for (Area.ProvinceBean.CityBean.AreaBean areaBean : areas) {
												if (areaBean.getId().equals(areaId+"")) {
													sb.append(areaBean.getName());
												}

											}
										}
									}
								}
							}
							address.setText(sb.toString() + orderDetail.getData().getReceipt().getAddress());

							orderNum.setText(orderDetail.getData().getOrder_num());
							username.setText(orderDetail.getData().getReceipt().getUname());
							phone.setText(orderDetail.getData().getReceipt().getMobile());
						}


					}
				});
	}



	@OnClick(R.id.to_pay) void onToPayClick() {
		//TODO implement
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

	private String TAG="GuideActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {


	}




	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private List<Area> initJsonData()
	{
		List<Area> areas= null;
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("area.json");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len, "gbk"));
			}
			is.close();
			Gson gson=new Gson();
			areas=gson.fromJson(sb.toString(),new TypeToken<List<Area>>() {
			}.getType());

		} catch (IOException e)
		{
			e.printStackTrace();

		}
		return areas;

	}








}
