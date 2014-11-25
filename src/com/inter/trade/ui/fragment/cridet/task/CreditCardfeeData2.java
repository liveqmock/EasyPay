package com.inter.trade.ui.fragment.cridet.task;

import com.inter.trade.data.SunType;

/**
 * 获取手续费
 * 
 * @author zhichao.huang
 *
 */
public class CreditCardfeeData2 implements SunType{
	public String paymoney;//还款金额
	public String feemoney;//手续费用
	public String allmoney;//总额
	public String bkntno;//交易流水号
	
	public String getPaymoney() {
		return paymoney;
	}
	public void setPaymoney(String paymoney) {
		this.paymoney = paymoney;
	}
	public String getFeemoney() {
		return feemoney;
	}
	public void setFeemoney(String feemoney) {
		this.feemoney = feemoney;
	}
	public String getAllmoney() {
		return allmoney;
	}
	public void setAllmoney(String allmoney) {
		this.allmoney = allmoney;
	}
	public String getBkntno() {
		return bkntno;
	}
	public void setBkntno(String bkntno) {
		this.bkntno = bkntno;
	}
	
	
}
