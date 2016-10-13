package com.zhuoyi.fauction.ui.mine.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.net.URLEncoder;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class RegisterFragment extends Fragment {
    private View viewHolder;
    @Bind(R.id.user_phone)
    EditText user_phone;
    @Bind(R.id.code)
    EditText code;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.comfirm_psd)
    EditText comfirm_psd;
    @Bind(R.id.get_code)
    Button get_code;
    @Bind(R.id.user_agreement)
    TextView user_agreement;
    @Bind(R.id.register)
    Button register;

    private String TAG="RegisterFragment";


    private RelativeLayout mine_remind;
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_register, container, false);
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
        if(Common.reg_phone!=null){
            user_phone.setText(Common.reg_phone);
        }
        if(Common.reg_code!=null){
            code.setText(Common.reg_code);
        }
        if(Common.reg_psd!=null){
            password.setText(Common.reg_psd);
        }
        if(Common.reg_cpsd!=null){
            comfirm_psd.setText(Common.reg_cpsd);
        }
        ((MainActivity)getActivity()).mTabHost.setVisibility(View.GONE);
    }

    @OnClick(R.id.user_agreement)
    public void user_agreement(){
        //纪录用户填写
        Common.reg_phone=user_phone.getText().toString();
        Common.reg_code=code.getText().toString();
        Common.reg_psd=password.getText().toString();
        Common.reg_cpsd=comfirm_psd.getText().toString();
        ((MainActivity) getActivity()).toFragment(new XieyiRegisterFragment());
    }

    @OnClick(R.id.get_code)
    public void getPhoneCode(){
        sendCodePost();
    }

    @OnClick(R.id.back) void onBackClick() {
        ((MainActivity)getActivity()).mTabHost.setCurrentTab(2);
        ((MainActivity)getActivity()).toFragment(new LoginFragment());
    }

    private void sendCodePost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Member","sendCode");

            OkHttpUtils.post()
                    .url(Constant.MEMBER_SENDCODE)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                    .addParams("user_id",ConfigUserManager.getUserId(getActivity()))
                    .addParams("mobile",user_phone.getText().toString().trim())
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
                            if (code == 0) {
                                ToastUtil.showLongToast(getActivity(),"成功");
                            } else if (code == -1) {
                                ToastUtil.showLongToast(getActivity(),"手机号错误");
                            } else if (code==-2) {
                                ToastUtil.showLongToast(getActivity(),"发送失败  \n" +
                                        "短信验证码有效时间为3分钟（180秒）");
                            }


                        }
                    });




    }

    @OnClick(R.id.register)
    public void onRegister(){
        memeberRegPost();
    }

    private void memeberRegPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Member","reg");
        String categoryString="";

        try {
            //List<Category> categories = DatabaseManager.getInstance().getDb().findAll(Category.class);
            List<Category> categories = Common.categories;
            StringBuilder sb=new StringBuilder();
            if(categories!=null){
                int size=categories.size();
                if(size==1){
                    sb.append(categories.get(0).getId());
                    categoryString= sb.toString();
                }else{
                    for(int i=0;i<size;i++){
                        if(i==(size-1)){
                            sb.append(categories.get(i).getId());
                        }else{
                            sb.append(categories.get(i).getId()+"|");
                        }
                    }
                    categoryString= URLEncoder.encode(sb.toString());
                }

            }else{
                categoryString="";
            }
            OkHttpUtils.post()
                    .url(Constant.MEMBER_REG)
                    .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                    .addParams("timestamp",timestamp)
                    .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                    .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                    .addParams("mobile", user_phone.getText().toString().trim())
                    .addParams("code",code.getText().toString().trim())
                    .addParams("pwd",password.getText().toString().trim())
                    .addParams("pwd2",comfirm_psd.getText().toString().trim())
                    .addParams("category",categoryString)
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
                            if (code == 0) {
                                //保存用户信息到数据库
                                ToastUtil.showLongToast(getActivity(), "注册成功");
                                ((MainActivity) getActivity()).mTabHost.setCurrentTab(2);
                                ((MainActivity) getActivity()).toFragment(new LoginFragment());
                            }else if(code==-1){
                                ToastUtil.showLongToast(getActivity(), "手机号错误");
                            }else if(code==-2){
                                ToastUtil.showLongToast(getActivity(), "密码字符只能由字母和数字组成");
                            }else if(code==-3){
                                ToastUtil.showLongToast(getActivity(), "密码长度须在6-16个任意字母或数字组合");
                            }else if(code==-4){
                                ToastUtil.showLongToast(getActivity(), "两次输入的密码不一致");
                            }else if(code==-5){
                                ToastUtil.showLongToast(getActivity(), "注册失败");
                            }else if(code==-6){
                                ToastUtil.showLongToast(getActivity(), "验证码错误");
                            }else if(code==-7){
                                ToastUtil.showLongToast(getActivity(), "手机号已经被注册 请更换手机号注册");
                            }


                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }


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
