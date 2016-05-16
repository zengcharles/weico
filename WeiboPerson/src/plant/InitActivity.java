package plant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.charles.weibo.R;
import com.charles.weibo.Config.Config;
import com.charles.weibo.module.LoginActivity;
import com.charles.weibo.utils.DeviceAndAppInfoUtils;
import com.charles.weibo.utils.HttpUtils;



public class InitActivity extends Activity {
	
	//检测版本是否完成
	private boolean mIsCheckVersionDone = false;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		initCheckVersion();
		// 用于加价优化性能
		handler.postDelayed(runnable, 1000);
	}
	  
	Handler handler = new Handler();
	Runnable runnable = new Runnable(){
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Intent intent =new  Intent();
			intent.setClass(InitActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}
	};
	
	//检测新版本
		private void initCheckVersion(){
			Runnable checkVersion = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpUtils utils = new HttpUtils(InitActivity.this);
					String remoteVersion = utils.getStringDataByHttp(Config.UPDATE_APP_URL);
					if (null == remoteVersion || "".equals(remoteVersion)) {
						return;
					}
					String localVersionCode = DeviceAndAppInfoUtils.getInstance().getAppVersionCode(InitActivity.this);
					Calendar calendar1 = Calendar.getInstance();
					Calendar calendar2 = Calendar.getInstance();
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					try {
						calendar1.setTime(format.parse(remoteVersion));
						calendar2.setTime(format.parse(localVersionCode));
						if (calendar1.after(calendar2)) {
							handler.sendEmptyMessage(0);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//isThreadRunning =false;
				}
			};
			checkVersion.run();
		}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}
}
