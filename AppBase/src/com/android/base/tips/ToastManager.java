package com.android.base.tips;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ToastManager {
	/**
	 * 显示简单的Toast
	 * @param context
	 * @param content
	 * @param duration
	 */
	public static void showNormalToast(Context context, String content,
			int duration) {
		Toast.makeText(context, content, duration).show();
	}

	/**
	 * 在屏幕中间显示Toast
	 * @param context
	 * @param msg
	 * @param duration
	 */
	public static void showToastInCenterPosition(Context context, String msg,
			int duration) {
		Toast toast = Toast.makeText(context, msg, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	/**
	 * 带图标的Toast
	 * @param context
	 * @param msg
	 * @param duration
	 * @param drawableId
	 */
	public static void showToastWithIcon(Context context, String msg,
			int duration, int drawableId) {
		Toast toast = Toast.makeText(context, msg, duration);
		toast.setGravity(Gravity.CENTER, 0, 0);
		LinearLayout toastView = (LinearLayout) toast.getView();
		ImageView image = new ImageView(context);
		image.setImageResource(drawableId);
		toastView.addView(image, 0);
		toast.show();
	}

	/**
	 * 自定义界面的Toast
	 * @param context
	 * @param duration
	 * @param layoutView
	 */
	public static void showToastWithCustomView(Context context,
			int duration, View layoutView) {
		Toast toast = new Toast(context);
		toast.setGravity(Gravity.CENTER | Gravity.TOP, 0, 0);
		toast.setDuration(duration);
		toast.setView(layoutView);
		toast.show();
	}
}
