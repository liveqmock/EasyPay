package com.inter.trade.ui.fragment.salarypay.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;

/**
 * 
 * @author chenguangchi
 *
 */
public class GetSalaryHistoryParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("querytype")){
						serializer.startTag("", "querytype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "querytype");
						
					}else if(data.mKey.equals("querywhere")){
						serializer.startTag("", "querywhere");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "querywhere");
						
					}else if(data.mKey.equals("bossauthorid")){
						serializer.startTag("", "bossauthorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bossauthorid");
						
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
			if(eventType!=XmlPullParser.END_DOCUMENT){
				eventType = parser.next();
			}
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
				if (parser.getName().compareTo("wagestanum") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("wagestanum", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("wageallmoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("wageallmoney", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("wagemonth") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("wagemonth", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("wagepaymoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("wagepaymoney", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("ilist") == 0) {
					ProtocolData r = new ProtocolData("ilist", null);
					parseChildItem(parser, r);
					parent.addChild(r);
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
			if(eventType!=XmlPullParser.END_DOCUMENT){
				eventType = parser.next();
			}
		}
		return;
	}
	
	private  void parseChildItem(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("msgchild") == 0) {
					ProtocolData r = new ProtocolData("msgchild", null);
					parseChild(parser, r);
					parent.addChild(r);
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("ilist") == 0) {
					return;
				}
			}
				break;
			case XmlPullParser.TEXT: {

			}
				break;
			}
			if(eventType!=XmlPullParser.END_DOCUMENT){
				eventType = parser.next();
			}
		}
		return;
	}
	
	private  void parseChild(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("mobile") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mobile", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("staname") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("staname", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("wagemoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("wagemoney", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardno", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbank") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbank", temp);
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
			if(eventType!=XmlPullParser.END_DOCUMENT){
				eventType = parser.next();
			}
		}
		return;
	}
	
}
