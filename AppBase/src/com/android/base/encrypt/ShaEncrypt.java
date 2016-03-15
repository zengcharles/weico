package com.android.base.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * sha加密方式<br>
 * 使用方法：<br>
 *
 */
public class ShaEncrypt implements IEncrypt {
	
	/**
	 * 对字符串sha加密
	 * @param  加密前的字符串
	 * @return 加密后的字符串
	 */
	public String encrypt(String str) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(str.getBytes());
			byte[] mess = digest.digest();
			return EncryptHelper.toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return str;
	}


}