package com.zhuoyi.fauction.ui.other;



import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.model.MyPicket;
import com.zhuoyi.fauction.model.MyPicketSel;
import com.zhuoyi.fauction.model.PackDo;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.adapters.SelectPackAdapter;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.Order;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.SelectPackWindow;
import com.zhuoyi.fauction.view.SelectPicPopWindow;
import com.zhuoyi.fauction.view.SelectPicketWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
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
public class OrderSubmitActivity extends BaseActivity {

	@Bind(R.id.receipt_una) TextView receiptUna;
	@Bind(R.id.receipt_phone) TextView receiptPhone;
	@Bind(R.id.receipt_address) TextView receiptAddress;
	@Bind(R.id.pic) ImageView pic;
	@Bind(R.id.title) TextView title;
	@Bind(R.id.cj_price) TextView cjPrice;
	@Bind(R.id.cj_num) TextView cjNum;
	@Bind(R.id.cj_all_price) TextView cjAllPrice;
	@Bind(R.id.yongjing) TextView yongjing;
	@Bind(R.id.baozhengjing) TextView baozhengjing;
	@Bind(R.id.commitionTx) TextView commitionTx;
	/*@Bind(R.id.pack)
	RadioGroup pack;
	@Bind(R.id.pack_zx)
	RadioButton packZx;
	@Bind(R.id.pack_dz) RadioButton packDz;*/
//	@Bind(R.id.deliver) RadioGroup deliver;
//	@Bind(R.id.delivery_wl) RadioButton deliveryWl;
//	@Bind(R.id.express) TextView express;
//	@Bind(R.id.deliver_zq) RadioButton deliverZq;
//	@Bind(R.id.insurance_a) RadioGroup insuranceA;
//	@Bind(R.id.insurance_r) RadioButton insuranceR;
//	@Bind(R.id.insurance) TextView insurance;
	@Bind(R.id.total) TextView total;
    @Bind(R.id.to_add_address_parent)
    RelativeLayout to_add_address_parent;
    @Bind(R.id.to_add_address) TextView to_add_address;
    @Bind(R.id.have_address) RelativeLayout have_address;

	@Bind(R.id.select_pack) RelativeLayout select_pack;
	@Bind(R.id.pack_type) TextView pack_type;

	@Bind(R.id.select_picket) RelativeLayout select_picket;
	@Bind(R.id.packet_num) TextView packet_num;
	//
	String idx;
	String packinx;
	String deliveryx;
	String expressx;
	String commissionx;
	String insurancex;
	String receipt_idx;
	String total_pricex;
	String keyx;
	double totalx;
	String orde_num;
	String bondx;

	List<MyPicketSel.DataBean> pickets=new ArrayList<MyPicketSel.DataBean>();;

	SelectPackWindow selectPackWindow;

	SelectPicketWindow selectPicketWindow;

	boolean hasAddress;

	List<Area> list;

	int proId;

	double wl;
	double yf;

	boolean isCanUseVoucher=true;

	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_order_submit);
		ButterKnife.bind(this);
		list=initJsonData();
		initView();
		Intent intent = getIntent();
		proId=intent.getIntExtra("productId", 0);
		Logger.i(TAG+"proid==",proId+"");
		Common.proId=proId;
		//Common.total=0.00;
		Common.picketSel=null;
		orderOrderInfoPost(proId);

       /* pack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                //获取变更后的选中项的ID
                int radioButtonId = group.getCheckedRadioButtonId();
                if(radioButtonId==R.id.pack_zx){
                    packinx="1";

                }else if(radioButtonId==R.id.pack_dz){
                    packinx="2";

                }
            }
        });*/

