package com.zhuoyi.fauction.ui.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhuoyi.fauction.R;


public class TableItemAdapter extends RecyclerView.Adapter<TableItemAdapter.ViewHoler>{

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




    public TableItemAdapter(Context context) {
        mContext=context;

        mInflater=LayoutInflater.from(context);
    }



    //public String[] urls={ Constants.url_app_qbcz, Constants.url_app_buy, Constants.url_app_shjf, Constants.url_app_sjcz, Constants.url_app_transfer};

    //创建ViewHolder并初始化赋值
    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.fragment_home_gallery_item, parent, false);
       // View  viewParent=mInflater.inflate(R.layout.activity_second_ui,parent,false);
        //View viewBmfw = viewParent.findViewById(R.id.recycle_yzg);
      /*  ViewGroup.LayoutParams layoutParams = viewBmfw.getLayoutParams();
        int measureHeight=viewBmfw.getMeasuredHeight();*/



        ViewHoler viewHoler=new ViewHoler(view);
        viewHoler.mIv=(ImageView) view.findViewById(R.id.id_index_gallery_item_image);
        viewHoler.mTv=(TextView)view.findViewById(R.id.id_index_gallery_item_text);
        viewHoler.mGv=(RelativeLayout) view.findViewById(R.id.gv_item);


        return viewHoler;
    }

    //设置值
    @Override
    public void onBindViewHolder(final ViewHoler holder, final int position) {
       //int postions=position;
       // holder.mIv.setBackgroundResource(imgs[postpositionions]);
       // holder.mGv.getParent().getParent()
        holder.mGv.setBackgroundResource(imgs[position]);

        holder.mTv.setText(img_text[position]);

       /* holder.mGv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!"".equals(Constants.urls[position])&&Constants.urls[position]!=null){
                    Intent intent=new Intent();
                    intent.setClass(mContext,WebShowActivity.class);
                    intent.putExtra("url",Constants.urls[position]);
                    mContext.startActivity(intent);
                }else{
                    Toast.makeText(mContext,"暂未开放", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        //如果设置了回调，则设置点击事件
        if(mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return imgs.length;
    }

    public static class ViewHoler extends RecyclerView.ViewHolder{

        public ViewHoler(View itemView) {
            super(itemView);
        }
        TextView mTv;
        ImageView mIv;
        RelativeLayout mGv;


    }
}
