package com.zhuoyi.fauction.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;


import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFormatException;
import android.provider.MediaStore;
import android.text.ClipboardManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhuoyi.fauction.R;
import com.zhuoyi.fauction.config.ConfigParams;
import com.zhuoyi.fauction.model.Area;
import com.zhuoyi.fauction.ui.PrefaceActivity;


/**
 * 通用工具类
 * */
public class CommonUtil {
	public static Resources getRes = PrefaceActivity.staticAcitivity
			.getResources();// 获取资源

	/**
	 * 根据年 月 获取对应的月份 天数
	 * */
	public static int getDaysByYearMonth(int year, int month) {

		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	/**
	 * 从assert文件夹中读取省市区的json文件，然后转化为json对象
	 */
	public static List<Area>  initJsonData()
	{
		List<Area> areas= null;
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = getRes.getAssets().open("area.json");
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

	public static boolean isMobileNO(String mobiles) {

		// Pattern p = Pattern
		// .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		//
		// Matcher m = p.matcher(mobiles);
		//
		// return m.matches();
		return isElevenNum(mobiles);// 直接调用判断11位数字的方法
	}

	/**
	 * 判断输入的是11位数字即可
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isElevenNum(String num) {
		Pattern p = Pattern.compile("^\\d{11}$");
		Matcher m = p.matcher(num);

		return m.matches();
	}

	public static void setListViewHeightBasedOnChildren(ListView listView,
			Context cxt, float itemHeight) {

		// 获取listview的适配器
		ListAdapter listAdapter = listView.getAdapter();

		if (listAdapter == null) {
			return;
		}

		float totalHeight = 0;

		for (int i = 0; i < listAdapter.getCount(); i++) {
			totalHeight += TypedValue.applyDimension(1, itemHeight, cxt
					.getResources().getDisplayMetrics())
					+ listView.getDividerHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = (int) totalHeight;

		listView.setLayoutParams(params);
	}

	//
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView != null) {
			ListAdapter listAdapter = listView.getAdapter();

			if (listAdapter == null) {
				return;
			}

			int totalHeight = 0;

			for (int i = 0; i < listAdapter.getCount(); i++) {
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0);
				totalHeight += listItem.getMeasuredHeight();
			}

			ViewGroup.LayoutParams params = listView.getLayoutParams();

			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));

			listView.setLayoutParams(params);
		}
	}

	/**
	 * String转Calendar
	 * */
	public static Calendar stringToCalendar(String time, String fomate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fomate);

		Date date = (Date) sdf.parse(time);

		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);

		return calendar;
	}

	/**
	 * 获取照片路径
	 * */
	public static String getAbsoluteImageFromUri(Context context, Uri uri)
			throws Exception {
		String[] proj = { MediaStore.Images.Media.DATA };
		if (context.getContentResolver() != null) {
			Cursor cusor = context.getContentResolver().query(uri, proj, null,
					null, null);
			if (cusor != null) {
				int columnIndex = cusor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cusor.moveToFirst();
				return cusor.getString(columnIndex);
			}
		}
		return uri.getPath();
	}

	/**
	 * 检测Edit数据
	 * */
	public static boolean checkEdit(EditText edt) {
		final String str = edt.getText().toString();
		if (TextUtils.isEmpty(str)) {
			UITool.showEditError(edt, "不能为空！");
			return false;
		}
		return true;
	}

	/**
	 * 获取屏幕高度
	 * */
	public static int getScreenHeight(Context c) {
		return c.getResources().getDisplayMetrics().heightPixels;
	}

	/**
	 * 获取屏幕宽度
	 * */
	public static int getScreenWidth(Context c) {
		return c.getResources().getDisplayMetrics().widthPixels;
	}

