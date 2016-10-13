package com.zhuoyi.fauction.ui.category;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.lidroid.xutils.exception.DbException;
import com.squareup.otto.Subscribe;
import com.yintai.DatabaseManager;
import com.yintai.common.util.Logger;
import com.yintai.common.view.DotLayout;
import com.yintai.common.view.NoScrollGridView;
import com.yintai.common.view.SlideShowView;
import com.yintai.common.view.SpanableTextView;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.GalleryAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class CategoryFragment extends Fragment{



    private View viewHolder;
    private List<Integer> mDatas;
    private List<String> mDataString;
    private MyGridView gridview;
    private MyGridAdapter mAdapter;
    private String TAG="HzjdFragment";

    List<Category> categoryList;
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_category, container, false);
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



    private void initComponent(){
        Common.home_tab=2;
        initDatas();
        guideMainIndexPost();

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

    private void guideMainIndexPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Main","index");

        //Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.MAIN_INDEX)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        categoryList = new ArrayList<Category>();
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        if (data.size() > 0) {
                            Category all=new Category();
                            all.setId("-1");
                            all.setTitle("全部");
                            categoryList.add(all);
                            for (int i = 0; i < data.size(); i++) {
                                final Category category = new Category();
                                JSONObject categoryJson = data.getJSONObject(i);
                                category.setId(categoryJson.getString("id"));
                                category.setTitle(categoryJson.getString("title"));
                                JSONObject picJsonObject = categoryJson.getJSONObject("pic");
                                String normal_img = picJsonObject.getString("3");
                                String select_img = picJsonObject.getString("4");
                                category.setSelect_img(select_img);
                                category.setNormal_img(normal_img);

                                categoryList.add(category);

                            }
                        }


                        Common.allCategories = categoryList;
                        gridview = (MyGridView) viewHolder.findViewById(R.id.gridview);
                        mAdapter = new MyGridAdapter(getActivity());
                        mAdapter.setCategories(categoryList);
                        gridview.setAdapter(mAdapter);
                        //

                        //


                        //System.out.println(response);


                        //ToastUtil.showShortToast(GuideActivity.this,response);

                    }
                });
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
