package com.charles.weibo.datainterface;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class ExpressDataPraser implements HttpQuery{
	
	 HttpUtils utils;
	 private  Context context;
	 
     public ExpressDataPraser(Context context) {
		super();
		this.context = context;
	}

	public JSONObject queryExpress(HashMap<String,Object> map){
    	String result= requestDataByHttpPost(map);
		try {
			return new JSONObject(result);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return new JSONObject( Config.expressErrorMsg);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
			}
		}
		return null;
     }
     
     private String requestDataByHttpPost( HashMap<String,Object> map){
    	 utils=new HttpUtils(context);
    	 String result = utils.getStringDataByHttp(Config.expressAction, map);
    	 /*
 		 * 此处暂时使用硬编码用于开发
 		 */
//    	 result ="{\"status\":\"ok\",\"errMsg\":\"\", \"expressInfo\":[{\"time\":\"2013-11-19 11:51:59\",\"msg\":\"签收人是：健\"},{\"time\":\"2013-11-19 11:51:06\",\"msg\":\"派件已签收\"},{\"time\":\"2013-11-19 08:14:44\",\"msg\":\"正在派件..(派件人:汤桂财,电话:13924297447)\"},{\"time\":\"2013-11-19 06:28:51\",\"msg\":\"快件在 广州集散中心 ,准备送往下一站 广州 \"},{\"time\":\"2013-11-19 03:31:35\",\"msg\":\"快件在 广州集散中心 ,准备送往下一站 广州集散中心 \"},{\"time\":\"2013-11-19 01:18:50\",\"msg\":\"快件在 深圳集散中心 ,准备送往下一站 广州集散中心 \"},{\"time\":\"2013-11-19 00:19:13\",\"msg\":\"快件在 深圳 ,准备送往下一站 深圳集散中心 \"},{\"time\":\"2013-11-18 22:50:15\",\"msg\":\"已收件\"}]}";
    	 
    	 if(null!=result&&!"".equalsIgnoreCase(result)){
    		 return result;
    	 }else{
    		 return Config.expressErrorMsg;
    	 }
     }
     
     private void expressQueryCancel(){
    	 if(null!=utils){
    		 utils.disconnectRequest();
    	 }
     }

	@Override
	public JSONObject query(HashMap<String,Object> map) {
		// TODO Auto-generated method stub
		return queryExpress(map);
	}

	@Override
	public void httpCancel() {
		// TODO Auto-generated method stub
		expressQueryCancel();
	}
}
