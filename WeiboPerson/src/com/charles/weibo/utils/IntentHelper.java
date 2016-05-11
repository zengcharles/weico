package com.charles.weibo.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.charles.weibo.wedget.slideingactivity.IntentUtils;

public class IntentHelper {

	public void IntentHelper() {

	}

	/**
	 * 根据类打开citvity
	 */
	public static void openActivity(Context mContext, Class<?> pClass) {
		openActivity(mContext, pClass, null, 0);

	}

	public static void openActivityForResult(Context mContext, Class<?> pClass,
			int requestCode) {
		openActivity(mContext, pClass, null, requestCode);
	}

	/**
	 * 根据类打开citvity,并携带参数
	 */
	public static void openActivity(Context mContext, Class<?> pClass,
			Bundle pBundle, int requestCode) {
		Intent intent = new Intent(mContext, pClass);
		if (pBundle != null) {
			intent.putExtras(pBundle);
		}
		if (requestCode == 0) {
			IntentUtils.startPreviewActivity(mContext, intent, 0);
			// startActivity(intent);
		} else {
			IntentUtils.startPreviewActivity(mContext, intent, requestCode);
			// startActivityForResult(intent, requestCode);
		}
		// actityAnim();
	}

}
