/*
 * @Title:  CreditcardInfoUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月22日 下午1:02:03
 * @version:  V1.0
 */
package com.inter.trade.ui.creditcard.util;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.data.BankData;

/**
 * 信用卡支付的相关信息的工具类
 * @author  ChenGuangChi
 * @data:  2014年7月22日 下午1:02:03
 * @version:  V1.0
 */
public class CreditcardInfoUtil {
	
	/**
	 * 身份证：IDCARD ;	护照：PASSPORT ;	军官证：OFFICERPASS;	澳居民往来内地通行证：HM_VISITORPASS ;台湾居民来往大陆通行证：T_VISITORPASS;			其它：OTHER
	 * @param str
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transferStringToId(String str){
		String result="IDCARD";
		if("身份证".equals(str)){
			result="IDCARD";
		}else if("护照".equals(str)){
			result="PASSPORT";
		}else if("军官证".equals(str)){
			result="OFFICERPASS";
		}else if("港澳居民往来内地通行证".equals(str)){
			result="HM_VISITORPASS";
		}else if("台湾居民来往大陆通行证".equals(str)){
			result="T_VISITORPASS";
		}else if("其它".equals(str)){
			result="OTHER";
		}
		return result;
	}
	
	
	/**
	 *
	 * 邮政储蓄银行  PSBCCREDIT
	 * 深圳发展银行  SDBCREDIT
	 * 民生银行     CMBCCREDIT
	 * 包商银行  BSBCREDIT
	 * 北京银行 BCCBCREDIT
	 * 上海银行 BOSHCREDIT
	 * 招商银行   CMBCHINACREDIT
	 * 中信银行  ECITICCREDIT
	 * 浦东发展银行  SPDBCREDIT
	 * 兴业银行  CIBCREDIT
	 * 华夏银行  HXBCREDIT 
	 * 农业银行  ABCCREDIT
	 * 广东发展银行 GDBCREDIT
	 * 工商银行  ICBCCREDIT
	 * 中国银行  BOCCREDIT
	 * 交通银行 BOCOCREDIT
	 * 建设银行  CCBCREDIT
	 * 平安银行 PINGANCREDIT
	 * 光大银行  EVERBRIGHTCREDIT
	 * 
	 * @param str   根据银行查询银行代号
	 * @return  
	 * @throw
	 * @return String
	 */
	public static String transferStringToCode(String str){
		if("邮政储蓄银行".equals(str)){
			return "PSBCCREDIT";
		}else if("深圳发展银行".equals(str)){
			return "SDBCREDIT";
		}else if("民生银行".equals(str)){
			return "CMBCCREDIT";
		}else if("包商银行".equals(str)){
			return "BSBCREDIT";
		}else if("北京银行".equals(str)){
			return "BCCBCREDIT";
		}else if("上海银行".equals(str)){
			return "BOSHCREDIT";
		}else if("招商银行".equals(str)){
			return "CMBCHINACREDIT";
		}else if("中信银行".equals(str)){
			return "ECITICCREDIT";
		}else if("浦东发展银行".equals(str)){
			return "SPDBCREDIT";
		}else if("兴业银行".equals(str)){
			return "CIBCREDIT";
		}else if("华夏银行".equals(str)){
			return "HXBCREDIT";
		}else if("农业银行".equals(str)){
			return "ABCCREDIT";
		}else if("广东发展银行".equals(str)){
			return "GDBCREDIT";
		}else if("工商银行".equals(str)){
			return "ICBCCREDIT";
		}else if("中国银行".equals(str)){
			return "BOCCREDIT";
		}else if("交通银行".equals(str)){
			return "BOCOCREDIT";
		}else if("建设银行".equals(str)){
			return "CCBCREDIT";
		}else if("平安银行".equals(str)){
			return "PINGANCREDIT";
		}else if("光大银行".equals(str)){
			return "EVERBRIGHTCREDIT";
		}
		
		return "";
	}
	
	/**
	 *  获取相应的年份
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transferStringToYear(String str){
		
		return "20"+str;
	}
	
	/**
	 * 转化相应的月份 
	 * @param str
	 * @return
	 * @throw
	 * @return String
	 */
	public static String transferStringToMonth(String str){
		System.out.println("月份"+str);
		if("10".equals(str) || "11".equals(str) || "12".equals(str)){
			return str;
		}else{
			if(str.length()==2){
				return str.substring(1, 2);
			}else{
				return str;
			}
			
		}
	}
	
