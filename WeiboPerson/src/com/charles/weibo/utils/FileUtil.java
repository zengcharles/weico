package com.charles.weibo.utils;

import java.io.File;

public class FileUtil {
	
	
	/**
	 * 删除文件
	 * @param file
	 */
	public final static void delete(File file) {
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] files = file.listFiles();
				for (File f : files) {
					delete(f);
				}
			}
			file.delete();
		}
	}
}
