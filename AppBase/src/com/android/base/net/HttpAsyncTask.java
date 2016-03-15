package com.android.base.net;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.android.base.R;
import com.android.base.callback.ICallBack;
import com.android.base.tips.ProgressManager;

public class HttpAsyncTask extends AsyncTask<IHttpQuery , Void,JSONObject> {
	
	private ProgressDialog mProgressDialog=null;
	
	private HashMap<String,Object> httpParams ;
	
	private int reqCodeValue;
	
	private IHttpResponse response;
	
	private Context mContext;
	
	private IHttpQuery httpQuery;
	public final static int ERR_ON_CONCEL=1000;
	
	public HttpAsyncTask(Context context,int reqCode,HashMap<String, Object> reqParams,IHttpResponse resp,boolean isCancelAble) {
		httpParams = reqParams;
		reqCodeValue =reqCode;
		response =resp;
		mContext =context;
		if(null==mProgressDialog){
			ProgressManager.ProgressParams params =new ProgressManager.ProgressParams();
			params.content=mContext.getString(R.string.WAITING);
			params.isCancelable = isCancelAble;
			if(isCancelAble){
				params.cancelCallBack =new ICallBack() {
					
					@Override
					public void call(Object... params) {
						// TODO Auto-generated method stub
						HttpAsyncTask.this.cancel(true);
					}
				};
			}
			params.isCancelOntouchOutside =false;
			mProgressDialog=ProgressManager.getProgressDialog(mContext, params);
		}
		mProgressDialog.show();
	}
	
	@Override
	protected JSONObject doInBackground(IHttpQuery... params) {
		// TODO Auto-generated method stub
		httpQuery =params[0];
		return httpQuery.query(reqCodeValue,httpParams);
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		if(null!=mProgressDialog&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		httpQuery.queryCancel();
		response.onHttpFailure(reqCodeValue, ERR_ON_CONCEL);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		response.onHttpSuccess(reqCodeValue, result);
		if(null!=mProgressDialog&&mProgressDialog.isShowing()){
			mProgressDialog.dismiss();
		}
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
	}
	
	public void query(IHttpQuery query){
		HttpAsyncTask.this.execute(query);
	}

}
