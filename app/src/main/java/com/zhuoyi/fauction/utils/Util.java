package com.zhuoyi.fauction.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.yintai.common.util.*;
import com.zhuoyi.fauction.config.ConfigUserManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

public final class Util {
	public static int getScreenHeight(Context c) {
		return c.getResources().getDisplayMetrics().heightPixels;
	}

	public static int getScreenWidth(Context c) {
		return c.getResources().getDisplayMetrics().widthPixels;
	}

	public static boolean getTimeNum(String str) {

		//临时字符串
		String temp;
		String day;
		String hour;
		String minute;
		String second;
		String[] dayArray=str.split("天");

		day=dayArray[0];
//        if(day.equals("-1")){
//            shootTime.setText(str+" 结束");
//            return false;
//        }

		int length = dayArray.length;
		Logger.i("length=", length + "");
		if(length==1){
			//shootTime.setText(str+" 结束");
			return false;
		}
		//去掉天后
		String[] hourArray=dayArray[1].split("小时");
		hour=hourArray[0];


		String[] minuteArray=hourArray[1].split("分");
		minute=minuteArray[0];

		String[] secondArray=minuteArray[1].split("秒");
		second=secondArray[0];


		System.out.println(day);
		System.out.println(hour);
		System.out.println(minute);
		System.out.println(second);

		//
//        mDay=Long.parseLong(day);
//        mHour=Long.parseLong(hour);
//        mMin=Long.parseLong(minute);
//        mSecond=Long.parseLong(second);

		return true;

	}

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

	public static int dipToPx(Context cxt, float dip) {
		return (int) TypedValue.applyDimension(1, dip, cxt.getResources()
				.getDisplayMetrics());
	}

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

