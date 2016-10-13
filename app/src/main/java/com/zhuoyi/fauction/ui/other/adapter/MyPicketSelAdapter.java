package com.zhuoyi.fauction.ui.other.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.MyPicket;
import com.zhuoyi.fauction.model.MyPicketSel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MyPicketSelAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyPicketSel.DataBean> fauctionDos;

    int mSelect = 0;   //选中项

    public MyPicketSelAdapter(Context context, List<MyPicketSel.DataBean> fauctionDos) {
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
            convertView = mInflater.inflate(R.layout.mypicket_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.price.setText("￥" + fauctionDos.get(position).getPrice());
        viewHolder.valid_price.setText("满"+fauctionDos.get(position).getValid_price()+"可用");
        viewHolder.valid_time.setText("有效期至"+fauctionDos.get(position).getValid_time());
        int type = Integer.parseInt(fauctionDos.get(position).getType());
        if(type==1){
            viewHolder.type.setText("限于支付保证金时使用");
        }else if(type==2){
            viewHolder.type.setText("限于支付货款时使用");
        }else if(type==3){
            viewHolder.type.setText("通用");
        }
        int state = fauctionDos.get(position).getState();
        int status = Integer.parseInt(fauctionDos.get(position).getStatus());
        if(state==0){
            //过期
            viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_his);
        }else if(state==1){
            if(status==3){
                //已使用
                viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_used);
            }else if(status==2){
                //正常
            }
        }


        if(selectItem==position){
            viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_sel); //选中项背景
        }else{
            viewHolder.pick_bg.setBackgroundResource(R.drawable.mypicket_nor);  //其他项背景
        }


        return convertView;


    }

    public  void setSelectItem(int selectItem) {
        this.selectItem = selectItem;
    }
    private int  selectItem=-1;
    public void changeSelected(int positon){ //刷新方法
        if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
        }
    }



    static class ViewHolder {

        @Bind(R.id.valid_price)
        TextView valid_price;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.type)
        TextView type;
        @Bind(R.id.valid_time)
        TextView valid_time;
        @Bind(R.id.pick_bg)
        LinearLayout pick_bg;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
