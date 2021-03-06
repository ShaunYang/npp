package com.zhuoyi.fauction.ui.home.adapter;

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
import com.zhuoyi.fauction.model.FauctionDo;

import java.util.ArrayList;
import java.util.List;


public class HomeTabJRZQAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;

    public String[] img_text = { "玉米", "青瓜", "辣椒", "四季豆" };
    public int[] imgs= {R.drawable.category_yumi,R.drawable.category_qinggua,R.drawable.category_lajiao,R.drawable.category_sijidou};

    int mHeight;

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    List<FauctionDo> fauctionDos;

    public HomeTabJRZQAdapter(Context context, List<FauctionDo> fauctionDos) {
        mContext=context;
        this.fauctionDos=fauctionDos;
        mInflater=LayoutInflater.from(context);
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
        if (null == convertView)
        {
            viewHolder = new ViewHolder();
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.home_tab_item_jrzq, null);


            //viewHolder.mGv=(RelativeLayout) convertView.findViewById(R.id.gv);
            viewHolder.title=(TextView) convertView.findViewById(R.id.title);
            viewHolder.stock_num=(TextView) convertView.findViewById(R.id.num);
            viewHolder.cur_price=(TextView) convertView.findViewById(R.id.price);
            viewHolder.residual_time=(TextView) convertView.findViewById(R.id.time);
            viewHolder.price_state=(ImageView) convertView.findViewById(R.id.price_state);
            viewHolder.title_img=(ImageView) convertView.findViewById(R.id.img);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.cur_price.setText("￥"+fauctionDos.get(position).getCurPrice()+"/"+fauctionDos.get(position).getUnit());
        viewHolder.stock_num.setText(fauctionDos.get(position).getStock()+fauctionDos.get(position).getUnit()+"/"+fauctionDos.get(position).getNum()+fauctionDos.get(position).getUnit());
        viewHolder.residual_time.setText(fauctionDos.get(position).getResidual_time());
//        if(fauctionDos.get(position).getStatus()==1){
//            viewHolder.price_state.setBackgroundResource(R.drawable.price_up);
//        }else{
//            viewHolder.price_state.setBackgroundResource(R.drawable.price_down);
//        }
        if(fauctionDos.get(position).getTitle_img()!=null){
            Picasso.with(mContext).load(fauctionDos.get(position).getTitle_img()).into(viewHolder.title_img);
        }

        //根据状态判断
        if(true){

        }

        return convertView;


    }

    public void addItems(List<FauctionDo> list) {


        for(FauctionDo fauctionDo : list) {
            fauctionDos.add(fauctionDo);
        }


    }

    public void deleteItems(ArrayList<FauctionDo> items) {

        for (FauctionDo item : items) {
            fauctionDos.remove(item);
        }

    }





    private static class ViewHolder{
        RelativeLayout mGv;

        TextView title;
        TextView cur_price;
        TextView stock_num;
        TextView residual_time;
        ImageView price_state;
        ImageView title_img;
    }
}
