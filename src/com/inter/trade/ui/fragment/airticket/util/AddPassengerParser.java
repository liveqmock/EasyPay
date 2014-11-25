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
 * 保存乘机人解析
 * @author zhichao.huang
 *
 */
public class AddPassengerParser extends NetParser{

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
					if(data.mKey.equals("name")){
						serializer.startTag("", "name");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "name");
					}else if(data.mKey.equals("cardType")){
						serializer.startTag("", "cardType");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardType");
					}else if(data.mKey.equals("cardId")){
						serializer.startTag("", "cardId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cardId");
					}else if(data.mKey.equals("phoneNumber")){
						serializer.startTag("", "phoneNumber");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "phoneNumber");
					}
					/**
					 * 默认值1代表成人，2代表儿童，3代表婴儿
					*/
					else if(data.mKey.equals("passengerType")){
						serializer.startTag("", "passengerType");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "passengerType");
					}
					else if(data.mKey.equals("birthDay")){
						serializer.startTag("", "birthDay");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "birthDay");
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