	/**
	 * 月份转换，如果月份是一位数则第一位补0
	 * @param str
	 * @return
	 */
	public static String transferTwoMonth (String str) {
		if(str== null ) return "";
		if(str.length() == 2) {
			return str;
		} else if(str.length() == 1) {
			return "0"+str;
		}
		return str;
	}
	
	/**
	 * 获取银行小图标
	 * @param logo
	 * @return
	 */
	public static int getDrawableOfSmallBank(String logo){
		String png=logo;
		if("bank1.png".equals(png)){
			return R.drawable.bank1_samll;
		}else if("bank3.png".equals(png)){
			return R.drawable.bank3_samll;
		}else if("bank4.png".equals(png)){
			return R.drawable.bank4_samll;
		}else if("bank2.png".equals(png)){
			return R.drawable.bank2_samll;
		}else if("bank8.png".equals(png)){
			return R.drawable.bank8_samll;
		}else if("bank11.png".equals(png)){
			return R.drawable.bank11_samll;
		}else if("bank12.png".equals(png)){
			return R.drawable.bank12_samll;
		}else if("bank13.png".equals(png)){
			return R.drawable.bank13_samll;
		}else if("bank14.png".equals(png)){
			return R.drawable.bank14_samll;
		}else if("bank15.png".equals(png)){
			return R.drawable.bank15_samll;
		}else if("bank16.png".equals(png)){
			return R.drawable.bank16_samll;
		}else if("bank17.png".equals(png)){
			return R.drawable.bank17_samll;
		}else if("bank28.png".equals(png)){
			return R.drawable.bank28_samll;
		}else if("bank90".equals(png)){
			return R.drawable.bank90_samll;
		}else if("bank97.png".equals(png)){
			return R.drawable.bank97_samll;
		}else if("bank101.png".equals(png)){
			return R.drawable.bank101_samll;
		}
		return R.drawable.bank_samll;
	}
	
	
	/**
	 * 获取银行大图标
	 * @param logo
	 * @return
	 */
	public static int getDrawableOfBigBank(String logo){
		String png=logo;
		System.out.println("图片"+png);
		if("bank1.png".equals(png)){
			return R.drawable.bank1_big;
		}else if("bank3.png".equals(png)){
			return R.drawable.bank3_big;
		}else if("bank4.png".equals(png)){
			return R.drawable.bank4_big;
		}else if("bank2.png".equals(png)){
			return R.drawable.bank2_big;
		}else if("bank8.png".equals(png)){
			return R.drawable.bank8_big;
		}else if("bank11.png".equals(png)){
			return R.drawable.bank11_big;
		}else if("bank12.png".equals(png)){
			return R.drawable.bank12_big;
		}else if("bank13.png".equals(png)){
			return R.drawable.bank13_big;
		}else if("bank14.png".equals(png)){
			return R.drawable.bank14_big;
		}else if("bank15.png".equals(png)){
			return R.drawable.bank15_big;
		}else if("bank16.png".equals(png)){
			return R.drawable.bank16_big;
		}else if("bank17.png".equals(png)){
			return R.drawable.bank17_big;
		}else if("bank28.png".equals(png)){
			return R.drawable.bank28_big;
		}else if("bank90.png".equals(png)){
			return R.drawable.bank90_big;
		}else if("bank97".equals(png)){
			return R.drawable.bank97_big;
		}else if("bank101.png".equals(png)){
			return R.drawable.bank101_big;
		}
		
		return R.drawable.bank_big;
	}
	
	/**
	 * 获取银行名称列表
	 * @param bankList
	 * @return
	 */
	public static String[] getBankNameList(ArrayList<BankData> bankList){
		String[] banks=new String[bankList.size()];
		for(int i=0;i<bankList.size();i++){
			banks[i]=bankList.get(i).bankname;
		}
		return banks;
	}
	
	/**
	 * 判断是否是易宝支持的银行
	 * @param bankList
	 * @param bankName
	 * @return
	 */
	public static boolean isYibaoBank(ArrayList<BankData> bankList, String bankName) {
		for(BankData bankData : bankList) {
			if(bankData.bankname.equals(bankName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 隐藏信用卡的信息
	 * 
	 * @param cardno
	 * @return
	 * @throw
	 * @return String
	 */
	public static String hideCardNo(String cardno ) {
		String result = "";
		if (cardno != null && cardno.length() >= 16) {
			result = cardno.substring(0, 4) + "********"
					+ cardno.substring(12, cardno.length());
		}
		return result;
	}
	
}
