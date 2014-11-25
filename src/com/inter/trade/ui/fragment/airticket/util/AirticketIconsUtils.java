package com.inter.trade.ui.fragment.airticket.util;

import com.inter.trade.R;

/**
 * 飞机票 航空公司图标辅助类
 * @author zhichao.huang
 *
 */
public class AirticketIconsUtils {
	
	public static int[] images=new int[]{R.drawable.airline_guoji,//国际航空
						   R.drawable.airline_nanfang,//南方航空
						   R.drawable.airline_dongfang,//东方航空
						   R.drawable.airline_yinglian,//鹰联航空
						   R.drawable.airline_sichuan,//四川航空
						   R.drawable.airline_shenzhen,//深圳航空
						   R.drawable.airline_shanghai,//上海航空
						   R.drawable.airline_default2,//默认
						   
	};
	
	
	/**
	 * 根据航空公司名获取对应的icon id
	 * @param name
	 * @return
	 * @throw
	 * @return int
	 */
	public static int getAirlineDrawable(String name){
		if("中国国航".equals(name)){
			return images[0];
		}else if("南方航空".equals(name)){
			return images[1];
		}else if("东方航空".equals(name)){
			return images[2];
		}else if("鹰联航空".equals(name)){
			return images[3];
		}else if("四川航空".equals(name)){
			return images[4];
		}else if("深圳航空".equals(name)){
			return images[5];
		}else if("上海航空".equals(name)){
			return images[6];
		}
		return images[7];
	}
}
