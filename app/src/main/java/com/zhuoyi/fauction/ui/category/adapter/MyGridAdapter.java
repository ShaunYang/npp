package com.zhuoyi.fauction.ui.category.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.squareup.picasso.Picasso;
import com.yintai.DatabaseManager;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.home.activity.HomeTabActivity;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;


public class MyGridAdapter extends BaseAdapter {
	private Context mContext;
    private int pos;
	public String[] img_text ={ "玉米", "青瓜", "尖椒", "四季豆","花菜","冬笋","锥栗","苦瓜","毛豆" };
	public int[] imgs = {R.drawable.category_yumi,R.drawable.category_qinggua,R.drawable.category_lajiao,R.drawable.category_sijidou
				,R.drawable.category_huocai,R.drawable.category_dongshun,R.drawable.category_zuili,R.drawable.category_kugua,R.drawable.category_maodou};
	public int[] imgs_a = {R.drawable.category_yumi_a,R.drawable.category_qinggua_a,R.drawable.category_lajiao_a,R.drawable.category_sijidou_a
			,R.drawable.category_huacai_a,R.drawable.category_dongsun_a,R.drawable.category_zuili_a,R.drawable.category_kugua_a,R.drawable.category_maodou_a};

	public String[] urls;

	public MyGridAdapter(Context mContext) {
		super();

		this.mContext = mContext;
	}

	public int[] getImgs() {
		return imgs;
	}

	public void setImgs(int[] imgs) {
		this.imgs = imgs;
	}

	public String[] getImg_text() {
		return img_text;
	}

	public void setImg_text(String[] img_text) {
		this.img_text = img_text;
	}

	public int[] getImgs_a() {
		return imgs_a;
	}

	public void setImgs_a(int[] imgs_a) {
		this.imgs_a = imgs_a;
	}

	public List<Category> categories;

	public List<Category> getCategories() {
		return categories;
	}

	DbUtils db ;

	public void setCategories(List<Category> categories) {

		this.categories = categories;
		db= DatabaseManager.getInstance().getDb();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categories.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.tv_item);
		final ImageView iv = BaseViewHolder.get(convertView, R.id.iv_item);
		RelativeLayout gv=BaseViewHolder.get(convertView,R.id.gv_item);
		if(position==0){
			iv.setBackgroundResource(R.drawable.all_category_nor);
			//iv.setImageBitmap();

			tv.setText(categories.get(position).getTitle());



			gv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//iv.setBackgroundResource(R.drawable.all_category_sel);

					//iv.setImageBitmap(categories.get(position).getSelect_img());
					Intent intent = new Intent();
					intent.putExtra("tab", categories.get(position).getId());
					Common.tab = categories.get(position).getId();
					intent.setClass(mContext, CategoryDoingTabActivity.class);

					mContext.startActivity(intent);

				}
			});
		}else{
			if(categories.get(position).getNormal_img()!=null){
				Picasso.with(mContext).load(categories.get(position).getNormal_img()).into(iv);
			}
			//iv.setImageBitmap();

			tv.setText(categories.get(position).getTitle());



			gv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//Picasso.with(mContext).load(categories.get(position).getSelect_img()).into(iv);


					//iv.setImageBitmap(categories.get(position).getSelect_img());
					Intent intent = new Intent();
					intent.putExtra("tab",categories.get(position).getId());
					Common.tab=categories.get(position).getId();
					intent.setClass(mContext, CategoryDoingTabActivity.class);

					mContext.startActivity(intent);

				}
			});
		}



		return convertView;
	}

}
