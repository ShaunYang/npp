package com.zhuoyi.fauction.config;

import android.widget.ImageView;

import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.MyPicketSel;
import com.zhuoyi.fauction.model.ProductDetail2;
import com.zhuoyi.fauction.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/25.
 */
public class Common {

    public static List<Category> categories;
    public static List<Category> allCategories;
    //临时商品详情2
    public static ProductDetail2 productDetail2;
    //用户选择得商品id
    public static int proId;
    //哪个界面 1代表商品详情 3代表提交订单页面 4代表我的提醒 5代表我参拍的
    public static int whichActivity=0;
    public static int whichActivity2=0;
    //
    public static String categoryTab;

    public static Area.ProvinceBean province;
    public static Area.ProvinceBean.CityBean city;
    public static Area.ProvinceBean.CityBean.AreaBean area;
    public static String tab;
    public static boolean isAlreadLogin=false;

    public static int accountType;
    public static String accountId;

    public static String yuer;

    public static String userAvator;

    public static String nonextpageText="";

    //
    public static String reg_phone;
    public static String reg_code;
    public static String reg_psd;
    public static String reg_cpsd;

    public static int home_tab=0;

    public static String packingId="";

    public static boolean isALLSelect=false;

    //
    public static String packType="";

    //
    public static String JD_ID="0";


    public static int TYPEID;

    public static MyPicketSel.DataBean picketSel;

    //public static double total=0.00;
    public static boolean isBondEqulsZero=false;

    public static boolean isOrderEqulsZero=false;
    //
    public static CustomViewPager viewPagers=null;

}
