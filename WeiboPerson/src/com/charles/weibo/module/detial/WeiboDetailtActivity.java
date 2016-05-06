package com.charles.weibo.module.detial;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.Inflater;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.CommenSimpletListAdapter;
import com.charles.weibo.adapter.ImageGridViewAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;
import com.charles.weibo.module.WriteCommentActivity;
import com.charles.weibo.module.base.BaseActivity;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.ui.ReheightGridView;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.CommonUtils;
import com.charles.weibo.utils.HttpAsyncTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;



public class WeiboDetailtActivity extends BaseActivity implements CallHttpResponse {
	
	private String TAG ="WeiboDetailtActivity";
	//---------Top------------
	private ImageView btnBack ; 
	private TextView txtTopCenter ; 
	// curr user 
	private WeiboModel weibo ; 
	private UserModel user ;
	private ImageView imgUserIcon ; 
	private ImageView imgWeibo ; 
	private TextView txtUserName ;
	private TextView txtCreateTime ;
	private TextView txtSendFrom ;
	private TextView txtWeibo ;
	private TextView txtRepostsNum ;
	private TextView txtAttitudesNum ;
	private TextView txtCommentNum ;
	private View _lay_gridview ;
	private ReheightGridView  gvImage3x3;
	private ImageGridViewAdapter imgAdapter1;
	
	// old weibo 
	private WeiboModel oldweibo ;
	private UserModel olderUser;
	private TextView OldContent ;
	private ImageView imgOldWeibo; 
	private LinearLayout oldViewContent;
	private View _lay_old_gridview ;
	private View _lay_old_weibo ;
	private ReheightGridView gvOldImage3x3 ;
	private ImageGridViewAdapter imgAdapter2;
	
	
	private View content; 
	private LinearLayout lay_weibo_detail;
	private LinearLayout  viewContent ;
	private Inflater inflater ; 
	
	
	// comment 
	private ArrayList<CommentModel> commentData; 
	private CommenSimpletListAdapter comAdapter ; 
	private SingleLayoutListView  lvComment ; 
	
	
	//---------------Bottom-----------
	private LinearLayout btnComment ; 
	private LinearLayout btnReposts ;
	private LinearLayout btnAttitudes;
	
	
	// 網絡請求
	private Oauth2AccessToken mAccessToken;
	HttpAsyncTask queryTask;
	private  final  int QUERY_COMMENT_CODE =100; 
	private int pageIndex = 1;
	private int totalPage = 0;
	private int count = 5 ;
	
	
	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.weibo_detail;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		btnBack = (ImageView)findViewById(R.id.btnBack);
		txtTopCenter = (TextView)findViewById(R.id.txtTopCenter);
		
		btnComment = (LinearLayout)findViewById(R.id.btnComment);
		btnReposts = (LinearLayout)findViewById(R.id.btnReposts);
		btnAttitudes = (LinearLayout)findViewById(R.id.btnAttitudes);
		content = LayoutInflater.from(this).inflate(R.layout.weibo_detail_header, null);
		// curr weico
		imgUserIcon = (ImageView)content.findViewById(R.id.imgUserIcon);
		imgWeibo = (ImageView)content.findViewById(R.id.imgWeibo);
		txtUserName = (TextView)content.findViewById(R.id.txtUserName);
		txtCreateTime = (TextView)content.findViewById(R.id.txtCreateTime);
		txtSendFrom = (TextView)content.findViewById(R.id.txtSendFrom);
		viewContent = (LinearLayout)content.findViewById(R.id.viewContent);
		txtRepostsNum = (TextView)content.findViewById(R.id.txtRepostsNum);
		txtAttitudesNum = (TextView)content.findViewById(R.id.txtAttitudesNum);
		txtCommentNum = (TextView)content.findViewById(R.id.txtCommentNum);
		txtWeibo = (TextView)content.findViewById(R.id.txtWeibo);
		_lay_gridview = LayoutInflater.from(this).inflate(R.layout.gridview3x3, null);
		gvImage3x3 = (ReheightGridView)_lay_gridview.findViewById(R.id.gvImage3x3);
		
