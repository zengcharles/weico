package com.charles.weibo.module;

import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.base.BaseActivity;
import com.charles.weibo.R;
import com.charles.weibo.common.Constants;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class LoginActivity extends BaseActivity{


	private AuthInfo mAuthInfo;
	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
	private Oauth2AccessToken mAccessToken;
	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
	private SsoHandler mSsoHandler;

	private Button btnLogin;
	private TextView txtAouth ;
	
	// 記錄登錄 用戶信息
	private LoginUserInfoKeeper mUserKeeper;
	private UserModel user; 
	
	

	/**
	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
	 * SharedPreferences 中。
	 */
	class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {
			// 从 Bundle 中解析 Token
			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
			if (mAccessToken.isSessionValid()) {
				// 显示 Token
				updateTokenView(false);
				
				// 保存 Token 到 SharedPreferences
				AccessTokenKeeper.writeAccessToken(LoginActivity.this,
						mAccessToken);
				Toast.makeText(LoginActivity.this,
						R.string.weibosdk_demo_toast_auth_success,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent() ; 
				intent.setClass(LoginActivity.this, MainActivity.class);
		        startActivity(intent);
		        finish();
		        overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
			}else {
				// 以下几种情况，您会收到 Code：
				// 1. 当您未在平台上注册的应用程序的包名与签名时；
				// 2. 当您注册的应用程序包名与签名不正确时；
				// 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
				String code = values.getString("code");
				String message = getString(R.string.weibosdk_demo_toast_auth_failed);
				if (!TextUtils.isEmpty(code)) {
					message = message + "\nObtained the code: " + code;
				}
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG)
						.show();
			}
		}

		@Override
		public void onCancel() {
			Toast.makeText(LoginActivity.this,
					R.string.weibosdk_demo_toast_auth_canceled,
					Toast.LENGTH_LONG).show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(LoginActivity.this,
					"Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
					.show();
		}
	}

	/**
	 * 显示当前 Token 信息。
	 * 
	 * @param hasExisted
	 *            配置文件中是否已存在 token 信息并且合法
	 */
	private void updateTokenView(boolean hasExisted) {
		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new java.util.Date(mAccessToken.getExpiresTime()));
		String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
		txtAouth.setText(String.format(format, mAccessToken.getToken(),
		 date));

		String message = String.format(format, mAccessToken.getToken(), date);
		if (hasExisted) {
			message = getString(R.string.weibosdk_demo_token_has_existed)
					+ "\n" + message;
		}
		txtAouth.setText(message);
		
		SharedPreferences mSettings = getSharedPreferences(Constants.PREFS_NAME, 0);  
		//先取得编辑器，通过编辑器对象存放数据  
		Editor editor = mSettings.edit();  
		editor.putString("access_token",mAccessToken.getToken());  
		//这一步骤之后，数据存在于内存中，并没有被提交到文件中，要记得下一步！！  
		editor.commit();  
		
	}
	
    /**
     * 当 SSO 授权 Activity 退出时，该函数被调用。
     * 
     * @see {@link Activity#onActivityResult}
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
           
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_login;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		btnLogin = (Button) findViewById(R.id.btnLogin);
		txtAouth = (TextView)findViewById(R.id.txtOauth);
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		// 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
				mAuthInfo = new AuthInfo(this, Constants.APP_KEY, 
						Constants.REDIRECT_URL, Constants.SCOPE);
				mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);
				mAccessToken = AccessTokenKeeper.readAccessToken(this);
				
				if (mAccessToken.isSessionValid()) {
					updateTokenView(true);
					Intent intent = new Intent() ; 
					intent.setClass(LoginActivity.this, MainActivity.class);
			        startActivity(intent);
			        finish();
				}else{
					mSsoHandler.authorizeWeb(new AuthListener());
					finish();
				}
				
				btnLogin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//mSsoHandler.authorizeClientSso(new AuthListener());
						mSsoHandler.authorizeWeb(new AuthListener());
					}
				});

		
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initAppParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View setLayoutView() {
		// TODO Auto-generated method stub
		return null;
	}
}
