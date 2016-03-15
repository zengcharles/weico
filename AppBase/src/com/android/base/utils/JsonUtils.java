package com.android.base.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	public static int ASC =0;
	public static int DESC =1;
	
	public JSONArray sortJsonArray(JSONArray arr, String sortColumn,final int asc) {
		JSONArray result = new JSONArray();
		try {
			ArrayList<SortObject> list = new ArrayList<SortObject>();
			for (int i = 0; i < arr.length(); i++) {
				SortObject object = new SortObject();

				object.sortColumn = Integer.valueOf(arr.getJSONObject(i)
						.getInt(sortColumn));
				object.sortContent = arr.getJSONObject(i);
				list.add(object);
			}
			Collections.sort(list, new Comparator<SortObject>() {

				@Override
				public int compare(SortObject lhs, SortObject rhs) {
					// TODO Auto-generated method stub
					if(asc==ASC){
						return Integer.valueOf(lhs.sortColumn)-Integer.valueOf(rhs.sortColumn);
					}else{
						return Integer.valueOf(rhs.sortColumn)-Integer.valueOf(lhs.sortColumn);
					}
					
				}
			});
			for (int i = 0; i < list.size(); i++) {
				result.put(list.get(i).sortContent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		if(null ==result || result.length() ==0){
			return arr;
		}else{
			return result;
		}
		
	}

	 public class SortObject{
		 public int sortColumn ;
		 public JSONObject sortContent;
	 }
	 
}
