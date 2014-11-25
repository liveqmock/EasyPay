package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;


/**
 * 刷卡器验证
 * @author apple
 *
 */
public class KeyParser extends NetParser{

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
					if(data.mKey.equals("paycardkey")){
						serializer.startTag("", "paycardkey");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardkey");
						
					}else if(data.mKey.equals("authorid")){
						serializer.startTag("", "authorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "authorid");
						
					}else if(data.mKey.equals("bkcardno")){
						serializer.startTag("", "bkcardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardno");
						
					}else if(data.mKey.equals("paytype")){
						serializer.startTag("", "paytype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paytype");
						
					}
					else if(data.mKey.equals("readmode")){
						serializer.startTag("", "readmode");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "readmode");
						
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
				}else if (parser.getName().compareTo("bkcardno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardno", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardman") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardman", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardphone") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardphone", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardbankid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardbankid", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardbankname") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardbankname", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardyxmonth") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardyxmonth", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardyxyear") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardyxyear", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardcvv") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardcvv", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardidcard") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardidcard", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardtype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardtype", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkcardbankno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkcardbankno", temp);
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

	
}
