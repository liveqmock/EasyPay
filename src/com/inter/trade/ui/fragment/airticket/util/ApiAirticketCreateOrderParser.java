package com.inter.trade.ui.fragment.airticket.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.ResponseData;
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;
import com.inter.trade.util.LoginUtil;

/**
 * 创建机票订单解析
 * @author zhichao.huang
 *
 */
public class ApiAirticketCreateOrderParser extends NetParser<String> {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//密钥
					if(data.mKey.equals("authorid")){
						serializer.startTag("", "authorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "authorid");
						
					}
					//总金额
					else if(data.mKey.equals("amount")){
						serializer.startTag("", "amount");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "amount");
					}
					
					//以下是飞机票信息 *****start********
					else if(data.mKey.equals("ticketList")){
						serializer.startTag("", "ticketList");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "ticketList");
					}
					else if(data.mKey.equals("ticketId")){
						serializer.startTag("", "ticketId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "ticketId");
					}
//					else if(data.mKey.equals("amout")){
//						serializer.startTag("", "amout");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "amout");
//					}
//					
//					else if(data.mKey.equals("departCityId")){
//						serializer.startTag("", "departCityId");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "departCityId");
//					}
//					
//					else if(data.mKey.equals("arriveCityId")){
//						serializer.startTag("", "arriveCityId");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "arriveCityId");
//					}
//					else if(data.mKey.equals("departPortCode")){
//						serializer.startTag("", "departPortCode");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "departPortCode");
//					}
//					else if(data.mKey.equals("arrivePortCode")){
//						serializer.startTag("", "arrivePortCode");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "arrivePortCode");
//						
//					}else if(data.mKey.equals("airlineCode")){
//						serializer.startTag("", "airlineCode");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "airlineCode");
//						
//					}else if(data.mKey.equals("flight")){
//						serializer.startTag("", "flight");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "flight");
//						
//					}else if(data.mKey.equals("class")){
//						serializer.startTag("", "class");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "class");
//						
//					}else if(data.mKey.equals("takeOffTime")){
//						serializer.startTag("", "takeOffTime");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "takeOffTime");
//						
//					}else if(data.mKey.equals("arriveTime")){
//						serializer.startTag("", "arriveTime");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "arriveTime");
//						
//					}else if(data.mKey.equals("rate")){
//						serializer.startTag("", "rate");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "rate");
//						
//					}else if(data.mKey.equals("price")){
//						serializer.startTag("", "price");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "price");
//						
//					}else if(data.mKey.equals("tax")){
//						serializer.startTag("", "tax");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "tax");
//						
//					}else if(data.mKey.equals("oilFee")){
//						serializer.startTag("", "oilFee");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "oilFee");
//						
//					}
					//****************end*************
					
					//乘车人信息 *****start*****
					else if(data.mKey.equals("passengerList")){
						serializer.startTag("", "passengerList");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "passengerList");
					}
					else if(data.mKey.equals("passengerId")){
						serializer.startTag("", "passengerId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "passengerId");
					}
					
//					else if(data.mKey.equals("passengerId")){
//						serializer.startTag("", "passengerId");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "passengerId");
//						
//					}else if(data.mKey.equals("name")){
//						serializer.startTag("", "name");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "name");
//						
//					}else if(data.mKey.equals("birthDay")){
//						serializer.startTag("", "birthDay");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "birthDay");
//						
//					}else if(data.mKey.equals("passportTypeId")){
//						serializer.startTag("", "passportTypeId");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "passportTypeId");
//						
//					}else if(data.mKey.equals("passportNo")){
//						serializer.startTag("", "passportNo");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "passportNo");
//						
//					}else if(data.mKey.equals("gender")){
//						serializer.startTag("", "gender");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "gender");
//						
//					}else if(data.mKey.equals("telephone")){
//						serializer.startTag("", "telephone");
//						serializer.text(data.mValue.trim());
//						serializer.endTag("", "telephone");
//						
//					}
					//****************end*************
					
					//联系人人信息 *****start*****
					else if(data.mKey.equals("contacterList")){
						serializer.startTag("", "contacterList");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "contacterList");
					}
					else if(data.mKey.equals("contacterId")){
						serializer.startTag("", "contacterId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "contacterId");
					}
					//****************end*************
					
					//****************支付信息 start*************
					else if(data.mKey.equals("payinfo")){
						serializer.startTag("", "payinfo");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "payinfo");
					}
					else if(data.mKey.equals("cardNumber")){
						serializer.startTag("", "cardNumber");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardNumber");
						
					}
					else if(data.mKey.equals("cardValidity")){
						serializer.startTag("", "cardValidity");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardValidity");
						
					}
					else if(data.mKey.equals("cardCVV2No")){
						serializer.startTag("", "cardCVV2No");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardCVV2No");
						
					}
					else if(data.mKey.equals("cardHolder")){
						serializer.startTag("", "cardHolder");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardHolder");
						
					}
					else if(data.mKey.equals("cardHolderIdCardType")){
						serializer.startTag("", "cardHolderIdCardType");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardHolderIdCardType");
						
					}
					else if(data.mKey.equals("cardHolderIdCardNumber")){
						serializer.startTag("", "cardHolderIdCardNumber");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardHolderIdCardNumber");
						
					}
					//有效月份
					else if(data.mKey.equals("bkcardexpireMonth")){
						serializer.startTag("", "bkcardexpireMonth");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireMonth");
						
					}
					//有效年份
					else if(data.mKey.equals("bkcardexpireYear")){
						serializer.startTag("", "bkcardexpireYear");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireYear");
						
					}
					//有效年份+月份
					else if(data.mKey.equals("validity")){
						serializer.startTag("", "validity");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "validity");
						
					}
					//****************end*********************
					
				}
			}
		}
	}

	@Override
	public ProtocolData parserBody(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		ProtocolData res = new ProtocolData("msgbody", null);
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("result") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("result", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("message") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("message", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("orderId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("orderId", temp);
						res.addChild(r);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("msgbody") == 0) {
					return res;
				}
			}
				break;
			case XmlPullParser.TEXT: {

			}
				break;
			}
			eventType = parser.next();
		}
		return res;
	}

	private  void parserHelpItem(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("couponid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("couponid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("couponmoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("couponmoney", temp);
						parent.addChild(data);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("msgchild") == 0) {
					return;
				}
			}
				break;
			case XmlPullParser.TEXT: {

			}
				break;
			}
			eventType = parser.next();
		}
		return;
	}

	/**
	 * 解析响应体的 List<ProtocolData> 集合
	 */
	@Override
	public ArrayList<String> parserResponseDatas(List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<String> responseDatas = new ArrayList<String>();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				List<ProtocolData> orderId = data.find("/orderId");
				if(orderId != null){
					responseDatas.add(orderId.get(0).mValue);
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					MoblieRechangeRecordData recordData = new MoblieRechangeRecordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("rechamoney")){
									recordData.rechamoney  = item.mValue;
									
								}else if(item.mKey.equals("rechapaymoney")){
									recordData.rechapaymoney  = item.mValue;
									
								}else if(item.mKey.equals("rechamobile")){
									
									recordData.rechamobile  = item.mValue;
									
								}else if(item.mKey.equals("rechamobileprov")){
									
									recordData.rechamobileprov  = item.mValue;
									
								}else if(item.mKey.equals("rechabkcardno")){
									
									recordData.rechabkcardno  = item.mValue;
									
								}else if(item.mKey.equals("rechadatetime")){
									
									recordData.rechadatetime  = item.mValue;
								}else if(item.mKey.equals("rechastate")){
									
									recordData.rechastate  = item.mValue;
								}
								/**else if(item.mKey.equals("allmoney")){
									
									picData.allmoney  = item.mValue;
								}else if(item.mKey.equals("huancardbank")){
									
									picData.huancardbank  = item.mValue;
								}else if(item.mKey.equals("fucardbank")){
									
									picData.fucardbank  = item.mValue;
								}*/
							}
						}
					}
					
//					responseDatas.add(recordData);
				}
				
			}
		}
		return responseDatas;
	}
	
}
