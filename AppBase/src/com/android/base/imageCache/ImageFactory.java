package com.android.base.imageCache;

import android.graphics.Bitmap;

import com.android.base.config.AppConfig;

public class ImageFactory {
    
    
    private CacheManager mCacheManager = null;
    
    /**
     * 通过传入的ImageInfo 对象获取对应的Bitmap
     * @param imageInfo
     * @return
     */
    public Bitmap getCachedImage(String imgUrl) {
        if (mCacheManager == null) {
            mCacheManager = new CacheManager(AppConfig.taskCount);
        }
        return mCacheManager.getSoftReferenceImage(imgUrl);
    }
    
    
    /**
     * 获取图片
     * @param imageInfo 
     * @return Bitmap
     */
    public Bitmap getAsynchronousImage(String imgUrl,boolean isSaveImage,String SaveImagepath) {
        Bitmap bmp = null;
        bmp = mCacheManager.loadLocalImage(imgUrl,SaveImagepath);
        if (bmp == null) {
            bmp = mCacheManager.loadImageWithUrl(imgUrl,isSaveImage,SaveImagepath);
        }
        return bmp;
    }
    
    /**
     * 获取图片
     * @param imageInfo 
     * @return Bitmap
     */
    public Bitmap getAsynchronousImageWithReferer(String imgUrl,boolean isSaveImage,String SaveImagepath,boolean isWithReferer) {
        Bitmap bmp = null;
        bmp = mCacheManager.loadLocalImage(imgUrl,SaveImagepath);
        if (bmp == null) {
        	if(isWithReferer){
        		bmp = mCacheManager.loadImageWithUrl(imgUrl,isSaveImage,SaveImagepath,isWithReferer);
        	}else{
        		bmp = mCacheManager.loadImageWithUrl(imgUrl,isSaveImage,SaveImagepath);
        	}
        }
        return bmp;
    }
    
    /**
     * 回收图片
     */
    public void recycleBitmaps() {
        if (mCacheManager != null) {
            mCacheManager.recycleAll();
            mCacheManager = null;
        }
    }
    
}


