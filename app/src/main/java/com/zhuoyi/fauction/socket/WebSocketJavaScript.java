package com.zhuoyi.fauction.socket;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.zhuoyi.fauction.R;

import java.net.URLDecoder;

/**
 * Created by Administrator on 2016/4/25.
 */
public class WebSocketJavaScript {

    private WebView webview;
    // 使用一个handler来处理加载事件
    private Handler handler;

    UrlBean urlBean;
    Context mContext;


    public WebSocketJavaScript(Context context, Handler handler,UrlBean urlBean) {
        this.handler = handler;
        webview = (WebView) ((Activity) context).findViewById(R.id.websocket);
        this.urlBean=urlBean;
        mContext=context;

    }

    @JavascriptInterface
    public void toastMessage(String message) {
        //取到数据做解析展示
        Log.i("message",message);
        Log.i("decode_message", URLDecoder.decode(message));
        Toast.makeText(mContext, URLDecoder.decode(message), Toast.LENGTH_LONG).show();


    }


    /*
     * java调用显示网页，异步
     */
    @JavascriptInterface
    public void show() {
        handler.post(new Runnable() {
            public void run() {
                // 重要：url的生成,传递数据给网页

                String json = "{\"url\":\""+urlBean.getUrl()+"\",\"uid\":\""+urlBean.getUid()+"\"}";
                System.out.println("json="+json);
                String url = "javascript:insertData('" + json + "')";
                webview.loadUrl(url);
            }
        });
    }
}
