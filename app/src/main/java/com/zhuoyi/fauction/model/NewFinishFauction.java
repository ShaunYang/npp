package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/6/15.
 */
public class NewFinishFauction {

    /**
     * ret : 200
     * code : 0
     * data : [{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null},{"title":"玉米测试1玉米测试1","num":"1","unit":"斤","user_name":null}]
     */

    private int ret;
    private int code;
    /**
     * title : 玉米测试1玉米测试1
     * num : 1
     * unit : 斤
     * user_name : null
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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String title;
        private String num;
        private String unit;
        private Object user_name;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public Object getUser_name() {
            return user_name;
        }

        public void setUser_name(Object user_name) {
            this.user_name = user_name;
        }
    }
}
