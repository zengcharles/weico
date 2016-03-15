package com.android.base.asyncTask;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.android.base.R;
import com.android.base.callback.ICallBack;
import com.android.base.tips.ProgressManager;


/**
 * @author zx
 * 异步任务类 ，可实现异步任务操作，包含网络操作，数据库操作等各种耗时操作
 * 调用时在回调函数doing中处理相关耗时任务
 * 在回调函数refreshView中刷新界面组件元素
 * 其他参数见构造函数的参数名
 */
public class GenAsyncTask extends AsyncTask<Object, Integer, Object> {

	private ProgressDialog dialog ;
	
	private ICallBack operation,refresh,cancelOperation;
	
	private boolean isShowDialog =true;
	
	private boolean isCancleAble =false;
	
	public static class Params {
		public ICallBack backgroundDoing;
		public ICallBack refreshView;
		public boolean isShowProgressDialog = true;
		public boolean isCancleAble = false;
		public ICallBack cancelDoing;
	}
	
	public GenAsyncTask(Context mContext,Params taskParams) {
		super();
		operation = taskParams.backgroundDoing;
		isShowDialog=taskParams.isShowProgressDialog;
		isCancleAble =taskParams.isCancleAble;
		refresh =taskParams.refreshView;
		cancelOperation=taskParams.cancelDoing;
		ProgressManager.ProgressParams params =new ProgressManager.ProgressParams();
		params.content=mContext.getString(R.string.TIPS);
		dialog =ProgressManager.getProgressDialog(mContext, params);
		if(isCancleAble){
			dialog.setButton(mContext.getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					GenAsyncTask.this.cancel(true);
				}
			});
		}
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if(null!=dialog&&dialog.isShowing()){
			dialog.dismiss();
		}
		cancelOperation.call();
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(null!=dialog&&dialog.isShowing()){
			dialog.dismiss();
		}
		refresh.call();
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(isShowDialog){
			dialog.show();
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		// TODO Auto-generated method stub
		operation.call();
		return null;
	}

}

