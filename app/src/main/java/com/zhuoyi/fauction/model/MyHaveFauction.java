package com.zhuoyi.fauction.model;

/**
 * Created by Apple on 16/5/31.
 */
public class MyHaveFauction {


    /**
     * id : 15
     * title : 精选优质西红柿
     * current_price : 10.00
     * stock : 98000
     * status : 已拍得
     * shoot_type : 1
     * num : 2000
     * deal_time : 2016-08-25 10:01
     * pay_time : 2天2分27秒
     * price : 10.00
     * unit : 斤
     * pay_state : 0
     * pic : http://192.168.0.107/Uploads/product/2016-05-21/573fc191c2305.png
     */

    private String id;
    private String title;
    private String current_price;
    private String stock;
    private String status;
    private int shoot_type;
    private String num;
    private String deal_time;
    private String pay_time;
    private String price;
    private String unit;
    private String pay_state;
    private String pic;
    private String pay_type;
    private String oid;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShoot_type() {
        return shoot_type;
    }

    public void setShoot_type(int shoot_type) {
        this.shoot_type = shoot_type;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(String deal_time) {
        this.deal_time = deal_time;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPay_state() {
        return pay_state;
    }

    public void setPay_state(String pay_state) {
        this.pay_state = pay_state;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
