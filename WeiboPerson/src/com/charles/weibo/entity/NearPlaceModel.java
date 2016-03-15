package com.charles.weibo.entity;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class NearPlaceModel  implements Serializable {
	private String phone ;
	private String poi_street_address;
	private String herenow_user_num;
	private String city;
	private String poiid;
	private int distance;
	private int selected;
	private String title;
	private String photo_num;
	private String map;
	private String province;
	private String lat;
	private String icon;
	private String checkin_num;
	private String poi_pic;
	private String lon ;
	private String trend ;
	private String icon_show ;
	private String enterprise ;
	private String checkin_user_num ;
	private String tip_num ;
	private String postcode ;
	private String todo_num ;
	private String country ;
	private String weibo_id ;
	private String category ;
	private String categorys ;
	private String category_name ;
	private String address ;
	
	public NearPlaceModel(){
		
	}
	public  NearPlaceModel(JSONObject obj ){
		try {
			this. poi_street_address = obj.getString("poi_street_address");
			this.herenow_user_num = obj.getString("herenow_user_num");
			this.title=obj.getString("title");
			this.address = obj.getString("address");
			this.lat = obj.getString("lat") ;
			this.lon = obj.getString("lon");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPoi_street_address() {
		return poi_street_address;
	}
	public void setPoi_street_address(String poi_street_address) {
		this.poi_street_address = poi_street_address;
	}
	public String getHerenow_user_num() {
		return herenow_user_num;
	}
	public void setHerenow_user_num(String herenow_user_num) {
		this.herenow_user_num = herenow_user_num;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPoiid() {
		return poiid;
	}
	public void setPoiid(String poiid) {
		this.poiid = poiid;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public int getSelected() {
		return selected;
	}
	public void setSelected(int selected) {
		this.selected = selected;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPhoto_num() {
		return photo_num;
	}
	public void setPhoto_num(String photo_num) {
		this.photo_num = photo_num;
	}
	public String getMap() {
		return map;
	}
	public void setMap(String map) {
		this.map = map;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCheckin_num() {
		return checkin_num;
	}
	public void setCheckin_num(String checkin_num) {
		this.checkin_num = checkin_num;
	}
	public String getPoi_pic() {
		return poi_pic;
	}
	public void setPoi_pic(String poi_pic) {
		this.poi_pic = poi_pic;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getTrend() {
		return trend;
	}
	public void setTrend(String trend) {
		this.trend = trend;
	}
	public String getIcon_show() {
		return icon_show;
	}
	public void setIcon_show(String icon_show) {
		this.icon_show = icon_show;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	public String getCheckin_user_num() {
		return checkin_user_num;
	}
	public void setCheckin_user_num(String checkin_user_num) {
		this.checkin_user_num = checkin_user_num;
	}
	public String getTip_num() {
		return tip_num;
	}
	public void setTip_num(String tip_num) {
		this.tip_num = tip_num;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getTodo_num() {
		return todo_num;
	}
	public void setTodo_num(String todo_num) {
		this.todo_num = todo_num;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getWeibo_id() {
		return weibo_id;
	}
	public void setWeibo_id(String weibo_id) {
		this.weibo_id = weibo_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategorys() {
		return categorys;
	}
	public void setCategorys(String categorys) {
		this.categorys = categorys;
	}
	public String getCategory_name() {
		return category_name;
	}
	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
