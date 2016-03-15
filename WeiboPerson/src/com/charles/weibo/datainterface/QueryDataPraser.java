package com.charles.weibo.datainterface;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class QueryDataPraser implements HttpQuery {

	HttpUtils utils;
	private Context mContext;
	private String uri;
	private String httpMethod; //post或者get

	public QueryDataPraser(Context mContext, String uri,String httpMethod) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		this.uri = uri;
		this.httpMethod = httpMethod.toUpperCase();
	}

	

	@Override
	public JSONObject query(HashMap<String, Object> map) {
		// TODO Auto-generated method stub		
			try {
				String result = null;
				if(httpMethod.equals("GET")){
					result = requestDataByHttpGet(map);
					
				}else if(httpMethod.equals("POST")){
					result = requestDataByHttpPost(map);
				}
				return new JSONObject(result);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					return new JSONObject(Config.dataErrorMsg);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
				}
			}
		return null;
	}

	@Override
	public void httpCancel() {
		// TODO Auto-generated method stub
		if (null != utils) {
			utils.disconnectRequest();
		}
	}
	


	private String requestDataByHttpPost(HashMap<String, Object> map) {
		utils = new HttpUtils(mContext);
		String result = utils.requestDataByHttpPost(uri, map);

		if (null != result && !"".equalsIgnoreCase(result)) {
			return result;
		} else {
			return Config.dataErrorMsg;
		}
	}
	
	private String requestDataByHttpGet(HashMap<String, Object> map){
		utils = new HttpUtils(mContext);
		String result = utils.requestDataByHttpGet(uri, map);

		if (null != result && !"".equalsIgnoreCase(result)) {
			return result;
		} else {
			return Config.dataErrorMsg;
		}
	}

}
