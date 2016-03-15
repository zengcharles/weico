package com.charles.weibo.utils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.charles.weibo.entity.BDLocationModel;

public class GPSLocationUtils {

	private static Context context;
	private static LocationClient mLocationClient = null;
	public static BDLocationModel station = new BDLocationModel();
	private static MyBDListener listener = new MyBDListener();

	public GPSLocationUtils(Context c) {
		// TODO Auto-generated constructor stub

		this.context = c;
		mLocationClient = new LocationClient(context);

	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.stop();
			mLocationClient = null;
		}
	}

	/**
	 * 更新位置并保存到SItude中
	 */
	public void updateListener() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();

		}
	}

	/**
	 * 获取经纬度信息
	 * 
	 * @return
	 */
	public BDLocationModel getLocation() {
		return station;
	}

	// 请求地理位置
	public void startLocation() {
		mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);
		
		option.setScanSpan(5000);
		option.setNeedDeviceDirect(false);
		option.setIsNeedAddress(false);
		option.setOpenGps(true); // 打开gps
		option.setCoorType("gcj02"); // 设置坐标类型为bd09ll
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		
		mLocationClient.setLocOption(option);
		mLocationClient.registerLocationListener(listener);
		mLocationClient.start();// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
	}

	private static class MyBDListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location.getCity() == null) {
				int type = mLocationClient.requestLocation();

			}
			station.latitude = location.getLatitude();
			station.longitude = location.getLongitude();
			//Toast.makeText(context, "定位结果："+station.latitude+","+station.longitude, Toast.LENGTH_LONG).show();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			// return
		}

	}


}
