package com.zhuoyi.fauction.ui.category.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.squareup.otto.Subscribe;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryPreTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.category.adapter.CategoryPreAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailPreActivity;
import com.zhuoyi.fauction.ui.home.event.PriceSortEventData;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;


public class CategoryPreListFragment extends Fragment implements View.OnClickListener,XListView.IXListViewListener {

    private View viewHolder;
    private XListView xListView;

    String TAG="CategoryPreListFragment";
    CategoryPreAdapter mDataAdapter;

    List<FauctionDo> fauctionDos;

    Category category;

    int order;

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



    private HeaderAndFooterRecyclerViewAdapter mHeaderAndFooterRecyclerViewAdapter = null;

    public static CategoryPreListFragment newInstance(String param1, String param2) {
        CategoryPreListFragment fragment = new CategoryPreListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_fauction_all, container, false);
            ButterKnife.bind(this, viewHolder);
            BusProvider.getInstance().register(this);
             initComponent();
        }
        ViewGroup parent = (ViewGroup) viewHolder.getParent();
        if (parent != null) {
            parent.removeView(viewHolder);
        }
        ButterKnife.bind(this, viewHolder);
        return viewHolder;
    }

    private void initComponent(){

        xListView=(XListView)viewHolder.findViewById(R.id.all_recycle);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent=new Intent();
                intent.setClass(getActivity(),ProductFauctionDetailPreActivity.class);
                FauctionDo fdo=fauctionDos.get(arg2-1);
                intent.putExtra("productId",fdo.getId());
                getActivity().startActivity(intent);


            }
        });



        loadDataListPost(mPage, true, order);





    }













    private void loadDataListPost(final int pageSize,final boolean isFlash,int order){

            if(category.getId().equals("-1")){
                if(pageSize<=mPageSize){
                    String timestamp= DateUtil.getStringDate();

                    String sign= Util.createSign(getActivity(),timestamp,"Product","soon");

                    OkHttpUtils.post()
                            .url(Constant.PRODUCT_SOON)
                            .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                            .addParams("timestamp", timestamp)
                            .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                            .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                            .addParams("page",String.valueOf(pageSize))
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
                                        ((CategoryPreTabActivity) getActivity()).dismiss();

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

                                            fauctionDo.setShoot_price(temp.getString("shoot_price"));
                                            fauctionDo.setShoot_time(temp.getString("shoot_time"));
                                            fauctionDo.setId(temp.getShort("id"));
                                            fauctionDo.setTitle(temp.getString("title"));
                                            fauctionDo.setNum(temp.getString("num"));
                                            fauctionDo.setUnit(temp.getString("unit"));
                                            fauctionDo.setTitle_img(temp.getString("pic"));
                                            fauctionDos.add(fauctionDo);
                                        }


                                        if (isFlash) {
                                            mDataAdapter = new CategoryPreAdapter(getActivity(), fauctionDos);
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
            }else{
                if(pageSize<=mPageSize){
                    String timestamp= DateUtil.getStringDate();

                    String sign= Util.createSign(getActivity(),timestamp,"Product","soon");

                    OkHttpUtils.post()
                            .url(Constant.PRODUCT_SOON)
                            .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                            .addParams("timestamp",timestamp)
                            .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                            .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                            .addParams("id",URLEncoder.encode(category.getId()))
                            .addParams("page",String.valueOf(pageSize))
                            .addParams("order",order+"")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(String response) {

                                    Logger.i(TAG+"45646456456====",response);
                                    if(isFirst)
                                        ((CategoryPreTabActivity)getActivity()).dismiss();

                                    if(isFlash)
                                        fauctionDos = new ArrayList<FauctionDo>();
                                    //json解析
                                    JSONObject jsonObject=JSONObject.parseObject(response);
                                    String strData=jsonObject.getString("data");
                                    if(!strData.equals("[]")){
                                        JSONObject data = jsonObject.getJSONObject("data");
                                        mPageSize=jsonObject.getInteger("total_page");
                                        for(int i=0;i<REQUEST_COUNT;i++){
                                            int j=i+1;
                                            JSONObject temp=data.getJSONObject(j+"");
                                            if(temp==null){
                                                break;
                                            }

                                            FauctionDo fauctionDo = new FauctionDo();
                                            fauctionDo.setShoot_price(temp.getString("shoot_price"));
                                            fauctionDo.setShoot_time(temp.getString("shoot_time"));
                                            fauctionDo.setId(temp.getShort("id"));
                                            fauctionDo.setTitle(temp.getString("title"));
                                            fauctionDo.setNum(temp.getString("num"));
                                            fauctionDo.setUnit(temp.getString("unit"));
                                            fauctionDo.setTitle_img(temp.getString("pic"));
                                            fauctionDos.add(fauctionDo);
                                        }


                                        if(isFlash){
                                            mDataAdapter=new CategoryPreAdapter(getActivity(), fauctionDos);
                                            xListView.setAdapter(mDataAdapter);
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
            }



    }




    //在恢复里及时更新数据
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //BusProvider.getInstance().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //BusProvider.getInstance().unregister(this);
    }

    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Subscribe
    public void orderRefresh(PriceSortEventData priceSortEventData){
        mPage=1;
        loadDataListPost(mPage, true, priceSortEventData.getOrder());
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
}
