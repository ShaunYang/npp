package com.zhuoyi.fauction.ui.mine.bond.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.yintai.app_common.ui.common.view.CommonSimpleTitleDialog;
import com.yintai.common.util.DialogUtil;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyBond;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.OrderDetailActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class BondItemAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyBond.DataBean> fauctionDos;

    public BondItemAdapter(Context context, List<MyBond.DataBean> fauctionDos) {
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
            convertView = mInflater.inflate(R.layout.bond_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.shootPrice.setText("￥" +fauctionDos.get(position).getShoot_price()+"/"+fauctionDos.get(position).getUnit());
        viewHolder.state.setText(fauctionDos.get(position).getStatus());
        viewHolder.allNum.setText(fauctionDos.get(position).getNum()+"/"+fauctionDos.get(position).getUnit());
        viewHolder.bond.setText("￥"+fauctionDos.get(position).getBond());

        if (fauctionDos.get(position).getPic() != null) {
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).resize(350, 300).into(viewHolder.titleImg);
        }


        //viewHolder.yunfeiYongjing.setText("（含运费险￥" + fauctionDos.get(position).getInsurance() + "、佣金￥" + fauctionDos.get(position).getBond() + "）");
        //viewHolder.yunfeiYongjing.setText("（含佣金￥"+fauctionDos.get(position).getBond()+"）");

        return convertView;


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
        @Bind(R.id.shoot_price)
        TextView shootPrice;
        @Bind(R.id.all_num)
        TextView allNum;
        @Bind(R.id.bond)
        TextView bond;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
