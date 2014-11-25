/*
 * @Title:  IdcardUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月17日 下午2:53:09
 * @version:  V1.0
 */
package com.inter.trade.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 有关身份证的工具类
 * @author  ChenGuangChi
 * @data:  2014年7月17日 下午2:53:09
 * @version:  V1.0
 */
public class IdcardUtil {
		
	/**
	 * 验证身份证是否合法 
	 * @param card
	 * @return
	 * @throw
	 * @return boolean
	 */
	public static boolean validateID(String card){
		if(card!=null){
			IdcardValidator iv = new IdcardValidator();
			return iv.isValidatedAllIdcard(card);
		}
		return false;
	}
	
	/**
	 * 获取身份证的性别信息 
	 * @return
	 * @throw
	 * @return String
	 */
	public static String getGender(String idcard){
		IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);
		return ie.getGender();
	}
	
	/**
	 * 获取身份证的生日信息    例如：1990-03-06
	 * @param idcard
	 * @return
	 * @throw
	 * @return Date
	 */
	public static String getBirthday(String idcard){
		IdcardInfoExtractor ie = new IdcardInfoExtractor(idcard);
		Date birthday =ie.getBirthday();
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		String result = format.format(birthday);
		return result;
		
	}
}
