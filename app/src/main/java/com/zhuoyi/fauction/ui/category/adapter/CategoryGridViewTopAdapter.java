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
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class CategoryGridViewTopAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;



    List<Category> categories;
    public CategoryGridViewTopAdapter(Context context, List<Category> categories) {
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
        viewHolder.category_name.setText(categories.get(position).getTitle());

        //根据状态判断
        if(true){

        }

        return convertView;


    }

    public void addItems(List<Category> list) {


        for(Category fauctionDo : list) {
            categories.add(fauctionDo);
        }


    }

    public void deleteItems(ArrayList<Category> items) {

        for (Category item : items) {
            categories.remove(item);
        }

    }





    private static class ViewHolder{


        TextView category_name;

    }



}
