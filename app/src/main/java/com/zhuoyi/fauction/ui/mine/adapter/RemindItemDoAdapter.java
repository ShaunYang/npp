package com.zhuoyi.fauction.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyRemind;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.other.OffinePayStateActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class RemindItemDoAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyRemind> fauctionDos;

    public RemindItemDoAdapter(Context context, List<MyRemind> fauctionDos) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);

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
        ViewHolder viewHolder = null;
        if (null == convertView) {

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.remind_item_do, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.price.setText("￥" + fauctionDos.get(position).getCurrent_price() + "/" + fauctionDos.get(position).getUnit());
        viewHolder.remindNum.setText(fauctionDos.get(position).getTotal()+fauctionDos.get(position).getUnit());
        //viewHolder.endTime.setText( fauctionDos.get(position).getDeal_time());
        viewHolder.niPrice.setText("￥"+fauctionDos.get(position).getPrice()+"/"+fauctionDos.get(position).getUnit());
        viewHolder.niNum.setText(fauctionDos.get(position).getBuy_num()+fauctionDos.get(position).getUnit());
        int shoot_type = fauctionDos.get(position).getShoot_type();
        if (shoot_type == 1) {
            viewHolder.shootType.setBackgroundResource(R.drawable.price_up);
        } else if (shoot_type == 2) {
            viewHolder.shootType.setBackgroundResource(R.drawable.price_down);
        }
        //判断是否得拍
        if(Integer.parseInt(fauctionDos.get(position).getState())==1){
            viewHolder.depaiImg.setVisibility(View.VISIBLE);
            viewHolder.timeType.setText("剩余付款时间:");
            viewHolder.endTime.setText( fauctionDos.get(position).getSurplus_time());
            if(Integer.parseInt(fauctionDos.get(position).getPay_state())==1){
                viewHolder.pay.setVisibility(View.GONE);
            }else{

                int pay_type = Integer.parseInt(fauctionDos.get(position).getPay_type());
                if(pay_type==5){
                    viewHolder.pay.setVisibility(View.VISIBLE);
                    //判断是否超时
                    //判断是否超时
                    viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OffinePayStateActivity.class);
                            intent.putExtra("oid", Integer.parseInt(fauctionDos.get(position).getOid()));
                            mContext.startActivity(intent);
                           /* //判断是否超时
                            if(getTimeNum(fauctionDos.get(position).getSurplus_time())==false){
                                ToastUtil.showLongToast(mContext, "付款超时无法支付");
                            }else{


                                Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                                intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getId()));
                                mContext.startActivity(intent);

                            }*/
                        }
                    });


                }else{
                    viewHolder.pay.setVisibility(View.VISIBLE);
                    //判断是否超时
                    //判断是否超时
                    viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //判断是否超时
                            if(getTimeNum(fauctionDos.get(position).getSurplus_time())==false){
                                ToastUtil.showLongToast(mContext, "付款超时无法支付");
                            }else{


                                Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                                intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getId()));
                                mContext.startActivity(intent);

                            }
                        }
                    });


                }
                viewHolder.pay.setVisibility(View.VISIBLE);
                //判断是否超时
                //判断是否超时
                viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //判断是否超时
                        if(getTimeNum(fauctionDos.get(position).getSurplus_time())==false){
                            ToastUtil.showLongToast(mContext, "付款超时无法支付");
                        }else{


                            Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                            intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getId()));
                            mContext.startActivity(intent);

                        }
                    }
                });

            }
        }else{
            viewHolder.pay.setVisibility(View.GONE);
            viewHolder.depaiImg.setVisibility(View.GONE);
            viewHolder.timeType.setText("拍卖结束时间:");
            viewHolder.endTime.setText( fauctionDos.get(position).getDeal_time());
        }

        viewHolder.cancleRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delRemindPost(Integer.parseInt(fauctionDos.get(position).getId()), position);
            }
        });
        if(fauctionDos.get(position).getPic()!=null){
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).resize(350, 300).into(viewHolder.titleImg);
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
        Logger.i("length=",length+"");
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

    private void delRemindPost(int id, final int pos) {



            String timestamp = DateUtil.getStringDate();

            String sign = Util.createSign(mContext, timestamp, "Product", "delRemind");

            OkHttpUtils.post()
                    .url(Constant.PRODUCT_DELREMIND)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                    .addParams("timestamp", timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                    .addParams("user_id", ConfigUserManager.getUserId(mContext))
                    .addParams("id", id + "")
                    .build()
                    .execute(new StringCallback() {
                                 @Override
                                 public void onError(Call call, Exception e) {

                                 }

                                 @Override
                                 public void onResponse(String response) {

                                     Logger.i("Remind--45646456456====", response);
                                     JSONObject jsonObject = JSONObject.parseObject(response);
                                     int code = jsonObject.getIntValue("code");
                                     if(code==0){
                                         //
                                         fauctionDos.remove(pos);
                                         notifyDataSetChanged();

                                     }else{
                                         ToastUtil.showLongToast(mContext,"取消失败");
                                     }


                                 }
                             }
                    );


    }


    static class ViewHolder {
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.depai_img)
        ImageView depaiImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.shoot_type)
        ImageView shootType;
        @Bind(R.id.remind_num)
        TextView remindNum;
        @Bind(R.id.end_time)
        TextView endTime;
        @Bind(R.id.nichujia)
        TextView nichujia;
        @Bind(R.id.ni_price)
        TextView niPrice;
        @Bind(R.id.shuliang)
        TextView shuliang;
        @Bind(R.id.time_type)
        TextView timeType;
        @Bind(R.id.ni_num)
        TextView niNum;
        @Bind(R.id.pay)
        Button pay;
        @Bind(R.id.cancle_remind)
        Button cancleRemind;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
