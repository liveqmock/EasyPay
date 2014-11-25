package com.inter.trade.ui.fragment.cridet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.BaseFragment;
import com.inter.trade.util.LoginUtil;

/**
 * 详情
 * @author apple
 *
 */

public class CridetRecordDetialFragment extends BaseFragment{
	private LayoutInflater mInflater;
	public  static ArrayList<LinkedHashMap<String, String>> mMaps = new ArrayList<LinkedHashMap<String,String>>();
	
	public static CridetRecordDetialFragment createFragment(ArrayList<LinkedHashMap<String, String>> cData){
		CridetRecordDetialFragment fragment = new CridetRecordDetialFragment();
		mMaps = cData;
		return fragment;
	}
	public CridetRecordDetialFragment(){
		
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setTitle("交易详情");
		setBackVisible();
		mInflater= LayoutInflater.from(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		LoginUtil.detection(getActivity());
		View view = inflater.inflate(R.layout.detial_layout, container,false);
		initResult(view);
		return view;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setTitle("交易详情");
	}
	private void initResult(View view){
		LinearLayout containerLayout = (LinearLayout)view.findViewById(R.id.container);
		containerLayout.removeAllViews();
		for(int i =0;i<mMaps.size();i++){
			HashMap<String, String> hashMap = mMaps.get(i);
			Set<String> keSet = hashMap.keySet();
			for(String s : keSet){
				Item item = new Item(s,hashMap.get(s));
				containerLayout.addView(item.mView);
			}
			
		}
	}
	class Item {
		private View mView;
		public Item(String key ,String value){
			mView = mInflater.inflate(R.layout.result_item, null);
			TextView name = (TextView)mView.findViewById(R.id.name);
			TextView content = (TextView)mView.findViewById(R.id.content);
				name.setText(key);
				content.setText(value);
		}
	}
}
