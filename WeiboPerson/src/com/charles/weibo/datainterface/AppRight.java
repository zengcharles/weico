package com.charles.weibo.datainterface;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.charles.weibo.Config.Config;
import com.charles.weibo.utils.HttpUtils;

public class AppRight {
	
	public static String EXPRESS_RIGHT="showExpressQuery";
	public static String DRUGS_RIGHT="showDrugsQuery";
	public static String GOODS_RIGHT="showGoodsQuery";
	public static String MAP_RIGHT="showMap";
	
	public static String EXPRESS_MSG="expressErrMsg";
	public static String DRUGS_MSG="drugsErrMsg";
	public static String GOODS_MSG="goodsErrMsg";
	public static String MAP_MSG="mapErrMsg";
	
	private String DEFAULT_MSG="该功能在维护升级中···";

	private Context mContext;

	private SharedPreferences preference;

	public AppRight(Context context) {
		this.mContext = context;
		preference = mContext.getSharedPreferences("appModuleRight",
				Context.MODE_PRIVATE);
	}

	public boolean isShowModule(String module) {
		return preference.getBoolean(module, true);
	}
	
	public String getModuleErrMsg(String msg) {
		return preference.getString(msg,DEFAULT_MSG);
	}

	public void setAppModuleRight() {
		HttpUtils utils = new HttpUtils(mContext);
		String result = utils.getStringDataByHttp(Config.appModuleRightUrl);
		/*
		 * 此处暂时使用硬编码用于开发
		 */
//		result = "{showExpressQuery:{isShow:true,errMsg:\"\"},showDrugsQuery:{isShow:true,errMsg:\"该功能在维护升级中···\"},showGoodsQuery:{isShow:false,errMsg:\"该功能在维护升级中···\"}, showMap:{isShow:true,errMsg:\"\"}}";
		Editor editor = preference.edit();
		if (null != result && !"".equalsIgnoreCase(result)) {
			try {
				JSONObject object = new JSONObject(result);
				try {
					JSONObject exrpressRight = object
							.getJSONObject(EXPRESS_RIGHT);
					if (null != object.getJSONObject(EXPRESS_RIGHT)) {
						editor.putBoolean(EXPRESS_RIGHT,
								exrpressRight.getBoolean("isShow"));
						editor.putString(EXPRESS_MSG,
								exrpressRight.getString("errMsg"));
					} else {
						editor.putBoolean(EXPRESS_RIGHT, false);
						editor.putString(EXPRESS_MSG, DEFAULT_MSG);
					}
				} catch (Exception e) {
					// TODO: handle exception
					editor.putBoolean(EXPRESS_RIGHT, true);
					editor.putString(EXPRESS_MSG, DEFAULT_MSG);
				}
				try {
					JSONObject exrpressRight = object
							.getJSONObject(DRUGS_RIGHT);
					if (null != object.getJSONObject(DRUGS_RIGHT)) {
						editor.putBoolean(DRUGS_RIGHT,
								exrpressRight.getBoolean("isShow"));
						editor.putString(DRUGS_MSG,
								exrpressRight.getString("errMsg"));
					} else {
						editor.putBoolean(DRUGS_RIGHT, false);
						editor.putString(DRUGS_MSG, DEFAULT_MSG);
					}
				} catch (Exception e) {
					// TODO: handle exception
					editor.putBoolean(DRUGS_RIGHT, true);
					editor.putString(DRUGS_MSG, DEFAULT_MSG);
				}
				try {
					JSONObject exrpressRight = object
							.getJSONObject(GOODS_RIGHT);
					if (null != object.getJSONObject(GOODS_RIGHT)) {
						editor.putBoolean(GOODS_RIGHT,
								exrpressRight.getBoolean("isShow"));
						editor.putString(GOODS_MSG,
								exrpressRight.getString("errMsg"));
					} else {
						editor.putBoolean(GOODS_RIGHT, false);
						editor.putString(GOODS_MSG, DEFAULT_MSG);
					}
				} catch (Exception e) {
					// TODO: handle exception
					editor.putBoolean(GOODS_RIGHT, true);
					editor.putString(GOODS_MSG, DEFAULT_MSG);
				}
				try {
					JSONObject exrpressRight = object.getJSONObject(MAP_RIGHT);
					if (null != object.getJSONObject(MAP_RIGHT)) {
						editor.putBoolean(MAP_RIGHT,
								exrpressRight.getBoolean("isShow"));
						editor.putString(MAP_MSG,
								exrpressRight.getString("errMsg"));
					} else {
						editor.putBoolean(MAP_RIGHT, false);
						editor.putString(MAP_MSG, DEFAULT_MSG);
					}
				} catch (Exception e) {
					// TODO: handle exception
					editor.putBoolean(MAP_RIGHT, true);
					editor.putString(MAP_MSG, DEFAULT_MSG);
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				setAllRightToFalse(editor);
			}
		} else {
			setAllRightToFalse(editor);
		}
		editor.commit();
	}
	
	private void setAllRightToFalse(Editor editor){
		editor.putBoolean(EXPRESS_RIGHT, true);
		editor.putString(EXPRESS_MSG, DEFAULT_MSG);
		editor.putBoolean(DRUGS_RIGHT, true);
		editor.putString(DRUGS_MSG, DEFAULT_MSG);
		editor.putBoolean(GOODS_RIGHT, true);
		editor.putString(GOODS_MSG, DEFAULT_MSG);
		editor.putBoolean(MAP_RIGHT, true);
		editor.putString(MAP_MSG, DEFAULT_MSG);
	}

}
