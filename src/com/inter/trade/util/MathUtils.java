/*
 * @Title:  MathUtils.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月16日 下午4:00:13
 * @version:  V1.0
 */
package com.inter.trade.util;

import android.annotation.SuppressLint;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 有关数学的工具类
 * 
 * @author chenguangchi
 * @data: 2014年9月16日 下午4:00:13
 * @version: V1.0
 */
public class MathUtils {

	/**
	 * 
	 * 获取小数一位的四舍五入
	 * 
	 * @param d
	 * @return
	 * @throw
	 * @return String
	 */
	@SuppressLint("NewApi")
	public static String getUPDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setRoundingMode(RoundingMode.HALF_UP);// 设置四舍五入
		nf.setMinimumFractionDigits(1);// 设置最小保留几位小数
		nf.setMaximumFractionDigits(1);// 设置最大保留几位小数
		return nf.format(d);
	}
	
	/**
	 * 
	 * 获取小数一位的四舍五入
	 * 
	 * @param d
	 * @return
	 * @throw
	 * @return String
	 */
	@SuppressLint("NewApi")
	public static String getDownDouble(double d) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setRoundingMode(RoundingMode.FLOOR);// 设置四舍五入
		nf.setMinimumFractionDigits(1);// 设置最小保留几位小数
		nf.setMaximumFractionDigits(1);// 设置最大保留几位小数
		return nf.format(d);
	}
}
