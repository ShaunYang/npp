package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/5/28.
 */
public class ProductDetail2 {


    /**
     * ret : 200
     * code : 0
     * data : {"id":"20","num":"30000","unit":"斤","content":"","facilitator":"山东1","address":"山东","area":0,"grade":"A级","picture":[{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-21/574020ada771d.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcc98714.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcddc731.jpg","title":""}],"province":0,"city":"0"}
     */

    private int ret;
    private int code;
    /**
     * id : 20
     * num : 30000
     * unit : 斤
     * content :
     * facilitator : 山东1
     * address : 山东
     * area : 0
     * grade : A级
     * picture : [{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-21/574020ada771d.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcc98714.jpg","title":""},{"pic_name":"http://192.168.0.107/Uploads/product/2016-05-23/5742bfcddc731.jpg","title":""}]
     * province : 0
     * city : 0
     */

    private DataBean data;

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
        private String num;
        private String unit;
        private String content;
        private String facilitator;
        private String address;
        private String area;
        private String grade;
        private String province;
        private String city;
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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getFacilitator() {
            return facilitator;
        }

        public void setFacilitator(String facilitator) {
            this.facilitator = facilitator;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }



        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
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
