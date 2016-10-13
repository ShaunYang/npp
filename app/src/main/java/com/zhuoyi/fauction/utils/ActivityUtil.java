package com.zhuoyi.fauction.utils;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Toast;

public class ActivityUtil {
	public static final void initBrightness(Activity activity) {
		SharedPreferences SP = PreferenceManager
				.getDefaultSharedPreferences(activity.getApplicationContext());
		float value = SP.getFloat("set_brightness", 0.5F);
		Util.setBrightness(activity, value);
	}



	public static final void setfullScreen(Activity activity) {
		activity.requestWindowFeature(1);
		activity.getWindow().setFlags(1024, 1024);
	}

	public static final void exitSystem(Activity activity) {
		activity.finish();
		Process.killProcess(Process.myPid());
	}

	public static final void toCaramerActivity(Activity cxt, String savePath,
			int requestCode) {
		if (!Util.SDCardExists()) {
			Toast.makeText(cxt, "存储卡不可用，暂无法完成拍照!", Toast.LENGTH_SHORT).show();
			return;
		}

		File vFile = new File(savePath);
		if (!vFile.exists()) {
			File vDirPath = vFile.getParentFile();
			vDirPath.mkdirs();
		}

		Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
		cameraIntent.putExtra("output", Uri.fromFile(vFile));
		cxt.startActivityForResult(cameraIntent, requestCode);
	}

	public static final void toPhotoActivity(Activity cxt, int requestCode) {
		if (!Util.SDCardExists()) {
			Toast.makeText(cxt, "存储卡不可用，暂无法从相册提取相片!", Toast.LENGTH_SHORT).show();
			return;
		}
		Intent galleryIntent = new Intent("android.intent.action.PICK", null);
		galleryIntent.setDataAndType(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		cxt.startActivityForResult(galleryIntent, requestCode);
	}

	public static void startPhotoZoom(Activity cxt, Uri uri, int requestCode,
			int w, int h) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");

		intent.putExtra("crop", "true");

		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		intent.putExtra("outputX", w);
		intent.putExtra("outputY", h);
		intent.putExtra("return-data", true);
		cxt.startActivityForResult(intent, requestCode);
	}

	public static void startCallPhoneActivity(Context cxt, String telNum) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.CALL");
		intent.setData(Uri.parse("tel:" + telNum));
		cxt.startActivity(intent);
	}

	public static void startToMarketAssess(Context cxt, String packageName) {
		try {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.setData(Uri.parse("market://details?id=" + packageName));
			cxt.startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(cxt, "未找到可用的应用市场！", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
}