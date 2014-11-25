/*
 * @Title:  PointNumComparator.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月7日 下午3:01:56
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.util;

import java.util.ArrayList;
import java.util.Comparator;

import com.inter.trade.ui.fragment.salaryget.bean.SalaryGet;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.data.IconData;
import com.inter.trade.util.ListUtils;

/**
 * 年月的比较器
 * @author  ChenGuangChi
 * @data:  2014年7月7日 下午3:01:56
 * @version:  V1.0
 */
public class MonthComparator implements Comparator<ArrayList<SalaryGet>> {

	@Override
	public int compare(ArrayList<SalaryGet> oList, ArrayList<SalaryGet> nList) {
		int result=0;
		if(!ListUtils.isEmptyList(oList)&& !ListUtils.isEmptyList(nList)){
			SalaryGet salary1 = oList.get(0);
			SalaryGet salary2 = nList.get(0);
			String oDate = salary1.wagemonth;
			String nDate = salary2.wagemonth;
			int oYear=Integer.parseInt(oDate.substring(0,4));
			int oMonth=Integer.parseInt(oDate.substring(oDate.length()-3, oDate.length()));
			
			int nYear=Integer.parseInt(nDate.substring(0,4));
			int nMonth=Integer.parseInt(nDate.substring(oDate.length()-3, oDate.length()));
			
			System.out.println("年"+oYear+"月"+oMonth+"年"+nYear+"月"+nMonth);
			
			if(oYear>nYear){
				result=-1;
			}else if(oYear<nYear){
				result=1;
			}else{
				if(oMonth>nMonth){
					result=1;
				}else{
					result=-1;
				}
			}
			
		}
		return result;
	}

}
