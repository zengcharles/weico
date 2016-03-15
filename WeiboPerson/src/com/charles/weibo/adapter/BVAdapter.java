package com.charles.weibo.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.charles.weibo.R;

/**
 * BottomView
 * 
 * @author TanDong
 * 
 */
public class BVAdapter extends BaseAdapter {
	private Context c;
	private List<String> alss;

	public BVAdapter(Context context, List<String> als) {
		this.c = context;
		this.alss = als;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return alss.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return alss.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		convertView = View.inflate(c, R.layout.item_bottom_view, null);
		TextView tv = (TextView) convertView.findViewById(R.id.tv_name);
		tv.setText(alss.get(position));
		return convertView;
	}

}
