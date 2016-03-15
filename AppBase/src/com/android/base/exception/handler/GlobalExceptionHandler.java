package com.android.base.exception.handler;

import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.widget.Toast;

import com.android.base.ActivityManagerTools;
import com.android.base.log.LogManager;
import com.android.base.tips.ToastManager;


public class GlobalExceptionHandler implements UncaughtExceptionHandler{

	private Context mContext=null;
	
	private static GlobalExceptionHandler handler =null;
	
	public static GlobalExceptionHandler getInstance(Context context){
		if(handler ==null){
			handler =new GlobalExceptionHandler();
		}
		if(handler.mContext==null){
			handler.mContext=context;
		}
		return handler;
	}
	
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		// TODO Auto-generated method stub
		//指定跳转页 或者 退出程序
//		intent.setClass(mContext,
//				AppConfig.errorProcessActivity);
//		BaseActivity.this.startActivity(intent);
		LogManager manager =new LogManager();
		String name ="crashLog";
		String content =ex.getMessage()+"\n";
		StackTraceElement[] elements =ex.getStackTrace();
		for (int i = 0; i < elements.length; i++) {
			content =content+ elements[i].toString()+"\n";
		}
		manager.writeLogToSdCard(mContext, name,content);
		ToastManager.showToastInCenterPosition(mContext, "系统异常退出", Toast.LENGTH_LONG);
		exitApp();
	}
	
	/**
	 * 退出程序操作
	 */
	public void exitApp() {
		ActivityManagerTools.getInstance().exit();
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

}
