package com.zhuoyi.fauction.ui.hzjd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.JDModel;
import com.zhuoyi.fauction.model.MyFauction;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/12.
 */
public class JDDetailRecycleAdapter extends RecyclerView.Adapter<JDDetailRecycleAdapter.ViewHoler> {

    private LayoutInflater mInflater;

    private Context mContext;

    List<JDModel.DataBean.PictureBean> picture;


    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }


    public JDDetailRecycleAdapter(Context context,  List<JDModel.DataBean.PictureBean> picture) {
        mContext = context;
        this.picture = picture;
        mInflater = LayoutInflater.from(context);
    }


    //public String[] urls={ Constants.url_app_qbcz, Constants.url_app_buy, Constants.url_app_shjf, Constants.url_app_sjcz, Constants.url_app_transfer};

    //创建ViewHolder并初始化赋值
    @Override
    public ViewHoler onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.detail_pic_item, parent, false);
        // View  viewParent=mInflater.inflate(R.layout.activity_second_ui,parent,false);
        //View viewBmfw = viewParent.findViewById(R.id.recycle_yzg);
      /*  ViewGroup.LayoutParams layoutParams = viewBmfw.getLayoutParams();
        int measureHeight=viewBmfw.getMeasuredHeight();*/


        ViewHoler viewHoler = new ViewHoler(view);
        viewHoler.detail_pic_item = (ImageView) view.findViewById(R.id.pic_img);



        return viewHoler;
    }

    //设置值
    @Override
    public void onBindViewHolder(final ViewHoler holder, final int position) {
        //int postions=position;
        // holder.mIv.setBackgroundResource(imgs[postpositionions]);
        // holder.mGv.getParent().getParent()

            if(picture.get(position).getPic_name()!=null){
                Picasso.with(mContext).load(picture.get(position).getPic_name()).into(holder.detail_pic_item);
            }


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
      /*  if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return picture.size();
    }
    public static class ViewHoler extends RecyclerView.ViewHolder{

        public ViewHoler(View itemView) {

            super(itemView);

        }

        ImageView detail_pic_item;



    }


}