//        deliver.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//			@Override
//			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				//获取变更后的选中项的ID
//				int radioButtonId = group.getCheckedRadioButtonId();
//				if (radioButtonId == R.id.delivery_wl) {
//
//					deliveryx = "1";
//				} else if (radioButtonId == R.id.deliver_zq) {
//
//					deliveryx = "2";
//				}
//			}
//		});


    }


	@Override
	protected void onStart() {
		//Common.total=0.00;
		Common.picketSel=null;
		orderOrderInfoPost(proId);
		super.onStart();
	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		Common.isOrderEqulsZero=false;
		if(Common.whichActivity==1){
			Intent intent=new Intent();
			intent.putExtra("tab",2);
			Common.whichActivity=1;
			intent.setClass(OrderSubmitActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}else{
			onBackPressed();
		}
	}

	private View.OnClickListener itemsOnClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()){



			}
		}
	};

	@OnClick(R.id.select_pack) void onSelectPack() {
		//
			selectPackWindow=new SelectPackWindow(OrderSubmitActivity.this,itemsOnClick);
			selectPackWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			//显示的位置
			selectPackWindow.showAtLocation(OrderSubmitActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			backgroundAlpha(0.5f);
			selectPackWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					backgroundAlpha(1f);
				}
			});


	}

	@OnClick(R.id.select_picket) void onSelectPicket() {
		//
		if(isCanUseVoucher) {
			selectPicketWindow=new SelectPicketWindow(OrderSubmitActivity.this,itemsOnClick,pickets,totalx);
			selectPicketWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			//显示的位置
			selectPicketWindow.showAtLocation(OrderSubmitActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			backgroundAlpha(0.5f);
			selectPicketWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					backgroundAlpha(1f);
				}
			});
		}

	}

	private void getPicketsPost(double price) {

		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OrderSubmitActivity.this, timestamp, "Voucher", "useVoucher");




		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.VOUCHER_USEVOUCHER)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderSubmitActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OrderSubmitActivity.this))
				.addParams("type","2")
				.addParams("price",price+"")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i("OrderSubmitActivity picket=", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){
							Gson gson = new Gson();
							MyPicketSel myPicketSel = gson.fromJson(response, MyPicketSel.class);
							if(myPicketSel.getData()==null){
								pickets=null;

							}else{
								pickets=myPicketSel.getData();
								packet_num.setText(myPicketSel.getNum()+"张可用");
								if(myPicketSel.getNum()<=0){
									isCanUseVoucher=false;
								}
							}


						}else if(code==-1){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "没有登录");
						}else if(code==-2){
							//ToastUtil.showLongToast(OrderSubmitActivity.this, "没有数据");
							isCanUseVoucher=false;
						}
						else if(code==-3){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "支付总金额不能为空");
						}






					}
				});
	}

	/**
	 * 设置添加屏幕的背景透明度
	 * @param bgAlpha
	 */
	public void backgroundAlpha(float bgAlpha)
	{
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = bgAlpha; //0.0-1.0
		getWindow().setAttributes(lp);
	}


		//生成订单接口
	private void orderOrderInfoPost(int productId) {
		String timestamp= DateUtil.getStringDate();

		String sign = Util.createSign(OrderSubmitActivity.this, timestamp, "Order", "orderInfo");
        Logger.i(TAG + "id", productId+"");

		// Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.ORDER_ORDERINFO)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderSubmitActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(OrderSubmitActivity.this))
				.addParams("id",String.valueOf(productId))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {
					}


					@Override
					public void onResponse(String response) {
						Logger.i(TAG + "orider", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){
							Gson gson = new Gson();
							Order order = gson.fromJson(response, Order.class);
							if (order.getCode() == 0) {
								Order.DataBean data = order.getData();
								//receiptUna.setText("xxx");
								title.setText(data.getTitle());
								cjPrice.setText("￥" + data.getPrice() + "/" + data.getUnit());
								cjNum.setText(data.getNum() + data.getUnit());
								cjAllPrice.setText("￥" + data.getTotal_price());
								double commission = Double.parseDouble(data.getCommission());
								//判断佣金是否小于等于0
								if(commission<=0){
									String string="佣金";
									SpannableString sp = new SpannableString(string);
									sp.setSpan(new StrikethroughSpan(), 0, string.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									commitionTx.setText(sp);
									yongjing.setText("推广期间，佣金全免");

								}else{
									yongjing.setText("￥" + data.getCommission());
								}

								baozhengjing.setText("￥" + data.getBond());
								String pack = data.getPack();
								/*packZx.setText(data.getPacking().getValue1());
								packDz.setText(data.getPacking().getValue2());
								packZx.setChecked(true);
								if (pack.equals("0")) {

								} else if (pack.equals("1")) {
									packinx="1";
									packZx.setChecked(true);
								} else if (pack.equals("2")) {
									packDz.setChecked(true);
									packinx="2";

								}*/
								String delivery = data.getDelivery();
//								deliveryWl.setChecked(true);
//								if (delivery.equals("0")) {
//
//								} else if (delivery.equals("1")) {
//									deliveryWl.setChecked(true);
//									deliveryx="1";
//									wl=Double.parseDouble(expressx);
//								} else if (delivery.equals("2")) {
//									deliverZq.setChecked(true);
//									deliveryx="2";
//									wl=0;
//								}
//								total.setText("￥"+data.getTotal_price());
//								express.setText("￥" + data.getExpress());
//								insurance.setText("￥" + data.getInsurance());
//								insuranceR.setChecked(true);
								Picasso.with(OrderSubmitActivity.this).load(data.getPic()).into(pic);
								if(data.getReceipt().getAddress()==null){
									hasAddress=false;
									//收货地址为空显示添加地址按钮
									to_add_address_parent.setVisibility(View.VISIBLE);

									to_add_address.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//跳转添加收货地址
											Common.whichActivity=3;
											Intent intent=new Intent(OrderSubmitActivity.this,AddressAddActivity.class);
											intent.putExtra("productId", proId);
											startActivity(intent);
											//finish();
										}
									});
								}else{
									hasAddress=true;
									have_address.setVisibility(View.VISIBLE);
									to_add_address_parent.setVisibility(View.GONE);

									//receiptAddress.setText(data.getReceipt().getAddress());
									int provinceId =data.getReceipt().getProvince();
									Logger.i("provinceId=",provinceId+"");
									int cityId = data.getReceipt().getCity();
									String areaId = data.getReceipt().getArea();
									StringBuilder sb = new StringBuilder();
									Logger.i("list size",list.size()+"");
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
									Logger.i("address=",sb.toString());
									receiptAddress.setText(sb.toString() + data.getReceipt().getAddress());


									receiptUna.setText(data.getReceipt().getUname());
									receiptPhone.setText(data.getReceipt().getMobile());
									have_address.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//跳转添加收货地址
											Common.whichActivity=3;
											Intent intent=new Intent(OrderSubmitActivity.this,AddressActivity.class);
											startActivity(intent);
										}
									});
								}

								idx=data.getId();
								expressx=String.valueOf(data.getExpress());
								commissionx=data.getCommission();

								insurancex=String.valueOf(data.getInsurance());
								receipt_idx=data.getReceipt().getId();
								// totalx=data.getTotal_price();
								orde_num=data.getOrder_num();
								total_pricex=data.getTotal_price();
								bondx=data.getBond();
								//
								//totalx=Double.parseDouble(total_pricex)+Double.parseDouble(commissionx)+wl+Double.parseDouble(insurancex)-Double.parseDouble(bondx);
								totalx=Double.parseDouble(total_pricex)+Double.parseDouble(commissionx)-Double.parseDouble(bondx);
								DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
								String p=decimalFormat.format(totalx);
								totalx=Double.parseDouble(p);
								//Common.total=totalx;
								total.setText("￥"+p);
								getPicketsPost(totalx);
								//
