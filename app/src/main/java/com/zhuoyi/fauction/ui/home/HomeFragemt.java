package com.zhuoyi.fauction.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.squareup.picasso.Picasso;
import com.unionpay.UPPayAssistEx;
import com.yintai.DatabaseManager;
import com.yintai.common.util.DialogUtil;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.yintai.common.view.CommonTopBar;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.MainBanner;
import com.zhuoyi.fauction.model.NewFinishFauction;
import com.zhuoyi.fauction.model.User;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryEndTabActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryPreTabActivity;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.home.activity.HomeTabActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailEndActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailPreActivity;
import com.zhuoyi.fauction.ui.home.adapter.GalleryAdapter;
import com.zhuoyi.fauction.ui.home.view.SlideShowView;
import com.zhuoyi.fauction.ui.other.BondPayActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class HomeFragemt extends Fragment {

    @Bind(R.id.ssv_top_ad)
    SlideShowView ssvTopAd;

    @Bind(R.id.home_iv_zxcj)
    ImageButton home_iv_zxcj;

    @Bind(R.id.home_iv_hrjx)
    ImageButton home_iv_hrjx;

    @Bind(R.id.home_iv_pmyz)
    ImageButton home_iv_pmyz;

    @Bind(R.id.home_iv_jrzq)
    ImageButton home_iv_jrzq;
    @Bind(R.id.main_recommond)
    LinearLayout main_recommond;

    @Bind(R.id.id_recyclerview_horizontal)
    RecyclerView getmRecyclerView;

    @Bind(R.id.home_ad_img)
    ImageView home_ad_img;

    @Bind(R.id.top_turnover_ll) LinearLayout topTurnoverLl;
    @Bind(R.id.trunover)
    TextView trunover;

    public String TAG="HomeFragemt";


    private RecyclerView mRecyclerView;
    private GalleryAdapter mAdapter;
    private List<Integer> mDatas;
    private List<String> mDataString;
    private View viewHolder;
    //
    private List<String> mImgBannerUrl;
    private ArrayList<MainBanner> mainBanners;

    List<Category> categoryList;

    List<Category> categories;

    public static HomeFragemt newInstance(String param1,String param2) {
        HomeFragemt fragment = new HomeFragemt();
        return fragment;
    }

    boolean isFirst=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_home_fragemt, container, false);
            ButterKnife.bind(this, viewHolder);
            //List<Category> categories= Common.categories;
            BusProvider.getInstance().register(this);
            Common.home_tab=0;
            initComponent();

        }
        ViewGroup parent = (ViewGroup) viewHolder.getParent();
        if (parent != null) {
            parent.removeView(viewHolder);
        }
        ButterKnife.bind(this, viewHolder);
        return viewHolder;
    }

    @Override
    public void onStart() {
        //ssvTopAd.setView(null);

        Logger.i(TAG,"222222");
        /*List<Category> categories= Common.categories;
        if( mAdapter!=null){
            mAdapter = new GalleryAdapter(getActivity(),categories);

            getmRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener()
            {
                @Override
                public void onItemClick(View view, int position)
                {
                    Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT)
                            .show();
                }
            });
            Logger.i(TAG,"333333333");
        }*/
        super.onStart();
    }




    @Override
    public void onResume(){
        if(!isFirst){
            initComponentResume();
        }

        super.onResume();
    }



    @Override
    public void onPause() {
        //ssvTopAd=null;
        super.onPause();
    }

    private void initComponentResume() {
        //ssvTopAd.setView(null);
        //ssvTopAd=(SlideShowView)getActivity().findViewById(R.id.ssv_top_ad);
        initDatasResume();

        try {
            //Common.categories=DatabaseManager.getInstance().getDb().findAll(Selector.from(Category.class).where("user_id","=",ConfigUserManager.getUserId(getActivity())));
            //当前用户是否已经登录
//            if(ConfigUserManager.isAlreadyLogin(getActivity())){
//                memeberLoginPost();
//
//            }else{
            categories=Common.categories;
            if(categories!=null){
                if(categories.size()>0){
                    //得到控件
                    if(categories.size()>1){
                        //得到控件

                        getmRecyclerView.setVisibility(View.VISIBLE);
                        main_recommond.setVisibility(View.GONE);
                        //设置布局管理器
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                        getmRecyclerView.setLayoutManager(linearLayoutManager);
                        //设置适配器
                        mAdapter = new GalleryAdapter(getActivity(),categories);

                        getmRecyclerView.setAdapter(mAdapter);
                        mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener()
                        {
                            @Override
                            public void onItemClick(View view, int position)
                            {
                                Intent intent = new Intent();
                                intent.putExtra("tab",categories.get(position).getId());
                                Common.tab=categories.get(position).getId();
                                intent.setClass(getActivity(), CategoryDoingTabActivity.class);

                                startActivity(intent);
                            }
                        });
                        //getmRecyclerView.setAdapter(mAdapter);
                    }else{
                        getmRecyclerView.setVisibility(View.GONE);
                        main_recommond.setVisibility(View.VISIBLE);
                        recommendAdPost();
                    }
                   /* getmRecyclerView.setVisibility(View.VISIBLE);
                    main_recommond.setVisibility(View.GONE);
                    //设置布局管理器
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                    getmRecyclerView.setLayoutManager(linearLayoutManager);
                    //设置适配器
                    mAdapter = new GalleryAdapter(getActivity(),categories);

                    getmRecyclerView.setAdapter(mAdapter);
                    mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener()
                    {
                        @Override
                        public void onItemClick(View view, int position)
                        {
                            Intent intent = new Intent();
                            intent.putExtra("tab",categories.get(position).getId());
                            Common.tab=categories.get(position).getId();
                            intent.setClass(getActivity(), CategoryDoingTabActivity.class);

                            startActivity(intent);
                        }
                    });*/
                    //getmRecyclerView.setAdapter(mAdapter);
                }else {
                    getmRecyclerView.setVisibility(View.GONE);
                    main_recommond.setVisibility(View.VISIBLE);
                    recommendAdPost();

                }

            }else{
                getmRecyclerView.setVisibility(View.GONE);
                main_recommond.setVisibility(View.VISIBLE);
                recommendAdPost();

            }
            // }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    private void initComponent() {
        //ssvTopAd.setView(null);
        //ssvTopAd=(SlideShowView)getActivity().findViewById(R.id.ssv_top_ad);
        //isFirst=false;
        initDatas();

        try {
            //Common.categories=DatabaseManager.getInstance().getDb().findAll(Selector.from(Category.class).where("user_id","=",ConfigUserManager.getUserId(getActivity())));
            //当前用户是否已经登录
//            if(ConfigUserManager.isAlreadyLogin(getActivity())){
//                memeberLoginPost();
//
//            }else{
                categories= Common.categories;
                if(categories!=null){
                    if(categories.size()>0){

                        if(categories.size()>1){
                            //得到控件

                            getmRecyclerView.setVisibility(View.VISIBLE);
                            main_recommond.setVisibility(View.GONE);
                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                            getmRecyclerView.setLayoutManager(linearLayoutManager);
                            //设置适配器
                            mAdapter = new GalleryAdapter(getActivity(),categories);

                            getmRecyclerView.setAdapter(mAdapter);
                            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener()
                            {
                                @Override
                                public void onItemClick(View view, int position)
                                {
                                    Intent intent = new Intent();
                                    intent.putExtra("tab",categories.get(position).getId());
                                    Common.tab=categories.get(position).getId();
                                    intent.setClass(getActivity(), CategoryDoingTabActivity.class);

                                    startActivity(intent);
                                }
                            });
                            //getmRecyclerView.setAdapter(mAdapter);
                        }else{
                            getmRecyclerView.setVisibility(View.GONE);
                            main_recommond.setVisibility(View.VISIBLE);
                            recommendAdPost();
                        }

                    }else {
                        getmRecyclerView.setVisibility(View.GONE);
                        main_recommond.setVisibility(View.VISIBLE);
                        recommendAdPost();

                    }

                }else{
                    getmRecyclerView.setVisibility(View.GONE);
                    main_recommond.setVisibility(View.VISIBLE);
                    recommendAdPost();

                }
           // }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }
    @OnClick(R.id.home_iv_hrjx)
    public void toHomeTabHRJX(){
        Intent intent = new Intent();
        intent.putExtra("tab",2);
        intent.setClass(getActivity(), HomeTabActivity.class);

        startActivity(intent); //这里用getActivity().startActivity(intent);

    }

    //首页广告图
    private void mainIndexAdPost(){
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Main","indexAd");

        OkHttpUtils.post()
                .url(Constant.MAIN_INDEXAD)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG + "11111111111111", response);
                        mainBanners = new ArrayList<MainBanner>();
                        mImgBannerUrl = new ArrayList<String>();
                        JSONObject jsonObj = JSONObject.parseObject(response);
                        JSONArray jsonArray = jsonObj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject bannerJsonObj = jsonArray.getJSONObject(i);
                            MainBanner mainBanner = new MainBanner();
                            mainBanner.setPic_url(bannerJsonObj.getString("pic_url"));
                            mainBanner.setTitle(bannerJsonObj.getString("title"));
                            mainBanner.setId(bannerJsonObj.getInteger("id"));
                            mainBanner.setUrl(bannerJsonObj.getString("url"));
                            mainBanner.setStatus(bannerJsonObj.getInteger("status"));
                            mainBanners.add(mainBanner);
                            mImgBannerUrl.add(bannerJsonObj.getString("pic_url"));

                        }

                        String[] strings = mImgBannerUrl.toArray(new String[mImgBannerUrl.size()]);
                        if(strings!=null){
                            if(ssvTopAd!=null){
                                ssvTopAd.setIsJump(true);
                                ssvTopAd.setMainBannerList(mainBanners);
                                ssvTopAd.setView(strings);
                            }

                        }
                        mainTurnOverPost();


                    }
                });
    }

