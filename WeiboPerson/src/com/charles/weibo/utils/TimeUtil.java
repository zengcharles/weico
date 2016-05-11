package com.charles.weibo.utils;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static String getCurDate() {

		SimpleDateFormat sDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		return sDateFormat.format(new java.util.Date());
	}

	public static boolean isNeedToClearImgCache(String strLastClearDate) {

		return dateCompare(strLastClearDate, getCurDate(), 7);
	}

	/**
	 * 比较两个时间相差的天数
	 * 
	 * @param pStrDate1
	 *            日期-"yyyy-MM-dd HH:mm:ss"
	 * @param pStrDate2
	 *            日期-"yyyy-MM-dd HH:mm:ss"
	 * @param expectDay
	 *            期望天数差
	 * @return if(>=天数差)返回true
	 * */
	public static boolean dateCompare(String strDate1, String strDate2,
			int expectDay) {
		try {
			// 设定时间的模板
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			// 得到指定模范的时间
			Date d1 = sdf.parse(strDate1);
			Date d2 = sdf.parse(strDate2);
			// 比较
			if (Math.abs(((d1.getTime() - d2.getTime()) / (24 * 3600 * 1000))) >= 7) {
				// System.out.println("大于7天");
				return true;
			} else {
				// System.out.println("小于7天");
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
