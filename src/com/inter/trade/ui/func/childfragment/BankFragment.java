/*
 * @Title:  BankFragment.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月25日 上午10:18:35
 * @version:  V1.0
 */
package com.inter.trade.ui.func.childfragment;

import java.lang.reflect.Field;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.inter.trade.R;
import com.inter.trade.qrsacncode.CaptureActivity;
import com.inter.trade.ui.func.FuncData;

/**
 * 掌上银行和便民服务的公用页面
 * @author  ChenGuangChi
 * @data:  2014年7月25日 上午10:18:35
 * @version:  V1.0
 */
public class BankFragment extends Fragment implements OnClickListener{
	private ViewPager mViewPager;
	
	private Button btnDimenCode;
	
	private static final String DATAS="datas";
	
	private ArrayList<ArrayList<FuncData>> mDatas;
	
	public static BankFragment newInstance(ArrayList<ArrayList<FuncData>> list){
		BankFragment bankFragment = new BankFragment();
		Bundle bundle=new Bundle();
		bundle.putSerializable(DATAS, list);
		bankFragment.setArguments(bundle);
		return bankFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_bank, null);
		initData();
		initView(view);
		
		return view;
	}

	private void initData() {
		if(getArguments()!=null){
			mDatas=(ArrayList<ArrayList<FuncData>>) getArguments().getSerializable(DATAS);
		}
	}

	private void initView(View view) {
		mViewPager=(ViewPager) view.findViewById(R.id.ad_pager);
		btnDimenCode=(Button) view.findViewById(R.id.iv_dimencode);
		btnDimenCode.setOnClickListener(this);
		if(mDatas!=null){
			mViewPager.setAdapter(new ChildFragmentPagerAdapter(getChildFragmentManager(), mDatas));
		}
	}
	
	

	@Override
	public void onDetach() {
		super.onDetach();
		 super.onDetach();

		    try {
		        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
		        childFragmentManager.setAccessible(true);
		        childFragmentManager.set(this, null);

		    } catch (NoSuchFieldException e) {
		        throw new RuntimeException(e);
		    } catch (IllegalAccessException e) {
		        throw new RuntimeException(e);
		    }
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		Intent intent=new Intent(getActivity(),CaptureActivity.class);
		startActivity(intent);
	}
		
}
