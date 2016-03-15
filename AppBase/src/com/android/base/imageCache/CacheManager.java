package com.android.base.imageCache;

import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import android.graphics.Bitmap;

import com.android.base.net.ApacheHttpUtils;

/**
 * 缓存图片的管理者，用来维护这个程序的图片缓存，如：下载，本地读取，操作缓存集合等。
 * 
 */
public class CacheManager {

	/** 存放缓存图片的集合，Value 为受软引用管理的 Bitmap */
	private Map<String, SoftReference<Bitmap>> mImgCache = null;

	/** 管理 ImageInfo 的优先级队列，当缓存集合已满时，由来它决定优先移除哪张缓存图片 */
	private Queue<String> mImgQueue = null;

	/** 缓存集合的最大容量 */
	private int mCapacity = 0;

	/**
	 * 用于比较两个ImageInfo的时间戳
	 * 
	 */
	private class TimeStampComparator implements Comparator<ImageInfo>,
			Serializable {

		private static final long serialVersionUID = 1360150786631409529L;

		@Override
		public int compare(ImageInfo object1, ImageInfo object2) {
			long l1 = object1.getTimeStamp();
			long l2 = object2.getTimeStamp();
			return (int) (l1 - l2);
		}
	}

	public CacheManager(int capacity) {
		mCapacity = capacity;
		mImgCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
				capacity);
		mImgQueue = new PriorityQueue<String>();
	}

	/**
	 * 加载本地图片
	 * 
	 * @param imageInfo
	 * @param path
	 *            图片保存路径
	 * @return Bitmap
	 */
	public Bitmap loadLocalImage(String imgUrl, String path) {
		try {
			ImageHelper imageHelper = new ImageHelper();
			Bitmap bitmap = imageHelper.loadSDImage(imgUrl, path);
			if (bitmap != null) {
				// Log.e("updateImageQueue","updateImageQueue");
				updateImageQueue(imgUrl, bitmap);
				// Log.e("storeImageToSdCardstart","updateImageQueuestart");
				// imageHelper.storeImageToSdCard(imgUrl, bitmap);
				// Log.e("storeImageToSdCardend","updateImageQueuesend");
			}

			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

	/**
	 * 加载网络图片
	 * 
	 * @param imgUrl
	 *            图片下载地址
	 * @param isSaveImage
	 *            是否保存数据
	 * @param path
	 *            图片保存sdcard的路径
	 * @return Bitmap
	 */
	public Bitmap loadImageWithUrl(String imgUrl, boolean isSaveImage,
			String path) {
		ImageHelper imageHelper = new ImageHelper();
		Bitmap bitmap = imageHelper.downloadImageWithUrl(imgUrl);
		if (bitmap != null) {
			updateImageQueue(imgUrl, bitmap);
			if (isSaveImage) {
				imageHelper.storeImageToSdCard(imgUrl, bitmap, path);
			}
		}

		return bitmap;
	}

	/**
	 * 从缓存集合中获得软引用图片
	 * 
	 * @param info
	 * @return Bitmap
	 */
	public Bitmap getSoftReferenceImage(String imgUrl) {
		SoftReference<Bitmap> softRef = mImgCache.get(imgUrl);
		if (softRef != null) {
			Bitmap bmp = softRef.get();
			if (bmp != null) {
				// LogPrinter.debugError("~~~~~~~~~~~~~getSoftReferenceImage-----"
				// + info.getImageUrl());
				updateImageQueue(imgUrl, bmp);
				return bmp;
			} else {
				mImgCache.remove(imgUrl);
				mImgQueue.remove(imgUrl);
			}
		}
		return null;
	}

	/**
	 * 更新缓存图片集合与优先级队列
	 * 
	 * @param info
	 * @param bmp
	 */
	private synchronized void updateImageQueue(String imgUrl, Bitmap bmp) {

		/** 如果已经达到缓存容量则释放一张软引用图片 */
		if (mImgQueue.size() >= mCapacity) {
			releaseOne();
		}

		if (imgUrl != null) {
			/** 向缓存集合添加一张图片 */
			if (!mImgCache.containsKey(imgUrl)) {
				mImgQueue.offer(imgUrl);
				mImgCache.put(imgUrl, new SoftReference<Bitmap>(bmp));

			} else {
				/**
				 * 如果集合里已存在该图片，则更新该图片对应的 ImageInfo, 使该图片的时间戳为最新，不至于被优先移除
				 */
				for (String imgInfo : mImgQueue) {
					if (imgInfo.equals(imgUrl)) {
						mImgQueue.remove(imgInfo);
						mImgQueue.offer(imgUrl);
					}
				}// end for
			}// end else
		}
	}

	/**
	 * 释放一张软引用图片
	 */
	private synchronized void releaseOne() {
		String imgInfo = mImgQueue.poll();
		SoftReference<Bitmap> softBmp = mImgCache.remove(imgInfo);
		if (softBmp != null) {
			Bitmap bitmap = softBmp.get();
			if (bitmap != null) {
				if (bitmap.isRecycled()) {
					bitmap.recycle();
				}
				bitmap = null;
			}
		}
	}

	/**
	 * 回收缓存集合中的所有图片
	 */
	public synchronized void recycleAll() {
		Set<String> set = mImgCache.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String imgInfo = iterator.next();
			SoftReference<Bitmap> softBmp = mImgCache.get(imgInfo);
			if (softBmp != null) {
				Bitmap bitmap = softBmp.get();
				if (bitmap != null) {
					if (!bitmap.isRecycled()) {
						bitmap.recycle();
					}
					bitmap = null;
				}
			}
		}
		mImgCache.clear();
		mImgQueue.clear();
		mImgCache = null;
		mImgQueue = null;
		System.gc();

		try {
			Thread.sleep(50); // 促进系统垃圾回收的调用
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Bitmap loadImageWithUrl(String imgUrl, boolean isSaveImage,
			String path,boolean isWithReferer) {
		ImageHelper imageHelper = new ImageHelper();
		
		Bitmap bitmap =null;
		if(isWithReferer){
			bitmap = new ApacheHttpUtils().getBitmap(imgUrl);
		}else{
			bitmap = imageHelper.downloadImageWithUrl(imgUrl);
		}
		if (bitmap != null) {
			updateImageQueue(imgUrl, bitmap);
			if (isSaveImage) {
				imageHelper.storeImageToSdCard(imgUrl, bitmap, path);
			}
		}

		return bitmap;
	}
}
