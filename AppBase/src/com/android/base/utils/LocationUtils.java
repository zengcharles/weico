package com.android.base.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtils {
	public Context context;

	public LocationUtils(Context mContext) {
		this.context = mContext;
	}

	public double[] getLocationByGPS() {

		// 获取位置管理服务
		LocationManager locationManager;
		String serviceName = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) context
				.getSystemService(serviceName);
		// 查找到服务信息
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE); // 高精度
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW); // 低功耗

		String provider = locationManager.getBestProvider(criteria, true); // 获取GPS信息
		Location location = locationManager.getLastKnownLocation(provider); // 通过GPS获取位置
		//Latitude 经度  ， Longitude 纬度
		return new double[] { location.getLatitude(), location.getLongitude() };
	};

}