//    private void memeberLoginPost() {
//        User user=null;
//        try {
//            user=DatabaseManager.getInstance().getDb().findById(User.class,ConfigUserManager.getUserId(getActivity()));
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//        String timestamp= DateUtil.getStringDate();
//
//        String sign= Util.createSign(getActivity(),timestamp,"Member","login");
//
//        OkHttpUtils.post()
//                .url(Constant.MEMBER_LOGIN)
//                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
//                .addParams("timestamp", timestamp)
//                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
//                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
//                .addParams("mobile",ConfigUserManager.getRegistPhone(getActivity()))
//                .addParams("pwd", MD5Util.getMD5String(MD5Util.getMD5String(ConfigUserManager.getLoginPwd(getActivity())) + ConfigUserManager.getDataAuth(getActivity())))
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        Logger.i(TAG+"222222222222",response);
//                        ConfigUserManager.setAlreadyLogin(getActivity(),true);
//                        JSONObject retJsonObj= JSONObject.parseObject(response);
//                        int ret=retJsonObj.getInteger("ret");
//                        if(ret==200){
//                            JSONObject data=retJsonObj.getJSONObject("data");
//                            String uid=data.getString("uid");
//                            JSONArray array=data.getJSONArray("category_id");
//                            String categoryString="";
//                            StringBuilder sb=new StringBuilder();
//
//                            if(array!=null){
//                                int size=array.size();
//                                if(size==1){
//                                    sb.append(array.getString(0));
//                                    categoryString= sb.toString();
//                                }else{
//                                    for(int i=0;i<size;i++){
//                                        if(i==(size-1)){
//                                            sb.append(array.getString(i));
//                                        }else{
//                                            sb.append(array.getString(i)+"|");
//                                        }
//                                    }
//                                    categoryString= URLEncoder.encode(sb.toString());
//                                }
//
//                            }else{
//                                categoryString="";
//                            }
//                            ConfigUserManager.setUserId(getActivity(),uid);
//                            //User user=new User();
//                            //去数据库取用户 如果没有就重新创建一个user并发送获取category请求
//                            //DbUtils db = DatabaseManager.getInstance().getDb();
//                            try {
//                                // User user=db.findFirst(Selector.from(User.class).where("id","=",uid));
//                               /* if(user!=null){
//                                    Common.categories=DatabaseManager.getInstance().getDb().findAll(Selector.from(Category.class).where("user_id","=",ConfigUserManager.getUserId(getActivity())));
//                                }else{*/
//                                // 重新创建一个user并发送获取category请求
//                                mainIndexCategoryPost(categoryString);
//                                // }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                          /*  if(){
//
//                            }*/
//                        }
//
//
//                    }
//                });
//    }

