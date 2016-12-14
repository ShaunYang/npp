package com.zhuoyi.fauction.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
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
import com.zhuoyi.fauction.ui.GuideActivity;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.mine.activity.FragmentMineInfoActivity;
import com.zhuoyi.fauction.ui.mine.activity.MineRemindActivity;
import com.zhuoyi.fauction.ui.mine.activity.MineSelectCatetoryActivity;
import com.zhuoyi.fauction.ui.mine.activity.MineWalletActivity;
import com.zhuoyi.fauction.ui.mine.activity.MyHaveFauctionActivity;
import com.zhuoyi.fauction.ui.mine.activity.MyPicketActivity;
import com.zhuoyi.fauction.ui.mine.activity.SettingActivity;
import com.zhuoyi.fauction.ui.mine.bond.BondDetailActivity;
import com.zhuoyi.fauction.ui.mine.fragment.LoginFragment;
import com.zhuoyi.fauction.ui.mine.fragment.MineInfoFragment;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;


public class MineFragment extends Fragment {
    private View viewHolder;
    @Bind(R.id.username)
    TextView username;
    @Bind(R.id.to_login)
    Button to_login;
    @Bind(R.id.mine_avator)
    RoundedImageView mine_avator;
    String TAG="MineFragment";

    @Bind(R.id.myhave_fauction)
    RelativeLayout myhave_fauction;

    @Bind(R.id.mine_remind)
    RelativeLayout mineRemind;


    @Bind(R.id.my_fauction)
    RelativeLayout myFauction;

    @Bind(R.id.to_wallet)
    RelativeLayout myWallet;

    @Bind(R.id.mine_sel_category) RelativeLayout mineSelCategory;

    private RelativeLayout mine_remind;
    public static MineFragment newInstance(String param1, String param2) {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_mine, container, false);
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

    private void initComponent(){
        Common.home_tab=4;
        Common.whichActivity=3;
        //Picasso.with(mContext).load(categories.get(position).getNormal_img()).into(iv);
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            //取数据库用户信息
//            DbUtils db = DatabaseManager.getInstance().getDb();
//            try {
//               User user= db.findFirst(Selector.from(User.class).where("phone","=",ConfigUserManager.getRegistPhone(getActivity())));
//                String name=user.getName();
            memberUserInfoPost();
            if(Common.userAvator!=null){
                Picasso.with(getActivity()).load(Common.userAvator).into(mine_avator);
            }



//            } catch (DbException e) {
//                e.printStackTrace();
//            }
            //隐藏登录
            to_login.setVisibility(View.GONE);
        }else{
            //
            to_login.setVisibility(View.VISIBLE);
        }
    }


    @OnClick(R.id.setting) void onSettingClick() {
        Intent intent=new Intent(getActivity(), SettingActivity.class);
        getActivity().startActivity(intent);
    }

    @OnClick(R.id.to_login)
    public void toLogin(){
        ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
    }

    @OnClick(R.id.mine_pick)
    public void toMinePick(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), MyPicketActivity.class);
            startActivity(intent);
        }else{
            // ToastUtil.showLongToast(getActivity(),"请先登录");
            // ((MainActivity)getActivity()).toFragment(new LoginFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }
    }

    @OnClick(R.id.mine_avator)
    public void toMineInfo(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), FragmentMineInfoActivity.class);
            getActivity().startActivity(intent);
        }else{
           // ToastUtil.showLongToast(getActivity(),"请先登录");
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.mine_bond)
    public void toMineBond(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), BondDetailActivity.class);
            getActivity().startActivity(intent);
        }else{
            // ToastUtil.showLongToast(getActivity(),"请先登录");
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.my_fauction)
    public void toMyFauction(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
            //getActivity().startActivity(intent);
        }else{
            //ToastUtil.showLongToast(getActivity(),"请先登录");
            //((MainActivity)getActivity()).toFragment(new LoginFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.myhave_fauction)
    public void toMyhaveFauction(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), MyHaveFauctionActivity.class);
            startActivity(intent);
        }else{
           // ToastUtil.showLongToast(getActivity(),"请先登录");
           // ((MainActivity)getActivity()).toFragment(new LoginFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.to_wallet)
    public void toMyWallet(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), MineWalletActivity.class);
            startActivity(intent);
        }else{
           // ToastUtil.showLongToast(getActivity(),"请先登录");
           // ((MainActivity)getActivity()).toFragment(new LoginFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.mine_remind)
    public void toMindRemind(){
        if(ConfigUserManager.isAlreadyLogin(getContext())){

            Intent intent=new Intent(getActivity(), MineRemindActivity.class);
            startActivity(intent);
        }else{
           // ToastUtil.showLongToast(getActivity(),"请先登录");
           // ((MainActivity)getActivity()).toFragment(new LoginFragment());
            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);
        }

    }

    @OnClick(R.id.mine_sel_category)
    public void toMindSelCategory(){
//        if(ConfigUserManager.isAlreadyLogin(getContext())){
            Intent intent=new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
//        }else{
//            ToastUtil.showLongToast(getActivity(),"请先登录");
//        }

    }

    //在恢复里及时更新数据
    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onStart() {
        initComponent();
        super.onStart();
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

    private void memberUserInfoPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(), timestamp, "Member", "userInfo");

        OkHttpUtils.post()
                .url(Constant.MEMBER_USERINFO)
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

                        Logger.i(TAG + "333333333333=", response);
                        JSONObject retJsonObj= JSONObject.parseObject(response);
                        int code = retJsonObj.getIntValue("code");
                        if(code==0){
                       // if(!retJsonObj.getString("data").equals("[]")){
                            JSONObject data=retJsonObj.getJSONObject("data");
                            String name=data.getString("user_name");
                            if(name!=null&&!name.equals("")){
                                if(username!=null){
                                    username.setText(name);
                                }

                            }else{
                                if(username!=null){
                                    username.setText("");
                                }

                            }
                            if(mine_avator!=null){
                                if(data.getString("img")!=null){
                                    Picasso.with(getActivity()).load(data.getString("img")).into(mine_avator);
                                }

                            }

                        }else if(code==-1){
                            //登录超时 重新登录
                            ConfigUserManager.setAlreadyLogin(getActivity(), false);
                            ((MainActivity)getActivity()).mTabHost.setCurrentTab(3);

                        }

                    }
                });
    }
}
