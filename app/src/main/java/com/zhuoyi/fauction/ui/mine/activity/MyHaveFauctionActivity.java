package com.zhuoyi.fauction.ui.mine.activity;



import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.view.CommonTopBar;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.model.MyHaveFauction;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.mine.adapter.MyHaveFauctionDoingAdapter;
import com.zhuoyi.fauction.ui.mine.adapter.MyHaveFauctionEndAdapter;
import com.zhuoyi.fauction.ui.other.OrderDetailActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class MyHaveFauctionActivity extends BaseActivity implements XListView.IXListViewListener{





	@Bind(R.id.list)
	XListView xListView;
	@Bind(R.id.doing)
	Button doing;
	@Bind(R.id.end) Button end;
	/**服务器端一共多少条数据*/
	private static  int TOTAL_COUNTER = 64;

	/**每一页展示多少条数据*/
	private static  int REQUEST_COUNT = 10;

	/**已经获取到多少条数据了*/
	private int mCurrentCounter = 0;

	/**当前页*/
	private int mPage = 1;
	List<MyHaveFauction> fauctionDos;

	MyHaveFauctionDoingAdapter mDataAdapter;
	MyHaveFauctionEndAdapter mDataAdapter2;

	int mStartNo=1;
	//总页数
	int mPageSize=10;
	boolean isFirst=true;

	int type=1;


	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.fragment_myhave_fauction);
		ButterKnife.bind(this);

		initView();


	}

	private void loadType(final int pageSize,final boolean isFlash){
		if(type==1){
			loadDataListPostType1(pageSize,isFlash,1);
		}else if(type==2){
			loadDataListPostType2(pageSize, isFlash, 2);
		}
	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
	}

	private void loadDataListPostType1(final int pageSize,final boolean isFlash,int type){



				String timestamp= DateUtil.getStringDate();

				String sign= Util.createSign(MyHaveFauctionActivity.this,timestamp,"Order","myShooting");

				OkHttpUtils.post()
						.url(Constant.ORDER_MYSHOOTING)
						.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(MyHaveFauctionActivity.this))
						.addParams("timestamp",timestamp)
						.addParams("client_id", ConfigUserManager.getUnicodeIEME(MyHaveFauctionActivity.this))
						.addParams("user_id",ConfigUserManager.getUserId(MyHaveFauctionActivity.this))
						.addParams("page", String.valueOf(pageSize))
						.addParams("type",type+"")
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e) {

							}

							@Override
							public void onResponse(String response) {

								Logger.i(TAG + "list====", response);
//								if(isFirst)
//									//((CategoryDoingTabActivity)getActivity()).dismiss();

								if(isFlash)
									fauctionDos = new ArrayList<MyHaveFauction>();
								//json解析
								JSONObject jsonObject=JSONObject.parseObject(response);
								int code=jsonObject.getIntValue("code");
								if(code==0){
									JSONObject data = jsonObject.getJSONObject("data");
									mPageSize=jsonObject.getInteger("total_page");
									for(int i=0;i<REQUEST_COUNT;i++){
										int j=i+1;
										JSONObject temp=data.getJSONObject(j+"");
										if(temp==null){
											break;
										}

										Gson gson=new Gson();
										String s = JSON.toJSONString(temp);
										MyHaveFauction myHaveFauction = gson.fromJson(s, MyHaveFauction.class);
										fauctionDos.add(myHaveFauction);
									}


									if(isFlash){
										mDataAdapter=new MyHaveFauctionDoingAdapter(MyHaveFauctionActivity.this, fauctionDos);
										xListView.setAdapter(mDataAdapter);
										xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
											@Override
											public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
												Intent intent=new Intent();
												intent.setClass(MyHaveFauctionActivity.this,ProductFauctionDetailDoingActivity.class);
												MyHaveFauction fdo=fauctionDos.get(position-1);
												intent.putExtra("productId",Integer.parseInt(fdo.getId()));
												startActivity(intent);
											}
										});
									}else{
										mDataAdapter.notifyDataSetChanged();
									}
									onLoad();
									// mStartNo=startNo+pageSize;
									//页数自增1
									mPage++;
									isFirst=false;

								}








							}
						});





	}
	private void loadDataListPostType2(final int pageSize,final boolean isFlash,int type){



			String timestamp= DateUtil.getStringDate();

			String sign= Util.createSign(MyHaveFauctionActivity.this,timestamp,"Order","myShooting");

			OkHttpUtils.post()
					.url(Constant.ORDER_MYSHOOTING)
					.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(MyHaveFauctionActivity.this))
					.addParams("timestamp",timestamp)
					.addParams("client_id", ConfigUserManager.getUnicodeIEME(MyHaveFauctionActivity.this))
					.addParams("user_id",ConfigUserManager.getUserId(MyHaveFauctionActivity.this))
					.addParams("page", String.valueOf(pageSize))
					.addParams("type",type+"")
					.build()
					.execute(new StringCallback() {
						@Override
						public void onError(Call call, Exception e) {

						}

						@Override
						public void onResponse(String response) {

							Logger.i(TAG + "list====", response);
//							if (isFirst)
//								//((CategoryDoingTabActivity)getActivity()).dismiss();

								if (isFlash)
									fauctionDos = new ArrayList<MyHaveFauction>();
							//json解析
							JSONObject jsonObject=JSONObject.parseObject(response);
							int code=jsonObject.getIntValue("code");
							if(code==0){
								JSONObject data = jsonObject.getJSONObject("data");
								mPageSize=jsonObject.getInteger("total_page");
								for (int i = 0; i < REQUEST_COUNT; i++) {
									int j = i + 1;
									JSONObject temp = data.getJSONObject(j + "");
									if (temp == null) {
										break;
									}

									Gson gson = new Gson();
									String s = JSON.toJSONString(temp);
									MyHaveFauction myHaveFauction = gson.fromJson(s, MyHaveFauction.class);
									fauctionDos.add(myHaveFauction);
								}


								if (isFlash) {
									mDataAdapter2 = new MyHaveFauctionEndAdapter(MyHaveFauctionActivity.this, fauctionDos);
									xListView.setAdapter(mDataAdapter2);
									xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
											Intent intent = new Intent();
											intent.setClass(MyHaveFauctionActivity.this, ProductFauctionDetailEndActivity.class);
											MyHaveFauction fdo = fauctionDos.get(position - 1);
											intent.putExtra("productId",Integer.parseInt(fdo.getId()));
											startActivity(intent);
										}
									});

								} else {
									mDataAdapter2.notifyDataSetChanged();
								}
								onLoad();
								// mStartNo=startNo+pageSize;
								//页数自增1
								mPage++;
								isFirst = false;
							}


						}
					});





	}

	@Override
	public void onRefresh() {
		mPage=1;


		loadType(mPage, true);
	}

	@Override
	public void onLoadMore() {
		if(mPage<=mPageSize){
			loadType(mPage, false);
		}else{
			xListView.stopRefresh();
			xListView.stopLoadMore();
			xListView.mFootTextView.setText(Common.nonextpageText);

		}

	}

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");
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

		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);

