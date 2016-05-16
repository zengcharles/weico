package com.charles.weibo.module.homepage;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.CardsAnimationAdapter;
import com.charles.weibo.adapter.WeiboListAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.entity.WeiboModel;
import com.charles.weibo.http.HttpUtil;
import com.charles.weibo.http.json.CommentListJson;
import com.charles.weibo.http.json.WeiboListJson;
import com.charles.weibo.module.WriteWeicoActivity;
import com.charles.weibo.module.base.BaseActivity;
import com.charles.weibo.module.base.BaseFragment;
import com.charles.weibo.module.detial.WeiboDetailtActivity;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.charles.weibo.ui.AppMsg;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.ACache;
import com.charles.weibo.utils.HttpAsyncTask;
import com.charles.weibo.utils.IntentHelper;
import com.charles.weibo.utils.StringUtils;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class HomeFragment extends BaseFragment  implements CallHttpResponse    {
	
	private String TAG = "HomeFragment" ; 
	private static String CACHE_FLAG = "CommentActivity";
	//---------------TopBar---------------
	private TextView txtTitle;
	private ImageView imgSendWeico ;
	
	private ProgressBar mProgressBar;
	
	private Context mContext ;
	private LayoutInflater inflater ; 
	
	private SingleLayoutListView mListView;
	private ArrayList<WeiboModel> weiboList;
	private WeiboListAdapter mAdapter;
	private UserModel userModel ;
	private WeiboModel weiboModel  ; 
	
	private  int pageIndex = 1 ;
	private boolean isRefresh =false;
	
	// 网络请求任务,网络请求码
	private HttpAsyncTask  weiboTask ;    	 			
	private HttpAsyncTask  userInfoTask ;
	private HttpAsyncTask  groupTask;
	private final int WEIBO_LIST_QUERY_CODE = 100 ;		
	private final int USER_INFO_QUERY_CODE = 101 ;
	private final int GROUP_QUERY_CODE = 102 ; 
	
	private AppMsg.Style style ;
	private AppMsg appMsg;
	
	private PopupWindow  popupWindow;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View contentView= inflater.inflate(R.layout.fragment_home, container, false) ; 	
		mContext = getActivity() ;
		this.inflater = inflater;
		findView(contentView);
		initData();
		return contentView;
	}
	
	private void findView(View v){
		mListView = (SingleLayoutListView)v.findViewById(R.id.mListView);
		txtTitle = (TextView)v.findViewById(R.id.txtTitle) ;
		imgSendWeico = (ImageView)v.findViewById(R.id.imgSendWeico); 
		mProgressBar = (ProgressBar)v.findViewById(R.id.progressBar);
		
	}
	
	private void initData(){
		initPopList(inflater,txtTitle);
		imgSendWeico.setImageDrawable(getResources().getDrawable(R.drawable.write_selecter)) ;
		style = new AppMsg.Style(2000, R.color.confirm);
		txtTitle.setText(mUserKeeper.readUserInfo(mContext).getName());
		weiboList = new ArrayList<WeiboModel>(); 
		
		mAdapter = new WeiboListAdapter(weiboList, mContext); 
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
	    animationAdapter.setAbsListView(mListView);
	    
	    mListView.setAdapter(animationAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		
		loadData(pageIndex);
		loadUserInfo(AccessTokenKeeper.readAccessToken(mContext));
		loadGroup(AccessTokenKeeper.readAccessToken(mContext));
		
		onClickListener();
	}
	
	private void onClickListener() {
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO 下拉刷新
				pageIndex = 1;
				isRefresh = true;
				loadData(pageIndex);
			}

		});
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				pageIndex = pageIndex + 1;
				loadData(pageIndex);
			}
		});

		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();
				weiboModel = weiboList.get(position - 1);
				bundle.putSerializable("weibo", weiboModel);
				IntentHelper.openActivity(getActivity(),
						WeiboDetailtActivity.class, bundle, 0);
			}
		});

		imgSendWeico.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IntentHelper.openActivity(getActivity(),
						WriteWeicoActivity.class);
			}
		});

		txtTitle.setOnClickListener(new OnClickListener() {

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
	
	@Override
	public void onHttpSuccess(int requestCode, JSONObject result) {
		try {
			if(result!=null){
				switch (requestCode) {
				case WEIBO_LIST_QUERY_CODE:
					if (result != null) {
						getResult(result.toString());
					}
						
					break;
				case USER_INFO_QUERY_CODE:
					if(result!=null){
						mUserKeeper = new LoginUserInfoKeeper() ;
						userModel = new UserModel();
						userModel.setId(result.getLong("id"));
						userModel.setName(result.getString("name"));
						userModel.setProvince(result.getString("province"));
						userModel.setCity(result.getString("city"));
						userModel.setDescription(result.getString("description"));
						userModel.setProfile_image_url(result.getString("profile_image_url"));
						userModel.setFollowers_count(result.getString("followers_count"));
						userModel.setFriends_count(result.getString("friends_count"));
						mUserKeeper.writeLoginUserInfo(mContext, userModel);
						txtTitle.setText(userModel.getName());
					}
					break;
				case GROUP_QUERY_CODE:
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
	
	public void getResult(String strResult) {
		JSONObject result;
		try {
			result = new JSONObject(strResult);
			ACache.get(getActivity()).put(CACHE_FLAG + pageIndex, result.toString());
			if (result != null) {
				if (isRefresh) {
					weiboList.clear();
					isRefresh = false;
				}						
				ArrayList<WeiboModel> list = WeiboListJson.instance(getActivity()).readJsonWeiboModels(result.toString());	
				weiboList.addAll(list);
				mAdapter.appendList(list);
				
				if (pageIndex == 1) {
					mListView.onRefreshComplete(); // 下拉刷新完成
					/*
					 * if(!isFirstLoading){ Random r = new Random() ;
					 * double d3 = r.nextDouble() * 2.5 ; //[1,2.5] int
					 * i = (int)(d3*10) ; appMsg =
					 * AppMsg.makeText(getActivity(), i+"条新微博", style);
					 * appMsg.show(); }
					 */
				} else {
					mListView.onLoadMoreComplete(); // 加载更多完成
				}
				mProgressBar.setVisibility(View.GONE);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 加载所有我关注好友的微博 load data
	private void loadData(final int pageIndex) {
		if (HttpUtil.isNetworkAvailable(getActivity())) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("access_token", mAccessToken.getToken());
			params.put("count", "30");
			params.put("page", pageIndex);
			QueryDataPraser praser = new QueryDataPraser(mContext,Config.friendsNewWeiboAction, Config.GET);
			weiboTask = new HttpAsyncTask(this.getActivity(), WEIBO_LIST_QUERY_CODE,params, this, true);
			weiboTask.query(praser);
		}else{
			mListView.onLoadMoreComplete();
            mProgressBar.setVisibility(View.GONE);
            Toast.makeText(getActivity(), getString(R.string.not_network), Toast.LENGTH_SHORT).show();
            String result =  ACache.get(getActivity()).getAsString(CACHE_FLAG + pageIndex);
            if (!StringUtils.isEmpty(result)) {
                getResult(result);
            }
		}
	}
	
	private void  loadUserInfo(Oauth2AccessToken OAuth){
		if (HttpUtil.isNetworkAvailable(getActivity())) {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("uid", OAuth.getUid());
			params.put("access_token", OAuth.getToken());
			QueryDataPraser praser = new QueryDataPraser(mContext,Config.loginUsreInfoAction, Config.GET);
			userInfoTask = new HttpAsyncTask(mContext, USER_INFO_QUERY_CODE, params,this, true);
			userInfoTask.query(praser);
		}
	}
	
	private void  loadGroup(Oauth2AccessToken OAuth){
		if (HttpUtil.isNetworkAvailable(getActivity())) {
			HashMap<String,Object> params = new HashMap<String,Object>();
			params.put("access_token",OAuth.getToken());
			params.put("uid",OAuth.getUid());
			QueryDataPraser praser = new QueryDataPraser(mContext, Config.groupAction,Config.GET);
			groupTask = new HttpAsyncTask(mContext,GROUP_QUERY_CODE, params, this, true);
			groupTask.query(praser);
		}
	}
}	
