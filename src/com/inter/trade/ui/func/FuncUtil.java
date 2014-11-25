package com.inter.trade.ui.func;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import android.util.Log;

import com.inter.trade.R;
import com.inter.trade.db.DBHelper;
import com.inter.trade.db.Record;
import com.inter.trade.ui.PayApp;
import com.inter.trade.util.PreferenceConfig;

public class FuncUtil {
	public static final int DEFAULT_COUNT = 9;// 默认每页显示12个功能菜单

	public static HashMap<Integer, FuncData> functionList;

	// 初始化默认功能
	private static ArrayList<FuncData> initFirst() {
		ArrayList<FuncData> list = new ArrayList<FuncData>();

		FuncData cridetData = new FuncData();
		cridetData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.CRIDET_FUNC_KEY, FuncMap.CRIDET_INDEX_FUNC);
		cridetData.imageId = R.drawable.index_card_visa;
		cridetData.name = "信用卡还款";
		list.add(cridetData);

		FuncData transferData = new FuncData();
		transferData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TRANSFER_FUNC_KEY, FuncMap.TRANSFER_INDEX_FUNC);
		transferData.imageId = R.drawable.index_transfer;
		transferData.name = "转账汇款";
		list.add(transferData);

		// FuncData walletData = new FuncData();
		// walletData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.WALLET_FUNC_KEY, FuncMap.WALLET_INDEX_FUNC);
		// walletData.imageId = R.drawable.my_wallet;
		// walletData.name = "我的钱包";
		// list.add(walletData);

		// FuncData returnData = new FuncData();
		// returnData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.RETURN_DAIKUAN_FUNC_KEY,
		// FuncMap.RETURN_DAIKUAN_INDEX_FUNC);
		// returnData.imageId = R.drawable.return_money;
		// returnData.name = "还贷款";
		// list.add(returnData);

		FuncData couponData = new FuncData();
		couponData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.COUPON_FUNC_KEY, FuncMap.COUPON_INDEX_FUNC);
		couponData.imageId = R.drawable.coupon;
		couponData.name = "现金抵用券";
		list.add(couponData);

		// FuncData ordreData = new FuncData();
		// ordreData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.ORDER_FUNC_KEY, FuncMap.ORDER_INDEX_FUNC);
		// ordreData.imageId = R.drawable.order_pay;
		// ordreData.name = "订单号付款";
		// list.add(ordreData);

		// FuncData ordreQureyData = new FuncData();
		// ordreQureyData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.ORDER_QUERY_FUNC_KEY,
		// FuncMap.ORDER_QUERY_INDEX_FUNC);
		// ordreQureyData.imageId = R.drawable.order_query;
		// ordreQureyData.name = "订单查询";
		// list.add(ordreQureyData);

		FuncData expressData = new FuncData();
		expressData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.EXPRESS_FUNC_KEY, FuncMap.EXPRESS_INDEX_FUNC);
		expressData.imageId = R.drawable.expressage;
		expressData.name = "快递查询";
		list.add(expressData);

		FuncData balanceData = new FuncData();
		balanceData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BALANCE_FUNC_KEY, FuncMap.BALANCE_INDEX_FUNC);
		balanceData.imageId = R.drawable.balance_enquiry;
		balanceData.name = "余额查询";
		list.add(balanceData);

		// 新增话费充值
		FuncData telephoneData = new FuncData();
		telephoneData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TELEPHONE_FUNC_KEY, FuncMap.TELEPHONE_INDEX_FUNC);
		telephoneData.imageId = R.drawable.moblie_rechange;
		telephoneData.name = "话费充值";
		list.add(telephoneData);

		// 新增Q币充值
		FuncData qMoneyData = new FuncData();
		qMoneyData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.QMONEY_FUNC_KEY, FuncMap.QMONEY_INDEX_FUNC);
		qMoneyData.imageId = R.drawable.qqcoincharge;
		qMoneyData.name = "Q币充值";
		list.add(qMoneyData);

		// 新增代理商
		FuncData agentData = new FuncData();
		agentData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AGENT_FUNC_KEY, FuncMap.AGENT_INDEX_FUNC);
		agentData.imageId = R.drawable.dailishang;
		agentData.name = "代理商";
		list.add(agentData);

		// //新增固话充值
		// FuncData fixPhoneData = new FuncData();
		// fixPhoneData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.FIXPHONE_FUNC_KEY, FuncMap.FIXPHONE_INDEX_FUNC);
		// fixPhoneData.imageId = R.drawable.balance_enquiry;
		// fixPhoneData.name = "宽带缴费";
		// list.add(fixPhoneData);

		// //新增电费充值
		// FuncData electricChargeData = new FuncData();
		// electricChargeData.mnuid=PreferenceConfig.instance(PayApp.pay)
		// .getInt(FuncMap.ELECTRICCHARGE_FUNC_KEY,
		// FuncMap.ELECTRICCHARGE_INDEX_FUNC);
		// electricChargeData.imageId = R.drawable.balance_enquiry;
		// electricChargeData.name = "缴电费";
		// list.add(electricChargeData);

		// 购买刷卡器
		FuncData buySwipCardData = new FuncData();
		buySwipCardData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BUY_SWIPCARD_FUNC_KEY, FuncMap.BUY_SWIPCARD_INDEX_FUNC);
		buySwipCardData.imageId = R.drawable.buycard_icon;
		buySwipCardData.name = "购买刷卡器";
		list.add(buySwipCardData);

		// 水电煤缴费
		FuncData waterElectricGasData = new FuncData();
		waterElectricGasData.mnuid = PreferenceConfig.instance(PayApp.pay)
				.getInt(FuncMap.WATER_ELECTRIC_GAS_FUNC_KEY,
						FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC);
		waterElectricGasData.imageId = R.drawable.water_fee;
		waterElectricGasData.name = "水电煤缴费";
		list.add(waterElectricGasData);

		// 游戏充值

		FuncData gameRechargeData = new FuncData();
		gameRechargeData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.GAME_RECHARGE_FUNC_KEY,
				FuncMap.GAME_RECHARGE_INDEX_FUNC);
		gameRechargeData.imageId = R.drawable.default_index;
		gameRechargeData.name = "游戏充值";
		list.add(gameRechargeData);

		// 机票
		FuncData airTicketData = new FuncData();
		airTicketData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AIR_TICKET_FUNC_KEY, FuncMap.AIR_TICKET_INDEX_FUNC);
		airTicketData.imageId = R.drawable.default_index;
		airTicketData.name = "机票";
		list.add(airTicketData);

		return list;
	}

	/**
	 * 初始化所有的功能模块图标
	 * 
	 * @param data
	 * @return
	 * @throw
	 * @return ArrayList<FuncData>
	 */
	private static void initFunctionMap() {
		functionList = new HashMap<Integer, FuncData>();

		FuncData cridetData = new FuncData();
		cridetData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.CRIDET_FUNC_KEY, FuncMap.CRIDET_INDEX_FUNC);
		cridetData.imageId = R.drawable.selector_icon_visa;
		cridetData.name = "信用卡还款";
		functionList.put(FuncMap.CRIDET_INDEX_FUNC, cridetData);

		FuncData transferData = new FuncData();
		transferData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TRANSFER_FUNC_KEY, FuncMap.TRANSFER_INDEX_FUNC);
		transferData.imageId = R.drawable.selector_icon_transfer;
		transferData.name = "转账汇款";
		functionList.put(FuncMap.TRANSFER_INDEX_FUNC, transferData);

		FuncData couponData = new FuncData();
		couponData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.COUPON_FUNC_KEY, FuncMap.COUPON_INDEX_FUNC);
		couponData.imageId = R.drawable.selector_icon_coupon;
		couponData.name = "现金抵用券";
		functionList.put(FuncMap.COUPON_INDEX_FUNC, couponData);

		FuncData expressData = new FuncData();
		expressData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.EXPRESS_FUNC_KEY, FuncMap.EXPRESS_INDEX_FUNC);
		expressData.imageId = R.drawable.selector_icon_expressage;
		expressData.name = "快递查询";
		functionList.put(FuncMap.EXPRESS_INDEX_FUNC, expressData);

		FuncData balanceData = new FuncData();
		balanceData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BALANCE_FUNC_KEY, FuncMap.BALANCE_INDEX_FUNC);
		balanceData.imageId = R.drawable.selector_icon_balance;
		balanceData.name = "余额查询";
		functionList.put(FuncMap.BALANCE_INDEX_FUNC, balanceData);

		// 新增话费充值
		FuncData telephoneData = new FuncData();
		telephoneData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TELEPHONE_FUNC_KEY, FuncMap.TELEPHONE_INDEX_FUNC);
		telephoneData.imageId = R.drawable.selector_icon_mobilecharge;
		telephoneData.name = "话费充值";
		functionList.put(FuncMap.TELEPHONE_INDEX_FUNC, telephoneData);

		// 新增Q币充值
		FuncData qMoneyData = new FuncData();
		qMoneyData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.QMONEY_FUNC_KEY, FuncMap.QMONEY_INDEX_FUNC);
		qMoneyData.imageId = R.drawable.selector_icon_qq;
		qMoneyData.name = "Q币充值";
		functionList.put(FuncMap.QMONEY_INDEX_FUNC, qMoneyData);

		// 新增代理商
		FuncData agentData = new FuncData();
		agentData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AGENT_FUNC_KEY, FuncMap.AGENT_INDEX_FUNC);
		agentData.imageId = R.drawable.selector_icon_agent;
		agentData.name = "代理商";
		functionList.put(FuncMap.AGENT_INDEX_FUNC, agentData);

		// 购买刷卡器
		FuncData buySwipCardData = new FuncData();
		buySwipCardData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.BUY_SWIPCARD_FUNC_KEY, FuncMap.BUY_SWIPCARD_INDEX_FUNC);
		buySwipCardData.imageId = R.drawable.selector_icon_buycard;
		buySwipCardData.name = "购买刷卡器";
		functionList.put(FuncMap.BUY_SWIPCARD_INDEX_FUNC, buySwipCardData);

		// 水电煤缴费
		FuncData waterElectricGasData = new FuncData();
		waterElectricGasData.mnuid = PreferenceConfig.instance(PayApp.pay)
				.getInt(FuncMap.WATER_ELECTRIC_GAS_FUNC_KEY,
						FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC);
		waterElectricGasData.imageId = R.drawable.selector_icon_waterfee;
		waterElectricGasData.name = "水电煤缴费";
		functionList.put(FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC,
				waterElectricGasData);

		// 游戏充值
		FuncData gameRechargeData = new FuncData();
		gameRechargeData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.GAME_RECHARGE_FUNC_KEY,
				FuncMap.GAME_RECHARGE_INDEX_FUNC);
		gameRechargeData.imageId = R.drawable.selector_icon_game;
		gameRechargeData.name = "游戏充值";
		functionList.put(FuncMap.GAME_RECHARGE_INDEX_FUNC, gameRechargeData);

		// 机票
		FuncData airTicketData = new FuncData();
		airTicketData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.AIR_TICKET_FUNC_KEY, FuncMap.AIR_TICKET_INDEX_FUNC);
		airTicketData.imageId = R.drawable.selector_icon_plane;
		airTicketData.name = "机票预订";
		functionList.put(FuncMap.AIR_TICKET_INDEX_FUNC, airTicketData);
		
		//火车票
		FuncData trainTicketData = new FuncData();
		trainTicketData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.TRAIN_TICKET_FUNC_KEY, FuncMap.TRAIN_TICKET_INDEX_FUNC);
		trainTicketData.imageId = R.drawable.selector_icon_train;
		trainTicketData.name = "火车票预订";
		functionList.put(FuncMap.TRAIN_TICKET_INDEX_FUNC, trainTicketData);
		
		//酒店
		FuncData hotelData = new FuncData();
		hotelData.mnuid = PreferenceConfig.instance(PayApp.pay).getInt(
				FuncMap.HOTEL_FUNC_KEY, FuncMap.HOTEL_INDEX_FUNC);
		hotelData.imageId = R.drawable.selector_icon_hotel;
		hotelData.name = "酒店预订";
		functionList.put(FuncMap.HOTEL_INDEX_FUNC, hotelData);
		
	}

	public static boolean setIndentity(int id, int identity,
			ArrayList<FuncData> datas) {
		// if(identity != -1){
		// return true;
		// }
		switch (id) {
		case FuncMap.CRIDET_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.CRIDET_FUNC_KEY, identity);
			datas.get(0).identify = identity;
			break;
		case FuncMap.TRANSFER_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.TRANSFER_FUNC_KEY, identity);
			datas.get(1).identify = identity;
			break;
		case FuncMap.WALLET_INDEX_FUNC:
			// PreferenceConfig.instance(PayApp.pay)
			// .getInt(FuncMap.WALLET_FUNC_KEY, identity);
			// datas.get(2).identify=identity;
			break;
		case FuncMap.RETURN_DAIKUAN_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.RETURN_DAIKUAN_FUNC_KEY, identity);
			datas.get(3).identify = identity;
			break;
		case FuncMap.COUPON_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.COUPON_FUNC_KEY, identity);
			datas.get(4).identify = identity;
			break;
		case FuncMap.BALANCE_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.WALLET_FUNC_KEY, identity);
			datas.get(8).identify = identity;
			break;
		case FuncMap.TELEPHONE_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.TELEPHONE_FUNC_KEY, identity);
			datas.get(9).identify = identity;
			break;
		case FuncMap.QMONEY_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.QMONEY_FUNC_KEY, identity);
			datas.get(10).identify = identity;
			break;
		case FuncMap.FIXPHONE_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.FIXPHONE_FUNC_KEY, identity);
			datas.get(11).identify = identity;
			break;
		case FuncMap.ELECTRICCHARGE_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.ELECTRICCHARGE_FUNC_KEY, identity);
			datas.get(12).identify = identity;
			break;
		case FuncMap.BUY_SWIPCARD_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.BUY_SWIPCARD_FUNC_KEY, identity);
			datas.get(13).identify = identity;
			break;
		case FuncMap.AGENT_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.AGENT_FUNC_KEY, identity);
			datas.get(14).identify = identity;
			break;
		case FuncMap.ORDER_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.ORDER_FUNC_KEY, identity);
			datas.get(5).identify = identity;
			break;
		case FuncMap.ORDER_QUERY_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.ORDER_QUERY_FUNC_KEY, identity);
			datas.get(6).identify = identity;
			break;
		case FuncMap.EXPRESS_INDEX_FUNC:
			PreferenceConfig.instance(PayApp.pay).getInt(
					FuncMap.EXPRESS_FUNC_KEY, identity);
			datas.get(7).identify = identity;
			break;
		default:
			break;
		}
		/**
		 * if(id>0&&id<10){ return true; }
		 */
		if (id > 0 && id < 16) {
			return true;
		} else {
			if (identity != -1) {
				return true;
			} else {
				return false;
			}
		}
	}

	/**
	 * 根据返回结果，获取功能列表每一页数据集合
	 * 
	 * @param extendDatas
	 * @return
	 */
	public static ArrayList<ArrayList<FuncData>> getFunctions() {

		ArrayList<ArrayList<FuncData>> list = new ArrayList<ArrayList<FuncData>>();

		ArrayList<FuncData> defaultList = initFirst();

		int totalSize = defaultList.size();

		if (totalSize <= DEFAULT_COUNT) {// 小于每页显示个数
			list.add(defaultList);
		} else {// 大于每页显示个数
			int page = 0;
			if (totalSize % DEFAULT_COUNT == 0) {// 功能数刚好是默认的倍数
				page = totalSize / DEFAULT_COUNT;
				int start = 0;
				for (int i = 1; i <= page; i++) {
					ArrayList<FuncData> subList = subList(defaultList, start,
							start + DEFAULT_COUNT);
					list.add(subList);
					start += DEFAULT_COUNT;
				}
			} else {
				page = totalSize / DEFAULT_COUNT + 1;// 功能数不是默认的倍数
				int start = 0;
				for (int i = 1; i <= page; i++) {
					if (i == page) {
						ArrayList<FuncData> subList = subList(defaultList,
								start, defaultList.size());
						list.add(subList);
					} else {
						ArrayList<FuncData> subList = subList(defaultList,
								start, start + DEFAULT_COUNT);
						list.add(subList);
					}
					start += DEFAULT_COUNT;
				}
			}
		}
		System.out.println("获取的页数" + list.size());
		return list;
	}

	/**
	 * 根据分类获取各个分类的功能模块
	 * 
	 * @param data
	 * @return
	 * @throw
	 * @return ArrayList<ArrayList<FuncData>>
	 */
	public static ArrayList<ArrayList<FuncData>> getFunctions(Context context) {

		initFunctionMap();

		ArrayList<ArrayList<FuncData>> list = new ArrayList<ArrayList<FuncData>>();
		ArrayList<FuncData> favourList = initDefaultFavour();
		
		
		/*DBHelper helper = DBHelper.getInstance(context);
		if(helper.getCount()>0){//用户开始记录使用习惯
			ArrayList<Record> recordList = helper.queryAll();
			helper.closeDB();//关闭数据库
			if(recordList!=null){
				if(recordList.size()==9){//用户使用的功能数超过9个
					favourList.clear();
					for(Record record:recordList){
						favourList.add(functionList.get(record.getKey()));
					}
				}else{//用户使用的功能数少于9个
					
					for(Record record:recordList){//判断点击的按钮是否在默认的首页当中,存在就把favourList当中对应的项删除
						if(isDefault(record)){
							favourList=remove(favourList, record);
						}
					}
					if(favourList.size()+recordList.size()>9){//当前两者的和大于9，删除默认首页的最后几项知道两者之和为9
						int fsize =favourList.size();
						int rsize =recordList.size();
						System.out.println("当前默认功能"+fsize+"当前记录"+rsize);
						int deletesize=fsize+rsize-9;
						int add = fsize-deletesize;
						for(int i=fsize;i>add;i--){
							favourList.remove(fsize-1);
							fsize-=1;
						}
					}
					for(Record record:recordList){//将用户点击的功能项添加到默认的首页中
						favourList.add(functionList.get(record.getKey()));
					}
				}
			}
		}
		*/

		ArrayList<FuncData> shopList = new ArrayList<FuncData>();// 商城分类

		ArrayList<FuncData> airplaneList = new ArrayList<FuncData>();// 出行旅游模块
		
		airplaneList.add(functionList.get(FuncMap.AIR_TICKET_INDEX_FUNC));

		ArrayList<FuncData> convinenceList = new ArrayList<FuncData>();// 便民服务
		convinenceList.add(functionList.get(FuncMap.TELEPHONE_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.QMONEY_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.GAME_RECHARGE_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.AIR_TICKET_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.TRAIN_TICKET_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.HOTEL_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.SMSRECEIPT_INDEX_FUNC));
		convinenceList.add(functionList
				.get(FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC));
		convinenceList.add(functionList.get(FuncMap.EXPRESS_INDEX_FUNC));

		ArrayList<FuncData> moneyList = new ArrayList<FuncData>();// 理财分类
		moneyList.add(functionList.get(FuncMap.CRIDET_INDEX_FUNC));
		moneyList.add(functionList.get(FuncMap.TRANSFER_INDEX_FUNC));
		moneyList.add(functionList.get(FuncMap.COUPON_INDEX_FUNC));
		moneyList.add(functionList.get(FuncMap.BALANCE_INDEX_FUNC));
		moneyList.add(functionList.get(FuncMap.BUY_SWIPCARD_INDEX_FUNC));
		moneyList.add(functionList.get(FuncMap.AGENT_INDEX_FUNC));

		list.add(favourList);
		//list.add(shopList);
		list.add(moneyList);
		//list.add(airplaneList);
		list.add(convinenceList);

		return list;
	}
	
	
	/**
	 * 删除记录对应的默认首页的功能项
	 * @param record
	 * @throw
	 * @return void
	 */
	private static ArrayList<FuncData> remove(ArrayList<FuncData> favourList,Record record){
		int deleteNo=0;
		for(int i=0;i<favourList.size();i++){
			FuncData data = favourList.get(i);
			if(data.mnuid==record.getKey()){
				deleteNo=i;
			}
		}
		favourList.remove(deleteNo);
		return favourList;
	}
	
	/**
	 * 判断该功能是否在首页当中 
	 * @param record
	 * @return
	 * @throw
	 * @return boolean
	 */
	private static boolean isDefault(Record record){
		Integer key = record.getKey();
		if(key==FuncMap.CRIDET_INDEX_FUNC || key==FuncMap.TRANSFER_INDEX_FUNC || key==FuncMap.COUPON_INDEX_FUNC
				|| key==FuncMap.BALANCE_INDEX_FUNC || key==FuncMap.EXPRESS_INDEX_FUNC || key==FuncMap.TELEPHONE_INDEX_FUNC
				|| key==FuncMap.QMONEY_INDEX_FUNC || key==FuncMap.BUY_SWIPCARD_INDEX_FUNC || key==FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 初始化默认的首页功能列表
	 * @throw
	 * @return void
	 */
	private static ArrayList<FuncData> initDefaultFavour() {
		ArrayList<FuncData> favourList = new ArrayList<FuncData>();// 添加最常用的模块
		favourList.add(functionList.get(FuncMap.CRIDET_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.TRANSFER_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.COUPON_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.BALANCE_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.EXPRESS_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.TELEPHONE_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.QMONEY_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.BUY_SWIPCARD_INDEX_FUNC));
		favourList.add(functionList.get(FuncMap.WATER_ELECTRIC_GAS_INDEX_FUNC));
		return favourList;
	}

	/**
	 * 根据返回结果，获取功能列表每一页数据集合
	 * 
	 * @param extendDatas
	 * @return
	 */
	public static ArrayList<ArrayList<FuncData>> getFunctions(
			ArrayList<ExtendData> extendDatas) {

		ArrayList<ArrayList<FuncData>> list = new ArrayList<ArrayList<FuncData>>();

		list.add(initFirst());

		return list;

		// Logger.d("parser", "getFunctions");
		// ArrayList<ArrayList<FuncData>> list = new
		// ArrayList<ArrayList<FuncData>>();
		//
		//
		// ArrayList<FuncData> defaultDatas = initFirst();
		// int totalCount = extendDatas.size()+defaultDatas.size();
		// int temp =0;
		// if(extendDatas != null && extendDatas.size()>=0){
		// //默认功能总数少于每页默认显示个数
		// if(defaultDatas.size()<=DEFAULT_COUNT){
		// int len = DEFAULT_COUNT-defaultDatas.size();
		// if(extendDatas.size() >0){
		// for(int i =0;i<len;i++)
		// {
		// ExtendData data = extendDatas.get(i);
		// FuncData funcData = new FuncData();
		//
		// try {
		// if(data.mnuurl!=null || !"".equals(data.mnuurl)){
		// funcData.identify=Integer.parseInt(data.mnuurl);
		// }else{
		// funcData.identify=0;
		// }
		//
		// } catch (Exception e) {
		// // TODO: handle exception
		// funcData.identify=-1;
		// }
		// if(data.mnuid==null){
		// Logger.d("parser", "dataname"+data.mnuname);
		// }
		// if(data.mnuid!=null){
		// int id = Integer.parseInt(data.mnuid);
		// if(!setIndentity(id, funcData.identify,defaultDatas)){
		//
		// funcData.url=data.mnupic;
		// funcData.name = data.mnuname;
		// funcData.mnuid= id;
		//
		// defaultDatas.add(funcData);
		//
		// }
		// }
		//
		// }
		// }
		//
		//
		// list.add(defaultDatas);
		//
		//
		// temp=DEFAULT_COUNT;
		// int start = len;
		// while(temp < totalCount){
		// ArrayList<FuncData> funcDatas = getFuncDatas(start,
		// extendDatas,defaultDatas);
		// if(funcDatas==null||funcDatas.size()==0){
		// break;
		// }
		// list.add(funcDatas);
		// temp+=funcDatas.size();
		// start += funcDatas.size();
		// }
		// }else{
		//
		// //默认功能总数大于每页默认显示个数
		// int len = defaultDatas.size();
		// for(int i =0;i<len;i++)
		// {
		// ExtendData data = extendDatas.get(i);
		// FuncData funcData = new FuncData();
		//
		// try {
		// funcData.identify=Integer.parseInt(data.mnuurl);
		// } catch (Exception e) {
		// // TODO: handle exception
		// funcData.identify=-1;
		// }
		//
		// funcData.url=data.mnupic;
		// funcData.name = data.mnuname;
		// defaultDatas.add(funcData);
		// }
		//
		//
		// temp=0;
		// int start = 0;
		// while(temp < totalCount){
		// ArrayList<FuncData> funcDatas = getAllDatas(start, defaultDatas);
		// if(funcDatas==null){
		// break;
		// }
		// list.add(funcDatas);
		// temp+=funcDatas.size();
		// start += funcDatas.size();
		// }
		//
		//
		// }
		//
		// }
		//
		// return list;
	}

	/**
	 * 默认功能总数少于每页默认显示个数
	 * 
	 * @param start
	 * @param extendDatas
	 * @return
	 */
	private static ArrayList<FuncData> getFuncDatas(int start,
			ArrayList<ExtendData> extendDatas, ArrayList<FuncData> tempDatas) {
		if (extendDatas.size() <= start) {
			return null;
		}
		ArrayList<FuncData> funcDatas = new ArrayList<FuncData>();
		int len = start + DEFAULT_COUNT;
		for (int index = start; index < len; index++) {
			if (index >= extendDatas.size()) {
				break;
			}
			ExtendData data = extendDatas.get(index);
			FuncData funcData = new FuncData();
			try {
				if (data.mnuurl != null || !"".equals(data.mnuurl)) {
					funcData.identify = Integer.parseInt(data.mnuurl);
				} else {
					funcData.identify = 0;
				}

			} catch (Exception e) {
				// TODO: handle exception
				funcData.identify = -1;
			}
			// try {
			// funcData.identify=Integer.parseInt(data.mnuurl);
			// } catch (Exception e) {
			// // TODO: handle exception
			// funcData.identify=-1;
			// }
			if (data.mnuid != null) {
				int id = Integer.parseInt(data.mnuid);
				if (!setIndentity(id, funcData.identify, tempDatas)) {
					funcData.url = data.mnupic;
					funcData.name = data.mnuname;
					funcDatas.add(funcData);
				}
			}

		}
		return funcDatas;
	}

	/**
	 * //默认功能总数大于每页默认显示个数
	 * 
	 * @param start
	 * @param extendDatas
	 * @return
	 */
	private static ArrayList<FuncData> getAllDatas(int start,
			ArrayList<FuncData> extendDatas) {
		if (extendDatas.size() <= start) {
			return null;
		}
		ArrayList<FuncData> funcDatas = new ArrayList<FuncData>();
		for (int index = start; index < extendDatas.size(); index++) {
			if (index >= extendDatas.size()) {
				break;
			}
			FuncData data = extendDatas.get(index);
			funcDatas.add(data);
		}
		return funcDatas;
	}

	public static ArrayList<FuncData> subList(ArrayList<FuncData> list,
			int start, int end) {
		ArrayList<FuncData> subList = new ArrayList<FuncData>();

		for (int i = start; i < end; i++) {
			subList.add(list.get(i));
		}
		return subList;
	}
}
