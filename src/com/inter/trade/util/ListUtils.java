/*
 * @Title:  ListUtils.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年10月13日 下午2:19:33
 * @version:  V1.0
 */
package com.inter.trade.util;

import java.util.ArrayList;
import java.util.List;

/**
 * list的工具类
 * @author  chenguangchi
 * @data:  2014年10月13日 下午2:19:33
 * @version:  V1.0
 */
public class ListUtils {
	
	/**
	 *判断list是否为空 
	 * @param list
	 * @return
	 * @throw
	 * @return boolean
	 */
	public static boolean isEmptyList(ArrayList list){
		if(list!=null && list.size()>0){
			return false;
		}
		return true;
	}
	
	/**
     * is null or its size is 0
     * 
     * <pre>
     * isEmpty(null)   =   true;
     * isEmpty({})     =   true;
     * isEmpty({1})    =   false;
     * </pre>
     * 
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
     */
    public static <V> boolean isEmpty(List<V> sourceList) {
        return (sourceList == null || sourceList.size() == 0);
    }
}
