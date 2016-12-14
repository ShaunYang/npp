package com.zhuoyi.fauction.ui.category.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.view.CommonTopBar;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.ui.category.fragment.CategoryDoListFragment;
import com.zhuoyi.fauction.ui.home.event.PriceSortEventData;
import com.zhuoyi.fauction.ui.home.fragment.HRJXFragment;
import com.zhuoyi.fauction.ui.home.fragment.JRZQFragment;
import com.zhuoyi.fauction.ui.home.fragment.PMYZFragment;
import com.zhuoyi.fauction.ui.home.fragment.ZXCJFragment;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhuoyi.fauction.view.SelectPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CategoryDoingTabActivity extends BaseActivity {


    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    @Bind(R.id.spinner_nav)
    Spinner mTopSpinner;
    @Bind(R.id.price_sort)
    TextView price_sort;
    @Bind(R.id.categoies)
    ImageView all_category;
    @Bind(R.id.top_bar)
    RelativeLayout top_bar;
   // String[] title = { "捡漏专区", "拍卖预展", "火热进行","最新成交" };
    List<CategoryDoListFragment> categoryDoListFragments;
    List<Category> mCategories;

    List<Category> allCategory;

    //自定义的弹出框类
    SelectPopWindow menuWindow;
    //纪录当前fragment
    int curItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initComponent(Bundle bundle) {
        mCategories=Common.allCategories;

        setContentView(R.layout.fragment_category_doing);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        String tab = intent.getStringExtra("tab");
        //
        categoryDoListFragments=new ArrayList<CategoryDoListFragment>();
        allCategory=new ArrayList<Category>();
        /*Category first=new Category();
        first.setTitle("全部");
        first.setId("-1");*/

       /* CategoryDoListFragment fcategoryDoListFragment=new CategoryDoListFragment();
        fcategoryDoListFragment.setCategory(first);
        fcategoryDoListFragment.setOrder(2);
        allCategory.add(first);
        categoryDoListFragments.add(fcategoryDoListFragment);*/
        if(mCategories!=null){
            for(Category category:mCategories){
                CategoryDoListFragment categoryDoListFragment=new CategoryDoListFragment();
                categoryDoListFragment.setCategory(category);
                categoryDoListFragment.setOrder(2);
                categoryDoListFragments.add(categoryDoListFragment);
                allCategory.add(category);
            }
        }

        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        // tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setDividerColor(0xFFFFFF);
        for(int i=0;i<categoryDoListFragments.size();i++){
            CategoryDoListFragment categoryDoListFragment=categoryDoListFragments.get(i);

            if(categoryDoListFragment.getCategory().getId().equals(Common.tab)){
                viewPager.setCurrentItem(i);
                break;
            }

        }

        mTopSpinner.setSelection(1);


        mTopSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){

                TextView tv = (TextView)view;
                if(tv!=null){
                    tv.setTextColor(getResources().getColor(R.color.white));    //设置颜色

                    tv.setTextSize(22);    //设置大小

                    tv.setGravity(Gravity.CENTER);   //设置居中
                    if(position==0){

                        Intent intent=new Intent(CategoryDoingTabActivity.this,CategoryPreTabActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(position==1){

                    } else if(position==2){

                        Intent intent=new Intent(CategoryDoingTabActivity.this,CategoryEndTabActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){}
        });
        //mainCategory.setTitle("正在进行");
        //

       /* // 建立数据源
        String[] mItems = getResources().getStringArray(R.array.spinnername);
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        mTopSpinner.setAdapter(_Adapter);*/



    }

    private void sortFragmentCategory(int order,int curItem) {
        setContentView(R.layout.fragment_category_doing);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        categoryDoListFragments=new ArrayList<CategoryDoListFragment>();
        allCategory=new ArrayList<Category>();
        Category first=new Category();
        first.setTitle("全部");
        first.setId("-1");

        CategoryDoListFragment fcategoryDoListFragment=new CategoryDoListFragment();
        fcategoryDoListFragment.setCategory(first);
        fcategoryDoListFragment.setOrder(order);
        allCategory.add(first);
        categoryDoListFragments.add(fcategoryDoListFragment);
        for(Category category:mCategories){
            CategoryDoListFragment categoryDoListFragment=new CategoryDoListFragment();
            categoryDoListFragment.setCategory(category);
            categoryDoListFragment.setOrder(order);
            categoryDoListFragments.add(categoryDoListFragment);
            allCategory.add(category);
        }
        //viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        // tabs = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);
        tabs.setDividerColor(0xFFFFFF);
        viewPager.setCurrentItem(curItem);

    }

    @OnClick(R.id.categoies)
    public void showAllCategory(){
        //实例化SelectPicPopupWindow
        menuWindow = new SelectPopWindow(CategoryDoingTabActivity.this,allCategory,viewPager);
        //显示窗口
        menuWindow.showAsDropDown(top_bar,0,0); //设置layout在PopupWindow中显示的位置


    }

    //popwindow价格排序选择
    @OnClick(R.id.price_sort)
    public void selectPriceSort(){
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = mLayoutInflater.inflate(R.layout.list_pop, null);// R.layout.pop为 PopupWindow 的布局文件
        final TextView low_height=(TextView)contentView.findViewById(R.id.low_height);
        final TextView height_low=(TextView)contentView.findViewById(R.id.height_low);

        final PopupWindow pop = new PopupWindow(contentView, LinearLayout.LayoutParams.WRAP_CONTENT , LinearLayout.LayoutParams.WRAP_CONTENT);
        //pop.setBackgroundDrawable(new BitmapDrawable());
        // 指定 PopupWindow 的背景
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setFocusable(true);


        //
        pop.showAsDropDown(price_sort,0,0);
        low_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // low_height.setTextColor(getResources().getColor(R.color.common_topbar_bg));
                curItem = viewPager.getCurrentItem();


                BusProvider.getInstance().post(new PriceSortEventData(2));
                pop.dismiss();

            }
        });
        height_low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  height_low.setTextColor(getResources().getColor(R.color.common_topbar_bg));
                curItem = viewPager.getCurrentItem();



                BusProvider.getInstance().post(new PriceSortEventData(3));
                pop.dismiss();
            }
        });
    }
    class MyPagerAdapter extends FragmentPagerAdapter {



        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
           return   categoryDoListFragments.get(position);
        }

        @Override
        public int getCount() {

            return categoryDoListFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return categoryDoListFragments.get(position).getCategory().getTitle();
        }

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void pause() {

    }

    @Override
    protected void resume() {

    }

    @Override
    protected void destory() {

    }

    @Override
    protected void onNetWorkReConnected() {

    }

    @Override
    protected void onNetWorkConnectFail() {

    }

    @OnClick(R.id.back) void onBackClick() {
        onBackPressed();
    }


}
