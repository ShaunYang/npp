package com.zhuoyi.fauction.ui.other;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alipay.sdk.app.PayTask;
import com.unionpay.UPPayAssistEx;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
public class MainPayActivity extends BaseActivity {

	@Bind(R.id.price) TextView price;
	@Bind(R.id.xieyi)
	CheckBox xieyi;
	@Bind(R.id.xieyicontent) TextView xieyicontent;
	@Bind(R.id.alipy_pay) ImageView alipyPay;

//	@Bind(R.id.wechat_pay) ImageView wechatPay;
//	@Bind(R.id.bank_pay) ImageView bankPay;
	String money;
	String oid;
	String order_num;

	boolean flag;

	private static final int SDK_PAY_FLAG = 1;

	//@Bind(R.id.paymoney) Button paymoney;
	@Bind(R.id.zfb)
	RelativeLayout zfb;
	@Bind(R.id.bank) RelativeLayout bank;
	@Bind(R.id.offine) RelativeLayout offine;

	@Bind(R.id.alipy_pay_arror) ImageView alipy_pay_arror;
	@Bind(R.id.bank_pay_arror) ImageView bank_pay_arror;
	@Bind(R.id.offine_pay_arror) ImageView offine_pay_arror;

	int payMethod=1;//1支付宝 2网银 3线下付款

	/*****************************************************************
	 * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
	 *****************************************************************/
	private final String mMode = "00";

	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.fragment_paymethod);
		ButterKnife.bind(this);

		initView();
	}

	@OnClick(R.id.pay_confirm) void onPayConfirmClick() {
		if(xieyi.isChecked()){
			if(payMethod==1){
				zfbPayPost(oid);
			}
			if(payMethod==2){
				bankPayTNPost(oid);
			}
			if(payMethod==3){
				//线下付款界面跳转
				offinePaySubmitPost(oid);
			}
		}else{
			ToastUtil.showLongToast(MainPayActivity.this,"请勾选竞拍协议");
		}

	}

	@OnClick(R.id.zfb) void zfbMethod() {
		payMethod=1;
		alipy_pay_arror.setVisibility(View.VISIBLE);
		bank_pay_arror.setVisibility(View.GONE);
		offine_pay_arror.setVisibility(View.GONE);

	}

	@OnClick(R.id.bank) void bankMethod() {
		payMethod=2;
		alipy_pay_arror.setVisibility(View.GONE);
		bank_pay_arror.setVisibility(View.VISIBLE);
		offine_pay_arror.setVisibility(View.GONE);
	}

	@OnClick(R.id.offine) void offineMethod() {
		payMethod=3;
		alipy_pay_arror.setVisibility(View.GONE);
		bank_pay_arror.setVisibility(View.GONE);
		offine_pay_arror.setVisibility(View.VISIBLE);

	}

	@OnClick(R.id.back) void onBackClick() {

		onBackPressed();
	}

	@OnClick(R.id.xieyicontent) void xieyiContent() {

		Intent intent=new Intent(MainPayActivity.this,FauctionXieYiActivity.class);
		startActivity(intent);
	}

	private void offinePaySubmitPost(String oid) {

		order_num=oid;
		Logger.i("oid1---=", order_num);
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(getApplicationContext(),timestamp,"Order","offlinePayment");


		OkHttpUtils.post()
				.url(Constant.ORDER_OFFINEPAYMENT)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.addParams("oid",oid)
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
							Intent intent=new Intent(MainPayActivity.this,OffinePayStateActivity.class);
							intent.putExtra("oid", order_num);
							intent.putExtra("id",getIntent().getStringExtra("id"));
							startActivity(intent);
							finish();

						}else if(code==-1){
							ToastUtil.showLongToast(MainPayActivity.this,"没有找到订单数据");
						}else if(code==-2){
							ToastUtil.showLongToast(MainPayActivity.this,"系统忙 请重新提交");
						}
					}
				});
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
				.addParams("type",1+"")
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
							UPPayAssistEx.startPay(MainPayActivity.this, null, null, tn, mMode);

						}else if(code==-1){
							ToastUtil.showLongToast(MainPayActivity.this,"没有找到订单数据");
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
		Toast.makeText(MainPayActivity.this,msg , Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(MainPayActivity.this,PayStateActivity.class);
		intent.putExtra("oid", oid);
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

	private String TAG="MainPayActivity";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {
		Intent intent = getIntent();
		oid = intent.getStringExtra("oid");
		price.setText(intent.getStringExtra("showprice"));

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
				.addParams("type",1+"")
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
//							Intent intent=new Intent(MainPayActivity.this, PayDemoActivity.class);
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
									PayTask alipay = new PayTask(MainPayActivity.this);
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
							ToastUtil.showLongToast(MainPayActivity.this,"没有找到订单数据");
						}
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
						Toast.makeText(MainPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
						flag=true;

					} else {
						// 判断resultStatus 为非"9000"则代表可能支付失败
						// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						if (TextUtils.equals(resultStatus, "8000")) {
							Toast.makeText(MainPayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
							flag=false;

						} else {
							// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
							Toast.makeText(MainPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
							flag=false;


						}
					}

					Intent intent=new Intent(MainPayActivity.this,PayStateActivity.class);
					intent.putExtra("oid", oid);
					intent.putExtra("id",getIntent().getStringExtra("id"));
					startActivity(intent);

					break;
				}
				default:
					break;
			}
		};
	};


	private boolean verify(String msg, String sign64, String mode) {
		// 此处的verify，商户需送去商户后台做验签
		return true;

	}



}
