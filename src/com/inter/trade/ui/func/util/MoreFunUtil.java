/*
 * @Title:  MoreFunUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年8月6日 下午2:11:33
 * @version:  V1.0
 */
package com.inter.trade.ui.func.util;

import java.util.ArrayList;

import com.inter.trade.R;
import com.inter.trade.ui.func.FuncData;

/**
 * 初始化更多的菜单
 * @author  ChenGuangChi
 * @data:  2014年8月6日 下午2:11:33
 * @version:  V1.0
 */
public class MoreFunUtil {
		
	/**
	 * 获取更多菜单的数据 
	 * @return
	 * @throw
	 * @return ArrayList<FuncData>
	 */
	public static ArrayList<FuncData> getMoreFun(){
		
		ArrayList<FuncData> fList=new ArrayList<FuncData>();
		
		FuncData cridetData = new FuncData();
		cridetData.imageId = R.drawable.icon_account_main;
		cridetData.name = "账户管理";
		fList.add(cridetData);
		
		FuncData data2 = new FuncData();
		data2.imageId = R.drawable.icon_activate_main;
		data2.name = "服务信息";
		fList.add(data2);
		
		FuncData data3 = new FuncData();
		data3.imageId = R.drawable.icon_agent_main;
		data3.name = "代理商";
		fList.add(data3);
		
		FuncData data4 = new FuncData();
		data4.imageId = R.drawable.icon_help_main;
		data4.name = "帮助中心";
		fList.add(data4);
		
		FuncData data5 = new FuncData();
		data5.imageId = R.drawable.icon_feedback_main;
		data5.name = "意见反馈";
		fList.add(data5);
		
		FuncData data6 = new FuncData();
		data6.imageId = R.drawable.icon_about_main;
		data6.name = "关于我们";
		fList.add(data6);
		
		
		FuncData data7 = new FuncData();
		data7.imageId = R.drawable.icon_update_main;
		data7.name = "检查更新";
		fList.add(data7);
		
		FuncData data8 = new FuncData();
		data8.imageId = R.drawable.icon_download_main;
		data8.name = "APP下载";
		fList.add(data8);
		
		FuncData data9 = new FuncData();
		data9.imageId = R.drawable.icon_recommend;
		data9.name = "推荐应用";
		//fList.add(data9);
		
		return fList;
	}
}
