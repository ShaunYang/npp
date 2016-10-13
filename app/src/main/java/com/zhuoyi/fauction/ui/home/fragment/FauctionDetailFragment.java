package com.zhuoyi.fauction.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.ProductDetail2;
import com.zhuoyi.fauction.ui.home.adapter.DetailPicAdapter;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;
import com.zhuoyi.fauction.ui.home.adapter.ProDetailRecycleAdapter;
import com.zhuoyi.fauction.utils.ViewUtil;
import com.zhuoyi.fauction.view.ListViewForScrollView;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FauctionDetailFragment extends Fragment {

    private View viewHolder;

    @Bind(R.id.shangpingjianjie)
    TextView shangpingjianjie;
    @Bind(R.id.shangjia) TextView shangjia;
    @Bind(R.id.cunhuodi) TextView cunhuodi;
    @Bind(R.id.zhongliang) TextView zhongliang;
    @Bind(R.id.pingzhongdengji) TextView pingzhongdengji;
    @Bind(R.id.all_recycle)
    RecyclerView allRecycle;

    ViewPager viewPager;


    public static FauctionDetailFragment newInstance(ProductDetail2 productDetail2) {
        FauctionDetailFragment fragment = new FauctionDetailFragment();
        return fragment;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_fauction_detail, container, false);
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
        ProductDetail2 productDetail2= Common.productDetail2;

        if(productDetail2!=null){
            ProductDetail2.DataBean data= productDetail2.getData();
            if(data!=null){
                if(data.getContent()!=null){
                    shangpingjianjie.setText(data.getContent());
                }

                shangjia.setText(data.getFacilitator());
                cunhuodi.setText(data.getAddress());
                zhongliang.setText(data.getNum()+"/"+data.getUnit());
                pingzhongdengji.setText(data.getGrade());
                ProDetailRecycleAdapter detailPicAdapter = new ProDetailRecycleAdapter(getActivity(), data.getPicture());
                //设置布局管理器
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext()){
                    @Override
                    public boolean canScrollVertically() {
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                allRecycle.setLayoutManager(linearLayoutManager);
                allRecycle.setAdapter(detailPicAdapter);
                //setListViewHeightBasedOnChildren(allRecycle);
                //detailPicAdapter.notifyDataSetChanged();
                /*int listViewHeight = ViewUtil.setListViewHeightBasedOnChildren1(allRecycle);
                Logger.i("listViewHeight=====",listViewHeight+"");
                ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                params.height = listViewHeight;
                viewPager.setLayoutParams(params);*/


            }

        }







    }

  /*  public void setListViewHeightBasedOnChildren(ListView listView) {
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
    }*/

    /**
     * 动态的算出ListView实际的LayoutParams
     * 最关键的是算出LayoutParams.height
     */
   /* public ViewGroup.LayoutParams getListViewParams() {
        //通过ListView获取其中的适配器adapter
        ListAdapter listAdapter = allRecycle.getAdapter();

        //声明默认高度为0
        int totalHeight = 0;
        //便利ListView所有的item，累加所有item的高度就是ListView的实际高度
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, allRecycle);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //将累加获取的totalHeight赋值给LayoutParams的height属性
        ViewGroup.LayoutParams params = allRecycle.getLayoutParams();
        params.height = totalHeight + (allRecycle.getDividerHeight() * (listAdapter.getCount() - 1));
        return params;
    }*/

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
}
