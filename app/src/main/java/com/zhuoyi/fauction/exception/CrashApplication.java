package com.zhuoyi.fauction.exception;


import com.zhuoyi.fauction.MobileManagerApplication;


public class CrashApplication extends MobileManagerApplication{
	 	@Override  
	    public void onCreate() {  
	        super.onCreate();  
	        CrashHandler crashHandler = CrashHandler.getInstance();  
	        crashHandler.init(getApplicationContext());  
	    }  
}
