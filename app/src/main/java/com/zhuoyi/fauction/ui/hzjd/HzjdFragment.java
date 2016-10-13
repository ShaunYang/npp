package com.zhuoyi.fauction.ui.hzjd;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.google.gson.Gson;
import com.yintai.common.util.Logger;
import com.yintai.common.util.ToastUtil;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.event.BusProvider;
import com.zhuoyi.fauction.model.Category;
import com.zhuoyi.fauction.model.City;
import com.zhuoyi.fauction.model.FauctionDo;
import com.zhuoyi.fauction.model.MyFauction;
import com.zhuoyi.fauction.model.Provience;
import com.zhuoyi.fauction.model.Qu;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.ui.MainActivity;
import com.zhuoyi.fauction.ui.category.adapter.MyGridAdapter;
import com.zhuoyi.fauction.ui.category.adapter.MyGridView;
import com.zhuoyi.fauction.ui.hzjd.adapter.JDItemAdapter;
import com.zhuoyi.fauction.ui.mine.MineFragment;
import com.zhuoyi.fauction.ui.myfauction.adapter.FauctionItemAdapter;
import com.zhuoyi.fauction.ui.other.OrderDetailActivity;
import com.zhuoyi.fauction.util.Toast;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.xlistview.XListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import okhttp3.Call;


public class HzjdFragment extends Fragment  implements XListView.IXListViewListener{



    private View viewHolder;



    List<Category> categoryList;

    ArrayList<Provience> proviences;

    OptionsPickerView pvOptions;

    TextView tvOptions;

    View vMasker;

    private XListView xListView;
    /**服务器端一共多少条数据*/
    private static  int TOTAL_COUNTER = 64;

    /**每一页展示多少条数据*/
    private static  int REQUEST_COUNT = 10;

    /**已经获取到多少条数据了*/
    private int mCurrentCounter = 0;

    /**当前页*/
    private int mPage = 1;

    //
    int index=1;
    String TAG="HzjdFragment";

    ArrayList<MyFauction> fauctionDos;
    JDItemAdapter mDataAdapter;

    int mStartNo=1;
    //总页数
    int mPageSize=10;
    boolean isFirst=true;

    String base=null;

