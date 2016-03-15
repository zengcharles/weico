package com.charles.weibo.module;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.charles.weibo.common.Constants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public abstract class AbstractActivity extends Activity {
	
	private Oauth2AccessToken mAccessToken ;
	private String aouth = "" ; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences mSettings = getSharedPreferences(Constants.PREFS_NAME, 0);  
		aouth =mSettings.getString("aouth","");  
	}


}
