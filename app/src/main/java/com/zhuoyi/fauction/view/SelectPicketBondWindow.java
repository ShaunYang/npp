package com.zhuoyi.fauction.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.zhuoyi.fauction.model.MyPicketSel;
import com.zhuoyi.fauction.model.PackDo;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.adapter.MyPicketSelAdapter;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DecimalFormat;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SelectPicketBondWindow extends PopupWindow{
    private View mMenuView;
    private ListView mListview;

    private Activity mContext;

    private int idx=1;

    View recordPrePackView;

    private String packinId;

    boolean isDiazhuang=false;

    TextView packTypeTx;

    TextView picketNumTx;

    TextView bondTx;

    String packDos;

    List<MyPicketSel.DataBean> mpickets;
    String oid;

   MyPicketSelAdapter myPicketSelAdapter;

    double totals;
   public SelectPicketBondWindow(Activity context, View.OnClickListener itemsOnClick, List<MyPicketSel.DataBean> pickets, double total,String oids) {
        super(context);
        mContext=context;
        mpickets=pickets;
        totals=total;
        oid=oids;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.picket_select_pop, null);

        mListview=(ListView)mMenuView.findViewById(R.id.pack_list);



       picketNumTx=(TextView)context.findViewById(R.id.packet_num);
       bondTx=(TextView)context.findViewById(R.id.bond);

       myPicketSelAdapter = new MyPicketSelAdapter(context, pickets);
       mListview.setAdapter(myPicketSelAdapter);

        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //做抵用劵校验
                Common.picketSel=mpickets.get(position);
                DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                String p=decimalFormat.format(totals);
                picketBondTestPost(p,Common.picketSel.getNum(),oid,position);

            }
        });


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
                      //  packTypeTx.setText(packDos);
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    private void picketBondTestPost(String price,String num,String oid,final int position) {

        String timestamp= DateUtil.getStringDate();

        String sign = Util.createSign(mContext, timestamp, "Voucher", "payVoucher");
        /*DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String p=decimalFormat.format(totalx);*/

        Logger.i("postttttt=======",ConfigUserManager.getToken(mContext)+"-----------oid="+oid+"-------voucher_num--------"+num+"-----price---"+price);

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.VOUCHER_PAYVOUCHER)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                .addParams("user_id",ConfigUserManager.getUserId(mContext))
                .addParams("type","1")
                .addParams("price",price)
                .addParams("voucher_num",num)
                .addParams("oid",oid)
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
                            //myPicketSelAdapter.changeSelected(position);
                            myPicketSelAdapter.setSelectItem(position);
                            myPicketSelAdapter.notifyDataSetInvalidated();
                            picketNumTx.setText("￥"+Common.picketSel.getPrice());
                            double curTotal=totals-Double.parseDouble(Common.picketSel.getPrice());
                           // Common.total=curTotal;
                            DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                            String p=decimalFormat.format(curTotal);
                            if(curTotal<=0.00){
                                bondTx.setText("￥"+0.00);
                                Common.isBondEqulsZero=true;
                            }else{
                                bondTx.setText("￥"+p);
                                Common.isBondEqulsZero=false;
                            }

                            dismiss();
                        }else if(code==-1){
                            ToastUtil.showLongToast(mContext, "登陆超时");
                        }else if(code==-2){
                            ToastUtil.showLongToast(mContext, "金额不能为空");
                        }else if(code==-3){
                            ToastUtil.showLongToast(mContext, "优惠券不能为空");
                        }else if(code==-4){
                            ToastUtil.showLongToast(mContext, "优惠券不能使用 过期或者已经被使用");
                        }






                    }
                });
    }



}


