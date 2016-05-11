package com.charles.weibo.ui.customwebview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.charles.weibo.R;

/**
 * Created by kongqw on 2016/3/7.
 * 带有进度条的WebView
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;
    private WebChromeClientListener mWebChromeClientListener;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 创建一个进度条
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        // 设置进度条参数（位置）
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 10, 0, 0));
        // 添加一个自定义的样式，系统默认的样式上下有边距
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);

        // 添加进度条
        addView(progressbar);

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                // 获取到Title
                if (null != mWebChromeClientListener) {
                    mWebChromeClientListener.onReceivedTitle(title);
                }
            }
        };
        // 给WebView添加监听
        setWebChromeClient(webChromeClient);

        //是否可以缩放
        getSettings().setSupportZoom(true);
        getSettings().setBuiltInZoomControls(true);
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // newProgress 加载进度
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE) {
                    progressbar.setVisibility(VISIBLE);
                }
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setWebChromeClientListener(WebChromeClientListener listener) {
        mWebChromeClientListener = listener;
    }
}