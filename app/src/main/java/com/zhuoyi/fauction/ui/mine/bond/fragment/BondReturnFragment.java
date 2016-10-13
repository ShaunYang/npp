package com.zhuoyi.fauction.ui.mine.bond.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.MyBond;
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.mine.bond.adapter.BondItemAdapter;
import com.zhuoyi.fauction.ui.other.OrderDetailActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;


public class BondReturnFragment extends Fragment implements XListView.IXListViewListener {

    private View viewHolder;
    private XListView xListView;
    /**
     * 服务器端一共多少条数据
     */
    private static int TOTAL_COUNTER = 64;

    /**
     * 每一页展示多少条数据
     */
    private static int REQUEST_COUNT = 10;

    /**
     * 已经获取到多少条数据了
     */
    private int mCurrentCounter = 0;

    /**
     * 当前页
     */
    private int mPage = 1;
    String TAG = "FauctionAllFragment";

    List<MyBond.DataBean> fauctionDos;
    BondItemAdapter mDataAdapter;

    int mStartNo = 1;
    //总页数
    int mPageSize = 10;
    boolean isFirst = true;

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

    private void initComponent() {
        xListView = (XListView) viewHolder.findViewById(R.id.all_recycle);
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                MyBond.DataBean fdo = fauctionDos.get(arg2 - 1);
                //String state = fdo.getState();
                int state = fdo.getShoot_state();
                if (state == 1) {

                    Intent intent = new Intent(getActivity(), ProductFauctionDetailDoingActivity.class);
                    intent.putExtra("productId", Integer.parseInt(fdo.getId()));
                    getActivity().startActivity(intent);


                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), ProductFauctionDetailEndActivity.class);

                    intent.putExtra("productId", Integer.parseInt(fdo.getId()));
                    getActivity().startActivity(intent);
                }


            }
        });


        loadDataListPost(mPage, true, 4);


    }

    private void loadDataListPost(final int pageSize,final boolean isFlash,int type) {


        if (pageSize <= mPageSize) {
            String timestamp = DateUtil.getStringDate();

            String sign = Util.createSign(getActivity(), timestamp, "Order", "bondInfo");

            OkHttpUtils.post()
                    .url(Constant.ORDER_BONDINFO)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                    .addParams("timestamp", timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                    .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                    .addParams("type", type + "")
                    .addParams("page", String.valueOf(pageSize))

                    .build()
                    .execute(new StringCallback() {
                                 @Override
                                 public void onError(Call call, Exception e) {

                                 }

                                 @Override
                                 public void onResponse(String response) {

                                     Logger.i(TAG + "45646456456====", response);
                                     // if (isFirst)
                                     // ((MainActivity) getActivity()).dismiss();

                                     if (isFlash) {

                                         fauctionDos = new ArrayList<MyBond.DataBean>();
                                     }
                                     //
                                     //json解析
                                     JSONObject jsonObject = JSONObject.parseObject(response);
                                     int code = jsonObject.getIntValue("code");
                                     if (code==0) {
                                         Gson gson=new Gson();
                                         MyBond myBond = gson.fromJson(response, MyBond.class);
                                         mPageSize = myBond.getTotal_page();

                                         if (isFlash) {
                                             fauctionDos=myBond.getData();
                                             mDataAdapter = new BondItemAdapter(getActivity(),fauctionDos );
                                             xListView.setAdapter(mDataAdapter);
                                             mDataAdapter.notifyDataSetChanged();
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
                             }
                    );

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
    public void onRefresh() {
        mPage = 1;
        loadDataListPost(mPage, true, 4);
    }

    @Override
    public void onLoadMore() {
        if (mPage <= mPageSize) {
            loadDataListPost(mPage, false, 4);
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
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
