package com.zhuoyi.fauction.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

public class UITool {

	/**
	 * 
	 * @param v
	 *            填写错误的编辑框
	 * @param msg
	 *            错误提示
	 */
	public static void showEditError(TextView v, String msg) {
		v.requestFocus();
		v.setError(Html.fromHtml("<font color=#B2001F>" + msg + "</font>"));
	}

	public static void showEditError(EditText v, String msg) {
		v.requestFocus();
		v.setError(Html.fromHtml("<font color=#B2001F>" + msg + "</font>"));
	}

	// 关闭键盘
	public static void closeInputMethodManager(Context cxt, IBinder binder) {
		InputMethodManager imm = (InputMethodManager) cxt
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(binder, 0);
	}

	public static void openInputMethodManager(Context cxt, View view) {
		InputMethodManager imm = (InputMethodManager) cxt
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.RESULT_SHOWN);
		imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
	}

	/**
	 * 带清理按钮
	 */
	public static void setEditWithClearButton(final EditText edt,
			final int imgRes) {

		edt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Drawable[] drawables = edt.getCompoundDrawables();
				if (hasFocus && edt.getText().toString().length() > 0) {
					edt.setTag(true);
					// edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgRes,
					// 0);
					edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
							drawables[1], edt.getContext().getResources()
									.getDrawable(imgRes), drawables[3]);

				} else {
					edt.setTag(false);
					edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
							drawables[1], null, drawables[3]);
					// edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
				}
			}
		});
		final int padingRight = Util.dip2px(edt.getContext(), 50);
		edt.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					int curX = (int) event.getX();
					if (curX > v.getWidth() - padingRight
							&& !TextUtils.isEmpty(edt.getText())) {
						if (edt.getTag() != null && (Boolean) edt.getTag()) {
							edt.setText("");
							int cacheInputType = edt.getInputType();
							edt.setInputType(InputType.TYPE_NULL);
							edt.onTouchEvent(event);
							edt.setInputType(cacheInputType);
							return true;
						} else {
							return false;
						}
					}
					break;
				}
				return false;
			}
		});

		edt.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				Drawable[] drawables = edt.getCompoundDrawables();
				if (edt.getText().toString().length() == 0) {
					edt.setTag(false);
					// edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
					edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
							drawables[1], null, drawables[3]);

				} else {
					edt.setTag(true);
					// edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, imgRes,
					// 0);
					edt.setCompoundDrawablesWithIntrinsicBounds(drawables[0],
							drawables[1], edt.getContext().getResources()
									.getDrawable(imgRes), drawables[3]);

				}
			}
		});

	}

	/**
	 * 自动打开键盘
	 * 
	 * @param context
	 * @param view
	 */
	public static void autoOpenInputMethod(final Context context,
			final View view) {
		autoOpenInputMethod(context, view, 500);
	}

	/**
	 * 自动打开键盘
	 * 
	 * @param context
	 * @param view
	 * @param PendingTime
	 */
	public static void autoOpenInputMethod(final Context context,
			final View view, int PendingTime) {
		final InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		view.setFocusable(true);
		view.requestFocus();
		view.setFocusableInTouchMode(true);
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				// imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
				// imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
				// InputMethodManager.HIDE_IMPLICIT_ONLY);
				// 切换键盘
				imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, PendingTime);
	}

	public static int getViewWidth(View view) {
		int measure = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(measure, measure);
		return view.getMeasuredWidth();
	}

	public static int getViewHeight(View view) {
		int measure = View.MeasureSpec.makeMeasureSpec(0,
				View.MeasureSpec.UNSPECIFIED);
		view.measure(measure, measure);
		return view.getMeasuredHeight();
	}

	public static void showLuancherImage(Context cxt, int imgRes, int delayTime) {
		final WindowManager mWindows = (WindowManager) cxt
				.getSystemService(Context.WINDOW_SERVICE);
		WindowManager.LayoutParams wmParams = new WindowManager.LayoutParams();
		wmParams.type = 2002;
		wmParams.flags = 8;
		wmParams.format = PixelFormat.RGBA_8888;
		wmParams.gravity = Gravity.LEFT | Gravity.TOP;
		// wmParams.windowAnimations = android.R.style.Animation_Toast;
		wmParams.width = -1;
		wmParams.height = -1;
		final View layoutLuanch = new View(cxt);
		layoutLuanch.setBackgroundResource(imgRes);
		mWindows.addView(layoutLuanch, wmParams);
		Handler mHandler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				try {
					mWindows.removeView(layoutLuanch);
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.gc();
			};
		};
		mHandler.sendEmptyMessageDelayed(2, 3000);
	}

	public static void setTextView(View root, int resId, String str) {
		((TextView) root.findViewById(resId)).setText(str);
	}

}
