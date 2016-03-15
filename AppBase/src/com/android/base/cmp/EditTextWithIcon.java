package com.android.base.cmp;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author Administrator 示例
 * 对textview增加textchangelistener就可以增加图标
 *      ======================初始化 mIconClear =====================
 *      private Drawable mIconClear; // 文本框清除文本内容图标
 *      mIconClear = getResources().getDrawable(R.drawable.txt_clear);
 *      ======================添加 图标并注册事件 ========================
 *		barcodeNum.addTextChangedListener(new myTextWatcher(barcodeNum, mIconClear));
 *		barcodeNum.setOnTouchListener(new OnTouchListener() {
 *	        @Override
 *	        public boolean onTouch(View v, MotionEvent event) {
 *	            switch (event.getAction()) {
 *	            case MotionEvent.ACTION_UP:
 *	                int curX = (int) event.getX();
 *	                if (curX > v.getWidth() - 38
 *	                        && !TextUtils.isEmpty(barcodeNum.getText())) {
 *	                	barcodeNum.setText("");
 *	                    int cacheInputType = barcodeNum.getInputType();// backup  the input type
 *	                    barcodeNum.setInputType(InputType.TYPE_NULL);// disable soft input
 *	                    barcodeNum.onTouchEvent(event);// call native handler
 *	                    barcodeNum.setInputType(cacheInputType);// restore input  type
 *	                    return true;// consume touch even
 *	                }
 *	                break;
 *	            }
 *	            return false;
 *	        }
 *	    });
 *
 */

public class EditTextWithIcon implements TextWatcher {
	private EditText editText;
	private Drawable mIconClear; // 文本框清除文本内容图标
	
	public EditTextWithIcon(EditText editText,Drawable mIconClear) {
		super();
		this.editText = editText;
		this.mIconClear =  mIconClear;
	}
	
	 //缓存上一次文本框内是否为空
    private boolean isnull = true;

    @Override
    public void afterTextChanged(Editable s) {
        
        if (TextUtils.isEmpty(s)) {
            if (!isnull) {
            	editText.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null);
                isnull = true;
            }
        } else {
            if (isnull) {
            	editText.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, mIconClear, null);
                isnull = false;
            }
        }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
    }

    /**
     * 随着文本框内容改变动态改变列表内容
     */
    @Override
    public void onTextChanged(CharSequence s, int start, int before,
            int count) {
        
    }
    
}
