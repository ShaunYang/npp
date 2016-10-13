package com.zhuoyi.fauction.socket;

/**
 * Created by Administrator on 2016/4/25.
 */
public class UrlBean {
    private String url;
    private String uid;

    public UrlBean(String url, String uid) {
        this.url = url;
        this.uid = uid;
    }

    public UrlBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
