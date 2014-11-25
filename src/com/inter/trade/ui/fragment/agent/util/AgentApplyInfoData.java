package com.inter.trade.ui.fragment.agent.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.inter.trade.data.BaseData;

public class AgentApplyInfoData {
	/*
	 * 代理商申请，名义ID
	 */
	public String custypeid;
	
	/*
	 * 代理商申请,名义内容
	 */
	public String custypename;
	
	/*
	 * 代理商申请，有3个名义
	 */
	public ArrayList<BaseData> infoDataList;
	
	/*
	 * 代理商申请，以某个名义申请时所需的详细资料1~4项
	 */
	public ArrayList<String> infoStrList;
	
	/*
	 * 代理商申请，以某个名义申请时所需的详细资料1~4项
	 */
	public BaseData infoData;

//	class InfoData {
//		/*
//		 * 代理商申请,图片分类id
//		 */
//		public String pictypeid;
//		
//		/*
//		 * 代理商申请,图片名
//		 */
//		public String pictypename;
//		
//		/*
//		 * 代理商申请,图片编号
//		 */
//		public String pictypeno;
//		
//		/*
//		 * 代理商申请,图片上传地址
//		 */
//		public String picuploadurl;
	
//		/*
//		 * (自定义)代理商申请,是否已选择图片
//		 */
//		public String selectpic;
//		
//	}
}
