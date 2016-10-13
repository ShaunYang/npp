package com.zhuoyi.fauction.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.MyHaveFauction;
import com.zhuoyi.fauction.ui.other.OffinePayStateActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MyHaveFauctionEndAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;


    List<MyHaveFauction> fauctionDos;

    public MyHaveFauctionEndAdapter(Context context, List<MyHaveFauction> fauctionDos) {
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
            convertView = mInflater.inflate(R.layout.myhavefauction_item_end, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.end_price.setText("￥" + fauctionDos.get(position).getCurrent_price() + "/"+fauctionDos.get(position).getUnit());
        viewHolder.remindNum.setText(fauctionDos.get(position).getStock() + "/"+fauctionDos.get(position).getUnit());
        viewHolder.end_time.setText(fauctionDos.get(position).getDeal_time());
        //判断是否得拍
        Logger.i("sidepai",fauctionDos.get(position).getStatus());
        if(!fauctionDos.get(position).getStatus().equals("0")){
            viewHolder.depaiImg.setVisibility(View.VISIBLE);
            if(Integer.parseInt(fauctionDos.get(position).getPay_state())==1){
                viewHolder.pay.setVisibility(View.GONE);
                int pay_type = Integer.parseInt(fauctionDos.get(position).getPay_type());
                if(pay_type==5){
                    viewHolder.unpay_time.setVisibility(View.GONE);
                    viewHolder.unpay_time_show.setVisibility(View.GONE);
                }
            }else{
                int pay_type = Integer.parseInt(fauctionDos.get(position).getPay_type());
                if(pay_type==5){
                    viewHolder.pay.setVisibility(View.GONE);
                    viewHolder.unpay_time.setVisibility(View.GONE);
                    viewHolder.unpay_time_show.setVisibility(View.GONE);
                   /* viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, OffinePayStateActivity.class);
                            intent.putExtra("oid", Integer.parseInt(fauctionDos.get(position).getOid()));
                            mContext.startActivity(intent);
                            //判断是否超时
                            *//*if(getTimeNum(fauctionDos.get(position).getPay_time())==false){
                                ToastUtil.showLongToast(mContext, "付款超时无法支付");
                            }else{


                                Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                                intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getId()));
                                mContext.startActivity(intent);

                            }*//*
                        }
                    });*/
                }else{
                    viewHolder.pay.setVisibility(View.VISIBLE);
                    viewHolder.unpay_time.setVisibility(View.VISIBLE);
                    viewHolder.unpay_time_show.setVisibility(View.VISIBLE);
                    viewHolder.unpay_time_show.setText(fauctionDos.get(position).getPay_time());
                    viewHolder.pay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //判断是否超时
                            if(getTimeNum(fauctionDos.get(position).getPay_time())==false){
                                ToastUtil.showLongToast(mContext, "付款超时无法支付");
                            }else{


                                Intent intent = new Intent(mContext, OrderSubmitActivity.class);
                                intent.putExtra("productId", Integer.parseInt(fauctionDos.get(position).getId()));
                                mContext.startActivity(intent);

                            }
                        }
                    });
                }



            }
        }else{
            viewHolder.pay.setVisibility(View.GONE);
            viewHolder.depaiImg.setVisibility(View.GONE);
        }



        //viewHolder.num.setText(fauctionDos.get(position).getNum());
        //viewHolder.shootPrice.setText(fauctionDos.get(position).getPrice());
        if(fauctionDos.get(position).getPic()!=null){
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).resize(350,300).into(viewHolder.titleImg);
        }

        //Picasso.with(mContext).load(R.drawable.depai).resize(200, 300).into(viewHolder.depaiImg);


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
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.depai_img)
        ImageView depaiImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.end_price)
        TextView end_price;
        @Bind(R.id.remind_num)
        TextView remindNum;
        @Bind(R.id.end_time)
        TextView end_time;
        @Bind(R.id.pay)
        Button pay;
        @Bind(R.id.unpay_time)
        TextView unpay_time;
        @Bind(R.id.unpay_time_show)
        TextView unpay_time_show;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
