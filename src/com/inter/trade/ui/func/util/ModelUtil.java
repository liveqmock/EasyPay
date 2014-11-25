/*
 * @Title:  ModelUtil.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年7月7日 下午1:15:02
 * @version:  V1.0
 */
package com.inter.trade.ui.func.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import com.inter.trade.R;
import com.inter.trade.db.Record;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.func.FuncData;
import com.inter.trade.ui.func.FuncMap;
import com.inter.trade.ui.func.data.IconData;
import com.inter.trade.ui.func.data.ModleData;
import com.inter.trade.util.PreferenceConfig;

/**
 * 首页功能模块的工具类
 * @author  ChenGuangChi
 * @data:  2014年7月7日 下午1:15:02
 * @version:  V1.0
 */
public class ModelUtil {
	public static HashMap<Integer, FuncData> functionList;
	public static HashMap<String,FuncData> functionMap;
	
	public static final int DEFAULT_COUNT = 9;// 默认每页显示12个功能菜单
	
	/***记录默认的功能项目的id集合*/
	private static ArrayList<Integer> favourKeyList=new ArrayList<Integer>();
	/**
	 * 初始化所有的功能模块图标
	 * @return
	 * @throw
	 * @return ArrayList<FuncData>
	 */
	private static void initFunctionMap() {
		

		FuncData cridetData = new FuncData();
		cridetData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.CRIDET_FUNC_KEY, FuncMap.CRIDET_INDEX_FUNC);
		cridetData.imageId = R.drawable.selector_icon_visa;
		cridetData.name = "信用卡还款";
		