		viewContent = (LinearLayout)content.findViewById(R.id.viewContent);
		// oldWeibo
		_lay_old_weibo = LayoutInflater.from(this).inflate(R.layout.item_old_weibo, null); 
		OldContent = (TextView)_lay_old_weibo.findViewById(R.id.txtOldContent);
		imgOldWeibo = (ImageView)_lay_old_weibo.findViewById(R.id.imgOldWeobo);
		oldViewContent = (LinearLayout)_lay_old_weibo.findViewById(R.id.oldViewContent);
		_lay_old_gridview = LayoutInflater.from(this).inflate(R.layout.gridview3x3, null);
		gvOldImage3x3 = (ReheightGridView)_lay_old_gridview.findViewById(R.id.gvImage3x3);
		
		// comment 
		lvComment =  (SingleLayoutListView)findViewById(R.id.lvComment);
		lvComment.addHeaderView(content);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		txtTopCenter.setText(getResources().getString(R.string.article));
		mAccessToken = AccessTokenKeeper.readAccessToken(this) ;
		Bundle bundle = getIntent().getExtras() ; 
		//if(bundle!=null){
			weibo = (WeiboModel)bundle.getSerializable("weibo"); 
			createContent(weibo);
	//	}
		commentData = new ArrayList<CommentModel>();
		comAdapter = new CommenSimpletListAdapter(this, commentData) ;
		lvComment.setAdapter(comAdapter);
		
		loadCommentData(pageIndex);
		
		lvComment.setCanLoadMore(true);
		lvComment.setCanRefresh(true);
		lvComment.setAutoLoadMore(true);
		
		lvComment.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				pageIndex = 1; 
				
