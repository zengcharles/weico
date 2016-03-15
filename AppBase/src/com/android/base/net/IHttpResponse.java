package com.android.base.net;

import org.json.JSONObject;

public interface IHttpResponse {
	
	public void onHttpSuccess(int requestCode,JSONObject result);
    
    public void onHttpFailure(int requestCode,int errCode);
}
