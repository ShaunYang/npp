package com.zhuoyi.fauction.ui.other.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.MounthPo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */
public class AccountDetailListAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;


    List<MounthPo> fauctionDos;

    public AccountDetailListAdapter(Context context, List<MounthPo> fauctionDos) {
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
            convertView = mInflater.inflate(R.layout.account_detail_list_item, null);
            viewHolder = new ViewHolder(convertView);





            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.date.setText(fauctionDos.get(position).getData());
        viewHolder.type.setText(fauctionDos.get(position).getType());
        viewHolder.price.setText(fauctionDos.get(position).getPrice());

        //根据状态判断
        if (true) {

        }

        return convertView;


    }


    static class ViewHolder {
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
