package com.android.base.encrypt;

/**
 * 加密接口类
 * 
 */
public interface IEncrypt {
	/**
	 * 对字符串加密
	 * @param  加密前的字符串
	 * @return 加密后的字符串
	 */
	String encrypt(String str);

}