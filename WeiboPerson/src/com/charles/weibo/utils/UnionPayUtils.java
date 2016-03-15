package com.charles.weibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

import com.charles.weibo.Config.Config;
import com.charles.weibo.ui.CommonAlertDialog;
import com.unionpay.UPPayAssistEx;

public class UnionPayUtils {

	private Context context;
	private int ret;
	
	
	private static final int PLUGIN_VALID = 0;
    private static final int PLUGIN_NOT_INSTALLED = -1;
    private static final int PLUGIN_NEED_UPGRADE = 2;
	
	public UnionPayUtils(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	
	//跳转银联接口进行交易
	public void gotoPay(String tn){
		
		//UPPayAssistEx.startPayByJAR(getActivity(), PayActivity.class, null, null, getTn(), serverMode);
		ret = UPPayAssistEx.startPay((Activity)context, null, null, tn, Config.serverMode);
		 if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
             // 需要重新安装控件 
             new CommonAlertDialog.Builder(context)
             .setMessage("完成购买需要安装银联支付控件，是否安装？")
             .setLeftButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			})
			.setRightButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					boolean success = UPPayAssistEx.installUPPayPlugin(context);
					if(success){
						dialog.dismiss();
					}
				}
			}).create().show();
         }
	}
	
	
}
