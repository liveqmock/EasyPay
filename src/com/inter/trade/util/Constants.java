package com.inter.trade.util;


public class Constants {
	public static boolean DEBUG = false;
	public static int LOG_LEVEL = android.util.Log.DEBUG;
	public static String VERSION_KEY="VERSION_KEY";//版本号key
	public static String VERSION_MSUT_UPDATE="VERSION_MSUT_UPDATE";//必须强制更新
	public static String VERSION_UPDATE_URL="VERSION_UPDATE_URL";//强制更新网址
	public static String VERSION_UPDATE_MESSAGE="VERSION_UPDATE_MESSAGE";//版本更新信息
	
	public static String USER_ZHANGHAO="USER_ZHANGHAO";
	
	public static String FUNC_ITEM_KEY="FUNC_ITEM_KEY";
	
	public static final int DISPLAY_COUNT=3;
	public static final int RECORD_DISPLAY_COUNT =15;
	
	public static final int ACTIVITY_FINISH =100;//结束activity
	public static final int ACTIVITY_NO_FINISH =101;//不结束activity
	public static final int ACTIVITY_FINISH_TO_MENU =102;//结束所有子activity,回到主菜单
	
	public static final String OBJECTFILE="object";
	
	
	/**
	 * 清除本地信息
	 */
	public static String CLEAN_FLAG = "cleanFlag";
	
	/**
	 * 用户名
	 */
	public static String USER_NAME = "user_name";
	
	/**
	 * 用户密码
	 */
	public static String USER_PASSWORD = "user_password";

	/**
	 * 手势键
	 */
	public static String LOCK_KEY= "lock_key";
	
	/**
	 * 手势键值
	 */
	public static String LOCK_KEY_VALUE = "x58abfghfghgf";
	
	/**
	 * 用户手势密码
	 */
	public static String USER_GESTURE_PWD = "user_gesture_pwd";
	
	/**
	 * 用户authorid
	 */
	public static String USER_AUTHORID = "user_authorid";
	
	/**
	 * 是否跳到手机充值页面
	 */
	public static String SHOW_MOBLIE_RECHANGE = "show_moblie_rechange";
	
	/**
	 * 判断是否设置过密保，true：已设；false：未设
	 */
	public static String IS_SET_PWDSAFETY = "is_set_pwdsafety";
	
	/**
	 * 判断是否绑定过代理商，true：已设；false：未设
	 */
	public static String IS_BIND_AGENT = "is_bind_agent";
	
	/**
	 * 判断是否显示过公告，true：已显示过；false：未
	 */
	public static String IS_SHOWED_NOTICE = "is_showed_notice";
	
	/**
	 * 保存代理商id，大于0：代理商；小于等于0：非代理商
	 */
	public static String AGENT_ID = "agent_id";
	
	/**
	 * 保存代理商类型id，0普通用户，1正式代理商，2虚拟代理商
	 */
	public static String AGENT_TYPE_ID = "agent_type_id";
	
	/**
	 * 保存请求网络的动态码 au_token
	 */
	public static String AUTO_TOKEN = "auto_token";
	/**
	 * 配置文件中的代理商号
	 */
	public static String AGENT_NO_TO_BIND="agent_no_to_bind";
	
	/**
	 * 是否绑定了财务人员
	 */
	public static String FINACIAL_BIND="finacial_bind";
	
	/**
	 * 上一次登陆的账号
	 */
	public static String LAST_LOGIN_USERNAME="last_login_username";
	
	/**
	 * 授权码
	 */
	public static String REQ_TOKEN="req_token";
	
	
	/**
	 * 记录是否第一次登陆
	 */
	public static String IS_FIRST_USE_IN_PHONE="is_first_use_in_phone";
}
