package com.android.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;


public class DipUtil {

	/**
	 * 描述 将dip转换为pix
	 * 
	 * @param dip
	 * @return
	 */
	public static int convertDipToPix(Context context, int dip) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float desity = dm.density;
		int pix = (int) (desity * dip + 0.5f);
		return pix;
	}

	public static int px2sp(Context context, float pxValue) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		float desity = dm.density;
		return (int) (pxValue / desity + 0.5f);
	}

	/**
	 * 描述 获取view相对于屏幕的Y坐标
	 * 
	 * @param view
	 * @return
	 */
	private static int getViewYpos(View view) {
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		return location[1];
	}

}
