package com.zhuoyi.fauction.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
//正在进行商品vo
public class PackDo {


    /**
     * ret : 200
     * code : 0
     * data : [{"id":"1","title":"精品箱装","type":"1","pic_id":"998","price":"5.00","num":"50-100斤","case":"1斤","spec":"100*50*200CM","pic":"http://o9gjmntyw.bkt.clouddn.com/sadasdasdasd.jpg"}]
     */

    private int ret;
    private int code;
    /**
     * id : 1
     * title : 精品箱装
     * type : 1
     * pic_id : 998
     * price : 5.00
     * num : 50-100斤
     * case : 1斤
     * spec : 100*50*200CM
     * pic : http://o9gjmntyw.bkt.clouddn.com/sadasdasdasd.jpg
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
        private String id;
        private String title;
        private String type;
        private String pic_id;
        private String price;
        private String num;
        @SerializedName("case")
        private String caseX;
        private String spec;
        private String pic;

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

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPic_id() {
            return pic_id;
        }

        public void setPic_id(String pic_id) {
            this.pic_id = pic_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getCaseX() {
            return caseX;
        }

        public void setCaseX(String caseX) {
            this.caseX = caseX;
        }

        public String getSpec() {
            return spec;
        }

        public void setSpec(String spec) {
            this.spec = spec;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }
}