    private ArrayList<Provience> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<City>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<Qu>>> options3Items = new ArrayList<>();
    public static HzjdFragment newInstance(String param1, String param2) {
        HzjdFragment fragment = new HzjdFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (viewHolder == null) {
            viewHolder = inflater.inflate(R.layout.fragment_hzjd, container, false);
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

    @Override
    public void onStart() {
        //initComponent();
        super.onStart();
    }



    private void initComponent(){
        Common.home_tab=1;
        initDatas();
       getBaseCoPost();
        loadDataListPost(mPage,true,base);

        xListView=(XListView)viewHolder.findViewById(R.id.all_recycle);
        xListView.setPullLoadEnable(true);
        //xListview.setPullLoadEnable(false);
        xListView.setXListViewListener(this);

        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                /*Intent intent=new Intent();
                intent.setClass(getActivity(),OrderDetailActivity.class);
                MyFauction fdo=fauctionDos.get(arg2-1);
                intent.putExtra("id",fdo.getId()+"");
                getActivity().startActivity(intent);*/
                Common.JD_ID=fauctionDos.get(arg2-1).getId();
                ((MainActivity)getActivity()).toFragment(new JDDetailFragment());


            }
        });





    }





    private void getBaseCoPost() {
        String timestamp= DateUtil.getStringDate();

        String sign= Util.createSign(getActivity(),timestamp,"Co","getBase");

        //Logger.i(TAG,"user_id="+ ConfigUserManager.getUserId(getApplicationContext()));
        OkHttpUtils.post()
                .url(Constant.CO_GETBASE)
                .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                .addParams("timestamp",timestamp)
                .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(String response) {
                        Logger.i(TAG+"----json=",response);
                        categoryList = new ArrayList<Category>();
                        JSONObject jsonObject = JSONObject.parseObject(response);
                        proviences=new ArrayList<Provience>();
                        Provience provienceAll=new Provience();
                        provienceAll.setEnum_name("所有地区");
                        ArrayList<City> cityArrayList=new ArrayList<City>();
                        City city1 = new City();
                        city1.setEnum_name("");
                        cityArrayList.add(city1);
                        ArrayList<Qu> quList=new ArrayList<Qu>();
                        Qu qut=new Qu();
                        qut.setEnum_name("");
                        quList.add(qut);
                        city1.setQus(quList);

                        provienceAll.setCitys(cityArrayList);
                        proviences.add(provienceAll);
                        Object provienceObj = jsonObject.get("data");
                        if(provienceObj.getClass().equals(JSONArray.class)){
                            JSONArray data = jsonObject.getJSONArray("data");

                            JSONObject jsonObjectdata = data.getJSONObject(0);
                                //  JSONObject data = jsonObject.getJSONObject("data");
                               // Set<String> strings = jsonObjectdata.keySet();
                            Provience provience=new Provience();
                            ArrayList<City> cityP=new ArrayList<City>();
                            provience.setCitys(cityP);
                            provience.setEnum_name(jsonObjectdata.getString("enum_name"));
                            provience.setEnum_value(jsonObjectdata.getString("enum_value"));
                            proviences.add(provience);
                            //遍历市
                            Object cityObj = jsonObjectdata.get(jsonObjectdata.getString("enum_value"));
                            //如果是数组
                            if(cityObj.getClass().equals(JSONArray.class)){
                                JSONArray cityData = jsonObjectdata.getJSONArray(jsonObjectdata.getString("enum_value"));
                                for(int i=0;i<cityData.size();i++){
                                    JSONObject jsonObjectCity = cityData.getJSONObject(i);
                                    //城市转map
                                    //Iterator<String> iterator1 = jsonObjectCity.keySet().iterator();
                                    City city=new City();
                                    city.setEnum_name(jsonObjectCity.getString("enum_name"));
                                    city.setEnum_value(jsonObjectCity.getString("enum_value"));
                                    cityP.add(city);
                                    ArrayList<Qu> qus=new ArrayList<Qu>();
                                    city.setQus(qus);
                                    Object quObj = jsonObjectCity.get(jsonObjectCity.getString("enum_value"));
                                    if(quObj.getClass().equals(JSONArray.class)){
                                        //jsonObjectCity.get("");
                                        JSONArray quData=jsonObjectCity.getJSONArray(jsonObjectCity.getString("enum_value"));
                                        for(int k=0;k<quData.size();k++){
                                            JSONObject quJsonObj = quData.getJSONObject(k);
                                            Qu qu=new Qu();
                                            qu.setEnum_name(quJsonObj.getString("enum_name"));
                                            qu.setEnum_value(quJsonObj.getString("enum_value"));
                                            qus.add(qu);
                                            city.setQus(qus);
                                            //cityP.add(city);
                                        }

                                    }

                                   /* while(iterator1.hasNext()){
                                        //City city=new City();
                                        String citykey=iterator1.next().toString();
                                        String cityvalue=jsonObjectCity.getString(citykey);
                                        Log.i(TAG+"city:",citykey+"---"+cityvalue);
                                        City city=new City();
                                        city.setEnum_name();
                                    }*/
                                }
                            }/*else {
                                JSONObject cityData = jsonObjectdata.getJSONObject(jsonObjectdata.getString("enum_value"));
                                Set<String> cityKeys = cityData.keySet();
                                Iterator<String> cityKeyIt = cityKeys.iterator();
                                while (cityKeyIt.hasNext()) {
                                    String citykey = cityKeyIt.next().toString();
                                    String cityvalue = cityData.getString(citykey);
                                    Log.i(TAG + "city:", citykey + "---" + cityvalue);
                                    //取地区
                                    //取值jsonobjcet
                                    JSONObject cityJsonObj = JSONObject.parseObject(cityvalue);
                                    //new city
                                    City city = new City();
                                    city.setEnum_name(cityJsonObj.getString("enum_name"));
                                    city.setEnum_value(cityJsonObj.getString("enum_value"));
                                    Object quObj = cityJsonObj.get(cityJsonObj.getString("enum_value"));
                                    if (quObj != null) {
                                        if (quObj.getClass().equals(JSONArray.class)) {
                                            //
                                            JSONArray quData = cityJsonObj.getJSONArray(cityJsonObj.getString("enum_value"));
                                            ArrayList<Qu> qus = new ArrayList<Qu>();
                                            for (int i = 0; i < quData.size(); i++) {
                                                JSONObject jsonObjectQu = quData.getJSONObject(i);
                                                //区
                                                Qu qu = new Qu();
                                                qu.setEnum_name(jsonObjectQu.getString("enum_name"));
                                                qu.setEnum_value(jsonObjectQu.getString("enum_value"));
                                                qus.add(qu);
                                            }
                                            city.setQus(qus);
                                        }
                                    } else {
                                        ArrayList<Qu> qus = new ArrayList<Qu>();
                                        Qu qu = new Qu();
                                        qu.setEnum_name("");
                                        // qu.setEnum_value(quData.getString("enum_value"));
                                        qus.add(qu);
                                        city.setQus(qus);
                                    }

                                    // JSONObject cityJsonObjStr = cityJsonObj.getJSONObject(cityJsonObj.getString("enum_value"));

                                    cityP.add(city);
                                }
                            }*/
                        }else{
                            JSONObject data2 = jsonObject.getJSONObject("data");
                            Set<String> strings = data2.keySet();
                            Iterator<String> iterator = strings.iterator();
                            while (iterator.hasNext()){
                                String key=iterator.next().toString();
                                String value=data2.getString(key);

                                //取值jsonobjcet
                                JSONObject provienceJsonObj = JSONObject.parseObject(value);
                                //new 省
                                Provience provience1=new Provience();
                                ArrayList<City> cityP1=new ArrayList<City>();
                                provience1.setCitys(cityP1);
                                provience1.setEnum_name(provienceJsonObj.getString("enum_name"));
                                provience1.setEnum_value(provienceJsonObj.getString("enum_value"));
                                //遍历市
                                Object cityObj1 = provienceJsonObj.get(provienceJsonObj.getString("enum_value"));
                                //如果是数组
                                if(cityObj1.getClass().equals(JSONArray.class)){
                                    JSONArray cityData = provienceJsonObj.getJSONArray(provienceJsonObj.getString("enum_value"));
                                    for(int i=0;i<cityData.size();i++){
                                        JSONObject jsonObjectCity = cityData.getJSONObject(i);
                                        //城市转map
                                        //Iterator<String> iterator1 = jsonObjectCity.keySet().iterator();
                                        City city=new City();
                                        city.setEnum_name(jsonObjectCity.getString("enum_name"));
                                        city.setEnum_value(jsonObjectCity.getString("enum_value"));
                                        ArrayList<Qu> qus=new ArrayList<Qu>();
                                        Qu qu=new Qu();
                                        qu.setEnum_name("");
                                        // qu.setEnum_value(quData.getString("enum_value"));
                                        qus.add(qu);
                                        city.setQus(qus);
                                        cityP1.add(city);
                                   /* while(iterator1.hasNext()){
                                        //City city=new City();
                                        String citykey=iterator1.next().toString();
                                        String cityvalue=jsonObjectCity.getString(citykey);
                                        Log.i(TAG+"city:",citykey+"---"+cityvalue);
                                        City city=new City();
                                        city.setEnum_name();
                                    }*/
                                    }
                                }else{
                                    JSONObject cityData = provienceJsonObj.getJSONObject(provienceJsonObj.getString("enum_value"));
                                    Set<String> cityKeys = cityData.keySet();
                                    Iterator<String> cityKeyIt = cityKeys.iterator();
                                    while (cityKeyIt.hasNext()){
                                        String citykey=cityKeyIt.next().toString();
                                        String cityvalue=cityData.getString(citykey);
                                        Log.i(TAG+"city:",citykey+"---"+cityvalue);
                                        //取地区
                                        //取值jsonobjcet
                                        JSONObject cityJsonObj = JSONObject.parseObject(cityvalue);
                                        //new city
                                        City city=new City();
                                        city.setEnum_name(cityJsonObj.getString("enum_name"));
                                        city.setEnum_value(cityJsonObj.getString("enum_value"));
                                        Object quObj = cityJsonObj.get(cityJsonObj.getString("enum_value"));
                                        if(quObj!=null){
                                            if(quObj.getClass().equals(JSONArray.class)){
                                                //
                                                JSONArray quData = cityJsonObj.getJSONArray(cityJsonObj.getString("enum_value"));
                                                ArrayList<Qu> qus=new ArrayList<Qu>();
                                                for(int i=0;i<quData.size();i++){
                                                    JSONObject jsonObjectQu = quData.getJSONObject(i);
                                                    //区
                                                    Qu qu=new Qu();
                                                    qu.setEnum_name(jsonObjectQu.getString("enum_name"));
                                                    qu.setEnum_value(jsonObjectQu.getString("enum_value"));
                                                    qus.add(qu);
                                                }
                                                city.setQus(qus);
                                            }
                                        }else{
                                            ArrayList<Qu> qus=new ArrayList<Qu>();
                                            Qu qu=new Qu();
                                            qu.setEnum_name("");
                                            // qu.setEnum_value(quData.getString("enum_value"));
                                            qus.add(qu);
                                            city.setQus(qus);
                                        }

                                        // JSONObject cityJsonObjStr = cityJsonObj.getJSONObject(cityJsonObj.getString("enum_value"));

                                        cityP1.add(city);
                                    }
                                    //

                                }

                                proviences.add(provience1);
                                Logger.i(TAG+"provience:",key+"---"+value);
                            }
                        }

                       /* //数组
                        if(provienceObj.getClass().equals(JSONArray.class)){
                            JSONArray data = jsonObject.getJSONArray("data");
                            if (data.size() > 0) {
                                data.
                            }


                        }else{
                        //对象

                        }*/
                        //选项1
                        for(Provience p:proviences){
                            options1Items.add(p);
                            //选项2

                            options2Items.add(p.getCitys());




                        }

                        //选项3
                        for(Provience p:proviences){
                            ArrayList<ArrayList<Qu>> ques=new ArrayList<>();
                            ArrayList<City> citys = p.getCitys();
                            for(City c:citys){
                                ArrayList<Qu> options3Items_01=new ArrayList<>();
                                for(Qu q:c.getQus()){
                                    options3Items_01.add(q);

                                }
                                ques.add(options3Items_01);
                            }
                            options3Items.add(ques);
                        }


                        //三级联动效果
                        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
                        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
                        pvOptions.setTitle("选择城市");
                        pvOptions.setCyclic(false, false, false);
                        //设置默认选中的三级项目
                        //监听确定选择按钮

                        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

                            @Override
                            public void onOptionsSelect(int options1, int option2, int options3) {
                                //返回的分别是三个级别的选中位置
                                String tx = options1Items.get(options1).getEnum_name()
                                        + options2Items.get(options1).get(option2).getEnum_name()
                                        + options3Items.get(options1).get(option2).get(options3).getEnum_name();
                                 tvOptions.setText(tx);
                                mPage=1;
                                String base1=options3Items.get(options1).get(option2).get(options3).getEnum_value();
                                String base2=options2Items.get(options1).get(option2).getEnum_value();
                                String base3=options1Items.get(options1).getEnum_value();
                                if(base1!=null){
                                    base=base1;
                                    loadDataListPost(mPage,true,base);
                                }else{
                                    if(base2!=null){
                                        base=base2;
                                        loadDataListPost(mPage,true,base);
                                    }else{
                                        if(base3!=null){
                                            base=base3;
                                            loadDataListPost(mPage,true,base);
                                        }else{
                                            base=null;
                                            loadDataListPost(mPage,true,base);
                                        }
                                    }
                                }
                                //省市县代码判断
                               /* if(base!=null){
                                    loadDataListPost(mPage,true,base);
                                }else{
                                    loadDataListPost(mPage,true,null);
                                }*/

                                // ToastUtil.showShortToast(getContext(),options3Items.get(options1).get(option2).get(options3).getEnum_value());
                                vMasker.setVisibility(View.GONE);

                            }
                        });
                        //点击弹出选项选择器
                        tvOptions.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                pvOptions.show();
                            }
                        });




                    }
                });
    }

    //在恢复里及时更新数据
    @Override
    public void onResume() {
       // initComponent();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mPage=1;
        loadDataListPost(mPage,true,base);
    }



    private void loadDataListPost(final int pageSize,final boolean isFlash,String base) {

        if(base!=null){
            if (pageSize <= mPageSize) {
                String timestamp = DateUtil.getStringDate();

                String sign = Util.createSign(getActivity(), timestamp, "Co", "listDate");

                OkHttpUtils.post()
                        .url(Constant.CO_LISTDATE)
                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                        .addParams("timestamp", timestamp)
                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                        .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                        .addParams("base", base)
                        .addParams("page", String.valueOf(pageSize))

                        .build()
                        .execute(new StringCallback() {
                                     @Override
                                     public void onError(Call call, Exception e) {

                                     }

                                     @Override
                                     public void onResponse(String response) {

                                         Logger.i(TAG + "45646456456====", response);
                                         // if (isFirst)
                                         // ((MainActivity) getActivity()).dismiss();


                                         //
                                         //json解析
                                         JSONObject jsonObject = JSONObject.parseObject(response);
                                         int code = jsonObject.getIntValue("code");
                                         if (code==0) {
                                             if (isFlash)
                                                 fauctionDos = new ArrayList<MyFauction>();
                                             JSONArray data = jsonObject.getJSONArray("data");

                                             mPageSize = jsonObject.getInteger("total_page");
                                             for (int i = 0; i < data.size(); i++) {

                                                 JSONObject temp = data.getJSONObject(i);
                                                 if (temp == null) {
                                                     break;
                                                 }

                                                 MyFauction fauctionDo = new MyFauction();
                                                 fauctionDo.setId(temp.getString("id"));
                                                 fauctionDo.setTitle(temp.getString("title"));
                                                 fauctionDo.setPic(temp.getString("pic"));
                                                 fauctionDos.add(fauctionDo);
                                             }


                                             if (isFlash) {
                                                 mDataAdapter = new JDItemAdapter(getActivity(), fauctionDos);
                                                 xListView.setAdapter(mDataAdapter);
                                             } else {
                                                 mDataAdapter.notifyDataSetChanged();
                                             }
                                             onLoad();
                                             // mStartNo=startNo+pageSize;
                                             //页数自增1
                                             mPage++;
                                             isFirst = false;


                                         }else if(code==-1){
                                             xListView.mFooterView.hide();
                                             xListView.mFooterView.setOnClickListener(null);
                                             //清空数据
                                             if(fauctionDos!=null){
                                                 fauctionDos.clear();
                                                 mDataAdapter.notifyDataSetChanged();

                                             }
                                         }
                                     }
                                 }
                        );

            }
        }else{
            if (pageSize <= mPageSize) {
                String timestamp = DateUtil.getStringDate();

                String sign = Util.createSign(getActivity(), timestamp, "Co", "listDate");

                OkHttpUtils.post()
                        .url(Constant.CO_LISTDATE)
                        .addParams("sign", sign).addParams("codes", ConfigUserManager.getToken(getContext()))
                        .addParams("timestamp", timestamp)
                        .addParams("client_id", ConfigUserManager.getUnicodeIEME(getActivity()))
                        .addParams("user_id", ConfigUserManager.getUserId(getActivity()))
                        .addParams("page", String.valueOf(pageSize))

                        .build()
                        .execute(new StringCallback() {
                                     @Override
                                     public void onError(Call call, Exception e) {

                                     }

                                     @Override
                                     public void onResponse(String response) {

                                         Logger.i(TAG + "45646456456====", response);
                                         // if (isFirst)
                                         // ((MainActivity) getActivity()).dismiss();


                                         //
                                         //json解析
                                         JSONObject jsonObject = JSONObject.parseObject(response);
                                         int code = jsonObject.getIntValue("code");
                                         if (code==0) {
                                             if (isFlash)
                                                 fauctionDos = new ArrayList<MyFauction>();

                                             JSONArray data = jsonObject.getJSONArray("data");

                                             mPageSize = jsonObject.getInteger("total_page");
                                             for (int i = 0; i < data.size(); i++) {

                                                 JSONObject temp = data.getJSONObject(i);
                                                 if (temp == null) {
                                                     break;
                                                 }

                                                 MyFauction fauctionDo = new MyFauction();
                                                 fauctionDo.setId(temp.getString("id"));
                                                 fauctionDo.setTitle(temp.getString("title"));
                                                 fauctionDo.setPic(temp.getString("pic"));
                                                 fauctionDos.add(fauctionDo);
                                             }


                                             if (isFlash) {
                                                 mDataAdapter = new JDItemAdapter(getActivity(), fauctionDos);
                                                 xListView.setAdapter(mDataAdapter);
                                             } else {
                                                 mDataAdapter.notifyDataSetChanged();
                                             }
                                             onLoad();
                                             // mStartNo=startNo+pageSize;
                                             //页数自增1
                                             mPage++;
                                             isFirst = false;


                                         }else if(code==-1){
                                             //清空数据
                                             if(fauctionDos!=null){
                                                 fauctionDos.clear();
                                                 mDataAdapter.notifyDataSetChanged();

                                             }
                                         }
                                     }
                                 }
                        );

            }
        }

    }



    @Override
    public void onLoadMore() {
        if(mPage<=mPageSize){
            loadDataListPost(mPage,false,base);
        }else{
            xListView.stopRefresh();
            xListView.stopLoadMore();
            xListView.mFootTextView.setText("");

        }

    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime("刚刚");
    }



    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    public String unescapeUnicode(String str){
        StringBuffer b=new StringBuffer();
        Matcher m = Pattern.compile("\\\\u([0-9a-fA-F]{4})").matcher(str);
        while(m.find())
            b.append((char)Integer.parseInt(m.group(1),16));
        return b.toString();
    }

    private void initDatas()
    {

        tvOptions= (Button) viewHolder.findViewById(R.id.ntitle);
        vMasker=viewHolder.findViewById(R.id.vMasker);
        //选项选择器
        pvOptions = new OptionsPickerView(getContext());


        //选项2

        //选项3
//        mDatas = new ArrayList<Integer>(Arrays.asList(R.drawable.category_yumi,
//                R.drawable.category_qinggua, R.drawable.category_lajiao, R.drawable.category_sijidou));
//        mDataString=new ArrayList<String>(Arrays.asList("玉米", "青瓜","辣椒","四季豆"));
    }
}
