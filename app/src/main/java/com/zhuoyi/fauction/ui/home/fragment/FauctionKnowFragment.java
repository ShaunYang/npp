package com.zhuoyi.fauction.ui.home.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.socket.UrlBean;
import com.zhuoyi.fauction.ui.home.adapter.HomeTabItemAdapter;
import com.zhuoyi.fauction.view.CustomViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;

@SuppressLint("ValidFragment")
public class FauctionKnowFragment extends Fragment {

    private View viewHolder;
    @Bind(R.id.webview)
    WebView webview;

    CustomViewPager viewPager;



    public FauctionKnowFragment(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.faction_know, container, false);
            ButterKnife.bind(this, viewHolder);
            viewPager.setObjectForPosition(viewHolder,1);
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

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    private void initComponent(){
        // 加载页面
       // WebChromeClient webChromeClient = new WebChromeClient();

       // webview.setWebChromeClient(new WebChromeClient());
        webview.setWebViewClient(new WebViewClient());

        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 允许JavaScript执行
        webview.getSettings().setJavaScriptEnabled(true);
        // webView.addJavascriptInterface(new Contact(), "contact");
       // webview.getSettings().setPluginState(WebSettings.PluginState.ON);
       // webview.getSettings().setAllowFileAccess(true);

        // 加载index.html
        webview.loadUrl(Constant.PRODUCT_KNOW+"3");
        /*webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                int measuredHeight = 200;
                Log.i("measuredHeight---",measuredHeight+"");
                ViewGroup.LayoutParams params = viewPager.getLayoutParams();
                params.height = measuredHeight;
                viewPager.setLayoutParams(params);
                super.onPageFinished(view, url);
            }
        });*/






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
        if (webview != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webview.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webview);
            }

            webview.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webview.getSettings().setJavaScriptEnabled(false);
            webview.clearHistory();
            webview.clearView();
            webview.removeAllViews();

            try {
                webview.destroy();
            } catch (Throwable ex) {

            }
        }
        viewPager.removeAllViews();
        viewPager=null;
        super.onDestroy();
    }
}
