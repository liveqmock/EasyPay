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
public class AddEmployerInfoParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("month")){
						serializer.startTag("", "month");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "month");
						
					}else if(data.mKey.equals("phone")){
						serializer.startTag("", "phone");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "phone");
						
					}else if(data.mKey.equals("name")){
						serializer.startTag("", "name");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "name");
						
					}else if(data.mKey.equals("money")){
						serializer.startTag("", "money");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "money");
						
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
}
