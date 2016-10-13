package com.zhuoyi.fauction.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;

import butterknife.ButterKnife;


public class FauctionStepFragment extends Fragment {

    private View viewHolder;
    private RecyclerView allRecycle;
    ViewPager viewPager;
    public static FauctionStepFragment newInstance(String param1, String param2) {
        FauctionStepFragment fragment = new FauctionStepFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_fauction_step, container, false);
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
      /*  allRecycle = (RecyclerView) viewHolder.findViewById(R.id.all_recycle);

        //设置布局管理器
        allRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        //设置适配器
        HomeTabItemAdapter adapter=new HomeTabItemAdapter(getActivity());
        allRecycle.setAdapter(adapter);*/

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
