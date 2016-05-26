
package com.charles.weibo.http.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.sax.StartElementListener;

import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;

public class CommentListJson extends JsonPacket {

    public static CommentListJson newListJson;

    public ArrayList<CommentModel> newModles;

    public CommentListJson(Context context) {
        super(context);
    }

    public static CommentListJson instance(Context context) {
        if (newListJson == null) {
            newListJson = new CommentListJson(context);
        }
        return newListJson;
    }

    public ArrayList<CommentModel> readJsonCommentModels(String res) {
        newModles = new ArrayList<CommentModel>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            CommentModel newModle = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i = 1; i < jsonArray.length(); i++) {
                newModle = new CommentModel();
                JSONObject obj = jsonArray.optJSONObject(i);
                newModle.setCreated_at(obj.getString("created_at"));
                newModle.setId(obj.getLong("id"));
                newModle.setText(obj.getString("text"));
                newModle.setSource(obj.getString("source"));
				JSONObject userObject = obj.optJSONObject("user"); 
				if(userObject!=null){
					UserModel user  = new UserModel(userObject);
					newModle.setUser(user);
				}
				newModle.setMid(obj.getString("mid"));
				newModle.setIdstr(obj.getString("idstr"));
				newModle.setStatus(obj.optJSONObject("status"));
				newModle.setTotal_number(jsonObject.getInt("total_number"));
				newModle.setReply_comment(obj.optJSONObject("reply_comment"));
                newModles.add(newModle);
                
            }
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            System.gc();
        }
        return newModles;
    }
}
