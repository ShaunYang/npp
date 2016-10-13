package com.zhuoyi.fauction.ui.other;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.PayPo;
import com.zhuoyi.fauction.model.ReminderPo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.zhuoyi.fauction.ui.MainActivity;
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
public class OffinePayStateActivity extends BaseActivity {


	@Bind(R.id.success_title) TextView successTitle;
	@Bind(R.id.state_img) ImageView stateImg;
	@Bind(R.id.state_txt) TextView stateTxt;
	@Bind(R.id.npp) TextView npp;
	@Bind(R.id.price) TextView price;
	@Bind(R.id.title) TextView title;
	@Bind(R.id.order_num) TextView orderNum;
	@Bind(R.id.pay_state) TextView payState;
	@Bind(R.id.pay_method) TextView payMethod;
	@Bind(R.id.username) TextView username;
	@Bind(R.id.phone) TextView phone;
	@Bind(R.id.address) TextView address;
	@Bind(R.id.g_name) TextView g_name;
	@Bind(R.id.g_username) TextView g_username;
	@Bind(R.id.g_lxfs) TextView g_lxfs;
	@Bind(R.id.g_lxdz) TextView g_lxdz;
/*	@Bind(R.id.gridview)
	MyGridView gridview;*/
	@Bind(R.id.back_main)
	Button backMain;
	private static final int SDK_PAY_FLAG = 1;

	List<Area> list;

	boolean flag;

	String oid;

	String supplierId;

	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.offine_pay_wait_state);
		ButterKnife.bind(this);
		list=initJsonData();
		orderOrderPayStatePost();
		initView();



    }

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		//onBackPressed();
		Intent intent=new Intent();
		intent.setClass(OffinePayStateActivity.this,MainActivity.class);
		intent.putExtra("tab",Common.home_tab);
		startActivity(intent);
		finish();


	}

	private void orderOrderPayStatePost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OffinePayStateActivity.this, timestamp, "Order", "payResult");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.ORDER_PAYRESULT)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OffinePayStateActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OffinePayStateActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OffinePayStateActivity.this))
				.addParams("order_num",getIntent().getStringExtra("oid"))

				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "pay_state", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							Gson gson = new Gson();
							PayPo payPo = gson.fromJson(response, PayPo.class);
							price.setText("￥" + payPo.getData().getTotal());
							title.setText(payPo.getData().getTitle());
							payMethod.setText(payPo.getData().getPay_type());
							payState.setText(payPo.getData().getPay_state());
							oid=payPo.getData().getId();



							orderNum.setText(payPo.getData().getOrder_num());
							username.setText(payPo.getData().getReceipt().getUname());
							phone.setText(payPo.getData().getReceipt().getMobile());
							int provinceId = payPo.getData().getReceipt().getProvince();
							int cityId = payPo.getData().getReceipt().getCity();
							String areaId = payPo.getData().getReceipt().getArea();
							StringBuilder sb = new StringBuilder();
							for (Area area : list) {
								if (area.getProvince().getId().equals(provinceId)) {
									Area.ProvinceBean province = area.getProvince();
									sb.append(province.getName());
									List<Area.ProvinceBean.CityBean> citys = province.getCity();
									for (Area.ProvinceBean.CityBean city : citys) {
										if (city.getId().equals(cityId)) {
											sb.append(city.getName());
											List<Area.ProvinceBean.CityBean.AreaBean> areas = city.getArea();
											for (Area.ProvinceBean.CityBean.AreaBean areaBean : areas) {
												if (areaBean.getId().equals(areaId)) {
													sb.append(areaBean.getName());
												}

											}
										}
									}
								}
							}
							address.setText(sb.toString() + payPo.getData().getReceipt().getAddress());
							supplierId=payPo.getData().getSupplier_id();
							if(supplierId!=null){
								coGetGysInfoPost(supplierId);
							}

							//recommondProductPost(payPo.getData().getType());
						}

					}
				});
	}


	//获取线下支付供应商信息
	private void coGetGysInfoPost(String id) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OffinePayStateActivity.this, timestamp, "Co", "getGysInfo");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.CO_GETGYSINFO)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OffinePayStateActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OffinePayStateActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OffinePayStateActivity.this))
				.addParams("id",id)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "offinepay_state", response);
						JSONObject jsonObject=JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){
							JSONObject data = jsonObject.getJSONObject("data");
							if(data!=null){
								g_name.setText("供应商名称:"+data.getString("title"));
								g_username.setText("姓名:"+data.getString("uname"));
								g_lxfs.setText("联系方式:"+data.getString("mobile"));
								g_lxdz.setText("地址:"+data.getString("addr"));
							}


						}else if(code==-2){
							ToastUtil.showLongToast(OffinePayStateActivity.this,"没有找到数据");
						}else if(code==-1){
							ToastUtil.showLongToast(OffinePayStateActivity.this,"非法操作");
						}

					}
				});
	}


	//推荐商品
	private void recommondProductPost(String type) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OffinePayStateActivity.this, timestamp, "Order", "recommend");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.ORDER_RECOMMEND)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OffinePayStateActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OffinePayStateActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OffinePayStateActivity.this))
				.addParams("idd",Common.proId+"")
				.addParams("id",type)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "pay_state", response);
						Gson gson = new Gson();
						ReminderPo reminderPo=gson.fromJson(response,ReminderPo.class);
						if(reminderPo.getCode()==0){
							/*gridview.setAdapter(new MyGridAdapter(OffinePayStateActivity.this,reminderPo.getData()));
							gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

								}
							});*/
						}

					}
				});
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

	@OnClick(R.id.back_main) void onBackMainClick() {
		Intent intent=new Intent(OffinePayStateActivity.this, MainActivity.class);
		Common.home_tab=3;
		startActivity(intent);
		finish();

		/*if(flag){
			//首页
			Intent intent=new Intent(OffinePayStateActivity.this, MainActivity.class);
			startActivity(intent);
		}else{
			//重新支付
			finish();
		}*/
	}


	/*@OnClick(R.id.order_detail) void onOrderDetailClick() {
		//跳到订单详情
		Intent intent=new Intent();
		intent.setClass(OffinePayStateActivity.this,OrderDetailActivity.class);
		intent.putExtra("id",oid);
		startActivity(intent);
		finish();
	}*/




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

	private String TAG="PayStateActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {


	}














}
