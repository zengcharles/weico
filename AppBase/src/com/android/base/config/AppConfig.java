package com.android.base.config;


import android.app.Application;
import android.os.Environment;

import com.android.base.exception.handler.GlobalExceptionHandler;
import com.android.base.imageCache.ImageCacheUtil;

public class AppConfig extends Application{
	
	 //网络超时设置
	 public static final int REQUEST_TIMEOUT = 60 * 1000;// 设置请求超时10秒钟
	 public static final int SO_TIMEOUT = 60 * 1000; // 设置等待数据超时时间10秒钟
	
	
	 //系统样式参数
     public static boolean isDebug =false;
     public static final boolean isNoActivityTitle =false; 
     public static final boolean isFullScreen =false;
     public static final boolean forceToLogin =true;
     public static final boolean writeRealTimeLogToSDCard =false;
     
     //系統参数
     public static final String appName ="appName";
     public static final String DBName="iAgentCRM.db";
 	 public static final int DBVersion=1;
 	 
 	 public static final String CharSet ="UTF-8";
     
     /** 异步加载图片线程数 */
 	 public static final int taskCount = 5;
 	 public static int BackKeyCount=0;
 	 
 	 public static int backPress=0;
 	 
 	 //文件存储路径
 	 public static final String ROOT_PATH =Environment.getExternalStorageDirectory().getAbsolutePath();
     public static final String IMAGE_CACHE_PATH =ROOT_PATH+"/"+appName+"/imageCache/";
 	 public static final String LOG_PATH = ROOT_PATH+"/"+appName+"/log/";
 	 public static final String DOWNLOAD_FILE_PATH = ROOT_PATH+"/"+appName+"/download/";
    
 	 
 	 //SessionID
 	 public static String sessionID="";
 	 //URL地址
 	 public static String HOST ="";
 	 
 	 
 	 
 	 @Override
 	 public void onCreate() {
 		// TODO Auto-generated method stub
 		super.onCreate();
 		ImageCacheUtil.init(this);
 		if(!isDebug){
 			Thread.currentThread().setUncaughtExceptionHandler(GlobalExceptionHandler.getInstance(getApplicationContext()));
 		}
 	 }
}
