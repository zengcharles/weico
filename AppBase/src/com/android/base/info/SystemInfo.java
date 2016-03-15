package com.android.base.info;

import android.content.Context;

public class SystemInfo {

	private static SystemInfo info = null;

	public static SystemInfo getInstance() {
		if (null == info) {
			info = new SystemInfo();
		}
		return info;
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

}
