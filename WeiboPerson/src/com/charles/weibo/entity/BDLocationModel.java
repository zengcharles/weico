package com.charles.weibo.entity;

import java.io.Serializable;

public class BDLocationModel implements Serializable   {
	public double latitude;
	public double longitude;

	public BDLocationModel() {
		// TODO Auto-generated constructor stub
	}

	public double getlatitude() {
		return this.latitude;
	}

	public void setlatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getlongitude() {
		return this.longitude;
	}

	public void setlongitude(double longitude) {
		this.longitude = longitude;
	}
}
