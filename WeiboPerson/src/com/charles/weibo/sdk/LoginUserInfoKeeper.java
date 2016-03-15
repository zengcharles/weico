package com.charles.weibo.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.charles.weibo.entity.UserModel;



public class LoginUserInfoKeeper {

	   private static final String PREFERENCES_NAME = "login_user_info";
	   
	   public static void writeLoginUserInfo(Context context, UserModel user) {
	        if (null == context || null == user) {
	            return;
	        }
	        try {
	        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	        Editor editor = pref.edit();
	        editor.putString("name", user.getName());
	        editor.putString("profile_image_url", user.getProfile_image_url());
	        editor.putLong("id", user.getId());
	        editor.commit();
	    	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
	    }
	   
	   
	    public static UserModel readUserInfo(Context context) {
	        if (null == context) {
	            return null;
	        }
	        UserModel user = new UserModel();
	        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
	        user.setName(pref.getString("name", ""));
	        user.setId(pref.getLong("id", 0));
	        user.setProfile_image_url(pref.getString("profile_image_url", ""));
	        return user;
	    }
	   
}
