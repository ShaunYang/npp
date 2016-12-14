package com.zhuoyi.fauction.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.DialogUtil;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.ProductDetail1;
import com.zhuoyi.fauction.model.ProductDetail2;
import com.zhuoyi.fauction.model.ProductDetailPre;
import com.zhuoyi.fauction.model.RecordDetail;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.home.adapter.DetailRecordAdapter;
import com.zhuoyi.fauction.ui.home.fragment.FauctionDetailFragment;
import com.zhuoyi.fauction.ui.home.fragment.FauctionKnowFragment;
import com.zhuoyi.fauction.ui.home.fragment.FauctionStepFragment;
import com.zhuoyi.fauction.ui.home.view.SlideShowView;
import com.zhuoyi.fauction.ui.other.BondPayActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.CustomViewPager;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhuoyi.fauction.view.SelectPricePopWindow;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ProductFauctionDetailPreActivity extends BaseActivity {

    @Bind(R.id.top_ad) SlideShowView topAd;
    @Bind(R.id.pro_title) TextView proTitle;
    @Bind(R.id.cur_price) TextView curPrice;
    @Bind(R.id.remind_num) TextView remindNum;
    @Bind(R.id.qipai) TextView qipai;
    @Bind(R.id.qipai_num) TextView qipaiNum;
    @Bind(R.id.baozj) TextView baozj;
    @Bind(R.id.jiafu) TextView jiafu;
    @Bind(R.id.isbaoliu) TextView isbaoliu;
    @Bind(R.id.tx_txt) TextView tx_txt;
    @Bind(R.id.yanshizhouqi) TextView yanshizhouqi;
    //@Bind(R.id.jiangjiafudu) TextView jiangjiafudu;
    @Bind(R.id.kaipaishijian)
    TextView kaipaishijian;
    @Bind(R.id.jieshushijian) TextView jieshushijian;
    @Bind(R.id.price_record_icon)
    ImageView priceRecordIcon;
    @Bind(R.id.fauction_record) TextView fauctionRecord;
    @Bind(R.id.remind) TextView remind;
    @Bind(R.id.more_paimaijilu) TextView morePaimaijilu;
    @Bind(R.id.shoot_time) TextView shootTime;
    @Bind(R.id.paimairecycleview)
    ListView paimairecycleview;
    @Bind(R.id.id_stickynavlayout_indicator) PagerSlidingTabStrip tabs;
    @Bind(R.id.id_stickynavlayout_viewpager)
    CustomViewPager viewPagers;
    @Bind(R.id.tixing)
    LinearLayout tixing;
    @Bind(R.id.price_state) ImageView priceState;

    @Bind(R.id.chujia)Button chuJia;

    @Bind(R.id.record_title) LinearLayout record_title;

    String[] title = { "拍卖详情", "拍卖须知", "拍卖流程" };
    //当前价格
    String currentPriceStr;

    //剩余数量
    String remainCountStr;
    //剩余时间
    String remianTime;

    SelectPricePopWindow selectPricePopWindow;

    //纪录当前拍卖状态 向上还是向下
    int state;
    //起拍价
    float qipai_price;
    //当前价
    float cur_price;
    //剩余数量
    int remind_num;

    //加价幅度
    float price_fd;
    //起拍数量
    int shoot_num;
    //用户选择的价格
    float sel_price;
    //用户选择的数量
    int sel_num;
    //产品id
    int proId;
    //登陆使用出价状态
    int login_status;
    //保证金
    String mbound;
    //开拍时间
    String shoot_time;
    //结束时间
    String end_time;

    //
    boolean isTx=false;

    String cownStr;

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            Intent intent=new Intent();
            intent.setClass(ProductFauctionDetailPreActivity.this,ProductFauctionDetailDoingActivity.class);
            intent.putExtra("productId", proId);
            startActivity(intent);
            finish();
        }
    };

    private View.OnClickListener itemsOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //加价
                case R.id.price_add:
                    sel_price=Float.parseFloat(selectPricePopWindow.edit_price.getText().toString().trim())+price_fd;
                    selectPricePopWindow.edit_price.setText(sel_price+"");
                    break;
                //减价
                case R.id.price_cut:
                    sel_price=Float.parseFloat(selectPricePopWindow.edit_price.getText().toString().trim())-price_fd;
                    selectPricePopWindow.edit_price.setText(sel_price+"");
                    break;
                //减数量
                case R.id.num_cut:
                    sel_num=Integer.parseInt(selectPricePopWindow.edit_num.getText().toString().trim())-shoot_num;
                    selectPricePopWindow.edit_num.setText(sel_num+"");
                    break;
                case R.id.num_add:
                    sel_num=Integer.parseInt(selectPricePopWindow.edit_num.getText().toString().trim())+shoot_num;
                    selectPricePopWindow.edit_num.setText(sel_num+"");
                    break;
                //确定出价
                case R.id.price_comfirm:
                    selectPricePopWindow.dismiss();
                    if(login_status==0){
                        //跳去登录界面

                        Intent intent=new Intent();
                        intent.putExtra("tab",2);
                        Common.whichActivity=1;
                        intent.setClass(ProductFauctionDetailPreActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        if(state==1){
                            if(sel_price<cur_price){
                                ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"你的出价不能小于当前价");
                                break;
                            }
                            if(sel_num<shoot_num){
                                ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"你填写数量不能小于起拍数量");
                                break;
                            }
                            if(sel_num>remind_num){
                                ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"你填写的数量不能大于剩余量");
                                break;
                            }
                            //都满足做出价操作
                            productOfferPost(proId,sel_price+"",sel_num+"");
                        }else if(state==2){
                            //判断出价和数量是否合法

                            if(sel_num<shoot_num){
                                ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"你填写数量不能小于起拍数量");
                                break;
                            }
                            if(sel_num>remind_num){
                                ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"你填写的数量不能大于剩余量");
                                break;
                            }
                            //都满足做出价操作
                            productOfferPost(proId,sel_price+"",sel_num+"");
                        }

                    }

                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @OnClick(R.id.tixing)
    public void tixingPost(){

        if(login_status==0){
            //跳去登录界面
            //提示登录
            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"您还未登录请先登录");
            Intent intent=new Intent();
            intent.putExtra("tab",2);
            Common.whichActivity=2;
            intent.setClass(ProductFauctionDetailPreActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else {
            if(isTx){
                quxiaotixinPost();
            }else{
                tixinPost();
            }

        }

    }

    @OnClick(R.id.back) void onBackClick() {
        onBackPressed();
    }


    private void tixinPost(){
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "remind");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_REMIND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(proId))
                .addParams("type","1")
                .addParams("shoot_time",shoot_time)
                .addParams("end_time",end_time)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        Gson gson = new Gson();

                        Ret ret = gson.fromJson(response, Ret.class);
                        if (ret.getCode() == 0) {
                            ToastUtil.showShortToast(ProductFauctionDetailPreActivity.this, "设置成功");
                            tx_txt.setText("已提醒");
                            isTx = true;
                        } else if (ret.getCode() == -1) {
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this, "用户未登录");
                        } else if (ret.getCode() == -2) {
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this, "非法操作");
                        } else if (ret.getCode() == -3) {
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this, "已经设置过提现");
                        } else if (ret.getCode() == -4) {
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this, "提醒设置失败");
                        }


                    }
                });
    }

    private void quxiaotixinPost(){
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "delRemind");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_DELREMIND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(proId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        Gson gson = new Gson();

                        Ret ret = gson.fromJson(response, Ret.class);
                        if(ret.getCode()==0){
                            ToastUtil.showShortToast(ProductFauctionDetailPreActivity.this,"取消提醒成功");
                            tx_txt.setText("提醒");
                            isTx=false;
                        }else if(ret.getCode()==-1){
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"用户未登录");
                        }else if(ret.getCode()==-2){
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this,"取消");
                        }


                    }
                });
    }

    @OnClick(R.id.more_paimaijilu)
    public void toAllPriceRecord(){
        Intent intent=new Intent();
        intent.setClass(ProductFauctionDetailPreActivity.this,PriceRecordActivity.class);

        startActivity(intent);
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_fauction_detail_pre);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        proId = intent.getIntExtra("productId", 0);
        Common.proId=proId;
        detailFirstPagePost(proId);
       // detailSecondPagePost(proId);
        //auctionDetailRecordPost(proId);








    }



