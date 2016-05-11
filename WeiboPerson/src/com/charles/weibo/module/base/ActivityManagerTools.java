package com.charles.weibo.module.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ActivityManagerTools extends Application {
	// activity 栈
	private List<Activity> activityStack = new ArrayList<Activity>();
	
	public HashMap<String,Integer> activityLaunchMode=new HashMap<String,Integer>();
	
	private static ActivityManagerTools manager;
	
	public static ActivityManagerTools getInstance(){
		if(null==manager){
			manager =new ActivityManagerTools();
		}
		return manager;
	}
	
	public void addActivityToStack(Activity activity){
		int launchMode =getActivityLaunchMode(activity);
		int location =isInTheStack(activity);
		switch (launchMode) {
		case ActivityInfo.LAUNCH_MULTIPLE:
			//standard 模式穿件多实例
			activityStack.add(activity);
			break;
		case ActivityInfo.LAUNCH_SINGLE_INSTANCE:
			//singleInstance 单实例，多栈管理
			if(location!=-1){
				activityStack.remove(location);
			}
			activityStack.add(activity);
			break;
		case ActivityInfo.LAUNCH_SINGLE_TASK:
			//singleTask 单实例 ，单栈管理
			if(location!=-1){
				activityStack.remove(location);
			}
			activityStack.add(activity);
			break;
		case ActivityInfo.LAUNCH_SINGLE_TOP:
			//singleTop 条件性的选择实例数量
			if(location!=-1 && location!= (activityStack.size()-1)){
				activityStack.add(activity);
			}
			if(location==-1){
				activityStack.add(activity);
			}
			break;
		}
	}
	
	/*
	 * 获取activity的启动模式
	 */
	private int getActivityLaunchMode(Activity ctx){
		return activityLaunchMode.get(ctx.getClass().getName().toString());
	}
	
	public void initActivitysLaunchMode(Context ctx){
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			ActivityInfo[] activities=pi.activities;
			for (int i = 0; i < activities.length; i++) {
				activityLaunchMode.put(activities[i].name.toString(), activities[i].launchMode);
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void removeActivityFromStack(Activity activity){
		activityStack.remove(activity);
	}
	
	public void finishAllActivity(){
		for (int i = activityStack.size()-1; i >= 0 ; i--) {
			activityStack.get(i).finish();
			activityStack.remove(i);
		}
	}
	
	public void finishAllActivityExcept(Activity activity){
		for (int i = activityStack.size()-1; i >= 0 ; i--) {
			if(activityStack.get(i).equals(activity)){
				continue;
			}
			activityStack.get(i).finish();
			activityStack.remove(i);
		}
	}
	
	private int isInTheStack(Activity activity){
		int location =-1;
		for (int i = 0; i < activityStack.size(); i++) {
			if(activityStack.get(i).getClass().getSimpleName().equalsIgnoreCase(activity.getClass().getSimpleName())){
				location = i;
				break;
			}
		}
		return location;
	}
	
	public void exit(){
		finishAllActivity();
		if(activityStack.size()>0){
			activityStack.clear();
		}
	}
}

