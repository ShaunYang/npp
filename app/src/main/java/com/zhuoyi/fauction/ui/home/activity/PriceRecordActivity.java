package com.zhuoyi.fauction.ui.home.activity;



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
import com.zhuoyi.fauction.model.MyHaveFauction;
import com.zhuoyi.fauction.model.RecordDetail;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.home.adapter.DetailRecordAdapter;
import com.zhuoyi.fauction.ui.mine.adapter.MyHaveFauctionDoingAdapter;
import com.zhuoyi.fauction.ui.mine.adapter.MyHaveFauctionEndAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class PriceRecordActivity extends BaseActivity implements XListView.IXListViewListener{



	@Bind(R.id.price_record_icon) ImageView priceRecordIcon;
	@Bind(R.id.fauction_record) TextView fauctionRecord;
	@Bind(R.id.remind) TextView remind;
	//@Bind(R.id.more_paimaijilu) TextView morePaimaijilu;
	@Bind(R.id.paimairecycleview) XListView xListView;
	/**服务器端一共多少条数据*/
	private static  int TOTAL_COUNTER = 64;

	/**每一页展示多少条数据*/
	private static  int REQUEST_COUNT = 10;

	/**已经获取到多少条数据了*/
	private int mCurrentCounter = 0;

	/**当前页*/
	private int mPage = 1;
	//List<MyHaveFauction> fauctionDos;

	DetailRecordAdapter mDataAdapter;


	int mStartNo=1;
	//总页数
	int mPageSize=10;
	boolean isFirst=true;

	int mType=1;

	List<RecordDetail.DataBean> mdata;


	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.activity_price_record);
		ButterKnife.bind(this);
		mType=getIntent().getIntExtra("shootType",1);
		initView();


	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
	}



	private void loadDataListPostType(final int pageSize,final boolean isFlash,int type){


			if(pageSize<=mPageSize){
				String timestamp= DateUtil.getStringDate();

				String sign= Util.createSign(PriceRecordActivity.this,timestamp,"Product","auctionRecordList");
				Logger.i("data===",String.valueOf(pageSize)+"-"+type);
				OkHttpUtils.post()
						.url(Constant.PRODUCT_AUCTIONRECORDLIST)
						.addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(PriceRecordActivity.this))
						.addParams("timestamp", timestamp)
						.addParams("client_id", ConfigUserManager.getUnicodeIEME(PriceRecordActivity.this))
						.addParams("user_id", ConfigUserManager.getUserId(PriceRecordActivity.this))
						.addParams("id", Common.proId+"")
						.addParams("page", String.valueOf(pageSize))
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e) {

							}

							@Override
							public void onResponse(String response) {

								Logger.i(TAG + "list====", response);
//								if (isFirst)
//									//((CategoryDoingTabActivity)getActivity()).dismiss();
//
								if (isFlash)
									mdata = new ArrayList<RecordDetail.DataBean>();
								//json解析
								JSONObject jsonObject = JSONObject.parseObject(response);
								int code = jsonObject.getIntValue("code");
								if(code==0){
									mPageSize=jsonObject.getIntValue("total_page");
									Gson gson=new Gson();
									RecordDetail recordDetail = gson.fromJson(response, RecordDetail.class);
									if(recordDetail!=null){
										List<RecordDetail.DataBean> data = recordDetail.getData();
										if(data!=null){
											Logger.i(TAG+"--data size=", ""+data.size());
											for(RecordDetail.DataBean dataBean:data){
												mdata.add(dataBean);
											}


											if (isFlash) {
												mDataAdapter = new DetailRecordAdapter(PriceRecordActivity.this, mdata);
												xListView.setAdapter(mDataAdapter);
											} else {
												mDataAdapter.notifyDataSetChanged();
											}
											onLoad();
											// mStartNo=startNo+pageSize;
											//页数自增1
											mPage++;
											isFirst = false;
											remind.setText(recordDetail.getEnlist_num()+"人报名 出价"+recordDetail.getOffer_num()+"次");
										}
										//remind.setText(recordDetail.getEnlist_num()+"人报名 出价"+recordDetail.getOffer_num()+"次");
									}
								}





							}
						});
			}




	}


	@Override
	public void onRefresh() {
		mPage=1;

		loadDataListPostType(mPage, true, mType);
		//loadType(mPage, true);
	}

	@Override
	public void onLoadMore() {
		if(mPage<=mPageSize){
			loadDataListPostType(mPage, false, mType);
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

		xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {


			}
		});
		loadDataListPostType(mPage,true,mType);
		//loadType(mPage, true);



	}












}
