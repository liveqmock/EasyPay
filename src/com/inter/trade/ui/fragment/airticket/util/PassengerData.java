/*
 * @Title:  PassengerData.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月15日 下午4:56:39
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.airticket.util;

import java.io.Serializable;

/**
 * 乘机人
 * @author  ChenGuangChi
 * @data:  2014年7月15日 下午4:56:39
 * @version:  V1.0
 */
public class PassengerData implements Serializable{
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 年龄段  ADU（成人），CHI（儿童）， BAB（婴儿）
	 */
	private String  ageRange;
	/**
	 * 证件类型  1 身份证，2 护照，4 军人证，7 回乡证，8 台胞证，10港澳通行证，11国际海员证，20外国人永久居留证，25户口簿，27出生证明，99其它
	 */
	private String idtype;
	/**
	 * 证件号
	 */
	private String passportNo;
	
	/**
	 * 生日  形如：1984-01-01
	 */
	private String birthDay;
	
	/**
	 * 标识是否进行编辑
	 */
	private boolean isUpdate=false;
	
	/**
	 * 电话号码
	 */
	private String phoneNumber;
	
	/**
	 *ID,用于删除某个联系人
	 */
	private String id;
	
	/**
	 * 乘客类型：默认值1代表成人，2代表儿童，3代表婴儿
	 */
	private String passengerType;
	
	/**
	 * 标识是否处于选中状态
	 */
	private boolean isCheck = false;
	

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public boolean isUpdate() {
		return isUpdate;
	}

	public void setUpdate(boolean isUpdate) {
		this.isUpdate = isUpdate;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public String getIdtype() {
		return idtype;
	}

	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}


	public String getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(String birthDay) {
		this.birthDay = birthDay;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassengerType() {
		return passengerType;
	}

	public void setPassengerType(String passengerType) {
		this.passengerType = passengerType;
	}

	
	
}
