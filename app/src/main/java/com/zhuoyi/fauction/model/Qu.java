package com.zhuoyi.fauction.model;

/**
 * Created by Administrator on 2016/9/8.
 */
public class Qu {
    String enum_name;
    String enum_value=null;

    public String getEnum_value() {
        return enum_value;
    }

    public void setEnum_value(String enum_value) {
        this.enum_value = enum_value;
    }

    public String getEnum_name() {
        return enum_name;
    }

    public void setEnum_name(String enum_name) {
        this.enum_name = enum_name;
    }

    @Override
    public String toString() {
        return enum_name;
    }
}
