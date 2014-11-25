package com.inter.trade.ui.factory;

import com.inter.trade.data.SunType;

import android.support.v4.app.Fragment;

public abstract class AbstractFactory {
	public abstract  Fragment createFragment(int index,SunType params);
}
