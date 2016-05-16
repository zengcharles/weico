package com.charles.weibo.module;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.charles.weibo.R;
import com.charles.weibo.common.Constants;
import com.charles.weibo.entity.NearPlaceModel;
import com.charles.weibo.module.base.BaseActivity;
import com.charles.weibo.sdk.AccessTokenKeeper;
import com.charles.weibo.sdk.LoginUserInfoKeeper;
import com.charles.weibo.service.LocationHelper;
import com.charles.weibo.utils.Utils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.openapi.legacy.StatusesAPI;
import com.sina.weibo.sdk.openapi.models.ErrorInfo;
import com.sina.weibo.sdk.openapi.models.Status;
import com.sina.weibo.sdk.openapi.models.StatusList;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.tencentmap.mapsdk.map.GeoPoint;

public class WriteWeicoActivity extends BaseActivity  {
	private String TAG="WriteWeicoActivity" ; 
	private TextView txtText; 
	private TextView btnSend ; 
	private TextView btnCancel;
	private TextView txtUserName;
	private ImageView imgBarPicture;
	private ImageView imgBarAt;
	private ImageView imgBarSharp;
	private ImageView imgBarEmo;
	private ImageView imgBarAdd;
	private LinearLayout layLocation;
	private TextView txtLocation ; 
	private ImageView btnClearLoc ; 
	private LinearLayout layOpen; 
	private Context mContext;
	private  final int RESULT_CODE = 200 ; 
	private final String LOCATION = "LocationSetting";
//	private LoginUserInfoKeeper mUserKeeper ;
	 /** 当前 Token 信息 */
    private Oauth2AccessToken mAccessToken;
    /** 用于获取微博信息流等操作的API */
    private StatusesAPI mStatusesAPI;
    private LocationHelper mLocationHelper;
    /**定位附近地方的坐标*/
    private String npLat ="" ;           
    private String npLon ="" ; 

    
    
	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_write_weico;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		txtText = (TextView)findViewById(R.id.txtText);
		btnSend = (TextView)findViewById(R.id.btnSend);
		btnCancel= (TextView)findViewById(R.id.btnCancel);
		
		txtUserName= (TextView)findViewById(R.id.txtUserName);
		imgBarPicture = (ImageView)findViewById(R.id.imgBarPicture);
		imgBarAt = (ImageView)findViewById(R.id.imgBarAt);
		imgBarSharp = (ImageView)findViewById(R.id.imgBarSharp);
		imgBarEmo = (ImageView)findViewById(R.id.imgBarEmo);
		imgBarAdd = (ImageView)findViewById(R.id.imgBarAdd);
		layLocation = (LinearLayout)findViewById(R.id.layLocation) ;
		
		txtLocation = (TextView)findViewById(R.id.txtLocation) ; 
		btnClearLoc = (ImageView)findViewById(R.id.btnClearLoc);
		
