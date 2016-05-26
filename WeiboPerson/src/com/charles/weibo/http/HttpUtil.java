
package com.charles.weibo.http;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class HttpUtil {
	public static int TIMEOUT_DEFAULT = 30 * 1000; // 超时时间 默认30秒

	// 包含超时 的 http Post 请求
	public static String getInfoPost(String url, List<NameValuePair> params)
			throws Exception {
		HttpPost request = new HttpPost(url);

		DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
		// 请求超时
		defaultHttpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, TIMEOUT_DEFAULT);
		// 读取超时
		defaultHttpClient.getParams().setParameter(
				CoreConnectionPNames.SO_TIMEOUT, TIMEOUT_DEFAULT);
		if (params != null) {
			request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
		}
		HttpResponse response = defaultHttpClient.execute(request);
		if (response.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(response.getEntity());
			return result;
		}
		return "";
	}
	
    // 网络连接部分

 /*   public static String postByHttpURLConnection(String strUrl,
            NameValuePair... nameValuePairs) {
        return CustomHttpURLConnection.PostFromWebByHttpURLConnection(strUrl,
                nameValuePairs);
    }

    public static String getByHttpURLConnection(String strUrl,
            NameValuePair... nameValuePairs) {
        return CustomHttpURLConnection.GetFromWebByHttpUrlConnection(strUrl,
                nameValuePairs);
    }

    public static String postByHttpClient(Context context, String strUrl,
            NameValuePair... nameValuePairs) throws Exception {
        String result = CustomHttpClient.PostFromWebByHttpClient(context, strUrl, nameValuePairs);
        return result;
    }

    public static String getByHttpClient(Context context, String strUrl,
            NameValuePair... nameValuePairs) throws Exception {
        String result = CustomHttpClient.getFromWebByHttpClient(context, strUrl, nameValuePairs);

        if (TextUtils.isEmpty(result)) {
            result = "";
        }

        return result;
    }*/

    // 网络连接判断
    /**
     * 判断是否有网络
     */
    public static boolean isNetworkAvailable(Context context) {
        return NetWorkHelper.isNetworkAvailable(context);
    }

    /**
     * 判断mobile网络是否可用
     */
    public static boolean isMobileDataEnable(Context context) {
        String TAG = "httpUtils.isMobileDataEnable()";
        try {
            return NetWorkHelper.isMobileDataEnable(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断wifi网络是否可用
     */
    public static boolean isWifiDataEnable(Context context) {
        String TAG = "httpUtils.isWifiDataEnable()";
        try {
            return NetWorkHelper.isWifiDataEnable(context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否为漫�?
     */
    public static boolean isNetworkRoaming(Context context) {
        return NetWorkHelper.isNetworkRoaming(context);
    }

    /**
     * 编码测试
     */
    public static void testCharset(String datastr) {
        try {
            String temp = new String(datastr.getBytes(), "GBK");
            Log.v("TestCharset", "****** getBytes() -> GBK ******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "UTF-8");
            Log.v("TestCharset", "****** GBK -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("GBK"), "ISO-8859-1");
            Log.v("TestCharset", "****** GBK -> ISO-8859-1 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "UTF-8");
            Log.v("TestCharset", "****** ISO-8859-1 -> UTF-8 *******/n" + temp);
            temp = new String(datastr.getBytes("ISO-8859-1"), "GBK");
            Log.v("TestCharset", "****** ISO-8859-1 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "GBK");
            Log.v("TestCharset", "****** UTF-8 -> GBK *******/n" + temp);
            temp = new String(datastr.getBytes("UTF-8"), "ISO-8859-1");
            Log.v("TestCharset", "****** UTF-8 -> ISO-8859-1 *******/n" + temp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
