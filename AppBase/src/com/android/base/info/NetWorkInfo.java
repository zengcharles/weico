package com.android.base.info;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetWorkInfo {
	public static int getConnectedType(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isConnected();
			}
		}
		return false;
	}

	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isConnected();
			}
		}
		return false;
	}

	public static boolean isActiveNetworkConnected(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}

	public static boolean isActiveNetworkAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if(mConnectivityManager != null){
				NetworkInfo mNetworkInfo = mConnectivityManager
						.getActiveNetworkInfo();
				if (mNetworkInfo != null) {
					return mNetworkInfo.isAvailable();
				}
			}
		}
		return false;
	}
	
	/**
	 * 检测网络连接,判断手机是否存在连接服务
	 * @param netWorkActivity
	 * @return
	 */
	public static boolean isNetworkAvailableAndConnected(Context context){
		ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if(connectivity == null){
			return false;
		}else{
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if(info !=null){
				for(int i=0;i<info.length;i++){
					if(info[i].getState() == NetworkInfo.State.CONNECTED){
						return true;
					}
				}
			}
		}
		return false;
	}
}
