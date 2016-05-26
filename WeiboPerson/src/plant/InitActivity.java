package plant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.android.base.config.AppConfig;
import com.charles.weibo.R;
import com.charles.weibo.module.LoginActivity;
import com.charles.weibo.module.base.BaseActivity;
import com.charles.weibo.receiver.WsResultReceiver;
import com.charles.weibo.upgrade.CheckVersionIService;
import com.charles.weibo.upgrade.NotificationUpdateActivity;

public class InitActivity extends BaseActivity implements WsResultReceiver.Receiver {

	// 检测版本是否完成
	private boolean isCheckVersionDone = false;
	
	private AppConfig app;
	public WsResultReceiver wsReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onReceiveResult(int resultCode, Bundle resultData) {
		// TODO Auto-generated method stub
		switch (resultCode) {
		case CheckVersionIService.STATUS_CHECKING:
			break;
		case CheckVersionIService.STATUS_NO_UPGRADE:
			isCheckVersionDone = true;
			handler.sendEmptyMessage(1);
			break;
		case CheckVersionIService.STATUS_CAN_UPGRADE:
			isCheckVersionDone = true;
			handler.sendEmptyMessage(2);
			break;
		case CheckVersionIService.STATUS_MUST_UPGRADE:
			handler.sendEmptyMessage(2);
			break;
		case CheckVersionIService.STATUS_ERROR:
			isCheckVersionDone = true;
			handler.sendEmptyMessage(1);
			break;
		}
	}
	
	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.activity_init;
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		app = (AppConfig)this.getApplication();
		
		/* 初始化receiver */
		wsReceiver = new WsResultReceiver(new Handler());
		wsReceiver.setReceiver(this);
		
		//方便測試，关闭检查更新，直接进入APP
		//startCheckVersionIService();
		isCheckVersionDone = true ;
		handler.sendEmptyMessage(1);
		
	}

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				break;
			case 1:
				if(isCheckVersionDone){
					Intent intent = new Intent();
					intent.setClass(InitActivity.this, LoginActivity.class);
					startActivity(intent);
				}
				finish();
				break;
			case 2:
				String title = "检测到新版本";
				String message = "下载更新？";
				showUpdateDialog(title,message);
				break;
			}
		};
	};
			

	private void showUpdateDialog(String title,String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent it = new Intent(InitActivity.this,NotificationUpdateActivity.class);
				startActivity(it);
				app.setDownload(true);
				finish();
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(1);
				dialog.dismiss();
				
			}
		});
		builder.show();
	}
	
	/** 啟動檢測版本的Service */
	private void startCheckVersionIService() {
		if (hasNetWork()) {
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, this,CheckVersionIService.class);
			intent.putExtra("receiver", wsReceiver);
			startService(intent);
		} else {
			isCheckVersionDone = true;
			handler.sendEmptyMessage(1);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//handler.removeCallbacks(runnable);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
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
}
