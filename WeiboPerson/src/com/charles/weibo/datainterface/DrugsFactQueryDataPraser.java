package com.charles.weibo.datainterface;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class DrugsFactQueryDataPraser implements HttpQuery {
	
	private Context context;
	

	public DrugsFactQueryDataPraser(Context context) {
		super();
		this.context = context;
	}

	HttpUtils utils;

	private String requestDataByHttpPost(HashMap<String, Object> map) {
		utils = new HttpUtils(context);
		String result = utils.getStringDataByHttp(Config.drugsFactAction, map);
		/*
		 * 此处暂时使用硬编码用于开发
		 */
//		result ="{\"status\":\"ok\",\"errMsg\":\"\",\"drugsValidateInfo\":{\"validateType\":\"1\",\"validateResults\":{\"resultUrl\":\"\",\"resultInfo\":{\"companyInfo\":{\"companyName\":\"广东华南药业集团有限公\",\"companyAddr\":\"广东省东莞市石龙镇西湖工业区信息产业园\",\"FirmStatus\":\"有效\"},\"drugsInfo\":{\"barcode\":\"6910942381270\"}}}}}";
//		result = "{\"status\":\"ok\",\"errMsg\":\"\",\"drugsValidateInfo\":{\"validateType\":\"0\",\"validateResults\":{\"resultUrl\":\"http://mobile.9500.cn/95app/druginfo/ShowResultforAndroid.jsp?code=81349040033513111096&imei=354356056492083&type=CODE_128\",\"resultInfo\":{\"companyInfo\":{},\"drugsInfo\":{}}}}}";
		if (null != result && !"".equalsIgnoreCase(result)) {
			return result;
		} else {
			return Config.drugsFactErrorMsg;
		}
	}

	@Override
	public JSONObject query(HashMap<String, Object> map) {
		// TODO Auto-generated method stub
		String result = requestDataByHttpPost(map);
		try {
			return new JSONObject(result);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				return new JSONObject(Config.drugsFactErrorMsg);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
			}
		}
		return null;
	}

	@Override
	public void httpCancel() {
		// TODO Auto-generated method stub
		if (null != utils) {
			utils.disconnectRequest();
		}
	}

}
