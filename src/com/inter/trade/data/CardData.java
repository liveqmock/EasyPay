package com.inter.trade.data;

public class CardData extends BaseData{
	public String firmwareVersion;
	public String encryptionMode;
	public String trackInfo;
	public String first6Pan;
	public String xxx;
	public String last4Pan;
	public String expiryDate;
	public String userName;
	public String ksn;
	public String encrypedData;
	public String pan;
	public String decrypedData;
	/**
	 * 第二磁道信息(刷卡后获取的原始数据)
	 */
	public String twoTrack23InfoSRC;
	/**
	 * 第二磁道信息(格式化后的信息)
	 */
	public String twoTrack23Info;
	/**
	 * 第一磁道信息(刷卡后获取的原始数据)
	 */
	public String oneTrack1InfoSRC;
}
