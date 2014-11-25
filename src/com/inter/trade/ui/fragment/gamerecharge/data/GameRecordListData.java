package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

/**
 * 
 * @author chenguangchi
 *
 */
public class GameRecordListData implements Serializable{
	
	private String bkntno;//银联流水号
	private String quantity;//充值数量
	private String gameName;//游戏名称
	private String status;//支付状态
	private String totalPrice;//总价
	private String completeTime;//完成支付时间
	private String price;//单价
	private String area;//游戏分区
	private String account;//账号
	
	
	
	public String getBkntno() {
		return bkntno;
	}
	public void setBkntno(String bkntno) {
		this.bkntno = bkntno;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCompleteTime() {
		return completeTime;
	}
	public void setCompleteTime(String completeTime) {
		this.completeTime = completeTime;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	
	
	
}
