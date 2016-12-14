package com.zhuoyi.fauction.ui.mine.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.yintai.DatabaseManager;
import com.yintai.common.util.Logger;

import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.User;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.GuideGridAdapter;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailDoingActivity;
import com.zhuoyi.fauction.ui.home.activity.ProductFauctionDetailPreActivity;
import com.zhuoyi.fauction.ui.mine.MineFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class LoginFragment extends Fragment {
    private View viewHolder;
    @Bind(R.id.to_register)
    TextView to_register;
    @Bind(R.id.user_account)
    EditText user_account;
    @Bind(R.id.user_psd)
    EditText user_psd;
    @Bind(R.id.logining)
    Button logining;
    @Bind(R.id.forget_psd) TextView forgetPsd;

    private String TAG="LoginFragment";

    List<Category> categoryList;

    private RelativeLayout mine_remind;
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_login, container, false);
            ButterKnife.bind(this, viewHolder);
            BusProvider.getInstance().register(this);
            initComponent();
        }
        ViewGroup parent = (ViewGroup) viewHolder.getParent();
        if (parent != null) {
            parent.removeView(viewHolder);
        }
        ButterKnife.bind(this, viewHolder);
        return viewHolder;
    }


    public void initComponent(){
        ((MainActivity)getActivity()).mTabHost.setVisibility(View.GONE);
        user_account.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    memeberLoginPost();
                    return true;
                }
                return false;
            }
        });
        user_psd.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    memeberLoginPost();
                    return true;
                }
                return false;
            }
        });

    }

    @OnClick(R.id.back) void onBackClick() {
        /*if(Common.home_tab==4){
            //((MainActivity)getActivity()).toFragment(new MineFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(4);
        }else{
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(0);
        }*/

        ((MainActivity)getActivity()).mTabHost.setCurrentTab(Common.home_tab);

        ((MainActivity)getActivity()).mTabHost.setVisibility(View.VISIBLE);


    }

    @OnClick(R.id.forget_psd)
    public void forget_psd(){
        Intent intent=new Intent();
        intent.setClass(((MainActivity) getActivity()),AlertPasswordActivity.class);
        intent.putExtra("title", "忘记密码");
        ((MainActivity) getActivity()).startActivity(intent);
    }

    @OnClick(R.id.to_register)
    public void toRegister(){
        ((MainActivity)getActivity()).toFragment(new RegisterFragment());
    }

    @OnClick(R.id.logining)
    public void logining(){
        memeberLoginPost();
    }

    private void memeberLoginPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Member","login");

        OkHttpUtils.post()
                .url(Constant.MEMBER_LOGIN)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                .addParams("mobile",user_account.getText().toString().trim())
                .addParams("pwd", MD5Util.getMD5String(MD5Util.getMD5String(user_psd.getText().toString().trim())+ConfigUserManager.getDataAuth(getActivity())))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {

                        Logger.i(TAG + "login=====", response);
                        //ConfigUserManager.setAlreadyLogin(getActivity(),true);
                        JSONObject retJsonObj = JSONObject.parseObject(response);
                        int ret = retJsonObj.getInteger("ret");
                        String code = retJsonObj.getString("code");
                        if (code.equals("-1")) {
                            ToastUtil.showLongToast(getActivity(), "账户不能为空");
                        } else if (code.equals("-2")) {
                            ToastUtil.showLongToast(getActivity(), "密码不能为空");
                        } else if (code.equals("-3")) {
                            ToastUtil.showLongToast(getActivity(), "用户不存在或密码错误");
                        } else if (code.equals("-4")) {
                            ToastUtil.showLongToast(getActivity(), "用户被禁用");
                        }else{
                            //缓存token
                            ConfigUserManager.setToken(getActivity(),code);
                            ConfigUserManager.setAlreadyLogin(getActivity(), true);
                                JSONObject data = retJsonObj.getJSONObject("data");
                                String uid = data.getString("uid");
                                Common.userAvator = data.getString("img");
                                JSONArray array = data.getJSONArray("category_id");
                                String categoryString = "";
                                StringBuilder sb = new StringBuilder();

                                if (array != null) {
                                    int size = array.size();
                                    if (size == 1) {
                                        sb.append(array.getString(0));
                                        categoryString = sb.toString();
                                    } else {
                                        for (int i = 0; i < size; i++) {
                                            if (i == (size - 1)) {
                                                sb.append(array.getString(i));
                                            } else {
                                                sb.append(array.getString(i) + "|");
                                            }
                                        }
                                        categoryString = URLEncoder.encode(sb.toString());
                                    }

                                } else {
                                    categoryString = "";
                                }
                              /*  ConfigUserManager.setUserId(getActivity(), uid);
                                ConfigUserManager.setLoginPwd(getActivity(), user_psd.getText().toString().trim());*/
                                //User user=new User();
                                //去数据库取用户 如果没有就重新创建一个user并发送获取category请求
                                //DbUtils db = DatabaseManager.getInstance().getDb();
                                try {
                                    // User user=db.findFirst(Selector.from(User.class).where("id","=",uid));
                               /* if(user!=null){
                                    Common.categories=DatabaseManager.getInstance().getDb().findAll(Selector.from(Category.class).where("user_id","=",ConfigUserManager.getUserId(getActivity())));
                                }else{*/
                                    // 重新创建一个user并发送获取category请求
                                    mainIndexCategoryPost(categoryString);
                                    // }
                                } catch (Exception e) {
                                    e.printStackTrace();
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
                .addParams("timestamp",timestamp)
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

                        Logger.i(TAG+"333333333333=",response);
                        categoryList=new ArrayList<Category>();
                        JSONObject jsonObject=JSONObject.parseObject(response);
                            JSONArray data = jsonObject.getJSONArray("data");
                            for(int i=0;i<data.size();i++){
                                final Category category=new Category();
                                JSONObject categoryJson=data.getJSONObject(i);
                                category.setId(categoryJson.getString("id"));
                                category.setTitle(categoryJson.getString("title"));
                                JSONObject picJsonObject=categoryJson.getJSONObject("pic");
                                String normal_img=picJsonObject.getString("3");
                                String select_img=picJsonObject.getString("4");
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
                            Common.categories=categoryList;
                        ConfigUserManager.saveObject(getActivity(), "sel_category", Common.categories);
                        ToastUtil.showLongToast(getActivity(), "登录成功");
                            ConfigUserManager.setAlreadyLogin(getActivity(), true);
                            //ConfigUserManager.isAlreadyLogin(getContext())=true;
                            //设置记住密码便于下一次启动app登录
                            // ConfigUserManager.
                            if(Common.whichActivity==1){
                                //商品详情跳转过来
                                Intent intent=new Intent(getActivity(), ProductFauctionDetailDoingActivity.class);
                                intent.putExtra("productId",Common.proId);
                                getActivity().startActivity(intent);
                            }else if(Common.whichActivity==2){
                                //商品详情跳转过来
                                Intent intent=new Intent(getActivity(), ProductFauctionDetailPreActivity.class);
                                intent.putExtra("productId",Common.proId);
                                getActivity().startActivity(intent);
                            }else if(Common.whichActivity==3){
                                //用户中心跳转过来
                                ((MainActivity) getActivity()).mTabHost.setCurrentTab(4);
                            }else{
                                ((MainActivity) getActivity()).mTabHost.setCurrentTab(0);

                            }
                        ((MainActivity)getActivity()).mTabHost.setVisibility(View.VISIBLE);



                    }
                });
    }

    //在恢复里及时更新数据
    @Override
    public void onResume() {

        super.onResume();
    }

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
