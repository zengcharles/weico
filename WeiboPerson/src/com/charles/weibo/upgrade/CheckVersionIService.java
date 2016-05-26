package com.charles.weibo.upgrade;

import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.charles.weibo.Config.Config;
import com.charles.weibo.http.HttpUtil;
import com.charles.weibo.utils.StringUtils;

public class CheckVersionIService extends IntentService {

	private String checkVersionUrl = null;
	public static final int STATUS_CHECKING = 0;
	public static final int STATUS_ERROR = 1;
	public static final int STATUS_NO_UPGRADE = 2;
	public static final int STATUS_CAN_UPGRADE = 3;
	public static final int STATUS_MUST_UPGRADE = 4;
	public static final int STATUS_LOAD_SELECT_IMAGE_DONE = 5;

	public CheckVersionIService() {
		super("CheckVersionIService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		Bundle b = new Bundle();

		checkVersionUrl = Config.UPDATE_APP_URL;
		getAppInfo(this);

		/* 檢測  更新 */
		if (checkVersionUrl != null) {
			try {
				String _strResult ="{\"AppInfo\":{\"newAppName\":\"新版本\",\"newVerName\": \"1.00.01\",\"newAppVerCode\": 2,\"InstallAddress\": \"http://125.88.125.201:877/themes/softs/iCare.apk\"}}";
				//	String _strResult = HttpUtil.getInfoPost(checkVersionUrl, null);
				
				// ***************** 檢測版本 *******************/
				if (!StringUtils.isEmpty(_strResult)) {
					/* 获得新版本号和新版Url地址 */
					JSONObject appObject = new JSONObject(_strResult);
					JSONObject obj = appObject.getJSONObject("AppInfo");
					String _appsVerName = obj.getString("newVerName");
					String _appsVerCode = obj.getString("newAppVerCode");
					String _installAddress = obj.getString("InstallAddress");

					Config.newVersionName = _appsVerName;
					Config.newVersionCode = _appsVerCode ;
					Config.APK_URL = _installAddress;

					/* 判断是否需要下载 */
					if (!StringUtils.isEmpty(Config.newVersionName)&&!StringUtils.isEmpty(Config.newVersionCode)) {

						// 由 x.xx.xx 拆分成3个整数
						// 如果用“.”作为分隔的话，必须是如下写法：String.split("\\."),这样才能正确的分隔开，不能用String.split(".");
						String[] newVerNumArray = Config.newVersionName.split("\\.");
						String[] oldVerNumArray = Config.oldVersionName.split("\\.");	
						
						if (Integer.parseInt(newVerNumArray[0]) > Integer
								.parseInt(oldVerNumArray[0])) {
							// 强制更新
							b.putString("newVersionName", Config.newVersionName);
							receiver.send(STATUS_MUST_UPGRADE, b);

						} else if (Integer.parseInt(newVerNumArray[0]) < Integer
								.parseInt(oldVerNumArray[0])) {
							// 已是最新
							receiver.send(STATUS_NO_UPGRADE, b);
						} else if (Integer.parseInt(newVerNumArray[1]) > Integer
								.parseInt(oldVerNumArray[1])) {
							// 强制更新
							b.putString("newVersionName", Config.newVersionName);
							receiver.send(STATUS_MUST_UPGRADE, b);

						} else if (Integer.parseInt(newVerNumArray[1]) < Integer
								.parseInt(oldVerNumArray[1])) {
							// 已是最新
							receiver.send(STATUS_NO_UPGRADE, b);
						}else if (Integer.parseInt(newVerNumArray[2])>Integer.parseInt(oldVerNumArray[2])) {
							// 不强制更新
							b.putString("newVersionName", Config.newVersionName);
							receiver.send(STATUS_CAN_UPGRADE, b);

						} else {
							// 已是最新
							receiver.send(STATUS_NO_UPGRADE, b);
						}
					}

				} else {
					receiver.send(STATUS_ERROR, b);
				}
			} catch (Exception e) {
				e.printStackTrace();
				receiver.send(STATUS_ERROR, b);
			}
		}
		this.stopSelf();
	}

	public static void getAppInfo(Context pContext) {
		try {
			PackageInfo info = pContext.getPackageManager().getPackageInfo(
					pContext.getPackageName(), 0);
			Config.oldVersion = info.versionCode;
			Config.oldVersionName = info.versionName;
			Config.appName = info.applicationInfo.loadLabel(
					pContext.getPackageManager()).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
}
