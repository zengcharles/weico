package com.android.base.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.android.base.R;
import com.android.base.callback.ICallBack;
import com.android.base.tips.ProgressManager;

/**
 * 用于异步下载文件
 * 
 */

public class HttpDownLoadTask extends AsyncTask<Object, Integer, Object> {

	private Context context = null;

	private int max_progress = 100;

	private int fileSize = 0;

	private int downLoadedSize = 0;
	
	private ICallBack onFinish;
	
	private String fileName;
	
	/*
	 * 返回值 resultFlag 值列表
	 * 0 表示下载成功
	 * 1表示下载失败
	 * 2表示手动中断下载
	 */
	private int resultFlag =0 ;
	
	ApacheHttpUtils utils ;

	public HttpDownLoadTask(Context mContext,ICallBack onFinishCallBack,boolean cancelable,String filename) {
		super();
		this.context = mContext;
		utils =new ApacheHttpUtils(context);
		onFinish = onFinishCallBack;
		fileName = filename;
		ProgressManager.ProgressParams params =new ProgressManager.ProgressParams();
		params.title =mContext.getString(R.string.DOWNLOADING)+filename;
		params.isCancelOntouchOutside =false;
		params.max = max_progress;
		params.progressStyle =ProgressManager.STYLE_STRIP;
		if(cancelable){
			params.isCancelable =cancelable;
			params.cancelCallBack =new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					utils.disconnectFileDownload();
					HttpDownLoadTask.this.cancel(true);
				}
			};
			params.withCancelBtn =cancelable;
			params.cancelBtnCallBack  =new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					utils.disconnectFileDownload();
					HttpDownLoadTask.this.cancel(true);
				}
			};
			
		}
		dialog=ProgressManager.getProgressDialog(mContext, params);
		dialog.show();
	}

	private ProgressDialog dialog = null;

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		resultFlag = 2;
		onFinish.call(resultFlag);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		dialog.dismiss();
		resultFlag = Integer.valueOf(result.toString());
		onFinish.call(resultFlag);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		dialog.setProgress(values[0]);
	}

	@Override
	protected Object doInBackground(Object... args) {
		// TODO Auto-generated method stub
		//args[0]为URL地址 args[1]为文件名
		boolean finish =utils.downLoadFileFromInternet(context, args[0].toString(),fileName,
				new ICallBack() {

					@Override
					public void call(Object... params) {
						// TODO Auto-generated method stub
						fileSize = Integer.valueOf(params[0].toString());
					}

				}, new ICallBack() {

					@Override
					public void call(Object... params) {
						// TODO Auto-generated method stub
						downLoadedSize = Integer.valueOf(params[0].toString());
						publishProgress(max_progress * downLoadedSize
								/ fileSize);
					}
					
				});
		if(finish){
			return 0;
		}else{
			return 1;
		}
	}

	public void startDownLoad(String absoluteUrl){
		HttpDownLoadTask.this.execute(absoluteUrl);
	}
}