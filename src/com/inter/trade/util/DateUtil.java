package com.inter.trade.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;



/**
 * 日期辅助类
 * @author zhichao.huang
 *
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {
	
	/**
	 * 获取当天日期
	 * @return
	 */
	public static String getNowDate(){   
	    String temp_str="";   
	    Date dt = new Date();   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    temp_str = sdf.format(dt);   
	    return temp_str;   
	} 
	
	/** 
     * 获得指定日期的前一天 
     *  
     * @param specifiedDay 
     * @return 
     * @throws Exception 
     */  
    public static String getSpecifiedDayBefore(String specifiedDay) {//可以用new Date().toLocalString()传递参数  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1);  
  
        String dayBefore = new SimpleDateFormat("yyyy-MM-dd").format(c  
                .getTime());  
        return dayBefore;  
    }  
  
    /** 
     * 获得指定日期的后一天 
     *  
     * @param specifiedDay 
     * @return 
     */  
    public static String getSpecifiedDayAfter(String specifiedDay) {  
        Calendar c = Calendar.getInstance();  
        Date date = null;  
        try {  
            date = new SimpleDateFormat("yy-MM-dd").parse(specifiedDay);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day + 1);  
  
        String dayAfter = new SimpleDateFormat("yyyy-MM-dd")  
                .format(c.getTime());  
        return dayAfter;  
    }  
    
    /**
     * 比较日期，规定的日期和当天的日期进行比较
     * 如果规定的日期在当天日期之前返回true; 否则false
     * @param specifiedDay 规定的日期
     * @return
     */
    public static boolean compareDate(String specifiedDay) {
    	
        try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date date = sdf.parse(specifiedDay);
        	return date.before(new Date());
        	
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * 日期比较
     * 
     * @param specifiedDay
     * @param afterDate
     * @return
     */
    public static boolean compareDate(String specifiedDay, Date afterDate) {
    	try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date date = sdf.parse(specifiedDay);
        	return date.before(afterDate);
        	
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        }
        return false;
    }
    
    /**
     * 日期比较
     * 
     * @param specifiedDay
     * @param afterDate
     * @return
     */
    public static boolean compareDate(String specifiedDay, String afterDate) {
    	try {
        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        	Date beforeDate = sdf.parse(specifiedDay);
        	Date _afterDate = sdf.parse(afterDate);
        	return beforeDate.before(_afterDate);
        	
        } catch (ParseException e) {
        	System.out.println(e.getMessage());
        }
        return false;
    }
    
    
    /**
     * 将Date格式转化成yyyy-MM格式的字符串 
     * @param date
     * @return
     * @throw
     * @return String
     */
    public static String getYYYYMMFormatStr(Date date){
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	String formatDate = sdf.format(date);
    	return formatDate;
    }
    
    /**
     * 
     * 将yyyy-MM格式的字符串转化成 Date格式
     * @param date
     * @return
     * @throw
     * @return Date
     */
    public static Date getDateForYYYYMM(String date){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
    	Date parser=null;
		try {
			parser = sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return parser;
    }
    
    /**
     * 将Date格式转化成yyyy-MM-dd格式的字符串 
     * @param date
     * @return
     * @throw
     * @return String
     */
    public static String getYYYYMMddFormatStr(Date date){
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    	String formatDate = sdf.format(date);
    	return formatDate;
    }
}
