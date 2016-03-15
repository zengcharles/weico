package com.charles.weibo.datainterface;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.text.TextUtils;

/**
 * Http连接器
 */
public class HttpConnector {

	public  static String sessionId;

	private static synchronized HttpClient getHttpClient(HttpRequest request) {
		HttpParams params = new BasicHttpParams();
		// 设置一些基本参数
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUseExpectContinue(params, true);

		// User Agent
		String model = android.os.Build.MODEL;
		String release = android.os.Build.VERSION.RELEASE;
		HttpProtocolParams
				.setUserAgent(
						params,
						"Mozilla/5.0(Linux;U;Android "
								+ release
								+ ";"
								+ model
								+ ") AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.45 Safari/535.19");

		// 超时设置
		/* 从连接池中取连接的超时时间 */
		ConnManagerParams.setTimeout(params, 8000);
		/* 连接超时 */
		HttpConnectionParams.setConnectionTimeout(params, 10000);
		/* 请求超时 */
		HttpConnectionParams.setSoTimeout(params, 10000);
		/* 关闭节省流量模式 */
		HttpConnectionParams.setTcpNoDelay(params, true);

		// 设置HttpClient支持HTTP和HTTPS两种模式
		SchemeRegistry schReg = new SchemeRegistry();
		schReg.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		schReg.register(new Scheme("https",
				SSLSocketFactory.getSocketFactory(), 443));

		// 使用线程安全的连接管理来创建HttpClient
		ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
				params, schReg);

		return new DefaultHttpClient(conMgr, params);
	}

	private static byte[] connect(HttpRequest request)
			throws ClientProtocolException, IOException {
		// Log.e("请求", CommonDefines.SESSIONID);
		HttpResponse response = null;
		if (!TextUtils.isEmpty(sessionId)) {
			// 设置sessionId，把第一次请求的id放在之后要请求的request报文头里
			request.setHeader("Cookie", sessionId);
		}
		if (request instanceof HttpPost) {
			HttpPost post = (HttpPost) request;
			response = getHttpClient(request).execute(post);
		} else if (request instanceof HttpGet) {
			HttpGet get = (HttpGet) request;
			response = getHttpClient(request).execute(get);
		}
		if (response != null
				&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity resEntity = response.getEntity();
			Header[] headers = response.getHeaders("set-cookie");
			// 保存服务器返回的session
			for (int i = 0; i < headers.length; i++) {
				// Log.e("sessionid", headers<i>.getValue());
				String value = headers[i].getValue();
				sessionId = value.substring(0, value.indexOf(";"));
			}

			return (resEntity == null) ? null : EntityUtils
					.toByteArray(resEntity);
		}
		return null;
	}

	public byte[] requestByPost(String url, List<BasicNameValuePair> params) {
		try {
			HttpPost httpPost = new HttpPost(url);
			if (params != null && params.size() > 0) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params,
						"utf-8");
				httpPost.setEntity(entity);
			}
			return connect(httpPost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();// 可能是没连接网络
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 销毁sessionId
	 */
	public static final void destroySession() {
		sessionId = null;
	}
}
