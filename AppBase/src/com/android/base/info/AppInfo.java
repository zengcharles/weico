package com.android.base.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class AppInfo {

	private static AppInfo info = null;
	
	public static AppInfo getInstance() {
		if (null == info) {
			info = new AppInfo();
		}
		return info;
	}
	
	/**
	 * APP版本号
	 * @param mContext
	 * @return
	 */
	public String getAppVersionCode(Context mContext) {
		try {
			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
			return String.valueOf(packageInfo.versionCode);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * APP版本名
	 * @param mContext
	 * @return
	 */
	public String getAppVersionName(Context mContext) {
		try {
			PackageManager packageManager = mContext.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(
					mContext.getPackageName(), 0);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
