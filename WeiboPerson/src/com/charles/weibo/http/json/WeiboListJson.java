
package com.charles.weibo.http.json;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;

public class WeiboListJson extends JsonPacket {

    public static WeiboListJson newListJson;

    public ArrayList<WeiboModel> newModles;

    public WeiboListJson(Context context) {
        super(context);
    }

    public static WeiboListJson instance(Context context) {
        if (newListJson == null) {
            newListJson = new WeiboListJson(context);
        }
        return newListJson;
    }

    public ArrayList<WeiboModel> readJsonWeiboModels(String res) {
        newModles = new ArrayList<WeiboModel>();
        try {
            if (res == null || res.equals("")) {
                return null;
            }
            WeiboModel newModle = null;
            UserModel user = null ;
            WeiboModel oldWeiBo  = null;
            JSONObject jsonObject = new JSONObject(res);
            JSONArray jsonArray = jsonObject.getJSONArray("statuses");
            for (int i = 1; i < jsonArray.length(); i++) {
            	newModle = new WeiboModel();
				JSONObject obj = jsonArray.optJSONObject(i);
				newModle.setId(obj.getLong("id"));
				newModle.setCreated_at(obj.getString("created_at"));
				newModle.setSource(obj.getString("source"));
				newModle.setText(obj.getString("text"));
				newModle.setPic_urls(obj.optJSONArray("pic_urls"));
				newModle.setReposts_count(obj.getInt("reposts_count"));
				newModle.setComments_count(obj.getInt("comments_count"));
				newModle.setAttitudes_count(obj.getInt("attitudes_count"));

				// 转发微博的用户信息
				user = new UserModel();
				JSONObject userObject = obj.optJSONObject("user");
				user.setProfile_image_url(userObject.getString("profile_image_url"));
				user.setName(userObject.getString("name"));
				newModle.setUser(user);

				// 原微博
				oldWeiBo = new WeiboModel();
				JSONObject oldObj = obj.optJSONObject("retweeted_status");
				if (oldObj != null) {
					oldWeiBo.setCreated_at(oldObj.getString("created_at"));
					oldWeiBo.setSource(oldObj.getString("source"));
					oldWeiBo.setText(oldObj.getString("text"));
					oldWeiBo.setPic_urls(oldObj.optJSONArray("pic_urls"));
					oldWeiBo.setReposts_count(oldObj.getInt("reposts_count"));
					oldWeiBo.setComments_count(oldObj.getInt("comments_count"));
					oldWeiBo.setAttitudes_count(oldObj.getInt("attitudes_count"));

					// 发送原微博用户信息
					UserModel oldUser = new UserModel();
					JSONObject oldUserObject = oldObj.optJSONObject("user");
					oldUser.setProfile_image_url(oldUserObject.getString("profile_image_url"));
					oldUser.setName(oldUserObject.getString("name"));
					oldWeiBo.setUser(oldUser);
					newModle.setOldWeibo(oldWeiBo);
				}
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
