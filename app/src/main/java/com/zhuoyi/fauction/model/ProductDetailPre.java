package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/6/1.
 */
public class ProductDetailPre {

    /**
     * ret : 200
     * code : 0
     * data : {"id":"17","title":"成都青白江基地水果玉米","shoot_price":"1.20","shoot_num":"1000","deal_price":"3.00","unit":"斤","shoot_time":"2016-05-21 15:49:00","end_time":"2016-07-29 16:11:00","bond":"800","fare":"0.01","delayed":"10","reserve_price":"1.00","delay_time":"0","cut_price":"0.01","num":"20000","picture":[{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-21/57402049f23a0.jpg","title":""},{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-23/5742bffd7a19e.jpg","title":""},{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-23/5742c00fa9244.jpg","title":""}],"price_cycle":"0","state":2,"deal_time":"2016-06-01 16:30:43","login_status":-1}
     */

    private int ret;
    private int code;
    /**
     * id : 17
     * title : 成都青白江基地水果玉米
     * shoot_price : 1.20
     * shoot_num : 1000
     * deal_price : 3.00
     * unit : 斤
     * shoot_time : 2016-05-21 15:49:00
     * end_time : 2016-07-29 16:11:00
     * bond : 800
     * fare : 0.01
     * delayed : 10
     * reserve_price : 1.00
     * delay_time : 0
     * cut_price : 0.01
     * num : 20000
     * picture : [{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-21/57402049f23a0.jpg","title":""},{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-23/5742bffd7a19e.jpg","title":""},{"pic_name":"http://a.nongpaipai.cn/Uploads/product/2016-05-23/5742c00fa9244.jpg","title":""}]
     * price_cycle : 0
     * state : 2
     * deal_time : 2016-06-01 16:30:43
     * login_status : -1
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
        private String deal_price;
        private String unit;
        private String shoot_time;
        private String end_time;
        private String bond;
        private String fare;
        private String delayed;
        private String reserve_price;
        private String delay_time;
        private String cut_price;
        private String num;
        private String price_cycle;
        private int state;
        private String deal_time;
        private int login_status;




        /**
         * pic_name : http://a.nongpaipai.cn/Uploads/product/2016-05-21/57402049f23a0.jpg
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

        public String getDeal_price() {
            return deal_price;
        }

        public void setDeal_price(String deal_price) {
            this.deal_price = deal_price;
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

        public String getDelay_time() {
            return delay_time;
        }

        public void setDelay_time(String delay_time) {
            this.delay_time = delay_time;
        }

        public String getCut_price() {
            return cut_price;
        }

        public void setCut_price(String cut_price) {
            this.cut_price = cut_price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getPrice_cycle() {
            return price_cycle;
        }

        public void setPrice_cycle(String price_cycle) {
            this.price_cycle = price_cycle;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
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
