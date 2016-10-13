package com.zhuoyi.fauction.ui.home.event;

/**
 * Created by Apple on 16/5/30.
 */
public class PriceSortEventData {
    int order;

    public PriceSortEventData(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
