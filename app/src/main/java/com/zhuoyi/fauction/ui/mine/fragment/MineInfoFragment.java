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

import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.MD5Util;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class MineInfoFragment extends Fragment {
    private View viewHolder;
    @Bind(R.id.to_register)
    TextView to_register;
    @Bind(R.id.user_account)
    EditText user_account;
    @Bind(R.id.user_psd)
    EditText user_psd;
    @Bind(R.id.logining)
    Button logining;

    private String TAG="MineInfoFragment";

    private RelativeLayout mine_remind;
    public static MineInfoFragment newInstance(String param1, String param2) {
        MineInfoFragment fragment = new MineInfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_mine_info, container, false);
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
        ((MainActivity)getActivity()).toFragment(new MineInfoFragment());
    }

    @OnClick(R.id.to_register)
    public void toRegister(){
        ((MainActivity)getActivity()).toFragment(new RegisterFragment());
    }

    @OnClick(R.id.logining)
    public void logining(){
        memeberLoginPost();
    }

    @OnClick(R.id.back) void onBackClick() {
        getActivity().onBackPressed();
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

                        Logger.i(TAG,response);
                        ConfigUserManager.setAlreadyLogin(getActivity(),true);


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
