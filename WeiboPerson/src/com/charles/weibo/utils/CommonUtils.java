package com.charles.weibo.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

import com.charles.weibo.R;
import com.charles.weibo.ui.CommonAlertDialog;

public class CommonUtils {
	
	
	/** 
     * 判别手机是否为正确手机号码； 
    *号码段分配如下： 
    *移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188  
    *联通：130、131、132、152、155、156、185、186  
    *电信：133、153、180、189、（1349卫通） 
     */  
	 public static boolean isMobileNum(Context context,String mobiles)  
     {  
//	  String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
//      Pattern p = Pattern.compile(regExp);  
//      Matcher m = p.matcher(mobiles);    
//      return m.find(); 
		boolean isMobile = false;
		String[] mobile = context.getResources().getStringArray(R.array.isMobile);
		for (int i = 0; i < mobile.length; i++) {
			if(mobiles.startsWith(mobile[i])&&mobiles.length()==11){
				isMobile = true;
				break;
			}
		}
		return isMobile;
     }
	 
	 
	 /**
	  * 验证邮箱地址是否正确
	  * @param email
	  * @return
	  */
	 public static boolean checkEmail(String email){
	   String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
	   Pattern regex = Pattern.compile(check);
	   Matcher matcher = regex.matcher(email);
	  return matcher.find();
	 }
	
	 /**
	 * 字符串转换成日期
	 * @param str
	 * @return date
	 */
	 public static Date StrToDate(String str) {
	   
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = null;
	    try {
	     date = format.parse(str);
	    } catch (ParseException e) {
	     e.printStackTrace();
	    }
	    return date;
	 }
	 
	 public static Date StrToDate1(String str) {
		   
		    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
		    Date date = null;
		    try {
		     date = format.parse(str);
		    } catch (ParseException e) {
		     e.printStackTrace();
		    }
		    return date;
		 }
	 
	 /**
		 * 日期转换成字符串
		 */
	 public static String timestampToString(Date date ) {
			String time = "";
			try{
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		      //  Date date = new java.util.Date();
				time = sdf.format(date);
			}catch(Exception e){
				e.printStackTrace();
				time = "";
			}
			return time;
		}
	 
	/**
	 * 时间戳转字符串
	 * @param cc_time 时间戳
	 * @param pattern 匹配规则
	 * @return
	 */
	public static String getStrTime(String cc_time,String pattern){
		String ret_cc_time = null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		long lcc_time = Long.parseLong(cc_time)*1000L;
		ret_cc_time = sdf.format(new Date(lcc_time));
		return ret_cc_time;
	}
	
	/**
	 * 简单提示框
	 * @param context
	 * @param title
	 * @param content
	 */
	public static void showSimpleAlertDlalog(Context context,String title,String content){
		new CommonAlertDialog.Builder(context)
        .setTitle(title)
        .setMessage(content)
        .setMiddleButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // User clicked OK so do some stuff 
            	dialog.dismiss();
            }
        }).create().show();
	}
	
	//从asset中安装apk
	public boolean retrieveApkFromAssets(Context context, String fileName,
			String path) {
		boolean isRetrieve = false;
		InputStream input = null;
		FileOutputStream output = null;
		try {
			input = context.getAssets().open(fileName);
			File file = new File(path);
			file.createNewFile();
			output = new FileOutputStream(file);
			byte[] temp = new byte[1024];
			int i = 0;
			while ((i = input.read(temp)) > 0) {
				output.write(temp, 0, i);
			}
			isRetrieve = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return isRetrieve;
	}
	
	//检查网络状态并弹出提示框
	public static void checkNetworkAndDialog(final Context context){
		//检查网络状态
		if(!NetWorkInfoUtil.isNetworkAvailableAndConnected(context)){
			new CommonAlertDialog.Builder(context)
			.setTitle("提示")
			.setMessage("请检查网络是否正常，移动数据或者Wifi是否开启")
			.setLeftButton("打开设置", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					Intent intent=null;
	                if(android.os.Build.VERSION.SDK_INT>10){
	                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
	                }else{
	                    intent = new Intent();
	                    ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
	                    intent.setComponent(component);
	                    intent.setAction("android.intent.action.VIEW");
	                }
	                ((Activity)context).startActivity(intent);

				}
			})
			.setRightButton("取消", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			}).create().show();
			return;
		}
	}
	
	
	//检测是否登录
	/*public static void checkLoginAndGo(Context context){
		
		MemberUtils memberUtils = new MemberUtils(context);
		if(!memberUtils.checkLogin()){
			Intent it = new Intent();
			it.setClass(context, MemberLoginActivity.class);
			context.startActivity(it);
			return;
		}
	}*/
	
	//根据支付方式代码获取支付方式中文名
	/*public static String getPaymentName(String payment_code){
		if(!"".equals(payment_code)){
			if("upop".equalsIgnoreCase(payment_code)){
				return Config.UPOP;
			}else if("alipay".equalsIgnoreCase(payment_code)){
				return Config.ALIPAY;
			}else if("userpay".equalsIgnoreCase(payment_code)){
				return Config.USERPAY;
			}else{
				return "";
			}
		}
		return "";
	}*/
	
	//计算星期几    i 代表当前日期要加的天数，然后返回该日期的星期几,i=0即返回当前日期的星期几
	public  static String getWeek(int i){    
		String week=null;
		SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.add(c.DAY_OF_MONTH, i);
		Date d = new Date();
		d = c.getTime();
		String[] times = (time.format(d)).split("-");
		int year = Integer.parseInt(times[0]);
		int month = Integer.parseInt(times[1]);
		int day = Integer.parseInt(times[2]);
		Calendar calendar = Calendar.getInstance();// 获得一个日历
		calendar.set(year, month - 1, day);// 设置当前时间,月份是从0月开始计算
		int number = calendar.get(Calendar.DAY_OF_WEEK);// 星期表示1-7，是从星期日开始，
		String[] str = { "", "周日", "周一", "周二", "周三", "周四", "周五", "周六", };
		week=str[number];
		return week;
	}
	
	
	
}
