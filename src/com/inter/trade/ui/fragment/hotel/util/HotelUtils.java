package com.inter.trade.ui.fragment.hotel.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import com.inter.trade.ui.fragment.hotel.data.HotelListData;

import android.util.Log;
/**
 * 酒店列表 辅助类
 * 用于筛选酒店列表数据
 * 
 * @author haifengli
 *
 */
public class HotelUtils {
	/**
	 * 价格从高到低
	 * 
	 * @param hotelList 源数据
	 * @return
	 */
	public static ArrayList<HotelListData> priceHightToLow(ArrayList<HotelListData> hotelList) {
		Collections.reverse(priceLowToHight(hotelList));
		return hotelList;
	}

	/**
	 * 价格从低到高
	 * 
	 * @param hotelList 源数据
	 * @return
	 */
	public static ArrayList<HotelListData> priceLowToHight(ArrayList<HotelListData> hotelList) {
		//		ArrayList<HotelListData> hotelLists = new ArrayList<HotelListData>();

		Collections.sort(hotelList, new Comparator<HotelListData>() {

			private int getHotelPrice(HotelListData data) {
				if (data == null || data.minPrice == null)
					return 0;

				return Integer.parseInt(data.minPrice);
			}

			@Override
			public int compare(HotelListData lhs,
					HotelListData rhs) {
				int d1 = getHotelPrice(lhs);
				int d2 = getHotelPrice(rhs);
				if (d1 == 0 && d2 == 0)
					return 0;
				if (d1 == 0)
					return -1;
				if (d2 == 0)
					return 1;

				if(d1 < d2)
					return -1;
				else if(d1 == d2)
					return 0;
				else
					return 1;
			}
		});

		return hotelList;

	}
	
	/**
	 * 字符串转化成时间类型（字符串可以是任意类型，只要和SimpleDateFormat中的格式一致即可）
	 * @param str
	 * @return
	 */
	public static Date strToDate (String str) {
		Date date=null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(str);
		}
		catch (ParseException e)
		{
			System.out.println(e.getMessage());
		}
		
		return date;
	}
	
	/**
	 * Date转化为Calendar
	 * @param date
	 * @return
	 */
	public static Calendar dateToCalendar (Date date) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(date);
		return (Calendar)cal;
	}
	
	/**
	 * 计算相隔天数的方法
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int getDaysBetween (Calendar d1, Calendar d2) {
        if (d1.after(d2)) { 
            java.util.Calendar swap = d1;
            d1 = d2;
            d2 = swap;
        }
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        if (d1.get(Calendar.YEAR) != y2) {
            d1 = (Calendar) d1.clone();
            do {
                days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                d1.add(Calendar.YEAR, 1);
            } while (d1.get(Calendar.YEAR) != y2);
        }
        return days;
    }
}