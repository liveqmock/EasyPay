package com.inter.trade.ui.manager.core;

/**
 * UI 常量定义
 * @author zhichao.huang
 *
 */
public class UIConstantDefault {
	/**
	 * 业务模块范例定义(每个模块对应一个静态类定义，减少混淆参数定义，便于区分)
	 * @author zhichao.huang
	 *
	 */
	public static class BusinessModuleUIConstantDefine {
		
		public static final int UI_CONSTANT_MAIN = 1;
		
		//......
	}
	
	/**
	 * 公共UIConstantDefine(所有模块需要共用的UI定义)
	 * @author zhichao.huang
	 *
	 */
	public static class PublicModuleUIConstantDefine {
		public static final int UI_PUBLIC_CONSTANT_MAIN = 1;
		
		//......
	}
	
	//......
	
	//*******************************飞机票********start*************************
	/**
	 * 机票 
	 */
	public static final int UI_CONSTANT_AIR_TICKET = 1;
	public static final int UI_CONSTANT_AIR_TICKET_OTHER = 2;
	/**
	 * 机票刷卡支付页面
	 */
	public static final int UI_CONSTANT_AIR_TICKET_SWIP_PAY = 3;
	/**
	 * 日期选择
	 */
	public static final int UI_CONSTANT_AIR_TICKET_CALENDAR_PICKER = 4;
	
	/**
	 * 城市选择
	 */
	public static final int UI_CONSTANT_AIR_TICKET_CITY_SELECTE = 5;
	/**
	 * 机票查询(去程)
	 */
	public static final int UI_CONSTANT_AIR_TICKET_QUERY = 6;
	/**
	 * 机票订单
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ORDER_FORM = 7;
	/**
	 * 机票详情
	 */
	public static final int UI_CONSTANT_AIR_TICKET_AIRLINE_DETAIL = 8;
	
	/**
	 * 添加乘机人
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ADD_PASSENGER = 9;
	
	/**
	 * 选择乘机人
	 */
	public static final int UI_CONSTANT_AIR_TICKET_SELECT_PASSENGER = 10;
	
	/**
	 * 选择联系人
	 */
	public static final int UI_CONSTANT_AIR_TICKET_SELECT_CONTACT = 14;
	
	/**
	 * 添加联系人
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ADD_CONTACT = 11;
	
	/**
	 * 机票清单结算
	 */
	public static final int UI_CONSTANT_AIR_TICKET_CLEARINGL = 12;
	/**
	 * 信用卡支付
	 */
	public static final int UI_CONSTANT_AIR_TICKET_CREDITCARD_PAY = 13;
	
	/**
	 * 订单支付成功
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ORDER_PAY_SUCCESS = 15;
	
	/**
	 * 历史订单
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LIST = 16;
	
	/**
	 * 历史订单 item详情
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ORDER_HISTORY_LISTITEM = 17;
	
	/**
	 * 机票订单（往返）
	 */
	public static final int UI_CONSTANT_AIR_TICKET_ORDER_FORM_WANGFAN = 18;
	
	/**
	 * 统一的信用卡支付页面
	 */
	public static final int UI_CONSTANT_CREDITCARD_PAY = 19;
	
	/**
	 * 机票查询(返程)
	 */
	public static final int UI_CONSTANT_AIR_TICKET_QUERY_FANCHENG = 20;
	
	//*******************************飞机票********end*************************
	
	
	//*******************************购买汇通卡********start*************************
	
	/**
	 * 购买汇通卡主页面
	 */
	public static final int UI_CONSTANT_BUY_HTBCARD_MAIN = 25;
	
	/**
	 * 订单支付
	 */
	public static final int UI_CONSTANT_ORDER_PAY = 26;
	
	/**
	 * 购买汇通卡支付成功
	 */
	public static final int UI_CONSTANT_BUY_HTBCARD_ORDER_PAY_SUCCESS = 27;
	
	
	//*******************************购买汇通卡********end*************************
	
	
	//****************************** 酒店预订start ****************************
	/**
	 * 酒店预订 常量初始值：30
	 */
	public static final int UI_CONSTANT_HOTEL_START = 30;
	
	/**
	 * 酒店预订 首页
	 */
	public static final int UI_CONSTANT_HOTEL = UI_CONSTANT_HOTEL_START;
	
	/**
	 *  酒店预订 订单历史
	 */
	public static final int UI_CONSTANT_HOTEL_ORDER_HISTORY_LIST = UI_CONSTANT_HOTEL_START+1;
	
