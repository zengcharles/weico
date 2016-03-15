package com.charles.weibo.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

public class DeviceAndAppInfoUtils {

	private static DeviceAndAppInfoUtils utils = null;

	private final static String TAG = "tag";

	// 用来存储设备信息和异常信息
	private Map<String, String> deviceInfos = new HashMap<String, String>();

	public static DeviceAndAppInfoUtils getInstance() {
		if (null == utils) {
			utils = new DeviceAndAppInfoUtils();
		}
		return utils;
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
	/**
	 * SDk版本
	 * @param mContext
	 * @return
	 */
	public String getSDKVersion(Context mContext) {
		return android.os.Build.VERSION.SDK;
	}
	/**
	 * 系统版本号
	 * @param mContext
	 * @return
	 */
	public String getSysVersion(Context mContext) {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 手机型号
	 * @param mContext
	 * @return
	 */
	public String getPhoneStyle(Context mContext) {
		return android.os.Build.MODEL ;
	}

	public String getMACAddress(Context mContext) {
		WifiManager wifiManager = (WifiManager) mContext
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		return wifiInfo.getMacAddress();
	}

	/**
	 * 设备IMEI码即设备号
	 * 
	 * @param mContext
	 * @return
	 */
	public String getIMEICode(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public String getDeviceId(Context mContext) {
		TelephonyManager telephonyManager = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();
	}

	public String formatDeviceMsg(Context ctx) {
		StringBuffer sb = new StringBuffer();
		collectDeviceInfo(ctx);
		for (Map.Entry<String, String> entry : deviceInfos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		return sb.toString();
	}

	/**
	 * 收集设备参数信息
	 */
	private void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null"
						: pi.versionName;
				String versionCode = pi.versionCode + "";
				deviceInfos.put("versionName", versionName);
				deviceInfos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();

		for (Field field : fields) {
			try {
				field.setAccessible(true);
				deviceInfos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}
}
