/*
 * @Title:  BillData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月25日 下午2:34:58
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.gamerecharge.data;

import java.io.Serializable;

/**
 * 订单信息
 * @author  ChenGuangChi
 * @data:  2014年6月25日 下午2:34:58
 * @version:  V1.0
 */
public class BillData implements Serializable{

		private String bkntno;//流水号
		private String totalPrice;//账号
		private String orderid;//订单号
		
		
		public String getOrderid() {
			return orderid;
		}
		public void setOrderid(String orderid) {
			this.orderid = orderid;
		}
		public String getBkntno() {
			return bkntno;
		}
		public void setBkntno(String bkntno) {
			this.bkntno = bkntno;
		}
		public String getTotalPrice() {
			return totalPrice;
		}
		public void setTotalPrice(String totalPrice) {
			this.totalPrice = totalPrice;
		}
		
}
