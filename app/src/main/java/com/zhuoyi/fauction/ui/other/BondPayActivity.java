package com.zhuoyi.fauction.ui.other;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.unionpay.UPPayAssistEx;
import com.zhuoyi.fauction.model.MyPicketSel;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.GuideGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.SelectPicketBondWindow;
import com.zhuoyi.fauction.view.SelectPicketWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import demo.PayDemoActivity;
import demo.PayResult;
import okhttp3.Call;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class BondPayActivity extends BaseActivity {

	@Bind(R.id.bond)
	TextView bond;
	@Bind(R.id.alipy_pay) ImageView alipyPay;

	@Bind(R.id.yuer) TextView yuer;
//	@Bind(R.id.wechat_pay) ImageView wechatPay;
//	@Bind(R.id.bank_pay)
	ImageView bankPay;
	String mbond;
	int proId;
	private static final int SDK_PAY_FLAG = 1;
	String order_num;
	int payMethod=1;//1支付宝 2网银 3钱包支付

	@Bind(R.id.paymoney) Button paymoney;
	@Bind(R.id.zfb)
	RelativeLayout zfb;
	@Bind(R.id.bank) RelativeLayout bank;

	@Bind(R.id.alipy_pay_arror) ImageView alipy_pay_arror;
	@Bind(R.id.bank_pay_arror) ImageView bank_pay_arror;
	@Bind(R.id.wallet_pay_arror) ImageView wallet_pay_arror;

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private final String mMode = "00";
	List<MyPicketSel.DataBean> pickets=new ArrayList<MyPicketSel.DataBean>();
	SelectPicketBondWindow selectPicketWindow;

	@Bind(R.id.select_picket) RelativeLayout select_picket;
	@Bind(R.id.packet_num) TextView packet_num;

	double totalx;

	String oid;

	boolean isCanUseVoucher=true;

	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.fragment_paybaomoney);
		ButterKnife.bind(this);

		initView();
	}

	@OnClick(R.id.back) void onBackClick() {
		Intent intent=new Intent();
		intent.setClass(BondPayActivity.this,ProductFauctionDetailDoingActivity.class);
		intent.putExtra("productId",proId);
		startActivity(intent);
		Common.isBondEqulsZero=false;
		finish();
	}

	private View.OnClickListener itemsOnClick=new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			switch (v.getId()){



			}
		}
	};

	@OnClick(R.id.select_picket) void onSelectPicket() {
		//
		if(isCanUseVoucher){
			selectPicketWindow=new SelectPicketBondWindow(BondPayActivity.this,itemsOnClick,pickets,totalx,oid);
			selectPicketWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
			//显示的位置
			selectPicketWindow.showAtLocation(BondPayActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
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

		String sign = Util.createSign(BondPayActivity.this, timestamp, "Voucher", "useVoucher");

		OkHttpUtils.post()
				.url(Constant.VOUCHER_USEVOUCHER)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(BondPayActivity.this))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(BondPayActivity.this))
				.addParams("user_id",ConfigUserManager.getUserId(BondPayActivity.this))
				.addParams("type","1")
				.addParams("price",price+"")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i("BondPayActivity picket=", response);
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
							ToastUtil.showLongToast(BondPayActivity.this, "没有登录");
						}else if(code==-2){
							//ToastUtil.showLongToast(BondPayActivity.this, "");
							isCanUseVoucher=false;
						}
						else if(code==-3){
							ToastUtil.showLongToast(BondPayActivity.this, "支付总金额不能为空");
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

	@OnClick(R.id.zfb) void zfbMethod() {
		payMethod=1;
		alipy_pay_arror.setVisibility(View.VISIBLE);
		bank_pay_arror.setVisibility(View.GONE);
		wallet_pay_arror.setVisibility(View.GONE);

	}

	@OnClick(R.id.bank) void bankMethod() {
		payMethod=2;
		alipy_pay_arror.setVisibility(View.GONE);
		bank_pay_arror.setVisibility(View.VISIBLE);
		wallet_pay_arror.setVisibility(View.GONE);
	}

	@OnClick(R.id.wallet) void walletMethod() {
		payMethod=3;
		alipy_pay_arror.setVisibility(View.GONE);
		bank_pay_arror.setVisibility(View.GONE);
		wallet_pay_arror.setVisibility(View.VISIBLE);
	}


	private void walletIndetPost() {
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(BondPayActivity.this, timestamp, "Wallet", "index");

		OkHttpUtils.post()
				.url(Constant.WALLET_INDEX)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(BondPayActivity.this))
				.addParams("timestamp",timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(BondPayActivity.this))
				.addParams("user_id", ConfigUserManager.getUserId(BondPayActivity.this))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {

						Logger.i(TAG + "333333333333=", response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code == 0) {
							JSONObject data = jsonObject.getJSONObject("data");

							yuer.setText("钱包可用余额: ￥" + data.getString("wallet"));
							String canUseMoney = data.getString("wallet");
							Common.yuer = canUseMoney;
						} else if (code == -1) {
							ToastUtil.showLongToast(BondPayActivity.this, "登录超时");
						} else if (code == -2) {
							ToastUtil.showLongToast(BondPayActivity.this, "数据不存在");
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
	@OnClick(R.id.paymoney)
	public void payMoney(){
		if(Common.isBondEqulsZero){
			//钱包支付
			DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
			String p=decimalFormat.format(totalx);
			walletPayPost(getIntent().getStringExtra("oid"),p,Common.picketSel.getNum());
		}else{
			if(payMethod==1){
				zfbPayPost(getIntent().getStringExtra("oid"));
			}
			if(payMethod==2){
				bankPayTNPost(getIntent().getStringExtra("oid"));
			}
			if(payMethod==3){
				//钱包支付
				DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
				String p=decimalFormat.format(totalx);
				if(Common.picketSel!=null){
					walletPayPost(getIntent().getStringExtra("oid"),p,Common.picketSel.getNum());
				}else{
					walletPayPost(getIntent().getStringExtra("oid"),p,"");
				}


			}
		}


	}

	private void initView() {
		Intent intent = getIntent();
		mbond = intent.getStringExtra("bond");
		oid=intent.getStringExtra("oid");
		bond.setText("￥" + mbond);
		proId=Common.proId;
		totalx=Double.parseDouble(mbond);
		//Common.total=totalx;
		getPicketsPost(totalx);
		walletIndetPost();
	}




	private void bankPayTNPost(String oid) {
		/*************************************************
		 * 步骤1：从网络开始,获取交易流水号即TN
		 ************************************************/
		order_num=oid;
		Logger.i("oid1---=", order_num);
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(getApplicationContext(),timestamp,"Pay","wyzf");


		OkHttpUtils.post()
				.url(Constant.PAY_WYZF)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.addParams("oid",oid)
				.addParams("type",2+"")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG, response);
						JSONObject jsonObject=JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){

							//跳转支付宝界面支付
//							Intent intent=new Intent(BondPayActivity.this, PayDemoActivity.class);
//							intent.putExtra("payInfo",jsonObject.getString("data"));
//							startActivity(intent);
							final String tn=jsonObject.getString("tn");
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604283844289307\"&subject=\"富硒优质精选毛豆\"&body=\"商品数量：1000/斤，成交单价：10.00/斤\"&total_fee=\"0.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbAppNotiy\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"hIQdbkLMXD26aLniLjEbEhd901DoR6Z4RbXe5PMsXYcdF4ZAi7xlOI8EE2QssV8wGAz3ADS0af11KnoQ1PTS8Xt0asCkJ0TcLy0JgUooBdVb76sbUK633AJrwDrG3lcSEgqnLaVvW8mwLGdxgw7qYQbnyDzx/OmtjJ4ts7kJUpM=\"&sign_type=\"RSA\"";


							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604283844289307\"&subject=\"富硒优质精选毛豆\"&body=\"商品数量：1000/斤，成交单价：10.00/斤\"&total_fee=\"20.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbAppNotiy\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"hIQdbkLMXD26aLniLjEbEhd901DoR6Z4RbXe5PMsXYcdF4ZAi7xlOI8EE2QssV8wGAz3ADS0af11KnoQ1PTS8Xt0asCkJ0TcLy0JgUooBdVb76sbUK633AJrwDrG3lcSEgqnLaVvW8mwLGdxgw7qYQbnyDzx/OmtjJ4ts7kJUpM=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160531800069276029\"&subject=\"绿色水果黄瓜（0.8元/斤）\"&body=\"商品数量：400/斤，成交单价：10.00/斤\"&total_fee=\"4000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"aJSC7DkkkhXg0uSsoPeq0YxTwTxLCcoOt9TEG5UIic0Rmbt4B6jjvoezqIlCT9JvGS/O8BLltK3N86CqdNP62YeshApBDyQrGzd9EfRJsG6FrAksyuRoWDqePo83bu3Uq1VLZA4XSotyqUwoaasy8kAY4ziZ2qUMdPXwUo618P0=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160531800069276029\"&subject=\"绿色水果黄瓜（0.8元/斤）\"&body=\"商品数量：400/斤，成交单价：10.00/斤\"&total_fee=\"4000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"aJSC7DkkkhXg0uSsoPeq0YxTwTxLCcoOt9TEG5UIic0Rmbt4B6jjvoezqIlCT9JvGS/O8BLltK3N86CqdNP62YeshApBDyQrGzd9EfRJsG6FrAksyuRoWDqePo83bu3Uq1VLZA4XSotyqUwoaasy8kAY4ziZ2qUMdPXwUo618P0=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604303792988828\"&subject=\"重庆农副产品毛豆批发\"&body=\"商品数量：200/斤，成交单价：10.00/斤\"&total_fee=\"2000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"nIlK3P18qYkmeKtd%2Bs0khVNw8yVV8vsfHOuht3JTIN5QMSZc6SenP3g7A4YGDa92E5kDZfDMLPjpnfAIHskCJSA%2BswFwrgQo3zwHWL65OxgM%2F3NMzDGDV8gXsDO7AAJ1rwCT4A4cTUfcIYrsgXU379PclRYCE%2BL1P1xGXXTsd1M%3D\"&sign_type=\"RSA\"";
							Log.i("tn==", tn);
							/*Runnable payRunnable = new Runnable() {

								@Override
								public void run() {
									// 构造PayTask 对象
									PayTask alipay = new PayTask(BondPayActivity.this);
									// 调用支付接口，获取支付结果
									String result = alipay.pay(payInfo, true);

									Message msg = new Message();
									msg.what = SDK_PAY_FLAG;
									msg.obj = result;
									mHandler.sendMessage(msg);
								}
							};

							// 必须异步调用
							Thread payThread = new Thread(payRunnable);
							payThread.start();*/
							/*************************************************
							 * 步骤2：通过银联工具类启动支付插件
							 ************************************************/
							UPPayAssistEx.startPay(BondPayActivity.this, null, null, tn, mMode);

						}else if(code==-1){
							ToastUtil.showLongToast(BondPayActivity.this,"没有找到订单数据");
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/*************************************************
		 * 步骤3：处理银联手机支付控件返回的支付结果
		 ************************************************/
		if (data == null) {
			return;
		}

		String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
		String str = data.getExtras().getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			// 支付成功后，extra中如果存在result_data，取出校验
			// result_data结构见c）result_data参数说明
			if (data.hasExtra("result_data")) {
				String result = data.getExtras().getString("result_data");
				try {
					JSONObject resultJson = JSONObject.parseObject(result);
					String sign = resultJson.getString("sign");
					String dataOrg = resultJson.getString("data");
					// 验签证书同后台验签证书
					// 此处的verify，商户需送去商户后台做验签
					boolean ret = verify(dataOrg, sign, mMode);
					if (ret) {
						// 验证通过后，显示支付结果
						msg = "支付成功！";
					} else {
						// 验证不通过后的处理
						// 建议通过商户后台查询支付结果
						msg = "支付失败！";
					}
				} catch (Exception e) {
				}
			} else {
				// 未收到签名信息
				// 建议通过商户后台查询支付结果
				msg = "支付成功！";
			}
		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";
		} else if (str.equalsIgnoreCase("cancel")) {
			msg = "用户取消了支付";
		}

		/*AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("支付结果通知");
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		// builder.setCustomTitle();
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();*/
		Toast.makeText(BondPayActivity.this,msg , Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(BondPayActivity.this,ProductFauctionDetailDoingActivity.class);
		intent.putExtra("productId",Common.proId);
		startActivity(intent);
		finish();
	}


	private void zfbPayPost(String oid) {

		order_num=oid;
		Logger.i("oid1---=", order_num);
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(getApplicationContext(),timestamp,"Pay","zfbPay");


		OkHttpUtils.post()
				.url(Constant.PAY_ZFBPAY)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.addParams("oid",oid)
				.addParams("type",2+"")
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG, response);
						JSONObject jsonObject=JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){

							//跳转支付宝界面支付
//							Intent intent=new Intent(BondPayActivity.this, PayDemoActivity.class);
//							intent.putExtra("payInfo",jsonObject.getString("data"));
//							startActivity(intent);
							final String payInfo=jsonObject.getString("data");
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604283844289307\"&subject=\"富硒优质精选毛豆\"&body=\"商品数量：1000/斤，成交单价：10.00/斤\"&total_fee=\"0.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbAppNotiy\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"hIQdbkLMXD26aLniLjEbEhd901DoR6Z4RbXe5PMsXYcdF4ZAi7xlOI8EE2QssV8wGAz3ADS0af11KnoQ1PTS8Xt0asCkJ0TcLy0JgUooBdVb76sbUK633AJrwDrG3lcSEgqnLaVvW8mwLGdxgw7qYQbnyDzx/OmtjJ4ts7kJUpM=\"&sign_type=\"RSA\"";


							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604283844289307\"&subject=\"富硒优质精选毛豆\"&body=\"商品数量：1000/斤，成交单价：10.00/斤\"&total_fee=\"20.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbAppNotiy\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"hIQdbkLMXD26aLniLjEbEhd901DoR6Z4RbXe5PMsXYcdF4ZAi7xlOI8EE2QssV8wGAz3ADS0af11KnoQ1PTS8Xt0asCkJ0TcLy0JgUooBdVb76sbUK633AJrwDrG3lcSEgqnLaVvW8mwLGdxgw7qYQbnyDzx/OmtjJ4ts7kJUpM=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160531800069276029\"&subject=\"绿色水果黄瓜（0.8元/斤）\"&body=\"商品数量：400/斤，成交单价：10.00/斤\"&total_fee=\"4000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"aJSC7DkkkhXg0uSsoPeq0YxTwTxLCcoOt9TEG5UIic0Rmbt4B6jjvoezqIlCT9JvGS/O8BLltK3N86CqdNP62YeshApBDyQrGzd9EfRJsG6FrAksyuRoWDqePo83bu3Uq1VLZA4XSotyqUwoaasy8kAY4ziZ2qUMdPXwUo618P0=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160531800069276029\"&subject=\"绿色水果黄瓜（0.8元/斤）\"&body=\"商品数量：400/斤，成交单价：10.00/斤\"&total_fee=\"4000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"aJSC7DkkkhXg0uSsoPeq0YxTwTxLCcoOt9TEG5UIic0Rmbt4B6jjvoezqIlCT9JvGS/O8BLltK3N86CqdNP62YeshApBDyQrGzd9EfRJsG6FrAksyuRoWDqePo83bu3Uq1VLZA4XSotyqUwoaasy8kAY4ziZ2qUMdPXwUo618P0=\"&sign_type=\"RSA\"";
							//final String payInfo="partner=\"2088221879278471\"&seller_id=\"fjnpp@sina.com\"&out_trade_no=\"O-160604303792988828\"&subject=\"重庆农副产品毛豆批发\"&body=\"商品数量：200/斤，成交单价：10.00/斤\"&total_fee=\"2000.00\"&service=\"mobile.securitypay.pay\"&notify_url=\"http://api.nongpaipai.cn/pay/zfbRefundNotify\"&payment_type=\"1\"&_input_charset=\"utf-8\"&it_b_pay=\"30m\"&sign=\"nIlK3P18qYkmeKtd%2Bs0khVNw8yVV8vsfHOuht3JTIN5QMSZc6SenP3g7A4YGDa92E5kDZfDMLPjpnfAIHskCJSA%2BswFwrgQo3zwHWL65OxgM%2F3NMzDGDV8gXsDO7AAJ1rwCT4A4cTUfcIYrsgXU379PclRYCE%2BL1P1xGXXTsd1M%3D\"&sign_type=\"RSA\"";
							Log.i("payInfo==", payInfo);
							Runnable payRunnable = new Runnable() {

								@Override
								public void run() {
									// 构造PayTask 对象
									PayTask alipay = new PayTask(BondPayActivity.this);
									// 调用支付接口，获取支付结果
									String result = alipay.pay(payInfo, true);

									Message msg = new Message();
									msg.what = SDK_PAY_FLAG;
									msg.obj = result;
									mHandler.sendMessage(msg);
								}
							};

							// 必须异步调用
							Thread payThread = new Thread(payRunnable);
							payThread.start();

						}else if(code==-1){
							ToastUtil.showLongToast(BondPayActivity.this,"没有找到订单数据");
						}
					}
				});
	}

	private void walletPayPost(String oid,String price,String num) {

		order_num=oid;
		Logger.i("oid1---=", order_num);
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(getApplicationContext(),timestamp,"Wallet","payBond");


		OkHttpUtils.post()
				.url(Constant.WALLET_PAYBOND)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.addParams("oid",oid)
				.addParams("price",price)
				.addParams("voucher_num",num)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG, response);
						JSONObject jsonObject=JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if(code==0){
							Toast.makeText(BondPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();

						}else if(code==-1){
							ToastUtil.showLongToast(BondPayActivity.this,"登录超时");
						}else if(code==-2){
							ToastUtil.showLongToast(BondPayActivity.this,"保证金不能为空");
						}else if(code==-3){
							ToastUtil.showLongToast(BondPayActivity.this,"订单号不能为空");
						}else if(code==-4){
							ToastUtil.showLongToast(BondPayActivity.this,"系统忙");
						}else if(code==-5){
							ToastUtil.showLongToast(BondPayActivity.this,"钱包余额不足");
						}
						Intent intent=new Intent(BondPayActivity.this,ProductFauctionDetailDoingActivity.class);
						intent.putExtra("productId",Common.proId);

						Common.isBondEqulsZero=false;
						finish();
					}
				});
	}



	private Handler mHandler = new Handler() {
		@SuppressWarnings("unused")
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case SDK_PAY_FLAG: {
					PayResult payResult = new PayResult((String) msg.obj);
					/**
					 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
					 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
					 * docType=1) 建议商户依赖异步通知
					 */
					String resultInfo = payResult.getResult();// 同步返回需要验证的信息

					String resultStatus = payResult.getResultStatus();
					// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
					if (TextUtils.equals(resultStatus, "9000")) {
						Toast.makeText(BondPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent(BondPayActivity.this,ProductFauctionDetailDoingActivity.class);
						intent.putExtra("productId",Common.proId);
						startActivity(intent);
						finish();

					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(BondPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(BondPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							Intent intent=new Intent(BondPayActivity.this,ProductFauctionDetailDoingActivity.class);
							intent.putExtra("productId",Common.proId);
							startActivity(intent);
							finish();


						}
					}

					break;
				}
				default:
					break;
			}
		};
	};

	int startpay(Activity act, String tn, int serverIdentifier) {
		return 0;
	}

	private boolean verify(String msg, String sign64, String mode) {
		// 此处的verify，商户需送去商户后台做验签
		return true;

	}


}
