package com.inter.trade.ui.fragment.gamerecharge.parser;

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
public class GetBillParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("gameName")){
						serializer.startTag("", "gameName");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "gameName");
						
					}else if(data.mKey.equals("area")){
						serializer.startTag("", "area");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "area");
						
					}else if(data.mKey.equals("quantity")){
						serializer.startTag("", "quantity");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "quantity");
						
					}else if(data.mKey.equals("userCount")){
						serializer.startTag("", "userCount");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "userCount");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardid");
						
					}else if(data.mKey.equals("rechabkcardno")){
						serializer.startTag("", "rechabkcardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "rechabkcardno");
						
					}else if(data.mKey.equals("gameId")){
						serializer.startTag("", "gameId");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "gameId");
						
					}else if(data.mKey.equals("server")){
						serializer.startTag("", "server");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "server");
						
					}else if(data.mKey.equals("price")){
						serializer.startTag("", "price");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "price");
						
					}else if(data.mKey.equals("cost")){
						serializer.startTag("", "cost");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "cost");
						
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
				}else if (parser.getName().compareTo("bkntno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkntno", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("totalPrice") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("totalPrice", temp);
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