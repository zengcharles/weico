package com.charles.weibo.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.weibo.R;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;
import com.charles.weibo.module.MainActivity;
import com.charles.weibo.module.detial.ImagePagerActivity;
import com.charles.weibo.ui.ReheightGridView;
import com.charles.weibo.utils.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tandong.bottomview.view.BottomView;

public class WeiboListAdapter extends BaseAdapter  {
	private ArrayList<WeiboModel> weiboList ; 
	private Context mContext ;
	private ImageGridViewAdapter imgAdapter1;
	private ImageGridViewAdapter imgAdapter2;
	private ListView lv_menu_list;
	private BottomView bottomView;
	
	View _lay_old_weibo ;
	LayoutInflater inflater  ; 
	
	public WeiboListAdapter(ArrayList<WeiboModel> weiboList ,Context mContext){
		this.weiboList = weiboList; 
		this.mContext = mContext ; 
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return weiboList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return weiboList.indexOf(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder  ;
		if(null==convertView){
			holder = new ViewHolder();
			inflater =  (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE); 
			convertView =   inflater.inflate(R.layout.item_weibo, null);
			
			// currUser
			holder.txtUserName=(TextView)convertView.findViewById(R.id.txtUserName);
			holder.txtCreateTime=(TextView)convertView.findViewById(R.id.txtCreateTime);
			holder.txtSendFrom=(TextView)convertView.findViewById(R.id.txtSendFrom);
			holder.txtWeibo=(TextView)convertView.findViewById(R.id.txtWeibo);
			holder.imgUserIcon=(ImageView)convertView.findViewById(R.id.imgUserIcon);
			holder.btnOperate = (ImageView)convertView.findViewById(R.id.btnOperate);
			
			holder.imgWeibo = (ImageView)convertView.findViewById(R.id.imgWeobo); 
			holder.viewContent = (LinearLayout)convertView.findViewById(R.id.viewContent);
			holder.txtRepostsNum = (TextView)convertView.findViewById(R.id.txtRepostsNum);
			holder.txtAttitudesNum = (TextView)convertView.findViewById(R.id.txtAttitudesNum);
			holder.txtCommentNum = (TextView)convertView.findViewById(R.id.txtCommentNum);
			holder._lay_gridview = inflater.inflate(R.layout.gridview3x3, null);
			holder.gvImage3x3 = (ReheightGridView)holder._lay_gridview.findViewById(R.id.gvImage3x3);
			
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		try{
			final WeiboModel weibo = weiboList.get(position);
		if(weibo!=null){
			final UserModel user = weibo.getUser();
			holder.imgWeibo.setVisibility(View.GONE);
			holder.viewContent.removeAllViews();
			holder.txtUserName.setText(user.getName());
			Date date = CommonUtils.StrToDate1(weibo.getCreated_at());
			holder.txtCreateTime.setText(CommonUtils.timestampToString(date));
			holder.txtSendFrom.setText(weibo.getSource());
			holder.txtWeibo.setText(weibo.getText());
			holder.txtRepostsNum.setText(String.valueOf(weibo.getReposts_count()));
			holder.txtCommentNum.setText(String.valueOf(weibo.getComments_count()));
			holder.txtAttitudesNum.setText(String.valueOf(weibo.getAttitudes_count()));
			if(!TextUtils.isEmpty(user.getProfile_image_url())){
				ImageLoader.getInstance().displayImage(user.getProfile_image_url(),holder.imgUserIcon);
			}
			if(weibo.getPic_urls().size()==1){
				holder.imgWeibo.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(weibo.getPic_urls().get(0))){
					ImageLoader.getInstance().displayImage(weibo.getPic_urls().get(0),holder.imgWeibo);
					holder.imgWeibo.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String[] urls = (String[])weibo.getPic_urls().toArray(new String[weibo.getPic_urls().size()]);
							imageBrower(0, urls);
						}
					});
				}	
			}else if(weibo.getPic_urls().size()>1){
				imgAdapter1 = new ImageGridViewAdapter(mContext, weibo.getPic_urls());
				holder.gvImage3x3.setAdapter(imgAdapter1);
				holder.viewContent.addView(holder._lay_gridview);
				holder.gvImage3x3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String[] urls = (String[])weibo.getPic_urls().toArray(new String[weibo.getPic_urls().size()]);
						imageBrower(position, urls);
					}
				});
			}
			
			final WeiboModel oldweibo = weibo.getOldWeibo();  
			if(oldweibo!=null){
				// oldWeibo
				_lay_old_weibo = inflater.inflate(R.layout.item_old_weibo, null); 
				holder.OldContent = (TextView)_lay_old_weibo.findViewById(R.id.txtOldContent);
				holder.imgOldWeibo = (ImageView)_lay_old_weibo.findViewById(R.id.imgOldWeobo);
				holder.oldViewContent = (LinearLayout)_lay_old_weibo.findViewById(R.id.oldViewContent);
				holder._lay_old_gridview = inflater.inflate(R.layout.gridview3x3, null);
				holder.gvOldImage3x3 = (ReheightGridView)holder._lay_old_gridview.findViewById(R.id.gvImage3x3);
				holder.oldViewContent.removeAllViews();
				holder.imgOldWeibo.setVisibility(View.GONE);
				final UserModel olderUser = oldweibo.getUser(); 
				String oldUserName = "@"+olderUser.getName();
				setTitle(mContext, holder.OldContent, oldUserName, ":"+oldweibo.getText());
				
				if(oldweibo.getPic_urls().size()==1){
					holder.imgOldWeibo.setVisibility(View.VISIBLE);
					if(!TextUtils.isEmpty(oldweibo.getPic_urls().get(0))){
						ImageLoader.getInstance().displayImage(oldweibo.getPic_urls().get(0),holder.imgOldWeibo);
						holder.imgOldWeibo.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String[] urls = (String[])oldweibo.getPic_urls().toArray(new String[oldweibo.getPic_urls().size()]);
								imageBrower(0, urls);
							}
						});
					}
					
				}else if(oldweibo.getPic_urls().size()>1){
					holder.imgOldWeibo.setVisibility(View.GONE);
					imgAdapter2 = new ImageGridViewAdapter(mContext, oldweibo.getPic_urls());
					holder.gvOldImage3x3.setAdapter(imgAdapter2);
					holder.oldViewContent.addView(holder._lay_old_gridview);
					
					holder.gvOldImage3x3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							String[] urls = (String[])oldweibo.getPic_urls().toArray(new String[oldweibo.getPic_urls().size()]);
							imageBrower(position, urls);
						}
					});
				}
				
				setOnClickViewListener(holder);
				holder.viewContent.addView(_lay_old_weibo);
			}else{
				holder.viewContent.removeAllViews();
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		return convertView;
	}
	
	private void  setOnClickViewListener(ViewHolder holder) {
		holder.btnOperate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				bottomView = new BottomView(mContext,R.style.BottomViewTheme_Defalut, R.layout.bottom_view);
				bottomView.setAnimation(R.style.BottomToTopAnim);				
				bottomView.showBottomView(true);
			
				lv_menu_list = (ListView) bottomView.getView().findViewById(R.id.lv_list);
				List<String> operations = Arrays.asList(mContext.getResources().getStringArray(R.array.operate));
				BVAdapter adapter = new BVAdapter(mContext, operations);
				lv_menu_list.setAdapter(adapter);
				lv_menu_list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						bottomView.dismissBottomView();
					}
				});
			}
		}) ; 
		
	}
	
	private void imageBrower(int position, String[] urls) {
		for(int i =0;i<urls.length;i++){
			urls[i]=urls[i].replace("thumbnail", "large");
		}
		Intent intent = new Intent(mContext, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		mContext.startActivity(intent);
	}
	
	class ViewHolder{ 			 
		private TextView txtUserName,txtCreateTime,txtSendFrom,txtWeibo ,txtAttitudesNum, txtCommentNum ,txtRepostsNum;
		private TextView oldUserName,OldContent;
		private ImageView imgUserIcon,imgWeibo,imgOldWeibo;
		private View _lay_gridview ,_lay_old_gridview;
		private ReheightGridView gvImage3x3 ,gvOldImage3x3;
		private LinearLayout viewContent;
		private LinearLayout oldViewContent ; 
		private ImageView btnOperate ;
	}
	
	public static void setTitle(Context context, TextView textView,
			String title1, String title2) {
		SpannableString spanString = new SpannableString(title1 + title2);
		ForegroundColorSpan span = new ForegroundColorSpan(context
				.getResources().getColor(R.color.title_bar_text_red));
		spanString.setSpan(span, 0, title1.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		// 设置文字
		textView.setText(spanString);
	}
}
