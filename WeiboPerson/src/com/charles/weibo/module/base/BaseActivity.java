package com.charles.weibo.module.base;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.base.config.AppConfig;
import com.android.base.exception.handler.GlobalExceptionHandler;
import com.android.base.imageCache.ImageManager;
import com.android.base.log.LogManager;
import com.charles.weibo.http.HttpUtil;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.utils.ACache;
import com.charles.weibo.utils.DialogUtil;
import com.charles.weibo.utils.StringUtils;
import com.charles.weibo.wedget.slideingactivity.IntentUtils;
import com.charles.weibo.wedget.slideingactivity.SlidingActivity;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;


public abstract class BaseActivity extends SlidingActivity implements OnClickListener {
    /** Called when the activity is first created. */
	//是否需要在标题栏显示进度条
	public boolean needTitleProgressBar =false;
	//标题栏进度条样式，圆形时为false
	public boolean isCycleStyle =false;
	//是否立即显示进度条
	public boolean isViewIndeterminate =false;
	//是否是使用xml显示布局界面
	public boolean isXmlLayout =true;
	// 屏幕参数 
	public DisplayMetrics mDisplayMetrics = null;
	//全屏设置参数
	public boolean isFullScreen =false;
	//设置是否需要标题栏
	public boolean isNoActivityTitle=true;
	//activity管理工具
	private ActivityManagerTools activityTools=ActivityManagerTools.getInstance();
	//统一日志工具
	public static LogManager logUtils =new LogManager();
	
	public ImageManager imageManager=null;
	
	private Dialog progressDialog;
	
	//微博验证token
	public Oauth2AccessToken mAccessToken ; 
	
	/**
	 * 增加全局网络状态获取监听器（未添加）
	 */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccessToken = AccessTokenKeeper.readAccessToken(this); 
        writeTraceLog("============onCreate===========");
        //初始化默认参数
        initAppParams();
        if(activityTools.activityLaunchMode.size() <=0){
        	activityTools.activityLaunchMode.clear();
        	activityTools.initActivitysLaunchMode(this);
        }
        activityTools.addActivityToStack(this);
        
        if (!AppConfig.isDebug) {
			// 程序异常统一处理机制
			errorProcess();
		}
        
