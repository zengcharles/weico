package plant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.charles.weibo.R;
import com.charles.weibo.module.LoginActivity;

public class InitActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		handler.postDelayed(runnable, 0);
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(runnable);
	}
}
