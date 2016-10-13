package com.zhuoyi.fauction.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/8.
 */
public class Provience {
    String enum_name;
    String enum_value=null;

    ArrayList<City> citys;

    public String getEnum_value() {
        return enum_value;
    }

    public void setEnum_value(String enum_value) {
        this.enum_value = enum_value;
    }

    public ArrayList<City> getCitys() {
        return citys;
    }

    public void setCitys(ArrayList<City> citys) {
        this.citys = citys;
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
