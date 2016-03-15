package com.android.base.imageCache;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;

import com.android.base.config.AppConfig;
import com.android.base.net.ApacheHttpUtils;
import com.android.base.utils.SDCardHelper;


/**
 * 图片加载辅助类，类中提供了加载本地图片loadSDImage（）操作、downloadImageWithUrl网络请求操作
 * 
 */
public class ImageHelper {

	/** 缓存图片的绝对路径*/
	public String mCachePath = "";

	/** 压缩质量参数 */
	public static final int COMPRESS_QUALITY = 100;

	public ImageHelper() {
		mCachePath = AppConfig.IMAGE_CACHE_PATH;
	}

	/**
	 * 加载本地图片
	 * 
	 * @param imgUrl 图片地址
	 * @param SaveImagepath
	 *            图片保存路径
	 * @return Bitmap
	 * 
	 */
	public Bitmap loadSDImage(String imgUrl, String SaveImagepath) {
		Bitmap bitmap = null;
		FileInputStream fis = null;
		try {
			String imgName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
			if (SDCardHelper.getInstance().sdCardMounted()) {
				File imgFile = new File(SaveImagepath + imgName);
				if (!imgFile.exists()) {
					return null;
				}
				fis = new FileInputStream(imgFile);
				byte[] bytes = stream2Bytes(fis);
				
				//2.为位图设置100K的缓存

				BitmapFactory.Options opts=new BitmapFactory.Options();

				opts.inTempStorage = new byte[100 * 1024];

				//3.设置位图颜色显示优化方式

				//ALPHA_8：每个像素占用1byte内存（8位）

				//ARGB_4444:每个像素占用2byte内存（16位）

				//ARGB_8888:每个像素占用4byte内存（32位）

				//RGB_565:每个像素占用2byte内存（16位）

				//Android默认的颜色模式为ARGB_8888，这个颜色模式色彩最细腻，显示质量最高。但同样的，占用的内存//也最大。也就意味着一个像素点占用4个字节的内存。我们来做一个简单的计算题：3200*2400*4 bytes //=30M。如此惊人的数字！哪怕生命周期超不过10s，Android也不会答应的。

				opts.inPreferredConfig = Bitmap.Config.RGB_565;

				//4.设置图片可以被回收，创建Bitmap用于存储Pixel的内存空间在系统内存不足时可以被回收

				opts.inPurgeable = true;

				//5.设置位图缩放比例

				//width，hight设为原来的四分一（该参数请使用2的整数倍）,这也减小了位图占用的内存大小；例如，一张//分辨率为2048*1536px的图像使用inSampleSize值为4的设置来解码，产生的Bitmap大小约为//512*384px。相较于完整图片占用12M的内存，这种方式只需0.75M内存(假设Bitmap配置为//ARGB_8888)。

//				opts.inSampleSize = 4;

				//6.设置解码位图的尺寸信息

				opts.inInputShareable = true;  

				//7.解码位图

				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,opts);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * 加载网络图片
	 * 
	 * @param info
	 * @return Bitmap
	 */
	public Bitmap downloadImageWithUrl(String imgUrl) {
		ApacheHttpUtils utils =new ApacheHttpUtils();
		return utils.downloadImageWithUrl(imgUrl);
	}

	/**
	 * 将流转成字节数组
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public byte[] stream2Bytes(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = 0;
		while ((length = is.read(buffer, 0, 1024)) != -1) {
			baos.write(buffer, 0, length);
		}
		baos.flush();
		return baos.toByteArray();
	}

	/**
	 * 获得图片的占用空间大小
	 * 
	 * @param bmp
	 * @return 图片占用的字节数
	 */
	private int getImageSize(Bitmap bmp) {
		try {
			if (bmp == null) {
				return 0;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bmp.compress(CompressFormat.PNG, COMPRESS_QUALITY, baos);
			int size = baos.size();
			baos.flush();
			baos.close();
			return size;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 将图片存入sdCard
	 * 
	 * @param imgUrl
	 *            图片网络地址
	 * @param bmp
	 *            网络请求过来的bitmap对象
	 * @param SaveImagepath
	 *            图片保存到sdcard 的路径
	 * @return 存储结果
	 */
	public String storeImageToSdCard(String imgUrl, Bitmap bmp,
			String SaveImagepath) {
		if (!SDCardHelper.getInstance().sdCardMounted()) {
			return "Memory card not found";
		}
		if (SDCardHelper.getInstance().getAvailableSdCardSize() < getImageSize(bmp)) {
			return "Not enough memory";
		}
		FileOutputStream fos = null;
		try {
			File dir = new File(SaveImagepath);
			if (!dir.exists()) {
				// LogManager.info(mCachePath);
				if (!dir.mkdirs()) {
					// LogManager.error("Failed to create dir ! ");
				}
			}

			String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
			File imgFile = new File(dir, fileName);
			if (imgFile.exists()) {
				imgFile.delete();
			}
			if (imgFile.createNewFile()) {
				fos = new FileOutputStream(imgFile);
				bmp.compress(CompressFormat.PNG, COMPRESS_QUALITY, fos);
			} else {
				// LogManager.error("Failed to create file ! ");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "Save failed";
		} catch (IOException e) {
			e.printStackTrace();
			return "Save failed";
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return "Save failed";
			}
		}
		return "Save successfully";
	}

}
