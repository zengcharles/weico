package com.charles.weibo.module.homepage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.WeiboListAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;
import com.charles.weibo.module.WriteWeicoActivity;
import com.charles.weibo.module.detial.WeiboDetailtActivity;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.charles.weibo.ui.AppMsg;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.HttpAsyncTask;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class HomeFragment extends Fragment  implements CallHttpResponse    {
	
	private String TAG = "HomeFragment" ; 
	Context mContext ;
	
	private TextView txtTitle;
	private ImageView imgSendWeico ;
	private LayoutInflater inflater ; 
	private SingleLayoutListView lvWeibo;
	private Oauth2AccessToken mAccessToken;
	private LoginUserInfoKeeper mUserKeeper ; 
	private ArrayList<WeiboModel> weiboList;
	private WeiboListAdapter mAdapter;
	private UserModel user ;
	private WeiboModel weibo  ; 
	
	private  int pageIndex = 1 ;
	private boolean isFirstLoading = true ;
	// 网络请求任务 , 网络请求码
	private HttpAsyncTask  queryTask ;    	 			
	private HttpAsyncTask  userInfoTask ;
	private HttpAsyncTask  groupTask;
	private final int WEIBO_LIST_CODE = 100 ;		
	private final int USER_INFO_CODE = 101 ;
	private final int GROUP_CODE = 102 ; 
	
	private AppMsg.Style style ;
	private AppMsg appMsg;
	
	private PopupWindow  popupWindow;
	
	private boolean isRefresh =false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_home, container, false) ; 
		
		mContext = getActivity() ;
		findView(v);
		initData();
		this.inflater = inflater;
		initPopList(inflater,txtTitle);
		return v;
	}
	
	private void findView(View v){
		lvWeibo = (SingleLayoutListView)v.findViewById(R.id.mListView);
		txtTitle = (TextView)v.findViewById(R.id.txtTitle) ;
		imgSendWeico = (ImageView)v.findViewById(R.id.imgSendWeico); 
		
	}
	
	private void initData(){
		mUserKeeper = new LoginUserInfoKeeper() ;
		imgSendWeico.setImageDrawable(getResources().getDrawable(R.drawable.write_selecter)) ;
		style = new AppMsg.Style(2000, R.color.confirm);
		txtTitle.setText(mUserKeeper.readUserInfo(mContext).getName());
		mAccessToken = AccessTokenKeeper.readAccessToken(mContext) ;
		weiboList = new ArrayList<WeiboModel>(); 
		mAdapter = new WeiboListAdapter(weiboList, mContext); 
		mAdapter.notifyDataSetChanged();
		lvWeibo.setAdapter(mAdapter);
		lvWeibo.setCanLoadMore(true);
		lvWeibo.setCanRefresh(true);
		lvWeibo.setAutoLoadMore(true);
		queryUserInfo(AccessTokenKeeper.readAccessToken(mContext));
		queryGroup(AccessTokenKeeper.readAccessToken(mContext));
		loadMyFriendWeibo(pageIndex);
		lvWeibo.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				pageIndex = 1; 
				isRefresh = true ;
				loadMyFriendWeibo(pageIndex);
			}

		});
		lvWeibo.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				Log.d(TAG, "----------------loadmore-------------");
				pageIndex = pageIndex+1;
				loadMyFriendWeibo(pageIndex);
			}
		});
		
		lvWeibo.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent()  ; 
				intent.setClass(mContext,WeiboDetailtActivity.class);
				weibo = weiboList.get(position-1); 
				intent.putExtra("weibo", weibo);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
			}
		});
	
		imgSendWeico.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			/*	appMsg = AppMsg.makeText(getActivity(), "Click Here!", style);
				appMsg.show();*/
				Intent intent  = new Intent();
				intent.setClass(getActivity(), WriteWeicoActivity.class);
				startActivity(intent);
				getActivity().overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
			}
		});
		
		txtTitle.setOnClickListener(new OnClickListener(){
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.showAsDropDown(v);
			}
			
		});
	}
	
	
	private void initPopList(LayoutInflater inflater,TextView v) {
		View contentView = inflater.from(mContext).inflate(R.layout.pop_group, null, true);
		// PopupWindow弹出的窗口显示的view,第二和第三参数：分别表示此弹出窗口的大小
		 popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		// 有了这句才可以点击返回（撤销）按钮dismiss()popwindow
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.setOutsideTouchable(true);
		// 动画效果
		// m_popupWindow.setAnimationStyle(R.style.PopupAnimation);
		//popupWindow.setWidth(v.getWidth()); 
		
	}
	
	// 加载所有我关注好友的微博  load data 
	private void loadMyFriendWeibo(final int pageIndex){	
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("access_token", mAccessToken.getToken());
		params.put("count", "30");
		params.put("page", pageIndex);
		QueryDataPraser praser = new QueryDataPraser(mContext, Config.friendsNewWeiboAction,Config.GET);
		queryTask = new HttpAsyncTask(this.getActivity(),WEIBO_LIST_CODE, params, this, true);
		queryTask.query(praser);
		if(isFirstLoading){
			queryTask.startLoadingDialog();
		}
	}
	
	
	@Override
	public void onHttpSuccess(int requestCode, JSONObject result) {
		try {
			if(result!=null){
				switch (requestCode) {
				case WEIBO_LIST_CODE:
						JSONArray jsonArray = result.getJSONArray("statuses"); 
						if(isRefresh){
							weiboList.clear();
							isRefresh = false ;
						}
						for(int i = 0;i<jsonArray.length();i++){
							// 转发微博
							WeiboModel weibo = new WeiboModel();
							JSONObject obj = jsonArray.optJSONObject(i);
							weibo.setId(obj.getLong("id"));
							weibo.setCreated_at(obj.getString("created_at"));
							weibo.setSource(obj.getString("source"));
							weibo.setText(obj.getString("text"));
							weibo.setPic_urls(obj.optJSONArray("pic_urls"));
							weibo.setReposts_count(obj.getInt("reposts_count"));
							weibo.setComments_count(obj.getInt("comments_count"));
							weibo.setAttitudes_count(obj.getInt("attitudes_count"));
						
							//转发微博的用户信息
							UserModel user =new UserModel();
							JSONObject userObject = obj.optJSONObject("user");
							user.setProfile_image_url(userObject.getString("profile_image_url"));
							user.setName(userObject.getString("name"));
							weibo.setUser(user);
							
							// 原微博
							WeiboModel oldWeiBo = new WeiboModel();
							JSONObject oldObj = obj.optJSONObject("retweeted_status");
							if(oldObj!=null){
							oldWeiBo.setCreated_at(oldObj.getString("created_at"));
							oldWeiBo.setSource(oldObj.getString("source"));
							oldWeiBo.setText(oldObj.getString("text"));
							oldWeiBo.setPic_urls(oldObj.optJSONArray("pic_urls"));
							oldWeiBo.setReposts_count(oldObj.getInt("reposts_count"));
							oldWeiBo.setComments_count(oldObj.getInt("comments_count"));
							oldWeiBo.setAttitudes_count(oldObj.getInt("attitudes_count"));
							
							// 发送原微博用户信息
							UserModel oldUser =new UserModel();
							JSONObject oldUserObject = oldObj.optJSONObject("user");
							oldUser.setProfile_image_url(oldUserObject.getString("profile_image_url"));
							oldUser.setName(oldUserObject.getString("name"));
							oldWeiBo.setUser(oldUser);
							weibo.setOldWeibo(oldWeiBo);
							}
							weiboList.add(weibo);
						}
						
						mAdapter.notifyDataSetChanged();
						mAdapter.notifyDataSetInvalidated();
						if (pageIndex == 1) {
							
							lvWeibo.onRefreshComplete(); // 下拉刷新完成
							if(!isFirstLoading){
								Random r = new Random() ;
								double d3 = r.nextDouble() * 2.5 ;    //[1,2.5]
								int i = (int)(d3*10) ; 
								appMsg = AppMsg.makeText(getActivity(), i+"条新微博", style);
								appMsg.show();
							}
						} else {
							lvWeibo.onLoadMoreComplete(); // 加载更多完成
						}
						
						if(isFirstLoading){
							queryTask.stopLoadingDialog();
							isFirstLoading = false ;
						}
						
					break;
				case USER_INFO_CODE:
					if(result!=null){
						mUserKeeper = new LoginUserInfoKeeper() ;
						user = new UserModel();
						user.setId(result.getLong("id"));
						user.setName(result.getString("name"));
						user.setProvince(result.getString("province"));
						user.setCity(result.getString("city"));
						user.setDescription(result.getString("description"));
						user.setProfile_image_url(result.getString("profile_image_url"));
						user.setFollowers_count(result.getString("followers_count"));
						user.setFriends_count(result.getString("friends_count"));
						mUserKeeper.writeLoginUserInfo(mContext, user);
						txtTitle.setText(user.getName());
					}
					break;
				case GROUP_CODE:
						if(result!=null){
							String s= "";
						}
					break;
				default:
					break;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onHttpError(int requestCode, int errCode) {
		// TODO Auto-generated method stub
		
	}
	
	private void  queryUserInfo(Oauth2AccessToken OAuth){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("uid",OAuth.getUid());
		params.put("access_token",OAuth.getToken());
		QueryDataPraser praser = new QueryDataPraser(mContext, Config.loginUsreInfoAction,Config.GET);
		userInfoTask = new HttpAsyncTask(mContext,USER_INFO_CODE, params, this, true);
		userInfoTask.query(praser);
	}
	
	private void  queryGroup(Oauth2AccessToken OAuth){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("access_token",OAuth.getToken());
		params.put("uid",OAuth.getUid());
		QueryDataPraser praser = new QueryDataPraser(mContext, Config.groupAction,Config.GET);
		groupTask = new HttpAsyncTask(mContext,GROUP_CODE, params, this, true);
		groupTask.query(praser);
	}
}	
