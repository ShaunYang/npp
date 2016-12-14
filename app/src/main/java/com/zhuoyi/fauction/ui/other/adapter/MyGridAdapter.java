package com.zhuoyi.fauction.ui.other.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.RecommondPo;
import com.zhuoyi.fauction.model.ReminderPo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MyGridAdapter extends BaseAdapter {
    private Context mContext;
    private int pos;
    List<RecommondPo> dataBeans;

    public MyGridAdapter(Context mContext, List<RecommondPo> dataBeanList) {
        super();
        dataBeans = dataBeanList;
        this.mContext = mContext;
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataBeans.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.recommand_item, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(dataBeans.get(position).getTitle());
        viewHolder.price.setText("￥" + dataBeans.get(position).getCurrent_price() + "/" + dataBeans.get(position).getUnit());
        if(dataBeans.get(position).getPic()!=null){
            Picasso.with(mContext).load(dataBeans.get(position).getPic()).into(viewHolder.titleImg);

        }

        //根据状态判断
        if (true) {

        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
