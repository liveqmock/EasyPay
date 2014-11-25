package com.inter.trade.ui.factory;

import android.support.v4.app.Fragment;

import com.inter.trade.data.ParamsValue;
import com.inter.trade.data.SunType;
import com.inter.trade.ui.fragment.cridet.CridetConfirmFragment;

public class ConfrimFactory extends AbstractFactory{
	public static final int CridetConfirmFragment_index = 1;
	public static final int CheckFragment_index = 2;

	
	private  static ConfrimFactory mFactory;
	public static ConfrimFactory create(){
		if(mFactory==null){
			mFactory= new ConfrimFactory();
		}
		return mFactory;
	}
	private ConfrimFactory(){
		
	}
	@Override
	public Fragment createFragment(int index, SunType params) {
		// TODO Auto-generated method stub
		Fragment fragment  = null;
		ParamsValue paramsValue = (ParamsValue)params;
		switch (index) {
		case CridetConfirmFragment_index:
			String costString=paramsValue.params.get("costString");
			String allString=paramsValue.params.get("allString");
			String no=paramsValue.params.get("no");
			
			fragment = CridetConfirmFragment.create(
					costString, allString, no);
			break;
		case CheckFragment_index:
			break;

		default:
			break;
		}
		return fragment;
	}
	
}
