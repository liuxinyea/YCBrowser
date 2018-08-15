package com.iotek.view;

import com.iotek.ycbrowser.R;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;

public class MyWebView extends LinearLayout{
    private WebView mWebView;
    private SwipeRefreshLayout mRefreshLayout;
	public MyWebView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.mywebview,this);
		mWebView=(WebView)findViewById(R.id.webview);
		mRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_container);
		mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {  
            
            @Override  
            public void onRefresh() {  
                //重新刷新页面  
            	mWebView.loadUrl(mWebView.getUrl());  
            }  
        });  
		/*mRefreshLayout.setColorScheme(Color.BLUE,  
				Color.GREEN,Color.RED,  
                Color.YELLOW);  */
          
	}
    public WebView getWebView(){
    	return this.mWebView;
    }
    public SwipeRefreshLayout getSwipeRefreshLayout(){
    	return mRefreshLayout;
    }
}
