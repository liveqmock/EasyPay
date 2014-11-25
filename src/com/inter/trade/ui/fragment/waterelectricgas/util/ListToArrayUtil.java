/*
 * @Title:  ListToArrayUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年6月24日 上午10:53:55
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.waterelectricgas.util;

import java.util.ArrayList;

import com.inter.trade.ui.fragment.gamerecharge.data.AreaData;

/**
 * List转化成string[]
 * @author  ChenGuangChi
 * @data:  2014年6月24日 上午10:53:55
 * @version:  V1.0
 */
public class ListToArrayUtil {
	
	/**
	 *   将省份转化成数组
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 *//*
	public static String[] toProvinceArray(ArrayList<ProviceData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		return null;
	}
	
	*//**
	 *   将城市转化成数组
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 *//*
	public static String[] toCityArray(ArrayList<CityData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		return null;
	}
	
	*//**
	 *   将产品转化成数组
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 *//*
	public static String[] toProductArray(ArrayList<ProductData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		return null;
	}
	
	*//**
	 *   将公司转化成数组
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 *//*
	public static String[] toCompanyArray(ArrayList<CompanyData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		return null;
	}
	
	*//**
	 *   将商品转化成数组
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 *//*
	public static String[] toPriceArray(ArrayList<PriceData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		return null;
	}
	*/
	/**
	 *将区服转化成String数组 
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 */
	public static String[] toAreaArray(ArrayList<AreaData> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i).getName();
			}
			return arr;
		}
		
		return null;
	}
	
	/**
	 *将服务器转化成String数组 
	 * @param mList
	 * @return
	 * @throw
	 * @return String[]
	 */
	public static String[] toServerArray(ArrayList<String> mList){
		if(mList!=null && mList.size()>0){
			String[] arr=new String[mList.size()];
			for(int i=0;i<mList.size();i++){
				arr[i]=mList.get(i);
			}
			return arr;
		}
		
		return null;
	}
}
