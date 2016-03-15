package com.charles.weibo.module.message;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.base.BaseActivity;
import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.CommentAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.CommentModel;
import com.charles.weibo.entity.UserModel;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.HttpAsyncTask;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
public class CommentActivity extends BaseActivity implements CallHttpResponse{
	
	//----------------Top--------------
	private TextView txtTopCenter ;
	private ImageView btnBack ; 
	
	private SingleLayoutListView mListView ; 
	private Oauth2AccessToken mAccessToken ; 
	private CommentAdapter commentAdapter;
	private ArrayList<CommentModel> commentArray ;
	private HttpAsyncTask asyncTask ; 
	private final int COMMENT_CODE  = 100;
	
	private int pageIndex = 1;
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
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		txtTopCenter.setText("所有评论");
		mAccessToken = AccessTokenKeeper.readAccessToken(this); 
		commentArray = new ArrayList<CommentModel>();
		commentAdapter = new CommentAdapter(CommentActivity.this, commentArray);
		mListView.setAdapter(commentAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		queryComment(pageIndex);
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
			
				queryComment(pageIndex);
				
			}

		});
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				pageIndex = pageIndex+1;
				queryComment(pageIndex);
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
				if(result!= null){
					JSONArray array = result.optJSONArray("comments"); 
					try{
					if(array.length()>0){
						if(isRefresh){
							commentArray.clear();
						}
						for (int i = 0; i < array.length(); i++) {
							
								CommentModel  comment = new CommentModel();
								JSONObject obj = array.optJSONObject(i);
								comment.setCreated_at(obj.getString("created_at"));
								comment.setId(obj.getLong("id"));
								comment.setText(obj.getString("text"));
								comment.setSource(obj.getString("source"));
								JSONObject userObject = obj.optJSONObject("user"); 
								if(userObject!=null){
									UserModel user  = new UserModel(userObject);
									comment.setUser(user);
								}
								comment.setMid(obj.getString("mid"));
								comment.setIdstr(obj.getString("idstr"));
								comment.setStatus(obj.optJSONObject("status"));
								comment.setTotal_number(result.getInt("total_number"));
								comment.setReply_comment(obj.optJSONObject("reply_comment"));
								commentArray.add(comment);
								commentAdapter.notifyDataSetChanged();
							
						}
						totalPage = result.getInt("total_number");
						
						if (pageIndex == 1) {
							mListView.onRefreshComplete(); // 下拉刷新完成
						} else {
							mListView.onLoadMoreComplete(); // 加载更多完成
						}
						
						if (pageIndex < totalPage ) {
							mListView.setCanLoadMore(true);
						} else {
							mListView.setNoMoreData();
							mListView.setCanLoadMore(false);
						}
					}
					}catch(Exception e){
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
	
	private void queryComment(int pageIndex ){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("count", 20);
		params.put("page", pageIndex);
		params.put("access_token", mAccessToken.getToken()); 
		QueryDataPraser praser = new QueryDataPraser(CommentActivity.this, Config.commentListAction, Config.GET);
		asyncTask = new HttpAsyncTask(CommentActivity.this, COMMENT_CODE, params, this, false);
		asyncTask.query(praser);
	}
}
