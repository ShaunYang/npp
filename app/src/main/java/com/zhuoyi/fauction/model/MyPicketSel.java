package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public class MyPicketSel {

    /**
     * ret : 200
     * code : 0
     * data : [{"id":"29","num":"88A9F7EC6F40E1D6B761FDC41C7BAC","type":"2","price":"30.00","valid_price":"30","valid_time":"2016-12-25","status":"2","state":1,"type_name":"货款抵用券"},{"id":"28","num":"88A9F7EC6F40E1D6B761FDC41C7BAC","type":"1","price":"20.00","valid_price":"20","valid_time":"2016-12-25","status":"2","state":1,"type_name":"货款抵用券"},{"id":"27","num":"0C66665006ACEF33EBE10B7E7DBE28","type":"3","price":"300.00","valid_price":"5000","valid_time":"2018-09-28","status":"1","state":1,"type_name":"通用券"},{"id":"26","num":"B53FFDB796B5D3CAC790D101BD608B","type":"3","price":"300.00","valid_price":"5000","valid_time":"2018-09-28","status":"1","state":1,"type_name":"通用券"},{"id":"25","num":"35A5E3F5A300F713A273090468DEFE","type":"3","price":"300.00","valid_price":"5000","valid_time":"2018-09-28","status":"1","state":1,"type_name":"通用券"},{"id":"24","num":"4A0B0494D1817F119833E54C44BDB0","type":"3","price":"300.00","valid_price":"5000","valid_time":"2018-09-28","status":"1","state":1,"type_name":"通用券"},{"id":"23","num":"83F31741A7D1934A77D6CEB8459CC5","type":"3","price":"300.00","valid_price":"5000","valid_time":"2018-09-28","status":"1","state":1,"type_name":"通用券"},{"id":"22","num":"466CC9F2642D048D96C7BB5A3DBB60","type":"2","price":"30.00","valid_price":"30","valid_time":"2016-12-25","status":"2","state":1,"type_name":"货款抵用券"},{"id":"21","num":"466CC9F2642D048D96C7BB5A3DBB60","type":"1","price":"20.00","valid_price":"20","valid_time":"2016-12-25","status":"2","state":1,"type_name":"货款抵用券"}]
     * total_page : 0
     * page : 1
     */

    private int ret;
    private int code;
    private int num;
    /**
     * id : 29
     * num : 88A9F7EC6F40E1D6B761FDC41C7BAC
     * type : 2
     * price : 30.00
     * valid_price : 30
     * valid_time : 2016-12-25
     * status : 2
     * state : 1
     * type_name : 货款抵用券
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String num;
        private String type;
        private String price;
        private String valid_price;
        private String valid_time;
        private String status;
        private int state;
        private String type_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getValid_price() {
            return valid_price;
        }

        public void setValid_price(String valid_price) {
            this.valid_price = valid_price;
        }

        public String getValid_time() {
            return valid_time;
        }

        public void setValid_time(String valid_time) {
            this.valid_time = valid_time;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }
}
