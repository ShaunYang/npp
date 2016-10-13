package com.zhuoyi.fauction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.PackDo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SelectPackAdapter extends BaseAdapter {


    private LayoutInflater mInflater;

    private Context mContext;


    List<PackDo.DataBean> fauctionDos;

    boolean isDaiZhuang=false;

    public SelectPackAdapter(Context context, List<PackDo.DataBean> fauctionDos,boolean daiZhuang) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);
        isDaiZhuang=daiZhuang;

    }

    int mSelect = 0;   //选中项



    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
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
            convertView = mInflater.inflate(R.layout.pack_item, null);
            viewHolder = new ViewHolder(convertView);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


      /* if(position==0){
            viewHolder.selectPack.setVisibility(View.VISIBLE);
            Common.packingId=fauctionDos.get(position).getId();
        }*/
        if(!isDaiZhuang){
            viewHolder.bottomTwo.setVisibility(View.VISIBLE);
            viewHolder.title.setText(fauctionDos.get(position).getTitle());
            viewHolder.sprice.setText(fauctionDos.get(position).getPrice());
            viewHolder.kzzl.setText(fauctionDos.get(position).getNum());
            viewHolder.xz.setText(fauctionDos.get(position).getCaseX());
            viewHolder.gg.setText(fauctionDos.get(position).getSpec());

        }else{
            viewHolder.bottomTwo.setVisibility(View.GONE);
            viewHolder.title.setText(fauctionDos.get(position).getTitle());
            viewHolder.sprice.setText(fauctionDos.get(position).getPrice());
            viewHolder.kzzl.setText(fauctionDos.get(position).getNum());

        }

        if (fauctionDos.get(position).getPic() != null) {
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).into(viewHolder.titleImg);
        }

        if(mSelect==position){
            viewHolder.selectPack.setVisibility(View.VISIBLE);  //选中项背景
        }else{
            viewHolder.selectPack.setVisibility(View.GONE);  //其他项背景
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
        @Bind(R.id.sprice)
        TextView sprice;
        @Bind(R.id.kzzl)
        TextView kzzl;
        @Bind(R.id.xztitle)
        TextView xztitle;
        @Bind(R.id.xz)
        TextView xz;
        @Bind(R.id.gg)
        TextView gg;
        @Bind(R.id.select_pack)
        ImageView selectPack;
        @Bind(R.id.bottomTwo)
        LinearLayout bottomTwo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
