package com.android.base.upgrade.app_upgrade;

import java.io.File;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.base.ActivityManagerTools;
import com.android.base.R;
import com.android.base.callback.ICallBack;
import com.android.base.config.AppConfig;
import com.android.base.info.NetWorkInfo;
import com.android.base.net.HttpDownLoadTask;
import com.android.base.tips.DialogManager;
import com.android.base.tips.ProgressManager;
import com.android.utils.PhoneClientApi;


/**
 * 包含内容，
 * 断点续传、选择性更新、强制更新、整包更新、增量更新
 */
public class AppUpgrade {
	
	private Context context ;
	
	private ProgressDialog  dialog;
	
	private String apkNameWithoutSuffix;
	
	private String urlTemp;
	
	private String fileNameTemp;
	
	private int updateTypeTemp =0;
	
	private int updateModeTemp =0;
	
	
	public AppUpgrade(Context mContext) {
		super();
		this.context = mContext;
	}

	/**
	 * 更新类型  强制更新和可选更新
	 */
	public static final int FORCE_UPDATE_TYPE = 0;
	public static final int OPTIONAL_UPDATE_TYPE = 1;
	
	/**
	 * 更新模式  整包更新和增量更新
	 */
	public static final int INCREMENT_UPDATE_MODE = 0;
	public static final int FULL_UPDATE_MODE = 1;
	
	//检查更新
	public void checkUpdate(){
		
	}
	
	public void updateApp(final String url,final String fileName,int updateType ,final int updateMode){
		urlTemp =url;
		fileNameTemp = fileName;
		updateTypeTemp=updateType;
		updateModeTemp = updateMode;
		apkNameWithoutSuffix = fileName.substring(0,fileName.indexOf("."));
		switch (updateType) {
		case FORCE_UPDATE_TYPE:
			//强制更新
			DialogManager.showOKCancelDlalog(context,context.getString(R.string.APP_UPDATE),context.getString(R.string.APP_FORCE_UPDATE_TIPS),new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					downLoadUpdateFile(url, fileName, updateMode,false);
				}
			},new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					ActivityManagerTools.getInstance().exit();
				}
			});
			break;
		case OPTIONAL_UPDATE_TYPE:
			//可选更新
			DialogManager.showOKCancelDlalog(context,context.getString(R.string.APP_UPDATE), context.getString(R.string.APP_OPTIONAL_UPDATE_TIPS),new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					downLoadUpdateFile(url, fileName, updateMode,true);
				}
			},new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		default:
			break;
		}
	}
	
	Handler handler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(null!=msg){
				Bundle b = msg.getData();
				if(null!=b.getString("msg")&&"showProgressDialog".equals(b.getString("msg"))){
					if(null ==dialog){
						ProgressManager.ProgressParams params =new ProgressManager.ProgressParams();
						params.content=context.getString(R.string.PREPARE_TO_INSTALL);
						dialog = ProgressManager.getProgressDialog(context,params);
					}
					dialog.show();
				}
				if(null!=b.getString("msg")&&"dismissProgressDialog".equals(b.getString("msg"))){
					if(null !=dialog &&dialog.isShowing()){
						dialog.dismiss();
						inistall(AppConfig.DOWNLOAD_FILE_PATH+apkNameWithoutSuffix+".apk");
					}
					
				}
				if(null!=b.getString("msg")&&"updateFailure".equals(b.getString("msg"))){
					if(null !=dialog &&dialog.isShowing()){
						dialog.dismiss();
						DialogManager.showSimpleAlertDlalog(context,context.getString(R.string.TIPS), "app升级失败，请稍后再试...");
					}
					
				}
			}
		}
	};
	
	private Runnable patchApk =new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				PhoneClientApi api = new PhoneClientApi();
				api.makeFromSource(context, AppConfig.DOWNLOAD_FILE_PATH+apkNameWithoutSuffix+".apk", AppConfig.DOWNLOAD_FILE_PATH+fileNameTemp);
				Bundle bundle =new Bundle();
				bundle.putString("msg", "dismissProgressDialog");
				Message msg =new Message();
				msg.setData(bundle);
				handler.sendMessage(msg);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Bundle bundle =new Bundle();
				bundle.putString("msg", "updateFailure");
				Message msg =new Message();
				msg.setData(bundle);
				handler.sendMessage(msg);
				
			}catch (Error e) {
				// TODO: handle exception
				e.printStackTrace();
				Bundle bundle =new Bundle();
				bundle.putString("msg", "updateFailure");
				Message msg =new Message();
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
	};
	
	private void downLoadUpdateFile(String url,final String fileName,final int updateMode,boolean cancelable){
		if(!NetWorkInfo.isNetworkAvailableAndConnected(context)){
			DialogManager.showOKDlalog(context,context.getString(R.string.TIPS),context.getString(R.string.NETWORK_ERROR), new ICallBack() {
				
				@Override
				public void call(Object... params) {
					// TODO Auto-generated method stub
					updateApp(urlTemp, fileNameTemp, updateTypeTemp, updateModeTemp);
					return ;
				}
			});
			return;
		}
		HttpDownLoadTask task =new HttpDownLoadTask(context,new ICallBack() {
			
			@Override
			public void call(Object... params) {
				// TODO Auto-generated method stub
				String result = params[0].toString();
				/*
				 * 可以在此回调做相关处理操作
				 */
				if(Integer.valueOf(result)==0){
					/*
					 * 下载成功
					 */
					switch (updateMode) {
					case INCREMENT_UPDATE_MODE:
						Bundle bundle =new Bundle();
						bundle.putString("msg", "showProgressDialog");
						Message msg =new Message();
						msg.setData(bundle);
						handler.sendMessage(msg);
						new Thread(patchApk).start();
						break;
					case FULL_UPDATE_MODE:
						inistall(AppConfig.DOWNLOAD_FILE_PATH+fileName);
						break;
					default:
						break;
					}
				}else if(Integer.valueOf(result)==1){
					/*
					 * 下载失败
					 */
					DialogManager.showOKDlalog(context,context.getString(R.string.TIPS),context.getString(R.string.DOWNLOAD_FAIL), new ICallBack() {
						
						@Override
						public void call(Object... params) {
							// TODO Auto-generated method stub
							updateApp(urlTemp, fileNameTemp, updateTypeTemp, updateModeTemp);
							return ;
						}
					});
					return;
				}else if(Integer.valueOf(result)==2){
					/*
					 * 下载终止
					 */
					
				}
			}

		},cancelable,fileName);
		task.startDownLoad(url);
	}
	
	private void inistall(String newApkPath){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(
				Uri.fromFile(new File(newApkPath)),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}

