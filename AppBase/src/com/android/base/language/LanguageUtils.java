package com.android.base.language;

import java.util.Locale;

import com.android.base.ActivityManagerTools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

public class LanguageUtils {
private Context context;
	
    public LanguageUtils(Context mContext) {
		super();
		this.context = mContext;
	}
    //语言切换 测试可用
	public void switchLanguage(Activity activity,Locale language){
    	
    	Resources resources = context.getResources();//获得res资源对象
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources .getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        config.locale = language; //简体中文
        resources.updateConfiguration(config, dm);
        //重置所有activity
        ActivityManagerTools.getInstance().finishAllActivityExcept(activity);
        Intent i =new Intent(activity,activity.getClass());
        activity.startActivity(i);
        activity.finish();
    }
	//语言切换
	public  void refreshLanguage(Activity activity,String language) {
		Resources resources = context.getResources();
		Configuration configuration = resources.getConfiguration();
		configuration.locale = new Locale(language);
		Locale.setDefault(configuration.locale);
		DisplayMetrics displayMetrics = resources.getDisplayMetrics();
		resources.updateConfiguration(configuration, displayMetrics);
		//重置所有activity
		ActivityManagerTools.getInstance().finishAllActivityExcept(activity);
		Intent i =new Intent(activity,activity.getClass());
        activity.startActivity(i);
        activity.finish();
	}
}
