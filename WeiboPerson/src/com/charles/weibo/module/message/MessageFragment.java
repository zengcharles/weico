package com.charles.weibo.module.message;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charles.weibo.R;

public class MessageFragment extends Fragment {
	LayoutInflater inflater; 
	private LinearLayout layComment; 
	private TextView  txtTitle ;
	private Context mContext ;
	View v ;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.v = inflater.inflate(R.layout.fragment_message,container,false);
		layComment = (LinearLayout)v.findViewById(R.id.layComment); 
		mContext = getActivity();
		initCmp();
		initData();
		return v;
	}
	
	private void initCmp(){
		txtTitle = (TextView)v.findViewById(R.id.txtTitle); 
		
	}
	private void initData(){
		txtTitle.setText(mContext.getResources().getString(R.string.message));
		layComment.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(); 
				intent.setClass(getActivity(),CommentActivity.class);
				startActivity(intent);
			}
		});
	}
	
}
