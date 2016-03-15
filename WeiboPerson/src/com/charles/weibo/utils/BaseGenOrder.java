package com.charles.weibo.utils;

import android.content.Context;
import android.content.Intent;


/**
 * 生成订单类
 * @author Administrator
 *
 */
public abstract class BaseGenOrder {

	private Context mContext;
	public String productName; //商品名称
	public String productId; //商品id
	public String productNum; //商品数量
	
	public BaseGenOrder(Context mContext) {
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
	}
	
	public String GenOrderNumber(){
		return null;
	}
	
	public void goToPay(){
		Intent it = new Intent();
	}
}