	public static int dip2px(Context context, float dipValue) {
		float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5F);
	}

	public static boolean SDCardExists() {
		return Environment.getExternalStorageState().equals("mounted");
	}

	public static Bitmap getBimap(File file) {
		if (!SDCardExists()) {
			return null;
		}
		if (file.exists()) {
			Bitmap img = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (img != null) {
				return img;
			}
		}
		return null;
	}

	public static boolean saveBitmap(Bitmap img, String path, String name)
			throws FileNotFoundException {
		if (!SDCardExists()) {
			return false;
		}

		if (img != null) {
			File f = new File(path);
			if (!f.exists()) {
				f.mkdirs();
			}
			FileOutputStream iStream = new FileOutputStream(
					new File(path, name));
			img.compress(CompressFormat.PNG, 100, iStream);
			return true;
		}
		return false;
	}

	public static void setBrightness(Activity activity, float value) {
		LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = value;
		activity.getWindow().setAttributes(lp);
	}

	public static final void sendShare4Text(Activity activity, String content) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.setType("text/plain");
		intent.putExtra("android.intent.extra.SUBJECT", "分享");
		intent.putExtra("android.intent.extra.TEXT", content);
		intent.setFlags(268435456);
		activity.startActivity(Intent.createChooser(intent, activity.getTitle()));
	}

	public static final void sendShare(Context ctx, String title,
			String content, String filepath) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addFlags(1);
		intent.setFlags(268435456);
		try {
			intent.putExtra("android.intent.extra.STREAM",
					Uri.fromFile(new File(filepath)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent.putExtra("android.intent.extra.TEXT", content);
		intent.putExtra("android.intent.extra.SUBJECT", title);
		intent.setType("image/*");
		ctx.startActivity(Intent.createChooser(intent, "分享"));
	}

	public static final void sendShare(Context ctx, String title,
			String content, Bitmap img) {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addFlags(1);
		intent.setFlags(268435456);
		try {
			intent.putExtra("android.intent.extra.STREAM", img);
		} catch (Exception e) {
			e.printStackTrace();
		}
		intent.putExtra("android.intent.extra.TEXT", content);
		intent.putExtra("android.intent.extra.SUBJECT", title);
		intent.setType("image/*");
		ctx.startActivity(Intent.createChooser(intent, "分享"));
	}

	public static final void finishApp(Activity activity) {
		activity.finish();
	}

	public static final String getImeiCode(Context context){
		TelephonyManager telephonyManager=(TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		String imei=telephonyManager.getDeviceId();

		return imei;
	}

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

	public static void showImage(Context context, String path) {
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			String type = "image/*";
			Uri name = Uri.parse("file://" + path);
			intent.setDataAndType(name, type);
			context.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String createSign(Context context, String timestamp,String kind,String method) {
		System.out.println("preSign="+ConfigUserManager.getUnicodeIEME(context)+timestamp+ConfigUserManager.getSecretKey(context)+kind+method);
		String sign= MD5Util.getMD5String(ConfigUserManager.getUnicodeIEME(context)+timestamp+ConfigUserManager.getSecretKey(context)+kind+method);
				return sign;
	}

	public static boolean deleteDir(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				String[] children = dir.list();
				for (int i = 0; i < children.length; i++) {
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

	public static void shortcut(Context cxt, String label, Bitmap bitmap,
			Intent intent) {
		String EXTRA_SHORTCUT_DUPLICATE = "duplicate";
		Intent it = new Intent();
		it.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		it.putExtra("android.intent.extra.shortcut.NAME", label);
		it.putExtra("android.intent.extra.shortcut.ICON", bitmap);
		it.putExtra("android.intent.extra.shortcut.INTENT", intent);
		it.putExtra("duplicate", true);
		cxt.sendBroadcast(it);
	}

	public static void shortcut(Context cxt, String label, Bitmap bitmap,
			ComponentName cn) {
		Intent it = new Intent();
		it.setComponent(cn);
		shortcut(cxt, label, bitmap, it);
	}

	public static float getTextWidth(String text, float Size) {
		TextPaint FontPaint = new TextPaint();
		FontPaint.setTextSize(Size);
		return FontPaint.measureText(text);
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & 0xF) >= 3;
	}

	public static double getScreenPhysicalSize(Activity ctx) {
		DisplayMetrics dm = new DisplayMetrics();
		ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
		double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2.0D)
				+ Math.pow(dm.heightPixels, 2.0D));
		return diagonalPixels / (160.0F * dm.density);
	}

	public static boolean checkFirst(Context cxt, String key) {
		SharedPreferences sp = cxt.getSharedPreferences(key, 0);
		boolean b = sp.getBoolean("key", true);
		if (b) {
			sp.edit().putBoolean("key", false).commit();
		}
		return b;
	}

	public static void startApkActivity(Context ctx, String packageName) {
		PackageManager pm = ctx.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent("android.intent.action.MAIN", null);
			intent.addCategory("android.intent.category.LAUNCHER");
			intent.setPackage(pi.packageName);

			List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				String className = ri.activityInfo.name;
				intent.setComponent(new ComponentName(packageName, className));
				ctx.startActivity(intent);
			}
		} catch (NameNotFoundException e) {
			Log.e("startActivity", e.toString());
		}
	}

	public static final void goToSetNetwork(Context fromPage) {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		intent.setFlags(268435456);
		fromPage.startActivity(intent);
	}

	public static final void goToSetNetwork(Activity fromPage) {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		fromPage.startActivity(intent);
	}

	public static final InputStream getAssetInput(Context cxt, String fileName)
			throws IOException {
		AssetManager asset = cxt.getAssets();
		return asset.open(fileName);
	}

	public static final void shortcut(Context cxt) {
		String packageName = cxt.getPackageName();
		PackageManager pm = cxt.getPackageManager();
		try {
			PackageInfo pi = pm.getPackageInfo(packageName, 0);
			Intent intent = new Intent("android.intent.action.MAIN", null);
			intent.addCategory("android.intent.category.LAUNCHER");
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
				shortcut(cxt, label, bd.getBitmap(), intent);
			} else {
				Log.e("shortcut", "no fond main activity");
			}
		} catch (Exception e) {
			Log.e("shortcut", e.toString());
		}
	}

	public static final String getMetaData(Context cxt, String key,
			String defaultValue) {
		String value = null;
		try {
		/*	ApplicationInfo appInfo = cxt.getPackageManager()
					.getApplicationInfo(cxt.getPackageName(), 128);*/
			//value = String.valueOf(appInfo.metaData.get(key));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return TextUtils.isEmpty(value) ? defaultValue : value;
	}

	public static final int getStatuBarHight(Context cxt) {
		int height = 0;
		try {
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

	public static <T> T[] toArray(Class<?> cls, ArrayList<T> items) {
		if ((items == null) || (items.size() == 0)) {
			return (T[]) Array.newInstance(cls, 0);
		}
		return items.toArray((T[]) Array.newInstance(cls, items.size()));
	}

	public static boolean isIntentAvailable(Context context, String action) {
		PackageManager packageManager = context.getPackageManager();
		Intent intent = new Intent(action);
	//	List list = packageManager.queryIntentActivities(intent, 65536);
		//list.size() > 0;
		return true;
	}
}