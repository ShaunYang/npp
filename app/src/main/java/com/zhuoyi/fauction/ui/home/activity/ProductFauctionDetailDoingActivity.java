package com.zhuoyi.fauction.ui.home.activity;
import android.app.Activity;
import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.unionpay.UPPayAssistEx;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.DialogUtil;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.ProductDetail1;
import com.zhuoyi.fauction.model.ProductDetail2;
import com.zhuoyi.fauction.model.RecordDetail;
import com.zhuoyi.fauction.model.Ret;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.socket.PushPo;
import com.zhuoyi.fauction.socket.UrlBean;
import com.zhuoyi.fauction.socket.WebSocketJavaScript;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.activity.CategoryDoingTabActivity;
import com.zhuoyi.fauction.ui.home.adapter.DetailRecordAdapter;
import com.zhuoyi.fauction.ui.home.fragment.FauctionDetailFragment;
import com.zhuoyi.fauction.ui.home.fragment.FauctionKnowFragment;
import com.zhuoyi.fauction.ui.home.fragment.FauctionStepFragment;
import com.zhuoyi.fauction.ui.home.view.SlideShowView;

import com.zhuoyi.fauction.ui.myfauction.activity.LikeFauctionProActivity;
import com.zhuoyi.fauction.ui.other.BondPayActivity;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.view.CustomViewPager;
import com.zhuoyi.fauction.view.PagerSlidingTabStrip;
import com.zhuoyi.fauction.view.SelectPricePopWindow;
import com.zhuoyi.fauction.view.SimpleViewPagerIndicator;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class ProductFauctionDetailDoingActivity extends BaseActivity {

    private final int VIEWPAGER_DEFAULT_HEIGHT = 200;


    @Bind(R.id.top_ad) SlideShowView topAd;
    @Bind(R.id.pro_title) TextView proTitle;
    @Bind(R.id.cur_price) TextView curPrice;
    @Bind(R.id.remind_num) TextView remindNum;
    @Bind(R.id.qipai) TextView qipai;
    @Bind(R.id.qipai_num) TextView qipaiNum;
    @Bind(R.id.baozj) TextView baozj;
    @Bind(R.id.jiafu) TextView jiafu;
    @Bind(R.id.isbaoliu) TextView isbaoliu;
    @Bind(R.id.yanshizhouqi) TextView yanshizhouqi;
    //@Bind(R.id.jiangjiafudu) TextView jiangjiafudu;
    @Bind(R.id.kaipaishijian)
    TextView kaipaishijian;
    //@Bind(R.id.jieshushijian) TextView jieshushijian;
    @Bind(R.id.price_record_icon)
    ImageView priceRecordIcon;
    @Bind(R.id.fauction_record) TextView fauctionRecord;
    @Bind(R.id.remind) TextView remind;
    @Bind(R.id.more_paimaijilu) TextView morePaimaijilu;
    @Bind(R.id.paimairecycleview)
    ListView paimairecycleview;
    @Bind(R.id.id_stickynavlayout_indicator)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.id_stickynavlayout_viewpager)
    CustomViewPager viewPagers;
    @Bind(R.id.tixing)
    LinearLayout tixing;
    @Bind(R.id.price_state) ImageView priceState;

    @Bind(R.id.chujia)Button chuJia;
    @Bind(R.id.tx_txt) TextView tx_txt;

    @Bind(R.id.websocket)WebView webView;

    @Bind(R.id.shoot_time_min) TextView shootTimeMin;
    @Bind(R.id.shoot_time_sec) TextView shootTimeSec;
    @Bind(R.id.shoot_time_hour) TextView shootTimeHour;

    @Bind(R.id.delay_time) TextView delay_time;

    @Bind(R.id.jieshushijian) TextView jieshushijian;

    @Bind(R.id.record_title) LinearLayout record_title;

    @Bind(R.id.current_remind_num) TextView current_remind_num;

    //String[] title = { "拍卖详情", "拍卖须知", "拍卖流程" };
    private String[] mTitles = new String[] { "拍卖详情", "拍卖须知", "拍卖流程" };
    //当前价格
    String currentPriceStr;

    //剩余数量
    String remainCountStr;
    //剩余时间
    String remianTime;

    boolean isTx=false;

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
    double price_fd;
    //起拍数量
    int shoot_num;
    //用户选择的价格
    double sel_price;
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

    //实时推送过来的数据包
    RecordDetail pushPo;
    //可拍数量
    int kepai_num;

    int pd_num;
    int pd_stock;

    //

    //
    public long mDay = 0;
    public long mHour = 0;
    public long mMin =0;
    public long mSecond = 00;// 天 ,小时,分钟,秒
    private boolean isRun = true;

    public int shootType;

    //已得拍付款状态
    public int payState;

    //支付方式
    public int payType;

    //是否开启推送
    public boolean isPush=true;

    public String bond;
    //单位
    String unit;

    //倒计时时间
    String cownStr;

    //判断是否出价小于当前价
    
    //
    // 使用一个handler来处理加载事件
     Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    String result = (String) msg.obj;
                    Toast.makeText(ProductFauctionDetailDoingActivity.this, result, Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    shootStatusPost(proId);
                   // jieshushijian.setText(pushPo.getEnd_time());
                    jieshushijian.setText(pushPo.getEnd_time());
                    if(pushPo.getPd().getNum()!=null){
                        current_remind_num.setText(pushPo.getPd().getNum()+unit);

                    }
                    isRun=false;
                    boolean isEnd=getTimeNum(pushPo.getCount_down());
                    if(isEnd){
                        //isRun=true;
                        shootTimeMin.setText(mMin+"");
                        shootTimeSec.setText(mSecond+"");
                        shootTimeHour.setText(mHour+"");
                        try {
                            startRun();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
                    }else{
                        shootTimeMin.setText(cownStr);
                        shootTimeSec.setText("00");
                        shootTimeHour.setText("00");
                    }
                    if(mDay==-1){
                        shootTimeMin.setText(cownStr);
                        shootTimeSec.setText("00");
                        shootTimeHour.setText("00");
                    }
                    delay_time.setText("(第" + pushPo.getDelay_num() + "轮)");

                    /*if (mDay==-1){
                        shootTime.setText(pushPo.getEnd_time());
                        delay_time.setText("结束");
                    }else{
                        shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
                        delay_time.setText("(第" + pushPo.getDelay_num() + "轮)");
                    }*/
//                    shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
//                    delay_time.setText("(延迟" + pushPo.getDelay_num() + "次)");
                    curPrice.setText("￥"+pushPo.getPd().getCurrent_price()+ "/"+unit);
                    cur_price=Float.parseFloat(pushPo.getPd().getCurrent_price());
                    sel_price=cur_price;
                    if(pushPo.getPd().getShoot_type()==1){
                        priceState.setBackgroundResource(R.drawable.price_up);
                    }else if(pushPo.getPd().getShoot_type()==2){
                        priceState.setBackgroundResource(R.drawable.price_down);
                    }
                    shootType=pushPo.getPd().getShoot_type();
                    //.setText(pushPo.getPd().getStock() + "/"+unit);
                    remindNum.setText(pushPo.getTotal()+unit);
                    remind_num=Integer.parseInt(pushPo.getPd().getStock());
                   // String num = pushPo.getPd().getNum();
                    if(pushPo.getPd().getNum()!=null){
                        pd_num=Integer.parseInt(pushPo.getPd().getNum());
                    }

                    pd_stock=Integer.parseInt(pushPo.getPd().getStock());
                    kepai_num=pd_num;
                    if(pushPo.getData()!=null){
                        if(pushPo.getData().size()!=0){
                            paimairecycleview.setVisibility(View.VISIBLE);
                            record_title.setVisibility(View.VISIBLE);
                            paimairecycleview.setAdapter(new DetailRecordAdapter(ProductFauctionDetailDoingActivity.this, pushPo.getData()));
                        }else{
                            paimairecycleview.setVisibility(View.GONE);
                            record_title.setVisibility(View.GONE);
                        }

                    }


                    remind.setText(pushPo.getEnlist_num() + "人报名 出价" + pushPo.getOffer_num() + "次");
                    int shoot_state = pushPo.getShoot_state();
                    if(shoot_state==1){
                        //跳转拍卖结束界面
                        Intent intent=new Intent();
                        intent.setClass(ProductFauctionDetailDoingActivity.this,ProductFauctionDetailEndActivity.class);
                        intent.putExtra("productId", proId);
                        startActivity(intent);
                        isRun=false;
                        webView=null;
                        isPush=false;
                        finish();
                    }
                    //isRun=true;
                   // startRun();
                    break;
                default:
                    break;
            }
        }
    };

    UrlBean urlBean;
    Context mContext;

    private View.OnClickListener itemsOnClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sel_num=Integer.parseInt(selectPricePopWindow.edit_num.getText().toString().trim());
            switch (v.getId()){
                //加价
                case R.id.price_add:
                    sel_price=Double.parseDouble(selectPricePopWindow.edit_price.getText().toString().trim())+price_fd;
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String p=decimalFormat.format(sel_price);
                    selectPricePopWindow.edit_price.setText(p+"");
                    selectPricePopWindow.popNum.setText(pd_stock+unit);
                    selectPricePopWindow.edit_num.setText(pd_stock+"");
                    break;
                //减价
                case R.id.price_cut:
                    sel_price=Double.parseDouble(selectPricePopWindow.edit_price.getText().toString().trim())-price_fd;
                     DecimalFormat decimalFormat2=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
                    String p2=decimalFormat2.format(sel_price);
                    selectPricePopWindow.edit_price.setText(p2+"");
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
                        //提示登录
                        ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"您还未登录请先登录");
                        Intent intent=new Intent();
                        intent.putExtra("tab",3);
                        Common.whichActivity=1;
                        intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        //都满足做出价操作
                        Double chu_price=Double.parseDouble(selectPricePopWindow.edit_price.getText().toString().trim());
                        DecimalFormat decimalFormat3=new DecimalFormat("0.00");//构造方法的字符格式http://fir.im/npp1这里如果小数不足2位,会以0补足.
                        String p3=decimalFormat3.format(chu_price);
                        productOfferPost(proId,p3+"",sel_num+"");

                  /*      if(shootType==1){
//                            if(sel_price<cur_price){
//                                //ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"你的出价不能小于当前价");
//                                //break;
//                            }
                            if(sel_num<shoot_num){
                                ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"你填写数量不能小于起拍数量");
                                break;
                            }
                            if(sel_num>remind_num){
                                ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"你填写的数量不能大于剩余量");
                                break;
                            }


                            //都满足做出价操作
                            Double chu_price=Double.parseDouble(selectPricePopWindow.edit_price.getText().toString().trim());
                            DecimalFormat decimalFormat3=new DecimalFormat("0.00");//构造方法的字符格式http://fir.im/npp1这里如果小数不足2位,会以0补足.
                            String p3=decimalFormat3.format(chu_price);
                            productOfferPost(proId,p3+"",sel_num+"");
                        }else if(shootType==2){
                            //判断出价和数量是否合法

                            if(sel_num<shoot_num){
                                ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"你填写数量不能小于起拍数量");
                                break;
                            }
                            if(sel_num>remind_num){
                                ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"你填写的数量不能大于剩余量");
                                break;
                            }
                            //都满足做出价操作
                            productOfferPost(proId,cur_price+"",sel_num+"");
                        }*/

                    }

                    break;

            }
        }
    };

    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch(Exception e) {
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setConfigCallback((WindowManager)getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

    }



    @OnClick(R.id.tixing)
    public void tixingPost(){

        if(login_status==0){
            //跳去登录界面
            //提示登录
            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"您还未登录请先登录");
            Intent intent=new Intent();
            intent.putExtra("tab",3);
            Common.whichActivity=1;
            intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else {
            if(isTx){
                quxiaotixinPost();
            }else{
                tixinPost();
            }
        }
       // UPPayAssistEx.startPay(ProductFauctionDetailDoingActivity.this, null, null, "201609011508258345618", "01");



    }

    private final String mMode = "00";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*************************************************
         * 步骤3：处理银联手机支付控件返回的支付结果
         ************************************************/
        if (data == null) {
            return;
        }

        String msg = "";
        /*
         * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
         */
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            // 支付成功后，extra中如果存在result_data，取出校验
            // result_data结构见c）result_data参数说明
            if (data.hasExtra("result_data")) {
                String result = data.getExtras().getString("result_data");
                try {
                    JSONObject resultJson = JSONObject.parseObject(result);
                    String sign = resultJson.getString("sign");
                    String dataOrg = resultJson.getString("data");
                    // 验签证书同后台验签证书
                    // 此处的verify，商户需送去商户后台做验签
                    boolean ret = verify(dataOrg, sign, mMode);
                    if (ret) {
                        // 验证通过后，显示支付结果
                        msg = "支付成功！";
                    } else {
                        // 验证不通过后的处理
                        // 建议通过商户后台查询支付结果
                        msg = "支付失败！";
                    }
                } catch (Exception e) {
                }
            } else {
                // 未收到签名信息
                // 建议通过商户后台查询支付结果
                msg = "支付成功！";
            }
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败！";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "用户取消了支付";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("支付结果通知");
        builder.setMessage(msg);
        builder.setInverseBackgroundForced(true);
        // builder.setCustomTitle();
        builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private boolean verify(String msg, String sign64, String mode) {
        // 此处的verify，商户需送去商户后台做验签
        return true;

    }

    @Override
    public void onBackPressed() {
        if(Common.whichActivity==1){
            Intent intent=new Intent();
            intent.putExtra("tab",2);
            Common.whichActivity=1;
            intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            super.onBackPressed();
        }


    }

    @OnClick(R.id.back) void onBackClick() {
        //onBackPressed();
        if(Common.whichActivity==1){
                Intent intent=new Intent();
                intent.putExtra("tab",2);
                Common.whichActivity=1;
                intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else{
                onBackPressed();
        }
       /* Intent intent=new Intent();
        intent.putExtra("tab",0);
        //Common.whichActivity=1;
        intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
        startActivity(intent);
        finish();*/
        //onBackPressed();

//        Intent intent = new Intent();
//        //intent.putExtra("tab",categories.get(position).getId());
//        Common.tab="-1";
//        intent.setClass(ProductFauctionDetailDoingActivity.this, CategoryDoingTabActivity.class);
//        startActivity(intent);
//        finish();

    }



    private void tixinPost(){
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "remind");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_REMIND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id", ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
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
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "设置成功");
                            tx_txt.setText("已提醒");
                            isTx = true;
                        } else if (ret.getCode() == -1) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "用户未登录");
                        } else if (ret.getCode() == -2) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "非法操作");
                        } else if (ret.getCode() == -3) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "已经设置过提现");
                        } else if (ret.getCode() == -4) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "提醒设置失败");
                        }


                    }
                });
    }

    private void quxiaotixinPost(){
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "delRemind");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_DELREMIND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
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
                        if (ret.getCode() == 0) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "取消提醒成功");
                            tx_txt.setText("提醒");
                            isTx = false;
                        } else if (ret.getCode() == -1) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "用户未登录");
                        } else if (ret.getCode() == -2) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "取消");
                        }


                    }
                });
    }

    @OnClick(R.id.more_paimaijilu)
    public void toAllPriceRecord(){
        Intent intent=new Intent();
        intent.putExtra("shootType",shootType);
        intent.setClass(ProductFauctionDetailDoingActivity.this, PriceRecordActivity.class);

        startActivity(intent);
    }

    @Override
    protected void initComponent(Bundle bundle) {


        setContentView(R.layout.activity_fauction_detail);
        ButterKnife.bind(this);
        BusProvider.getInstance().register(this);
        Intent intent = getIntent();
        proId = intent.getIntExtra("productId", 0);
        Common.proId=proId;
        detailFirstPagePost(proId);
//        detailSecondPagePost(proId);
//        auctionDetailRecordPost(proId);


        // 加载页面
        isPush=true;
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        // webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        // 允许JavaScript执行
        webView.getSettings().setJavaScriptEnabled(true);
        // webView.addJavascriptInterface(new Contact(), "contact");
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        UrlBean urlBean=new UrlBean();
        Logger.i(TAG,"proId======="+Common.proId);
        urlBean.setUid(Common.proId+"");
        urlBean.setUrl(Constant.FRONT_PUSH);

        webView.addJavascriptInterface(new SocketJavaScript(ProductFauctionDetailDoingActivity.this,handler,urlBean), "myjavascript");
        //webView.addJavascriptInterface();

        // 加载index.html
        webView.loadUrl("file:///android_asset/websocket.html");





    }

    private class SocketJavaScript {

        private WebView webview;
        // 使用一个handler来处理加载事件
        private Handler handler;

        UrlBean urlBean;
        Context mContext;


        public SocketJavaScript(Context context, Handler handler,UrlBean urlBean) {
            this.handler = handler;
            webview = (WebView) ((Activity) context).findViewById(R.id.websocket);
            this.urlBean=urlBean;
            mContext=context;

        }

        @JavascriptInterface
        public void toastMessage(String message) {

            //isRun=false;
            //取到数据做解析展示
            if(isPush){
                Log.i("message",message);
                Log.i("decode_message", URLDecoder.decode(message));
                //Toast.makeText(mContext, URLDecoder.decode(message), Toast.LENGTH_LONG).show();
                Gson gson=new Gson();
                pushPo=gson.fromJson(URLDecoder.decode(message), RecordDetail.class);

                //

//            paimairecycleview.setAdapter(new DetailRecordAdapter(ProductFauctionDetailDoingActivity.this, data));
//            remind.setText(data.getEnlist_num() + "人报名 出价" + recordDetail.getOffer_num() + "次");

                Message msg = Message.obtain();
                msg.what = 1;
                handler.sendMessage(msg);
            }






        }



        /*
         * java调用显示网页，异步
         */
        @JavascriptInterface
        public void show() {
            handler.post(new Runnable() {
                public void run() {
                    // 重要：url的生成,传递数据给网页
                    if (urlBean !=null){
                        //String json = "{\"url\":\"" + urlBean.getUrl() + "\",\"uid\":\"" + "t"+urlBean.getUid() + "\"}";
                       String json = "{\"url\":\"" + urlBean.getUrl() + "\",\"uid\":\"" + urlBean.getUid() + "\"}";
                        System.out.println("json=" + json);
                        String url = "javascript:insertData('" + json + "')";
                        webView.loadUrl(url);
                    }

                }
            });
        }

    }




    private void productPayBondPost(final int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getApplicationContext(),timestamp,"Product","payBond");


        OkHttpUtils.post()
                .url(Constant.PRODUCT_PAYBOND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getApplicationContext()))
                .addParams("user_id",ConfigUserManager.getUserId(getApplicationContext()))
                .addParams("id",String.valueOf(productId))
                .addParams("bond",mbound)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG, response);
                        Gson gson=new Gson();
                        Ret ret=gson.fromJson(response,Ret.class);
                        if(ret.getCode()==0){
                            //finish();
                            login_status=1;
                            chuJia.setText("出价");
                            JSONObject jsonObject=JSONObject.parseObject(response);
                            Logger.i("oid订单号=", jsonObject.getString("oid"));
//                            zfbPayPost(jsonObject.getString("oid"));
                            Intent intent=new Intent();
                            intent.setClass(ProductFauctionDetailDoingActivity.this,BondPayActivity.class);
                            intent.putExtra("oid", jsonObject.getString("oid"));
                            intent.putExtra("bond",mbound);
                            intent.putExtra("productId",String.valueOf(productId));
                            startActivity(intent);
                            finish();
                            //
                        }else if(ret.getCode()==-1){
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"用户未登录");
                        }else if(ret.getCode()==-2){
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"钱包余额不足，客户端调到支付页面");

                        }else if(ret.getCode()==-3){
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"系统超时（保证金更新失败）");
                        }else if(ret.getCode()==-4){
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"已拍得");
                        }else if(ret.getCode()==-5){
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"已付款");
                        }

                    }
                });
    }








    @OnClick(R.id.chujia)
    public void giveMoney(){
        if(login_status==-1){
            //保证金界面
            productPayBondPost(proId);
            return;
        }else if(login_status==2){
            //未付款 得拍去提交订单界面
            if(payState==0){
                if(payType==5){
                    Intent intent=new Intent(ProductFauctionDetailDoingActivity.this, LikeFauctionProActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = new Intent(ProductFauctionDetailDoingActivity.this, OrderSubmitActivity.class);
                    intent.putExtra("productId", proId);
                    startActivity(intent);
                    finish();
                }

            }else{
                //已付款
                ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"您已经付款等待拍卖结束");

            }





        }else if(login_status==1){
            //Log.i("kepainum=",kepai_num);
            selectPricePopWindow=new SelectPricePopWindow(ProductFauctionDetailDoingActivity.this,itemsOnClick,cur_price,kepai_num,state,unit);
            selectPricePopWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            //显示的位置
            selectPricePopWindow.showAtLocation(ProductFauctionDetailDoingActivity.this.findViewById(R.id.main_do), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            backgroundAlpha(0.5f);
            selectPricePopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    backgroundAlpha(1f);
                }
            });
        }else if(login_status==0){

                //跳去登录界面
                //提示登录
            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this,"您还未登录请先登录");
            Intent intent=new Intent();
            intent.putExtra("tab",3);
            Common.whichActivity=1;
            intent.setClass(ProductFauctionDetailDoingActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }

    }

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

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "viewContent");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_VIEWCONTENT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
                .addParams("id",String.valueOf(productId))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG+"22222222222222222222222=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if(code==0){
                            Gson gson = new Gson();
                            //存入临时变量
                            ProductDetail2 productDetail2 = gson.fromJson(response, ProductDetail2.class);
                            if (productDetail2 != null) {
                                Common.productDetail2 = productDetail2;
                                //Common.viewPagers=(CustomViewPager)findViewById(R.id.id_stickynavlayout_viewpager);
                                viewPagers.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
                                tabs = (PagerSlidingTabStrip) findViewById(R.id.id_stickynavlayout_indicator);
                                tabs.setViewPager(viewPagers);
                                tabs.setDividerColor(0xFFFFFF);
                                viewPagers.setCurrentItem(0);
                            }
                            auctionDetailRecordPost(proId);
                        }


                    }
                });
    }

    private void detailFirstPagePost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "viewing");
        DialogUtil.showDialog(ProductFauctionDetailDoingActivity.this, "加载中", false);
        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_VIEWING)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
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
                            Gson gson=new Gson();
                            ProductDetail1 productDetail1=gson.fromJson(response, ProductDetail1.class);
                            ProductDetail1.DataBean data = productDetail1.getData();
                            proTitle.setText(data.getTitle());
                            curPrice.setText("￥"+data.getCurrent_price() + "/" + data.getUnit());
                            unit=data.getUnit();
                            cur_price = Float.parseFloat(data.getCurrent_price());
                            sel_price=cur_price;
                            state = data.getState();
                            Common.TYPEID=data.getType();
                            shootType=state;
                            payState=data.getPay_state();
                            if(state==1){
                                priceState.setBackgroundResource(R.drawable.price_up);
                            }else if(state==2){
                                priceState.setBackgroundResource(R.drawable.price_down);
                            }
                            shoot_time=data.getShoot_time();
                            end_time=data.getEnd_time();
                            //remindNum.setText(data.getStock()+data.getUnit());
                            remind_num=Integer.parseInt(data.getStock());
                            qipai.setText("￥"+data.getShoot_price()+"/"+data.getUnit());
                            qipai_price=Float.parseFloat(data.getShoot_price());
                            qipaiNum.setText(data.getShoot_num()+data.getUnit());
                            shoot_num=Integer.parseInt(data.getShoot_num());
                            baozj.setText("￥"+data.getBond());
                            mbound=data.getBond();
                            jiafu.setText("￥"+data.getFare()+"/次");
                            price_fd=Double.parseDouble(data.getFare());
                            String reserve_price = data.getReserve_price();
                            float aFloat = Float.parseFloat(reserve_price);
                            if(aFloat>0){
                                isbaoliu.setText("有");
                            }else{
                                isbaoliu.setText("无");
                            }
                            //
                            int remind = productDetail1.getRemind();
                            if(remind==0){
                                tx_txt.setText("已提醒");
                                isTx = true;
                            }else{
                                tx_txt.setText("提醒");
                                isTx = false;
                            }
                            yanshizhouqi.setText(data.getDelayed()+"分钟/次");
                            cownStr=data.getDelayed();
                            //jiangjiafudu.setText("￥"+data.getCut_price()+"/"+data.getPrice_cycle()+"分钟");

                            Logger.i(TAG + "price_fd=", price_fd + "");
                            kaipaishijian.setText(data.getShoot_time());
                            //jieshushijian.setText(data.getEnd_time());
                            login_status=data.getLogin_status();
                            bond=data.getBond();
                            //未交保证金
                            shootStatusPost(proId);
                          /*  if(login_status==-1){
                                chuJia.setText("报名参拍\n(缴纳保证金：￥"+data.getBond()+")");
                            }else if(login_status==2){
                                //已经得拍
                                if(payState==0){
                                    chuJia.setText("您已成交\n点击去付款");
                                }else{
                                    chuJia.setText("您已成交\n已付款");
                                }

                            }*/
                            List<ProductDetail1.DataBean.PictureBean> picture = data.getPicture();
                            ArrayList<String> mImgBannerUrl=new ArrayList<String>();
                            for(ProductDetail1.DataBean.PictureBean pictureBean:picture){
                                mImgBannerUrl.add(pictureBean.getPic_name());
                            }
                            topAd.setView(mImgBannerUrl.toArray(new String[mImgBannerUrl.size()]));

                            //detailSecondPagePost(proId);
                            viewPagers=(CustomViewPager)findViewById(R.id.id_stickynavlayout_viewpager);
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

                         /*   viewPagers.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    viewPagers.resetHeight(position);


                                    *//*if (position == 0) {

                                        activityScdetailsBottomLinear.setBackgroundResource(R.drawable.fishbone_diagram_list_btn_1);
                                        activityScdetailsBottomTaskTv.setTextColor(Color.parseColor("#ffffff"));
                                        activityScdetailsBottomInfoTv.setTextColor(Color.parseColor("#c1c1c1"));
                                        activityScdetailsBottomTimeTv.setTextColor(Color.parseColor("#c1c1c1"));

                                    } else if (position == 1) {

                                        activityScdetailsBottomLinear.setBackgroundResource(R.drawable.fishbone_diagram_list_btn_2);
                                        activityScdetailsBottomTaskTv.setTextColor(Color.parseColor("#c1c1c1"));
                                        activityScdetailsBottomInfoTv.setTextColor(Color.parseColor("#ffffff"));
                                        activityScdetailsBottomTimeTv.setTextColor(Color.parseColor("#c1c1c1"));
                                    } else {

                                        activityScdetailsBottomLinear.setBackgroundResource(R.drawable.fishbone_diagram_list_btn_3);
                                        activityScdetailsBottomTaskTv.setTextColor(Color.parseColor("#c1c1c1"));
                                        activityScdetailsBottomInfoTv.setTextColor(Color.parseColor("#c1c1c1"));
                                        activityScdetailsBottomTimeTv.setTextColor(Color.parseColor("#ffffff"));
                                    }*//*

                                }
                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });*/

                            auctionDetailRecordPost(proId);
                        }



                    }
                });
    }

    private void auctionDetailRecordPost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "auctionRecord");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_AUCTIONRECORD)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
                .addParams("id",String.valueOf(productId))
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
                                remindNum.setText(recordDetail.getTotal()+unit);
                                current_remind_num.setText(recordDetail.getPd().getNum()+unit);
                                List<RecordDetail.DataBean> data = recordDetail.getData();
                                if(data!=null){
                                    Logger.i(TAG + "--recordauction=", "" + data.size());

                                    if(data.size()==0){
                                        paimairecycleview.setVisibility(View.GONE);
                                        record_title.setVisibility(View.GONE);
                                        remind.setText(recordDetail.getEnlist_num() + "人报名 出价" + recordDetail.getOffer_num() + "次");
                                        jieshushijian.setText(recordDetail.getEnd_time());//结束时间
                                        boolean isEnd=getTimeNum(recordDetail.getCount_down());
                                        Logger.i("recordDetail.getEnd_time()=",recordDetail.getEnd_time());
                                        Logger.i("recordDetail.getCount_down()=",recordDetail.getCount_down());
                                        if(isEnd){
                                            shootTimeMin.setText(mMin+"");
                                            shootTimeSec.setText(mSecond+"");
                                            shootTimeHour.setText(mHour+"");
                                            try {
                                                startRun();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            //shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
                                        }else{
                                           shootTimeMin.setText(cownStr);
                                            shootTimeSec.setText("00");
                                            shootTimeHour.setText("00");
                                        }
                                        pd_num=Integer.parseInt(recordDetail.getPd().getNum());
                                        pd_stock=Integer.parseInt(recordDetail.getPd().getStock());
                                        kepai_num=pd_num;
                                        delay_time.setText("(第" + recordDetail.getDelay_num() + "轮)");

                                    }else{
                                        paimairecycleview.setVisibility(View.VISIBLE);
                                        record_title.setVisibility(View.VISIBLE);
                                        paimairecycleview.setAdapter(new DetailRecordAdapter(ProductFauctionDetailDoingActivity.this, data));
                                        remind.setText(recordDetail.getEnlist_num() + "人报名 出价" + recordDetail.getOffer_num() + "次");
                                        jieshushijian.setText(recordDetail.getEnd_time());//结束时间
                                        boolean isEnd=getTimeNum(recordDetail.getCount_down());
                                        Logger.i("recordDetail.getEnd_time()=",recordDetail.getEnd_time());
                                        Logger.i("recordDetail.getCount_down()=",recordDetail.getCount_down());

                                        if(isEnd){
                                            shootTimeMin.setText(mMin+"");
                                            shootTimeSec.setText(mSecond+"");
                                            shootTimeHour.setText(mHour+"");

                                            try {
                                                startRun();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            //shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
                                        }else{
                                            shootTimeMin.setText(cownStr);
                                            shootTimeSec.setText("00");
                                            shootTimeHour.setText("00");

                                        }
                                        pd_num=Integer.parseInt(recordDetail.getPd().getNum());
                                        pd_stock=Integer.parseInt(recordDetail.getPd().getStock());
                                        kepai_num=pd_num;
                                        delay_time.setText("(第" + recordDetail.getDelay_num() + "轮)");

                                    }

                                }else{
                                    paimairecycleview.setVisibility(View.GONE);
                                    record_title.setVisibility(View.GONE);
                                    remind.setText(recordDetail.getEnlist_num() + "人报名 出价" + recordDetail.getOffer_num() + "次");
                                    jieshushijian.setText(recordDetail.getEnd_time());//结束时间
                                    boolean isEnd=getTimeNum(recordDetail.getCount_down());
                                    Logger.i("recordDetail.getEnd_time()=",recordDetail.getEnd_time());
                                    Logger.i("recordDetail.getCount_down()=",recordDetail.getCount_down());

                                    if(isEnd){
                                        shootTimeMin.setText(mMin+"");
                                        shootTimeSec.setText(mSecond+"");
                                        shootTimeHour.setText(mHour+"");
                                        try {
                                            startRun();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        //shootTime.setText("剩余" + mDay + "天" + mHour + "小时" + mMin + "分钟" + mSecond + "秒");
                                    }else{
                                        shootTimeMin.setText(cownStr);
                                        shootTimeSec.setText("00");
                                        shootTimeHour.setText("00");
                                    }
                                    pd_num=Integer.parseInt(recordDetail.getPd().getNum());
                                    pd_stock=Integer.parseInt(recordDetail.getPd().getStock());
                                    kepai_num=pd_num;
                                    delay_time.setText("(第" + recordDetail.getDelay_num() + "轮)");
                                    //startRun();
                                }
                                DialogUtil.dismiss();
                                //{"ret":200,"code":0,"data":[{"status":"\u5f97\u62cd","user_name":"123213","price":"10.00","num":"5200","unit":"\u65a4","add_time":"2016-06-01 16:09:52"},{"status":"\u5f97\u62cd","user_name":"","price":"10.00","num":"600","unit":"\u65a4","add_time":"2016-05-31 11:35:03"},{"status":"\u5f97\u62cd","user_name":"","price":"1.54","num":"1200","unit":"\u65a4","add_time":"2016-05-30 18:40:56"}],"enlist_num":"3","offer_num":"3","end_time":"33\u592921\u5c0f\u65f629\u52069\u79d2","delay_num":"0","pd":{"current_price":"10.00","shoot_type":0,"stock":"3000"}}
                                //{"data":{"user_name":"baihua","status":"\u5f97\u62cd","price":1.2,"num":500,"unit":"\u65a4"},"delay_num":2,"end_time":"0\u59291\u5c0f\u65f60\u52060\u79d2","pd":{"stock":5000,"shoot_type":2,"current_price":2}}
                            }
                        }


                    }
                });
    }

    //出价接口
    private void productOfferPost(final int productId,String price,String num) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "offer");

        String key= MD5Util.getMD5String(num + price + productId);

        Logger.i(TAG+"price====",price);


       // Logger.i(TAG,"sign="+sign+"-"+"timestamp"+timestamp+"-"+"client_id"+ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this)+"user_id"+ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this)+"id"+String.valueOf(productId)+"-"+);

        OkHttpUtils.post().url(Constant.PRODUCT_OFFER)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id", ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
                .addParams("id",String.valueOf(productId))
                .addParams("price", price)
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
                        Gson gson = new Gson();
                        Ret ret = gson.fromJson(response, Ret.class);
                        if (ret.getCode() == 0) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "出价成功");
                            //刷新UI
                            //临时跳转到提交订单界面
