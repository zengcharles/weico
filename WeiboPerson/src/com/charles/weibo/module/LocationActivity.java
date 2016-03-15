package com.charles.weibo.module;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.base.BaseActivity;
import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.adapter.NearPlaceAdapter;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.entity.NearPlaceModel;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.ui.SingleLayoutListView;
import com.charles.weibo.ui.SingleLayoutListView.OnLoadMoreListener;
import com.charles.weibo.ui.SingleLayoutListView.OnRefreshListener;
import com.charles.weibo.utils.HttpAsyncTask;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

public class LocationActivity extends BaseActivity implements CallHttpResponse{
	//--------Top-----------
	private TextView txtTopCenter ;
	private ImageView btnBack ;
	
	private final String LOCATION = "LocationSetting"; 
	private Oauth2AccessToken mAccessToken;
	
	private HttpAsyncTask asyncTask  ; 
	private final int  QUERY_CODE = 100  ; 
	private  final int RESULT_CODE = 200 ; 
	private int  totalPage = 0 ; 
	private int  pageIndex = 1 ; 
	
	private ArrayList<NearPlaceModel> npList ; 
	private NearPlaceAdapter npAdapter; 
	private SingleLayoutListView mListView ;
	
	private String lat  ;
	private String lng  ;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_location;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		mListView = (SingleLayoutListView)findViewById(R.id.mListView); 
		txtTopCenter=(TextView)findViewById(R.id.txtTopCenter) ;
		btnBack =(ImageView)findViewById(R.id.btnBack) ;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		txtTopCenter.setText(getResources().getString(R.string.loaction));
		npList = new ArrayList<NearPlaceModel>();
		npAdapter = new NearPlaceAdapter(npList, this);
		mListView.setAdapter(npAdapter);
		mListView.setCanLoadMore(true);
		mListView.setCanRefresh(true);
		mListView.setAutoLoadMore(true);
		SharedPreferences preferences =getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
		lat = preferences.getString("latitude", "");
		lng = preferences.getString("longitude", "");
		mAccessToken = AccessTokenKeeper.readAccessToken(this) ;
		if(!lat.equals("")&&!lng.equals("")){
			queryNearByPlace (Float.valueOf(lat),Float.valueOf(lng),pageIndex);
		}
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		mListView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				pageIndex = 1 ; 
				queryNearByPlace (Float.valueOf(lat),Float.valueOf(lng),pageIndex);
			}
		});
		mListView.setOnLoadListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				pageIndex =pageIndex+1 ;
				if(pageIndex<totalPage){
					queryNearByPlace (Float.valueOf(lat),Float.valueOf(lng),pageIndex);
				}else{
					mListView.setCanLoadMore(false);
				}
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				NearPlaceModel np = npList.get(position-1);
				Intent intent = new Intent();
				//intent.setClass(LocationActivity.this, WriteWeicoActivity.class);
				intent.putExtra("place_info", np);
				setResult(RESULT_CODE, intent);
				finish();
				overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish() ;
				overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
			}
		}) ; 
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
		case QUERY_CODE:
			    if(result!=null){
			    	try {
			    		totalPage = Integer.valueOf(result.getString("total_number"))/10;
			    		if(pageIndex==1){
			    			npList.clear();
			    			mListView.onRefreshComplete();
						}else{
							mListView.onLoadMoreComplete();
						}
			    		
			    		JSONArray data = result.optJSONArray("pois");
						 if (data!=null){
							 for (int i = 0; i < data.length(); i++) {
								JSONObject obj = data.optJSONObject(i);
								if(obj!=null){
									NearPlaceModel np = new NearPlaceModel(obj) ;
									npList.add(np); 
								}
							}
							 npAdapter.notifyDataSetChanged();
						 }
						 
						
						 if (pageIndex < totalPage ) {
							 mListView.setCanLoadMore(true);
							} else {
								mListView.setNoMoreData();
								mListView.setCanLoadMore(false);
							}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
	
	private void queryNearByPlace(float lat, float lng ,int pageIndex){
		HashMap<String,Object> params =new  HashMap<String,Object>();
		params.put("access_token", mAccessToken.getToken());
		params.put("lat", lat);
		params.put("long", lng);
		params.put("page", pageIndex);
		QueryDataPraser paraser = new QueryDataPraser(LocationActivity.this, Config.nearByPlaceAction, Config.GET);
		asyncTask = new HttpAsyncTask(this, QUERY_CODE, params, this, false);
		asyncTask.query(paraser);
	}

}
