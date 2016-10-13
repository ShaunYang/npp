package com.zhuoyi.fauction.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.model.MyRemind;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;
import com.zhuoyi.fauction.ui.mine.adapter.RemindItemDoAdapter;
import com.zhuoyi.fauction.ui.mine.adapter.RemindItemPreAdapter;
import com.zhuoyi.fauction.ui.myfauction.adapter.FauctionItemAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.ButterKnife;
import okhttp3.Call;


public class RemindDoFragment extends Fragment  implements XListView.IXListViewListener{

    private View viewHolder;
    private XListView xListView;
    /**服务器端一共多少条数据*/
    private static  int TOTAL_COUNTER = 64;

    /**每一页展示多少条数据*/
    private static  int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private int mCurrentCounter = 0;

    /**当前页*/
    private int mPage = 1;
    String TAG="FauctionAllFragment";

    ArrayList<MyRemind> fauctionDos;
    RemindItemDoAdapter mDataAdapter;

    int mStartNo=1;
    //总页数
    int mPageSize=10;

    int mtype=1;
    boolean isFirst=true;


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
                intent.setClass(getActivity(),ProductFauctionDetailDoingActivity.class);
                MyRemind fdo=fauctionDos.get(arg2-1);
                intent.putExtra("productId",fdo.getId());
                getActivity().startActivity(intent);


            }
        });



        loadDataListPost(mPage, true, mtype);


    }

    private void loadDataListPost(final int pageSize,final boolean isFlash,int type) {


        if (pageSize <= mPageSize) {
            String timestamp = DateUtil.getStringDate();

            String sign = Util.createSign(getActivity(), timestamp, "Order", "myRemindList");

            OkHttpUtils.post()
                    .url(Constant.ORDER_MYMINDLIST)
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

                                         if (isFlash)
                                             fauctionDos = new ArrayList<MyRemind>();
                                     //
                                     //json解析
                                     JSONObject jsonObject = JSONObject.parseObject(response);
                                     int code = jsonObject.getIntValue("code");
                                    // String dj = jsonObject.getString("data");
                                     if (code==0) {
                                         JSONObject data = jsonObject.getJSONObject("data");

                                         mPageSize = jsonObject.getInteger("total_page");
                                         for (int i = 0; i < REQUEST_COUNT; i++) {
                                             int j = i + 1;
                                             JSONObject temp = data.getJSONObject(j + "");
                                             if (temp == null) {
                                                 break;
                                             }

                                             Gson gson = new Gson();
                                             String s = JSON.toJSONString(temp);
                                             MyRemind myHaveFauction = gson.fromJson(s, MyRemind.class);
                                             fauctionDos.add(myHaveFauction);
                                         }


                                         if (isFlash) {
                                             mDataAdapter = new RemindItemDoAdapter(getActivity(), fauctionDos);
                                             xListView.setAdapter(mDataAdapter);
                                             xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                 @Override
                                                 public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                     Intent intent = new Intent();
                                                     intent.setClass(getActivity(), ProductFauctionDetailDoingActivity.class);
                                                     MyRemind fdo = fauctionDos.get(position - 1);
                                                     intent.putExtra("productId", Integer.parseInt(fdo.getId()));
                                                     startActivity(intent);
                                                 }
                                             });
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
        mPage=1;
        loadDataListPost(mPage,true,mtype);
    }

    @Override
    public void onLoadMore() {
        if(mPage<=mPageSize){
            loadDataListPost(mPage,false,mtype);
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
