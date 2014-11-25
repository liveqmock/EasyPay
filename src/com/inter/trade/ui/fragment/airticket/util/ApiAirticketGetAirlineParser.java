package com.inter.trade.ui.fragment.airticket.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;

/**
 * 飞机票，航班查询解析
 * @author zhichao.huang
 *
 */
public class ApiAirticketGetAirlineParser extends NetParser{

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
						
					}else if(data.mKey.equals("departCityCode")){
						serializer.startTag("", "departCityCode");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "departCityCode");
						
					}else if(data.mKey.equals("arriveCityCode")){
						serializer.startTag("", "arriveCityCode");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "arriveCityCode");
						
					}else if(data.mKey.equals("departDate")){
						serializer.startTag("", "departDate");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "departDate");
						
					}else if(data.mKey.equals("returnDate")){
						serializer.startTag("", "returnDate");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "returnDate");
						
					}else if(data.mKey.equals("searchType")){
						serializer.startTag("", "searchType");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "searchType");
						
					}
					//以下是航班详细附加字段
					else if(data.mKey.equals("departTime")){
						serializer.startTag("", "departTime");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "departTime");
						
					}else if(data.mKey.equals("returnTime")){
						serializer.startTag("", "returnTime");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "returnTime");
						
					}else if(data.mKey.equals("flight")){
						serializer.startTag("", "flight");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "flight");
					}
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
				}else if (parser.getName().compareTo("msgchild") == 0) {
					ProtocolData r = new ProtocolData("msgchild", null);
					parserHelpItem(parser, r);
					res.addChild(r);
				}else if (parser.getName().compareTo("msgallcount") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("msgallcount", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("msgdiscount") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("msgdiscount", temp);
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
				if (parser.getName().compareTo("takeOffTime") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("takeOffTime", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("arriveTime") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("arriveTime", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("flight") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("flight", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("airLineCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("airLineCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("airLineName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("airLineName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("craftType") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("craftType", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("price") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("price", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("standardPrice") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("standardPrice", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("oilFee") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("oilFee", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("tax") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("tax", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("standardPriceForChild") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("standardPriceForChild", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("oilFeeForChild") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("oilFeeForChild", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("taxForChild") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("taxForChild", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("standardPriceForBaby") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("standardPriceForBaby", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("oilFeeForBaby") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("oilFeeForBaby", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("taxForBaby") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("taxForBaby", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("quantity") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("quantity", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("rePolicy") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("rePolicy", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("rerNote") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("rerNote", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("endNote") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("endNote", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("refNote") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("refNote", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("dPortName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("dPortName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("aPortName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("aPortName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("dPortCode") == 0) {//出发机场三字码
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("dPortCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("aPortCode") == 0) {//到达机场三字码
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("aPortCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("class") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("class", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("dCityCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("dCityCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("id") == 0) {//航班ID，用于生成订单
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("id", temp);
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
	
}