		layOpen = (LinearLayout)findViewById(R.id.layOpen);
	}

	@Override
	public void initData() {
		 // 获取当前已保存过的 Token
		txtUserName.setText(LoginUserInfoKeeper.readUserInfo(this).getName());
		mLocationHelper = new LocationHelper(this);
		mContext = WriteWeicoActivity.this;
        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        // 对statusAPI实例化
        mStatusesAPI = new StatusesAPI(this, Constants.APP_KEY, mAccessToken);
        
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
		imgBarPicture.setOnClickListener(this);
		imgBarAt.setOnClickListener(this);
		imgBarSharp.setOnClickListener(this);
		imgBarEmo.setOnClickListener(this);
		imgBarAdd.setOnClickListener(this);
		layLocation.setOnClickListener(this);
		layOpen.setOnClickListener(this);
		btnClearLoc.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnSend:
				String weico = txtText.getText().toString().trim();
				if(weico.length()==0){
					Toast.makeText(this, "發送微博內容不能爲空", Toast.LENGTH_SHORT).show();
					btnSend.setClickable(false);
					return ; 
				}
				if(weico.length()>140){
					Toast.makeText(this, "發送微博內容超過140字符", Toast.LENGTH_SHORT).show();
					return ; 
				}
				if(weico.length()>0&&weico.length()<140){
					if(npLat.length()>0&&npLon.length()>0){
						mStatusesAPI.update(weico, npLat, npLon, mListener);
					}else{
						mStatusesAPI.update(weico, null, null, mListener);
					}
					btnSend.setClickable(false);
				}
				break;
		case R.id.btnCancel:
					finish();
			break;
		case R.id.imgBarPicture:
			break;
		case R.id.imgBarAt:
			break;
		case R.id.imgBarSharp:
			break;
		case R.id.imgBarEmo:
			break;
		case R.id.imgBarAdd:
			break;
		case R.id.layLocation:
			doMyLoc();
			break;
		case R.id.layOpen :
			/**
			 * 增加经纬度==zx
			 */
			/*SharedPreferences preferences =getSharedPreferences("LocationSetting", Context.MODE_PRIVATE);
			String location = preferences.getString("location", "");
		
			String loc[] = location.split(",");
			if(loc.length!=0){
				String lat = loc[0];  //纬度
				String lon = loc[1]; //经度
			}*/
			break;
		case R.id.btnClearLoc :
			npLat ="" ; 
			npLon = "";
			txtLocation.setText("显示位置");
			btnClearLoc.setVisibility(View.GONE);
		default:
			break;
		}
		
	}
	private void doMyLoc() {
		if (mLocationHelper.getLastLocation() != null) {
			animateTo(mLocationHelper.getLastLocation()); // 已有最新位置
		} else if (mLocationHelper.isStarted()) {
			toast(this, "正在定位"); // 当前正在定位
		} else {
			if(isGPSEnable(this)==false)
			{
				openOrCloseGPSModule();
			}
			toast(this, "开始定位");
			mLocationHelper.start(new Runnable() {
					public void run() {
						animateTo(mLocationHelper.getLastLocation());
					}
			});
		}
	}
	
	private void animateTo(TencentLocation location) {
		if (location != null) {
			GeoPoint c = Utils.of(location) ;
			double lat = c.getLatitudeE6()/1E6;
			double lng = c.getLongitudeE6()/1E6;
			toast(this, "经度"+lat+","+"纬度"+lng);
			if(lat>0&&lng>0){
				SharedPreferences preferences =getSharedPreferences(LOCATION, Context.MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putString("latitude",String.valueOf(lat));
				editor.putString("longitude",String.valueOf(lng));
				editor.commit();
				
				Intent intent  = new Intent ();
				intent.setClass(WriteWeicoActivity.this, LocationActivity.class);
				startActivityForResult(intent, RESULT_CODE);
				overridePendingTransition(R.anim.slide_left_in,R.anim.slide_left_out);
			}
			return;
		}
	}
	
	public boolean isGPSEnable(Context context) {
		String str = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}


public void openOrCloseGPSModule() {
	// 自动开启GPS代码
	/**
	 * 需配置权限 <uses-permission
	 * android:name="android.permission.WRITE_SETTINGS" /> <uses-permission
	 * android:name="android.permission.WRITE_SECURE_SETTINGS" />
	 */
	Intent gpsIntent = new Intent();
	gpsIntent.setClassName("com.android.settings",
			"com.android.settings.widget.SettingsAppWidgetProvider");
	gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
	gpsIntent.setData(Uri.parse("custom:3"));
	try {
		PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
	} catch (CanceledException e) {
		e.printStackTrace();
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
	
	  /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener mListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                if (response.startsWith("{\"statuses\"")) {
                    // 调用 StatusList#parse 解析字符串成微博列表对象
                    StatusList statuses = StatusList.parse(response);
                    if (statuses != null && statuses.total_number > 0) {
                        Toast.makeText(WriteWeicoActivity.this, 
                                "获取微博信息流成功, 条数: " + statuses.statusList.size(), 
                                Toast.LENGTH_LONG).show();
                    }
                } else if (response.startsWith("{\"created_at\"")) {
                    // 调用 Status#parse 解析字符串成微博对象
                    Status status = Status.parse(response);
                    Toast.makeText(WriteWeicoActivity.this, 
                            "发送一送微博成功, id = " + status.id, 
                            Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(WriteWeicoActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            ErrorInfo info = ErrorInfo.parse(e.getMessage());
            Toast.makeText(WriteWeicoActivity.this, info.toString(), Toast.LENGTH_LONG).show();
        }
    };

    static void toast(Context context, CharSequence text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	// TODO Auto-generated method stub
    	
    	switch (resultCode) {
		case RESULT_CODE:
			 	 Bundle bundle =data.getExtras() ; 
			 	 NearPlaceModel np =(NearPlaceModel)bundle.getSerializable("place_info");
			 	 String title = np.getTitle() ;
			 	 if(title!=null){
			 		txtLocation.setText(title);
			 		npLat =np.getLat() ;
			 		npLon = np.getLon() ; 
			 		btnClearLoc.setVisibility(View.VISIBLE);
			 	 }else{
			 		 btnClearLoc.setVisibility(View.GONE);
			 	 }
			break;

		default:
			break;
		}
    	super.onActivityResult(requestCode, resultCode, data);
    }
}