//		xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//									long arg3) {
//				Intent intent=new Intent();
//				intent.setClass(MyHaveFauctionActivity.this, ProductFauctionDetailDoingActivity.class);
//				MyHaveFauction myHaveFauction=fauctionDos.get(arg2-1);
//				intent.putExtra("productId", myHaveFauction.getId() + "");
//				startActivity(intent);
//
//			}
//		});

		loadType(mPage, true);

		doing.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				type = 1;
				mPage = 1;
				//doing.setBackgroundResource(R.drawable.guide_btn_sel);
				doing.setBackgroundResource(R.drawable.guide_btn_sel);
				doing.setTextColor(getResources().getColor(R.color.white));
				doing.setText("进行中");
				end.setBackgroundResource(R.drawable.guide_ok_nor);
				end.setTextColor(getResources().getColor(R.color.common_topbar_bg));
				end.setText("已结束");
				if (fauctionDos != null) {
					fauctionDos.clear();
				}

				if (mDataAdapter != null) {
					mDataAdapter.notifyDataSetChanged();

				} else {
					xListView.setAdapter(null);
				}

				loadType(1, true);

			}
		});

		end.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				type=2;
				mPage=1;
				//end.setBackgroundResource(R.drawable.guide_btn_sel);
				end.setBackgroundResource(R.drawable.guide_btn_sel);
				end.setTextColor(getResources().getColor(R.color.white));
				end.setText("已结束");
				doing.setBackgroundResource(R.drawable.guide_ok_nor);
				doing.setTextColor(getResources().getColor(R.color.common_topbar_bg));
				doing.setText("进行中");
				if(fauctionDos!=null){
					fauctionDos.clear();
				}

				if(mDataAdapter2!=null){
					mDataAdapter2.notifyDataSetChanged();
				}else{
					xListView.setAdapter(null);
				}

				loadType(1, true);
			}
		});

	}












}
