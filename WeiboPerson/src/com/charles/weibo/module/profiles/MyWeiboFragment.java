package com.charles.weibo.module.profiles;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.charles.weibo.R;
import com.charles.weibo.ui.customwebview.ProgressWebView;
import com.charles.weibo.ui.customwebview.WebChromeClientListener;

public class MyWeiboFragment extends Fragment {

	LayoutInflater inflater;
	View v;
	private ProgressWebView mWebView;
	private ImageView btnRefresh;
	private TextView txtTopCenter;
	private String profileUrl = "http://weibo.com/solelywj?is_all=1";
	private Context mContext;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// v= inflater.inflate(R.layout.fragment_myweibo,container, false);
		v = inflater.inflate(R.layout.profile, container, false);
		mContext = getActivity() ;
		initCmp();
		initData();
		initListerner();
		return v;
	}

	private void initCmp() {
		mWebView = (ProgressWebView) v.findViewById(R.id.mWebView);
		btnRefresh = (ImageView) v.findViewById(R.id.btnRefresh);
		txtTopCenter = (TextView) v.findViewById(R.id.txtTopCenter);
	}

	private void initData() {
		txtTopCenter.setText(mContext.getResources().getString(R.string.mine));
		WebSettings setting = mWebView.getSettings();
		setSettings(setting);
		//mWebView.setWebChromeClient(new WebChromeClient());
		mWebView.setWebViewClient(new WebViewClient());
		mWebView.loadUrl(profileUrl);
	}

	
	private void initListerner() {
		btnRefresh.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				mWebView.loadUrl(profileUrl);
			}
		});
	}

	@SuppressLint("NewApi")
	private void setSettings(WebSettings setting) {
		setting.setJavaScriptEnabled(true);
		setting.setAllowFileAccess(true);
		// setting.setBuiltInZoomControls(true);
		// setting.setDisplayZoomControls(false);
		// setting.setSupportZoom(true);

		setting.setDomStorageEnabled(true);
		setting.setDatabaseEnabled(true);
		setting.setSaveFormData(false);
		setting.setAppCacheEnabled(true);
		setting.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 全屏显示
		setting.setLoadWithOverviewMode(true); // <==== 一定要设置为false，不然有声音没图像
		setting.setUseWideViewPort(true);
		setting.setPluginState(PluginState.ON);
	}


}