//    @OnClick(R.id.chujia)
//    public void giveMoney(){
//        if(login_status==-1){
//            //保证金界面
//            Intent intent=new Intent();
//            intent.putExtra("bond",mbound);
//            intent.putExtra("productId", proId);
//            intent.setClass(ProductFauctionDetailPreActivity.this, BondPayActivity.class);
//            startActivity(intent);
//            return;
//        }else if(login_status==2){
//            //得拍去提交订单界面
//
//
//
//
//        }else if(login_status==0||login_status==1){
//            selectPricePopWindow=new SelectPricePopWindow(ProductFauctionDetailPreActivity.this,itemsOnClick,cur_price,remind_num,shoot_num,state);
//            //显示的位置
//            selectPricePopWindow.showAtLocation(ProductFauctionDetailPreActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//            backgroundAlpha(0.5f);
//            selectPricePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    backgroundAlpha(1f);
//                }
//            });
//        }
//
//    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void detailSecondPagePost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "viewContent");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_VIEWCONTENT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(productId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson = new Gson();
                            //存入临时变量
                            ProductDetail2 productDetail2 = gson.fromJson(response, ProductDetail2.class);
                            if (productDetail2 != null) {
                                Common.productDetail2 = productDetail2;
                                viewPagers.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                                tabs = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
                                tabs.setViewPager(viewPagers);
                                tabs.setDividerColor(0xFFFFFF);
                                viewPagers.setCurrentItem(0);
                            }

                            DialogUtil.dismiss();
                        }

                    }
                });
    }

    private void detailFirstPagePost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "viewSoon");
        DialogUtil.showDialog(ProductFauctionDetailPreActivity.this, "加载中", false);
        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_VIEWSOON)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(productId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG+"first", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson=new Gson();
                            ProductDetailPre productDetailPre=gson.fromJson(response, ProductDetailPre.class);
                            ProductDetailPre.DataBean data = productDetailPre.getData();
                            proTitle.setText(data.getTitle());
                            curPrice.setText(data.getShoot_price() + "/" + data.getUnit());
                            //cur_price = Float.parseFloat(data.getCurrent_price());
                            state = data.getState();

//                        if(state==1){
//                            priceState.setBackgroundResource(R.drawable.price_up);
//                        }else if(state==2){
//                            priceState.setBackgroundResource(R.drawable.price_down);
//                        }
                            shoot_time=data.getShoot_time();
                            shootTime.setText(shoot_time+"开始");
                            end_time=data.getEnd_time();
                            remindNum.setText(data.getNum()+data.getUnit());
                            //remind_num=Integer.parseInt(data.getStock());
                            qipai.setText("￥"+data.getShoot_price()+"/"+data.getUnit());
                            qipai_price=Float.parseFloat(data.getShoot_price());
                            qipaiNum.setText(data.getShoot_num()+data.getUnit());
                            shoot_num=Integer.parseInt(data.getShoot_num());
                            baozj.setText("￥"+data.getBond());
                            mbound=data.getBond();
                            jiafu.setText("￥"+data.getFare()+"/次");
                            String reserve_price = data.getReserve_price();
                            float aFloat = Float.parseFloat(reserve_price);
                            if(aFloat>0){
                                isbaoliu.setText("有");
                            }else{
                                isbaoliu.setText("无");
                            }
                            //
                            int remind = productDetailPre.getRemind();
                            if(remind==0){
                                tx_txt.setText("已提醒");
                                isTx = true;
                            }else{
                                tx_txt.setText("提醒");
                                isTx = false;
                            }
                            yanshizhouqi.setText(data.getDelayed()+"分钟/次");
                            //cownStr=data.getDelayed();
                            //jiangjiafudu.setText("￥"+data.getCut_price()+"/"+data.getPrice_cycle()+"分钟");
                            price_fd=Float.parseFloat(data.getCut_price());
                            kaipaishijian.setText(data.getShoot_time());
                            //定时时间到跳转正在进行
                            handler.postDelayed(runnable,getDistanceMills(data.getShoot_time(),getSystemDate()));
                            jieshushijian.setText(data.getEnd_time());
                            login_status=data.getLogin_status();
//                        //未交保证金
//                        if(login_status==-1){
//                            chuJia.setText("报名参拍\n(缴纳保证金：￥"+data.getBond()+")");
//                        }else if(login_status==2){
//                            //已经得拍
//                            chuJia.setText("您已得拍\n点击去付款");
//                        }
                            List<ProductDetailPre.DataBean.PictureBean> picture = data.getPicture();
                            ArrayList<String> mImgBannerUrl=new ArrayList<String>();
                            for(ProductDetailPre.DataBean.PictureBean pictureBean:picture){
                                mImgBannerUrl.add(pictureBean.getPic_name());
                            }
                            topAd.setView(mImgBannerUrl.toArray(new String[mImgBannerUrl.size()]));
                            viewPagers.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                            tabs = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
                            tabs.setViewPager(viewPagers);
                            tabs.setDividerColor(0xFFFFFF);
                            viewPagers.setCurrentItem(0);
                            tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    viewPagers.resetHeight(position);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });
                            viewPagers.resetHeight(0);
                            DialogUtil.dismiss();
                        }else{
                            DialogUtil.dismiss();
                        }




                    }
                });
    }

    private void auctionDetailRecordPost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "auctionRecord");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_AUCTIONRECORD)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(productId))
                .addParams("type",String.valueOf(state))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG+"--recordauction=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson=new Gson();
                            RecordDetail recordDetail = gson.fromJson(response, RecordDetail.class);
                            if(recordDetail!=null){
                                List<RecordDetail.DataBean> data = recordDetail.getData();
                                if(data!=null){
                                    Logger.i(TAG + "--recordauction=", "" + data.size());
                                    if(data.size()!=0){
                                        paimairecycleview.setVisibility(View.VISIBLE);
                                        record_title.setVisibility(View.VISIBLE);
                                        paimairecycleview.setAdapter(new DetailRecordAdapter(ProductFauctionDetailPreActivity.this, data));
                                    }else{
                                        paimairecycleview.setVisibility(View.GONE);
                                        record_title.setVisibility(View.GONE);
                                    }

                                    remind.setText(recordDetail.getEnlist_num() + "人报名 出价" + recordDetail.getOffer_num() + "次");
                                }

                            }
                        }


                    }
                });
    }

    //出价接口
    private void productOfferPost(final int productId,String price,String num) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailPreActivity.this, timestamp, "Product", "offer");

        String key= MD5Util.getMD5String(num + 10 + 2);

        Logger.i(TAG+"price====",price);
       // Logger.i(TAG,"sign="+sign+"-"+"timestamp"+timestamp+"-"+"client_id"+ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this)+"user_id"+ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this)+"id"+String.valueOf(productId)+"-"+);

        OkHttpUtils.post().url(Constant.PRODUCT_OFFER)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailPreActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailPreActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailPreActivity.this))
                .addParams("id",String.valueOf(productId))
                .addParams("type",2+"")
                .addParams("price", 10+"")
                .addParams("num", num)
                .addParams("key", key)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "chujia", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson = new Gson();
