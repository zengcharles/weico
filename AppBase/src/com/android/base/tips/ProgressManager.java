package com.android.base.tips;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.base.R;
import com.android.base.callback.ICallBack;

public class ProgressManager {
	
	public static final int STYLE_STRIP = 0; //长条形
	public static final int STYLE_CYCLE = 1; //圆形
	
	public static class ProgressParams{
		
		public int titleIcon = -1;
		public String title = null;
		public String content = null;
		public boolean withCancelBtn = false;
		public ICallBack cancelBtnCallBack;
		public boolean isCancelable = false;;
		public ICallBack cancelCallBack;
		public boolean isCancelOntouchOutside = false;;
		public int progressStyle = STYLE_CYCLE;
		public int max = 100;
	}
	
	public static ProgressDialog getProgressDialog(Context context ,final ProgressParams params){
		ProgressDialog mDialog = new ProgressDialog(context);
		if(params.titleIcon != -1){
			//带标题栏图标
			mDialog.setIcon(params.titleIcon);
		}
		if(null!=params.title &&!"".equalsIgnoreCase(params.title)){
			//带标题
			mDialog.setTitle(params.title);
		}
		if(null!=params.content &&!"".equalsIgnoreCase(params.content)){
			//带提示语信息
			mDialog.setMessage(params.content);
		}
		if(params.progressStyle != STYLE_CYCLE){
			//长条形
			mDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			mDialog.setMax(params.max);
		}else{
			//圆形
			mDialog.setIndeterminate(true);
		}
		if(params.withCancelBtn){
			//带取消按钮
			mDialog.setButton2(context.getText(R.string.CANCEL), new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int whichButton) {

	                /* User clicked No so do some stuff */
	            	params.cancelBtnCallBack.call(dialog,whichButton);
	            }
	        });
		}
		mDialog.setCancelable(params.isCancelable);
		if(params.isCancelable){
			//可以通过按系统返回键取消
			mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					params.cancelCallBack.call(dialog);
				}
			});
		}
		mDialog.setCanceledOnTouchOutside(params.isCancelOntouchOutside);
		return  mDialog;
	}
	
	
	/**
	 * 长条形dialog使用示例
	 * @param context
	 * @param titleIcon
	 * @param title
	 * @param onCancel
	 * @param onFinishCallBack
	 
	private static Handler mProgressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mProgress >= MAX_PROGRESS) {
                mProgressDialog.dismiss();
                onFinish.call();
            } else {
                mProgress++;
                mProgressDialog.incrementProgressBy(1);
                mProgressHandler.sendEmptyMessageDelayed(0, 100);
            }
        }
    };
	
	private static void showProcessDialogDemo(Context context,int titleIcon,String title,final CallBack onCancel,final CallBack onFinishCallBack){
		onFinish =onFinishCallBack;
		mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setIcon(titleIcon);
        mProgressDialog.setTitle(title);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMax(MAX_PROGRESS);
        mProgressDialog.setButton2(context.getText(R.string.CANCEL), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            	onCancel.call();
            }
        });
        mProgress = 0;
        mProgressDialog.setProgress(0);
        mProgressDialog.show();
        mProgressHandler.sendEmptyMessage(0);
	}
	*/
}
