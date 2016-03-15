package com.android.base.net;

import java.util.HashMap;

import org.json.JSONObject;

public interface IHttpQuery {
	public JSONObject query(int requestCode,HashMap<String,Object> map);
	public void queryCancel();
}
