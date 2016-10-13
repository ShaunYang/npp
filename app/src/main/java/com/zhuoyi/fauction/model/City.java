package com.zhuoyi.fauction.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class City {
    String enum_name;
    String enum_value;

    ArrayList<Qu> qus;

    public String getEnum_name() {
        return enum_name;
    }

    public void setEnum_name(String enum_name) {
        this.enum_name = enum_name;
    }

    public String getEnum_value() {
        return enum_value;
    }

    public void setEnum_value(String enum_value) {
        this.enum_value = enum_value;
    }

    public ArrayList<Qu> getQus() {
        return qus;
    }

    public void setQus(ArrayList<Qu> qus) {
        this.qus = qus;
    }

    @Override
    public String toString() {
        return enum_name;
    }
}
