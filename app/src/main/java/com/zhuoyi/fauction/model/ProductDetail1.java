package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/5/28.
 */
public class ProductDetail1 {

    /**
     * ret : 200
     * code : 0
     * data : {"id":"20","title":"山东聊城莘县优质黄瓜","shoot_price":"1.00","shoot_num":"500","unit":"斤","shoot_time":"2016-05-21 16:00:00","end_time":"2016-07-13 16:00:00","bond":"500","fare":"0.02","delayed":"10","reserve_price":"0.98","cut_price":"0.02","current_price":"0.80","stock":"30000","state":1,"delay_num":"0","picture":[{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-21/574020ada771d.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcc98714.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcddc731.jpg","title":""}],"price_cycle":"0","deal_time":"2016-05-28 14:40:50","login_status":0}
     */

    private int ret;
    private int code;
    /**
     * id : 20
     * title : 山东聊城莘县优质黄瓜
     * shoot_price : 1.00
     * shoot_num : 500
     * unit : 斤
     * shoot_time : 2016-05-21 16:00:00
     * end_time : 2016-07-13 16:00:00
     * bond : 500
     * fare : 0.02
     * delayed : 10
     * reserve_price : 0.98
     * cut_price : 0.02
     * current_price : 0.80
     * stock : 30000
     * state : 1
     * delay_num : 0
     * picture : [{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-21/574020ada771d.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcc98714.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcddc731.jpg","title":""}]
     * price_cycle : 0
     * deal_time : 2016-05-28 14:40:50
     * login_status : 0
     */

    private DataBean data;

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    private int remind;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String title;
        private String shoot_price;
        private String shoot_num;
        private String unit;
        private String shoot_time;
        private String end_time;
        private String bond;
        private String fare;
        private String delayed;
        private String reserve_price;
        private String cut_price;
        private String current_price;
        private String stock;
        private int state;
        private String delay_num;
        private String price_cycle;
        private String deal_time;
        private int login_status;
        private int pay_state;
        private int shoot_state;
        private int pay_type;
        private int type;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getPay_type() {
            return pay_type;
        }

        public void setPay_type(int pay_type) {
            this.pay_type = pay_type;
        }

        public int getPay_state() {
            return pay_state;
        }

        public void setPay_state(int pay_state) {
            this.pay_state = pay_state;
        }

        public int getShoot_state() {
            return shoot_state;
        }

        public void setShoot_state(int shoot_state) {
            this.shoot_state = shoot_state;
        }

        /**
         * pic_name : http://192.168.0.107/Uploads/product/2016-05-21/574020ada771d.jpg
         * title :
         */

        private List<PictureBean> picture;

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

        public String getShoot_price() {
            return shoot_price;
        }

        public void setShoot_price(String shoot_price) {
            this.shoot_price = shoot_price;
        }

        public String getShoot_num() {
            return shoot_num;
        }

        public void setShoot_num(String shoot_num) {
            this.shoot_num = shoot_num;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getShoot_time() {
            return shoot_time;
        }

        public void setShoot_time(String shoot_time) {
            this.shoot_time = shoot_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getBond() {
            return bond;
        }

        public void setBond(String bond) {
            this.bond = bond;
        }

        public String getFare() {
            return fare;
        }

        public void setFare(String fare) {
            this.fare = fare;
        }

        public String getDelayed() {
            return delayed;
        }

        public void setDelayed(String delayed) {
            this.delayed = delayed;
        }

        public String getReserve_price() {
            return reserve_price;
        }

        public void setReserve_price(String reserve_price) {
            this.reserve_price = reserve_price;
        }

        public String getCut_price() {
            return cut_price;
        }

        public void setCut_price(String cut_price) {
            this.cut_price = cut_price;
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

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getDelay_num() {
            return delay_num;
        }

        public void setDelay_num(String delay_num) {
            this.delay_num = delay_num;
        }

        public String getPrice_cycle() {
            return price_cycle;
        }

        public void setPrice_cycle(String price_cycle) {
            this.price_cycle = price_cycle;
        }

        public String getDeal_time() {
            return deal_time;
        }

        public void setDeal_time(String deal_time) {
            this.deal_time = deal_time;
        }

        public int getLogin_status() {
            return login_status;
        }

        public void setLogin_status(int login_status) {
            this.login_status = login_status;
        }

        public List<PictureBean> getPicture() {
            return picture;
        }

        public void setPicture(List<PictureBean> picture) {
            this.picture = picture;
        }

        public static class PictureBean {
            private String pic_name;
            private String title;

            public String getPic_name() {
                return pic_name;
            }

            public void setPic_name(String pic_name) {
                this.pic_name = pic_name;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