				loadCommentData(pageIndex);
				
			}

		});
		lvComment.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Log.d(TAG, "----------------loadmore-------------");
				pageIndex = pageIndex+1;
					loadCommentData(pageIndex);
			}
		});
		
		lvComment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String commentID = String.valueOf(commentData.get(position-2).getId());
				String weicoID = String.valueOf(weibo.getId());
				if(commentID!=null){
					Intent intent = new Intent();
					intent.setClass(WeiboDetailtActivity.this, WriteCommentActivity.class);
					intent.putExtra("commentID", commentID);
					intent.putExtra("weicoID", weicoID);
					startActivity(intent);
				}
			}
		});
		
	}
	
	
	// 動態創建weibo detail 
	private void createContent(WeiboModel weico){
		final WeiboModel weibo = weico ;
		if(weibo!=null){
			user = weibo.getUser(); 
			if(user!=null){
					ImageLoader.getInstance().displayImage(user.getProfile_image_url(), imgUserIcon); 
			}
			txtUserName.setText(user.getName());
			Date date = CommonUtils.StrToDate1(weibo.getCreated_at());
			txtCreateTime.setText(CommonUtils.timestampToString(date));
			txtSendFrom.setText(weibo.getSource());
			txtWeibo.setText(weibo.getText());
			
			if(weibo.getPic_urls().size()==1){
				imgWeibo.setVisibility(View.VISIBLE);
				if(!TextUtils.isEmpty(weibo.getPic_urls().get(0))){
					ImageLoader.getInstance().displayImage(weibo.getPic_urls().get(0),imgWeibo);
						imgWeibo.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String[] urls = (String[])weibo.getPic_urls().toArray(new String[weibo.getPic_urls().size()]);
							imageBrower(0, urls);
						}
					});
				}	
			}else if(weibo.getPic_urls().size()>1){
				imgAdapter1 = new ImageGridViewAdapter(this, weibo.getPic_urls());
				gvImage3x3.setAdapter(imgAdapter1);
				viewContent.addView(_lay_gridview);
				gvImage3x3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						String[] urls = (String[])weibo.getPic_urls().toArray(new String[weibo.getPic_urls().size()]);
						imageBrower(position, urls);
					}
				});
			}
			
			final WeiboModel oldweibo = weibo.getOldWeibo();
			if(oldweibo!=null){
				oldViewContent.removeAllViews();
				imgOldWeibo.setVisibility(View.GONE);
				olderUser = oldweibo.getUser(); 
				String oldUserName = "@"+olderUser.getName();
				setTitle(this, OldContent, oldUserName, ":"+oldweibo.getText());
				
				if(oldweibo.getPic_urls().size()==1){
					imgOldWeibo.setVisibility(View.VISIBLE);
					if(!TextUtils.isEmpty(oldweibo.getPic_urls().get(0))){
						ImageLoader.getInstance().displayImage(oldweibo.getPic_urls().get(0),imgOldWeibo);
						imgOldWeibo.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								String[] urls = (String[])oldweibo.getPic_urls().toArray(new String[oldweibo.getPic_urls().size()]);
								imageBrower(0, urls);
							}
						});
					}
				}else if(oldweibo.getPic_urls().size()>1){
					imgOldWeibo.setVisibility(View.GONE);
					imgAdapter2 = new ImageGridViewAdapter(this, oldweibo.getPic_urls());
					gvOldImage3x3.setAdapter(imgAdapter2);
					oldViewContent.addView(_lay_old_gridview);
					gvOldImage3x3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							String[] urls = (String[])oldweibo.getPic_urls().toArray(new String[oldweibo.getPic_urls().size()]);
							imageBrower(position, urls);
						}
					});
				}
				viewContent.addView(_lay_old_weibo);
			}else{
				viewContent.removeAllViews();
			}
			
			txtRepostsNum.setText("轉發"+weibo.getReposts_count());
			txtCommentNum.setText("評論"+weibo.getComments_count());
			txtAttitudesNum.setText("讚"+weibo.getAttitudes_count());
		}
	}
	
	private void imageBrower(int position, String[] urls) {
		for(int i =0;i<urls.length;i++){
			urls[i]=urls[i].replace("thumbnail", "large");
		}
		Intent intent = new Intent(this, ImagePagerActivity.class);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
		startActivity(intent);
	}
	
	private void loadCommentData(int pageIndex){
		HashMap<String,Object> params = new HashMap<String,Object>(); 
		params.put("id",weibo.getId());
		params.put("access_token",mAccessToken.getToken());
		params.put("count", count);
		params.put("page", pageIndex);
		
		QueryDataPraser praser = new QueryDataPraser(this, Config.commentDataAction,Config.GET);
		queryTask = new HttpAsyncTask(this,	QUERY_COMMENT_CODE, params, this, true);
		queryTask.query(praser);
	}
	
	
	@Override
	public void onHttpSuccess(int requestCode, JSONObject result) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case QUERY_COMMENT_CODE:
			if(result!=null){
				try {
					int totalMember = result.getInt("total_number") ;
					totalPage = totalMember/count+1; 
					
					if(pageIndex==1){
						commentData.clear();
						lvComment.onRefreshComplete();
					}else{
						lvComment.onLoadMoreComplete();
					}
					
					if(totalMember>0){
						JSONArray array = result.optJSONArray("comments") ;
					
						if(array.length()>0){
							for (int i = 0; i < array.length(); i++) {
								CommentModel comment = new CommentModel(); 
								JSONObject obj = array.getJSONObject(i);
								comment.setCreated_at(obj.getString("created_at"));
								comment.setId(obj.getLong("id"));
								comment.setText(obj.getString("text"));
								UserModel comUser =new UserModel(obj.optJSONObject("user"));
								comment.setUser(comUser);
								commentData.add(comment);
							}
							comAdapter.notifyDataSetChanged();
						}
						txtCommentNum.setText("評論"+totalMember);
						comAdapter.notifyDataSetChanged();
						
						// 如果當前是最後一頁，關閉加載更多功能，隱藏加載更多按鈕
						if (pageIndex < totalPage ) {
							lvComment.setCanLoadMore(true);
						} else {
							lvComment.setNoMoreData();
							lvComment.setCanLoadMore(false);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception 
					e.printStackTrace();
				}
			}
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onHttpError(int requestCode, int errCode) {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * 同一行文字設置不同顏色
	 */
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
	
	
	
	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		btnBack.setOnClickListener(this);
		btnComment.setOnClickListener(this);
		btnReposts.setOnClickListener(this);
		btnAttitudes.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
				finish();
			break;
		case R.id.btnComment:
			if(String.valueOf(weibo.getId())!=null){
				Bundle bundle = new Bundle();
				bundle.putString("weicoID", String.valueOf(weibo.getId()));
				((BaseActivity)this).openActivity(WriteCommentActivity.class,bundle,0);
			}
			break;
		case R.id.btnReposts:
			break;
		case R.id.btnAttitudes:
			break;
			
		default:
			break;
		}
		
	}

	@Override
	public void initAppParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View setLayoutView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//initData();
	}

}
