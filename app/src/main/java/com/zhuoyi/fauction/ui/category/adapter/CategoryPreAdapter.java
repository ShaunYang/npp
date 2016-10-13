package com.zhuoyi.fauction.ui.category.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.FauctionDo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */
public class CategoryPreAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;


    List<FauctionDo> fauctionDos;

    public CategoryPreAdapter(Context context, List<FauctionDo> fauctionDos) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.category_pre_item, null);
            viewHolder = new ViewHolder(convertView);




            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.curPrice.setText("￥" + fauctionDos.get(position).getShoot_price() + "/"+fauctionDos.get(position).getUnit());
        viewHolder.stockNum.setText(fauctionDos.get(position).getNum() + fauctionDos.get(position).getUnit());
        viewHolder.residualTime.setText(fauctionDos.get(position).getShoot_time());

        if(fauctionDos.get(position).getTitle_img()!=null){
            Logger.i("img=",fauctionDos.get(position).getTitle_img());
            Picasso.with(mContext).load(fauctionDos.get(position).getTitle_img()).into(viewHolder.titleImg);
        }

        //根据状态判断
        if (true) {

        }

        return convertView;


    }

    public void addItems(List<FauctionDo> list) {


        for (FauctionDo fauctionDo : list) {
            fauctionDos.add(fauctionDo);
        }


    }

    public void deleteItems(ArrayList<FauctionDo> items) {

        for (FauctionDo item : items) {
            fauctionDos.remove(item);
        }

    }


    static class ViewHolder {
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.cur_price)
        TextView curPrice;
        @Bind(R.id.stock_num)
        TextView stockNum;
        @Bind(R.id.residual_time)
        TextView residualTime;
        @Bind(R.id.gv)
        RelativeLayout gv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
