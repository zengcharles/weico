
package com.charles.weibo.module.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.charles.weibo.utils.StringUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class BaseFragment extends Fragment {
    public View mView;
    
	//微博验证token
	public Oauth2AccessToken mAccessToken ; 
	
	public  LoginUserInfoKeeper mUserKeeper ; 
    /**
     * 当前页
     */
    public int currentPagte = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	mAccessToken = AccessTokenKeeper.readAccessToken(getActivity()); 
    	mUserKeeper = new LoginUserInfoKeeper() ;
    }

    public boolean isNullString(String imgUrl) {

        if (StringUtils.isEmpty(imgUrl)) {
            return true;
        }
        return false;
    }
}
