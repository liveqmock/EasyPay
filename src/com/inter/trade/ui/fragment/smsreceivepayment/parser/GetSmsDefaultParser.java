package com.inter.trade.ui.fragment.smsreceivepayment.parser;

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
public class GetSmsDefaultParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("bkcardid")){
						serializer.startTag("", "bkcardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardid");
						
					}else if(data.mKey.equals("bkcardisdefault")){
						serializer.startTag("", "bkcardisdefault");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardisdefault");
						
					}else if(data.mKey.equals("bkcardshoudefault")){
						serializer.startTag("", "bkcardshoudefault");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardshoudefault");
						
					}else if(data.mKey.equals("bkcardfudefault")){
						serializer.startTag("", "bkcardfudefault");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardfudefault");
						
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
				}else if (parser.getName().compareTo("paychalname") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("paychalname", temp);
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
				if (parser.getName().compareTo("bkcardid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardno", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbankid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbankid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbank") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbank", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbanklogo") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbanklogo", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbankman") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbankman", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbankphone") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbankphone", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardyxmonth") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardyxmonth", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardyxyear") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardyxyear", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardcvv") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardcvv", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardidcard") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardidcard", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardisdefault") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardisdefault", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardcardtype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardcardtype", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbankcode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbankcode", temp);
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
