package com.charles.weibo.utils;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.android.base.encrypt.MD5Encrypt;
import com.charles.weibo.Config.Config;

public class MemberUtils{
	
	private final static String TAG = "MemberUtils";
	private Context mContext;
	private HttpAsyncTask asyncTask;
	
	
	JSONObject auth_config = null;
	
	private final int LOGIN_REQUEST_CODE = 100;

	//private final static String os_type = "android";
	private final String SharedPreferencesName = "auth_config";
	private SharedPreferences sharedPreferences = null;
	
	public MemberUtils(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		if(sharedPreferences == null){
			sharedPreferences = mContext.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE);
		}
	}
	
	
	
	//检查是否登录
	public boolean checkLogin(){
		if(!"0".equals(sharedPreferences.getString("uid", "0")) && !"".equals(sharedPreferences.getString("login_key", ""))){
			/*long expire_in = System.currentTimeMillis()/1000; //获取unix时间戳  Charles
			if(expire_in < sharedPreferences.getLong("expires_in", 0)){
				return true;
			}else{
				return false;
			}*/
			return true;
		}
		return false;
	}
	
	public void saveMemberInfo(String userId,String login_time,String login_key,String api_id){
		
		Editor editor = sharedPreferences.edit();
		editor.putString("uid", userId);
		editor.putString("api_id", api_id);
		editor.putString("login_time", login_time);
		editor.putString("login_key", login_key);
		editor.commit();
	}
	
	public void clearMemberInfo(){
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
	
	//取当前用户uid
	public String getMemUid(){
		return sharedPreferences.getString("uid", "0");
	}
	
	public String getApiId(){
		return sharedPreferences.getString("api_id", "0");
	}
	
	//取当前用户auth_token
	public String getLoginkey(){
		return sharedPreferences.getString("login_key", "0");
	}
	
	
	//取当前用户名
	public String getUsername(){
		//return sharedPreferences.getString("username", "请登录"); 
		//临时硬编码
		return "yiping";
	}
	

	/**
	 * 以下方法暂时由APP和商城对接
	 * 未来改成与后台交互
	 */
	public String getSign(){   //Charles
		MD5Encrypt encryptor = new MD5Encrypt();
		long uid = sharedPreferences.getLong("uid", 0);
		String username = sharedPreferences.getString("username", "");
		//临时硬编码
		//String username = "yiping";
		return encryptor.encrypt(String.valueOf(uid)+username);
	}
	
	//跳转登录页面   Charles
	/*public void gotoLogin(){ 
		Intent it = new Intent(mContext,MemberLoginActivity.class);
		((Activity)mContext).startActivity(it);
	}*/
	
	
}
