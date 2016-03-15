package com.android.base.encrypt;

/**
 * 加密辅助类，将传入的byte数组转换为AscII文本字符串
 */
public class EncryptHelper{
	/**
	 * 转化规则
	 */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
		'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
	
	/**
	 * 加密辅助类。提供将byte数组转换为ASCII文本
	 * @param  转换前的byte字符
	 * @return 转换之后的字符
	 */
	public static String toHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			sb.append(HEX_DIGITS[(bytes[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[bytes[i] & 0x0f]);
		}
		return sb.toString();
	}

}
