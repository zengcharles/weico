package com.charles.weibo.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.entity.NearPlaceModel;

public class NearPlaceAdapter  extends BaseAdapter{
	private ArrayList<NearPlaceModel> array; 
	private Context mContext; 
	private LayoutInflater inflater ; 
	
	public NearPlaceAdapter(ArrayList<NearPlaceModel> array,Context mContext){
		this.array = array; 
		this.mContext = mContext ; 
		this.inflater = inflater.from(mContext); 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return array.get(position);
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
		 		convertView = inflater.inflate(R.layout.item_location, null) ; 
		 		holder.txtPlaceName = (TextView)convertView.findViewById(R.id.txtPlaceName);
		 		holder.txtAddress = (TextView)convertView.findViewById(R.id.txtAddress);
		 		convertView.setTag(holder);
		 	}else{
		 		holder = (ViewHolder)convertView.getTag() ;
		 	}
		 	final NearPlaceModel np = array.get(position) ; 
	 		if(np!=null){
	 			holder.txtPlaceName.setText(np.getTitle());
	 			holder.txtAddress.setText(np.getAddress());
	 		}
		return convertView;
	}
	
	class ViewHolder {
		private TextView txtPlaceName ; 
		private TextView txtAddress ;
	}
}
