package com.inter.trade.ui.fragment.tfbcard;

import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.PromptUtil;

/**
 * 汇通宝充值页面
 * @author  chenguangchi
 * @data:  2014年8月26日 下午1:21:20
 * @version:  V1.0
 */
@SuppressLint("SetJavaScriptEnabled")
public class HTBRechargeFragment extends BaseFragment{
	
	private WebView wvWeb;
	
	public static HTBRechargeFragment instance(Bundle temp) {
		HTBRechargeFragment fragment = new HTBRechargeFragment();
		fragment.setArguments(temp);
		return fragment;
	}

	public HTBRechargeFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_htb_recharge, container, false);
		setBackVisible();
		setTitle("优惠卡充值");
		initView(view);
		initData();
		return view;
	}


	private void initView(View view) {
		wvWeb=(WebView) view.findViewById(R.id.wv);
	}
	
	@SuppressLint("NewApi")
	private void initData() {
		if (VERSION.SDK_INT > VERSION_CODES.GINGERBREAD_MR1) {
			wvWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		wvWeb.getSettings().setJavaScriptEnabled(true);
		wvWeb.getSettings().setBuiltInZoomControls(true);
		//wvWeb.getSettings().setDisplayZoomControls(false);
		wvWeb.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
				
				return true;
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
			handler.proceed();
			}
			
			
			
		});
		wvWeb.setWebChromeClient(new WebChromeClient(){

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
					if(newProgress==100){
						PromptUtil.dissmiss();
					}
			}
			
			
			
		});
		
		wvWeb.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {  
                    if (keyCode == KeyEvent.KEYCODE_BACK && wvWeb.canGoBack()) {  
                    	wvWeb.goBack();   //后退  
                        return true;    //已处理  
                    }  
                }  
                return false; 
			}
		});
//		wvWeb.loadUrl("https://pay.demo.expresspay.cn/Paygateway/recharge/mobCardRecharge.do");
		wvWeb.loadUrl("https://pay.expresspay.cn/Paygateway/recharge/mobCardRecharge.do");
		
		PromptUtil.showDialog(getActivity(), "请稍后...");
	}
}
