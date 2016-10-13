package com.zhuoyi.fauction.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.ui.category.adapter.CategoryGridViewTopAdapter;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SelectPricePopWindow extends PopupWindow{
    public View mMenuView;
    public TextView pop_price_title;
    public TextView popPrice;
    public TextView popNum;
    public TextView priceCut;
    public TextView priceAdd;
    public TextView numCut;
    public TextView numAdd;
    public TextView numTx;
    public EditText edit_price;
    public EditText edit_num;
    public Button price_comfirm;
    int state;//出价状态


    public SelectPricePopWindow(Activity context,View.OnClickListener itemsOnClick,float cur_price,int kepai_num,int state,String unit) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_chujia, null);

        pop_price_title=(TextView)mMenuView.findViewById(R.id.pop_price_title);
        popPrice=(TextView)mMenuView.findViewById(R.id.pop_price);
        popPrice.setText("￥"+cur_price);
        popNum=(TextView)mMenuView.findViewById(R.id.pop_num);
        popNum.setText(kepai_num+unit);
        priceAdd=(TextView)mMenuView.findViewById(R.id.price_add);
        priceCut=(TextView)mMenuView.findViewById(R.id.price_cut);
        numCut=(TextView)mMenuView.findViewById(R.id.num_cut);
        numAdd=(TextView)mMenuView.findViewById(R.id.num_add);
        price_comfirm=(Button)mMenuView.findViewById(R.id.price_comfirm);
        edit_num=(EditText)mMenuView.findViewById(R.id.edit_num);
        edit_price=(EditText)mMenuView.findViewById(R.id.edit_price);
        numTx=(TextView)mMenuView.findViewById(R.id.tx_num);
        numTx.setText("单位"+unit);

        edit_num.setText(kepai_num+"");
        edit_price.setText(cur_price+"");

        priceAdd.setOnClickListener(itemsOnClick);
        priceCut.setOnClickListener(itemsOnClick);
        numAdd.setOnClickListener(itemsOnClick);
        numCut.setOnClickListener(itemsOnClick);
        price_comfirm.setOnClickListener(itemsOnClick);

        state=state;
        if(state==1){
            popPrice.setVisibility(View.VISIBLE);
            pop_price_title.setVisibility(View.VISIBLE);
            priceAdd.setVisibility(View.VISIBLE);
            priceCut.setVisibility(View.VISIBLE);
            edit_price.setFocusable(true);
            edit_price.setFocusableInTouchMode(true);
        }else if(state==2){
            popPrice.setVisibility(View.GONE);
            pop_price_title.setVisibility(View.GONE);
            priceAdd.setVisibility(View.GONE);
            priceCut.setVisibility(View.GONE);
            edit_price.setFocusable(false);
            edit_price.setFocusableInTouchMode(false);
        }


        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}


