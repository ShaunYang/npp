package com.zhuoyi.fauction.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.adapters.SelectPackAdapter;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.Order;
import com.zhuoyi.fauction.model.PackDo;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.adapter.CategoryGridViewTopAdapter;
import com.zhuoyi.fauction.ui.other.MainPayActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SelectPackWindow extends PopupWindow{
    private View mMenuView;
    private TextView xiangZTX;
    private TextView daiZTX;
    private ListView mListview;

    private Activity mContext;

    private int idx=1;

    View recordPrePackView;

    private String packinId;

    boolean isDiazhuang=false;

    TextView packTypeTx;

    String packDos;


    public SelectPackWindow(Activity context,View.OnClickListener itemsOnClick) {
        super(context);
        mContext=context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pack_select_pop, null);

        mListview=(ListView)mMenuView.findViewById(R.id.pack_list);
        xiangZTX=(TextView)mMenuView.findViewById(R.id.xiangzhuang);
        daiZTX=(TextView)mMenuView.findViewById(R.id.daizhuang);

        packTypeTx=(TextView)context.findViewById(R.id.pack_type);

        xiangZTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx=1;
                isDiazhuang=false;
                xiangZTX.setBackgroundColor(mContext.getResources().getColor(R.color.common_topbar_bg));
                xiangZTX.setTextColor(mContext.getResources().getColor(R.color.white));
                xiangZTX.setText("箱装");
                daiZTX.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                daiZTX.setTextColor(mContext.getResources().getColor(R.color.black));
                daiZTX.setText("袋装");
                recordPrePackView=null;
                orderPackingPost();



            }
        });

        daiZTX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                idx=2;
                isDiazhuang=true;
                daiZTX.setBackgroundColor(mContext.getResources().getColor(R.color.common_topbar_bg));
                daiZTX.setTextColor(mContext.getResources().getColor(R.color.white));
                daiZTX.setText("袋装");
                xiangZTX.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                xiangZTX.setTextColor(mContext.getResources().getColor(R.color.black));
                xiangZTX.setText("箱装");
                recordPrePackView=null;
                orderPackingPost();
            }
        });

        orderPackingPost();

        //
       /* mGridView.setAdapter(new CategoryGridViewTopAdapter(context, categories));

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        viewPager.setCurrentItem(position);
                        dismiss();


                }
            }
        );*/

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
                        packTypeTx.setText(packDos);
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private void orderPackingPost() {

        String timestamp= DateUtil.getStringDate();

        String sign = Util.createSign(mContext, timestamp, "Order", "packing");




        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.ORDER_PACKING)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                .addParams("user_id",ConfigUserManager.getUserId(mContext))
                .addParams("id",idx+"")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i("packing=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson = new Gson();
                            final PackDo packDo = gson.fromJson(response, PackDo.class);
                            if(packDo.getData()!=null){
                                Common.packingId=packDo.getData().get(0).getId();
                                packDos=packDo.getData().get(0).getTitle();
                                final SelectPackAdapter selectPackAdapter = new SelectPackAdapter(mContext, packDo.getData(),isDiazhuang);

                                mListview.setAdapter(selectPackAdapter);

                                mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Common.packingId=packDo.getData().get(position).getId();
                                        selectPackAdapter.changeSelected(position);
                                        packDos=packDo.getData().get(position).getTitle();
                                        packTypeTx.setText(packDos);
                                        dismiss();
                                       /* view.findViewById(R.id.select_pack).setVisibility(View.VISIBLE);
                                        boolean clickable = view.isClickable();
                                        if(clickable){

                                        }*/
                                        /*for(int i=0;i<parent.getChildCount();i++){
                                            if(i==position){

                                            }else{

                                                View childAt = parent.getChildAt(i);
                                                if(childAt==view){
                                                    childAt.findViewById(R.id.select_pack).setVisibility(View.GONE);
                                                }
                                            }
                                        }*/
                                        /*if(recordPrePackView==null){
                                            recordPrePackView=view;
                                            view.findViewById(R.id.select_pack).setVisibility(View.VISIBLE);
                                            //恢复第一个VIew正常状态
                                           // parent.getChildAt(0).findViewById(R.id.select_pack).setVisibility(View.GONE);

                                        }else{
                                            recordPrePackView.findViewById(R.id.select_pack).setVisibility(View.GONE);
                                            recordPrePackView=view;
                                            view.findViewById(R.id.select_pack).setVisibility(View.VISIBLE);
                                        }*/
                                    }
                                });
                            }
                        }else if(code==-1){
                            ToastUtil.showLongToast(mContext, "没有数据");
                        }






                    }
                });
    }

}


