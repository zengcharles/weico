package com.charles.weibo.module;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.charles.weibo.R;

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

}
