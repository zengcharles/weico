package com.charles.weibo.datainterface;

import org.json.JSONObject;

public interface CallHttpResponse {
	
    public void onHttpSuccess(int requestCode,JSONObject result);
    
    public void onHttpError(int requestCode,int errCode);
}
