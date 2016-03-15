package com.android.base.net;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.base.callback.ICallBack;
import com.android.base.config.AppConfig;
import com.android.base.info.AppInfo;
import com.android.base.info.DeviceInfo;
import com.android.base.info.SystemInfo;

public class ApacheHttpUtils {

	private static String url = AppConfig.HOST;
	private String TAG="";

	private HttpClient client = null;
	private HttpPost post = null;
	private HttpGet get = null;
	private Context context;
	
	public ApacheHttpUtils(Context context) {
		this.context = context;
	}
	
	public ApacheHttpUtils() {
	}

	public String requestByHttpGet(String urlAction) {
		String result = "";
		get = new HttpGet(url + urlAction);
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
				result ="{\"status\":\"-1\",msg:\"此功能维护中···\", \"datas\":{}}";
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
	
	public String requestDataByHttpPost(String action) {
		String result = "";
		post = new HttpPost(url+action);
		try {
			// 设置超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					AppConfig.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
			// 新建http
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new RuntimeException("网络异常");
				result ="{\"status\":\"-1\",msg:\"此功能维护中···\", \"datas\":{}}";
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

	public String requestByHttpPost(String urlAction,
			HashMap<String, Object> args) {
		String result = "";
		try {
			post = new HttpPost(url + urlAction);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			/**
			 * 增加固定记录参数
			 */
			parameters.add(new BasicNameValuePair("appClientType","Android"));
			parameters.add(new BasicNameValuePair("deviceType",URLEncoder.encode(DeviceInfo.getInstance().getPhoneStyle(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("deviceId",URLEncoder.encode(DeviceInfo.getInstance().getDeviceId(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionCode",URLEncoder.encode(AppInfo.getInstance().getAppVersionCode(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionName",URLEncoder.encode(AppInfo.getInstance().getAppVersionName(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sdkVersion",URLEncoder.encode(SystemInfo.getInstance().getSDKVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sysVersion",URLEncoder.encode(SystemInfo.getInstance().getSysVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("userName",""));
			/**
			 * 增加其他参数
			 */
			if(null!=args){
				Set<String> key = args.keySet();
				Iterator iter = key.iterator();
				while (iter.hasNext()) {
					Map.Entry obj = (Map.Entry) iter.next();
					parameters.add(new BasicNameValuePair(obj.getKey().toString(), URLEncoder.encode(obj
							.getValue().toString(),HTTP.UTF_8)));
				}
			}
			
			/**
			 *   增加其他参数 (上述方式有可能报异常)
				 if(null!=args){
					Set<String> key = args.keySet();
					Iterator<String> iter = key.iterator();
					while (iter.hasNext()) {
						String mapkey = String.valueOf(iter.next());
						parameters.add(new BasicNameValuePair(mapkey,String.valueOf(args.get(mapkey))));
					}
				 }
			 */
		
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			post.setEntity(entity);
			
			if(AppConfig.sessionID==null||AppConfig.sessionID.equals("")){
				
			}else{
				post.setHeader("Cookie","Set-Cookie="+AppConfig.sessionID); 
				//post.setHeader("Cookie", "PHPSESSID=" + AppConfig.sessionID);
			}
			// 设置连接超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					AppConfig.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
			// 生成HttpClient对象
			client = new DefaultHttpClient(httpParams);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				//SessionID
				Header[] headers = response.getHeaders("Set-Cookie");
				if(headers.length>0){
					AppConfig.sessionID=headers[0].getValue();
				}
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new RuntimeException("网络异常");
				result ="{\"status\":\"-1\",msg:\"此功能维护中···\", \"datas\":{}}";
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
		}
		return result;
	}
	
	/**
	 * https证书从网络 获取
	 * @param urlAction
	 * @param args
	 * @return
	 */
	public String requestByHttpsPost(String urlAction,
			HashMap<String, Object> args) {
		String result = "";
		try {
			post = new HttpPost(url + urlAction);
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			/**
			 * 增加固定记录参数
			 */
			parameters.add(new BasicNameValuePair("appClientType","Android"));
			parameters.add(new BasicNameValuePair("deviceType",URLEncoder.encode(DeviceInfo.getInstance().getPhoneStyle(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("deviceId",URLEncoder.encode(DeviceInfo.getInstance().getDeviceId(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionCode",URLEncoder.encode(AppInfo.getInstance().getAppVersionCode(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionName",URLEncoder.encode(AppInfo.getInstance().getAppVersionName(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sdkVersion",URLEncoder.encode(SystemInfo.getInstance().getSDKVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sysVersion",URLEncoder.encode(SystemInfo.getInstance().getSysVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("userName",""));
			/**
			 * 增加其他参数
			 */
			if(null!=args){
				Set<String> key = args.keySet();
				Iterator iter = key.iterator();
				while (iter.hasNext()) {
					Map.Entry obj = (Map.Entry) iter.next();
					parameters.add(new BasicNameValuePair(obj.getKey().toString(), URLEncoder.encode(obj
							.getValue().toString(),HTTP.UTF_8)));
				}
			}
			/**
			 *   增加其他参数 (上述方式有可能报异常)
				 if(null!=args){
					Set<String> key = args.keySet();
					Iterator<String> iter = key.iterator();
					while (iter.hasNext()) {
						String mapkey = String.valueOf(iter.next());
						parameters.add(new BasicNameValuePair(mapkey,String.valueOf(args.get(mapkey))));
					}
				 }
			 */
		
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
					HTTP.UTF_8);
			post.setEntity(entity);
			// 生成HttpClient对象
			client = ApacheHttpsClient.getHttpClient();
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			}else{
//				throw new RuntimeException("网络异常");
				result ="{\"status\":\"-1\",msg:\"此功能维护中···\", \"datas\":{}}";
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
		}
		return result;
	}
	
	/**
	 * https证书从本地 获取
	 * @param context
	 * @param urlAction
	 * @param args
	 * @return
	 */
	public String requestByHttpsPost(Context context,String urlAction,HashMap<String, Object> args) {
		InputStream ins = null;
		String result = "";
		try {
			//导入证书
			ins = context.getAssets().open("app_pay.cer"); //下载的证书放到项目中的assets目录中
			CertificateFactory cerFactory = CertificateFactory
					.getInstance("X.509");
			Certificate cer = cerFactory.generateCertificate(ins);
			KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);

			SSLSocketFactory socketFactory = new SSLSocketFactory(keyStore);
			Scheme sch = new Scheme("https", socketFactory, 443);
			
			// 设置连接超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					AppConfig.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
			
			client = new DefaultHttpClient(httpParams);
			client.getConnectionManager().getSchemeRegistry()
					.register(sch);
			BufferedReader reader = null;
			
			List<NameValuePair> parameters = new ArrayList<NameValuePair>();
			/**
			 * 增加固定记录参数
			 */
			parameters.add(new BasicNameValuePair("appClientType","Android"));
			parameters.add(new BasicNameValuePair("deviceType",URLEncoder.encode(DeviceInfo.getInstance().getPhoneStyle(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("deviceId",URLEncoder.encode(DeviceInfo.getInstance().getDeviceId(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionCode",URLEncoder.encode(AppInfo.getInstance().getAppVersionCode(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("appVersionName",URLEncoder.encode(AppInfo.getInstance().getAppVersionName(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sdkVersion",URLEncoder.encode(SystemInfo.getInstance().getSDKVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("sysVersion",URLEncoder.encode(SystemInfo.getInstance().getSysVersion(context),HTTP.UTF_8)));
			parameters.add(new BasicNameValuePair("userName",""));
			/**
			 * 增加其他参数
			 */
			if(null!=args){
				Set<String> key = args.keySet();
				Iterator iter = key.iterator();
				while (iter.hasNext()) {
					Map.Entry obj = (Map.Entry) iter.next();
					parameters.add(new BasicNameValuePair(obj.getKey().toString(), URLEncoder.encode(obj
							.getValue().toString(),HTTP.UTF_8)));
				}
			}
			/**
			 *   增加其他参数 (上述方式有可能报异常)
				 if(null!=args){
					Set<String> key = args.keySet();
					Iterator<String> iter = key.iterator();
					while (iter.hasNext()) {
						String mapkey = String.valueOf(iter.next());
						parameters.add(new BasicNameValuePair(mapkey,String.valueOf(args.get(mapkey))));
					}
				 }
			 */
			
			try {
				Log.d(TAG, "executeGet is in,murl:" + urlAction);
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters,
						HTTP.UTF_8);
				post = new HttpPost(url + urlAction);
				post.setEntity(entity);
				
				HttpResponse response = client.execute(post);
				if (response.getStatusLine().getStatusCode() != 200) {
					post.abort();
					return result;
				}

				reader = new BufferedReader(new InputStreamReader(response
						.getEntity().getContent()));
				StringBuffer buffer = new StringBuffer();
				String line = null;
				while ((line = reader.readLine()) != null) {
					buffer.append(line);
				}
				result = buffer.toString();
				Log.d(TAG, "mUrl=" + urlAction + "\nresult = " + result);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
					reader.close();
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (ins != null)
					ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	

	/**
	 * 下载文件，实时返回读取字节数
	 */

	public boolean downLoadFileFromInternet(Context mContext, String downLoadUrl,String fileName,
			ICallBack getTotalSize, ICallBack getCurrentSize) {
		try {
			/**
			 * 增加固定记录参数
			 */
			downLoadUrl=downLoadUrl+"?appClientType=android";
			downLoadUrl=downLoadUrl+"&deviceType="+URLEncoder.encode(DeviceInfo.getInstance().getPhoneStyle(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&deviceId="+URLEncoder.encode(DeviceInfo.getInstance().getDeviceId(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&appVersionCode="+URLEncoder.encode(AppInfo.getInstance().getAppVersionCode(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&appVersionName="+URLEncoder.encode(AppInfo.getInstance().getAppVersionName(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&sdkVersion="+URLEncoder.encode(SystemInfo.getInstance().getSDKVersion(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&sysVersion="+URLEncoder.encode(SystemInfo.getInstance().getSysVersion(context),HTTP.UTF_8);
			downLoadUrl=downLoadUrl+"&userName="+"";
			get = new HttpGet(downLoadUrl);
			HttpResponse response;
			// 设置连接超时时间
			BasicHttpParams httpParams = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParams,
					AppConfig.REQUEST_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, AppConfig.SO_TIMEOUT);
			// 生成HttpClient对象
			client = new DefaultHttpClient(httpParams);
			response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				long length = entity.getContentLength();
				InputStream is = entity.getContent();
				getTotalSize.call(entity.getContentLength());// 调用回调返回文件大小
				FileOutputStream fileOutputStream = null;
				File path = new File(AppConfig.DOWNLOAD_FILE_PATH);
				if (!path.exists()) {
					path.mkdirs();
				}
				if (is != null) {
					File file;
					fileOutputStream = new FileOutputStream(new File(
							path,fileName));
					Log.d("mytag", "开始下载 length=" + length);

					byte[] buf = new byte[1024];
					int ch = -1;
					int count = 0;
					while ((ch = is.read(buf)) != -1) {
						fileOutputStream.write(buf, 0, ch);
						count += ch;
						getCurrentSize.call(count);// 调用回调返回当前已读取数据量大小
					}
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				Log.d("mytag", "下载完成");

				if (is != null) {
					is.close();
				}
				fileOutputStream.flush();
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			}else{
				Log.e("mytag", "下载失败");
				return false;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			Log.e("mytag", "下载失败");
			return false;
			// 更新失败
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("mytag", "下载失败");
			return false;
		}
		return true;
	}

	public void disconnectRequest() {
		try {
			if (post != null) {
				if (!post.isAborted()) {
					post.abort();
				}
			}
			if (get != null) {
				if (!get.isAborted()) {
					get.abort();
				}
			}
			client.getConnectionManager().shutdown();
		} catch (Exception e) {

		}
	}

	public void disconnectFileDownload() {
		try {
			if (get != null) {
				if (!get.isAborted()) {
					get.abort();
				}
			}
			client.getConnectionManager().shutdown();
		} catch (Exception e) {

		}
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
				//2.为位图设置100K的缓存

				BitmapFactory.Options opts=new BitmapFactory.Options();

				opts.inTempStorage = new byte[100 * 1024];

				//3.设置位图颜色显示优化方式

				//ALPHA_8：每个像素占用1byte内存（8位）

				//ARGB_4444:每个像素占用2byte内存（16位）

				//ARGB_8888:每个像素占用4byte内存（32位）

				//RGB_565:每个像素占用2byte内存（16位）

				//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。

				opts.inPreferredConfig = Bitmap.Config.RGB_565;

				//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收

				opts.inPurgeable = true;

				//5.设置位图缩放比例

				//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。

//				opts.inSampleSize = 4;

				//6.设置解码位图的尺寸信息

				opts.inInputShareable = true;  
				
				bitmap = BitmapFactory.decodeStream(is,null,opts);    
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

	/**
	 * 加载网络图片
	 * 
	 * @param info
	 * @return Bitmap
	 */
	public Bitmap downloadImageWithUrl(String imgUrl) {
		Bitmap bitmap = null;
		InputStream is = null;
		try {
			URL url = new URL(imgUrl);
			if (url == null || url.getContent() == null) {
				return null;
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				is = conn.getInputStream();
				int length = (int) conn.getContentLength();
				if (length != -1) {
					byte[] imgData = new byte[length];
					byte[] temp = new byte[512];
					int readLen = 0;
					int destPos = 0;
					while ((readLen = is.read(temp)) > 0) {
						System.arraycopy(temp, 0, imgData, destPos, readLen);
						destPos += readLen;
					}

					//2.为位图设置100K的缓存

					BitmapFactory.Options opts=new BitmapFactory.Options();

					opts.inTempStorage = new byte[100 * 1024];

					//3.设置位图颜色显示优化方式

					//ALPHA_8：每个像素占用1byte内存（8位）

					//ARGB_4444:每个像素占用2byte内存（16位）

					//ARGB_8888:每个像素占用4byte内存（32位）

					//RGB_565:每个像素占用2byte内存（16位）

					//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。

					opts.inPreferredConfig = Bitmap.Config.RGB_565;

					//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收

					opts.inPurgeable = true;

					//5.设置位图缩放比例

					//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。

//					opts.inSampleSize = 4;

					//6.设置解码位图的尺寸信息

					opts.inInputShareable = true;  

					//7.解码位图

					bitmap = BitmapFactory.decodeByteArray(imgData, 0,
							imgData.length,opts);

				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
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
}
