package com.zhuoyi.fauction.ui.other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.Mounth;
import com.zhuoyi.fauction.model.MounthPo;
import com.zhuoyi.fauction.model.MyAddress;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryEndTabActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryPreTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.CategoryDoingAdapter;
import com.zhuoyi.fauction.ui.category.fragment.CategoryDoListFragment;
import com.zhuoyi.fauction.ui.home.event.PriceSortEventData;
import com.zhuoyi.fauction.ui.other.adapter.AccountDetailListAdapter;
import com.zhuoyi.fauction.ui.other.adapter.AddressListAdapter;
import com.zhuoyi.fauction.ui.other.adapter.MouthGridViewTopAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhuoyi.fauction.view.SelectPopWindow;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AccountDetailActivity extends BaseActivity implements XListView.IXListViewListener{

    @Bind(R.id.main) RelativeLayout main;
    @Bind(R.id.top_bar) RelativeLayout topBar;
    @Bind(R.id.mounth_title) TextView mounthTitle;
//    @Bind(R.id.mounth_select) ImageView mounthSelect;
    @Bind(R.id.gridview)
    GridView gridview;
    @Bind(R.id.account_list)
    XListView accountList;
    ArrayList<Mounth> mounths=new ArrayList<Mounth>();
    ArrayList<MounthPo> mounthPos;

    AccountDetailListAdapter accountDetailListAdapter;


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

    int curMounth=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initComponent(Bundle bundle) {
        setContentView(R.layout.activity_account_detail);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        for(int i=0;i<12;i++){
            Mounth mounth=new Mounth();
            mounth.setNum(i+1);
            mounths.add(mounth);
        }
        gridview.setAdapter(new MouthGridViewTopAdapter(AccountDetailActivity.this, mounths));
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                curMounth = position + 1;
                Logger.i(TAG + "45646456456====", curMounth+"");
                if(mounthPos!=null){
                    mounthPos.clear();
                    if(accountDetailListAdapter!=null){
                        accountDetailListAdapter.notifyDataSetChanged();
                        accountList.setAdapter(accountDetailListAdapter);
                    }

                }
                mPage=1;
                loadDataListPost(mPage,true,curMounth);
                mounthTitle.setText("2016年"+curMounth+"月");

            }
        });

        accountList.setPullLoadEnable(true);
        accountList.setXListViewListener(this);

        loadDataListPost(mPage,true,7);


    }

    private void loadDataListPost(final int pageSize,final boolean isFlash,int mouth){





                    String timestamp= DateUtil.getStringDate();

                    String sign= Util.createSign(AccountDetailActivity.this,timestamp,"Wallet","info");

                    OkHttpUtils.post()
                            .url(Constant.WALLET_INFO)
                            .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(AccountDetailActivity.this))
                            .addParams("timestamp", timestamp)
                            .addParams("client_id", ConfigUserManager.getUnicodeIEME(AccountDetailActivity.this))
                            .addParams("user_id", ConfigUserManager.getUserId(AccountDetailActivity.this))
                            .addParams("page", String.valueOf(pageSize))
                            .addParams("year", 2016 + "")
                            .addParams("month",mouth+"")
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(String response) {

                                    Logger.i(TAG + "45646456456====", response);
                                    if (isFirst)
                                        (AccountDetailActivity.this).dismiss();

                                    if (isFlash)
                                        mounthPos = new ArrayList<MounthPo>();
                                    //json解析
                                    JSONObject jsonObject = JSONObject.parseObject(response);
                                    int code = jsonObject.getIntValue("code");
                                    if (code==0) {
                                        com.alibaba.fastjson.JSONArray data = jsonObject.getJSONArray("data");
                                        mPageSize = jsonObject.getInteger("total_page");
                                        for (int i = 0; i < data.size(); i++) {

                                            JSONObject temp = data.getJSONObject(i);
                                            if (temp == null) {
                                                break;
                                            }

                                            MounthPo mounthPo = new MounthPo();
                                            mounthPo.setData(temp.getString("date"));
                                            mounthPo.setPrice(temp.getString("price"));
                                            mounthPo.setType(temp.getString("type"));
                                            mounthPos.add(mounthPo);

                                        }


                                        if (isFlash) {
                                            accountDetailListAdapter = new AccountDetailListAdapter(AccountDetailActivity.this, mounthPos);
                                            accountList.setAdapter(accountDetailListAdapter);
                                        } else {
                                            accountDetailListAdapter.notifyDataSetChanged();
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

    @OnClick(R.id.back) void onBackClick() {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        mPage=1;
        loadDataListPost(mPage,true,curMounth);
    }

    @Override
    public void onLoadMore() {
        if(mPage<=mPageSize){
            loadDataListPost(mPage,false,curMounth);
        }else{
            accountList.stopRefresh();
            accountList.stopLoadMore();
            accountList.mFootTextView.setText(Common.nonextpageText);

        }

    }

    private void onLoad() {
        accountList.stopRefresh();
        accountList.stopLoadMore();
        accountList.setRefreshTime("刚刚");
    }

}
