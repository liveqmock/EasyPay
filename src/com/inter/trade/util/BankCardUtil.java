/*
 * @Title:  BankCardUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月23日 上午11:41:15
 * @version:  V1.0
 */
package com.inter.trade.util;

import java.util.ArrayList;

import com.inter.trade.db.SupportBank;
import com.inter.trade.ui.creditcard.data.DefaultBankCardData;

/**
 * 银行卡相关工具类
 * @author  ChenGuangChi
 * @data:  2014年7月23日 上午11:41:15
 * @version:  V1.0
 */
public class BankCardUtil {
	
	/**
	 * 判断是否为信用卡 
	 * @param card
	 * @return
	 * @throw
	 * @return boolean
	 */
	public static boolean isCreditCard(String card){
		if(card.length()==16){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * 判断刷卡的银行卡是否在易宝支持列表中
	 * @param creditCard 刷卡获取的数据
	 * @param bankList    易宝支持的银行列表
	 * @return
	 * @throw
	 * @return boolean
	 */
	public static boolean isSupportYiBao(DefaultBankCardData creditCard,ArrayList<SupportBank> bankList){
		
		if(!ListUtils.isEmptyList(bankList)&& creditCard!=null){
			for(SupportBank bank:bankList){
				if(creditCard.getBkcardbankid().equals(bank.getBankid())){
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * creditcard：信用卡    bankcard:储蓄卡
	 * @param types
	 * @return
	 * @throw
	 * @return String
	 */
	public static String getCardType(String types){
		String result="信用卡";
		if("creditcard".equals(types)){
			result="信用卡";
		}else if("bankcard".equals(types)){
			result="储蓄卡";
		}
		return result;
	}
}
