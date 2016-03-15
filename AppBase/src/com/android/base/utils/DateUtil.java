package com.android.base.utils;

public class DateUtil {

	/*
	 * 计算星期几
	 */
	public static String getWeekDayString(int year, int monthOfYear, int dayOfMonth) {
		int y = year - 1;
		int m = monthOfYear;
		int c = 20;
		int d = dayOfMonth + 12;
		int w = (y + (y / 4) + (c / 4) - 2 * c + (26 * (m + 1) / 10) + d - 1) % 7;
		String weekString = null;
		switch (w) {
		case 0:
			weekString = "日";
			break;
		case 1:
			weekString = "一";
			break;
		case 2:
			weekString = "二";
			break;
		case 3:
			weekString = "三";
			break;
		case 4:
			weekString = "四";
			break;
		case 5:
			weekString = "五";
			break;
		case 6:
			weekString = "六";
			break;
		default:
			break;
		}
		return weekString;
	}
}
