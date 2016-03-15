package com.charles.weibo.datainterface;

import java.util.HashMap;

import org.json.JSONObject;

public interface HttpQuery {
	public JSONObject query(HashMap<String,Object> map);
	public void httpCancel();
}