	/**
	 * ① dip转px
	 * */
	public static int Dip2Px(Context c, int dipValue) {
		float scale = c.getResources().getDisplayMetrics().density;
		if (scale == 0.0F)
			System.out.println("[scale] : 0");
		System.out.println("[scale] :  " + scale);
		System.out.println("[0.5f] :  0.5");
		System.out.println("[dipValue * scale + 0.5f] :  " + dipValue * scale
				+ 0.5F);
		return (int) (dipValue * scale + 0.5F);
	}

	/**
	 * ② dip转px
	 * */
	public static int dipToPx(Context cxt, float dip) {
		return (int) TypedValue.applyDimension(1, dip, cxt.getResources()
				.getDisplayMetrics());
	}

	/**
	 * XML InputStream 解析成 Element SAX解析
	 * */
	public static Element parseXML(InputStream inStream)
			throws XmlPullParserException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		Element root = null;
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document dom = builder.parse(inStream);
			root = dom.getDocumentElement();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		return root;
	}

	/**
	 * 设置亮度
	 * */
	public static void setBrightness(Activity activity, float value) {
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = value;
		activity.getWindow().setAttributes(lp);
	}

	/**
	 * 实现文字分享功能
	 * */
	public static final void sendShare4Text(Activity activity, String content) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
	}

	/**
	 * 实现照片分享功能
	 * */
	public static final void sendShare(Context ctx, String title,
			String content, String filepath) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(1);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.fromFile(new File(filepath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.setType("image/*");
		ctx.startActivity(Intent.createChooser(intent, "分享"));
	}

	/**
	 * 实现照片分享功能
	 * */
	public static final void sendShare(Context ctx, String title,
			String content, Bitmap img) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.addFlags(1);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			intent.putExtra(Intent.EXTRA_STREAM, img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent.putExtra(Intent.EXTRA_TEXT, content);
		intent.putExtra(Intent.EXTRA_SUBJECT, title);
		intent.setType("image/*");
		ctx.startActivity(Intent.createChooser(intent, "分享"));
	}

	/**
	 * 获取APP版本名称
	 * */
	public static String getAppVersionName(Context context, String packageName) {
		String versionName = null;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			versionName = pi.versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return versionName;
	}

	/**
	 * 获取APP版本号
	 * */
	@SuppressWarnings("finally")
	public static int getAppVersionCode(Context context, String packageName) {
		int versionCode = 0;
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return versionCode;
		}
	}

	/**
	 * 获取APP版本号
	 * */
	@SuppressWarnings("finally")
	public static int getAppVersionCode(PackageInfo pi) {
		int versionCode = 0;
		try {
			versionCode = pi.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return versionCode;
		}
	}

	/**
	 * 向用户展示图片数据
	 * 
	 * @param path
	 *            图片路径
	 * */
	public static void showImage(Context context, String path) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			String type = "image/*";
			Uri name = Uri.parse("file://" + path);
			intent.setDataAndType(name, type);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除路径（文件夹或文件）
	 * 
	 * @param dir
	 *            路径
	 * */
	public static boolean deleteDir(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; ++i) {
					boolean success = deleteDir(new File(dir, children[i]));
					if (!success) {
						return false;
					}
				}
			}
			return dir.delete();
		}
		return false;
	}

	/**
	 * 添加到Shortcut(快捷方式)选项中
	 * */
	public static void addShortcut(Context cxt, String label, Bitmap bitmap,
			Intent intent) {
		String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
		Intent it = new Intent();
		it.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		it.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
		it.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
		it.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		// 不允许重复创建
		it.putExtra(EXTRA_SHORTCUT_DUPLICATE, true);
		cxt.sendBroadcast(it);
	}

	/**
	 * 添加到Shortcut(快捷方式)选项中
	 * 
	 * 同时需要在manifest中设置以下权限： <uses-permission
	 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
	 * */
	public static void addShortcut(Context cxt, String label, Bitmap bitmap,
			ComponentName cn) {
		Intent it = new Intent();
		it.setComponent(cn);
		addShortcut(cxt, label, bitmap, it);
	}

	/**
	 * 添加到Shortcut(快捷方式)选项中
	 * 
	 * 同时需要在manifest中设置以下权限： <uses-permission
	 * android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
	 * */
	public static final void addShortcut(Context cxt) {
		String packageName = cxt.getPackageName();
		PackageManager pm = cxt.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				String className = ri.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));

				BitmapDrawable bd = (BitmapDrawable) cxt.getPackageManager()
						.getApplicationIcon(packageName);
				String label = cxt.getPackageManager()
						.getApplicationLabel(cxt.getApplicationInfo())
						.toString();
				addShortcut(cxt, label, bd.getBitmap(), intent);
			} else {
				Log.e("shortcut", "no fond main activity");
			}
		} catch (Exception e) {
			Log.e("shortcut", e.toString());
		}
	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * 同时需要在manifest中设置以下权限： <uses-permission
	 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
	 */
	public static void delShortcut(Context cxt, String label, Intent intent) {
		Intent it = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
		// 快捷方式的名称
		it.putExtra(Intent.EXTRA_SHORTCUT_NAME, label);
		it.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		cxt.sendBroadcast(it);
	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * 同时需要在manifest中设置以下权限： <uses-permission
	 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
	 */
	public static void delShortcut(Context cxt, String label, ComponentName cn) {
		Intent it = new Intent();
		it.setComponent(cn);
		delShortcut(cxt, label, it);
	}

	/**
	 * 删除程序的快捷方式
	 * 
	 * 同时需要在manifest中设置以下权限： <uses-permission
	 * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
	 */
	public static final void delShortcut(Context cxt) {
		String packageName = cxt.getPackageName();
		PackageManager pm = cxt.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				String className = ri.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));

				String label = cxt.getPackageManager()
						.getApplicationLabel(cxt.getApplicationInfo())
						.toString();
				delShortcut(cxt, label, intent);
			} else {
				Log.e("shortcut", "no fond main activity");
			}
		} catch (Exception e) {
			Log.e("shortcut", e.toString());
		}
	}

	/**
	 * 获取字符串显示的宽度值
	 * */
	public static float getTextWidth(String text, float Size) {
		TextPaint FontPaint = new TextPaint();
		FontPaint.setTextSize(Size);// 设置字体大小
		return FontPaint.measureText(text);
	}

	/**
	 * 判断是否是平板（官方用法）
	 * */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & 0xF) >= 3;
	}

	/**
	 * 精确获取屏幕尺寸（例如：3.5、4.0、5.0寸屏幕）
	 * */
	public static double getScreenPhysicalSize(Activity ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2.0D)
				+ Math.pow(dm.heightPixels, 2.0D));
		return diagonalPixels / (160.0F * dm.density);
	}

	/**
	 * 判断是否为第一次安装
	 * */
	public static boolean checkFirst(Context cxt, String key) {
		SharedPreferences sp = cxt.getSharedPreferences(key, 0);
		boolean b = sp.getBoolean("key", true);
		if (b) {
			sp.edit().putBoolean("key", false).commit();
		}
		return b;
	}

	/**
	 * 开启应用（APK）
	 * */
	public static void startApkActivity(Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent(Intent.ACTION_MAIN, null);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);// Activity 应该被显示在顶级的
			// launcher 中。
			intent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				String className = ri.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));
				ctx.startActivity(intent);
			}
		} catch (PackageManager.NameNotFoundException e) {
			Log.e("startActivity", e.toString());
		}
	}

	/**
	 * 跳转无线设置的界面
	 * */
	public static final void goToSetNetwork(Context fromPage) {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		fromPage.startActivity(intent);
	}

	/**
	 * 跳转无线设置的界面
	 * */
	public static final void goToSetNetwork(Activity fromPage) {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		fromPage.startActivity(intent);
	}

	/**
	 * 读取Asset目录下的文件
	 * */
	public static final InputStream getAssetInput(Context cxt, String fileName)
			throws IOException {
		AssetManager asset = cxt.getAssets();
		return asset.open(fileName);
	}

	/**
	 * 获取AndroidManifest.xml中<meta-data>标签中定义的值
	 * */
	/*public static final String getMetaData(Context cxt, String key,
			String defaultValue) {
		String value = null;
		try {
			ApplicationInfo appInfo = cxt.getPackageManager()
					.getApplicationInfo(cxt.getPackageName(), 128);
			value = String.valueOf(appInfo.metaData.get(key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (TextUtils.isEmpty(value)) ? defaultValue : value;
	}*/

	/**
	 * 获取状态栏(StatusBar)高度
	 * */
	public static final int getStatuBarHight(Context cxt) {
		int height = 0;
		try {
			@SuppressWarnings("rawtypes")
			Class c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			height = cxt.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height;
	}

	/**
	 * 检测intent是否存在.
	 * */
	public static boolean isIntentAvailable(Context context, String action) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(action);
		@SuppressWarnings("rawtypes")
		List list = packageManager.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * 获取时间
	 * 
	 * @param utd
	 *            System.currentTimeMillis()
	 * @param fomate
	 *            时间格式
	 * */
	public static final String fomateTime(long utd, String fomate) {
		SimpleDateFormat sdf = new SimpleDateFormat(fomate);
		return sdf.format(new Date(utd));
	}

	/**
	 * 判断网络状态
	 * */
	/*public static boolean isNetWorkStatus(Context context) {
		ConnectivityManager cwjManager = (ConnectivityManager) context
				.getSystemService("connectivity");
		NetworkInfo netWrokInfo = cwjManager.getActiveNetworkInfo();

		return (netWrokInfo != null) && (netWrokInfo.isAvailable());
	}*/

	/**
	 * 判断网络状态
	 * */
	/*public static boolean isMobileNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService("");
		NetworkInfo netWrokInfo = manager.getActiveNetworkInfo();

		return (netWrokInfo != null) && (netWrokInfo.getType() == 0);
	}*/

	/**
	 * 返回当前的系统时间，按照精确到毫秒后三位的格式返回
	 * 
	 * @return
	 */
	public static String getStringDateByMillionsNow() {
		return getStringDateByMillions(System.currentTimeMillis(),
				ConfigParams.TIME_LONG);
	}

	/**
	 * 根据毫秒数返回指定格式字符串的时间
	 * 
	 * @param mill
	 * @param formatStr
	 *            指定的格式
	 * @return
	 */
	public static String getStringDateByMillions(long mill, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		Date date = new Date(mill);
		return sdf.format(date);
	}

	/**
	 * 
	 * 传入原始字符串以及需要返回的指定格式化后的字符串
	 * 
	 * @param originDateStr
	 *            传入的原始日期字符串
	 * @param originFormatStr
	 *            对传入的原始日期字符串进行对应的格式化
	 * @param fromatStr
	 *            需要返回的字符串的格式化的日期格式
	 * @return 返回根据fromatstr格式化后的日期字符串
	 */
	public static String getStringDateFormat(String originDateStr,
			String originFormatStr, String fromatStr) {
		SimpleDateFormat sdf;
		Date date = null;
		SimpleDateFormat sdf2 = null;
		try {

			sdf = new SimpleDateFormat(originFormatStr);
			date = sdf.parse(originDateStr);
			sdf2 = new SimpleDateFormat(fromatStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf2.format(date);
	}

	/**
	 * 返回默认的时间格式 yyyy-MM-dd HH:mm
	 * 
	 * @param originDateStr
	 * @param isShowDay
	 *            是否是显示 昨天，前天
	 * @param isSameYear
	 *            是否是同一年
	 * @return
	 */
	public static String getStringDateFormatDefault(String originDateStr,
			boolean isShowDay, boolean isSameYear) {
		String timeStr = null;
		try {
			if (isSameYear) {
				if (isShowDay) {
					timeStr = getStringDateFormat(originDateStr,
							ConfigParams.TIME_LONG, ConfigParams.TIME_SHORT_HM);
				} else {
					timeStr = getStringDateFormat(originDateStr,
							ConfigParams.TIME_LONG, ConfigParams.TIME_SHORT_MD);
				}
			} else {
				timeStr = getStringDateFormat(originDateStr,
						ConfigParams.TIME_LONG,
						ConfigParams.TIME_LONG_NO_SECOND);
			}
		} catch (ParcelFormatException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return timeStr;
	}

	/**
	 * 根据传入的日期字符串进行判断是否早于今天
	 * 
	 * @param theDay
	 *            传入的 "yyyy-MM-dd"格式的字符串
	 * @return true 早于今天
	 */
	public static boolean isTheDayBeforeToday(String theDay) {
		Date nowDate = new Date();
		String formatDay = getStringDateFormat(theDay,
				ConfigParams.TIME_LONG_NO_SECOND, ConfigParams.TIME_DAY);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		String nowDateStr = sdf2.format(nowDate);
		Date theDayDate = null;
		try {
			theDayDate = sdf2.parse(formatDay);
			nowDate = sdf2.parse(nowDateStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return theDayDate.before(nowDate);
	}

	/**
	 * 根据传入的anim资源显示动画
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Animation AnimationIconRotate(Context context, int resId) {
		Animation anim = AnimationUtils.loadAnimation(context, resId);
		LinearInterpolator linearIp = new LinearInterpolator();
		anim.setInterpolator(linearIp);
		anim.setFillAfter(true);
		return anim;
	}

	/**
	 * 复制选中的
	 * 
	 * @param context
	 * @param fromStr
	 */
	@SuppressWarnings("deprecation")
	public static void TextCopy(Context context, String fromStr) {
		if (Build.VERSION.SDK_INT < 11) {
			ClipboardManager clip = (ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clip.setText(fromStr);
		} else {
			android.content.ClipboardManager clip2 = (android.content.ClipboardManager) context
					.getSystemService(Context.CLIPBOARD_SERVICE);
			clip2.setPrimaryClip(ClipData.newPlainText(null, fromStr));
			if (clip2.hasPrimaryClip()) {
				clip2.getPrimaryClip().getItemAt(0).getText();
			}
		}
	}

	/**
	 * 清除内存
	 */
	public static void clearAllUserData() {
		execRuntimeProcess("pm clear " + "com.comveedoctor");
		// android.os.Process.killProcess(ActivityMain.staticAcitivity.get)
	}

	public static Process execRuntimeProcess(String commond) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec(commond);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}

	/**
	 * 返回指定的日期格式
	 * 
	 * @param orginTime
	 * @return
	 */
	public static String getTimeFormatText(String orginTime) {
		long day = 24 * 60 * 60 * 1000;
		long year = day * 365;
		if (orginTime != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(ConfigParams.TIME_DAY);
			Date d;
			long diff;
			long r = 0;
			long y = 0;
			try {
				d = sdf.parse(orginTime);
				diff = new Date().getTime() - d.getTime();
				r = (diff / day);
				y = (diff / year);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// 同一年
			if (y == 0) {
				if (r == 2) {
					return getRes.getString(R.string.ask_time_before_yesterday)
							+ getStringDateFormatDefault(orginTime, true, true);
				} else if (r == 1) {
					return getRes.getString(R.string.ask_time_yesterday)
							+ getStringDateFormatDefault(orginTime, true, true);
				} else if (r == 0) {
					return getStringDateFormatDefault(orginTime, true, true);
				} else {
					return getStringDateFormatDefault(orginTime, false, true);
				}
			} else {
				return getStringDateFormatDefault(orginTime, false, false);
			}
		} else {
			return null;
		}
	}
}
