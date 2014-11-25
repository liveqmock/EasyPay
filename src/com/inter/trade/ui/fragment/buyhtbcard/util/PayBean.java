package com.inter.trade.ui.fragment.buyhtbcard.util;

import java.io.Serializable;

import com.inter.trade.data.SunType;

/**
 * 用于处理支付阶段的辅助对象
 * 
 * @author zhichao.huang
 *
 */
public class PayBean implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4725481852376210602L;

	/**
	 * 支付类型
	 */
	private String payType;
	
	//--------------------易宝请求交易-------------------------
	/**
	 * 易宝请求交易流水API接口名
	 */
	private String apiNameYibaoRequestTransaction;
	
	/**
	 * 易宝请求交易流水API函数名
	 */
	private String apiFuncYibaoRequestTransaction;
	
	/**
	 * 易宝短信支付验证API接口名
	 * 一般在请求交易流水API接口后运作
	 */
	private String apiNameYibaoSMSVerify;
	
	/**
	 * 易宝短信支付验证API函数名
	 * 一般在请求交易流水API接口后运作
	 */
	private String apiFuncYibaoSMSVerify;
	
	//---------------------银联请求交易-------------------------
	
	/**
	 * 银联请求交易流水API接口名
	 */
	private String apiNameUnionPayRequestTransaction;
	
	/**
	 * 银联请求交易流水API函数名
	 */
	private String apiFuncUnionPayRequestTransaction;
	
	/**
	 * 银联支付状态反馈API接口名
	 */
	private String apiNameUnionPayState;
	
	/**
	 * 银联支付状态反馈API函数名
	 */
	private String apiFuncUnionPayState;
	
	//-----------------------成功页面的ID-----------------------
	/**
	 * 支付成功后，对应成功页面的ID
	 */
	private int paySuccessUIId;
	
	/**
	 * 网络请求数据
	 */
	private SunType requestData;
	
	/**
	 * 扩展数据
	 */
	private Object expandData;
	
	public PayBean (SunType data) {
		this.requestData = data;
	}

	/**
	 * 获取支付类型
	 * @return
	 */
	public String getPayType() {
		return payType;
	}

	/**
	 * 设置设置支付类型
	 * @param payType
	 */
	public void setPayType(String payType) {
		this.payType = payType;
	}

	/**
	 * 获取易宝请求交易流水API接口名
	 * @return
	 */
	public String getApiNameYibaoRequestTransaction() {
		return apiNameYibaoRequestTransaction;
	}

	/**
	 * 设置易宝请求交易流水API接口名
	 * @param apiNameYibaoRequestTransaction
	 */
	public void setApiNameYibaoRequestTransaction(
			String apiNameYibaoRequestTransaction) {
		this.apiNameYibaoRequestTransaction = apiNameYibaoRequestTransaction;
	}

	/**
	 * 获取易宝请求交易流水API函数名
	 * @return
	 */
	public String getApiFuncYibaoRequestTransaction() {
		return apiFuncYibaoRequestTransaction;
	}

	/**
	 * 设置易宝请求交易流水API函数名
	 * @param apiFuncYibaoRequestTransaction
	 */
	public void setApiFuncYibaoRequestTransaction(
			String apiFuncYibaoRequestTransaction) {
		this.apiFuncYibaoRequestTransaction = apiFuncYibaoRequestTransaction;
	}

	/**
	 * 获取易宝短信支付验证API接口名
	 * @return
	 */
	public String getApiNameYibaoSMSVerify() {
		return apiNameYibaoSMSVerify;
	}

	/**
	 * 设置易宝短信支付验证API接口名
	 * @param apiNameYibaoSMSVerify
	 */
	public void setApiNameYibaoSMSVerify(String apiNameYibaoSMSVerify) {
		this.apiNameYibaoSMSVerify = apiNameYibaoSMSVerify;
	}

	/**
	 * 获取易宝短信支付验证API函数名
	 * @return
	 */
	public String getApiFuncYibaoSMSVerify() {
		return apiFuncYibaoSMSVerify;
	}

	/**
	 * 设置易宝短信支付验证API函数名
	 * @param apiFuncYibaoSMSVerify
	 */
	public void setApiFuncYibaoSMSVerify(String apiFuncYibaoSMSVerify) {
		this.apiFuncYibaoSMSVerify = apiFuncYibaoSMSVerify;
	}

	/**
	 * 获取银联请求交易流水API接口名
	 * @return
	 */
	public String getApiNameUnionPayRequestTransaction() {
		return apiNameUnionPayRequestTransaction;
	}

	/**
	 * 设置银联请求交易流水API接口名
	 * @param apiNameUnionPayRequestTransaction
	 */
	public void setApiNameUnionPayRequestTransaction(
			String apiNameUnionPayRequestTransaction) {
		this.apiNameUnionPayRequestTransaction = apiNameUnionPayRequestTransaction;
	}

	/**
	 * 获取银联请求交易流水API函数名
	 * @return
	 */
	public String getApiFuncUnionPayRequestTransaction() {
		return apiFuncUnionPayRequestTransaction;
	}

	/**
	 * 设置银联请求交易流水API函数名
	 * @param apiFuncUnionPayRequestTransaction
	 */
	public void setApiFuncUnionPayRequestTransaction(
			String apiFuncUnionPayRequestTransaction) {
		this.apiFuncUnionPayRequestTransaction = apiFuncUnionPayRequestTransaction;
	}

	/**
	 * 获取银联支付状态反馈API接口名
	 * @return
	 */
	public String getApiNameUnionPayState() {
		return apiNameUnionPayState;
	}

	/**
	 * 设置银联支付状态反馈API接口名
	 * @param apiNameUnionPayState
	 */
	public void setApiNameUnionPayState(String apiNameUnionPayState) {
		this.apiNameUnionPayState = apiNameUnionPayState;
	}

	/**
	 * 获取银联支付状态反馈API函数名
	 * @return
	 */
	public String getApiFuncUnionPayState() {
		return apiFuncUnionPayState;
	}

	/**
	 * 设置银联支付状态反馈API函数名
	 * @param apiFuncUnionPayState
	 */
	public void setApiFuncUnionPayState(String apiFuncUnionPayState) {
		this.apiFuncUnionPayState = apiFuncUnionPayState;
	}

	/**
	 * 获取支付成功后，对应成功页面的ID
	 * @return
	 */
	public int getPaySuccessUIId() {
		return paySuccessUIId;
	}

	/**
	 * 设置支付成功后，对应成功页面的ID
	 * @param paySuccessUIId
	 */
	public void setPaySuccessUIId(int paySuccessUIId) {
		this.paySuccessUIId = paySuccessUIId;
	}

	/**
	 * 扩展数据
	 */
	public Object getExpandData() {
		return expandData;
	}

	/**
	 * 设置扩展数据
	 */
	public void setExpandData(Object expandData) {
		this.expandData = expandData;
	}
	
	

}
