package com.zhuoyi.fauction;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;


import com.lidroid.xutils.exception.DbException;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.tencent.bugly.crashreport.CrashReport;
import com.yintai.DatabaseManager;
import com.yintai.UserManager;
import com.yintai.common.util.Logger;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by chendeji on 26/12/15.
 */
public class MobileManagerApplication extends Application {

    private static Context context;

    public static boolean isDebug;

    public static final String TAG = "MobileManagerApplication";

    public static Context getInstance(){
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        isDebug = BuildConfig.DEBUG;
        //TODO 初始化用户信息
        try {
            DatabaseManager.getInstance().initialization(this, "npp");

        } catch (DbException e) {
            e.printStackTrace();
        }
        //腾讯bugly初始化
        CrashReport.initCrashReport(getApplicationContext(),"900057684",true);
        //CrashReport.testJavaCrash();
      /*  UserManager.getInstance().init(DatabaseManager.getInstance().getDb(), new UserManager.OnLoginListener() {
            @Override
            public void loginSuccess() {
                Logger.i(TAG, "loginSuccess");
            }

            @Override
            public void loginFail() {
                Logger.i(TAG, "loginFail");
            }
//        });*/
//        Configuration config = new Configuration.Builder()
//                .chunkSize(256 * 1024)  //分片上传时，每片的大小。 默认 256K
//                .putThreshhold(512 * 1024)  // 启用分片上传阀值。默认 512K
//                .connectTimeout(10) // 链接超时。默认 10秒
//                .responseTimeout(60) // 服务器响应超时。默认 60秒
//                .recorder(recorder)  // recorder 分片上传时，已上传片记录器。默认 null
//                .recorder(recorder, keyGen)  // keyGen 分片上传时，生成标识符，用于片记录器区分是那个文件的上传记录
//                .zone(Zone.zone0) // 设置区域，指定不同区域的上传域名、备用域名、备用IP。默认 Zone.zone0
//                .build();




    }

    private List<Activity> list = new ArrayList<Activity>();




    public void addActivity(Activity activity) {
        list.add(activity);
    }

    public void exit(Context context) {
        for (Activity activity : list) {
            activity.finish();
        }
        System.exit(0);
    }




    @Override
    public void onTerminate() {

        super.onTerminate();

    }
}
