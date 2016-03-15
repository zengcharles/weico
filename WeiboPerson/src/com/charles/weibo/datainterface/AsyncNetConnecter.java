package com.charles.weibo.datainterface;

import java.util.List;

import org.apache.http.message.BasicNameValuePair;

/**
 * 网络异步连接器
 */
public class AsyncNetConnecter extends AbstractAsyncDataLoader {

	private String url;
	private List<BasicNameValuePair> params;

	public AsyncNetConnecter(String url, List<BasicNameValuePair> params) {
		this.url = url;
		this.params = params;
	}

	@Override
	protected byte[] doInBackground(Void... params) {
		byte[] b = new HttpConnector().requestByPost(url, this.params);
		return b;
	}

	@Override
	protected void onPostExecute(byte[] data) {
		if (callback != null)
			callback.onLoadFinished(data);
	}
}
