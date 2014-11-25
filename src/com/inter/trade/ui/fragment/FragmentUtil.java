package com.inter.trade.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.inter.trade.ui.FunctionActivity;

public class FragmentUtil {
	public static void addFragment(int id,int index,FragmentManager manager){
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(id, FragmentFactory.create().createFragment(index,null));
		transaction.commit();
	}
	
	public static void replaceFragment(int id,int index,FragmentManager manager){
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(id, FragmentFactory.create().createFragment(index,null));
		transaction.commit();
	}
	
	public static void startFuncActivity(Activity activity,int index){
		Intent intent = new Intent();
		intent.setClass(activity, FunctionActivity.class);
		intent.putExtra(FragmentFactory.INDEX_KEY, index);
		activity.startActivityForResult(intent, 200);
	}
}
