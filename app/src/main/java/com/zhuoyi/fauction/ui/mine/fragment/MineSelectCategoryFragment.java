package com.zhuoyi.fauction.ui.mine.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import okhttp3.Call;


public class MineSelectCategoryFragment extends Fragment{



    private View viewHolder;
    private List<Integer> mDatas;
    private List<String> mDataString;
    private MyGridView gridview;
    private MyGridAdapter mAdapter;
    private String TAG="HzjdFragment";

    List<Category> categoryList;
    public static MineSelectCategoryFragment newInstance(String param1, String param2) {
        MineSelectCategoryFragment fragment = new MineSelectCategoryFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.mine_category, container, false);
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

        initDatas();
        //guideMainIndexPost();
        if(Common.categories!=null){
            gridview=(MyGridView) viewHolder.findViewById(R.id.gridview);
            mAdapter=new MyGridAdapter(getActivity());
            mAdapter.setCategories(Common.categories);
            gridview.setAdapter(mAdapter);
        }



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
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG,response);
                        categoryList=new ArrayList<Category>();
                        JSONObject jsonObject=JSONObject.parseObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        for(int i=0;i<data.size();i++){
                            final Category category=new Category();
                            JSONObject categoryJson=data.getJSONObject(i);
                            category.setId(categoryJson.getString("id"));
                            category.setTitle(categoryJson.getString("title"));
                            JSONObject picJsonObject=categoryJson.getJSONObject("pic");
                            String normal_img=picJsonObject.getString("3");
                            String select_img=picJsonObject.getString("4");
                            category.setSelect_img(select_img);
                            category.setNormal_img(normal_img);

                            categoryList.add(category);

                        }

                        Common.allCategories=categoryList;
                        gridview=(MyGridView) viewHolder.findViewById(R.id.gridview);
                        mAdapter=new MyGridAdapter(getActivity());
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
