package com.zhuoyi.fauction.ui.hzjd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.JDModel;
import com.zhuoyi.fauction.model.ProductDetail2;

import java.util.List;

/**
 * Created by Administrator on 2016/5/27.
 */
public class DetailJDPicAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;



    List<JDModel.DataBean.PictureBean> picture;

    public DetailJDPicAdapter(Context context,List<JDModel.DataBean.PictureBean> picture) {
        mContext=context;
        this.picture=picture;
        mInflater=LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return picture.size();
    }

    @Override
    public Object getItem(int position) {
        return picture.get(position);
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
            convertView = mInflater.inflate(R.layout.detail_pic_item, null);



            viewHolder.pic_img=(ImageView) convertView.findViewById(R.id.pic_img);
            viewHolder.pic_img.setTag(picture.get(position).getPic_name());
            viewHolder.pic_img.setImageDrawable(null);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RequestCreator load = Picasso.with(mContext).load(picture.get(position).getPic_name());
        if(viewHolder.pic_img.getTag().equals(picture.get(position).getPic_name())){
            if(picture.get(position).getPic_name()!=null){
                load.into(viewHolder.pic_img);
            }
        }




        return convertView;


    }





    //public String[] urls={ Constants.url_app_qbcz, Constants.url_app_buy, Constants.url_app_shjf, Constants.url_app_sjcz, Constants.url_app_transfer};

    //创建ViewHolder并初始化赋值
   /* @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.category_doing_item, parent, false);
       // View  viewParent=mInflater.inflate(R.layout.activity_second_ui,parent,false);
        //View viewBmfw = viewParent.findViewById(R.id.recycle_yzg);
      *//*  ViewGroup.LayoutParams layoutParams = viewBmfw.getLayoutParams();
        int measureHeight=viewBmfw.getMeasuredHeight();*//*



        ViewHoler viewHoler=new ViewHoler(view);

        viewHoler.mGv=(RelativeLayout) view.findViewById(R.id.gv);
        viewHoler.title=(TextView) view.findViewById(R.id.title);
        viewHoler.stock_num=(TextView) view.findViewById(R.id.stock_num);
        viewHoler.cur_price=(TextView) view.findViewById(R.id.cur_price);
        viewHoler.residual_time=(TextView) view.findViewById(R.id.residual_time);
        viewHoler.price_state=(ImageView) view.findViewById(R.id.price_state);
        viewHoler.title_img=(ImageView) view.findViewById(R.id.title_img);

        return viewHoler;
    }*/

    //设置值
  /*  @Override
    public void onBindViewHolder(final ViewHoler holder, final int position) {
       //int postions=position;
       // holder.mIv.setBackgroundResource(imgs[postpositionions]);
       // holder.mGv.getParent().getParent()
       *//* holder.mGv.setBackgroundResource(imgs[position]);*//*
        holder.title.setText(fauctionDos.get(position).getTitle());
        holder.title.setText(fauctionDos.get(position).getTitle());
        holder.cur_price.setText("￥"+fauctionDos.get(position).getCurPrice()+"/斤");
        holder.stock_num.setText(fauctionDos.get(position).getStock()+"斤/"+fauctionDos.get(position).getNum()+"斤");
        holder.residual_time.setText(fauctionDos.get(position).getResidual_time());
        //根据状态判断
        if(true){

        }

       holder.mGv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent();
                    intent.setClass(mContext,ProductFauctionDetailDoingActivity.class);

                    mContext.startActivity(intent);

            }
        });*/

       /* //如果设置了回调，则设置点击事件
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }*/
    private static class ViewHolder{



        ImageView pic_img;
    }



}
