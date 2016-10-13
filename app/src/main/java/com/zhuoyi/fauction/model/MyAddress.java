package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/6/3.
 */
public class MyAddress {

    /**
     * ret : 200
     * code : 0
     * data : [{"id":"8","uname":"sdfsd","mobile":"23424234243","zip":"22222","area":"9501.002","address":"sfsdfwerwe","province":9500,"city":9501},{"id":"9","uname":"sdfsd","mobile":"23424234243","zip":"22222","area":"9501.002","address":"sfsdfwerwe","province":9500,"city":9501}]
     * num : 1
     */

    private int ret;
    private int code;
    private int num;
    /**
     * id : 8
     * uname : sdfsd
     * mobile : 23424234243
     * zip : 22222
     * area : 9501.002
     * address : sfsdfwerwe
     * province : 9500
     * city : 9501
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
        private String uname;
        private String mobile;
        private String zip;
        private String area;
        private String address;
        private int province;
        private int city;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getProvince() {
            return province;
        }

        public void setProvince(int province) {
            this.province = province;
        }

        public int getCity() {
            return city;
        }

        public void setCity(int city) {
            this.city = city;
        }
    }
}
