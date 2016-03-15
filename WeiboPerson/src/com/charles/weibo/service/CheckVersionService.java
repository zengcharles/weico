package com.charles.weibo.service;

import android.app.IntentService;
import android.content.Intent;

public class CheckVersionService extends IntentService{

	/** 任务参数名称 */
	public final static String KEY_TASK = "task";

	/** 检测版本任务 */
	public final static int TASK_CHECK = 100;


	/* 检测版本状态码 */
	/** 开始版本检测 */
	public final static int STATUS_CHECK_START = 0;
	/** 版本检测中 */
	public final static int STATUS_IS_CHECKING = 1;
	/** 已是最新版本 */
	public final static int STATUS_IS_LATEST_VERSION = 2;
	/** 可以升级 */
	public final static int STATUS_CAN_UPDATE = 3;
	/** 强制升级（重大升级导致当前版本已不可用） */
	public final static int STATUS_MUST_UPDATE = 4;
	/** 检测失败 */
	public final static int STATUS_CHECK_FAILED = 5;
	
	public CheckVersionService() {
		super("CheckVersionService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		
	}
}
