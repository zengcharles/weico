package com.charles.weibo.utils;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.HttpQuery;
import com.charles.weibo.ui.LoadingProgress;

public class HttpAsyncTask extends AsyncTask<HttpQuery , Void,JSONObject> {
	
	private LoadingProgress loadingProgress=null;
	
	private HashMap<String,Object> htpParams ;
	
	private int reqCodeValue;
	
	private CallHttpResponse response;
	
	private Context mContext;
	
	private HttpQuery httpQuery;
	public final static int ERR_ON_CONCEL=1000;
	
	public HttpAsyncTask(Context context,int reqCode,HashMap<String, Object> reqParams,CallHttpResponse resp,boolean isCancelAble) {
		this.htpParams = reqParams;
		this.reqCodeValue =reqCode;
		this.response =resp;
		this.mContext =context;
	}
	
	@Override
	protected JSONObject doInBackground(HttpQuery... params) {
		// TODO Auto-generated method stub
		httpQuery =params[0];
		return httpQuery.query(htpParams);	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
		//stopLoadingDialog();
		httpQuery.httpCancel();
		response.onHttpError(reqCodeValue, ERR_ON_CONCEL);
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		response.onHttpSuccess(reqCodeValue, result);
		//stopLoadingDialog();
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//startLoadingDialog();
	}
	
	public ProgressDialog getCycleStyleProgressBarWithoutTitle(Context context,String content,boolean isCancelable,boolean isCancelOntouchOutside){
		final ProgressDialog dialog = new ProgressDialog(context,0);
        dialog.setMessage(content);
        dialog.setIndeterminate(true);
        if(isCancelable){
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialogInterface) {
					HttpAsyncTask.this.cancel(true);
				}
			});
		}
        dialog.setCancelable(isCancelable);
        dialog.setCanceledOnTouchOutside(isCancelOntouchOutside);
        return dialog;
	}
	
	public void query(HttpQuery query){
		HttpAsyncTask.this.execute(query);
	}
	
	public void startLoadingDialog(){
		if(loadingProgress == null){
			loadingProgress = LoadingProgress.createDialog(mContext);
		}
		loadingProgress.show();		
	}
	
    public void stopLoadingDialog(){
		if(loadingProgress != null){
			loadingProgress.dismiss();
			loadingProgress = null;
		}
	}

}
