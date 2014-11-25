package com.inter.trade.data;

import java.util.HashMap;

public class BaseData implements SunType{
	public String authorid;
	public static String merReserved="merReserved";
	public HashMap<String, String> sunMap = new HashMap<String, String>();
	
	public void putValue(String key,String value){
		sunMap.put(key, value);
	}
	
	public String getValue(String key){
		return sunMap.get(key);
	}
}
