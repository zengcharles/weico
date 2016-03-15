package com.charles.weibo.utils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

import com.charles.weibo.Config.Config;

public class MobileUtil {

	public static String getRemoteInfo(String mobileCode){
		//指定WebService的命名空间和调用方法名
		SoapObject request = new SoapObject(Config.mobile_nameSpace,Config.mobile_methodName);
		request.addProperty("mobileCode", mobileCode);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut = request;
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);
		HttpTransportSE se = new HttpTransportSE(Config.mobile_endPoint);
		try {
			se.call(Config.mobile_soapAction, envelope);
			if(envelope.getResponse() != null){
				SoapObject object = (SoapObject)envelope.bodyIn;
				String result = object.getProperty(0).toString();
				return cutString(result);
			}else{
				return "查询手机归属地服务不可用";
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("MobileUtil", "代码运行出现异常"+e);
		}
		return null;
		
	}
	
	public static String cutString(String ret){
		
		if(ret.contains("：")||ret.contains(":")){
			return ret.substring(ret.indexOf("：")+1,ret.length()).trim();
		}else{
			return ret;
		}
	}
	
	public static boolean checkValidate(String ret){
		if("".equals(ret.trim()) || ret.length() != 11){
			return false;
		}
		return true;
	}
}
