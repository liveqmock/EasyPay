package com.inter.trade.ui.fragment.hotel.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;

/**
 * 酒店预订，获取关键字 解析
 * @author haifengli
 *
 */
public class HotelKeywordParser extends NetParser{

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
						
					}else if(data.mKey.equals("msgstart")){
						serializer.startTag("", "msgstart");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "msgstart");
						
					}else if(data.mKey.equals("msgdisplay")){
						serializer.startTag("", "msgdisplay");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "msgdisplay");
						
					}
					//城市ID
					else if(data.mKey.equals("cityId")){
						serializer.startTag("", "cityId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cityId");
					}
					//地标类型
					else if(data.mKey.equals("locationType")){
						serializer.startTag("", "locationType");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "locationType");
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
				if (parser.getName().compareTo("districtId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("districtId", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("districtName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("districtName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("zoneId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("zoneId", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("zoneName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("zoneName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("brandId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("brandId", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("brandName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("brandName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("themeId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("themeId", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("themeName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("themeName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("locationId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("locationId", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("locationName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("locationName", temp);
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
