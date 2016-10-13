package com.zhuoyi.fauction.model;

import java.util.List;

/**
 * Created by Apple on 16/5/29.
 */
public class Ret {


    /**
     * ret : 200
     * code : 0
     * data : []
     */

    private int ret;
    private int code;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
