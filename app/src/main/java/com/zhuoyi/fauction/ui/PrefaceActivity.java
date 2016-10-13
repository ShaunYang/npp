package com.zhuoyi.fauction.ui;



import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.exception.DbException;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.service.XGPushService;
import com.yintai.DatabaseManager;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.User;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class PrefaceActivity extends Activity {
	private static final String TAG="PrefaceActivity";
	private AlphaAnimation alphaAnimation;

	/** 定义一个全局的activity*/
	public static PrefaceActivity staticAcitivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preface);
		// 开启logcat输出，方便debug，发布时请关闭
		 XGPushConfig.enableDebug(this, true);
		// 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
		// 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
		// 具体可参考详细的开发指南
		// 传递的参数为ApplicationContext
		Context context = getApplicationContext();
		XGPushManager.registerPush(context);

		// 2.36（不包括）之前的版本需要调用以下2行代码
		Intent service = new Intent(context, XGPushService.class);
		context.startService(service);


		// 其它常用的API：
		// 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
		// 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
		// 反注册（不再接收消息）：unregisterPush(context)
		// 设置标签：setTag(context, tagName)
		// 删除标签：deleteTag(context, tagName)

		checkPermissionEnable();
		initView();

	}

	private void initView() {

		LinearLayout layout = (LinearLayout) findViewById(R.id.preface_layout);
		alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(3000);
		alphaAnimation.setFillAfter(true);
		layout.startAnimation(alphaAnimation);
		alphaAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {

				if (ConfigUserManager.isFirstOpen(PrefaceActivity.this)) {
					String imeiCode = Util.getImeiCode(PrefaceActivity.this);
					if(imeiCode==null){
						imeiCode = UUID.randomUUID().toString();

					}

					ConfigUserManager.setUnicodeIEME(getApplicationContext(), imeiCode);
					Logger.i("imei", imeiCode);



					firstInstallationPost();

				} else {
					Common.categories = (List<Category>) ConfigUserManager.readObject(PrefaceActivity.this, "sel_category");
					startAppPost();
				}


			}
		});
	}

	private void checkPermissionEnable() {
		ArrayList<String> list = new ArrayList<String>();
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
				list.add(Manifest.permission.READ_PHONE_STATE);
			}

			if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
				list.add(Manifest.permission.READ_CONTACTS);
			}

			if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
				list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
			}

			if (list.size() > 0) {
				String[] permissions = list.toArray(new String[list.size()]);
				requestPermissions(permissions, 100);
			}
		}
	}

	//客户端安装注册接口
	private void firstInstallationPost() {

		//获取IEME串号
		String ieme=ConfigUserManager.getUnicodeIEME(getApplicationContext());
		OkHttpUtils.post()
				.url(Constant.installation)
				.addParams("client_id", ieme)
				.addParams("type","1")
				.addParams("version", Util.getAppVersionName(getApplicationContext(),"com.zhuoyi.fauction"))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG, response);
						//System.out.println(response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							JSONObject object = JSONObject.parseObject(response);
							JSONObject data = object.getJSONObject("data");
							//secret_key
							String secret_key = data.getString("secret_key");
							Logger.i(TAG, "secret_key=" + secret_key);
							ConfigUserManager.setSecretKey(getApplicationContext(), secret_key);

							//user_id
							String user_id = data.getString("user_id");
							Logger.i(TAG, "user_id=" + user_id);
							ConfigUserManager.setUserId(getApplicationContext(), user_id);
//						User user=new User();
//						user.setId(user_id);
//						//user.setCategoryList(new ArrayList<Category>());
//
//						try {
//							DatabaseManager.getInstance().getDb().save(user);
//
//						} catch (DbException e) {
//							e.printStackTrace();
//						}
							//data_auth_key
							String data_auth_key = object.getString("data_auth_key");
							Logger.i(TAG, "data_auth_key=" + data_auth_key);
							ConfigUserManager.setDataAuth(getApplicationContext(), data_auth_key);
							//
							Intent intent = new Intent(PrefaceActivity.this,
									GuideActivity.class);
							startActivity(intent);
							finish();
							//ToastUtil.showShortToast(GuideActivity.this,response);
						}


					}
				});
	}

	private void guideMainIndexPost() {
		String timestamp= DateUtil.getStringDate();

		String sign= Util.createSign(PrefaceActivity.this, timestamp, "Main", "index");

		//Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MAIN_INDEX)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp", timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(PrefaceActivity.this))
				.addParams("user_id", ConfigUserManager.getUserId(PrefaceActivity.this))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG, response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							List<Category> categoryList = new ArrayList<Category>();
							JSONArray data = jsonObject.getJSONArray("data");
							for (int i = 0; i < data.size(); i++) {
								final Category category = new Category();
								JSONObject categoryJson = data.getJSONObject(i);
								category.setId(categoryJson.getString("id"));
								category.setTitle(categoryJson.getString("title"));
								JSONObject picJsonObject = categoryJson.getJSONObject("pic");
								String normal_img = picJsonObject.getString("3");
								String select_img = picJsonObject.getString("4");
								category.setSelect_img(select_img);
								category.setNormal_img(normal_img);

								categoryList.add(category);

							}

							Common.allCategories = categoryList;

							//
							Intent intent = new Intent(PrefaceActivity.this,
									MainActivity.class);
							intent.putExtra("tab",0);
							startActivity(intent);
							finish();
						}

						//


						//System.out.println(response);


						//ToastUtil.showShortToast(GuideActivity.this,response);

					}
				});
	}


	private void startAppPost() {

		//获取IEME串号
		String ieme=ConfigUserManager.getUnicodeIEME(getApplicationContext());
		OkHttpUtils.post()
				.url(Constant.installation)
				.addParams("client_id", ieme)
				.addParams("type","1")
				.addParams("version", Util.getAppVersionName(getApplicationContext(),"com.zhuoyi.fauction"))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG,response);
						//System.out.println(response);
						JSONObject jsonObject = JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							JSONObject object=JSONObject.parseObject(response);
							JSONObject data = object.getJSONObject("data");
							//secret_key
							String secret_key = data.getString("secret_key");
							Logger.i(TAG,"secret_key="+secret_key);
							ConfigUserManager.setSecretKey(getApplicationContext(), secret_key);

							//user_id
							String user_id = data.getString("user_id");
							Logger.i(TAG,"user_id="+user_id);
							ConfigUserManager.setUserId(getApplicationContext(), user_id);
//						User user=new User();
//						user.setId(user_id);
//						//user.setCategoryList(new ArrayList<Category>());
//
//						try {
//							DatabaseManager.getInstance().getDb().save(user);
//
//						} catch (DbException e) {
//							e.printStackTrace();
//						}
							//data_auth_key
							String data_auth_key=object.getString("data_auth_key");
							Logger.i(TAG, "data_auth_key=" + data_auth_key);
							ConfigUserManager.setDataAuth(getApplicationContext(), data_auth_key);
							//

							guideMainIndexPost();

							//ToastUtil.showShortToast(GuideActivity.this,response);
						}


					}
				});
	}




}
