package com.zhuoyi.fauction.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.otto.Subscribe;
import com.yintai.common.util.Logger;
import com.yintai.common.util.SystemUtil;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.MobileManagerApplication;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigParams;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.category.CategoryFragment;
import com.zhuoyi.fauction.ui.home.HomeFragemt;
import com.zhuoyi.fauction.ui.hzjd.HzjdFragment;
import com.zhuoyi.fauction.ui.mine.MineFragment;
import com.zhuoyi.fauction.ui.mine.fragment.RegisterFragment;
import com.zhuoyi.fauction.ui.myfauction.MyFauctionFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.UITool;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class MainActivity extends FragmentActivity {

    private static final String TAG=MainActivity.class.getSimpleName();

    //定义数组存放Fragment界面
    private Class fragmentArray[]={HomeFragemt.class, HzjdFragment.class,CategoryFragment.class, MyFauctionFragment.class, MineFragment.class};

    //定义数组存放按钮图片
    private int mImageViewArray[]={R.drawable.btn_home,R.drawable.btn_hzjd,R.drawable.btn_category,R.drawable.btn_myfauction,R.drawable.btn_mine};

    //定义数组存放标题
    private String mTabText[]={"精选","合作基地","品种","我拍到的","我的"};

    private LayoutInflater layoutInflater;
    public FragmentTabHost mTabHost;
    private long mLastClickBackButtonTime;
    private boolean isBackClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ((MobileManagerApplication)MobileManagerApplication.getInstance()).addActivity(this);
        BusProvider.getInstance().register(this);
        initView();
    }

    private void initData(){

    }

    public void toFragment(Fragment toFragment){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.realtabcontent, toFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    public void toFragmentReplayce(Fragment toFragment){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.realtabcontent, toFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
    }

    private void initView(){



        //实例化布局对象
        layoutInflater=LayoutInflater.from(this);
        FragmentManager manager=getSupportFragmentManager();
        //实例化TabHost对象，得到TabHost
        mTabHost=(FragmentTabHost) findViewById(android.R.id.tabhost);

        mTabHost.setup(this, manager, R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++){
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(mTabText[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            mTabHost.getTabWidget().setDividerDrawable(null);
//            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.radio_button_bg);
        }
        Intent intent=getIntent();
        int tab=intent.getIntExtra("tab",0);
        mTabHost.setCurrentTab(tab);
//        Fragment homeFragment = manager.findFragmentByTag(mTabText[1]);
//        Logger.i(TAG, "首页是否为空？"+homeFragment);
//        manager.beginTransaction().show(homeFragment).commit();
    }

    @Override
    protected void onRestart() {
        //mTabHost.setCurrentTab(Common.home_tab);
        super.onRestart();
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index){
        View view = layoutInflater.inflate(R.layout.tab_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView tabText = (TextView) view.findViewById(R.id.tv_tab_text);
        tabText.setText(mTabText[index]);

        return view;
    }

   /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goback();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    private void goback() {
        long currentTime = System.currentTimeMillis();
        long detal = currentTime - mLastClickBackButtonTime;
        if (isBackClicked && detal < 1300) {
            appExit();
        } else {
            isBackClicked = true;
            mLastClickBackButtonTime = currentTime;
            ToastUtil.showShortToast(this, getResources().getString(R.string.exit));
        }
    }

  /*  @Subscribe
    public void onLoginExitEven(OnAppExitEvent event){
        appExit();
    }*/

    private void appExit(){

        memberLoginOutPost();

    }

    // 退出时间

    private long currentBackPressedTime = 0;

    // 退出间隔

    private static final int BACK_PRESSED_INTERVAL = 2000;

    //重写onBackPressed()方法,继承自退出的方法

    @Override
    public void onBackPressed() {

        // 判断时间间隔

        if (System.currentTimeMillis()- currentBackPressedTime > BACK_PRESSED_INTERVAL) {

            currentBackPressedTime = System.currentTimeMillis();

            Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();

        } else {

            // 退出
            memberLoginOutPost();
           // finish();

        }

    }

    private void memberLoginOutPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(MainActivity.this, timestamp, "Member", "loginOut");

        OkHttpUtils.post()
                .url(Constant.MEMBER_LOGINOUT)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getApplicationContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(MainActivity.this))
                .addParams("user_id",ConfigUserManager.getUserId(MainActivity.this))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "333333333333=", response);
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        int code = jsonObject.getIntValue("code");
                        if (code==0) {
                            Logger.e("mgc", "应用退出！！！");

                            SystemUtil.exit(MainActivity.this, new SystemUtil.OnAppExit() {
                                @Override
                                public void exit() {
                          /*  ChatManager.getInstance().chatLogout();
                            ChatManager.getInstance().release();
                            AppServerConfig.destoryFactory();//销毁工厂*/
                                    //ConfigUserManager.setAlreadyLogin(MainActivity.this, false);

                                    ((MobileManagerApplication)MobileManagerApplication.getInstance()).exit(getApplicationContext());
                                    //((MobileManagerApplication)MobileManagerApplication.getInstance()).exit(MainActivity.this);


                                }
                            });
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        BusProvider.getInstance().unregister(this);
        super.onDestroy();
    }
}
