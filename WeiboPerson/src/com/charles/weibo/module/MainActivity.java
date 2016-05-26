package com.charles.weibo.module;

import java.io.File;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import com.charles.weibo.R;
import com.charles.weibo.utils.FileUtil;
import com.charles.weibo.utils.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener {

	// TabSpec 标签
	private final String TAB_HOME = "tab_home";
	private final String TAB_CONTECT = "tab_contect";
	private final String TAB_SETTING = "tab_setting";
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
 		setContentView(R.layout.activity_main);
		init();
	}

	private void init() {
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator("首页")
				.setContent(R.id.fragment_home));
		tabHost.addTab(tabHost.newTabSpec(TAB_CONTECT).setIndicator("信息")
				.setContent(R.id.fragment_cardcase));
		tabHost.addTab(tabHost.newTabSpec(TAB_SETTING).setIndicator("我的主页")
				.setContent(R.id.fragment_more));

		((RadioButton) findViewById(R.id.rb_home))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.rb_cont))
				.setOnCheckedChangeListener(this);
		((RadioButton) findViewById(R.id.rb_setting))
				.setOnCheckedChangeListener(this);

	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			switch (v.getId()) {
			case R.id.rb_home:
				tabHost.setCurrentTabByTag(TAB_HOME);
				break;
			case R.id.rb_cont:
				tabHost.setCurrentTabByTag(TAB_CONTECT);
				break;
			case R.id.rb_setting:
				tabHost.setCurrentTabByTag(TAB_SETTING);
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		/* 定期清理ImageLoader缓存 */
		// 若sharePrefence里上次清理时间为空或者间隔大于1个星期，清空SD缓存图片
		SharedPreferences _cacheImgInfo = getSharedPreferences(
				"cache_img_info", 0);
		String _lastClearDate = _cacheImgInfo.getString("lastClearDate", null);
		if (_lastClearDate != null) {
			// 对比今日日期和上次清理缓存日期，若>1星期，清理SD图片缓存
			if (TimeUtil.isNeedToClearImgCache(_lastClearDate)) {
				ImageLoader.getInstance().clearDiscCache();
				// 储存当前时间为lastClearDate
				_cacheImgInfo.edit()
						.putString("lastClearDate", TimeUtil.getCurDate())
						.commit();

				// 清除
				String filePath = Environment.getExternalStorageDirectory() +
		                "/Android/data/" + this.getPackageName() + "/imageCache/";
				FileUtil.delete(new File(filePath));
			}
		} else {
			// 储存当前时间为lastClearDate
			_cacheImgInfo.edit().putString("lastClearDate", TimeUtil.getCurDate()).commit();
		}
	}
	
	private boolean exitApp = false;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (exitApp) {
				finish();
			} else {
				exitApp = true;
				Toast.makeText(this, "再按一次返回鍵退出", Toast.LENGTH_SHORT).show();
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						exitApp = false;
					}
				}, 2000);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
