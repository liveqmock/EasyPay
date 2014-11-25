/*
 * @Title:  FormatUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月14日 下午2:29:48
 * @version:  V1.0
 */
package com.inter.trade.ui.func.util;

/**
 * 格式化工具
 * @author  ChenGuangChi
 * @data:  2014年7月14日 下午2:29:48
 * @version:  V1.0
 */
public class FormatUtil {
	
	/**
	 * String->int  
	 * @param target
	 * @return
	 * @throw
	 * @return int
	 */
	public static int parserInt(String target){
		int result=0;
		try {
			result=Integer.parseInt(target);
		} catch (Exception e) {
			return 0;
		}
		return result;
	}
}
