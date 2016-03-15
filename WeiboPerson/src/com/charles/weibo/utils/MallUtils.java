package com.charles.weibo.utils;

import java.util.HashMap;

public class MallUtils {

	private static HashMap<String, String> statusMap = new HashMap<String, String>(){
		{
		put("10", "已提交");
		put("11","待付款");
		put("20","待发货");
		put("30","已发货");
		put("40","已完成");
		put("0","已取消");
		}
		
	};
	
	//返回状态文字
	public static String GetStatusString(String status){
		return statusMap.get(status);
	}
	
	//计算运费
	public static String CountShippingFee(float first_price,int quantity,float step_price){
		float total = first_price+(quantity - 1)*step_price;
		return String.valueOf(total);
	}
	
	public static String CountTotalPrice(float goodsprice,float rebate,float shippingfee){
		float total = goodsprice-rebate+shippingfee;
		return String.valueOf(total);
	}
}
