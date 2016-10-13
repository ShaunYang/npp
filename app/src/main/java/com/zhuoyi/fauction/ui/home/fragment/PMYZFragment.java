package com.zhuoyi.fauction.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.home.activity.HomeTabActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailPreActivity;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabPMYZAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;


public class PMYZFragment extends Fragment  implements View.OnClickListener,XListView.IXListViewListener{

    private View viewHolder;
    private XListView xListView;
    HomeTabPMYZAdapter adapter;

    int mPageSize=10;
    boolean isFirst=true;

    /**当前页*/
    private int mPage = 1;

    List<FauctionDo> fauctionDos;

    /**每一页展示多少条数据*/
    private static  int REQUEST_COUNT = 10;
    public static PMYZFragment newInstance(String param1, String param2) {
        PMYZFragment fragment = new PMYZFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_home_tab_all, container, false);
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
                intent.setClass(getActivity(), ProductFauctionDetailPreActivity.class);
                //FauctionDo fdo=fauctionDos.get(arg2);
                intent.putExtra("productId",fauctionDos.get(arg2-1).getId());
                getActivity().startActivity(intent);


            }
        });
        loadDataListPost(mPage,true);

    }

    private void loadDataListPost(final int pageSize,final boolean isFlash){


        if(pageSize<=mPageSize){
            String timestamp= DateUtil.getStringDate();

            String sign= Util.createSign(getActivity(),timestamp,"Main","preview");

            OkHttpUtils.post()
                    .url(Constant.MAIN_PREVIEW)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                    .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                    .addParams("page",String.valueOf(pageSize))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e) {

                        }

                        @Override
                        public void onResponse(String response) {

                            Logger.i("45646456456====",response);
                            if(isFirst)
                                ((HomeTabActivity)getActivity()).dismiss();

                            if(isFlash)
                                fauctionDos = new ArrayList<FauctionDo>();
                            //json解析
                            JSONObject jsonObject=JSONObject.parseObject(response);
                            int code = jsonObject.getIntValue("code");
                            if(code==0){
                                JSONObject data = jsonObject.getJSONObject("data");
                                if(data!=null){
                                    mPageSize=jsonObject.getInteger("total_page");
                                    for(int i=0;i<REQUEST_COUNT;i++){
                                        int j=i+1;
                                        JSONObject temp=data.getJSONObject(j+"");
                                        if(temp==null){
                                            break;
                                        }

                                        FauctionDo fauctionDo=new FauctionDo();
                                        fauctionDo.setCurPrice(temp.getString("shoot_price"));
                                        fauctionDo.setId(temp.getInteger("id"));
                                        fauctionDo.setTitle(temp.getString("title"));
                                        fauctionDo.setResidual_time(temp.getString("shoot_time"));
                                        fauctionDo.setNum(temp.getString("num"));
                                        fauctionDo.setUnit(temp.getString("unit"));
                                        //fauctionDo.setStatus(temp.getInteger("status"));
                                        // fauctionDo.setStock(temp.getString("stock"));
                                        fauctionDo.setTitle_img(temp.getString("pic"));
                                        fauctionDos.add(fauctionDo);
                                    }


                                    if(isFlash){
                                        adapter=new HomeTabPMYZAdapter(getActivity(), fauctionDos);
                                        xListView.setAdapter(adapter);
                                    }else{
                                        adapter.notifyDataSetChanged();
                                    }
                                    onLoad();
                                    // mStartNo=startNo+pageSize;
                                    //页数自增1
                                    mPage++;
                                    isFirst=false;
                                }


                            }








                        }
                    });
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
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mPage=1;
        loadDataListPost(mPage,true);
    }

    @Override
    public void onLoadMore() {
        if(mPage<=mPageSize){
            loadDataListPost(mPage,false);
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