//                        Ret ret=gson.fromJson(response,Ret.class);
//                        if(ret.getCode()==0){
                            ToastUtil.showLongToast(ProductFauctionDetailPreActivity.this, "出价成功");
                            //刷新UI
                            //临时跳转到提交订单界面
                            Intent intent = new Intent(ProductFauctionDetailPreActivity.this, OrderSubmitActivity.class);
                            intent.putExtra("productId", productId);
                            startActivity(intent);
                            //}
                        }



                    }
                });


    }





    class MyPagerAdapter extends FragmentPagerAdapter {

        FauctionDetailFragment fauctionDetailFragment;
        FauctionKnowFragment fauctionKnowFragment;
        FauctionStepFragment fauctionStepFragment;




        public MyPagerAdapter(FragmentManager fm) {

            super(fm);


        }





        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    fauctionDetailFragment = new FauctionDetailFragment(viewPagers);
                   // mainCategory.setTitle(title[0]);
                    return fauctionDetailFragment;
                case 1:
                    fauctionKnowFragment = new FauctionKnowFragment(viewPagers);
                   // mainCategory.setTitle(title[1]);
                    return fauctionKnowFragment;
                case 2:
                    fauctionStepFragment = new FauctionStepFragment(viewPagers);
                    //mainCategory.setTitle(title[2]);
                    return fauctionStepFragment;



                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return title.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title[position];
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






    private String getSystemDate(){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());
        String str=format.format(curDate);
        return str;
    }

    private long getDistanceMills(String shootTime,String curTime){
        long diff=0;
        DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1=df.parse(shootTime);
            Date d2=df.parse(curTime);
            diff=d1.getTime()-d2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff;
    }
}
