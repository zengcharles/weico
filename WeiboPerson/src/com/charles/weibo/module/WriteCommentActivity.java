package com.charles.weibo.module;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.base.BaseActivity;
import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.common.Constants;
import com.charles.weibo.datainterface.CallHttpResponse;
import com.charles.weibo.datainterface.QueryDataPraser;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.charles.weibo.ui.AppMsg;
import com.charles.weibo.utils.HttpAsyncTask;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;

public class WriteCommentActivity extends BaseActivity  implements CallHttpResponse {

	//-- -----------Top-----------
	private TextView btnCancel ;
	private TextView txtTitle ; 
	private TextView txtUserName;
	private TextView btnSend ;
	private TextView txtText; 
	
	private ImageView imgBarAt;
	private ImageView imgBarSharp;
	private ImageView imgBarEmo;
	private ImageView imgBarAdd;
	
	private String weicoID = ""; 
	private String commentID = "" ;
	
	 /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;
    Context mContext ;
    
    private AppMsg.Style style ;
	private AppMsg appMsg;
	private Handler handler  ;
	private Runnable run  ;
    
    // 網絡請求
    private final int QUERY_COMMENT_CODE  = 100 ; 
    private final int QUERY_COMMENT_REP_CODE = 101 ;
    private HttpAsyncTask asyncTask ; 

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_comment;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		txtText = (TextView)findViewById(R.id.txtText);
		btnSend = (TextView)findViewById(R.id.btnSend);
		btnCancel= (TextView)findViewById(R.id.btnCancel);
		
		txtUserName= (TextView)findViewById(R.id.txtUserName);
		imgBarAt = (ImageView)findViewById(R.id.imgBarAt);
		imgBarSharp = (ImageView)findViewById(R.id.imgBarSharp);
		imgBarEmo = (ImageView)findViewById(R.id.imgBarEmo);
		imgBarAdd = (ImageView)findViewById(R.id.imgBarAdd);
		
		
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		 // 获取当前已保存过的 Token
		txtUserName.setText(LoginUserInfoKeeper.readUserInfo(this).getName());
		mContext = WriteCommentActivity.this;
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        style = new AppMsg.Style(2000, R.color.confirm);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
        	weicoID = bundle.getString("weicoID");
        	commentID = bundle.getString("commentID");
        }
        txtText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(txtText.getText().length()>0){
					btnSend.setClickable(true);
					btnSend.setTextColor(getResources().getColor(R.color.font_orange));
				}else{
					btnSend.setClickable(true);
					btnSend.setTextColor(getResources().getColor(R.color.font_CCCCCC));
				}
			}
		});
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		btnSend.setOnClickListener(this); 
		btnCancel.setOnClickListener(this); 
		imgBarAt.setOnClickListener(this);
		imgBarSharp.setOnClickListener(this);
		imgBarEmo.setOnClickListener(this);
		imgBarAdd.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSend:
				String commentStr = txtText.getText().toString().trim();
				if(commentStr.length()==0){
					Toast.makeText(this, "評論不能爲空", Toast.LENGTH_SHORT).show();
					btnSend.setClickable(false);
					return ; 
				}
				if(commentStr.length()>140){
					Toast.makeText(this, "評論內容不能超過140字符", Toast.LENGTH_SHORT).show();
					return ; 
				}
				if(0<commentStr.length()&&commentStr.length()<140){
					btnSend.setClickable(false);
				}
				if(commentID==null&weicoID!=null){
					sendComment(weicoID);
				}else if(commentID!=null&commentID!=null){
					sendCommentResponse(commentID,weicoID); 
				}
				break;
		case R.id.btnCancel:
			this.finish();
			overridePendingTransition(R.anim.slide_right_in,R.anim.slide_right_out);
			break;
		case R.id.imgBarAt:
			break;
		case R.id.imgBarSharp:
			break;
		case R.id.imgBarEmo:
			break;
		case R.id.imgBarAdd:
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

	
	// 评论微博
	private void sendComment(String weicoID){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("comment", txtText.getText().toString().trim());
		params.put("id",weicoID);
		params.put("access_token", mAccessToken.getToken());
		QueryDataPraser  praser = new QueryDataPraser(mContext,Config.commentAction,Config.POST);
		asyncTask = new HttpAsyncTask(WriteCommentActivity.this, QUERY_COMMENT_CODE, params, this, true);
		asyncTask.query(praser);
	}
	
	// 回复某人的评论
	private void sendCommentResponse(String commentID,String weicoID){
		HashMap<String,Object> params = new HashMap<String,Object>();
		params.put("comment", txtText.getText().toString().trim());
		params.put("cid",commentID);
		params.put("id",weicoID);
		params.put("access_token", mAccessToken.getToken());
		QueryDataPraser  praser = new QueryDataPraser(mContext,Config.commentRepAction,Config.POST);
		asyncTask = new HttpAsyncTask(WriteCommentActivity.this, QUERY_COMMENT_REP_CODE, params, this, true);
		asyncTask.query(praser);
	}
	
	@Override
	public void onHttpSuccess(int requestCode, JSONObject result) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case QUERY_COMMENT_CODE:
			if(result!=null){
				try {
					String commentID = result.getString("id");
					if(commentID!=null){
						  appMsg = AppMsg.makeText(WriteCommentActivity.this, "評論成功", style);
						  appMsg.show();
						   handler = new Handler();
						   run = new Runnable() {
							@Override
							public void run() {
								// TODO Auto-generated method stub
								 finish();
							}
						};
						handler.postDelayed(run, 2000);  
						 
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			break;
		case QUERY_COMMENT_REP_CODE:
				 if(result!=null){
					  appMsg = AppMsg.makeText(WriteCommentActivity.this, "評論成功", style);
					  appMsg.show();
					  handler = new Handler();
					   run = new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							 finish();
							
						}
					};
					 handler.postDelayed(run, 2000);  
					
				 }
			break ;
		default:
			break;
		}
	}

	@Override
	public void onHttpError(int requestCode, int errCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(handler!=null){
			handler.removeCallbacks(run);
		}
		super.onDestroy();
		
	}
	
	
}
