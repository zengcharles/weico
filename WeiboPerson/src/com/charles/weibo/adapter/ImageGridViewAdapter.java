package com.charles.weibo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.charles.weibo.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageGridViewAdapter extends BaseAdapter {
	private ArrayList<String> imgUriList;
	private Context mContext ;
	private LayoutInflater inflater ;
	
	public  ImageGridViewAdapter(Context mContext, ArrayList<String> imgUrlList){
		this.mContext = mContext ;
		this.imgUriList = imgUrlList;
		this.inflater = inflater.from(mContext);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgUriList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgUriList.indexOf(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder  holder ;
		if(null==convertView){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_image, null);
			holder.imgWeobo = (ImageView)convertView.findViewById(R.id.imgWeobo);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		try {
				String imgUri = imgUriList.get(position);
				if(!TextUtils.isEmpty(imgUri))
				ImageLoader.getInstance().displayImage(imgUri, holder.imgWeobo);
				
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		convertView.setClickable(false);
		convertView.setPressed(false);
		convertView.setEnabled(false);
		
		return convertView;
	}

	class ViewHolder{
		private ImageView imgWeobo ;
	}	
}
