package com.zhuoyi.fauction.model;

/**
 * Created by Apple on 16/6/8.
 */
public class OrderDetail {

    /**
     * ret : 200
     * code : 0
     * data : {"id":"76","status":"0","order_num":"O-160606852775956338","commission":"200.00","title":"山东烟台特产优质豇豆","add_time":"2016-06-06 11:54","price":"10.00","unit":"斤","num":"1000","total":"10000.00","total_price":"10000.00","express":"200.00","insurance":"20.00","aid":"32","pic_id":"206","pay_type":"0","receipt_id":"9","pay_time":"1970-01-01 08:00","deliver_time":"1970-01-01 08:00","pay_state":"0","state":"交易关闭","pay_time_count":"0","bond":0,"pay_type_cn":"未支付","pic":"http://a.nongpaipai.cn/Uploads/product/2016-05-23/574263ff90343.png","receipt":{"id":"10","uname":"小燕子","mobile":"18859162787","zip":"22222","area":"9501.003","address":"微信哦破坏","province":9500,"city":9501}}
     */

    private int ret;
    private int code;
    /**
     * id : 76
     * status : 0
     * order_num : O-160606852775956338
     * commission : 200.00
     * title : 山东烟台特产优质豇豆
     * add_time : 2016-06-06 11:54
     * price : 10.00
     * unit : 斤
     * num : 1000
     * total : 10000.00
     * total_price : 10000.00
     * express : 200.00
     * insurance : 20.00
     * aid : 32
     * pic_id : 206
     * pay_type : 0
     * receipt_id : 9
     * pay_time : 1970-01-01 08:00
     * deliver_time : 1970-01-01 08:00
     * pay_state : 0
     * state : 交易关闭
     * pay_time_count : 0
     * bond : 0
     * pay_type_cn : 未支付
     * pic : http://a.nongpaipai.cn/Uploads/product/2016-05-23/574263ff90343.png
     * receipt : {"id":"10","uname":"小燕子","mobile":"18859162787","zip":"22222","area":"9501.003","address":"微信哦破坏","province":9500,"city":9501}
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
        private String status;
        private String order_num;
        private String commission;
        private String title;
        private String add_time;
        private String price;
        private String unit;
        private String num;
        private String total;
        private String total_price;
        private String express;
        private String insurance;
        private String aid;
        private String pic_id;
        private String pay_type;
        private String receipt_id;
        private String pay_time;
        private String deliver_time;
        private String pay_state;
        private String state;
        private String pay_time_count;
        private String bond;
        private String pay_type_cn;
        private String pic;
        private String packing;
        private String logistics;
        private String voucher_num;
        private String voucher_price;

        public String getVoucher_num() {
            return voucher_num;
        }

        public void setVoucher_num(String voucher_num) {
            this.voucher_num = voucher_num;
        }

        public String getVoucher_price() {
            return voucher_price;
        }

        public void setVoucher_price(String voucher_price) {
            this.voucher_price = voucher_price;
        }

        public String getPacking() {
            return packing;
        }

        public void setPacking(String packing) {
            this.packing = packing;
        }

        public String getLogistics() {
            return logistics;
        }

        public void setLogistics(String logistics) {
            this.logistics = logistics;
        }

        /**
         * id : 10
         * uname : 小燕子
         * mobile : 18859162787
         * zip : 22222
         * area : 9501.003
         * address : 微信哦破坏
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getCommission() {
            return commission;
        }

        public void setCommission(String commission) {
            this.commission = commission;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getExpress() {
            return express;
        }

        public void setExpress(String express) {
            this.express = express;
        }

        public String getInsurance() {
            return insurance;
        }

        public void setInsurance(String insurance) {
            this.insurance = insurance;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getReceipt_id() {
            return receipt_id;
        }

        public void setReceipt_id(String receipt_id) {
            this.receipt_id = receipt_id;
        }

        public String getPay_time() {
            return pay_time;
        }

        public void setPay_time(String pay_time) {
            this.pay_time = pay_time;
        }

        public String getDeliver_time() {
            return deliver_time;
        }

        public void setDeliver_time(String deliver_time) {
            this.deliver_time = deliver_time;
        }

        public String getPay_state() {
            return pay_state;
        }

        public void setPay_state(String pay_state) {
            this.pay_state = pay_state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPay_time_count() {
            return pay_time_count;
        }

        public void setPay_time_count(String pay_time_count) {
            this.pay_time_count = pay_time_count;
        }

        public String getBond() {
            return bond;
        }

        public void setBond(String bond) {
            this.bond = bond;
        }

        public String getPay_type_cn() {
            return pay_type_cn;
        }

        public void setPay_type_cn(String pay_type_cn) {
            this.pay_type_cn = pay_type_cn;
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
