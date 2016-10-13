package com.zhuoyi.fauction.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.yintai.app_common.ui.common.view.CommonSimpleTitleDialog;
import com.yintai.common.util.ObjectUtil;

/**
 * Created by Administrator on 2016/9/14.
 */
public class DialogUtil {
    private static ProgressDialog dialog;

    private static CommonSimpleTitleDialog simpleTitleDialog;

    public DialogUtil() {
    }

    public static void showDialog(Context context, String msg) {
        showDialog(context, msg, true);
    }

    public static void showDialog(Context context, String msg, boolean cancel) {
        dialog = new ProgressDialog(context, com.yintai.app_common.R.style.common_dialog_style);
        dialog.setCanceledOnTouchOutside(cancel);
        dialog.setMessage(msg);
        if(!dialog.isShowing()) {
            dialog.show();
        }

    }

    public static void dismiss() {
        if(ObjectUtil.checkObject(dialog) && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = null;
    }

    public static void commonDialogDismiss(){
        if(ObjectUtil.checkObject(simpleTitleDialog) && simpleTitleDialog.isShowing()) {
            simpleTitleDialog.dismiss();
        }

        simpleTitleDialog = null;
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, String title, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        showCustomerSimpleTitleDialog(context, cancel, title, context.getString(com.yintai.app_common.R.string.submit), context.getString(com.yintai.app_common.R.string.cancle), submitClickListener, negativeClickListener);
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, int gravity, String title, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        showCustomerSimpleTitleDialog(context, cancel, gravity, title, context.getString(com.yintai.app_common.R.string.submit), context.getString(com.yintai.app_common.R.string.cancle), submitClickListener, negativeClickListener);
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, String title, String submitText, String cancelText, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        showCustomerSimpleTitleDialog(context, cancel, 0, title, submitText, cancelText, submitClickListener, negativeClickListener);
    }

    public static void showCustomerSimpleTitleDialog(Context context, boolean cancel, int gravity, String title, String submitText, String cancelText, CommonSimpleTitleDialog.OnSubmitClickListener submitClickListener, CommonSimpleTitleDialog.OnNegativeClickListener negativeClickListener) {
        simpleTitleDialog = new CommonSimpleTitleDialog(context, com.yintai.app_common.R.style.common_dialog_style, gravity);
        simpleTitleDialog.setCanceledOnTouchOutside(cancel);
        simpleTitleDialog.setTitle(title);
        simpleTitleDialog.setSubmitText(submitText);
        simpleTitleDialog.setCancelText(cancelText);
        simpleTitleDialog.setSubmitClickListener(submitClickListener);
        simpleTitleDialog.setNegativeClickListener(negativeClickListener);
        if(!simpleTitleDialog.isShowing()) {
            simpleTitleDialog.show();
        }

    }
}
