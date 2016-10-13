package com.zhuoyi.fauction.config;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Environment;

public class ConfigParams {

	public final static String TIME_LONG_MILL = "yyyy-MM-dd HH:mm:ss:SSS";//精确到毫秒
	public final static String TIME_LONG_NO_SECOND = "yyyy-MM-dd HH:mm";
    public final static String TIME_LONG = "yyyy-MM-dd HH:mm:ss";
    public final static String TIME_SHORT_HM = "HH:mm";
    public final static String TIME_SHORT_MD = "MM-dd";
    public final static String TIME_DAY = "yyyy-MM-dd";
    public final static String TIME_HMS = "HH:mm:ss";
    public final static String IMG_CACHE_PATH = Environment
	    .getExternalStorageDirectory() + "/.comveedoc";
    
    /** 用来给未处理的随访memberId防止shareprefence 重复key */
    public final static String UNDEAL_FOLLOW_NUMBER = "undealFollowParams";
    public final static String UNDEAL_FOLLOW_POSITION = "undealFollowPosition";
    public final static String MSG_CONTENT_DATE = "msgContentDate";
    public final static String PATIENT_LIST_DATE = "patientListDate";
    public final static String SEND_MESSAGE_ITEM = "sendMsgItem";//后台intent传递的key
    public final static String REFRESH_ASK_FRAGMENT = "refreshAskFrag";//刷新对话界面的广播
    public final static String RELOAD_APP_BORADCAST = "reloadAppBroadcast";//重启app的广播
    public final static String REFRESH_PATIENT_LIST = "refreshPatientList";//刷新患者界面的广播
    
    /**刷新首页的广播 action*/
    public final static String BORADCAST_REFRESH_INDEX = "com.comvee.refresh";
    /**网络请求超时的广播 action*/
    public final static String BROADCAST_NETWORK_TIME_OUT = "com.comvee.timeout";
    
    public final static String DB_NAME = "doctorcomvee.db";
    public final static int DB_VERSION = 20150128;
    public final static boolean ISDEBUG = true;
    public final static String VERSION_TIME = "2015-02-09 11:00:00";

  
}
