package com.android.base.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密<br>
 * 使用方法:<br>
 *        new MD5Encrypt().encrypt(paramter);
 */
public class MD5Encrypt implements IEncrypt{
	
	/**
	 * 对字符串MD5加密
	 * @param  加密前的字符串
	 * @return 加密后的字符串
	 */
	public String encrypt(String str){
		try {
			MessageDigest digest = MessageDigest.getInstance("md5");
			digest.update(str.getBytes());
			byte[] mess = digest.digest();
			return EncryptHelper.toHexString(mess);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return str;
	}

}