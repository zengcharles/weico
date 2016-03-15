package com.android.framework.test;

import java.io.IOException;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.base.SerialPortActivity;

/**
 *  串口编程实现步骤
 *  1、在manifest配置文件中将application项配置为android:name="com.android.base.Application"
 *  2、串口类继承 SerialPortActivity
 *  3、示例代码如本类文件（本实例基于技术密码键盘）
 */

public class SendAndReceiveActivity extends SerialPortActivity {
	
	EditText text1,text2 ;
	Button button;
	public static char STX='\u0002';  // 串口操作的启动符
	
	//接收结果处理函数
	@Override
	protected void onDataReceived(byte[] buffer, int size) {
		// TODO Auto-generated method stub
		System.out.println(String.valueOf(buffer));
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int settingBaudrate() {
		// TODO Auto-generated method stub
		return 9600;
	}

	@Override
	protected String settingSerialPort() {
		// TODO Auto-generated method stub
		return "/dev/ttyUSB0";
	}

	@Override
	public void initAppParams() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int setLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.sendandreceive;
	}

	@Override
	public void initCmp() {
		// TODO Auto-generated method stub
		button =(Button)findViewById(R.id.button1);
		text1=(EditText)findViewById(R.id.editText1);
		text2=(EditText)findViewById(R.id.editText2);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					String str= text1.getText().toString();
					char high =STX;
					String result =high +str;
					System.out.println(result.getBytes());
					mOutputStream.write(result.getBytes());
					//mOutputStream.write('\n');
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View setLayoutView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
