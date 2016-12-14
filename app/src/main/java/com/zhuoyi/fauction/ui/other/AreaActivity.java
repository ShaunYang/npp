package com.zhuoyi.fauction.ui.other;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuoyi.fauction.ui.BaseActivity;
import com.yintai.common.util.Logger;
import com.zhuoyi.fauction.R;

import com.zhuoyi.fauction.config.Common;
import com.zhuoyi.fauction.config.ConfigUserManager;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.model.Order;
import com.zhuoyi.fauction.net.Constant;
import com.zhuoyi.fauction.utils.DateUtil;
import com.zhuoyi.fauction.utils.Util;
import com.zhuoyi.fauction.widget.OnWheelChangedListener;
import com.zhuoyi.fauction.widget.WheelView;

import com.zhuoyi.fauction.widget.adapters.ArrayWheelAdapter;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 引导页面
 * 
 * @author Enway
 * 
 */
public class AreaActivity extends BaseActivity implements OnWheelChangedListener{


	@Bind(R.id.back) ImageView back;
	@Bind(R.id.id_province) WheelView mViewProvince;
	@Bind(R.id.id_city) WheelView mViewCity;
	@Bind(R.id.id_district)
	WheelView mViewDistrict;
	@Bind(R.id.btn_confirm)
	Button btn_confirm;


	Area.ProvinceBean province;
	Area.ProvinceBean.CityBean city;
	Area.ProvinceBean.CityBean.AreaBean area;

	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName ="";

	/**
	 * 当前省id
	 */
	protected String mCurrentProviceNameId;
	/**
	 * 当前市id
	 */
	protected String mCurrentCityNameId;
	/**
	 * 当前区id
	 */
	protected String mCurrentDistrictNameId ="";

	/**
	 * 所有省
	 */
	public String[] mProvinceDatas;

	List<Area> areas;


	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		// TODO Auto-generated method stub
		if (wheel == mViewProvince) {
			updateCities();
		} else if (wheel == mViewCity) {
			if(city==null){
				return;
			}
			updateAreas();
		} else if (wheel == mViewDistrict) {
			if(city!=null){
				List<Area.ProvinceBean.CityBean.AreaBean> areas = city.getArea();
				Area.ProvinceBean.CityBean.AreaBean areaBean = areas.get(newValue);
				mCurrentDistrictName=areaBean.getName();
				Common.area=areaBean;
			}else{
				mCurrentDistrictName="";
				Common.area=null;
			}

//			for(Area.ProvinceBean.CityBean.AreaBean areaT:areas){
//				if(areaT.getName().equals(newValue)){
//					area=areaT;
//					Common.area=area;
//					mCurrentDistrictName = area.getName();
//					Logger.i("sss",mCurrentDistrictName);
//					break;
//				}
//			}



		}
	}

	@OnClick(R.id.back) void onBackClick() {
		//TODO implement
		onBackPressed();
	}

	@OnClick(R.id.btn_confirm)
	public void comfirm_back(){
		/*if(Common.area==null){
			Common.area=areas.get(0).getProvince().getCity().get(0).getArea().get(0);
		}*/

		Intent intent=new Intent();
		intent.putExtra("mCurrentProviceName",mCurrentProviceName);
		intent.putExtra("mCurrentCityName",mCurrentCityName);
		intent.putExtra("mCurrentDistrictName",mCurrentDistrictName);
		setResult(RESULT_OK, intent);
		finish();

	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {

		int pCurrent = mViewCity.getCurrentItem();
		List<Area.ProvinceBean.CityBean> cityb = province.getCity();


		if(cityb!=null){
			if(province.getCity().size()<=0){
				Common.city=null;
				mCurrentCityName ="";
				Common.area=null;
				Common.province=province;
				mCurrentDistrictName="";
				area=null;
				city=null;
			}else{
				city =province.getCity().get(pCurrent);
				Common.city=city;
				mCurrentCityName = this.city.getName();
				if(city.getArea().size()<=0){
					Common.area=null;
					area=null;
					mCurrentDistrictName="";
				}else{
					Common.area=city.getArea().get(0);
					mCurrentDistrictName=Common.area.getName();
				}

			}


			List<String> areaList=new ArrayList<String>();
			if(city!=null){
				for(Area.ProvinceBean.CityBean.AreaBean areaBean: this.city.getArea()){
					areaList.add(areaBean.getName());
				}
				String[] areas =areaList.toArray(new String[areaList.size()]);

				if (areas == null) {
					areas = new String[] { "" };
				}

				mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(AreaActivity.this, areas));
				mViewDistrict.setCurrentItem(0);
			}else{
				//areas = new String[] { "" };
				mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(AreaActivity.this, new String[] { "" }));
				mViewDistrict.setCurrentItem(0);
			}

		}

	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		province = areas.get(pCurrent).getProvince();
		Common.province=province;
		int size = province.getCity().size();
		Logger.i("areactivity_provincesize=", size + "");
//		Logger.i("areactivity_provinceareasize=", province.getCity().get(0).getArea().size() + "");
		//Log.i("areactivity_provincesize=", size + "");
		if(size<=0){
			//mCurrentDistrictName="";

			Common.area=null;
			Common.city=null;
			city=null;
		}else{
			if(province.getCity().get(0).getArea().size()<=0){
				Common.area=null;
				Common.city=province.getCity().get(0);
				mCurrentDistrictName="";
			}else{
				Common.area=province.getCity().get(0).getArea().get(0);
				mCurrentDistrictName=Common.area.getName();
			}

		}


		//Common.province=province;
		mCurrentProviceName = province.getName();
		mCurrentProviceNameId=province.getId();
		List<String> cityList=new ArrayList<String>();
		for(Area.ProvinceBean.CityBean cityBean:province.getCity()){
			cityList.add(cityBean.getName());
		}
		String[] cities =cityList.toArray(new String[cityList.size()]);

		if (cities == null) {
			cities = new String[] { "" };
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}



	@Override
	protected void initComponent(Bundle bundle) {
		setContentView(R.layout.areasel_address);
		ButterKnife.bind(this);

		initView();
		areas=initJsonData();


		List<String> mProvinceList=new ArrayList<String>();
		for(Area area:areas){
			mProvinceList.add(area.getProvince().getName());
		}
		Common.area=areas.get(0).getProvince().getCity().get(0).getArea().get(0);
		mCurrentDistrictName=Common.area.getName();
		mProvinceDatas=mProvinceList.toArray(new String[mProvinceList.size()]);

		// 添加change事件
		mViewProvince.addChangingListener(this);
		// 添加change事件
		mViewCity.addChangingListener(this);
		// 添加change事件
		mViewDistrict.addChangingListener(this);

		//
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(AreaActivity.this, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();



	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	private List<Area>  initJsonData()
	{
		List<Area> areas= null;
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getAssets().open("area.json");
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

	}

	@Override
	protected void onNetWorkReConnected() {

	}

	@Override
	protected void onNetWorkConnectFail() {

	}




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


	}


	private void initView() {


	}












}
