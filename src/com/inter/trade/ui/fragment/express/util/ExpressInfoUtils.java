/*
 * @Title:  ExpressInfoUtils.java
 * @Copyright:  XXX Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * @Description:  TODO<请描述此文件是做什么的>
 * @author:  MeiYi
 * @data:  2014年9月16日 上午10:12:48
 * @version:  V1.0
 */
package com.inter.trade.ui.fragment.express.util;

import com.inter.trade.R;

/**
 * 快递工具类
 * @author  chenguangchi
 * @data:  2014年9月16日 上午10:12:48
 * @version:  V1.0
 */
public class ExpressInfoUtils {
	
	public static int[] images=new int[]{R.drawable.icon_tnt,//
						   R.drawable.icon_ems,//Ems
						   R.drawable.icon_fedex,//联邦快递
						   R.drawable.icon_guorui,//国瑞
						   R.drawable.icon_huitong,//百色汇通
						   R.drawable.icon_quanyi,//全一
						   R.drawable.icon_shentong,//申通
						   R.drawable.icon_shunfeng,//顺丰
						   R.drawable.icon_suer,//速尔快递
						   R.drawable.icon_tiantian,//天天
						   R.drawable.icon_ups,//优比速
						   R.drawable.icon_yuantong,//圆通
						   R.drawable.icon_yunda,//韵达
						   R.drawable.icon_zhongtie,//中铁
						   R.drawable.icon_zhongtong,//中通
						   R.drawable.express_unload,//默认
						   R.drawable.icon_debang,//德邦
						   R.drawable.icon_quanfeng,//全峰
						   R.drawable.icon_zaijisong};//宅急送
	
	public static String[] phones=new String[]{"800-820-9868",//tnt
							"11183",//EMS
							"400-889-1888",//联邦快递
							"",//国瑞
							"400-956-5656",//百世汇通
							"400-663-1111",//全一
							"400-889-5543",//申通
							"4008-111-111",//顺丰
							"400-158-9888",//速尔快递
							"400-188-8888",//天天
							"400-820-8388",//优比速
							"021-69777888",//圆通
							"400-821-6789",//韵达
							"",//中铁
							"400-827-0270",//中通
							"",//默认
							"400-830-5555",//德邦
							"400-100-0001",//全峰
							"400-6789-000",//宅急送
							"400-010-6660",//如风达
							"400-626-2356",//安信达
							"400-626-1166",//大田物流
							"400-111-0005",//共速达
							"400-808-6666",//华宇物流
							"400-000-0177",//汇强快递
							"400-820-5566",//佳吉快运
							"400-631-9999",//佳怡物流
							"416-979-8822",//加拿大邮政
							"400-830-4888",//快捷速递
							"021-39283333",//龙邦速递
							"0046-8-23-22-20",//瑞典邮政
							"020-86298999",//全日通
							"4008-000-222",//新邦物流
							"400-820-4400",//新蛋物流
							"00852-2921-2222",//香港邮政
							"400-111-1119",//优速快递
							"11183"//中邮物流
							
							};
	
	
	/**
	 * 根据快递名称 
	 * @param name
	 * @return
	 * @throw
	 * @return int
	 */
	public static int getExpressDrawable(String name){
		if("EMS".equals(name)){
			return images[1];
		}else if("联邦快递".equals(name)){
			return images[2];
		}else if("国瑞".equals(name)){
			return images[3];
		}else if("汇通".equals(name)){
			return images[4];
		}else if("全一快递".equals(name)){
			return images[5];
		}else if("申通".equals(name)){
			return images[6];
		}else if("顺丰".equals(name)){
			return images[7];
		}else if("速尔快递".equals(name)){
			return images[8];
		}else if("天天快递".equals(name)){
			return images[9];
		}else if("UPS快递".equals(name)){
			return images[10];
		}else if("圆通".equals(name)){
			return images[11];
		}else if("韵达".equals(name)){
			return images[12];
		}else if("申通".equals(name)){
			return images[13];
		}else if("中通快递".equals(name)){
			return images[14];
		}else if("TNT快递".equals(name)){
			return images[0];
		}else if("中通".equals(name)){
			return images[14];
		}else if("EMS国内".equals(name)){
			return images[1];
		}else if("EMS国际".equals(name)){
			return images[1];
		}else if("天天".equals(name)){
			return images[9];
		}else if("德邦".equals(name)){
			return images[16];
		}else if("全峰".equals(name)){
			return images[17];
		}else if("宅急送".equals(name)){
			return images[18];
		}else if("德邦物流".equals(name)){
			return images[16];
		}else if("申通快递".equals(name)){
			return images[6];
		}else if("全峰快递".equals(name)){
			return images[17];
		}else if("圆通快递".equals(name)){
			return images[11];
		}
		return images[15];
	}
	
	/**
	 * 根据快递名称 
	 * @param name
	 * @return
	 * @throw
	 * @return int
	 */
	public static String getExpressPhone(String name){
		if("EMS".equals(name)){
			return phones[1];
		}else if("联邦快递".equals(name)){
			return phones[2];
		}else if("国瑞".equals(name)){
			return phones[3];
		}else if("汇通".equals(name)){
			return phones[4];
		}else if("全一快递".equals(name)){
			return phones[5];
		}else if("申通".equals(name)){
			return phones[6];
		}else if("顺丰".equals(name)){
			return phones[7];
		}else if("速尔快递".equals(name)){
			return phones[8];
		}else if("天天快递".equals(name)){
			return phones[9];
		}else if("UPS快递".equals(name)){
			return phones[10];
		}else if("圆通".equals(name)){
			return phones[11];
		}else if("韵达".equals(name)){
			return phones[12];
		}else if("申通".equals(name)){
			return phones[13];
		}else if("中通快递".equals(name)){
			return phones[14];
		}else if("TNT快递".equals(name)){
			return phones[0];
		}else if("中通".equals(name)){
			return phones[14];
		}else if("EMS国内".equals(name)){
			return phones[1];
		}else if("EMS国际".equals(name)){
			return phones[1];
		}else if("天天".equals(name)){
			return phones[9];
		}else if("德邦".equals(name)){
			return phones[16];
		}else if("全峰".equals(name)){
			return phones[17];
		}else if("宅急送".equals(name)){
			return phones[18];
		}else if("德邦物流".equals(name)){
			return phones[16];
		}else if("圆通快递".equals(name)){
			return phones[11];
		}else if("如风达".equals(name)){
			return phones[19];
		}else if("安信达".equals(name)){
			return phones[20];
		}else if("大田物流".equals(name)){
			return phones[21];
		}else if("共速达".equals(name)){
			return phones[22];
		}else if("华宇物流".equals(name)){
			return phones[23];
		}else if("汇强快递".equals(name)){
			return phones[24];
		}else if("佳吉快运".equals(name)){
			return phones[25];
		}else if("佳怡物流".equals(name)){
			return phones[26];
		}else if("加拿大邮政".equals(name)){
			return phones[27];
		}else if("快捷速递".equals(name)){
			return phones[28];
		}else if("龙邦速递".equals(name)){
			return phones[29];
		}else if("瑞典邮政".equals(name)){
			return phones[30];
		}else if("全日通".equals(name)){
			return phones[31];
		}else if("新邦物流".equals(name)){
			return phones[32];
		}else if("新蛋物流".equals(name)){
			return phones[33];
		}else if("香港邮政".equals(name)){
			return phones[34];
		}else if("优速快递".equals(name)){
			return phones[35];
		}else if("中邮物流".equals(name)){
			return phones[36];
		}
		return "";
	}
}
