package com.charles.weibo.ui;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.charles.weibo.R;

public class LoadingProgress extends Dialog{
	private Context context = null;
	private static LoadingProgress loadingProgress = null;

	public LoadingProgress(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		this.context = context;
	}
	
    public LoadingProgress(Context context, int theme) {
		// TODO Auto-generated constructor stub
    	super(context, theme);
	}

    /**
     * 创建对话
     * @param context
     * @return
     */
    public static LoadingProgress createDialog(Context context){
    	loadingProgress = new LoadingProgress(context,R.style.LoadingDialog);
    	loadingProgress.setContentView(R.layout.loading);
    	loadingProgress.getWindow().getAttributes().gravity = Gravity.CENTER;
    	return loadingProgress;
    }
    
    @Override
    public void onWindowFocusChanged(boolean hasFocus){
    	if(loadingProgress == null){
    		return;
    	}
    	ImageView loadingImage = (ImageView)loadingProgress.findViewById(R.id.loadingImage);
    	AnimationDrawable animationDrawable = (AnimationDrawable)loadingImage.getBackground();
    	animationDrawable.start();
    }
    
    
    /**
     * 设置标题
     * @param strTitle
     * @return
     */
    public LoadingProgress setTitle(String strTitle){
    	return loadingProgress;
    }
    
    /**
     * 设置背景是否显示
     * @param isshow
     * @return
     */
    public LoadingProgress setBackground(Context context,boolean isshow){
    	LinearLayout liLoading = (LinearLayout)loadingProgress.findViewById(R.id.liLoading);
    	if(liLoading != null){
    		if(isshow){
    			liLoading.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.radius_5_white));
    		}
    	}
    	return loadingProgress;
    }
    
    /**
     * 设置提示内容
     * @param message
     * @return
     */
    public LoadingProgress setMessage(String message){
    	TextView txtLoadingHint = (TextView)loadingProgress.findViewById(R.id.txtLoadingHint);
    	if(txtLoadingHint != null){
    		txtLoadingHint.setVisibility(View.VISIBLE);
    		txtLoadingHint.setText(message);
    	}
    	return loadingProgress;
    }
    
    public static void startLoadingDialog(Context context){
		if(loadingProgress == null){
			loadingProgress = LoadingProgress.createDialog(context);
		}
		loadingProgress.show();
	}
	
    public static void stopLoadingDialog(){
		if(loadingProgress != null){
			loadingProgress.dismiss();
			loadingProgress = null;
		}
	}
    
}
