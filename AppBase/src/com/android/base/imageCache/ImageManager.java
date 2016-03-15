package com.android.base.imageCache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.base.callback.ICallBack;
import com.android.base.utils.DipUtil;

public class ImageManager {

	private Context context;

	/** 图片加载工厂 */
	protected ImageFactory mImageFactory = null;

	/** 异步图片加载线程管理类 */
	protected ImageTaskExecutor mTaskExecutor = null;
	
	private int screenWidth,screenHeight;
	

	public ImageManager(Context context) {

		this.context = context;
		// 初始化图片加载工厂
		mImageFactory = new ImageFactory();
		// 初始化图片加载异步线程管理对象
		mTaskExecutor = new ImageTaskExecutor();
		
		WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		
		screenWidth = wm.getDefaultDisplay().getWidth();
		
		screenHeight = wm.getDefaultDisplay().getHeight();
	}

	public interface Custom {
		/**
		 * 每次图片加载完成后在setImageBitmap之前都会调用perform(Bitmap bitmap),它在异常线程中执行
		 * 
		 * @param bitmap
		 *            加载完成的图片资源
		 * @return 自定义处理完成的图片资源
		 */
		public Bitmap perform(Bitmap bitmap);
	}

	/**
	 * 为ImageView组件填充 Bitmap,此方法不保存图片到sdcard中，如果想保存，请调用重载方法inflateImage(final
	 * String imgUrl, final View view, final int error_bg_Id, final boolean
	 * isSaveImage, String SaveImagepath)
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图Id,例如R.drawable.error_bg
	 * @param custom
	 *            自定义图片处理类,每次图片加载完成后在setImageBitmap之前都会调用perform(Bitmap
	 *            bitmap),它在异常线程中执行
	 */
	public void inflateImage(final String imgUrl, final View view,
			final int error_bg_Id, final Custom custom,
			final boolean isWithReferer) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			if (bmp != null) {
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(width>screenWidth){
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				}
				if(width < screenWidth / 2){
					height = height * (screenWidth /2) / width;
					width = screenWidth / 2;
					view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
				}
				((ImageView) view).setImageBitmap(bmp);
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(width>screenWidth){
										view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
									}
									if(width < screenWidth / 2){
										height = height * (screenWidth /2) / width;
										width = screenWidth / 2;
										view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
									}
									((ImageView) view).setImageBitmap(bmp);
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											false, "", isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									false, "");
						}
						if (custom == null) {

						} else {
							bmp = custom.perform(bmp);
						}

						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {

		}
	}

	/**
	 * 为ImageView组件填充 Bitmap,此方法不保存图片到sdcard中，如果想保存，请调用重载方法inflateImage(final
	 * String imgUrl, final View view, final int error_bg_Id, final boolean
	 * isSaveImage, String SaveImagepath)
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图Id,例如R.drawable.error_bg
	 */
	public void inflateImage(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isWithReferer) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			if (bmp != null) {
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(width>screenWidth){
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				}
				if(width < screenWidth / 2){
					height = height * (screenWidth /2) / width;
					width = screenWidth / 2;
					view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
				}
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null && error_bg_Id != 0) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(width>screenWidth){
										view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
									}
									if(width < screenWidth / 2){
										height = height * (screenWidth /2) / width;
										width = screenWidth / 2;
										view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
									}
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											false, "", isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									false, "");
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 为ImageView组件填充 Bitmap
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图
	 * @param isSaveImage
	 *            是否保存图片，true 为保存，false为不保存
	 * @param SaveImagepath
	 *            保存图片的路径
	 */
	public void inflateImage(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			if (bmp != null) {
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
	
	/**
	 * 为ImageView组件填充 Bitmap
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图
	 * @param isSaveImage
	 *            是否保存图片，true 为保存，false为不保存
	 * @param SaveImagepath
	 *            保存图片的路径
	 */
	public void inflateImage2(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			final DipUtil util =new DipUtil();
			if (bmp != null) {
				int layoutHeight = util.convertDipToPix(context, 150);
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(width>screenWidth){
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				}else{
					width = width * layoutHeight/height;
					height = layoutHeight;
					view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
				}
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int layoutHeight = util.convertDipToPix(context, 150);
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(width>screenWidth){
										view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
									}else{
										width = width * layoutHeight/height;
										height = layoutHeight;
										view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
									}
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
	
	
	/**
	 * 为ImageView组件填充 Bitmap
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图
	 * @param isSaveImage
	 *            是否保存图片，true 为保存，false为不保存
	 * @param SaveImagepath
	 *            保存图片的路径
	 */
	public void inflateImage2(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer,final int viewHeight) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			final DipUtil util =new DipUtil();
			if (bmp != null) {
				int layoutHeight = util.convertDipToPix(context, viewHeight);
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(width>screenWidth){
					view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
				}else{
					width = width * layoutHeight/height;
					height = layoutHeight;
					view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
				}
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int layoutHeight = util.convertDipToPix(context, viewHeight);
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(width>screenWidth){
										view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
									}else{
										width = width * layoutHeight/height;
										height = layoutHeight;
										view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
									}
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}

	/**
	 * 为ImageView组件填充 Bitmap
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图
	 * @param isSaveImage
	 *            是否保存图片，true 为保存，false为不保存
	 * @param SaveImagepath
	 *            保存图片的路径
	 */
	public void inflateImageWithFrameLayout(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer,final boolean fillScreenWidth) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			final DipUtil util =new DipUtil();
			if (bmp != null) {
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(fillScreenWidth){
					height = height*screenWidth/width;
					width = screenWidth;
				}else{
					width = width * screenHeight/height;
					height = screenHeight;
				}
				view.setLayoutParams(new FrameLayout.LayoutParams(width, height));
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				view.postInvalidate();
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(fillScreenWidth){
										height = height*screenWidth/width;
										width = screenWidth;
									}else{
										width = width * screenHeight/height;
										height = screenHeight;
									}
									view.setLayoutParams(new FrameLayout.LayoutParams(width, height));
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
									view.postInvalidate();
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
	
	public void inflateImage(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer,final boolean fillScreenWidth) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			final DipUtil util =new DipUtil();
			if (bmp != null) {
				int width = bmp.getWidth();
				int height = bmp.getHeight();
				if(fillScreenWidth){
					height = height*screenWidth/width;
					width = screenWidth;
				}else{
					width = width * screenHeight/height;
					height = screenHeight;
				}
				view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									int width = bmp.getWidth();
									int height = bmp.getHeight();
									if(fillScreenWidth){
										height = height*screenWidth/width;
										width = screenWidth;
									}else{
										width = width * screenHeight/height;
										height = screenHeight;
									}
									view.setLayoutParams(new LinearLayout.LayoutParams(width, height));
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
	
	public void inflateImage3(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			final DipUtil util =new DipUtil();
			if (bmp != null) {
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
	
	/**
	 * 为ImageView组件填充 Bitmap
	 * 
	 * @param imgUrl
	 *            图片对应的下载地址
	 * @param view
	 *            图片要显示的view对象
	 * @param error_bg_Id
	 *            当无法下载图片时显示的背景图
	 * @param isSaveImage
	 *            是否保存图片，true 为保存，false为不保存
	 * @param SaveImagepath
	 *            保存图片的路径
	 *  @param  callBack 用于得到bitmap参数和设置 view的属性
	 */
	public void inflateImage(final String imgUrl, final View view,
			final int error_bg_Id, final boolean isSaveImage,
			final String SaveImagepath, final boolean isWithReferer,final ICallBack callBack) {

		try {
			view.setTag(imgUrl);
			Bitmap bmp = mImageFactory.getCachedImage(imgUrl);
			if (bmp != null) {
				callBack.call(view,bmp);
				((ImageView) view)
						.setBackgroundDrawable(new BitmapDrawable(bmp));
				return;
			} else {
				((ImageView) view).setImageBitmap(null);
				mTaskExecutor.addNewTask(new ImageTask(imgUrl) {

					Bitmap bmp = null;
					Handler inflateHandler = new Handler() {
						public void handleMessage(Message msg) {
							if (view != null && view instanceof ImageView) {
								if (imgUrl.equals(view.getTag())) {
									if (bmp == null) {
										bmp = BitmapFactory.decodeResource(
												context.getResources(),
												error_bg_Id);
									}
									callBack.call(view,bmp);
									((ImageView) view)
											.setBackgroundDrawable(new BitmapDrawable(
													bmp));
								}
							}
						}
					};

					@Override
					public void execute() {
						if (isWithReferer) {
							bmp = mImageFactory
									.getAsynchronousImageWithReferer(imgUrl,
											isSaveImage, SaveImagepath,
											isWithReferer);
						} else {
							bmp = mImageFactory.getAsynchronousImage(imgUrl,
									isSaveImage, SaveImagepath);
						}
						inflateHandler.sendMessage(new Message());
					}
				});
			}

		} catch (Exception e) {
		}
	}
}
