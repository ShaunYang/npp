package com.zhuoyi.fauction.ui.category.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.model.Category;

import java.util.List;

/**
 * Created by Administrator on 2016/5/17.
 */
public class GalleryAdapter extends
        RecyclerView.Adapter<GalleryAdapter.ViewHolder>
{

    /**
     * ItemClick的回调接口
     * @author zhy
     *
     */
    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<Integer> mDataImg;
    private List<String> mDataString;
    private List<Category> mCategories;
    Context mContext;

    public GalleryAdapter(Context context, List<Category> categories)
    {
        mInflater = LayoutInflater.from(context);
        mCategories=categories;
        mContext=context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        ImageView mImg;
        TextView mTxt;
    }

    @Override
    public int getItemCount()
    {
        return mCategories.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.fragment_home_gallery_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        viewHolder.mTxt=(TextView)view.findViewById(R.id.id_index_gallery_item_text);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
        //viewHolder.mImg.setImageResource(mCategories.);
        viewHolder.mTxt.setText(mCategories.get(i).getTitle());
        if(mCategories.get(i).getSelect_img()!=null){
            Picasso.with(mContext).load(mCategories.get(i).getSelect_img()).into(viewHolder.mImg);
        }



        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}