	/**
	 *  酒店预订 价格选择
	 */
	public static final int UI_CONSTANT_HOTEL_PRICE = UI_CONSTANT_HOTEL_START+2;
	
	/**
	 *  酒店预订 星级选择
	 */
	public static final int UI_CONSTANT_HOTEL_STAR_LEVEL = UI_CONSTANT_HOTEL_START+3;
	
	/**
	 *  酒店预订 日期选择
	 */
	public static final int UI_CONSTANT_HOTEL_DATE = UI_CONSTANT_HOTEL_START+4;
	
	/**
	 *  酒店预订 城市选择
	 */
	public static final int UI_CONSTANT_HOTEL_CITY = UI_CONSTANT_HOTEL_START+5;
	
	/**
	 *  酒店预订 关键字选择
	 */
	public static final int UI_CONSTANT_HOTEL_KEYWORD = UI_CONSTANT_HOTEL_START+6;
	
	/**
	 *  酒店预订 酒店列表
	 */
	public static final int UI_CONSTANT_HOTEL_LIST = UI_CONSTANT_HOTEL_START+7;
	
	/**
	 *  酒店预订 酒店详情
	 */
	public static final int UI_CONSTANT_HOTEL_DETAIL = UI_CONSTANT_HOTEL_START+8;
	
	/**
	 *  酒店预订 酒店简介
	 */
	public static final int UI_CONSTANT_HOTEL_DESCRIPTION = UI_CONSTANT_HOTEL_START+9;
	
	/**
	 *  酒店预订 订单填写
	 */
	public static final int UI_CONSTANT_HOTEL_ORDER = UI_CONSTANT_HOTEL_START+10;
	
	/**
	 *  酒店预订 支付页面
	 */
	public static final int UI_CONSTANT_HOTEL_PAY_CONFIRM = UI_CONSTANT_HOTEL_START+11;
	
	
	//****************************** 酒店预订end   ****************************
	
	
	//****************************** 发工资start ****************************
	
	/**
	 * 发工资主页
	 */
	public static final int UI_CONSTANT_SALARYPAY_MAIN = 60;
	
	/**
	 * 清单确认
	 */
	public static final int UI_CONSTANT_SALARYPAY_LISTCONFIRM = UI_CONSTANT_SALARYPAY_MAIN+1;
	
	/**
	 * 查询历史
	 */
	public static final int UI_CONSTANT_SALARYPAY_HISTORY = UI_CONSTANT_SALARYPAY_MAIN+2;
	
	/**
	 * 账单页面
	 */
	public static final int UI_CONSTANT_SALARYPAY_CONFIRM = UI_CONSTANT_SALARYPAY_MAIN+3;
	
	/**
	 * 成功支付页面
	 */
	public static final int UI_CONSTANT_SALARYPAY_SUCCESS = UI_CONSTANT_SALARYPAY_MAIN+4;
	
	/**
	 * 工资模块的主页
	 * 
	 */
	public static final int UI_CONSTANT_SALARY_MAIN = UI_CONSTANT_SALARYPAY_MAIN+5;
	/**
	 * 添加员工的页面
	 */
	public static final int UI_CONSTANT_SALARY_EMPLOYEE_EDIT = UI_CONSTANT_SALARYPAY_MAIN+6;
	
	/**
	 * 信用卡支付页面
	 */
	public static final int UI_CONSTANT_SALARY_CREDITPAY = UI_CONSTANT_SALARYPAY_MAIN+7;
	
	
	//****************************** 发工资end ****************************
	
	
	//****************************** 签收工资start ****************************
	/**
	 * 签收工资主页
	 */
	public static final int UI_CONSTANT_SALARYGET_MAIN = 80;
	
	/**
	 * 签收工资历史
	 */
	public static final int UI_CONSTANT_SALARYGET_HISTORY = UI_CONSTANT_SALARYGET_MAIN+1;
	//****************************** 签收工资end ****************************
	
	//****************************** 转账start ****************************
	/**
	 * 转账初始常量值
	 */
	public static final int UI_CONSTANT_TRANSFER = 100;

	/**
	 * 转账主页
	 */
	public static final int UI_CONSTANT_TRANSFER_MAIN = UI_CONSTANT_TRANSFER+1;
	/**
	 * 转账-收款人信息确认页面
	 */
	public static final int UI_CONSTANT_TRANSFER_SHOUKUAN_CONFIRM = UI_CONSTANT_TRANSFER+2;
	/**
	 * 转账-付款人信息页面
	 */
	public static final int UI_CONSTANT_TRANSFER_FUKUAN_PAY = UI_CONSTANT_TRANSFER+3;
	//****************************** 转账end ****************************
	
}
