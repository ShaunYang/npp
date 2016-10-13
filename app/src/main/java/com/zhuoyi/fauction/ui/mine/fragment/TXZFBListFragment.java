package com.zhuoyi.fauction.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.alibaba.fastjson.JSONObject;
import com.cundong.recyclerview.HeaderAndFooterRecyclerViewAdapter;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.MyAccount;
import com.zhuoyi.fauction.model.MyAddress;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.event.PriceSortEventData;
import com.zhuoyi.fauction.ui.mine.activity.AddAcountZFBaoActivity;
import com.zhuoyi.fauction.ui.mine.adapter.ZfbListAdapter;
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


public class TXZFBListFragment extends Fragment {

    private View viewHolder;

    String TAG="TXZFBListFragment";
    ZfbListAdapter mDataAdapter;

    @Bind(R.id.address_list)
    ListView accountList;


    View defaultAddressView;

    View recordPreAddress;





    public static TXZFBListFragment newInstance(String param1, String param2) {
        TXZFBListFragment fragment = new TXZFBListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_zfbaolist_tx, container, false);
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




        loadDataListPost();





    }













    private void loadDataListPost(){


                    String timestamp= DateUtil.getStringDate();

                    String sign= Util.createSign(getActivity(),timestamp,"Wallet","accountList");

                    OkHttpUtils.post()
                            .url(Constant.WALLET_ACCOUNTLIST)
                            .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                            .addParams("timestamp", timestamp)
                            .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                            .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                            .addParams("type", 1+"")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(String response) {

                                    Logger.i(TAG + "45646456456====", response);
                                    //json解析
                                    //显示列表
                                    JSONObject jsonObject = JSONObject.parseObject(response);
                                    int code = jsonObject.getIntValue("code");
                                    if(code==0){
                                        Gson gson = new Gson();
                                        final MyAccount myAccount = gson.fromJson(response, MyAccount.class);
                                        accountList.setAdapter(new ZfbListAdapter(getActivity(),myAccount.getData(),accountList));
                                        accountList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                if(recordPreAddress!=null){
                                                    recordPreAddress.findViewById(R.id.account_sel).setVisibility(View.GONE);
                                                }

                                                view.findViewById(R.id.account_sel).setVisibility(View.VISIBLE);
                                                recordPreAddress=view;
                                                Common.accountType=1;
                                                Common.accountId=myAccount.getData().get(position).getId();
                                            }
                                        });
                                    }


                                }

                            });





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



    @OnClick(R.id.add_addaccount) void onAddAddaccountClick() {
        Intent intent=new Intent(getActivity(), AddAcountZFBaoActivity.class);
        startActivity(intent);
    }

}
