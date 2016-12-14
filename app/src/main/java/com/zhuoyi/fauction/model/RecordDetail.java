package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/5/29.
 */
public class RecordDetail {


    /**
     * ret : 200
     * code : 0
     * data : [{"status":"得拍","user_name":"","price":"10.00","num":"1000","unit":"斤","add_time":"2016-05-31 11:30:24"}]
     * enlist_num : 2
     * offer_num : 1
     * end_time : 40天21小时57分41秒
     * delay_num : 0
     * pd : {"current_price":"10.00","shoot_type":0,"stock":"29000"}
     */

    private int ret;
    private int code;
    private String enlist_num;
    private String offer_num;
    private String end_time;
    private String delay_num;
    private String count_down;
    private int shoot_state;
    private String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getShoot_state() {
        return shoot_state;
    }

    public void setShoot_state(int shoot_state) {
        this.shoot_state = shoot_state;
    }

    /**
     * current_price : 10.00
     * shoot_type : 0
     * stock : 29000
     */

    private PdBean pd;

    public String getCount_down() {
        return count_down;
    }

    public void setCount_down(String count_down) {
        this.count_down = count_down;
    }

    /**
     * status : 得拍
     * user_name :
     * price : 10.00
     * num : 1000
     * unit : 斤
     * add_time : 2016-05-31 11:30:24
     */



    private List<DataBean> data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
        private String stock;
        private String num;

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

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
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
