package com.android.base.exception;

public class NetException extends RuntimeException {
	
	/**
	 * 网络异常信息类
	 */
	private static final long serialVersionUID = 1L;
	
	private String msg;

	public NetException(String message) {
		this.msg = message;
	}

	public String getMsg() {
		return msg;
	}

}
