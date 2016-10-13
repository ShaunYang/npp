package com.zhuoyi.fauction.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Apple on 16/5/30.
 */
public class Order {


    /**
     * ret : 200
     * code : 0
     * data : {"id":"30","order_num":"O-160531663171126208","title":"时令果蔬特产农家青椒","num":"900","unit":"斤","price":"10.00","total_price":"9000.00","add_time":"1464666317","pic_id":"222","bond":"300","delivery":"0","pack":"0","commission":"180.00","packing":{"1":"装箱","2":"袋装"},"express":200,"insurance":20,"total":9100,"pic":"http://192.168.0.107/Uploads/product/2016-05-23/57426d508c671.png","receipt":{"id":"8","uname":"sdfsd","mobile":"23424234243","zip":"22222","area":"9501.002","address":"sfsdfwerwe","province":9500,"city":9501}}
     */

    private int ret;
    private int code;
    /**
     * id : 30
     * order_num : O-160531663171126208
     * title : 时令果蔬特产农家青椒
     * num : 900
     * unit : 斤
     * price : 10.00
     * total_price : 9000.00
     * add_time : 1464666317
     * pic_id : 222
     * bond : 300
     * delivery : 0
     * pack : 0
     * commission : 180.00
     * packing : {"1":"装箱","2":"袋装"}
     * express : 200
     * insurance : 20
     * total : 9100
     * pic : http://192.168.0.107/Uploads/product/2016-05-23/57426d508c671.png
     * receipt : {"id":"8","uname":"sdfsd","mobile":"23424234243","zip":"22222","area":"9501.002","address":"sfsdfwerwe","province":9500,"city":9501}
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
        private String order_num;
        private String title;
        private String num;
        private String unit;
        private String price;
        private String total_price;
        private String add_time;
        private String pic_id;
        private String bond;
        private String delivery;
        private String pack;
        private String commission;
        /**
         * 1 : 装箱
         * 2 : 袋装
         */

        private PackingBean packing;
        private int express;
        private int insurance;
        private float total;
        private String pic;
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

        private ReceiptBean receipt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getBond() {
            return bond;
        }

        public void setBond(String bond) {
            this.bond = bond;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public String getPack() {
            return pack;
        }

        public void setPack(String pack) {
            this.pack = pack;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public PackingBean getPacking() {
            return packing;
        }

        public void setPacking(PackingBean packing) {
            this.packing = packing;
        }

        public int getExpress() {
            return express;
        }

        public void setExpress(int express) {
            this.express = express;
        }

        public int getInsurance() {
            return insurance;
        }

        public void setInsurance(int insurance) {
            this.insurance = insurance;
        }

        public float getTotal() {
            return total;
        }

        public void setTotal(float total) {
            this.total = total;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public ReceiptBean getReceipt() {
            return receipt;
        }

        public void setReceipt(ReceiptBean receipt) {
            this.receipt = receipt;
        }

        public static class PackingBean {
            @SerializedName("1")
            private String value1;
            @SerializedName("2")
            private String value2;

            public String getValue1() {
                return value1;
            }

            public void setValue1(String value1) {
                this.value1 = value1;
            }

            public String getValue2() {
                return value2;
            }

            public void setValue2(String value2) {
                this.value2 = value2;
            }
        }

        public static class ReceiptBean {
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
}
