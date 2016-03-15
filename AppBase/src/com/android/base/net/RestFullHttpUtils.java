package com.android.base.net;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.android.base.config.AppConfig;
import com.android.base.info.AppInfo;
import com.android.base.info.DeviceInfo;
import com.android.base.info.SystemInfo;

public class RestFullHttpUtils {
	
	private final int RETURNTYEP_STRING =0;
	private final int RETURNTYEP_BITMAP =1;
	private final int RETURNTYEP_INPUTSTREAM =2;
	
	private final int HTTPMETHOD_GET =0;
	private final int HTTPMETHOD_POST =1;
	private final int HTTPMETHOD_PUT =2;
	private final int HTTPMETHOD_DELETE =3;
	
	private Context context;
	
	private HttpClient client =null;
	private HttpGet get =null;
	private HttpPost post =null;
	private HttpPut put =null;
	private HttpDelete delete =null;
	private HttpRequestBase request=null;
	public RestFullHttpUtils(Context context) {
		super();
		this.context = context;
	}
	
	private HttpParams generateRequestParams(boolean isAddDefaultParams,HashMap<String,Object> params){
		
		HttpParams parameters =new BasicHttpParams();
		try {
			/**
			 * 增加固定记录参数
			 */
			if(isAddDefaultParams){
				parameters.setParameter("appClientType","Android");
				parameters.setParameter("deviceType",URLEncoder.encode(DeviceInfo.getInstance().getPhoneStyle(context),HTTP.UTF_8));
				parameters.setParameter("deviceId",URLEncoder.encode(DeviceInfo.getInstance().getDeviceId(context),HTTP.UTF_8));
				parameters.setParameter("appVersionCode",URLEncoder.encode(AppInfo.getInstance().getAppVersionCode(context),HTTP.UTF_8));
				parameters.setParameter("appVersionName",URLEncoder.encode(AppInfo.getInstance().getAppVersionName(context),HTTP.UTF_8));
				parameters.setParameter("sdkVersion",URLEncoder.encode(SystemInfo.getInstance().getSDKVersion(context),HTTP.UTF_8));
				parameters.setParameter("sysVersion",URLEncoder.encode(SystemInfo.getInstance().getSysVersion(context),HTTP.UTF_8));
				parameters.setParameter("userName","");
			}
			/**
			 * 增加其他参数
			 */
			if(null!=params){
				Set<String> key = params.keySet();
				Iterator<String> iter = key.iterator();
				while (iter.hasNext()) {
					String mapkey = String.valueOf(iter.next());
					parameters.setParameter(mapkey,URLEncoder.encode(String.valueOf(params.get(mapkey)),HTTP.UTF_8));
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return parameters;
	}
	
	private BasicHttpParams setBasicHttpParams() {
		// 设置连接超时时间
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				AppConfig.REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
		return httpParams;
	}
	
	private Object handlerResponse(HttpResponse response,int returnType) {
		try {
			if (response.getStatusLine().getStatusCode() == 200) {
				switch (returnType) {
				case RETURNTYEP_STRING:
					return EntityUtils.toString(response.getEntity(), "utf-8");
				case RETURNTYEP_BITMAP:
					if(null!=response.getEntity()&&null!=response.getEntity().getContent()){
						return BitmapFactory.decodeStream(response.getEntity().getContent());
					}else{
						return null;
					}
				case RETURNTYEP_INPUTSTREAM:
					if(null!=response.getEntity()&&null!=response.getEntity().getContent()){
						return response.getEntity().getContent();
					}else{
						return null;
					}
				}
			}else{
				return null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object requestByHttpGet(boolean isAddDefaultParams,String absoluteUrl,HashMap<String,Object> params,int returnType){
		get =new HttpGet(absoluteUrl);
		HttpParams parameters = generateRequestParams(isAddDefaultParams, params);
		get.setParams(parameters);
		// 生成HttpClient对象
		client = new DefaultHttpClient(setBasicHttpParams());
		try {
			HttpResponse response = client.execute(get);
			return handlerResponse(response, returnType);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public Object requestByHttpPost(boolean isAddDefaultParams,String absoluteUrl,HashMap<String,Object> params,int returnType){
		post =new HttpPost(absoluteUrl);
		HttpParams parameters = generateRequestParams(isAddDefaultParams, params);
		post.setParams(parameters);
		// 生成HttpClient对象
		client = new DefaultHttpClient(setBasicHttpParams());
		try {
			HttpResponse response = client.execute(post);
			return handlerResponse(response, returnType);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Object requestByHttpPut(boolean isAddDefaultParams,String absoluteUrl,HashMap<String,Object> params,int returnType){
		put =new HttpPut(absoluteUrl);
		HttpParams parameters = generateRequestParams(isAddDefaultParams, params);
		put.setParams(parameters);
		// 生成HttpClient对象
		client = new DefaultHttpClient(setBasicHttpParams());
		try {
			HttpResponse response = client.execute(put);
			return handlerResponse(response, returnType);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object requestByHttpDelete(boolean isAddDefaultParams,String absoluteUrl,HashMap<String,Object> params,int returnType){
		delete =new HttpDelete(absoluteUrl);
		HttpParams parameters = generateRequestParams(isAddDefaultParams, params);
		delete.setParams(parameters);
		// 生成HttpClient对象
		client = new DefaultHttpClient(setBasicHttpParams());
		try {
			HttpResponse response = client.execute(delete);
			return handlerResponse(response, returnType);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Object requestByHttpRequest(int httpMethod,boolean isAddDefaultParams,String absoluteUrl,HashMap<String,Object> params,int returnType){
		switch (httpMethod) {
		case HTTPMETHOD_GET:
			request =new HttpGet(absoluteUrl);
			break;
		case HTTPMETHOD_POST:
			request =new HttpPost(absoluteUrl);
			break;
		case HTTPMETHOD_PUT:
			request =new HttpPut(absoluteUrl);
			break;
		case HTTPMETHOD_DELETE:
			request =new HttpDelete(absoluteUrl);
			break;
		}
		HttpParams parameters = generateRequestParams(isAddDefaultParams, params);
		request.setParams(parameters);
		// 生成HttpClient对象
		client = new DefaultHttpClient(setBasicHttpParams());
		try {
			HttpResponse response = client.execute(request);
			return handlerResponse(response, returnType);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
