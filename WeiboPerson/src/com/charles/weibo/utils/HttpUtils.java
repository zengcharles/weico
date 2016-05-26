package com.charles.weibo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.base.config.AppConfig;
import com.charles.weibo.Config.Config;


public class HttpUtils {

	private HttpClient client = null;
	private HttpPost post = null;
	private HttpGet get = null;
	private static String url = Config.HOST;
	private Context context;
	
	public HttpUtils(Context context) {
		this.context = context;
	}
	
	public HttpUtils() {
		
	}

	public Bitmap getBitmap(String url) {
		Bitmap bitmap=null;
		InputStream is =null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		// 添加http头信息
		get.addHeader("Referer",url);
		HttpResponse response;
		try {
			response = httpclient.execute(get);
			int code = response.getStatusLine().getStatusCode();
			// 检验状态码，如果成功接收数据
			if (code == 200) {
				HttpEntity httpEntity = response.getEntity();  
				//获得一个输入流   
				is = httpEntity.getContent();  
				bitmap = BitmapFactory.decodeStream(is);  
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	public String getStringDataByHttp(String url) {
		String result = "";
		post = new HttpPost(url);
		try {
			// 设置超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,Config.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, Config.SO_TIMEOUT);
			// 新建http
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new RuntimeException("网络异常");
				result = Config.dataErrorMsg;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return result;
	}
	
	public String getStringDataByHttp(String urlAction,
			HashMap<String, Object> args) {
		String result = "";
		post = new HttpPost(url + urlAction);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		/**
		 * 增加固定记录参数
		 */
		parameters.add(new BasicNameValuePair("appType","request from android"));
		parameters.add(new BasicNameValuePair("deviceInfo","Mobile Type:"+DeviceAndAppInfoUtils.getInstance().getPhoneStyle(context)));
		parameters.add(new BasicNameValuePair("deviceId","DeviceId:"+DeviceAndAppInfoUtils.getInstance().getDeviceId(context)));
		parameters.add(new BasicNameValuePair("appVersion","V"+DeviceAndAppInfoUtils.getInstance().getAppVersionName(context)+",上线日期："+DeviceAndAppInfoUtils.getInstance().getAppVersionCode(context)));
		parameters.add(new BasicNameValuePair("sdkVersion","android:"+DeviceAndAppInfoUtils.getInstance().getSysVersion(context)+",sdk:"+DeviceAndAppInfoUtils.getInstance().getSDKVersion(context)));
		/**
		 * 增加其他参数
		 */
		if(null!=args){
			Set<String> key = args.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				String mapkey = String.valueOf(iter.next());
				parameters.add(new BasicNameValuePair(mapkey,String.valueOf(args.get(mapkey))));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			post.setEntity(entity);
			
			// 设置连接超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					Config.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, Config.SO_TIMEOUT);
			// 生成HttpClient对象
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new NetException("网络异常");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	public String requestDataByHttpGet(String urlAction,HashMap<String, Object> params) {
		String result = "";
		String url = encodeUrl(urlAction,params);
		get = new HttpGet(url);
		// 设置连接超时时间
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				AppConfig.REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
		// 生成HttpClient对象
		client = new DefaultHttpClient(httpParams);
		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				//SessionID
				Header[] headers = response.getHeaders("Set-Cookie");
				if(headers.length>0){
					AppConfig.sessionID=headers[0].getValue();
				}
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new RuntimeException("网络异常");
				result = Config.dataErrorMsg;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}
	
	public String requestDataByHttpPost(String urlAction,HashMap<String, Object> args) {
		String result = "";
		post = new HttpPost(urlAction);
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		/**
		 * 增加固定记录参数
		 */
		try{
		parameters.add(new BasicNameValuePair("appType","request from android"));
		parameters.add(new BasicNameValuePair("deviceInfo","Mobile Type:"+DeviceAndAppInfoUtils.getInstance().getPhoneStyle(context)));
		parameters.add(new BasicNameValuePair("deviceId","DeviceId:"+DeviceAndAppInfoUtils.getInstance().getDeviceId(context)));
		parameters.add(new BasicNameValuePair("appVersion","V"+DeviceAndAppInfoUtils.getInstance().getAppVersionName(context)+",上线日期："+DeviceAndAppInfoUtils.getInstance().getAppVersionCode(context)));
		parameters.add(new BasicNameValuePair("sdkVersion","android:"+DeviceAndAppInfoUtils.getInstance().getSysVersion(context)+",sdk:"+DeviceAndAppInfoUtils.getInstance().getSDKVersion(context)));
		}catch(Exception e){
			e.printStackTrace();
		}
		/**
		 * 增加其他参数
		 */
		if(null!=args){
			Set<String> key = args.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				String mapkey = String.valueOf(iter.next());
				parameters.add(new BasicNameValuePair(mapkey,String.valueOf(args.get(mapkey))));
			}
		}
		try {
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			post.setEntity(entity);
			
			// 设置连接超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					Config.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, Config.SO_TIMEOUT);
			// 生成HttpClient对象
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new NetException("网络异常");
				result = Config.dataErrorMsg;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = e.getMessage();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
	
	
	public void disconnectRequest() {
		try {
			if (post != null) {
				if (!post.isAborted()) {
					post.abort();
				}
			}
			client.getConnectionManager().shutdown();
		} catch (Exception e) {

		}
	}
	
	//拼接查询地址参数
	public static String encodeUrl(String BasicUrl,HashMap<String, Object> params) {
		if(params == null){
			return BasicUrl;
		}
		
		StringBuilder sbfull = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		if(null!=params){
			Set<String> key = params.keySet();
			Iterator<String> iter = key.iterator();
			while (iter.hasNext()) {
				
				String mapkey = String.valueOf(iter.next());
				parameters.add(new BasicNameValuePair(mapkey,String.valueOf(params.get(mapkey))));
			}
		}
		
		for(int i=0;i<parameters.size();i++){
			sb.append(URLEncoder.encode(parameters.get(i).getName()) + "="
						+ URLEncoder.encode(parameters.get(i).getValue()));
			if(i<parameters.size()-1){
				sb.append("&");
			}
		}
		sbfull.append(BasicUrl);
		if(BasicUrl.indexOf("?") == -1){
			sbfull.append("?"+sb);
		}else{
			sbfull.append("&"+sb);
			
		}
		return sbfull.toString();
	}
}