//								deliveryWl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//									@Override
//									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//										if(isChecked){
//
//											totalx=totalx+Double.parseDouble(expressx);
//											DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//											String p1=decimalFormat.format(totalx);
//											total.setText("￥" + p1);
//										}else{
//											totalx=totalx-Double.parseDouble(expressx);
//											DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//											String p2=decimalFormat.format(totalx);
//											total.setText("￥" + p2);
//										}
//									}
//								});
//								insuranceR.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//									@Override
//									public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//										if (isChecked) {
//											totalx = totalx + Double.parseDouble(insurancex);
//											DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//											String p3=decimalFormat.format(totalx);
//											total.setText("￥" + p3);
//										} else {
//											totalx = totalx - Double.parseDouble(insurancex);
//											DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
//											String p4=decimalFormat.format(totalx);
//											total.setText("￥" + p4);
//										}
//									}
//								});
						}

						}


					}
				});
	}

	//0元接口
	private void wallPayOrderZeroPost(final String oid,double price,String num) {
		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		final String priceP=decimalFormat.format(price);
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(getApplicationContext(),timestamp,"Wallet","payOrder");
		Logger.i("wallPayOrderZeroPost","price="+priceP+"---oid="+oid+"---voucher_num="+num);

		OkHttpUtils.post()
				.url(Constant.WALLET_PAYORDER)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
				.addParams("timestamp",timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.addParams("price",priceP)
				.addParams("oid",orde_num)
				.addParams("voucher_num",num)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i("WALLET_PAYORDER", response);
						Gson gson=new Gson();
						Ret ret=gson.fromJson(response,Ret.class);
						if(ret.getCode()==0){
							/*Intent intent=new Intent();
							intent.setClass(OrderSubmitActivity.this,MainPayActivity.class);
							Logger.i("oid订单号=", orde_num);
							intent.putExtra("oid", orde_num);
							intent.putExtra("total",priceP+"");
							startActivity(intent);*/
							ToastUtil.showLongToast(OrderSubmitActivity.this, "支付成功");

						}else if(ret.getCode()==-1){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "登陆超时");
						}else if(ret.getCode()==-2){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "订单金额不能为空");
						}else if(ret.getCode()==-3){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "订单号不能为空");
						}else if(ret.getCode()==-4){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "系统忙");
						}else if(ret.getCode()==-5){
							ToastUtil.showLongToast(OrderSubmitActivity.this, "钱包余额不足");
						}
						Intent intent=new Intent(OrderSubmitActivity.this,PayStateActivity.class);
						intent.putExtra("oid", orde_num);
						intent.putExtra("id", oid);
						startActivity(intent);
						Common.isOrderEqulsZero=false;
						finish();

					}
				});
	}

	//提交订单接口
	private void submitOrderPost() {

		DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
		//这里判断金额调用哪个接口
		if(totalx<0){
			ToastUtil.showLongToast(OrderSubmitActivity.this,"提交订单失败");
			return;
		}
		if(Common.isOrderEqulsZero){
			wallPayOrderZeroPost(idx,totalx,Common.picketSel.getNum());
		}else{

			//判断是否选择抵用劵
			if(Common.picketSel!=null){
				String num = Common.picketSel.getNum();
				if(num!=null){
					final String totalP=decimalFormat.format(totalx);
					String timestamp= DateUtil.getStringDate();

					String sign = Util.createSign(OrderSubmitActivity.this, timestamp, "Order", "submitOrder");
					String key= MD5Util.getMD5String(idx+receipt_idx+totalP);
					OkHttpUtils.post()
							.url(Constant.ORDER_SUBMITORDER)
							.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
							.addParams("timestamp", timestamp)
							.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderSubmitActivity.this))
							.addParams("user_id",ConfigUserManager.getUserId(OrderSubmitActivity.this))
							.addParams("id",idx)
							.addParams("packing",Common.packingId)
							.addParams("delivery","2")
							.addParams("commission",commissionx)
							.addParams("receipt_id",receipt_idx)
							.addParams("total",totalP)
							.addParams("bond",bondx)
							.addParams("key",key)
							.addParams("voucher_num",num)
							.build()
							.execute(new StringCallback() {
								@Override
								public void onError(Call call, Exception e) {

								}

								@Override
								public void onResponse(String response) {
									Logger.i(TAG+"submit", response);
									//json解析
									JSONObject jsonObject = JSONObject.parseObject(response);
									int code = jsonObject.getIntValue("code");
									int voucher = jsonObject.getIntValue("voucher");
									if(voucher==0){
										ToastUtil.showLongToast(OrderSubmitActivity.this,"抵用券无法使用");
										//totalx=totalx+Double.parseDouble(Common.picketSel.getPrice());
										//Common.total=totalx;
										DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
										String p=decimalFormat.format(totalx);
										totalx=Double.parseDouble(p);
										total.setText("￥"+p);
										getPicketsPost(totalx);
									}else if(voucher==1){
										if(code==0){
											Intent intent=new Intent();
											intent.setClass(OrderSubmitActivity.this,MainPayActivity.class);
											Logger.i("oid订单号=", orde_num);
											intent.putExtra("oid", orde_num);
											intent.putExtra("id", idx);
											intent.putExtra("total",totalP+"");
											intent.putExtra("showprice",total.getText().toString());
											startActivity(intent);
										}else if(code==-1){
											ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
										}else if(code==-2){
											ToastUtil.showLongToast(OrderSubmitActivity.this,"登录超时");
										}else if(code==-3){
											ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
										}else if(code==-4){
											ToastUtil.showLongToast(OrderSubmitActivity.this,"订单信息提交失败");
										}
										//清空临时total
										//Common.total=0.00;
									}









								}
							});
				}else{
					final String totalP=decimalFormat.format(totalx);
					String timestamp= DateUtil.getStringDate();

					String sign = Util.createSign(OrderSubmitActivity.this, timestamp, "Order", "submitOrder");
					String key= MD5Util.getMD5String(idx+receipt_idx+totalP);
					OkHttpUtils.post()
							.url(Constant.ORDER_SUBMITORDER)
							.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
							.addParams("timestamp", timestamp)
							.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderSubmitActivity.this))
							.addParams("user_id",ConfigUserManager.getUserId(OrderSubmitActivity.this))
							.addParams("id",idx)
							.addParams("packing",Common.packingId)
							.addParams("delivery","2")
							.addParams("commission",commissionx)
							.addParams("receipt_id",receipt_idx)
							.addParams("total",totalP)
							.addParams("bond",bondx)
							.addParams("key",key)
							.build()
							.execute(new StringCallback() {
								@Override
								public void onError(Call call, Exception e) {

								}

								@Override
								public void onResponse(String response) {
									Logger.i(TAG+"submit", response);
									Gson gson=new Gson();
									Ret ret = gson.fromJson(response, Ret.class);
									if(ret.getCode()==0){
										Intent intent=new Intent();
										intent.setClass(OrderSubmitActivity.this,MainPayActivity.class);
										Logger.i("oid订单号=", orde_num);
										intent.putExtra("oid", orde_num);
										intent.putExtra("id", idx);
										intent.putExtra("total",totalP+"");
										intent.putExtra("showprice",total.getText().toString());
										startActivity(intent);
									}else if(ret.getCode()==-1){
										ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
									}else if(ret.getCode()==-2){
										ToastUtil.showLongToast(OrderSubmitActivity.this,"登录超时");
									}else if(ret.getCode()==-3){
										ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
									}else if(ret.getCode()==-4){
										ToastUtil.showLongToast(OrderSubmitActivity.this,"订单信息提交失败");
									}
									//清空临时total
									//Common.total=0.00;






								}
							});
				}
			}else{
				final String totalP=decimalFormat.format(totalx);
				String timestamp= DateUtil.getStringDate();

				String sign = Util.createSign(OrderSubmitActivity.this, timestamp, "Order", "submitOrder");
				String key= MD5Util.getMD5String(idx+receipt_idx+totalP);
				OkHttpUtils.post()
						.url(Constant.ORDER_SUBMITORDER)
						.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(OrderSubmitActivity.this))
						.addParams("timestamp", timestamp)
						.addParams("client_id", ConfigUserManager.getUnicodeIEME(OrderSubmitActivity.this))
						.addParams("user_id",ConfigUserManager.getUserId(OrderSubmitActivity.this))
						.addParams("id",idx)
						.addParams("packing",Common.packingId)
						.addParams("delivery","2")
						.addParams("commission",commissionx)
						.addParams("receipt_id",receipt_idx)
						.addParams("total",totalP)
						.addParams("bond",bondx)
						.addParams("key",key)
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e) {

							}

							@Override
							public void onResponse(String response) {
								Logger.i(TAG+"submit", response);
								Gson gson=new Gson();
								Ret ret = gson.fromJson(response, Ret.class);
								if(ret.getCode()==0){
									Intent intent=new Intent();
									intent.setClass(OrderSubmitActivity.this,MainPayActivity.class);
									Logger.i("oid订单号=", orde_num);
									intent.putExtra("oid", orde_num);
									intent.putExtra("id", idx);
									intent.putExtra("total",totalP+"");
									intent.putExtra("showprice",total.getText().toString());
									startActivity(intent);
								}else if(ret.getCode()==-1){
									ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
								}else if(ret.getCode()==-2){
									ToastUtil.showLongToast(OrderSubmitActivity.this,"登录超时");
								}else if(ret.getCode()==-3){
									ToastUtil.showLongToast(OrderSubmitActivity.this,"非法操作");
								}else if(ret.getCode()==-4){
									ToastUtil.showLongToast(OrderSubmitActivity.this,"订单信息提交失败");
								}
								//清空临时total
								//Common.total=0.00;






							}
						});
			}


		}

	}


	@OnClick(R.id.submit_order) void onSubmitOrderClick() {
		//提交订单 跳到付款界面
		if(hasAddress){
			submitOrderPost();
		}else {
			ToastUtil.showLongToast(OrderSubmitActivity.this,"请填写地址");
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

	private String TAG="OrderSubmitActivity";


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
