package com.android.base.utils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;
import android.os.StatFs;

/**
 * SD 卡相关操作类
 *
 */
public class SDCardHelper {
	
	private static SDCardHelper mSDCardHelper = new SDCardHelper();
	
	private SDCardHelper() {
		
	}
	
	public static SDCardHelper getInstance() {
		return mSDCardHelper;
	}
	
	/**
	 * 获得sd卡的可用空间
	 * @return 可用字节
	 */
	public long getAvailableSdCardSize() {
		String path = Environment.getExternalStorageDirectory().getPath();
		StatFs statFs = new StatFs(path);
		long blockSize = statFs.getBlockSize();
		int available = statFs.getAvailableBlocks();
		return available * blockSize;
	}
	
	/**
	 * 是否安装了sd
	 * @return false 未安
	 */
	public boolean sdCardMounted() {
		final String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)
				&& !state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			return true;
		}
		return false;
	}
	
	public File creatSDDir(String dirFolderName) {
		String path = Environment.getExternalStorageDirectory().getPath()+"/"+ dirFolderName;
	    if (!new File(path).exists()){
	    	new File(path).mkdir();
	    }
        return  new File(path);
}
	
	/** 
	 * @Description: TODO(在SD卡上读取文件) 
	 * @return
	 * @throws IOException
	 ***************************************************
	  Version      Date         Author        Desc
	 ---------------------------------------------------
	  
	 ***************************************************
	 */
	public String readFromSDCardFile(String fileName,String folderName) throws IOException{
		//如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
		if(sdCardMounted()){
			String path = Environment.getExternalStorageDirectory().getPath();
			File targetFileFolder = new File(path+"/"+folderName);
			File readFile = new File(targetFileFolder, fileName);
			FileInputStream inputStream = new FileInputStream(readFile);
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = inputStream.read(buffer)) != -1){
				outStream.write(buffer, 0, len);
			}
			byte[] data = outStream.toByteArray();
			return new String(data);
		}else{
			return "";
		}
	}
	
	/** 
	 * @Description: TODO(在SD卡上创建文件) 
	 * @throws IOException
	 ***************************************************
	  Version      Date         Author        Desc
	 ---------------------------------------------------

	 ***************************************************
	 */
	public void writeToSDCardFile(String folderName,String content,String fileName) throws IOException{
		//如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
		if(sdCardMounted()){
			String path = Environment.getExternalStorageDirectory().getPath();
			File targetFileFolder;
			if(folderName.startsWith(path)){
				targetFileFolder =new File(folderName);
			}else{
				targetFileFolder= new File(path+"/"+folderName);
			}
			if(!targetFileFolder.exists()){
				targetFileFolder.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(new File(targetFileFolder, fileName));
			try {
				fileOutputStream.write(content.getBytes());
				fileOutputStream.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				fileOutputStream.close();
			}
		}
	}
	
	public void writeToSDCardFileByAppend(String folderName,String content,String fileName) throws IOException{
		//如果手机插入了SD卡，而且应用程序具有访问SD卡的权限
		if(sdCardMounted()){
			String path = Environment.getExternalStorageDirectory().getPath();
			File targetFileFolder;
			if(folderName.startsWith(path)){
				targetFileFolder =new File(folderName);
			}else{
				targetFileFolder= new File(path+"/"+folderName);
			}
			if(!targetFileFolder.exists()){
				targetFileFolder.mkdirs();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(new File(targetFileFolder, fileName),true);
			try {
				fileOutputStream.write(content.getBytes());
				fileOutputStream.flush();
			} catch (Exception e) {
				// TODO: handle exception
			}finally{
				fileOutputStream.close();
			}
		}
	}

    public boolean isFileFolderExist(String subFileFolder) {
    	    String path = Environment.getExternalStorageDirectory().getPath();
            File file = new File(path+"/" + subFileFolder);
            return file.exists();
    }

    /** 
     * @Description: TODO(删除SD卡中mnt/sdcard/中的目录) 
     * @return
     ***************************************************
      Version      Date         Author        Desc
     ---------------------------------------------------

     ***************************************************
     */
    public boolean deleteDir(File dir){
    	if (dir.isDirectory()){
    		String[] children = dir.list();
    		//递归删除目录中的子目录下
    		for (int i=0;i<children.length;i++){
    			boolean success = deleteDir(new File(dir,children[i]));
    			if (!success)
    				return false;
    		}
    	}
    	//目录此时为空，可以删
    	return dir.delete();
    }
    
    /** 
	 * @Description: TODO(删除具体某个文件) 
	 * @throws IOException
	 ***************************************************
	  Version      Date         Author        Desc
	 ---------------------------------------------------

	 ***************************************************
	 */
	public void deleteFile(String folderName,String fileName) throws IOException{
		 String path = Environment.getExternalStorageDirectory().getPath();
         	File targetFile = new File(path+"/"+folderName+"/"+fileName);
			if (targetFile.exists()){
				targetFile.delete();
			}
	}

}

