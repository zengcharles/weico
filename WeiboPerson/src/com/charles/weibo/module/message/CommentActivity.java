package com.charles.weibo.module.message;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.CardsAnimationAdapter;
import com.charles.weibo.adapter.CommentAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.http.json.CommentListJson;
import com.charles.weibo.module.base.BaseActivity;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.ACache;
import com.charles.weibo.utils.HttpAsyncTask;
import com.charles.weibo.utils.StringUtils;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
public class CommentActivity extends BaseActivity implements CallHttpResponse{
	
	//----------------Top--------------
	private TextView txtTopCenter ;
	private ImageView btnBack ; 
	
	private ProgressBar mProgressBar ;
	
	private SingleLayoutListView mListView ; 
	private CommentAdapter mAdapter;
	private ArrayList<CommentModel> commentList ;
	
	private HttpAsyncTask asyncTask ; 
	private final int COMMENT_CODE  = 100;
	private static String CACHE_FLAG = "CommentActivity";
	private static final int QUERY_DEFAULT_NUM = 20 ;
	
	private int pageIndex = 0;
	private int totalPage = 0 ;
	private boolean isRefresh =false;
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnBack:
			 		finish();
					overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
			break;
		default:
			break;
		}
		
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_comment_view;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		mListView = (SingleLayoutListView)findViewById(R.id.mListView);	
		txtTopCenter = (TextView)findViewById(R.id.txtTopCenter) ; 
		btnBack= (ImageView)findViewById(R.id.btnBack);
		mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		txtTopCenter.setText("所有评论");
		commentList = new ArrayList<CommentModel>();
		mAdapter = new CommentAdapter(CommentActivity.this, commentList);
		AnimationAdapter animationAdapter = new CardsAnimationAdapter(mAdapter);
	    animationAdapter.setAbsListView(mListView);
	    mListView.setAdapter(animationAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		
		loadData(pageIndex);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		btnBack.setOnClickListener(this);
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
				pageIndex = pageIndex+1;
				loadData(pageIndex);
			}
		});
		
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
	public void onHttpSuccess(int requestCode, JSONObject result) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case COMMENT_CODE:
			if (result != null) {
				getResult(result.toString());
			}
			break;
		default:
			break;
		}
	}

	public void getResult(String strResult) {
		JSONObject result;
		try {
			result = new JSONObject(strResult);

			setCacheStr(CACHE_FLAG + pageIndex, result.toString());
			if (result != null) {
				if (isRefresh) {
					isRefresh = false;
					commentList.clear();
				}
				totalPage = result.getInt("total_number");

				ArrayList<CommentModel> list = CommentListJson.instance(this).readJsonCommentModels(result.toString());
				mAdapter.appendList(list);
				mProgressBar.setVisibility(View.GONE);

				if (pageIndex == 1) {
					mListView.onRefreshComplete(); // 下拉刷新完成
				} else {
					mListView.onLoadMoreComplete(); // 加载更多完成
				}

				if (pageIndex >= totalPage) {
					mListView.setNoMoreData();
					mListView.setCanLoadMore(false);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@Override
	public void onHttpError(int requestCode, int errCode) {
		// TODO Auto-generated method stub
		
	}
	
	private void loadData(int pageIndex ){
		if(hasNetWork()){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("count", QUERY_DEFAULT_NUM);
		params.put("page", pageIndex);
		params.put("access_token", mAccessToken.getToken()); 
		QueryDataPraser praser = new QueryDataPraser(CommentActivity.this, Config.commentListAction, Config.GET);
		asyncTask = new HttpAsyncTask(CommentActivity.this, COMMENT_CODE, params, this, false);
		asyncTask.query(praser);
		}else{
				mListView.onLoadMoreComplete();
	            mProgressBar.setVisibility(View.GONE);
	            showShortToast(getString(R.string.not_network));
	            String result = getCacheStr(CACHE_FLAG + pageIndex);
	            if (!StringUtils.isEmpty(result)) {
	                getResult(result);
	            }
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
