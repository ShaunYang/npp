package com.zhuoyi.fauction.ui.other.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.Mounth;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class MouthGridViewTopAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;



    List<Mounth> categories;
    public MouthGridViewTopAdapter(Context context, List<Mounth> categories) {
        mContext=context;
        this.categories=categories;
        mInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.gridview_item, null);


            viewHolder.category_name=(TextView) convertView.findViewById(R.id.category_name);


            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.category_name.setText(categories.get(position).getNum()+"月");

        //根据状态判断
        if(true){

        }

        return convertView;


    }







    private static class ViewHolder{


        TextView category_name;

    }



}
