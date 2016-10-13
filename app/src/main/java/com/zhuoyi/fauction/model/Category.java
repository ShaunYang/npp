package com.zhuoyi.fauction.model;

import android.graphics.Bitmap;

import com.lidroid.xutils.db.annotation.Foreign;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Unique;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/5/23.
 */
public class Category implements Serializable{

    @Id
    @NoAutoIncrement
    @Unique
    public String id;
    public String title;
    public String normal_img;
    public String select_img;

    public String user_id;

    public boolean isSelect=false;

    public boolean isSelect() {
        return isSelect;
    }

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

    public String getNormal_img() {
        return normal_img;
    }

    public void setNormal_img(String normal_img) {
        this.normal_img = normal_img;
    }

    public String getSelect_img() {
        return select_img;
    }

    public void setSelect_img(String select_img) {
        this.select_img = select_img;
    }
}
