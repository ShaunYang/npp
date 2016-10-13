package com.zhuoyi.fauction.socket;

import java.util.List;

/**
 * Created by Administrator on 2016/6/2.
 */
public class PushPo {


    /**
     * enlist_num : 3
     * offer_num : 3
     * end_time : 68天6小时37分51秒
     * delay_num : 0
     * data : [{"status":"领先","user_name":"","price":"122.03","num":"1000","unit":"斤","add_time":"2016-05-30 18:52:38"},{"status":"得拍","user_name":"123213","price":"122.00","num":"1","unit":"斤","add_time":"2016-05-25 14:41:33"},{"status":"得拍","user_name":"","price":"10.00","num":"1000","unit":"斤","add_time":"2016-06-03 09:46:08"}]
     * pd : {"current_price":"10","shoot_type":2,"stock":12999}
     */

    private String enlist_num;
    private String offer_num;
    private String end_time;
    private String delay_num;
    /**
     * current_price : 10
     * shoot_type : 2
     * stock : 12999
     */

    private PdBean pd;
    /**
     * status : 领先
     * user_name :
     * price : 122.03
     * num : 1000
     * unit : 斤
     * add_time : 2016-05-30 18:52:38
     */

    private List<DataBean> data;

    public String getEnlist_num() {
        return enlist_num;
    }

    public void setEnlist_num(String enlist_num) {
        this.enlist_num = enlist_num;
    }

    public String getOffer_num() {
        return offer_num;
    }

    public void setOffer_num(String offer_num) {
        this.offer_num = offer_num;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDelay_num() {
        return delay_num;
    }

    public void setDelay_num(String delay_num) {
        this.delay_num = delay_num;
    }

    public PdBean getPd() {
        return pd;
    }

    public void setPd(PdBean pd) {
        this.pd = pd;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class PdBean {
        private String current_price;
        private int shoot_type;
        private int stock;

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public int getShoot_type() {
            return shoot_type;
        }

        public void setShoot_type(int shoot_type) {
            this.shoot_type = shoot_type;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }

    public static class DataBean {
        private String status;
        private String user_name;
        private String price;
        private String num;
        private String unit;
        private String add_time;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }
    }
}
