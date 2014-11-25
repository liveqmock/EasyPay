package com.inter.trade.ui.fragment.coupon;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.ui.fragment.coupon.task.CheckPayStatusTask;
import com.inter.trade.ui.fragment.waterelectricgas.data.ResponseStateListener;
import com.inter.trade.util.Constants;
import com.inter.trade.util.PromptUtil;

/**
 * 汇通宝支付页面 
 * @author  chenguangchi
 * @data:  2014年8月26日 下午1:21:20
 * @version:  V1.0
 */
@SuppressLint("SetJavaScriptEnabled")
public class HTBFragment extends BaseFragment implements OnClickListener 
	,ResponseStateListener{
	
	private Button btnFinish;
	private WebView wvWeb;
	private TextView tvMoney;
	private String orderid;
	
	public static HTBFragment instance(Bundle temp) {
		HTBFragment fragment = new HTBFragment();
		fragment.setArguments(temp);
		return fragment;
	}

	public HTBFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_htb, container, false);
		setBackVisible();
		setTitle("确认支付");
		initView(view);
		initData();
		return view;
	}


	private void initView(View view) {
		btnFinish=(Button) view.findViewById(R.id.btn_finish);
		wvWeb=(WebView) view.findViewById(R.id.wv);
		tvMoney=(TextView) view.findViewById(R.id.tv_money);
		
		btnFinish.setOnClickListener(this);
	}
	
	private void initData() {
		Bundle b =getArguments();
		String money = b.getString("money");
		String url = b.getString("url");
		orderid = b.getString("orderid");
		
		String string = getActivity().getResources().getString(R.string.coupon_money_tip);
		String format = String.format(string, money);
		tvMoney.setText(format);
		
		wvWeb.getSettings().setJavaScriptEnabled(true);
		wvWeb.setWebViewClient(new WebViewClient(){

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				
				view.loadUrl(url);
				
				return true;
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
		
		String deurl=null;
		try {
			deurl = URLDecoder.decode(url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		wvWeb.loadUrl(deurl);
		PromptUtil.showDialog(getActivity(), "请稍后...");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			new CheckPayStatusTask(getActivity(), this).execute(orderid);
			break;

		default:
			break;
		}
	}

	@Override
	public void onSuccess(Object obj, Class cla) {
		String message=(String) obj;
		PromptUtil.showToast(getActivity(), message);
		getActivity().setResult(Constants.ACTIVITY_FINISH);
		getActivity().finish();
	}

}