//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        mainTurnOverPost();
//        super.onViewCreated(view, savedInstanceState);
//    }

    private void mainTurnOverPost() {

        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Main","turnover");

        OkHttpUtils.post()
                .url(Constant.MAIN_TURNOVER)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG+"addddddd",response);

                        JSONObject retJsonObj= JSONObject.parseObject(response);
                        int code = retJsonObj.getIntValue("code");
                        if(code==0){
                            //topTurnoverLl.setVisibility(View.VISIBLE);
                            Gson gson=new Gson();
                            NewFinishFauction newFinishFauction = gson.fromJson(response, NewFinishFauction.class);
                            List<NewFinishFauction.DataBean> datas = newFinishFauction.getData();
                            StringBuilder sb=new StringBuilder();
                            for(NewFinishFauction.DataBean data:datas){
                                sb.append("用户："+data.getUser_name()+"成交"+data.getTitle()+" "+data.getNum()+data.getUnit()+"   ");
                            }
                           // trunover=(TextView)getActivity().findViewById(R.id.trunover);
                            if(sb!=null){
                                if(sb.toString()!=null){
                                    if(trunover!=null){
                                        trunover.setText(sb.toString());
                                        trunover.requestFocus();
                                        trunover.setFocusable(true);
                                        trunover.setHorizontallyScrolling(true);
                                        trunover.setSingleLine(true);
                                        trunover.setFocusableInTouchMode(true);
                                        trunover.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                                        trunover.setMarqueeRepeatLimit(-1);
                                    }

                                }

                            }




                        }else{
                            //topTurnoverLl.setVisibility(View.GONE);
                        }
                        DialogUtil.dismiss();
                        isFirst=false;

                    }
                });
    }

    private void recommendAdPost() {
        User user=null;
        try {
            user=DatabaseManager.getInstance().getDb().findById(User.class,ConfigUserManager.getUserId(getActivity()));
        } catch (DbException e) {
            e.printStackTrace();
        }
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Main","recommendAd");

        OkHttpUtils.post()
                .url(Constant.MAIN_RECOMMENDAD)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .addParams("id","8")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG+"addddddd",response);

                        JSONObject retJsonObj= JSONObject.parseObject(response);

                        int ret = retJsonObj.getIntValue("ret");
                        if(ret==200){

                            final JSONObject dataObj=retJsonObj.getJSONObject("data");
                            if(home_ad_img!=null){
                                Picasso.with(getActivity()).load(dataObj.getString("pic_url")).resize(home_ad_img.getWidth(),home_ad_img.getHeight())
                                        .into(home_ad_img);
                                main_recommond.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int status = dataObj.getIntValue("status");
                                        if (status == 1) {
                                            Intent intent = new Intent(getActivity(), ProductFauctionDetailPreActivity.class);
                                            intent.putExtra("productId", dataObj.getIntValue("id"));
                                            getActivity().startActivity(intent);
                                        }
                                        if (status == 2) {
                                            Intent intent = new Intent(getActivity(), ProductFauctionDetailDoingActivity.class);
                                            intent.putExtra("productId", dataObj.getIntValue("id"));
                                            getActivity().startActivity(intent);
                                        }
                                        if (status == 3) {
                                            Intent intent = new Intent(getActivity(), ProductFauctionDetailEndActivity.class);
                                            intent.putExtra("productId", dataObj.getIntValue("id"));
                                            getActivity().startActivity(intent);
                                        }


                                    }
                                });

                            }
                        }




                    }
                });
    }

    private void mainIndexCategoryPost(String categoryStr) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Main","indexCategory");

        OkHttpUtils.post()
                .url(Constant.MAIN_INDEXCATEGORY)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .addParams("id",categoryStr)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "333333333333=", response);
                        categoryList = new ArrayList<Category>();
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        JSONArray data = jsonObject.getJSONArray("data");
                        for (int i = 0; i < data.size(); i++) {
                            final Category category = new Category();
                            JSONObject categoryJson = data.getJSONObject(i);
                            category.setId(categoryJson.getString("id"));
                            category.setTitle(categoryJson.getString("title"));
                            JSONObject picJsonObject = categoryJson.getJSONObject("pic");
                            String normal_img = picJsonObject.getString("3");
                            String select_img = picJsonObject.getString("4");
                            category.setSelect_img(select_img);
                            category.setNormal_img(normal_img);

                            categoryList.add(category);
                            // category.setUser_id(ConfigUserManager.getUserId(getActivity()));

                            /*try {
                                DatabaseManager.getInstance().getDb().saveOrUpdate(category);
                            } catch (DbException e) {
                                e.printStackTrace();
                            }*/

                        }
                        Common.categories = categoryList;
                        final List<Category> categories = Common.categories;
                        if (categories != null) {
                            //得到控件

                            getmRecyclerView.setVisibility(View.VISIBLE);
                            main_recommond.setVisibility(View.GONE);
                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

                            getmRecyclerView.setLayoutManager(linearLayoutManager);
                            //设置适配器
                            mAdapter = new GalleryAdapter(getActivity(), categories);

                            getmRecyclerView.setAdapter(mAdapter);
                            mAdapter.setOnItemClickLitener(new GalleryAdapter.OnItemClickLitener() {
                                @Override
                                public void onItemClick(View view, int position) {
//                                    Toast.makeText(getActivity(), position+"", Toast.LENGTH_SHORT)
//                                            .show();
                                    //
                                    Intent intent = new Intent();
                                    intent.putExtra("tab", categories.get(position).getId());
                                    Common.tab = categories.get(position).getId();
                                    intent.setClass(getActivity(), CategoryDoingTabActivity.class);

                                    startActivity(intent);
                                }
                            });
                            //getmRecyclerView.setAdapter(mAdapter);
                        } else {
                            getmRecyclerView.setVisibility(View.GONE);
                            main_recommond.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }

    @OnClick(R.id.home_iv_zxcj)
    public void toHomeTabZXCJ(){
       // UPPayAssistEx.startPay(BondPayActivity.this, null, null, "201609011447318319218", "00");
       Intent intent = new Intent();
        intent.putExtra("tab",3);
        intent.setClass(getActivity(), HomeTabActivity.class);

        startActivity(intent); //这里用getActivity().startActivity(intent);

    }



    @OnClick(R.id.home_iv_pmyz)
    public void toHomeTabPMYZ(){
        Intent intent = new Intent();
        intent.putExtra("tab",1);
        intent.setClass(getActivity(), HomeTabActivity.class);

        startActivity(intent); //这里用getActivity().startActivity(intent);

    }

    @OnClick(R.id.home_iv_jrzq)
    public void toHomeTabJRZQ(){
        Intent intent = new Intent();
        intent.putExtra("tab", 0);
        intent.setClass(getActivity(), HomeTabActivity.class);

        startActivity(intent); //这里用getActivity().startActivity(intent);

    }

    private void initDatas()
    {
        DialogUtil.showDialog(getActivity(), "加载中", false);
        if(ConfigUserManager.isAlreadyLogin(getContext())){

        }
        mainIndexAdPost();


//        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.category_yumi,
//                R.drawable.category_qinggua, R.drawable.category_lajiao, R.drawable.category_sijidou));
//    mDataString=new ArrayList<String>(Arrays.asList("玉米", "青瓜","辣椒","四季豆"));
}

    private void initDatasResume()
    {
        DialogUtil.showDialog(getActivity(), "加载中", false);
       // mainIndexAdPost();
        mainTurnOverPost();


//        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.category_yumi,
//                R.drawable.category_qinggua, R.drawable.category_lajiao, R.drawable.category_sijidou));
//    mDataString=new ArrayList<String>(Arrays.asList("玉米", "青瓜","辣椒","四季豆"));
    }

