/*
 * @Title:  ChildFragmentPagerAdapter.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月28日 上午9:17:14
 * @version:  V1.0
 */
package com.inter.trade.ui.func.childfragment;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.inter.trade.ui.func.FuncData;

/**
 * 掌上银行和便民服务的子页适配器
 * @author  ChenGuangChi
 * @data:  2014年7月28日 上午9:17:14
 * @version:  V1.0
 */
public class ChildFragmentPagerAdapter extends FragmentPagerAdapter {
	
	private ArrayList<ArrayList<FuncData>> mList;

	public ChildFragmentPagerAdapter(FragmentManager fm,ArrayList<ArrayList<FuncData>> mList) {
		super(fm);
		this.mList=mList;
	}

	@Override
	public Fragment getItem(int arg0) {
		return ChildIndexItem.create(mList.get(arg0));
	}

	@Override
	public int getCount() {
		
		return mList.size();
	}

}
