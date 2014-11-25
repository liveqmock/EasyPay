/*
 * @Title:  PassengerInfoUtils.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月16日 上午9:50:10
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.airticket.util;

import com.unionpay.uppay.task.s;

/**
 * 乘机人信息工具类
 * 
 * @author ChenGuangChi
 * @data: 2014年7月16日 上午9:50:10
 * @version: V1.0
 */
public class PassengerInfoUtils {

	/**
	 * 
	 * 将证件类型代号——>文字 证件类型: 1 身份证，2 护照，4 军人证，7 回乡证，8
	 * 台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
	 * 
	 * @param idtype
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transferTypeToString(String idtype) {
		if ("1".equals(idtype)) {
			return "身份证";
		} else if ("2".equals(idtype)) {
			return "护照";
		} else if ("4".equals(idtype)) {
			return "军人证";
		} else if ("7".equals(idtype)) {
			return "回乡证";
		} else if ("8".equals(idtype)) {
			return "台胞证";
		} else if ("10".equals(idtype)) {
			return "港澳通行证";
		} else if ("11".equals(idtype)) {
			return "国际海员证";
		} else if ("20".equals(idtype)) {
			return "外国人永久居留证";
		} else if ("25".equals(idtype)) {
			return "户口簿";
		} else if ("27".equals(idtype)) {
			return "出生证明";
		} else if ("99".equals(idtype)) {
			return "其它";
		}
		return "其它";
	}

	/**
	 * 
	 * 将文字——>证件类型代号 证件类型: 1 身份证，2 护照，4 军人证，7 回乡证，8
	 * 台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
	 * 
	 * @param idtype
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transferStringToType(String str) {
		if ("身份证".equals(str)) {
			return "1";
		} else if ("护照".equals(str)) {
			return "2";
		} else if ("出生证明".equals(str)) {
			return "27";
		} else if ("军人证".equals(str)) {
			return "4";
		} else if ("回乡证".equals(str)) {
			return "7";
		} else if ("台胞证".equals(str)) {
			return "8";
		} else if ("港澳通行证".equals(str)) {
			return "10";
		} else if ("国际海员证".equals(str)) {
			return "11";
		} else if ("外国人永久居留证".equals(str)) {
			return "20";
		} else if ("户口簿".equals(str)) {
			return "25";
		}
		return "1";
	}

	/**
	 * 将id——>代码(ADU) 年龄段 ADU（成人），CHI（儿童）， BAB（婴儿）
	 * 
	 * @param select
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transfetIntToAgeRange(int select) {
		String ageRange="ADU";
		switch (select) {
		case 0:
			ageRange="ADU";
			break;
		case 1:
			ageRange="CHI";
			break;
		case 2:
			ageRange="BAB";
			break;

		default:
			break;
		}
		return ageRange;
	}
}
