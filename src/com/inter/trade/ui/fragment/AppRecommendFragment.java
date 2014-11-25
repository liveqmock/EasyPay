package com.inter.trade.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;

/**
 * 应用推荐
 * @author apple
 *
 */
public class AppRecommendFragment extends BaseFragment{
	
	private Button btnDownload;
	
	public AppRecommendFragment()
	{
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBackVisible();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_app_recommend, container, false);
		btnDownload=(Button) view.findViewById(R.id.btn_download);
		btnDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
		        intent.setAction("android.intent.action.VIEW");
		        Uri content_url = Uri.parse("https://www.51zhangdan.com/download/zhangdan/51zhangdan.apk");
		        intent.setData(content_url);
		        startActivity(intent);
			}
		});
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setTitle("推荐应用");
	}
}
