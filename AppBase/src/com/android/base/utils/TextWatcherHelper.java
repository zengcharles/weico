package com.android.base.utils;

import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherHelper implements TextWatcher {
	private EditText editText;
	private Drawable mIconClear; // 文本框清除文本内容图标
	
	public TextWatcherHelper(EditText editText,Drawable mIconClear) {
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
