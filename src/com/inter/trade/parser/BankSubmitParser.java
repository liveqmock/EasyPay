package com.inter.trade.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;


/**
 * 修改银行卡信息
 * @author apple
 *
 */
public class BankSubmitParser extends NetParser{

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
					if(data.mKey.equals("aushoucardman")){
						serializer.startTag("", "aushoucardman");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aushoucardman");
						
					}else if(data.mKey.equals("aushoucardphone")){
						serializer.startTag("", "aushoucardphone");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aushoucardphone");
						
					}else if(data.mKey.equals("aushoucardno")){
						serializer.startTag("", "aushoucardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aushoucardno");
						
					}else if(data.mKey.equals("aushoucardbank")){
						serializer.startTag("", "aushoucardbank");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aushoucardbank");
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
			eventType = parser.next();
		}
		return res;
	}

	
	
}
