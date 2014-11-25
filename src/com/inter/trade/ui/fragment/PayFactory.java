package com.inter.trade.ui.fragment;

import android.support.v4.app.Fragment;

import com.inter.trade.data.SunType;
import com.inter.trade.ui.factory.AbstractFactory;
import com.inter.trade.ui.fragment.order.PaySelectedFragment;
import com.inter.trade.ui.fragment.order.query.OrderQureyFragment;
import com.inter.trade.util.LoginUtil;

public class PayFactory extends AbstractFactory{
	public static final String INDEX_KEY = "INDEX_KEY";
	
	public static final int LOGIN_FRAGMENT_INDEX=-1;
	public static final int START__INDEX=0;
	public static final int PAY_METHOD__INDEX=1;
	public static final int PAY_QUERY_METHOD__INDEX=2;
	
	public static int current_index=0;//当前界面索引
	/**
	 * 根据索引创建fragment
	 * @param index
	 * @return
	 */
	public  Fragment createFragment(int index,SunType params){
		Fragment fragment = null;
		
		if(!LoginUtil.isLogin){
			fragment = new LoginFragment();
			current_index = index;
			return fragment;
		}
			switch (index) {
			case -1:
				fragment = new LoginFragment();
				break;
			case PAY_METHOD__INDEX:
				fragment = new PaySelectedFragment();
				break;
			case PAY_QUERY_METHOD__INDEX:
				fragment = new OrderQureyFragment();
				break;
			default:
				break;
			}
		
		if(fragment==null){
			fragment = new LoginFragment();
		}
		
		return fragment;
	}
	
	
}
