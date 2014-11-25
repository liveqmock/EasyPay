/*
 * @Title:  PointNumComparator.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月7日 下午3:01:56
 * @version:  V1.0
 */
package com.inter.trade.ui.func.util;

import java.util.Comparator;

import com.inter.trade.ui.func.data.IconData;

/**
 * 功能模块按照点击次数的比较器
 * @author  ChenGuangChi
 * @data:  2014年7月7日 下午3:01:56
 * @version:  V1.0
 */
public class PointNumComparator implements Comparator<IconData> {

	@Override
	public int compare(IconData lhs, IconData rhs) {
		String pointnum = lhs.getPointnum();
		String pointnum2 = rhs.getPointnum();
		int result=0;
		try {
			int num1 = Integer.parseInt(pointnum);
			int num2=Integer.parseInt(pointnum2);
			if(num1>num2){
				result=-1;
			}else if(num1<num2){
				result=1;
			}
		} catch (Exception e) {
		}
		return result;
	}

}
