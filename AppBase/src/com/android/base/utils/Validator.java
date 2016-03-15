package com.android.base.utils;

import java.util.regex.Pattern;

import android.content.Context;

public class Validator {
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	public static boolean ifFectureExists(Context context,
			String packageManagerFecture) {
		return context.getPackageManager().hasSystemFeature(
				packageManagerFecture);
	}

}
