package com.zhuoyi.fauction.model;

/**
 * Created by Administrator on 2016/5/26.
 */
//正在进行商品vo
public class FauctionEnd {

    public String title;
    private String curPrice;
    private String residual_time;
    private String stock;
    private String num;
    private String title_img;
    private String unit;
    private int status;
    public int id;

    public String getTitle_img() {
        return title_img;
    }

    public void setTitle_img(String title_img) {
        this.title_img = title_img;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(String curPrice) {
        this.curPrice = curPrice;
    }

    public String getResidual_time() {
        return residual_time;
    }

    public void setResidual_time(String residual_time) {
        this.residual_time = residual_time;
    }
}
