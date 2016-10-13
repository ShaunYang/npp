package com.zhuoyi.fauction.ui.home.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.socket.UrlBean;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FauctionKnowFragment extends Fragment {

    private View viewHolder;
    @Bind(R.id.webview)
    WebView webview;

    ViewPager viewPager;
    public static FauctionKnowFragment newInstance(String param1, String param2) {
        FauctionKnowFragment fragment = new FauctionKnowFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.faction_know, container, false);
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

    public ViewPager getViewPager() {
        return viewPager;
    }

    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void initComponent(){
        // 加载页面

        //webview.setWebChromeClient(new WebChromeClient());
        // webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 允许JavaScript执行
        webview.getSettings().setJavaScriptEnabled(true);
        // webView.addJavascriptInterface(new Contact(), "contact");
        webview.getSettings().setPluginState(WebSettings.PluginState.ON);
        webview.getSettings().setAllowFileAccess(true);

        // 加载index.html
        webview.loadUrl("http://api.nongpaipai.cn/v1/new/index/id/3");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                int measuredHeight = webview.getMeasuredHeight();
                /*ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                params.height = measuredHeight;
                viewPager.setLayoutParams(params);*/
                super.onPageFinished(view, url);
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
}