        if(needTitleProgressBar&&!isNoActivityTitle){
        	if(isCycleStyle){
        		//圆形
        		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        	}else{
        		/**
            	 * 注：
            	 * 长条形标题栏进度条的进度大小默认设置为  0..10000
            	 */
        		//长条形
        		requestWindowFeature(Window.FEATURE_PROGRESS);
        	}
        }
        // 判断是否要acitivity 标题栏 true 为不要Title ，false 为要他title栏。默认true不要Title栏。
     	if (isNoActivityTitle) {
     		requestWindowFeature(Window.FEATURE_NO_TITLE);
     	}
        // 判断是否全屏 true为全屏 false为不全屏
 		if (isFullScreen) {
 			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
 					WindowManager.LayoutParams.FLAG_FULLSCREEN);
 		}
 		/** 初始化设备屏幕参数 */
 		mDisplayMetrics = new DisplayMetrics();
 		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
 		
 		if(isXmlLayout){
        	setContentView(setLayoutId());
        }else{
        	setContentView(setLayoutView());
        }
        //需要进度条，且需要立即显示时先显示
        if(needTitleProgressBar&&isViewIndeterminate){
        	setProgressBarIndeterminateVisibility(needTitleProgressBar);
        }
        
        imageManager =new ImageManager(this);
        //初始化界面组件
        writeTraceLog("============before init cmp===========");
        initCmp();
        //初始化界面监听器
        writeTraceLog("============before init listener===========");
        initListener();
        //初始化数据
        writeTraceLog("============before init data===========");
        initData();
    }
    
    /**
	 * 处理程序崩溃跳转到默认的Activity
	 */
	private final void errorProcess() {
		Thread.currentThread().setUncaughtExceptionHandler(GlobalExceptionHandler.getInstance(this));
	}
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		writeTraceLog("============onDestroy===========");
		activityTools.removeActivityFromStack(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		writeTraceLog("============onPause===========");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		writeTraceLog("============onResume===========");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		writeTraceLog("============onStart===========");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		writeTraceLog("============onStop===========");
	}
	
	
	
	/**
     * 用于设置每个activity通过xml构建的界面
     * @return
     */
    abstract public int setLayoutId();
    /**
     * 初始化布局组件
     */
    abstract public void initCmp();
    
    /**
     * 初始化简单数据
     */
    abstract public void initData();
    
    /**
     * 初始化监听器
     */
    abstract public void initListener();
	/**
     * 用于设置默认参数 如 是否设置标题栏进度条，
     * 标题栏进度条样式  是否使用xml进行布局 等默认的父类参数
     * @return
     */
    abstract public void initAppParams();


    /**
     * 用于设置每个activity通过代码构建的界面
     * @return
     */
    abstract public View setLayoutView();
    
    /**
	 * 退出程序操作
	 */
	public void exitApp() {
		logUtils.printLogInfo("ExitApp", this.getClass().getSimpleName().toString()+"============ExitApp===========");
		activityTools.exit();
		android.os.Process.killProcess(android.os.Process.myPid());
		finish();
		System.exit(0);
	}
    
    /**
     * 使用长条形进度条时，可用该方法进行更新进度
     */
    public void updateTitleProgress(int pro){
    	setProgress(100 * pro);
    }
    
    /**
     * 日志追踪器，主要用于跟踪应用进行情况
     * @param msg
     */
    public void writeTraceLog(String msg){
    	logUtils.printLogInfo(this.getClass().getSimpleName().toString(), this.getClass().getSimpleName().toString()+msg);
    	if(AppConfig.writeRealTimeLogToSDCard){
    		logToSDCardByAppend("appLog", msg);
    	}
    }
    
    public void wiriteExceptionLog(Exception e){
    	if(null!=e){
    		logUtils.printLogError(this.getClass().getSimpleName().toString(), this.getClass().getSimpleName().toString()+ e.toString());
    		if(AppConfig.writeRealTimeLogToSDCard){
    			logToSDCardByAppend("ExceptionLog",e.toString());
    		}
        	StackTraceElement[] elements =e.getStackTrace();
            for (int i = 0; i < elements.length; i++) {
            	logUtils.printLogError(this.getClass().getSimpleName().toString(), this.getClass().getSimpleName().toString()+ elements[i].toString());
            	if(AppConfig.writeRealTimeLogToSDCard){
            	   logToSDCardByAppend("ExceptionLog", elements[i].toString());
            	}
        	}
    	}
    }
    
    public void logToSDCard(String fileName,String msg){
    	logUtils.writeLogToSdCard(this, fileName, msg);
    }
    
    public void logToSDCardByAppend(String fileName,String msg){
    	String message = msg +"\n";
    	logUtils.appendLogToSdCard(this, fileName, message);
    }
    
    
    
    //------------------------------------------------------------------
    
    /**
     * 判断是否有网络
     * 
     * @return
     */
    public boolean hasNetWork() {
        return HttpUtil.isNetworkAvailable(this);
    }
    
    /**
     * 显示LongToast
     */
    public void showShortToast(String pMsg) {
        // ToastUtil.createCustomToast(this, pMsg, Toast.LENGTH_LONG).show();
        Toast.makeText(this, pMsg, Toast.LENGTH_SHORT).show();
    }
    
	/**
	 * 设置缓存数据（key,value）
	 */
	public void setCacheStr(String key, String value) {
		if (!StringUtils.isEmpty(value)) {
			ACache.get(this).put(key, value);
		}
	}
	
	  /**
     * 获取缓存数据根据key
     */
    public String getCacheStr(String key) {
        return ACache.get(this).getAsString(key);
    }
    
    /**
     * 显示dialog
     * 
     * @param msg 显示内容
     */
    public void showProgressDialog() {
        try {

            if (progressDialog == null) {
                progressDialog = DialogUtil.createLoadingDialog(this);

            }
            progressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 隐藏dialog
     */
    public void dismissProgressDialog() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * dialog是否显示
     */
    public boolean isShow() {
        try {

            if (progressDialog != null && progressDialog.isShowing()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * 更具类打開citvity
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0);

    }

    public void openActivityForResult(Class<?> pClass, int requestCode) {
        openActivity(pClass, null, requestCode);
    }

    /**
     * 根据类打开citvity,并携带参数     */
    public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        if (requestCode == 0) {
            IntentUtils.startPreviewActivity(this, intent, 0);
            // startActivity(intent);
        } else {
            IntentUtils.startPreviewActivity(this, intent, requestCode);
            // startActivityForResult(intent, requestCode);
        }
        // actityAnim();
    }
}