//
//    private void memeberLoginPost() {
//        String timestamp= DateUtil.getStringDate();
//
//        String sign= Util.createSign(getActivity(),timestamp,"Member","login");
//
//        OkHttpUtils.post()
//                .url(Constant.MEMBER_LOGIN)
//                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
//                .addParams("timestamp",timestamp)
//                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
//                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
//                .addParams("mobile",user_account.getText().toString().trim())
//                .addParams("pwd", MD5Util.getMD5String(MD5Util.getMD5String(user_psd.getText().toString().trim())+ConfigUserManager.getDataAuth(getActivity())))
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        Logger.i(TAG + "login=====", response);
//                        //ConfigUserManager.setAlreadyLogin(getActivity(),true);
//                        JSONObject retJsonObj = JSONObject.parseObject(response);
//                        int ret = retJsonObj.getInteger("ret");
//                        String code = retJsonObj.getString("code");
//                        if (code.equals("-1")) {
//                            ToastUtil.showLongToast(getActivity(), "账户不能为空");
//                        } else if (code.equals("-2")) {
//                            ToastUtil.showLongToast(getActivity(), "密码不能为空");
//                        } else if (code.equals("-3")) {
//                            ToastUtil.showLongToast(getActivity(), "用户不存在或密码错误");
//                        } else if (code.equals("-4")) {
//                            ToastUtil.showLongToast(getActivity(), "用户被禁用");
//                        }else{
//                            //缓存token
//                            ConfigUserManager.setToken(getActivity(),code);
//
//                            JSONObject data = retJsonObj.getJSONObject("data");
//                            String uid = data.getString("uid");
//                            Common.userAvator = data.getString("img");
//                            JSONArray array = data.getJSONArray("category_id");
//                            String categoryString = "";
//                            StringBuilder sb = new StringBuilder();
//
//                            if (array != null) {
//                                int size = array.size();
//                                if (size == 1) {
//                                    sb.append(array.getString(0));
//                                    categoryString = sb.toString();
//                                } else {
//                                    for (int i = 0; i < size; i++) {
//                                        if (i == (size - 1)) {
//                                            sb.append(array.getString(i));
//                                        } else {
//                                            sb.append(array.getString(i) + "|");
//                                        }
//                                    }
//                                    categoryString = URLEncoder.encode(sb.toString());
//                                }
//
//                            } else {
//                                categoryString = "";
//                            }
//                              /*  ConfigUserManager.setUserId(getActivity(), uid);
//                                ConfigUserManager.setLoginPwd(getActivity(), user_psd.getText().toString().trim());*/
//                            //User user=new User();
//                            //去数据库取用户 如果没有就重新创建一个user并发送获取category请求
//                            //DbUtils db = DatabaseManager.getInstance().getDb();
//                            try {
//                                // User user=db.findFirst(Selector.from(User.class).where("id","=",uid));
//                               /* if(user!=null){
//                                    Common.categories=DatabaseManager.getInstance().getDb().findAll(Selector.from(Category.class).where("user_id","=",ConfigUserManager.getUserId(getActivity())));
//                                }else{*/
//                                // 重新创建一个user并发送获取category请求
//                                mainIndexCategoryPost(categoryString);
//                                // }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//
//
//                    }
//                });
//    }
//
//    private void mainIndexCategoryPost(String categoryStr) {
//        String timestamp= DateUtil.getStringDate();
//
//        String sign= Util.createSign(getActivity(),timestamp,"Main","indexCategory");
//
//        OkHttpUtils.post()
//                .url(Constant.MAIN_INDEXCATEGORY)
//                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
//                .addParams("timestamp",timestamp)
//                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
//                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
//                .addParams("id",categoryStr)
//                .build()
//                .execute(new StringCallback() {
//                    @Override
//                    public void onError(Call call, Exception e) {
//
//                    }
//
//                    @Override
//                    public void onResponse(String response) {
//
//                        Logger.i(TAG+"333333333333=",response);
//                        categoryList=new ArrayList<Category>();
//                        JSONObject jsonObject=JSONObject.parseObject(response);
//                        JSONArray data = jsonObject.getJSONArray("data");
//                        for(int i=0;i<data.size();i++){
//                            final Category category=new Category();
//                            JSONObject categoryJson=data.getJSONObject(i);
//                            category.setId(categoryJson.getString("id"));
//                            category.setTitle(categoryJson.getString("title"));
//                            JSONObject picJsonObject=categoryJson.getJSONObject("pic");
//                            String normal_img=picJsonObject.getString("3");
//                            String select_img=picJsonObject.getString("4");
//                            category.setSelect_img(select_img);
//                            category.setNormal_img(normal_img);
//
//                            categoryList.add(category);
//                            // category.setUser_id(ConfigUserManager.getUserId(getActivity()));
//
//                            /*try {
//                                DatabaseManager.getInstance().getDb().saveOrUpdate(category);
//                            } catch (DbException e) {
//                                e.printStackTrace();
//                            }*/
//
//                        }
//                        Common.categories=categoryList;
//                        ConfigUserManager.saveObject(getActivity(), "sel_category", Common.categories);
//                        ToastUtil.showLongToast(getActivity(), "登录成功");
//                        ConfigUserManager.setAlreadyLogin(getActivity(), true);
//                        //ConfigUserManager.isAlreadyLogin(getContext())=true;
//                        //设置记住密码便于下一次启动app登录
//                        // ConfigUserManager.
//                        if(Common.whichActivity==1){
//                            //商品详情跳转过来
//                            Intent intent=new Intent(getActivity(), ProductFauctionDetailDoingActivity.class);
//                            intent.putExtra("productId",Common.proId);
//                            getActivity().startActivity(intent);
//                        }else if(Common.whichActivity==2){
//                            //商品详情跳转过来
//                            Intent intent=new Intent(getActivity(), ProductFauctionDetailPreActivity.class);
//                            intent.putExtra("productId",Common.proId);
//                            getActivity().startActivity(intent);
//                        }else{
//                            ((MainActivity) getActivity()).mTabHost.setCurrentTab(0);
//
//                        }
//                        ((MainActivity)getActivity()).mTabHost.setVisibility(View.VISIBLE);
//
//
//
//                    }
//                });
//    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }



    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
