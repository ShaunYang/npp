package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Administrator on 2016/9/9.
 */
public class JDModel {


    /**
     * ret : 200
     * code : 0
     * data : {"base":"501.001","title":"闽坤果蔬交易市场","uname":"陈道光 ","mobile":"+86-0599-3599084 ","addr":"福建省建瓯市东峰镇万宁果蔬交易市场","picture":[{"pic_name":"http://o9gjmntyw.bkt.clouddn.com/b5cae893ee5c57949a9d74ad5c204234","title":""},{"pic_name":"http://o9gjmntyw.bkt.clouddn.com/38b5561fe88ebb201d662ac30b323d09","title":""}],"content":"  闽坤果蔬交易市场坐落在建瓯市，东峰镇坤口大棚蔬菜生产基地。距离市区31公里，省道204线从此穿过。市场占得面积14000平方米，是目前闽北乡镇最大的果蔬交易市场。"}
     */

    private int ret;
    private int code;
    /**
     * base : 501.001
     * title : 闽坤果蔬交易市场
     * uname : 陈道光
     * mobile : +86-0599-3599084
     * addr : 福建省建瓯市东峰镇万宁果蔬交易市场
     * picture : [{"pic_name":"http://o9gjmntyw.bkt.clouddn.com/b5cae893ee5c57949a9d74ad5c204234","title":""},{"pic_name":"http://o9gjmntyw.bkt.clouddn.com/38b5561fe88ebb201d662ac30b323d09","title":""}]
     * content :   闽坤果蔬交易市场坐落在建瓯市，东峰镇坤口大棚蔬菜生产基地。距离市区31公里，省道204线从此穿过。市场占得面积14000平方米，是目前闽北乡镇最大的果蔬交易市场。
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
        private String base;
        private String title;
        private String uname;
        private String mobile;
        private String addr;
        private String content;
        /**
         * pic_name : http://o9gjmntyw.bkt.clouddn.com/b5cae893ee5c57949a9d74ad5c204234
         * title :
         */

        private List<PictureBean> picture;

        public String getBase() {
            return base;
        }

        public void setBase(String base) {
            this.base = base;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

        public String getAddr() {
            return addr;
        }

        public void setAddr(String addr) {
            this.addr = addr;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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