//                            Intent intent = new Intent(ProductFauctionDetailDoingActivity.this, OrderSubmitActivity.class);
//                            intent.putExtra("productId", productId);
//                            startActivity(intent);
                            //}
                        } else if (ret.getCode() == -1) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "用户未登录");
                        } else if (ret.getCode() == -2) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "非法操作");
                        } else if (ret.getCode() == -3) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "出价必须高于当前价格");
                        } else if (ret.getCode() == -4) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "库存不足");
                        } else if (ret.getCode() == -5) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "未缴纳保证金");
                        } else if (ret.getCode() == -6) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "该产品已经拍卖成功");
                        } else if (ret.getCode() == -7) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "拍卖已经结束");
                        } else if (ret.getCode() == -8) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "请填写购买数量");
                        } else if (ret.getCode() == -9) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "保证金必须大于0");
                        } else if (ret.getCode() == -10) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "超时 请重新提交");
                        } else if (ret.getCode() == -11) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "出价价格必须是加价幅度倍数");
                        }else if (ret.getCode() == -12) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "购买数量必须是起拍数量的倍数");
                        }else if (ret.getCode() == -13) {
                            ToastUtil.showLongToast(ProductFauctionDetailDoingActivity.this, "购买数量必须大于可拍数量");
                        }


                    }
                });


    }


    private void shootStatusPost(int productId) {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(ProductFauctionDetailDoingActivity.this, timestamp, "Product", "getShootStatus");

        // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.PRODUCT_GETSHOOTSTATUS)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(ProductFauctionDetailDoingActivity.this))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(ProductFauctionDetailDoingActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(ProductFauctionDetailDoingActivity.this))
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
                            Gson gson=new Gson();
                            ProductDetail1 productDetail1=gson.fromJson(response, ProductDetail1.class);
                            ProductDetail1.DataBean data = productDetail1.getData();

                            login_status=data.getLogin_status();
                            payType=data.getPay_type();
                            //未交保证金
                            if(login_status==-1){
                                chuJia.setText("报名参拍\n(缴纳保证金：￥"+bond+")");
                            }else if(login_status==2){
                                //已经得拍
                                if(payState==0){

                                    if(payType==5){
                                        chuJia.setText("查看相似拍品");
                                    }else{
                                        chuJia.setText("您已成交\n点击去付款");
                                    }
                                    mMin=Integer.parseInt(cownStr);
                                    mSecond=0;
                                    mHour=0;
                                    shootTimeMin.setText(cownStr+"");
                                    shootTimeSec.setText(0+"");
                                    shootTimeHour.setText(0+"");

                                }else{
                                    chuJia.setText("您已成交\n已付款");
                                    mMin=Integer.parseInt(cownStr);
                                    mSecond=0;
                                    mHour=0;
                                    shootTimeMin.setText(cownStr+"");
                                    shootTimeSec.setText(0+"");
                                    shootTimeHour.setText(0+"");
                                }

                            }
                        }





                    }
                });
    }


    class MyPagerAdapter extends FragmentPagerAdapter {

        FauctionDetailFragment fauctionDetailFragment =new FauctionDetailFragment(viewPagers);
        FauctionKnowFragment fauctionKnowFragment= new FauctionKnowFragment(viewPagers);
        FauctionStepFragment fauctionStepFragment= new FauctionStepFragment(viewPagers);




        public MyPagerAdapter(FragmentManager fm) {

            super(fm);


        }





        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return fauctionDetailFragment;
                case 1:


                    return fauctionKnowFragment;
                case 2:

                    return fauctionStepFragment;



                default:
                    return null;
            }
        }

        @Override
        public int getCount() {

            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

    }


    Handler timeHandler=new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if (msg.what==1) {
                computeTime();
//                if(mDay==-1) {
//                    if (pushPo != null) {
//                        shootTime.setText(pushPo.getEnd_time());
//                    }
//
//                    delay_time.setText("结束");
//
//                }else{
                    //shootTime.setText("剩余"+mDay+"天"+mHour+"小时"+mMin+"分钟"+mSecond+"秒");
                    if((mMin==0)&&(mSecond==0)){
                        isRun=false;
                        shootTimeMin.setText(0+"");
                        shootTimeSec.setText(0+"");
                        shootTimeHour.setText(0+"");
                    }
                    shootTimeMin.setText(mMin+"");
                    shootTimeSec.setText(mSecond+"");
                     shootTimeHour.setText(mHour+"");

                if(pushPo!=null){
                        delay_time.setText("(第" + pushPo.getDelay_num() + "轮)");
                   //}

                }

            }
        }
    };

    private boolean getTimeNum(String str) {

        //临时字符串2016-10-21 11:36
        String temp;
        String day;
        String hour;
        String minute;
        String second;
        String[] dayArray=str.split("天");

        day=dayArray[0];

//        if(day.equals("-1")){
//            shootTime.setText(str+" 结束");
//            return false;
//        }

        int length = dayArray.length;
        Logger.i("length=",length+"");
        if("0".equals(str)){
            return false;
        }
        /*if(length==1){
           // shootTime.setText(str+" 结束");
            return false;
        }*/
        //去掉天后
        Logger.i("crowd_str=",str+"");
        if(length<2){
            return false;
        }
        String[] hourArray=dayArray[1].split("小时");
        hour=hourArray[0];


        String[] minuteArray=hourArray[1].split("分");
        minute=minuteArray[0];

        String[] secondArray=minuteArray[1].split("秒");
        second=secondArray[0];


        System.out.println(day);
        System.out.println(hour);
        System.out.println(minute);
        System.out.println(second);

        //
        mDay=Long.parseLong(day);
        mHour=Long.parseLong(hour);
        mMin=Long.parseLong(minute);
        mSecond=Long.parseLong(second);

        return true;

    }

    /**
     * 倒计时计算
     */
    private void computeTime() {

        mSecond--;
        if (mSecond < 0) {
            mMin--;
            mSecond = 59;
            if (mMin < 0) {
                mMin = 59;
                mHour--;
                if (mHour < 0) {
                    // 倒计时结束
                    mHour = 23;
                    mDay--;
                }
            }
        }
    }
    Thread cthread;
    Thread preThread;
    /**
     * 开启倒计时
     */
    private void startRun() throws InterruptedException {
        /*if(cthread!=null){
            cthread.stop();
        }*/
        cthread = new Thread(new Runnable() {

            @Override
            public void run() {

                   try {
                        Thread.sleep(1000);
                        isRun = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }



                // TODO Auto-generated method stub
                while (isRun) {
                    try {
                        Thread.sleep(1000); // sleep 1000msComputeTime();
                        Message message = Message.obtain();
                        message.what = 1;
                        timeHandler.sendMessage(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //preThread=cthread;
        cthread.start();


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
       /* BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);*/
        //Common.viewPagers.removeAllViews();
        Common.viewPagers=null;
       // setConfigCallback(null);
    }

    @Override
    protected void onNetWorkReConnected() {

    }

    @Override
    protected void onNetWorkConnectFail() {

    }
}
