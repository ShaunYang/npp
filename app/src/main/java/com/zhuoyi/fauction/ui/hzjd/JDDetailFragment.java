package com.zhuoyi.fauction.ui.hzjd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.JDModel;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.home.adapter.DetailPicAdapter;
import com.zhuoyi.fauction.ui.hzjd.adapter.DetailJDPicAdapter;
import com.zhuoyi.fauction.ui.hzjd.adapter.JDDetailRecycleAdapter;
import com.zhuoyi.fauction.ui.mine.MineFragment;
import com.zhuoyi.fauction.ui.mine.fragment.LoginFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.ListViewForScrollView;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class JDDetailFragment extends Fragment{



    private View viewHolder;
    private XListView xListView;
    private String TAG="JDDetailFragment";

    @Bind(R.id.company_title)
    TextView company_title;
    @Bind(R.id.company_detail)
    TextView company_detail;
    @Bind(R.id.company_lxr)
    TextView company_lxr;
    @Bind(R.id.company_lxdh)
    TextView company_lxdh;
    @Bind(R.id.company_lxdz)
    TextView company_lxdz;
    @Bind(R.id.all_recycle_pic)
    RecyclerView all_recycle_pic;

    List<Category> categoryList;
    public static JDDetailFragment newInstance(String param1, String param2) {
        JDDetailFragment fragment = new JDDetailFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_hzjd_detail, container, false);
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

    @Override
    public void onStart() {
        //initComponent();
        super.onStart();
    }

    @OnClick(R.id.back) void onBackClick() {
        getActivity().getSupportFragmentManager().popBackStack();





    }



    private void initComponent(){
       // Common.home_tab=1;
        initDatas();
        guideMainIndexPost(Common.JD_ID);

        /* mRecyclerView=(RecyclerView) viewHolder.findViewById(R.id.table_item_view);
        //设置布局管理器
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
       *//* //设置适配器
         TableItemAdapter adapter=new TableItemAdapter(getActivity());
         recyclerView.setAdapter(adapter);*//*
        //设置适配器
         mAdapter = new GalleryAdapter(getActivity(), mDatas,mDataString);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);*/
    }

    private void guideMainIndexPost(String id) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Co","view");

        //Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.CO_VIEW)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                .addParams("id",id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                       Gson gson=new Gson();
                        JDModel jdMode = gson.fromJson(response, JDModel.class);
                        if(jdMode!=null){
                            if(jdMode.getCode()==0){
                                if(jdMode.getData()!=null){
                                    company_title.setText(jdMode.getData().getTitle());

                                    company_detail.setText(jdMode.getData().getContent());

                                    company_lxr.setText("联系人:"+jdMode.getData().getUname());

                                    company_lxdh.setText("联系电话:"+jdMode.getData().getMobile());

                                    company_lxdz.setText("联系地址:"+jdMode.getData().getAddr());
                                    //设置布局管理器
                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()){
                                        @Override
                                        public boolean canScrollVertically() {
                                            return false;
                                        }
                                    };
                                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                    all_recycle_pic.setLayoutManager(linearLayoutManager);

                                    JDDetailRecycleAdapter detailPicAdapter = new JDDetailRecycleAdapter(getActivity(), jdMode.getData().getPicture());
                                    all_recycle_pic.setAdapter(detailPicAdapter);
                                   // setListViewHeightBasedOnChildren(all_recycle_pic);
                                }

                            }
                        }


                        // JSONObject data = jsonObject.getJSONObject("data");
                        /*if(data!=null){
                            company_title.setText(data.getString("title"));

                            company_detail.setText(data.getString(""));

                            company_lxr;

                            company_lxdh;

                            company_lxdz;
                        }*/

                    }
                });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }



    //在恢复里及时更新数据
    @Override
    public void onResume() {
       // initComponent();
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

    private void initDatas()
    {
//        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.category_yumi,
//                R.drawable.category_qinggua, R.drawable.category_lajiao, R.drawable.category_sijidou));
//        mDataString=new ArrayList<String>(Arrays.asList("玉米", "青瓜","辣椒","四季豆"));
    }
}
