package com.inter.trade.ui.fragment.buylicensekey.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.inter.trade.R;
import com.inter.trade.ui.fragment.hotel.data.HotelListData;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
/**
 * 购买授权码 辅助类
 * 
 * @author haifengli
 *
 */
public class BuyLicenseKeyUtils {
	
	 /**
     * 切换Fragment
     * @param targetFragment 目标Fragment
     * @param isAddToBackStack 是否要添加到堆栈；=1 ：添加到堆栈；否则不添加到堆栈
     */
    public static void switchFragment(FragmentTransaction ft, Fragment targetFragment, int isAddToBackStack){
    	if(ft == null || targetFragment == null){
    		return;
    	}
//    	FragmentTransaction ft= getFragmentManager().beginTransaction();
    	ft.replace(R.id.func_container, targetFragment);
    	ft.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE)/**.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)*/;
    	if(isAddToBackStack == 1) {
    		ft.addToBackStack(null);
    	}
    	ft.commit();
    }
    
    /**
	 * 更改绑定状态后，返回上一个页面：我的授权码 详情 BuyLicenseKeyDetailFragment
	 */
//    public static void goBack(Fragment context){
//    	if(context == null ){
//    		return;
//    	}
//		FragmentManager manager = context.getFragmentManager();
//		 int len = manager.getBackStackEntryCount();
//		 if(len>0){
//			 manager.popBackStack();
//		 }
//	}
}