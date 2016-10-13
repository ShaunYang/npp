package com.zhuoyi.fauction.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;

import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.ui.category.adapter.CategoryGridViewTopAdapter;
import com.zhuoyi.fauction.ui.category.fragment.CategoryDoListFragment;

import java.util.List;

import static com.zhuoyi.fauction.R.layout.gridview_category;

/**
 * Created by Administrator on 2016/5/27.
 */
public class SelectPopWindow extends PopupWindow{
    private View mMenuView;
    private GridView mGridView;


    public SelectPopWindow(Activity context,List<Category> categories,final ViewPager viewPager) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.gridview_category, null);

        mGridView=(GridView)mMenuView.findViewById(R.id.gridview);
        //
        mGridView.setAdapter(new CategoryGridViewTopAdapter(context, categories));

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                 viewPager.setCurrentItem(position);
                                                 dismiss();


                                             }
                                         }
        );

        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);

      //  this.setBackgroundDrawable(new PaintDrawable());
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xFFFFFF);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

}


