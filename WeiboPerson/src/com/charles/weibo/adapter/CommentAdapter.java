package com.charles.weibo.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.CommentModel.ReplyComment;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;
import com.charles.weibo.module.WriteCommentActivity;
import com.charles.weibo.module.detial.WeiboDetailtActivity;
import com.charles.weibo.utils.CommonUtils;
import com.charles.weibo.utils.IntentHelper;
import com.charles.weibo.utils.Options;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CommentAdapter  extends BaseAdapter{
	private ArrayList<CommentModel> array ; 
	private LayoutInflater inflater ;
	private Context mContext ; 
	
	protected DisplayImageOptions options;
	
	public CommentAdapter(Context mComtext, ArrayList<CommentModel> array){
		this.array = array ;
		this.mContext = mComtext;
		this.inflater = inflater.from(mContext) ;
		options =Options.getListOptions(); 
	}
	
    public void appendList(List<CommentModel> list) {
        if (!array.containsAll(list) && list != null && list.size() > 0) {
        	array.addAll(list);
        }
        notifyDataSetChanged();
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
			convertView = inflater.inflate(R.layout.item_comment, null);
			holder.imgUserIcon = (ImageView)convertView.findViewById(R.id.imgUserIcon);
			holder.txtUserName = (TextView)convertView.findViewById(R.id.txtUserName);
			holder.txtCreateTime = (TextView)convertView.findViewById(R.id.txtCreateTime);
			holder.txtSendFrom = (TextView)convertView.findViewById(R.id.txtSendFrom);
			holder.btnResp = (TextView)convertView.findViewById(R.id.btnResp);
			holder.txtComment2 = (TextView)convertView.findViewById(R.id.txtComment2);
			holder.txtcomment1 = (TextView)convertView.findViewById(R.id.txtcomment1);
			holder.imgWeicoUserIcon = (ImageView)convertView.findViewById(R.id.imgWeicoUserIcon);
			holder.txtWeicoUserName = (TextView)convertView.findViewById(R.id.txtWeicoUserName);
			holder.txtWeicoText = (TextView)convertView.findViewById(R.id.txtWeicoText);
			holder.txtRpUserName = (TextView)convertView.findViewById(R.id.txtRpUserName); 
			
			convertView.setTag(holder);
		}else{
			holder =(ViewHolder)convertView.getTag(); 
			
		}
		
		try {
			final CommentModel comment = array.get(position);
			if(comment!=null){
				UserModel user = comment.getUser(); 
				if(user!=null){
					ImageLoader.getInstance().displayImage(user.getProfile_image_url(), holder.imgUserIcon,options);
					holder.txtUserName.setText(user.getName());
					Date date = CommonUtils.StrToDate1(comment.getCreated_at());
					holder.txtCreateTime.setText(CommonUtils.timestampToString(date));
					holder.txtSendFrom.setText(comment.getSource());
					holder.txtComment2.setText(comment.getText());
					if(comment.getStatus()!=null){
						WeiboModel  status = comment.getStatus() ;
						holder.txtcomment1.setText(status.getText());
						UserModel statusUser = status.getUser() ;
							if(statusUser!=null){
								ImageLoader.getInstance().displayImage(user.getProfile_image_url(), holder.imgWeicoUserIcon,options);
								holder.txtWeicoUserName.setText(statusUser.getName());
								holder.txtWeicoText.setText(status.getText());
								
							}
						ReplyComment rc = comment.getReply_comment();	
						if(rc!=null){
							holder.txtcomment1.setText(rc.getText());
							if(holder.txtcomment1.getText()!=null&&!holder.txtcomment1.getText().equals("")&&rc.getText()!=null){
								holder.txtRpUserName.setText("@"+rc.getUser().getName()+":");
							}else{
								holder.txtRpUserName.setVisibility(View.GONE);
								holder.txtcomment1.setVisibility(View.GONE);
							}
						}
					}
				}
				
				holder.btnResp.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						/*Intent intent = new Intent();
						intent.setClass(mContext, WriteCommentActivity.class);
						String commentID =  comment.getIdstr();
						String weicoID = comment.getStatus().getIdstr();
						intent.putExtra("weicoID", weicoID); 
						intent.putExtra("commentID", commentID); 
						mContext.startActivity(intent);*/
						
						String commentID =  comment.getIdstr();
						String weicoID = comment.getStatus().getIdstr();
						Bundle bundle = new Bundle();
						bundle.putString("weicoID", weicoID); 
						bundle.putString("commentID", commentID); 
						IntentHelper.openActivity(mContext, WriteCommentActivity.class, bundle, 0);
						
					}
				});
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		return convertView;
	}
	
	class ViewHolder{
		private ImageView imgUserIcon ,imgWeicoUserIcon; 
		private  TextView txtUserName,txtCreateTime,txtSendFrom,btnResp, txtRpUserName,txtComment2,txtcomment1,txtWeicoUserName,txtWeicoText ; 
		
	}

}
