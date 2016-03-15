package com.charles.weibo.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.charles.weibo.R;


public class CommonAlertDialog extends Dialog{
	
	public CommonAlertDialog(Context context,int theme) {
		super(context,theme);
	}
	
	public CommonAlertDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public static class Builder{
		private Context context;
		private String title;
		private String message;
		private String leftButtonText;
		private String middleButtonText;
		private String rightButtonText;
		private boolean cancleAble;
		private View contentView;
		
		private DialogInterface.OnClickListener leftButtonClickListener,
											middleButtonClickListener,
											rightButtonClickListener;
		
		public Builder(Context context){
			this.context = context;
		}
		
		/**
		 * 根据字符串创建提示
		 * @param message
		 * @return
		 */
		public Builder setMessage(String message){
			this.message = message;
			return this;
		}
		
		/**
		 * 根据resourceId创建提示
		 * @param message
		 * @return
		 */
		public Builder setMessage(int message){
			this.message = (String)context.getText(message);
			return this;
		}
		
		/**
		 * 根据字符串创建标题
		 * @param title
		 * @return
		 */
		public Builder setTitle(String title){
			this.title = title;
			return this;
		}
		
		public Builder setCancleAble(boolean cancleAble){
			this.cancleAble = cancleAble;
			return this;
		}
		
		/**
		 * 根据resourceId创建标题
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title){
			this.title = (String)context.getText(title);
			return this;
		}
		
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		
		
		/**
		 * 创建左边按钮
		 * @param leftButtonText
		 * @param listener
		 * @return
		 */
		public Builder setLeftButton(int leftButtonText,
				DialogInterface.OnClickListener listener){
			this.leftButtonText = (String)context.getText(leftButtonText);
			this.leftButtonClickListener = listener;
			return this;
		}
		
		public Builder setLeftButton(String leftButtonText,
				DialogInterface.OnClickListener listener){
			this.leftButtonText = leftButtonText;
			this.leftButtonClickListener = listener;
			return this;
		}
		
		/**
		 * 创建中间按钮
		 * @param middleButtonText
		 * @param listener
		 * @return
		 */
		public Builder setMiddleButton(int middleButtonText,
				DialogInterface.OnClickListener listener){
			this.middleButtonText = (String)context.getText(middleButtonText);
			this.middleButtonClickListener = listener;
			return this;
		}
		
		public Builder setMiddleButton(String middleButtonText,
				DialogInterface.OnClickListener listener){
			this.middleButtonText = middleButtonText;
			this.middleButtonClickListener = listener;
			return this;
		}
		
		/**
		 * 创建右边按钮
		 * @param rightButtonText
		 * @param listener
		 * @return
		 */
		public Builder setRightButton(int rightButtonText,
				DialogInterface.OnClickListener listener){
			this.rightButtonText = (String)context.getText(rightButtonText);
			this.rightButtonClickListener = listener;
			return this;
		}
		
		public Builder setRightButton(String rightButtonText,
				DialogInterface.OnClickListener listener){
			this.rightButtonText = rightButtonText;
			this.rightButtonClickListener = listener;
			return this;
		}
		
		
		public CommonAlertDialog create(){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final CommonAlertDialog dialog = new CommonAlertDialog(context,R.style.LianAlertDialog);
			
			View layout = inflater.inflate(R.layout.showalertdialog_layout, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
			//设置左边按钮
			if(leftButtonText != null){
				((Button)layout.findViewById(R.id.btnLeft)).setText(leftButtonText);
				if(leftButtonClickListener != null){
					((Button)layout.findViewById(R.id.btnLeft)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							leftButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
						}
					});
				}
			}else{
				layout.findViewById(R.id.btnLeft).setVisibility(View.GONE);
			}
			//设置中间按钮
			if(middleButtonText != null){
				((Button)layout.findViewById(R.id.btnMiddle)).setText(middleButtonText);
				if(middleButtonClickListener != null){
					((Button)layout.findViewById(R.id.btnMiddle)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							middleButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
						}
					});
				}
			}else{
				layout.findViewById(R.id.btnMiddle).setVisibility(View.GONE);
			}
			//设置标题
			if(title != null){
				((TextView)layout.findViewById(R.id.txtTitle)).setText(title);
			}else{
				((TextView)layout.findViewById(R.id.txtTitle)).setText("提示");
			}
			//设置右边按钮
			if(rightButtonText != null){
				((Button)layout.findViewById(R.id.btnRight)).setText(rightButtonText);
				if(rightButtonClickListener != null){
					((Button)layout.findViewById(R.id.btnRight)).setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							rightButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
						}
					});
				}
			}else{
				layout.findViewById(R.id.btnRight).setVisibility(View.GONE);
			}
			//设置消息
			if(message != null){
				((TextView)layout.findViewById(R.id.txtContent)).setText(message);
			}else if(contentView != null){
				((LinearLayout)layout.findViewById(R.id.content)).removeAllViews();
				((LinearLayout)layout.findViewById(R.id.content)).addView(contentView,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				
			}
			dialog.setCanceledOnTouchOutside(cancleAble);
			dialog.setCancelable(cancleAble);
			dialog.setContentView(layout);
			return dialog;
			
		}
		
		
	}

	
	
	

}
