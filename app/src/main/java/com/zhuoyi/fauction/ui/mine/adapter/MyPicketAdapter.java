package com.zhuoyi.fauction.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyPicket;
import com.zhuoyi.fauction.model.MyRemind;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.OffinePayStateActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class MyPicketAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyPicket.DataBean> fauctionDos;

    public MyPicketAdapter(Context context, List<MyPicket.DataBean> fauctionDos) {
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
            convertView = mInflater.inflate(R.layout.mypicket_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.price.setText("￥" + fauctionDos.get(position).getPrice());
        viewHolder.valid_price.setText("满"+fauctionDos.get(position).getValid_price()+"可用");
        viewHolder.valid_time.setText("有效期至"+fauctionDos.get(position).getValid_time());
        int type = Integer.parseInt(fauctionDos.get(position).getType());
        if(type==1){
            viewHolder.type.setText("限于支付保证金时使用");
        }else if(type==2){
            viewHolder.type.setText("限于支付货款时使用");
        }else if(type==3){
            viewHolder.type.setText("通用");
        }
        int state = fauctionDos.get(position).getState();
        int status = Integer.parseInt(fauctionDos.get(position).getStatus());
        if(state==0){
            //过期
            viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_his);
        }else if(state==1){
            if(status==3){
                //已使用
                viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_used);
            }else if(status==2){
                //正常
            }
        }

        return convertView;


    }






    static class ViewHolder {

        @Bind(R.id.valid_price)
        TextView valid_price;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.valid_time)
        TextView valid_time;
        @Bind(R.id.pick_bg)
        LinearLayout pick_bg;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
