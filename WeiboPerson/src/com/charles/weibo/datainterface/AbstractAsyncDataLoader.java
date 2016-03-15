package com.charles.weibo.datainterface;

import android.os.AsyncTask;

/**
 * 数据加载器抽象类
 */
public abstract class AbstractAsyncDataLoader extends
		AsyncTask<Void, Void, byte[]> {

	protected LoaderCallback callback;

	public void setCallback(LoaderCallback callback) {
		this.callback = callback;
	}

	@Override
	protected void onPreExecute() {
		if (callback != null) {
			callback.onPrepare();
		}
		super.onPreExecute();
	}

	public interface LoaderCallback {
		public void onPrepare();

		public void onLoadFinished(byte[] data);
	}
}
