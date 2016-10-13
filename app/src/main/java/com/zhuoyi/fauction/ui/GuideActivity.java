package com.zhuoyi.fauction.ui;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.util.MD5Util;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.adapter.GuideGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class GuideActivity extends BaseActivity {

	boolean isAllSelect=false;
	boolean isNotSelect=true;
	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_guide);
		initView();
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
	private AlphaAnimation alphaAnimation;
	MyGridView gridview;
	public String[] img_text ={ "玉米", "青瓜", "尖椒", "四季豆","花菜","冬笋","锥栗","苦瓜" };
	public int[] imgs = {R.drawable.category_yumi,R.drawable.category_qinggua,R.drawable.category_lajiao,R.drawable.category_sijidou
			,R.drawable.category_huocai,R.drawable.category_dongshun,R.drawable.category_zuili,R.drawable.category_kugua};
	public int[] imgs_a = {R.drawable.category_yumi_a,R.drawable.category_qinggua_a,R.drawable.category_lajiao_a,R.drawable.category_sijidou_a
			,R.drawable.category_huacai_a,R.drawable.category_dongsun_a,R.drawable.category_zuili_a,R.drawable.category_kugua_a};

	Button guide_ok_btn;
	Button guide_all_btn;

	List<Category> categoryList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}

	private void initView() {
		showDialog("加载中");
		gridview=(MyGridView)findViewById(R.id.guide_gridview);

		//Logger.i(TAG,Util.getAppVersionName(GuideActivity.this,"com.zhuoyi.fauction"));
	//	Logger.i(TAG,Util.getAppVersionName(GuideActivity.this,Constant.installation));
		//firstInstallationPost();
		guideMainIndexPost();
	}


	private void guideMainIndexPost() {
		String timestamp= DateUtil.getStringDate();
		Logger.i(TAG,"timestamp="+timestamp);
		String sign= Util.createSign(getApplicationContext(),timestamp,"Main","index");
		Logger.i(TAG,"sign="+sign);
		Logger.i(TAG,"user_id="+ConfigUserManager.getUserId(getApplicationContext()));
		OkHttpUtils.post()
				.url(Constant.MAIN_INDEX)
				.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
				.addParams("timestamp",timestamp)
				.addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
				.addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
				.build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call call, Exception e) {

					}

					@Override
					public void onResponse(String response) {
						Logger.i(TAG,response);
						categoryList=new ArrayList<Category>();
						JSONObject jsonObject=JSONObject.parseObject(response);
						int code = jsonObject.getIntValue("code");
						if (code==0) {
							JSONArray data = jsonObject.getJSONArray("data");
							for(int i=0;i<data.size();i++){
								final Category category=new Category();
								JSONObject categoryJson=data.getJSONObject(i);
								category.setId(categoryJson.getString("id"));
								category.setTitle(categoryJson.getString("title"));
								String pic = categoryJson.getString("pic");
								if(!pic.equals("[]")){
									JSONObject picJsonObject=categoryJson.getJSONObject("pic");
									String normal_img=picJsonObject.getString("3");
									String select_img=picJsonObject.getString("4");
									category.setSelect_img(select_img);
									category.setNormal_img(normal_img);
								}

							/*OkHttpUtils.get()//
							.url(normal_img)//
							.build()//
							.execute(new BitmapCallback() {
								@Override
								public void onError(Call call, Exception e) {

								}

								@Override
								public void onResponse(Bitmap response) {
									category.setNormal_img(response);
								}
							});*/
							/*OkHttpUtils.get()//
									.url(select_img)//
									.build()//
									.execute(new BitmapCallback() {
										@Override
										public void onError(Call call, Exception e) {

										}

										@Override
										public void onResponse(Bitmap response) {
											category.setSelect_img(response);
										}
									});*/
								categoryList.add(category);
								dismiss();
							}


							final GuideGridAdapter adapter=new GuideGridAdapter(GuideActivity.this);

							adapter.setCategories(categoryList);
							Common.allCategories=categoryList;
							gridview.setAdapter(adapter);
							gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
								@Override
								public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
									isNotSelect = true;
								}
							});

							//
							ConfigUserManager.setFirstOpen(GuideActivity.this, false);
							//
							guide_ok_btn=(Button)findViewById(R.id.guide_ok_btn);
							guide_ok_btn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									//Common.categories=(List<Category>) ConfigUserManager.readObject(GuideActivity.this,"sel_category");
									if (isNotSelect) {
										if (Common.categories != null) {
											ConfigUserManager.saveObject(GuideActivity.this, "sel_category", Common.categories);
											Intent intent = new Intent(GuideActivity.this,
													MainActivity.class);
											intent.putExtra("tab", 0);
											startActivity(intent);
											finish();
										} else {
											ToastUtil.showLongToast(GuideActivity.this, "请选择农产品");
										}
									} else {
										if (isAllSelect) {
											Common.categories = Common.allCategories;
											ConfigUserManager.saveObject(GuideActivity.this, "sel_category", Common.categories);
											Intent intent = new Intent(GuideActivity.this,
													MainActivity.class);
											intent.putExtra("tab", 0);
											intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
											startActivity(intent);
											finish();
										}
									}


								}
							});
							int size = categoryList.size();
							Logger.i("csize=",size+"");


							guide_all_btn=(Button)findViewById(R.id.guide_all_btn);
							guide_all_btn.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									if(isAllSelect){
										Common.isALLSelect=false;
										guide_all_btn.setText("全选");
										isAllSelect=false;
										isNotSelect=true;
										List<ImageView> itemViews = adapter.itemViews;
										int size = itemViews.size();
										Logger.i("size=",size+"");
										for(int i=0;i<size;i++){
											Category category = categoryList.get(i);
											category.setIsSelect(false);
											Picasso.with(GuideActivity.this).load(category.getNormal_img()).into(itemViews.get(i));
										}
									}else{
										Common.isALLSelect=true;
										guide_all_btn.setText("取消");
										isAllSelect=true;
										isNotSelect=false;
										List<ImageView> itemViews = adapter.itemViews;
										int size = itemViews.size();
										Logger.i("size=",size+"");
										for(int j=0;j<size;j++){
											Category category = categoryList.get(j);
											category.setIsSelect(true);
											ImageView imageView = itemViews.get(j);
											Picasso.with(GuideActivity.this).load(category.getSelect_img()).into(imageView);
										}
									}
								}
							});

							dismiss();
							//System.out.println(response);



							//ToastUtil.showShortToast(GuideActivity.this,response);

						}

					}
				});
	}

	private void initDatas()
	{

	}







}
