package com.zhuoyi.fauction.ui.mine.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyAccount;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class YHKListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;

    private ListView listview;


    List<MyAccount.DataBean> fauctionDos;

    public YHKListAdapter(Context context, List<MyAccount.DataBean> fauctionDos, ListView alist) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);
        listview = alist;

    }

    @Override
    public int getCount() {
        return fauctionDos.size();
    }

    @Override
    public Object getItem(int position) {
        return fauctionDos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {

            LayoutInflater mInflater = LayoutInflater.from(mContext);
            convertView = mInflater.inflate(R.layout.bank_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(fauctionDos.get(position).getUname());
        viewHolder.phone.setText(fauctionDos.get(position).getAccount());
        //


        viewHolder.accountSel.setVisibility(View.GONE);
        viewHolder.accountDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext).setTitle("提示").setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除重新获取
                                String timestamp = DateUtil.getStringDate();

                                String sign = Util.createSign(mContext, timestamp, "Wallet", "delAccount");


                                // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
                                OkHttpUtils.post()
                                        .url(Constant.WALLET_DELACCOUNT)
                                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                                        .addParams("timestamp", timestamp)
                                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                                        .addParams("user_id", ConfigUserManager.getUserId(mContext))
                                        .addParams("id", fauctionDos.get(position).getId())
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e) {

                                            }

                                            @Override
                                            public void onResponse(String response) {
                                                Logger.i("address_del" + "===", response);
                                                //显示列表
                                                JSONObject jsonObject = JSONObject.parseObject(response);
                                                int code = jsonObject.getIntValue("code");
                                                if (code == 0) {
                                                    //重新刷新
                                                    // listview中点击按键弹出对话框
                                                    fauctionDos.remove(position);
                                                    // 通过程序我们知道删除了，但是怎么刷新ListView呢？
                                                    // 只需要重新设置一下adapter
                                                    listview.setAdapter(YHKListAdapter.this);

                                                } else if (code == -1) {
                                                    ToastUtil.showLongToast(mContext, "登录超时");
                                                } else if (code == -2) {
                                                    ToastUtil.showLongToast(mContext, "删除失败");
                                                }


                                            }
                                        });

                            }
                        }).show();

            }
        });


        return convertView;


    }


    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.phone)
        TextView phone;
        @Bind(R.id.account_del)
        ImageView accountDel;
        @Bind(R.id.account_sel)
        ImageView accountSel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
