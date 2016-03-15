package com.charles.weibo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.charles.weibo.R;

public class ServiceRouteHelper {

	private Context mContext;
	
	public ServiceRouteHelper(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	public void goIntent(String Code){
		
		
	}
	//支付完成后的跳转处理
	public void payResultGoto(String code,Bundle bundle){
		
	}
	
	//不带数据调整
	private void gotoIntent(Class<?> cls){
		
		Intent it = new Intent(mContext, cls);
		mContext.startActivity(it);
		((Activity)mContext).overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
	}
	//带数据的跳转
	private void gotoIntentWithBundle(Class<?> cls,Bundle bundle){
		Intent it = new Intent(mContext, cls);
		it.putExtras(bundle);
		mContext.startActivity(it);
	}
	
	//显示提示框
	public void showUnuserful(){
		CommonUtils.showSimpleAlertDlalog(mContext, "提示", "此功能暂未开放，敬请关注！");
	}
	
	//获取分类名称
	public static String getCatName(String cat_code){
		if(cat_code.equals("C1")){//社区充值
			return "社区充值";
		}else if(cat_code.equals("C2")){
			return "社区保险";
		}else if(cat_code.equals("C3")){
			return "社区邮递";
		}else if(cat_code.equals("C4")){
			return "社区金融";
		}else if(cat_code.equals("C5")){
			return "社区医务";
		}else if(cat_code.equals("C6")){
			return "社区票务";
		}else if(cat_code.equals("C7")){
			return "公益彩票";
		}else if(cat_code.equals("C8")){
			return "便民查询";
		}else{
			return "";
		}
	}
}
