package com.inter.trade.error;

public interface ErrorDeal {
	/**
	 * @param headMsg 头部返回消息信息
	 * @param bodyMsg body返回的消息信息
	 */
	public void success(String headMsg,String bodyMsg);
	public void fail(String failMsg);
}
