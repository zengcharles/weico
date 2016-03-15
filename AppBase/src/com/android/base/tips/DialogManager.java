package com.android.base.tips;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.android.base.R;
import com.android.base.callback.ICallBack;

public class DialogManager {
	/**
	 * 简单的alertDialog
	 * @param context
	 * @param title
	 * @param content
	 */
	public static void showSimpleAlertDlalog(Context context,String title,String content){
		new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                /* User clicked OK so do some stuff */
            	
            }
        }).create().show();
	}
	
	public static void showOKDlalog(Context context,String title,String content,final ICallBack onOKCallBack){
		new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(content)
        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked OK so do some stuff */
            	onOKCallBack.call();
            }
        })
        .create()
        .show();
	}
	
	public static void showOKCancelDlalog(Context context,String title,String content,final ICallBack onOKCallBack,final ICallBack onCancelCallBack){
		new AlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(content)
        .setCancelable(false)
        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked OK so do some stuff */
            	onOKCallBack.call();
            }
        })
        .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /* User clicked Cancel so do some stuff */
            	onCancelCallBack.call();
            }
        })
        .create()
        .show();
	}
}
