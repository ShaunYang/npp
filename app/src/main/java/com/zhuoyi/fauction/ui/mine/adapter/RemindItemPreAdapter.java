package com.zhuoyi.fauction.ui.mine.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.MyRemind;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.other.OrderSubmitActivity;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class RemindItemPreAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;


    List<MyRemind> fauctionDos;

    public RemindItemPreAdapter(Context context, List<MyRemind> fauctionDos) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);

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
            convertView = mInflater.inflate(R.layout.remind_item_pre, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.cancleRemind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delRemindPost(Integer.parseInt(fauctionDos.get(position).getId()), position);
            }
        });
        viewHolder.title.setText(fauctionDos.get(position).getTitle());
        viewHolder.price.setText("￥" + fauctionDos.get(position).getPrice() + "/"+fauctionDos.get(position).getUnit());
        viewHolder.remindNum.setText(fauctionDos.get(position).getNum()+fauctionDos.get(position).getUnit());
        viewHolder.endTime.setText("￥" + fauctionDos.get(position).getShoot_time());

        if(fauctionDos.get(position).getPic()!=null){
            Picasso.with(mContext).load(fauctionDos.get(position).getPic()).into(viewHolder.titleImg);
        }



        return convertView;


    }

    private void delRemindPost(int id, final int pos) {



        String timestamp = DateUtil.getStringDate();

        String sign = Util.createSign(mContext, timestamp, "Product", "delRemind");

        OkHttpUtils.post()
                .url(Constant.PRODUCT_DELREMIND)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(mContext))
                .addParams("timestamp", timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(mContext))
                .addParams("user_id", ConfigUserManager.getUserId(mContext))
                .addParams("id", id + "")
                .build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {

                             }

                             @Override
                             public void onResponse(String response) {

                                 Logger.i("Remind--45646456456====", response);
                                 JSONObject jsonObject = JSONObject.parseObject(response);
                                 int code = jsonObject.getIntValue("code");
                                 if (code == 0) {
                                     //
                                     fauctionDos.remove(pos);
                                     notifyDataSetChanged();

                                 } else {
                                     ToastUtil.showLongToast(mContext, "取消失败");
                                 }


                             }
                         }
                );


    }


    static class ViewHolder {
        @Bind(R.id.title_img)
        ImageView titleImg;
        @Bind(R.id.title)
        TextView title;
        @Bind(R.id.price)
        TextView price;
        @Bind(R.id.remind_num)
        TextView remindNum;
        @Bind(R.id.end_time)
        TextView endTime;
        @Bind(R.id.cancle_remind)
        Button cancleRemind;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
