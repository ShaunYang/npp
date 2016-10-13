package com.zhuoyi.fauction.ui.home.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.RecordDetail;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */
public class DetailRecordAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;


    List<RecordDetail.DataBean> dataBeans;

    public DetailRecordAdapter(Context context, List<RecordDetail.DataBean> dataBeans) {
        mContext = context;
        this.dataBeans = dataBeans;
        mInflater = LayoutInflater.from(context);


    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return dataBeans.get(position);
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
            convertView = mInflater.inflate(R.layout.fauction_record_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecordDetail.DataBean dataBean = dataBeans.get(position);
        viewHolder.recordName.setText(dataBean.getUser_name() + "");
        viewHolder.recordNum.setText(dataBean.getNum() +  dataBean.getUnit());
        viewHolder.recordPrice.setText("￥" + dataBean.getPrice() + "/" + dataBean.getUnit());
        String add_time = dataBean.getAdd_time();
        String sub_time = add_time.substring(5, add_time.length());
        viewHolder.recordTime.setText(sub_time);
        viewHolder.recordStats.setText(dataBean.getStatus());
        if (dataBean.getStatus().equals("领先")) {

            //viewHolder.recordStats.setTextColor(0xFFFFFF);
            viewHolder.recordStats.setBackgroundColor(0xF5A220);
            //viewHolder.recordStats.setTextColor(Color.BLUE);



        }else {
            viewHolder.recordStats.setBackgroundColor(0x000000);
        }



        //根据状态判断
        if (true) {

        }

        return convertView;


    }


    static class ViewHolder {
        @Bind(R.id.record_stats)
        TextView recordStats;
        @Bind(R.id.status_bg)
        LinearLayout statusBg;
        @Bind(R.id.record_name)
        TextView recordName;
        @Bind(R.id.record_time)
        TextView recordTime;
        @Bind(R.id.record_price)
        TextView recordPrice;
        @Bind(R.id.record_num)
        TextView recordNum;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
