package com.zhuoyi.fauction.ui.myfauction.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.yintai.common.view.CommonTopBar;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;
import com.zhuoyi.fauction.ui.home.fragment.HRJXFragment;
import com.zhuoyi.fauction.ui.home.fragment.JRZQFragment;
import com.zhuoyi.fauction.ui.home.fragment.PMYZFragment;
import com.zhuoyi.fauction.ui.home.fragment.ZXCJFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
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

//相似拍品
public class LikeFauctionProActivity extends BaseActivity implements View.OnClickListener,XListView.IXListViewListener {



    @Bind(R.id.xlistview) XListView xListView;


    String TAG="LikeFauctionProActivity";
    CategoryDoingAdapter mDataAdapter;

    List<FauctionDo> fauctionDos;

    Category category;

    int order=2;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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
        if(Common.whichActivity==1){
            Intent intent=new Intent();
            intent.putExtra("tab",2);
            Common.whichActivity=1;
            intent.setClass(LikeFauctionProActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            onBackPressed();
        }
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_like_fauction);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);


        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = new Intent();
                intent.setClass(LikeFauctionProActivity.this, ProductFauctionDetailDoingActivity.class);
                FauctionDo fdo = fauctionDos.get(arg2 - 1);
                intent.putExtra("productId", fdo.getId());
                startActivity(intent);
                finish();

            }
        });



        loadDataListPost(mPage, true, order);

    }

    private void loadDataListPost(final int pageSize,final boolean isFlash,int order){


            if(pageSize<=mPageSize){
                String timestamp= DateUtil.getStringDate();

                String sign= Util.createSign(LikeFauctionProActivity.this, timestamp, "Product", "deal");

                OkHttpUtils.post()
                        .url(Constant.PRODUCT_DEAL)
                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(LikeFauctionProActivity.this))
                        .addParams("timestamp", timestamp)
                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(LikeFauctionProActivity.this))
                        .addParams("user_id",ConfigUserManager.getUserId(LikeFauctionProActivity.this))
                        .addParams("page", String.valueOf(pageSize))
                        .addParams("id",Common.TYPEID+"")
                        .addParams("order",order+"")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e) {

                            }

                            @Override
                            public void onResponse(String response) {

                                Logger.i(TAG + "45646456456====", response);
                                if (isFirst)
                                    dismiss();

                                if (isFlash)
                                    fauctionDos = new ArrayList<FauctionDo>();
                                //json解析
                                JSONObject jsonObject = JSONObject.parseObject(response);
                                int code = jsonObject.getIntValue("code");
                                if (code == 0) {
                                    JSONObject data = jsonObject.getJSONObject("data");
                                    mPageSize = jsonObject.getInteger("total_page");
                                    for (int i = 0; i < REQUEST_COUNT; i++) {
                                        int j = i + 1;
                                        JSONObject temp = data.getJSONObject(j + "");
                                        if (temp == null) {
                                            break;
                                        }

                                        FauctionDo fauctionDo = new FauctionDo();
                                        fauctionDo.setCurPrice(temp.getString("current_price"));
                                        fauctionDo.setId(temp.getShort("id"));
                                        fauctionDo.setTitle(temp.getString("title"));
                                        fauctionDo.setResidual_time(temp.getString("residual_time"));
                                        fauctionDo.setNum(temp.getString("num"));
                                        fauctionDo.setUnit(temp.getString("unit"));
                                        fauctionDo.setStatus(temp.getInteger("status"));
                                        fauctionDo.setStock(temp.getString("stock"));
                                        fauctionDo.setTitle_img(temp.getString("pic"));
                                        fauctionDos.add(fauctionDo);
                                    }


                                    if (isFlash) {
                                        mDataAdapter = new CategoryDoingAdapter(LikeFauctionProActivity.this, fauctionDos);
                                        xListView.setAdapter(mDataAdapter);
                                    } else {
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
            xListView.mFootTextView.setText(Common.nonextpageText);

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
