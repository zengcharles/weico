package com.charles.weibo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class DownUpScrollView extends ScrollView{

	private boolean canScroll;
	
	private GestureDetector mGestureDetector;
    View.OnTouchListener mGestureListener;

	public DownUpScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mGestureDetector = new GestureDetector(new YScrollDetector());
        canScroll = true;

	}
	
	@Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
		if(ev.getAction() == MotionEvent.ACTION_MOVE){
			canScroll = true;
		}else{
			canScroll = false;
		}
		 boolean value =true;
		if(!canScroll){
			value =super.onInterceptTouchEvent(ev);
		}else{
			value =super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
		}
		return value;
    }


	class YScrollDetector extends SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(canScroll)
                if (Math.abs(distanceY) >= Math.abs(distanceX))
                    canScroll = true;
                else
                    canScroll = false;
            return canScroll;
        }
        
        @Override
		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			canScroll = true;
			return super.onDown(e);
		}
    }

}
