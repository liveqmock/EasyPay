/*
 * @Title:  SalaryGetInfoUtils.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月16日 上午9:21:12
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.salaryget.util;

/**
 * 工资签收的工具类
 * @author  chenguangchi
 * @data:  2014年9月16日 上午9:21:12
 * @version:  V1.0
 */
public class SalaryGetInfoUtils {
	
	/**
	 * 获取签收状态 
	 * @param strState
	 * @return    签收状态
	 * 1：已签收 0：未签收，可以签收  -1：还未发完工资，还不可以签收
	 * @throw
	 * @return String
	 */
	public static String  getState(String strState){
		
		if("0".equals(strState)){
			return "未签收";
		}else if("-1".equals(strState)){
			return "暂不可签收";
		}else if("1".equals(strState)){
			return "已签收";
		}
		return "未签收";
	}
}
