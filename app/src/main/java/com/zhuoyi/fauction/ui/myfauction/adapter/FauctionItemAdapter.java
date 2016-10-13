package com.zhuoyi.fauction.ui.myfauction.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yintai.app_common.ui.common.view.CommonSimpleTitleDialog;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.AddressActivity;
import com.zhuoyi.fauction.ui.other.MainPayActivity;
import com.zhuoyi.fauction.ui.other.OffinePayStateActivity;
import com.zhuoyi.fauction.ui.other.OrderDetailActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.DialogUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class FauctionItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyFauction> fauctionDos;

    public FauctionItemAdapter(Context context, List<MyFauction> fauctionDos) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getItemViewType(int position) {
        int pay_type=Integer.parseInt(fauctionDos.get(position).getPay_type());
        return pay_type;
    }

    @Override
    public int getCount() {
        return fauctionDos.size();
    }

    @Override
    public Object getItem(int position) {
        return fauctionDos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Logger.i("hd_position====================",position+"");
       // ViewHolder viewHolder = null;
        if (null == convertView) {

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.fauction_item, null);

        }

        TextView title=(TextView)convertView.findViewById(R.id.title);

        TextView states=(TextView)convertView.findViewById(R.id.state);

        RelativeLayout topItem=(RelativeLayout) convertView.findViewById(R.id.top_item);

        ImageView titleImg=(ImageView)convertView.findViewById(R.id.title_img);

        TextView time=(TextView)convertView.findViewById(R.id.time);

        TextView endPrice=(TextView)convertView.findViewById(R.id.end_price);

        TextView express=(TextView)convertView.findViewById(R.id.express);

        TextView yunfeiYongjing=(TextView)convertView.findViewById(R.id.yunfei_yongjing);

        TextView total=(TextView)convertView.findViewById(R.id.total);

        TextView remindTime=(TextView)convertView.findViewById(R.id.remind_time);
        Button pay=(Button)convertView.findViewById(R.id.pay);

       title.setText(fauctionDos.get(position).getTitle());
        endPrice.setText("￥" + fauctionDos.get(position).getPrice() + "/"+fauctionDos.get(position).getUnit()+"*"+fauctionDos.get(position).getNum());
       express.setText("￥"+fauctionDos.get(position).getExpress());
        total.setText("￥"+fauctionDos.get(position).getTotal());
       states.setText(fauctionDos.get(position).getStatus());
        time.setText(fauctionDos.get(position).getAdd_time());

        if(fauctionDos.get(position).getPic()!=null){
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).resize(350,300).into(titleImg);
        }
        int state=Integer.parseInt(fauctionDos.get(position).getState());
        int pay_type=Integer.parseInt(fauctionDos.get(position).getPay_type());
       // int pay_type=getItemViewType(position);
        if(pay_type==5){
            //线下支付
          pay.setVisibility(View.GONE);
            remindTime.setText("");
            if(state == 1){

                    return convertView;
            }
            if (state == 0) {
              /*  pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //临时跳转到提交订单界面
                        Intent intent = new Intent(mContext, OffinePayStateActivity.class);
                        intent.putExtra("oid", Integer.parseInt(fauctionDos.get(position).getOrder_num()));
                        mContext.startActivity(intent);
                    }
                });*/
                remindTime.setText("");

                 return convertView;


            }

            if(state==2){
                pay.setVisibility(View.VISIBLE);
               pay.setText("确认收货");
                final Button payButton=pay;
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showCustomerSimpleTitleDialog(mContext, true, "是否确定收货？", "确认", "取消", new CommonSimpleTitleDialog.OnSubmitClickListener() {
                            @Override
                            public void onSubmit(Dialog dialog) {

                                String timestamp = DateUtil.getStringDate();

                                String sign = Util.createSign(mContext, timestamp, "Order", "confirmReceipt");

                                OkHttpUtils.post()
                                        .url(Constant.ORDER_CONFIRMRECEIPT)
                                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                                        .addParams("timestamp", timestamp)
                                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                                        .addParams("user_id", ConfigUserManager.getUserId(mContext))
                                        .addParams("id", fauctionDos.get(position).getId() + "")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e) {

                                            }

                                            @Override
                                            public void onResponse(String response) {

                                                Logger.i("JDItemAdapter" + "4564645645====", response);
                                                JSONObject jsonObject = JSONObject.parseObject(response);
                                                int code = jsonObject.getIntValue("code");
                                                if (code == 0) {
                                                    //成功
                                                    payButton.setVisibility(View.GONE);
                                                    //ToastUtil.showLongToast(mContext, "成功");
                                                    //跳转订单详情
                                                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                                    intent.putExtra("id", fauctionDos.get(position).getId());
                                                    mContext.startActivity(intent);

                                                    fauctionDos.remove(position);
                                                    notifyDataSetChanged();
                                                    //  DialogUtil.commonDialogDismiss();
                                                } else if (code == -1) {
                                                    ToastUtil.showLongToast(mContext, "登陆超时");
                                                    //  DialogUtil.commonDialogDismiss();
                                                } else if (code == -2) {
                                                    ToastUtil.showLongToast(mContext, "提交失败");
                                                    // DialogUtil.commonDialogDismiss();
                                                }

                                                DialogUtil.commonDialogDismiss();


                                            }
                                        });
                            }
                        }, new CommonSimpleTitleDialog.OnNegativeClickListener() {
                            @Override
                            public void onNegative(Dialog dialog) {

                            }
                        });
                    }
                });



                 return convertView;
            }
            if(state==3){
                pay.setVisibility(View.VISIBLE);
               pay.setText("删除订单");
                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timestamp = DateUtil.getStringDate();

                        String sign = Util.createSign(mContext, timestamp, "Order", "delOrder");

                        OkHttpUtils.post()
                                .url(Constant.ORDER_DELORDER)
                                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                                .addParams("timestamp", timestamp)
                                .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                                .addParams("user_id", ConfigUserManager.getUserId(mContext))
                                .addParams("id", fauctionDos.get(position).getId()+"")
                                .build()
                                .execute(new StringCallback() {
                                             @Override
                                             public void onError(Call call, Exception e) {

                                             }

                                             @Override
                                             public void onResponse(String response) {

                                                 Logger.i("JDItemAdapter" + "45646456456====", response);
                                                 JSONObject jsonObject = JSONObject.parseObject(response);
                                                 int code = jsonObject.getIntValue("code");
                                                 if (code == 0) {
                                                     //成功
                                                     ToastUtil.showLongToast(mContext, "删除成功");
                                                     fauctionDos.remove(position);
                                                     notifyDataSetChanged();

                                                 } else if (code == -1) {
                                                     ToastUtil.showLongToast(mContext, "登陆超时");
                                                 } else if (code==-2) {
                                                     ToastUtil.showLongToast(mContext,"删除失败");
                                                 }



                                             }
                                         }
                                );
                    }
                });
                  return convertView;
            }
            if(state==4){
                return convertView;
            }
        }else{
            if(state == 1){
                pay.setVisibility(View.GONE);
                //viewHolder.pay.setText("提醒发货");
               remindTime.setText("");
                 return convertView;
            }
            if (state == 0) {

                if(getTimeNum(fauctionDos.get(position).getPay_time())==false){
                   pay.setVisibility(View.GONE);
                    remindTime.setText("超过付款时间 不能付款");
                  pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ToastUtil.showLongToast(mContext, "付款超时无法支付");
                        }
                    });
                }else{
                   pay.setVisibility(View.VISIBLE);
                   pay.setText("立即付款");
                   remindTime.setText("剩余付款时间：" + fauctionDos.get(position).getPay_time());
                    //跳到支付页面

                   pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //临时跳转到提交订单界面
                            Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                            intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getAid()));
                            mContext.startActivity(intent);
                        }
                    });
                }
                  return convertView;


            }

            if(state==2){
                pay.setVisibility(View.VISIBLE);
               pay.setText("确认收货");
                final Button payButton=pay;
               pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogUtil.showCustomerSimpleTitleDialog(mContext, true, "是否确定收货？", "确认", "取消", new CommonSimpleTitleDialog.OnSubmitClickListener() {
                            @Override
                            public void onSubmit(Dialog dialog) {

                                String timestamp = DateUtil.getStringDate();

                                String sign = Util.createSign(mContext, timestamp, "Order", "confirmReceipt");

                                OkHttpUtils.post()
                                        .url(Constant.ORDER_CONFIRMRECEIPT)
                                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                                        .addParams("timestamp", timestamp)
                                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                                        .addParams("user_id", ConfigUserManager.getUserId(mContext))
                                        .addParams("id", fauctionDos.get(position).getId() + "")
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e) {

                                            }

                                            @Override
                                            public void onResponse(String response) {

                                                Logger.i("JDItemAdapter" + "4564645645====", response);
                                                JSONObject jsonObject = JSONObject.parseObject(response);
                                                int code = jsonObject.getIntValue("code");
                                                if (code == 0) {
                                                    //成功
                                                    payButton.setVisibility(View.GONE);
                                                    //ToastUtil.showLongToast(mContext, "成功");
                                                    //跳转订单详情
                                                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                                                    intent.putExtra("id", fauctionDos.get(position).getId());
                                                    mContext.startActivity(intent);

                                                    fauctionDos.remove(position);
                                                    notifyDataSetChanged();
                                                    //  DialogUtil.commonDialogDismiss();
                                                } else if (code == -1) {
                                                    ToastUtil.showLongToast(mContext, "登陆超时");
                                                    //  DialogUtil.commonDialogDismiss();
                                                } else if (code == -2) {
                                                    ToastUtil.showLongToast(mContext, "提交失败");
                                                    // DialogUtil.commonDialogDismiss();
                                                }

                                                DialogUtil.commonDialogDismiss();


                                            }
                                        });
                            }
                        }, new CommonSimpleTitleDialog.OnNegativeClickListener() {
                            @Override
                            public void onNegative(Dialog dialog) {

                            }
                        });
                    }
                });



                  return convertView;
            }
            if(state==3){
               pay.setVisibility(View.VISIBLE);
                pay.setText("删除订单");
              pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String timestamp = DateUtil.getStringDate();

                        String sign = Util.createSign(mContext, timestamp, "Order", "delOrder");

                        OkHttpUtils.post()
                                .url(Constant.ORDER_DELORDER)
                                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                                .addParams("timestamp", timestamp)
                                .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                                .addParams("user_id", ConfigUserManager.getUserId(mContext))
                                .addParams("id", fauctionDos.get(position).getId()+"")
                                .build()
                                .execute(new StringCallback() {
                                             @Override
                                             public void onError(Call call, Exception e) {

                                             }

                                             @Override
                                             public void onResponse(String response) {

                                                 Logger.i("JDItemAdapter" + "45646456456====", response);
                                                 JSONObject jsonObject = JSONObject.parseObject(response);
                                                 int code = jsonObject.getIntValue("code");
                                                 if (code == 0) {
                                                     //成功
                                                     ToastUtil.showLongToast(mContext, "删除成功");
                                                     fauctionDos.remove(position);
                                                     notifyDataSetChanged();

                                                 } else if (code == -1) {
                                                     ToastUtil.showLongToast(mContext, "登陆超时");
                                                 } else if (code==-2) {
                                                     ToastUtil.showLongToast(mContext,"删除失败");
                                                 }



                                             }
                                         }
                                );
                    }
                });
                 return convertView;
            }
            if(state==4){
                 return convertView;
            }
        }


        return convertView;


    }


    private boolean getTimeNum(String str) {

        //临时字符串
        String temp;
        String day;
        String hour;
        String minute;
        String second;
        String[] dayArray=str.split("天");

        day=dayArray[0];
//        if(day.equals("-1")){
//            shootTime.setText(str+" 结束");
//            return false;
//        }

        int length = dayArray.length;
        Logger.i("length=", length + "");
        if(length==1){
            //shootTime.setText(str+" 结束");
            return false;
        }
        //去掉天后
        String[] hourArray=dayArray[1].split("小时");
        hour=hourArray[0];


        String[] minuteArray=hourArray[1].split("分");
        minute=minuteArray[0];

        String[] secondArray=minuteArray[1].split("秒");
        second=secondArray[0];


        System.out.println(day);
        System.out.println(hour);
        System.out.println(minute);
        System.out.println(second);

        //
//        mDay=Long.parseLong(day);
//        mHour=Long.parseLong(hour);
//        mMin=Long.parseLong(minute);
//        mSecond=Long.parseLong(second);

        return true;

    }


    static class ViewHolder {
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.state)
        TextView state;
        @Bind(R.id.top_item)
        RelativeLayout topItem;
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.end_price)
        TextView endPrice;
        @Bind(R.id.express)
        TextView express;
        @Bind(R.id.yunfei_yongjing)
        TextView yunfeiYongjing;
        @Bind(R.id.total)
        TextView total;
        @Bind(R.id.remind_time)
        TextView remindTime;
        @Bind(R.id.pay)
        Button pay;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
