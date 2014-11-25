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

import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.data.IconData;

/**
 * 功能模块按照排序号的比较器
 * @author  ChenGuangChi
 * @data:  2014年7月7日 下午3:01:56
 * @version:  V1.0
 */
public class OrderComparator implements Comparator<FuncData> {

	@Override
	public int compare(FuncData lhs, FuncData rhs) {
		int result=0;
		int one=lhs.orderId;
		int two=rhs.orderId;
		if(one>two){
			result=1;
		}else if(one<two){
			result=-1;
		}
		return result;
	}

}
