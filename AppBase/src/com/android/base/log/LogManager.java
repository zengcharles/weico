package com.android.base.log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.Log;

import com.android.base.config.AppConfig;
import com.android.base.info.DeviceInfo;
import com.android.base.utils.SDCardHelper;

public class LogManager {
	
	//按此写日志
	public void writeLogToSdCard(Context context,String fileName,String logMsg){
		SDCardHelper helper =SDCardHelper.getInstance();
		String dateFlg =new  SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date());
		String tempFileName =dateFlg+ fileName+ ".txt";
		String content = DeviceInfo.getInstance().formatDeviceMsg(context) + logMsg;
		try {
			helper.writeToSDCardFile(AppConfig.LOG_PATH , content, tempFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//增量日志为按天一日志的形式（实时SDcard日志）
	public void appendLogToSdCard(Context context,String fileName,String logMsg){
		SDCardHelper helper =SDCardHelper.getInstance();
		//每天一日志
		String dateFlg =new  SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String tempFileName =dateFlg+ fileName+ ".txt";
		String content = DeviceInfo.getInstance().formatDeviceMsg(context) + logMsg;
		try {
			helper.writeToSDCardFileByAppend(AppConfig.LOG_PATH , content, tempFileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	//控制台日志
	public void printLogInfo(String tag,String msg){
		Log.i(tag,"theLogis "+msg);
	}
	
	public void printLogError(String tag,String msg){
		Log.e(tag,msg);
	}

}
