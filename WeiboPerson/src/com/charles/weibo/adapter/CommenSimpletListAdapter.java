package com.charles.weibo.adapter;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommenSimpletListAdapter extends BaseAdapter{
	private ArrayList<CommentModel> array ; 
	private CommentModel comModel; 	
	private UserModel user; 
	Context mContext ; 
	
	private LayoutInflater inflater ;
	
	public CommenSimpletListAdapter (Context mContext,ArrayList<CommentModel> array){
		this.mContext =  mContext ;
		this.array=array; 
		this.inflater =LayoutInflater.from(mContext) ;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array.indexOf(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder ;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_simple_comment, null);
			holder.imgUserIcon = (ImageView)convertView.findViewById(R.id.imgUserIcon);
			holder.txtUserName = (TextView)convertView.findViewById(R.id.txtUserName);
			holder.txtCreateTime = (TextView)convertView.findViewById(R.id.txtCreateTime);
			holder.txtText = (TextView)convertView.findViewById(R.id.txtText);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		try{
			comModel = array.get(position);
			if(comModel!=null){
				user =comModel.getUser(); 
				if(user!=null){
					ImageLoader.getInstance().displayImage(user.getProfile_image_url(), holder.imgUserIcon);
					holder.txtUserName.setText(user.getName());
				}
				
				Date date = CommonUtils.StrToDate1(comModel.getCreated_at());
				holder.txtCreateTime.setText(CommonUtils.timestampToString(date));
				holder.txtText.setText(comModel.getText());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return convertView;
	}
	class ViewHolder{
		private ImageView imgUserIcon ; 
		private TextView txtUserName ; 
		private TextView txtCreateTime;
		private TextView txtText; 
	} 
}
