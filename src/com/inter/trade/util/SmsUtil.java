/*
 * @Title:  SmsUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月4日 上午10:05:08
 * @version:  V1.0
 */
package com.inter.trade.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;

/**
 * 手机验证码的工具类
 * @author  ChenGuangChi
 * @data:  2014年8月4日 上午10:05:08
 * @version:  V1.0
 */
public class SmsUtil {
	
	/**
	 * SMS_SENDER   发送验证码的手机号码
	 */
	private static final String SMS_SENDER2 = "1252013726154947";
	private static final String SMS_SENDER3 = "1252013726154947";
	private static final String SMS_SENDER1 = "1252013726154947";
	/**
	 * VERIFICATION_CODE_LEN 手机验证码的长度
	 */
	public static final int VERIFICATION_CODE_LEN = 3;


	/**
	 * 提取手机验证码 
	 * @param c
	 * @return
	 * @throw
	 * @return String
	 */
	public static String getsmsyzm(Activity c) {
		Uri uri = Uri.parse("content://sms/inbox");
		String[] projection = new String[] { "address", "person", "body" };
		
		String selection1 = " address='" +SMS_SENDER1+ "' ";
		String selection2 = " address='" +SMS_SENDER2+ "' ";
		String selection3 = " address='" +SMS_SENDER3+ "' ";
		
		
		String[] selectionArgs = new String[] {};
		String sortOrder = "date desc";
		@SuppressWarnings("deprecation")
		Cursor cur = c.managedQuery(uri, projection, selection1, selectionArgs,
				sortOrder);
		if(cur!=null&&cur.getCount()>0){
			cur.moveToFirst();
			String body = cur.getString(cur.getColumnIndex("body")).replaceAll(
					"\n", " ");
			cur.close();
			return getyzm(body, VERIFICATION_CODE_LEN);
		}
		cur.close();
		
		@SuppressWarnings("deprecation")
		Cursor cursor2 = c.managedQuery(uri, projection, selection2, selectionArgs,
				sortOrder);
		if(cursor2!=null&&cursor2.getCount()>0){
			cursor2.moveToFirst();
			String body = cursor2.getString(cursor2.getColumnIndex("body")).replaceAll(
					"\n", " ");
			cursor2.close();
			return getyzm(body, VERIFICATION_CODE_LEN);
		}
		cursor2.close();
		
		@SuppressWarnings("deprecation")
		Cursor cursor3 = c.managedQuery(uri, projection, selection3, selectionArgs,
				sortOrder);
		if(cursor3!=null&&cursor3.getCount()>0){
			cursor3.moveToFirst();
			String body = cursor3.getString(cursor3.getColumnIndex("body")).replaceAll(
					"\n", " ");
			cursor3.close();
			return getyzm(body, VERIFICATION_CODE_LEN);
		}
		cursor3.close();
		return null;
	}


        /**
	 * 从短信字符窜提取验证码
	 * @param body 短信内容 
         * @param YZMLENGTH  验证码的长度 一般6位或者4位
	 * @return 接取出来的验证码
	 */
	public static String getyzm(String body, int YZMLENGTH) {
		// 首先([a-zA-Z0-9]{YZMLENGTH})是得到一个连续的六位数字字母组合
		// (?<![a-zA-Z0-9])负向断言([0-9]{YZMLENGTH})前面不能有数字
		// (?![a-zA-Z0-9])断言([0-9]{YZMLENGTH})后面不能有数字出现
		Pattern p = Pattern
 				.compile("(?<![a-zA-Z0-9])([a-zA-Z0-9]{" + YZMLENGTH + "})(?![a-zA-Z0-9])");
		Matcher m = p.matcher(body);
		if (m.find()) {
			System.out.println(m.group());
			return m.group(0);
		}
		return null;
	}
}
