package com.android.base.imageCache;


import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.base.R;
import com.android.base.config.AppConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.process.BitmapProcessor;
import com.nostra13.universalimageloader.utils.StorageUtils;



/**
 * ImageLoader工具类函数
 * @author hrz
 *
 */
public class ImageCacheUtil {

	/**
	 * 必须在application create的时候调用	 * 
	 * @param context
	 */

	public static void init(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context,
				AppConfig.IMAGE_CACHE_PATH);

		
		ImageLoaderConfiguration cfg = new ImageLoaderConfiguration.Builder(context)
		.threadPriority(Thread.NORM_PRIORITY - 2)
		.denyCacheImageMultipleSizesInMemory()
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCache(new UnlimitedDiscCache(cacheDir))
		.threadPoolSize(5)
		.threadPriority(Thread.MIN_PRIORITY + 3)
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new UsingFreqLimitedMemoryCache(2000000)) // You can pass your own memory cache implementation
		.discCache(new UnlimitedDiscCache(cacheDir)) // You can pass your own disc cache implementation
		//.writeDebugLogs()
		.build();

		ImageLoader.getInstance().init(cfg);
	}
	
	// options
	public interface OPTIONS {
		public DisplayImageOptions default_options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.pic_loading)
		.showImageForEmptyUri(R.drawable.pic_loading)
		.showImageOnFail(R.drawable.pic_loading)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.considerExifParams(true)
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
		.displayer(new RoundedBitmapDisplayer(0))
		.build();

	}
	
}
