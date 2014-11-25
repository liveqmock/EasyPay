package com.inter.trade.ui.fragment.airticket.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.util.Log;

/**
 * 航空公司帮助类
 * 用于获取，遍历航班数据
 * 
 * @author zhichao.huang
 *
 */
public class AirLineUtils {

	/**
	 * 过滤相同的航空公司
	 * @param airs
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> toAirLine ( ArrayList<ApiAirticketGetAirlineData> airs ) {
		ArrayList<ApiAirticketGetAirlineData> airnames = new ArrayList<ApiAirticketGetAirlineData>();
		ArrayList<String> tempAirNames = new ArrayList<String>();

		for (ApiAirticketGetAirlineData airlineData : airs) {
			if(tempAirNames.size() == 0) {
				tempAirNames.add(airlineData.airLineName);
				airnames.add(airlineData);

			}else{
				if(!tempAirNames.contains(airlineData.airLineName)) {
					tempAirNames.add(airlineData.airLineName);
					airnames.add(airlineData);
				}
			}
		}
		return airnames;
	}

	/**
	 * 根据航空公司名去查找相同航空公司的航班
	 * 
	 * @param airs 源数据
	 * @param airName 航空公司名
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> toAirLine (ArrayList<ApiAirticketGetAirlineData> airs, String airName ) {
		ArrayList<ApiAirticketGetAirlineData> airnames = new ArrayList<ApiAirticketGetAirlineData>();

		for (ApiAirticketGetAirlineData airlineData : airs) {

			if(airlineData.airLineName.equals(airName)) {
				airnames.add(airlineData);
			}

		}
		return airnames;
	}

	/**
	 * 根据出发时间去查找航空公司的航班（早中晚）
	 * 
	 * @param airs 源数据
	 * @param timeMark 0:早上；1：下午；2：晚上
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> toStartTimeAirLine (ArrayList<ApiAirticketGetAirlineData> airs, int timeMark) {

		ArrayList<ApiAirticketGetAirlineData> airnames = new ArrayList<ApiAirticketGetAirlineData>();

		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		int hour;//小时

		for (ApiAirticketGetAirlineData data : airs) {

			try {
				date = sdf.parse(data.takeOffTime.substring(11));

				hour = date.getHours();

				if(timeMark == 0) {
					if (hour >= 6 && hour < 12) {//早上
						airnames.add(data);
					}

				} else if(timeMark == 1) {
					if (hour >= 12 && hour < 18) {//下午

						airnames.add(data);
					}
				} else {
					if (hour >= 6 && hour < 12) {//早上

					}  else if (hour >= 12 && hour < 18) {//下午

					}  else {//晚上
						airnames.add(data);
					}
				}

			} catch (ParseException e) {

				e.printStackTrace();
			}



		}
		return airnames;
	}

	/**
	 * 价格从高到低
	 * 
	 * @param airs 源数据
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> priceHightToLow(ArrayList<ApiAirticketGetAirlineData> airs) {
		Collections.reverse(priceLowToHight(airs));
		return airs;
	}

	/**
	 * 价格从低到高
	 * 
	 * @param airs 源数据
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> priceLowToHight(ArrayList<ApiAirticketGetAirlineData> airs) {
		//		ArrayList<ApiAirticketGetAirlineData> airnames = new ArrayList<ApiAirticketGetAirlineData>();

		Collections.sort(airs, new Comparator<ApiAirticketGetAirlineData>() {

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			private int getAirticketPrice(ApiAirticketGetAirlineData data) {
				if (data == null || data.price == null)
					return 0;

				return Integer.parseInt(data.price);
			}

			@Override
			public int compare(ApiAirticketGetAirlineData lhs,
					ApiAirticketGetAirlineData rhs) {
				int d1 = getAirticketPrice(lhs);
				int d2 = getAirticketPrice(rhs);
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

		return airs;

	}

	/**
	 * 时间从早到晚
	 * 
	 * @param airs 源数据
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> timeHightToLow(ArrayList<ApiAirticketGetAirlineData> airs) {
		//		ArrayList<ApiAirticketGetAirlineData> airnames = new ArrayList<ApiAirticketGetAirlineData>();

		Collections.sort(airs, new Comparator<ApiAirticketGetAirlineData>() {

			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

			private Date getAirticketStartDate(ApiAirticketGetAirlineData data) {
				if (data == null || data.takeOffTime == null)
					return null;
				try {
					return sdf.parse(data.takeOffTime.substring(11));
				}
				catch (ParseException e) {
					return null;
				}
			}

			@Override
			public int compare(ApiAirticketGetAirlineData lhs,
					ApiAirticketGetAirlineData rhs) {
				Date d1 = getAirticketStartDate(lhs);
				Date d2 = getAirticketStartDate(rhs);
				if (d1 == null && d2 == null)
					return 0;
				if (d1 == null)
					return -1;
				if (d2 == null)
					return 1;
				return d1.compareTo(d2);
			}
		});

		return airs;
	}

	/**
	 * 时间从晚到早
	 * 
	 * @param airs 源数据
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> timeLowToHight(ArrayList<ApiAirticketGetAirlineData> airs) {

		Collections.reverse(timeHightToLow(airs));

		return airs;
	}

	/**
	 * 去程/返程,获取往返数据
	 * 
	 * @param airs
//	 * @param searchType 目前只有两种：S表示单程；D表示返程
	 * @param dCityCode 出发城市代码,(用于判断去程和回程的字段，如该字段和用户选择的出发城市是一致的说明是去程，反之是回程)
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> getWangfanAirDatas (ArrayList<ApiAirticketGetAirlineData> airs, /*String searchType,*/ String dCityCode) {
		ArrayList<ApiAirticketGetAirlineData> quchengDatas = new ArrayList<ApiAirticketGetAirlineData>();//去程
		ArrayList<ApiAirticketGetAirlineData> huichengDatas = new ArrayList<ApiAirticketGetAirlineData>();//回程

		ArrayList<ApiAirticketGetAirlineData> totalDatas = new ArrayList<ApiAirticketGetAirlineData>();//综合


		for (ApiAirticketGetAirlineData airlineData : airs) {

			Log.i("air__1", airlineData.dCityCode);
			if(dCityCode.equals(airlineData.dCityCode)) {
				quchengDatas.add(airlineData);
			} else {
				huichengDatas.add(airlineData);
			}

		}

//		for(int i = 0; i < quchengDatas.size(); i ++) {
//
//			ApiAirticketGetAirlineData quData = quchengDatas.get(i);
//			if (huichengDatas.size() > 0) {
//				if (i < huichengDatas.size()) {
//					quData.airticketFanchengAirlineData = huichengDatas.get(i);
//				} else {
//					quData.airticketFanchengAirlineData = huichengDatas.get(0);//当去程的飞机数量 大于 回程的飞机数量，默认把回程的第一个值赋值上去
//				}
//			}
//
//			totalDatas.add(quData);
//		}
		
		Log.i("air__", "quchengDatas:"+quchengDatas.size() + ";huichengDatas:"+huichengDatas.size());
		
		
		if(quchengDatas.size() > 0 ) {
			//说明当前用户选择的是往返查询
			if(huichengDatas.size() > 0) {
				return huichengDatas;
				
			} else {
				return quchengDatas;
			}
			
		} else{
			
			return quchengDatas;
		}
		

		

//		return totalDatas;
	}
	
	/** 
	 * 获取 去程/回程 数据
	 * 
	 * @param airs
	 * @param dCityCode
	 * @param AirlineMark 航班标识 ： 1-去程；2-回程
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> getAirlineDatas(ArrayList<ApiAirticketGetAirlineData> airs, String dCityCode, int AirlineMark) {
		
		ArrayList<ApiAirticketGetAirlineData> quchengDatas = new ArrayList<ApiAirticketGetAirlineData>();//去程
		ArrayList<ApiAirticketGetAirlineData> huichengDatas = new ArrayList<ApiAirticketGetAirlineData>();//回程

//		ArrayList<ApiAirticketGetAirlineData> totalDatas = new ArrayList<ApiAirticketGetAirlineData>();//综合


		for (ApiAirticketGetAirlineData airlineData : airs) {

			Log.i("air__1", airlineData.dCityCode);
			if(dCityCode.equals(airlineData.dCityCode)) {
				quchengDatas.add(airlineData);
			} else {
				huichengDatas.add(airlineData);
			}

		}
//		Log.i("air__", "quchengDatas:"+quchengDatas.size() + ";huichengDatas:"+huichengDatas.size());
		if(AirlineMark == 1) {//去s
			return quchengDatas;
		} else {//回
			return huichengDatas;
		}
		
	}
	
	/**
	 *  混合排序
	 * 
	 * @param srcDatas 原始数据
	 * @param timeInterval 时段（如早上（6-12点））；
	 * 选项<-1：用户没选择；0:早上；1：下午；2：晚上>
	 * 
	 * @param highLow 排序（如按价格升序）;
	 * 选项< -1：用户没选择;  0:时间从早到晚；1：时间从晚到早；2：价格从高到低；3：价格从低到高>
	 * 
	 * @param name 如航空公司名字
	 * @return
	 */
	public static ArrayList<ApiAirticketGetAirlineData> mixSort(ArrayList<ApiAirticketGetAirlineData> srcDatas, 
			int timeInterval, int highLow, String name) {
		ArrayList<ApiAirticketGetAirlineData> newDatas = new ArrayList<ApiAirticketGetAirlineData>();

		ArrayList<ApiAirticketGetAirlineData> tempDatas = new ArrayList<ApiAirticketGetAirlineData>();

		ArrayList<ApiAirticketGetAirlineData> resultDatas = new ArrayList<ApiAirticketGetAirlineData>();

		switch (timeInterval) {
		case 0://早上
			newDatas = toStartTimeAirLine(srcDatas, 0);
			break;

		case 1://下午
			newDatas = toStartTimeAirLine(srcDatas, 1);
			break;

		case 2://晚上
			newDatas = toStartTimeAirLine(srcDatas, 2);
			break;

		default:
			newDatas = srcDatas;
			break;
		}

		//公司名称
		if(name != null && !name.equals("")) {
			tempDatas.addAll(toAirLine(newDatas, name));
		} else{
			tempDatas.addAll(newDatas);
		}

		//排序
		switch (highLow) {
		case 0://时间从早到晚
			resultDatas = timeHightToLow(tempDatas);
			break;

		case 1://时间从晚到早
			resultDatas = timeLowToHight(tempDatas);
			break;

		case 2://价格从高到低
			resultDatas = priceHightToLow(tempDatas);
			break;

		case 3://价格从低到高
			resultDatas = priceLowToHight(tempDatas);
			break;

		default:
			resultDatas = tempDatas;
			break;
		}

		return resultDatas;
	}
	
	/**
	 * 根据标识获取订单状态
	 * W-未处理，P-处理中，S-已成交，C-已取消，R-全部退票，T-部分退票，U-未提交
	 * @param orderState
	 * @return
	 */
	public static String getOrderState (String orderState) {
		if(orderState == null || orderState.equals("")){
			return "未处理";
		}
		
		if(orderState.equals("W")) {
			return "未处理";
		} else if(orderState.equals("P")) {
			return "处理中";
		} else if(orderState.equals("S")) {
			return "已成交";
		}else if(orderState.equals("C")) {
			return "已取消";
		}else if(orderState.equals("R")) {
			return "全部退票";
		}else if(orderState.equals("T")) {
			return "部分退票";
		}else if(orderState.equals("U")) {
			return "未提交";
		}
		return "未处理";
	}


}
