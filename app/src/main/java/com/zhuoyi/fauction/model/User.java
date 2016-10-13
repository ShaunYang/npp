package com.zhuoyi.fauction.model;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Unique;

import java.util.List;

/**
 * Created by yintai002 on 2015/10/29.
 */
public class User {

    /**
     * id : 165150651
     * name : 用户名
     * phone : 15060112132
     * headimg : http://图片资源地址
     * indentify_code : 350182199165894
     * lisence : 营业执照
     */

    @Id
    @NoAutoIncrement
    @Unique
    private String id;
    private String name;
    private String phone;
    private String headimg;
    private String indentify_code;
    private String lisence;

    private String mPsw;

    private boolean islogin;

    private long loginTime;
    private long logoutTime;

    private int ranking;
    private int up;



    public String getmPsw() {
        return mPsw;
    }

    public void setmPsw(String mPsw) {
        this.mPsw = mPsw;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getUp() {
        return up;
    }

    public void setUp(int up) {
        this.up = up;
    }

    public String getPassword() {
        return mPsw;
    }

    public void setPassword(String password) {
        this.mPsw = password;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    public long getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(long logoutTime) {
        this.logoutTime = logoutTime;
    }

    public boolean islogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public void setIndentify_code(String indentify_code) {
        this.indentify_code = indentify_code;
    }

    public void setLisence(String lisence) {
        this.lisence = lisence;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getHeadimg() {
        return headimg;
    }

    public String getIndentify_code() {
        return indentify_code;
    }

    public String getLisence() {
        return lisence;
    }
}
