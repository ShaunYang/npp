package com.zhuoyi.fauction.ui.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.MyPicket;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.mine.adapter.MyPicketAdapter;
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

//相似拍品
public class MyPicketActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener {



    @Bind(R.id.xlistview) XListView xListView;
    @Bind(R.id.sygz) TextView sygz;
    @Bind(R.id.dyj_his)
    TextView dyj_his;


    String TAG="MyPicketActivity";
    MyPicketAdapter mDataAdapter;

    List<MyPicket.DataBean> fauctionDos;



    int order=2;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }



    /**服务器端一共多少条数据*/
    private static  int TOTAL_COUNTER = 64;

    /**每一页展示多少条数据*/
    private static  int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private int mCurrentCounter = 0;

    /**当前页*/
    private int mPage = 1;


    int mStartNo=1;
    //总页数
    int mPageSize=10;
    boolean isFirst=true;

    // private PreviewHandler mHandler = new PreviewHandler(getActivity());



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.back) void onBackClick() {
        //TODO implement
        onBackPressed();
    }

    @OnClick(R.id.dyj_his) void toMyPicketHis() {
        Intent intent=new Intent(MyPicketActivity.this, MyPicketHistoryActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.sygz) void toSYGZ() {
        Intent intent=new Intent(MyPicketActivity.this, PicketHelpActivity.class);
        startActivity(intent);
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_mypicket);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);


        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
             /*   Intent intent = new Intent();
                intent.setClass(MyPicketActivity.this, ProductFauctionDetailDoingActivity.class);
                 fdo = fauctionDos.get(arg2 - 1);
                intent.putExtra("productId", fdo.getId());
                startActivity(intent);
                finish();*/

            }
        });



        loadDataListPost(mPage, true, order);

    }

    private void loadDataListPost(final int pageSize,final boolean isFlash,int order){


            if(pageSize<=mPageSize){
                String timestamp= DateUtil.getStringDate();

                String sign= Util.createSign(MyPicketActivity.this, timestamp, "Voucher", "getVoucher");

                OkHttpUtils.post()
                        .url(Constant.VOUCHER_GETVOUCHER)
                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(MyPicketActivity.this))
                        .addParams("timestamp", timestamp)
                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(MyPicketActivity.this))
                        .addParams("user_id",ConfigUserManager.getUserId(MyPicketActivity.this))
                        .addParams("page", String.valueOf(pageSize))
                        .addParams("state","1")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {

                                Logger.i(TAG + "MyPicketActivity====", response);
                                if (isFirst)
                                    dismiss();


                                //json解析
                                JSONObject jsonObject = JSONObject.parseObject(response);
                                int code = jsonObject.getIntValue("code");
                               if (code == 0) {
                                   Gson gson=new Gson();
                                   MyPicket myPicket = gson.fromJson(response, MyPicket.class);
                                   if (isFlash){
                                       fauctionDos = myPicket.getData();


                                       mPageSize =myPicket.getTotal_page();
                                       mDataAdapter = new MyPicketAdapter(MyPicketActivity.this, fauctionDos);
                                       xListView.setAdapter(mDataAdapter);
                                   }else{
                                       for(MyPicket.DataBean dataBean:myPicket.getData()){
                                           fauctionDos.add(dataBean);
                                       }
                                       mDataAdapter.notifyDataSetChanged();
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




    }
    @Override
    public void onRefresh() {
        mPage=1;
        loadDataListPost(mPage,true,order);
    }

    @Override
    public void onLoadMore() {
        if(mPage<=mPageSize){
            loadDataListPost(mPage,false,order);
        }else{
            xListView.stopRefresh();
            xListView.stopLoadMore();
            xListView.mFootTextView.setText("");

        }

    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }

    @Override
    public void onClick(View v) {

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
}
