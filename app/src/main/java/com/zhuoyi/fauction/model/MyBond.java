package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/6/1.
 */
public class MyBond {


    /**
     * ret : 200
     * code : 0
     * data : [{"bond":"0.01","status":"已成交","title":"玉米测试5","pic_id":"1056","shoot_price":"0.01","num":"10000","unit":"斤","delay_time":"1470040770","id":"70","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"0.01","status":"待退还","title":"玉米测试4","pic_id":"1054","shoot_price":"0.01","num":"10000","unit":"斤","delay_time":"1470039240","id":"69","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"0.01","status":"待退还","title":"玉米测试3","pic_id":"1052","shoot_price":"0.02","num":"10000","unit":"斤","delay_time":"1470039120","id":"68","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"0.01","status":"已成交","title":"玉米测试2","pic_id":"1050","shoot_price":"0.01","num":"10000","unit":"斤","delay_time":"1470038520","id":"67","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"0.01","status":"待退还","title":"玉米商品测试","pic_id":"1026","shoot_price":"0.01","num":"1000","unit":"斤","delay_time":"1470020820","id":"62","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"0.01","status":"已成交","title":"苦瓜（测试2）","pic_id":"1013","shoot_price":"0.01","num":"500","unit":"斤","delay_time":"1469862001","id":"61","pic":"http://o9gjmntyw.bkt.clouddn.com/rtyrtyryt.jpg","shoot_state":0},{"bond":"0.01","status":"已缴纳","title":"苦瓜（测试）","pic_id":"1012","shoot_price":"0.01","num":"100000","unit":"斤","delay_time":"1475215934","id":"60","pic":"http://o9gjmntyw.bkt.clouddn.com/rtyrtyryt.jpg","shoot_state":1},{"bond":"0.01","status":"已成交","title":"建瓯玉米","pic_id":"1000","shoot_price":"0.01","num":"1000","unit":"斤","delay_time":"1469753409","id":"58","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0},{"bond":"800.00","status":"已成交","title":"西瓜","pic_id":"978","shoot_price":"0.75","num":"50000","unit":"斤","delay_time":"1469460658","id":"51","pic":"http://o9gjmntyw.bkt.clouddn.com/QQ图片20160725084231.jpg","shoot_state":0},{"bond":"0.01","status":"已退还","title":"玉米测试","pic_id":"966","shoot_price":"0.01","num":"1000","unit":"斤","delay_time":"1469179080","id":"50","pic":"http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg","shoot_state":0}]
     * total_page : 2
     * page : 1
     */

    private int ret;
    private int code;
    private int total_page;
    private int page;
    /**
     * bond : 0.01
     * status : 已成交
     * title : 玉米测试5
     * pic_id : 1056
     * shoot_price : 0.01
     * num : 10000
     * unit : 斤
     * delay_time : 1470040770
     * id : 70
     * pic : http://o9gjmntyw.bkt.clouddn.com/145484481623240245.jpg
     * shoot_state : 0
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

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String bond;
        private String status;
        private String title;
        private String pic_id;
        private String shoot_price;
        private String num;
        private String unit;
        private String delay_time;
        private String id;
        private String pic;
        private int shoot_state;

        public String getBond() {
            return bond;
        }

        public void setBond(String bond) {
            this.bond = bond;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getShoot_price() {
            return shoot_price;
        }

        public void setShoot_price(String shoot_price) {
            this.shoot_price = shoot_price;
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

        public String getDelay_time() {
            return delay_time;
        }

        public void setDelay_time(String delay_time) {
            this.delay_time = delay_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public int getShoot_state() {
            return shoot_state;
        }

        public void setShoot_state(int shoot_state) {
            this.shoot_state = shoot_state;
        }
    }
}
