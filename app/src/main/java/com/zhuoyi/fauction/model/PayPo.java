package com.zhuoyi.fauction.model;

/**
 * Created by Apple on 16/6/7.
 */
public class PayPo {

    /**
     * ret : 200
     * code : 0
     * data : {"order_num":"O-160607903242585108","aid":"17","title":"成都青白江基地水果玉米","receipt_id":"7","total":"0.01","type":"2","pay_state":"支付成功","pay_type":"支付宝","receipt":{"id":"7","uname":"345","mobile":"18859162786","zip":"22222","area":"9502.002","address":"sdf","province":9500,"city":9502}}
     */

    private int ret;
    private int code;
    /**
     * order_num : O-160607903242585108
     * aid : 17
     * title : 成都青白江基地水果玉米
     * receipt_id : 7
     * total : 0.01
     * type : 2
     * pay_state : 支付成功
     * pay_type : 支付宝
     * receipt : {"id":"7","uname":"345","mobile":"18859162786","zip":"22222","area":"9502.002","address":"sdf","province":9500,"city":9502}
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
        private String order_num;
        private String aid;
        private String id;
        private String title;
        private String receipt_id;
        private String total;
        private String type;
        private String pay_state;
        private String pay_type;
        private String supplier_id;



        /**
         * id : 7
         * uname : 345
         * mobile : 18859162786
         * zip : 22222
         * area : 9502.002
         * address : sdf
         * province : 9500
         * city : 9502
         */

        private ReceiptBean receipt;

        public String getOrder_num() {
            return order_num;
        }

        public void setOrder_num(String order_num) {
            this.order_num = order_num;
        }

        public String getAid() {
            return aid;
        }

        public void setAid(String aid) {
            this.aid = aid;
        }

        public String getTitle() {
            return title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getReceipt_id() {
            return receipt_id;
        }

        public void setReceipt_id(String receipt_id) {
            this.receipt_id = receipt_id;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPay_state() {
            return pay_state;
        }

        public void setPay_state(String pay_state) {
            this.pay_state = pay_state;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(String supplier_id) {
            this.supplier_id = supplier_id;
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