		FuncData transferData = new FuncData();
		transferData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TRANSFER_FUNC_KEY, FuncMap.TRANSFER_INDEX_FUNC);
		transferData.imageId = R.drawable.selector_icon_transfer;
		transferData.name = "转账汇款";
		
		FuncData couponData = new FuncData();
		couponData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.COUPON_FUNC_KEY, FuncMap.COUPON_INDEX_FUNC);
		couponData.imageId = R.drawable.selector_icon_coupon;
		couponData.name = "商户收款";
		
		FuncData expressData = new FuncData();
		expressData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.EXPRESS_FUNC_KEY, FuncMap.EXPRESS_INDEX_FUNC);
		expressData.imageId = R.drawable.selector_icon_expressage;
		expressData.name = "快递查询";
		
		FuncData balanceData = new FuncData();
		balanceData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BALANCE_FUNC_KEY, FuncMap.BALANCE_INDEX_FUNC);
		balanceData.imageId = R.drawable.selector_icon_balance;
		balanceData.name = "余额查询";
		
		// 新增话费充值
		FuncData telephoneData = new FuncData();
		telephoneData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TELEPHONE_FUNC_KEY, FuncMap.TELEPHONE_INDEX_FUNC);
		telephoneData.imageId = R.drawable.selector_icon_mobilecharge;
		telephoneData.name = "话费充值";
		
		// 新增Q币充值
		FuncData qMoneyData = new FuncData();
		qMoneyData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.QMONEY_FUNC_KEY, FuncMap.QMONEY_INDEX_FUNC);
		qMoneyData.imageId = R.drawable.selector_icon_qq;
		qMoneyData.name = "Q币充值";
		
		// 新增代理商
		FuncData agentData = new FuncData();
		agentData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AGENT_FUNC_KEY, FuncMap.AGENT_INDEX_FUNC);
		agentData.imageId = R.drawable.selector_icon_agent;
		agentData.name = "代理商";
		
		// 购买刷卡器
		FuncData buySwipCardData = new FuncData();
		buySwipCardData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BUY_SWIPCARD_FUNC_KEY, FuncMap.BUY_SWIPCARD_INDEX_FUNC);
		buySwipCardData.imageId = R.drawable.selector_icon_buycard;
		buySwipCardData.name = "购买刷卡器";
		
		// 水电煤缴费
		FuncData waterElectricGasData = new FuncData();
		waterElectricGasData.mnuid = PreferenceConfig.instance(PayApp.pay)
				.getInt(FuncMap.WATER_ELECTRIC_GAS_FUNC_KEY,
						FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC);
		waterElectricGasData.imageId = R.drawable.selector_icon_waterfee;
		waterElectricGasData.name = "水电煤缴费";
		
		// 游戏充值
		FuncData gameRechargeData = new FuncData();
		gameRechargeData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.GAME_RECHARGE_FUNC_KEY,
				FuncMap.GAME_RECHARGE_INDEX_FUNC);
		gameRechargeData.imageId = R.drawable.selector_icon_game;
		gameRechargeData.name = "游戏充值";
		

		// 机票
		FuncData airTicketData = new FuncData();
		airTicketData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AIR_TICKET_FUNC_KEY, FuncMap.AIR_TICKET_INDEX_FUNC);
		airTicketData.imageId = R.drawable.selector_icon_plane;
		airTicketData.name = "机票预订";
		
		//火车票
		FuncData trainTicketData = new FuncData();
		trainTicketData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TRAIN_TICKET_FUNC_KEY, FuncMap.TRAIN_TICKET_INDEX_FUNC);
		trainTicketData.imageId = R.drawable.selector_icon_train;
		trainTicketData.name = "火车票预订";
		
		//酒店
		FuncData hotelData = new FuncData();
		hotelData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.HOTEL_FUNC_KEY, FuncMap.HOTEL_INDEX_FUNC);
		hotelData.imageId = R.drawable.selector_icon_hotel;
		hotelData.name = "酒店预订";
		
		//以Id为key
		functionList = new HashMap<Integer, FuncData>();
		functionList.put(FuncMap.CRIDET_INDEX_FUNC, cridetData);
		functionList.put(FuncMap.TRANSFER_INDEX_FUNC, transferData);
		functionList.put(FuncMap.COUPON_INDEX_FUNC, couponData);
		functionList.put(FuncMap.EXPRESS_INDEX_FUNC, expressData);
		functionList.put(FuncMap.BALANCE_INDEX_FUNC, balanceData);
		functionList.put(FuncMap.TELEPHONE_INDEX_FUNC, telephoneData);
		functionList.put(FuncMap.QMONEY_INDEX_FUNC, qMoneyData);
		functionList.put(FuncMap.AGENT_INDEX_FUNC, agentData);
		functionList.put(FuncMap.BUY_SWIPCARD_INDEX_FUNC, buySwipCardData);
		functionList.put(FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC,
				waterElectricGasData);
		functionList.put(FuncMap.GAME_RECHARGE_INDEX_FUNC, gameRechargeData);
		functionList.put(FuncMap.AIR_TICKET_INDEX_FUNC, airTicketData);
		functionList.put(FuncMap.TRAIN_TICKET_INDEX_FUNC, trainTicketData);
		functionList.put(FuncMap.HOTEL_INDEX_FUNC, hotelData);
		
		//以编号为key
		functionMap=new HashMap<String, FuncData>();
		functionMap.put(FuncMap.CREDITCARD, cridetData);
		functionMap.put(FuncMap.TFMG, transferData);
		functionMap.put(FuncMap.COUPON, couponData);
		functionMap.put(FuncMap.DELIVERY, expressData);
		functionMap.put(FuncMap.BALANCE, balanceData);
		functionMap.put(FuncMap.MOBILERECHARGE, telephoneData);
		functionMap.put(FuncMap.QQRECHARGE, qMoneyData);
		functionMap.put(FuncMap.AGENTBUY, agentData);
		functionMap.put(FuncMap.ORDERBUY, buySwipCardData);
		functionMap.put(FuncMap.FAMILY,
				waterElectricGasData);
		functionMap.put(FuncMap.GAME, gameRechargeData);
		functionMap.put(FuncMap.AIRPLANE, airTicketData);
		functionMap.put(FuncMap.TRAIN, trainTicketData);
		functionMap.put(FuncMap.HOTEL, hotelData);
	}
	/**
	 * 
	 * 根据返回数据排序首页模块和分类功能模块
	 * @param modle
	 * @return
	 * @throw
	 * @return ArrayList<ArrayList<FuncData>>
	 */
	public static ArrayList<ArrayList<FuncData>> getFunctions(ModleData modle){
		initFunctionMap();
		ArrayList<ArrayList<FuncData>> funList=new ArrayList<ArrayList<FuncData>>(); 
		ArrayList<FuncData> favourList=new ArrayList<FuncData>();//首页列表的功能列表
		
		//默认首页的列表
		ArrayList<IconData> mList = modle.getIconList();	
		ArrayList<IconData> record=new ArrayList<IconData>();
		
		for(IconData icons:mList){//添加点击次数大于0的
			String pointnum = icons.getPointnum();
			try {
				int num = FormatUtil.parserInt(pointnum);
				if(num>0){
					record.add(icons);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Collections.sort(record,new PointNumComparator());//根据点击次数排序
		//截取前9条记录
		ArrayList<IconData> tempList=new ArrayList<IconData>();
		for(int i=0;i<record.size();i++){
			if(i==9) break;
			tempList.add(record.get(i));
		}
		record=tempList;
		
		ArrayList<FuncData> recordList=new ArrayList<FuncData>();//用户点击记录列表
		for(IconData id:record){
			//recordList.add(findFun(Integer.parseInt(id.getMnuid())));
			recordList.add(findFun(id.getMnuno()));
		}
		
		ArrayList<IconData> fList=new ArrayList<IconData>();//获取默认首页功能
		for(IconData icondata:mList){
			if("1".equals(icondata.getMnuisconst())){
				fList.add(icondata);
				//favourList.add(findFun(Integer.parseInt(icondata.getMnuid())));
				favourList.add(findFun(icondata.getMnuno()));
			}
		}
		getDefaultKeyList(favourList);
		
		if(record.size()>0){//用户开始记录使用习惯
			if(record.size()==9){//功能的使用个数为9
				favourList.clear();
				for(FuncData data:recordList){
					favourList.add(data);
				}
			}else{//功能的记录少于9个
				int size = favourList.size();
				System.out.println("-----------------------favour-size:"+size);
				out: for(int i=size-1;i>=0;i--){//判断记录功能是否在默认列表中,在的删掉默认列表中存在的
					for(FuncData icon:recordList){
						if(isDefault(icon.mnuid)){
							System.out.println("-----------------------icon-mnuid:"+icon.mnuid);
							favourList.remove(icon);
							continue out;
						}
					}
				}
				System.out.println("-----------------------favour-size:"+favourList.size());
				System.out.println("-----------------------record-size:"+recordList.size());
				if(favourList.size()+recordList.size()>9){//当前两者的和大于9，删除默认首页的最后几直到两者之和为9
					int fsize =favourList.size();
					int rsize =recordList.size();
					System.out.println("当前默认功能"+fsize+"当前记录"+rsize);
					int deletesize=fsize+rsize-9;
					int add = fsize-deletesize;
					int deleteno=fsize;
					for(int i=fsize;i>add;i--){
						favourList.remove(deleteno-1);
						deleteno-=1;
					}
				}
				for(FuncData r:recordList){//将用户点击的功能项添加到默认的首页中
					favourList.add(r);
				}
		   }
		}
		funList.add(favourList);
		
		//各种分类的列表
		HashMap<String,ArrayList<FuncData>> map=new HashMap<String, ArrayList<FuncData>>();//以mnutypeid为key
		ArrayList<IconData> iconList = mList;
		System.out.println("获取的功能模块的个数:"+iconList.size());
		if(iconList!=null){
			for(IconData icon:iconList){
				String mnutypeid = icon.getMnutypeid();
				ArrayList<FuncData> arrayList=null;
				if(map.containsKey(mnutypeid)){//包含这个值
					arrayList = map.get(mnutypeid);
				}else{
					arrayList=new ArrayList<FuncData>();
				}
				//FuncData fun = findFun(Integer.parseInt(icon.getMnuid()));
				FuncData fun = findFun(icon.getMnuno());
				fun.orderId=FormatUtil.parserInt(icon.getMnuorder());
				fun.id=icon.getMnuid();
				arrayList.add(fun);
				map.remove(mnutypeid);
				map.put(mnutypeid, arrayList);
			}
			//对相应的分类进行排序
			for(String key:map.keySet()){
				ArrayList<FuncData> list = map.get(key);
				Collections.sort(list, new OrderComparator());
			}
			//判断各个分类的的大小是否比9大
			for(String key:map.keySet()){
				ArrayList<FuncData> defaultList = map.get(key);
				int totalSize = defaultList.size();
				if(totalSize<=DEFAULT_COUNT){// 小于每页显示个数
					funList.add(defaultList);
				}else{// 大于每页显示个数
					int page = 0;
					if (totalSize % DEFAULT_COUNT == 0) {// 功能数刚好是默认的倍数
						page = totalSize / DEFAULT_COUNT;
						int start = 0;
						for (int i = 1; i <= page; i++) {
							ArrayList<FuncData> subList = subList(defaultList, start,
									start + DEFAULT_COUNT);
							funList.add(subList);
							start += DEFAULT_COUNT;
						}
					}else {
						page = totalSize / DEFAULT_COUNT + 1;// 功能数不是默认的倍数
						int start = 0;
						for (int i = 1; i <= page; i++) {
							if (i == page) {
								ArrayList<FuncData> subList = subList(defaultList,
										start, defaultList.size());
								funList.add(subList);
							} else {
								ArrayList<FuncData> subList = subList(defaultList,
										start, start + DEFAULT_COUNT);
								funList.add(subList);
							}
							start += DEFAULT_COUNT;
						}
					} 
				}
				
			}
			System.out.println("获取的菜单的页数:"+funList.size());
		}
		
		return funList;
	}
	/**
	 * 获取对应的功能的相关信息 (以id为key)
	 * @param key
	 * @return
	 * @throw
	 * @return FuncData
	 */
	public static FuncData findFun(int key){
		FuncData funcData = functionList.get(key);
		if(funcData==null){
			funcData=functionList.get(FuncMap.TELEPHONE_INDEX_FUNC);
		}
		return funcData;
	}
	
	/**
	 * 获取对应的功能的相关信息 (以编号为key)
	 * @param key
	 * @return
	 * @throw
	 * @return FuncData
	 */
	public static FuncData findFun(String key){
		FuncData funcData = functionMap.get(key);
		if(funcData==null){
			funcData=functionList.get(FuncMap.TELEPHONE_INDEX_FUNC);
		}
		return funcData;
	}
	
	/**
	 *获取默认的菜单的id的集合 
	 * @param favourList
	 * @throw
	 * @return void
	 */
	private static void getDefaultKeyList(ArrayList<FuncData> favourList){
		for(FuncData data:favourList){
			favourKeyList.add(data.mnuid);
		}
	}
	
	/**
	 *判断记录中的功能项是否在默认功能列表中 
	 * @param key
	 * @return
	 * @throw
	 * @return boolean
	 */
	private static boolean isDefault(int key){
		for(Integer in:favourKeyList){
			if(in==key){
				favourKeyList.remove(in);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 截取 List
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 * @throw
	 * @return ArrayList<FuncData>
	 */
	public static ArrayList<FuncData> subList(ArrayList<FuncData> list,
			int start, int end) {
		ArrayList<FuncData> subList = new ArrayList<FuncData>();

		for (int i = start; i < end; i++) {
			subList.add(list.get(i));
		}
		return subList;
	}
}
