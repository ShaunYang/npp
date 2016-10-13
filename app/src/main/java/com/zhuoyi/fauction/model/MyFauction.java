package com.zhuoyi.fauction.model;

/**
 * Created by Apple on 16/6/1.
 */
public class MyFauction {


    /**
     * id : 37
     * aid : 19
     * order_num : O-160531800069276029
     * title : 绿色水果黄瓜（0.8元/斤）
     * status : 交易关闭
     * state : 0
     * add_time : 2016-05-31 15:33
     * price : 10.00
     * unit : 斤
     * num : 400
     * pay_time : 0
     * total : 4000.00
     * bond : 0
     * express : 200.00
     * insurance : 20.00
     * pic : http://a.nongpaipai.cn/Uploads/product/2016-05-21/5740208c2530c.jpg
     */

    private String id;
    private String aid;
    private String order_num;
    private String title;
    private String status;
    private String state;
    private String add_time;
    private String price;
    private String unit;
    private String num;
    private String pay_time;
    private String total;
    private String bond;
    private String express;
    private String insurance;
    private String pic;
    private String pay_type;

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

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPay_time() {
        return pay_time;
    }

    public void setPay_time(String pay_time) {
        this.pay_time = pay_time;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
