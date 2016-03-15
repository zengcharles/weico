package com.charles.weibo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.GridView;

public class ReheightGridView extends GridView{

	public ReheightGridView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
    }  


    public ReheightGridView(Context context) {  
        super(context);  
    }  


    public ReheightGridView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
    }  


    @Override  
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  


        int expandSpec = MeasureSpec.makeMeasureSpec(  
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);  
        super.onMeasure(widthMeasureSpec, expandSpec);  
    }  
    
    @Override
    public void onDraw(Canvas canvas){
    	super.onDraw(canvas);
    }
}
