package com.zhuoyi.fauction.ui.other;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.model.RecommondPo;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.model.Order;
import com.zhuoyi.fauction.model.PayPo;
import com.zhuoyi.fauction.model.ReminderPo;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.ui.other.adapter.MyGridAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
public class PayStateActivity extends BaseActivity {


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
	@Bind(R.id.gridview)
	MyGridView gridview;
	@Bind(R.id.back_main)
	Button backMain;
	private static final int SDK_PAY_FLAG = 1;

	List<Area> list;

	boolean flag;

	String oid;

	ArrayList<RecommondPo> fauctionDos=new ArrayList<RecommondPo>();;

	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.order_pay_state);
		ButterKnife.bind(this);
		list=initJsonData();
		orderOrderPayStatePost();
		initView();



    }

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		//onBackPressed();
		Intent intent=new Intent();
		intent.setClass(PayStateActivity.this,MainActivity.class);
		intent.putExtra("tab",3);
		startActivity(intent);
		finish();


	}

	private void orderOrderPayStatePost() {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(PayStateActivity.this, timestamp, "Order", "payResult");

		String order_num=getIntent().getStringExtra("oid");
		 Logger.i("PayStateActivity","oid---="+order_num);

		OkHttpUtils.post()
				.url(Constant.ORDER_PAYRESULT)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(PayStateActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(PayStateActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(PayStateActivity.this))
				.addParams("order_num",order_num)
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
							String state=payPo.getData().getPay_state();
							if(state.equals("支付成功")){
								stateTxt.setText("订单支付成功");
								stateImg.setBackgroundResource(R.drawable.pay_success);
								backMain.setText("返回首页");
								flag=true;

							}else{
								stateTxt.setText("订单支付失败");
								stateImg.setBackgroundResource(R.drawable.pay_faile);
								backMain.setText("重新支付");
								flag=false;
							}

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
							recommondProductPost(payPo.getData().getType());
						}

					}
				});
	}

	//推荐商品
	private void recommondProductPost(String type) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(PayStateActivity.this, timestamp, "Order", "recommend");


		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.ORDER_RECOMMEND)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(PayStateActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(PayStateActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(PayStateActivity.this))
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
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							JSONObject data = jsonObject.getJSONObject("data");

							for (int i = 0; i < 6; i++) {
								int j = i + 1;
								JSONObject temp = data.getJSONObject(j + "");
								if (temp == null) {
									break;
								}

								Gson gson = new Gson();
								String s = JSON.toJSONString(temp);
								RecommondPo myHaveFauction = gson.fromJson(s, RecommondPo.class);
								fauctionDos.add(myHaveFauction);
							}
							gridview.setAdapter(new MyGridAdapter(PayStateActivity.this,fauctionDos));
							gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

								}
							});
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
		if(flag){
			//首页
			Intent intent=new Intent(PayStateActivity.this, MainActivity.class);
			Common.home_tab=3;
			startActivity(intent);
			finish();
		}else{
			//重新支付
			finish();
		}
	}


	@OnClick(R.id.order_detail) void onOrderDetailClick() {
		//跳到订单详情
		Intent intent=new Intent();
		intent.setClass(PayStateActivity.this,OrderDetailActivity.class);
		intent.putExtra("id",getIntent().getStringExtra("id"));
		startActivity(intent);
		finish();
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

	private String TAG="PayStateActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {


	}














}
