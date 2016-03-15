/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.android.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.InvalidParameterException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android_serialport_api.SerialPort;

import com.android.base.config.AppConfig;
import com.android.base.utils.HexToCnUtil;

public abstract class SerialPortActivity extends BaseActivity {

	protected Application mApplication;
	public SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	public ReadThread mReadThread;
	public int resultCode=-1;

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null) return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size,resultCode);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}
	
	public char[] getChars(byte[] bytes,int size) {
		Charset cs = Charset.forName(AppConfig.CharSet);
		ByteBuffer bb = ByteBuffer.allocate(size);
		for (int i = 0; i < size; i++) {
			bb.put(bytes[i]);
		}		
		bb.flip();
		CharBuffer cb = cs.decode(bb);
		return cb.array();
	}
	
	public void sendSerialCMDRequest(int requestCode,byte[] command) throws IOException{
		resultCode = requestCode;
		mOutputStream.write(command);
	}
	
	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SerialPortActivity.this.finish();
			}
		});
		b.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (Application) getApplication();
		try {
			mSerialPort = mApplication.getSerialPort(settingSerialPort(),settingBaudrate());
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
//			mReadThread = new ReadThread();
//			mReadThread.start();
		} catch (SecurityException e) {
			DisplayError(R.string.error_security);
		} catch (IOException e) {
			DisplayError(R.string.error_unknown);
		} catch (InvalidParameterException e) {
			DisplayError(R.string.error_configuration);
		}
	}
	//设置串口文件位置
	protected abstract String settingSerialPort();
	//设置波特率
	protected abstract int settingBaudrate();

	protected abstract void onDataReceived(final byte[] buffer, final int size,final int resultCode);

	@Override
	protected void onDestroy() {
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		super.onDestroy();
	}
	
	
	protected String decodeKeyBoardHexString(String str) {
		StringBuffer buffer= new StringBuffer();
		for (int i = 0; i < str.length()/2; i++) {
			String temp=HexToCnUtil.deUnicode("00"+str.substring(i*2,(i+1)*2));
			buffer.append(temp);
		}
		return buffer.toString();
	}
	
	public void stopThread(){
		if (mReadThread != null)
			mReadThread.interrupt();
	}
	
	public void startThread(){
		if (mReadThread != null)
			mReadThread.start();
		else{
			mReadThread = new ReadThread();
			mReadThread.start();
		}
	}
	
}
