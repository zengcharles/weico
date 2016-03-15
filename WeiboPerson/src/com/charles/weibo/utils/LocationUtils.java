package com.charles.weibo.utils;

import java.util.Iterator;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.android.base.callback.ICallBack;
import com.android.base.log.LogManager;

public class LocationUtils {
	public Context context;

	public ICallBack callBack;

	LogManager manager =new LogManager();
	public LocationUtils(Context mContext) {
		this.context = mContext;
	}

	public void setCallBackListener(ICallBack callBack) {
		this.callBack = callBack;
	}

	private LocationManager lm;

	public void closeGPS() {
		manager.printLogInfo("LocationUtils", "closeGPS");
		// TODO Auto-generated method stub
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lm.removeUpdates(locationListener);
		openOrCloseGPSModule();
	}

	// 此段代码是得到当前GPS的状态 （开启 true 关闭 false）

	public boolean isGPSEnable(Context context) {

		String str = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}

	public void openOrCloseGPSModule() {
		manager.printLogInfo("LocationUtils", "openOrCloseGPSModule");
		// 自动开启GPS代码
		/**
		 * 需配置权限 <uses-permission
		 * android:name="android.permission.WRITE_SETTINGS" /> <uses-permission
		 * android:name="android.permission.WRITE_SECURE_SETTINGS" />
		 */
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}

	}

	public void getLocationByGPS() {
		manager.printLogInfo("LocationUtils", "getLocationByGPS");
		lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);

		// 为获取地理位置信息时设置查询条件
		String bestProvider = lm.getBestProvider(getCriteria(), true);
		// 获取位置信息
		// 如果不设置查询要求，getLastKnownLocation方法传人的参数为LocationManager.GPS_PROVIDER
		Location location = lm.getLastKnownLocation(bestProvider);
		if (location == null) {
			manager.printLogInfo("LocationUtils", "can't get location");
		} else {
			manager.printLogInfo("LocationUtils", "location is:"+location.getLatitude() + ","
					+ location.getLongitude());
			callBack.call(location.getLatitude() + ","
					+ location.getLongitude());
		}
		// 监听状态
		lm.addGpsStatusListener(listener);
		// 绑定监听，有4个参数
		// 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种
		// 参数2，位置信息更新周期，单位毫秒
		// 参数3，位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
		// 参数4，监听
		// 备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新

		// 1秒更新一次，或最小位移变化超过1米更新一次；
		// 注意：此处更新准确度非常低，推荐在service里面启动一个Thread，在run中sleep(10000);然后执行handler.sendMessage(),更新位置
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1,
				locationListener);
	};

	private Criteria getCriteria() {
		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		// 设置是否要求速度
		criteria.setSpeedRequired(false);
		// 设置是否允许运营商收费
		criteria.setCostAllowed(false);
		// 设置是否需要方位信息
		criteria.setBearingRequired(false);
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(false);
		// 设置对电源的需求
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	// 位置监听
	private LocationListener locationListener = new LocationListener() {

		/**
		 * 位置信息变化时触发
		 */
		public void onLocationChanged(Location location) {
			manager.printLogInfo("LocationUtils", "update location to:"+location.getLatitude() + ","
					+ location.getLongitude());
			callBack.call(location.getLatitude() + ","
					+ location.getLongitude());
			// updateView(location);
			// Log.i(TAG, "时间："+location.getTime());
			// Log.i(TAG, "经度："+location.getLongitude());
			// Log.i(TAG, "纬度："+location.getLatitude());
			// Log.i(TAG, "海拔："+location.getAltitude());
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				// Log.i(TAG, "当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				// Log.i(TAG, "当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// Log.i(TAG, "当前GPS状态为暂停服务状态");
				break;
			}
		}

		/**
		 * GPS开启时触发
		 */
		public void onProviderEnabled(String provider) {
			Location location = lm.getLastKnownLocation(provider);
			callBack.call(location.getLatitude() + ","
					+ location.getLongitude());
		}

		/**
		 * GPS禁用时触发
		 */
		public void onProviderDisabled(String provider) {

		}

	};

	// 状态监听
	GpsStatus.Listener listener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			switch (event) {
			// 第一次定位
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				// Log.i(TAG, "第一次定位");
				break;
			// 卫星状态改变
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				// Log.i(TAG, "卫星状态改变");
				// 获取当前状态
				GpsStatus gpsStatus = lm.getGpsStatus(null);
				// 获取卫星颗数的默认最大值
				int maxSatellites = gpsStatus.getMaxSatellites();
				// 创建一个迭代器保存所有卫星
				Iterator<GpsSatellite> iters = gpsStatus.getSatellites()
						.iterator();
				int count = 0;
				while (iters.hasNext() && count <= maxSatellites) {
					GpsSatellite s = iters.next();
					count++;
				}
				System.out.println("搜索到：" + count + "颗卫星");
				break;
			// 定位启动
			case GpsStatus.GPS_EVENT_STARTED:
				// Log.i(TAG, "定位启动");
				break;
			// 定位结束
			case GpsStatus.GPS_EVENT_STOPPED:
				// Log.i(TAG, "定位结束");
				break;
			}
		};
	};
}
