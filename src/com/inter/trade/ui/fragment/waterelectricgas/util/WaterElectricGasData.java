package com.inter.trade.ui.fragment.waterelectricgas.util;

import java.util.ArrayList;
import java.util.HashMap;

public class WaterElectricGasData {
	/*
	 * 省份名称
	 */
	public String provinceName;
	
	/*
	 * 城市名称
	 */
	public String cityName;
	
	/*
	 * 城市名称拼音首字母A~Z
	 */
	public String cityNameTag;
	
	/*
	 * 水电煤缴费类型，有3个元素
	 */
	public ArrayList<Integer> payTypeList;
//	public String[] payTypeList = new String[3];
//	public int[] payTypeList;
//	public HashMap<String, Integer> payTypeList = new HashMap<String, Integer>();
	
	/*
	 * 水电煤缴费类型所对应的缴费公司，有不定个数元素，如无该项缴费类型，则公司为空
	 */
	public ArrayList<ArrayList<String>> companyLList;
	
	/*
	 * 水电煤缴费类型所对应的缴费公司ID，有不定个数元素，如无该项缴费类型，则公司ID为空
	 */
	public ArrayList<ArrayList<String>> companyIdLList;

}
