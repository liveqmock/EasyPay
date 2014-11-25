package com.inter.trade.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import com.inter.trade.R;


import com.inter.trade.log.Logger;
import com.inter.trade.util.PromptUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

/**
 * WebViewActivity
 * @author Administrator
 *
 */
public class WebViewActivity extends Activity {

	/** log tag. */
	private static final String TAG = WebViewActivity.class.getSimpleName();

	private WebView mWebView;
	
	/**
	 * 飞机票
	 */
	private String defaultURL = "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=615&sid=451200&allianceid=20230&ouid=";
	/**
	 * 酒店
	 */
//	private String defaultURL = "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=636&sid=451200&allianceid=20230&ouid=";
	/**
	 * 火车票
	 */
//	private String defaultURL = "http://u.ctrip.com/union/CtripRedirect.aspx?TypeID=670&departcity=guangzhou&arrivecity=shanghai&time=&traintype=&sourceid=1&sid=451200&allianceid=20230&ouid=";
	private TextView webview_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.default_webview_layout);
		
		String buylink = getIntent().getStringExtra("buylink");
		Logger.i("当前的链接:"+buylink);
		if(buylink != null && !buylink.equals("")) {
			defaultURL = buylink;
		}

		initViews();
		initWebView(mWebView);
		try {
			mWebView.loadUrl(defaultURL);
			PromptUtil.showDialog(this, "请稍后...");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void initViews() {
		findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (!goBack()) {
					finish();
//				}
			}
		});
		
		webview_title = (TextView)findViewById(R.id.webview_title);
//		gifImageView = (GifImageView)findViewById(R.id.gif);
		mWebView = (WebView)findViewById(R.id.ls_webview);
		
	}
	
	/**
	 * GIF loading
	 */
//	private void onGIFLoading(boolean isload) {
//		if(!isload) {
//			gifImageView.setVisibility(View.GONE);
//			return;
//		}
//		gifImageView.setVisibility(View.VISIBLE);
//		try {
//			gifDrawable=new GifDrawable(getResources(), R.drawable.loading);
//			gifImageView.setImageDrawable(gifDrawable);
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}


	/**
	 * 设置Webview的WebviewClient
	 * @param webview webview
	 */
	private void initWebView(WebView webview) {
		WebSettings settings = webview.getSettings();
		settings.setJavaScriptEnabled(true);
		//Uncaught TypeError: Cannot call method 'getItem' of null at http://webresource.c-ctrip.com/code/liza,
		//這個錯誤的引起是因為剛剛一開始該網頁是在 DOM 載入之後才開始對 DOM 元素進行操作
		//預設是不做 DOM 儲存的動作，也就是說在整個網頁載入完成之後並不把 DOM tree 儲存起來，所以導致後續對於 DOM 元素的操作，因為找不到這棵 tree 而發生錯誤
		settings.setDomStorageEnabled(true);
//		webview.getSettings().setSupportZoom(true);
//		webview.getSettings().setBuiltInZoomControls(true);
		
		webview.setWebChromeClient(new WebChromeClient() {  
            @Override  
            public void onReceivedTitle(WebView view, String title) {  
                super.onReceivedTitle(view, title);  
                webview_title.setText(title);
            }

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if(newProgress==100){
					PromptUtil.dissmiss();
				}
			}  
            
        });

		webview.setWebViewClient(new WebViewClient() {

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//				onGIFLoading(false);
				handler.proceed(); 
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
//				onGIFLoading(false);
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

		});
		
//		onGIFLoading(true);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (goBack()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean goBack() {
		WebView webView = mWebView;
		if (webView != null && webView.canGoBack()) {
			webView.goBack();

			return true;
		}
		
		return false;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if( mWebView != null){
			mWebView.removeAllViews();
			mWebView.destroy();
		}
		
	}
}
