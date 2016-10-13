package com.zhuoyi.fauction.ui.other.adapter;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.MyAddress;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;


public class AddressListAdapter extends BaseAdapter {

    private LayoutInflater mInflater;

    private Context mContext;

    private ListView listview;

    List<Area> list;


    List<MyAddress.DataBean> fauctionDos;

    public AddressListAdapter(Context context, List<MyAddress.DataBean> fauctionDos,ListView alist) {
        mContext = context;
        this.fauctionDos = fauctionDos;
        mInflater = LayoutInflater.from(context);
        listview=alist;
        list=initJsonData();
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
            convertView = mInflater.inflate(R.layout.address_item, null);
            viewHolder = new ViewHolder(convertView);


            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(fauctionDos.get(position).getUname());
        viewHolder.phone.setText(fauctionDos.get(position).getMobile());
        //

        int provinceId = fauctionDos.get(position).getProvince();
        int cityId = fauctionDos.get(position).getCity();
        String areaId = fauctionDos.get(position).getArea();
        StringBuilder sb = new StringBuilder();
        for (Area area : list) {
            String pid = area.getProvince().getId();
            if (pid.equals(provinceId + "")) {
                Area.ProvinceBean province = area.getProvince();
                Logger.i("province.getName()=",province.getName()+"");
                sb.append(province.getName());
                List<Area.ProvinceBean.CityBean> citys = province.getCity();
                for (Area.ProvinceBean.CityBean city : citys) {
                    if (city.getId().equals(cityId+"")) {
                        sb.append(city.getName());
                        List<Area.ProvinceBean.CityBean.AreaBean> areas = city.getArea();
                        for (Area.ProvinceBean.CityBean.AreaBean areaBean : areas) {
                            if (areaBean.getId().equals(areaId+"")) {
                                sb.append(areaBean.getName());
                            }

                        }
                    }
                }
            }
        }
        //address.setText(sb.toString() + orderDetail.getData().getReceipt().getAddress());
        viewHolder.address.setText(sb.toString()+fauctionDos.get(position).getAddress());
        viewHolder.addressSel.setVisibility(View.GONE);
        viewHolder.addressDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(mContext).setTitle("提示").setMessage("确定要删除吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除重新获取
                                String timestamp = DateUtil.getStringDate();

                                String sign = Util.createSign(mContext, timestamp, "Member", "delAddress");


                                // Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
                                OkHttpUtils.post()
                                        .url(Constant.MEMBER_DELADDRESS)
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
                                                    listview.setAdapter(AddressListAdapter.this);
                                                } else if (code == -1) {
                                                    ToastUtil.showLongToast(mContext, "登录超时");
                                                } else if (code == -2) {
                                                    ToastUtil.showLongToast(mContext, "非法操作");
                                                } else if (code == -3) {
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

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private List<Area> initJsonData()
    {
        List<Area> areas= null;
        try
        {
            StringBuffer sb = new StringBuffer();
            InputStream is = mContext.getAssets().open("area.json");
            int len = -1;
            byte[] buf = new byte[1024];
            while ((len = is.read(buf)) != -1)
            {
                sb.append(new String(buf, 0, len, "gbk"));
            }
            is.close();
            Gson gson=new Gson();
            areas=gson.fromJson(sb.toString(),new TypeToken<List<Area>>() {
            }.getType());

        } catch (IOException e)
        {
            e.printStackTrace();

        }
        return areas;

    }


    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.phone)
        TextView phone;
        @Bind(R.id.address)
        TextView address;
        @Bind(R.id.address_sel)
        ImageView addressSel;
        @Bind(R.id.address_del)
        ImageView addressDel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
