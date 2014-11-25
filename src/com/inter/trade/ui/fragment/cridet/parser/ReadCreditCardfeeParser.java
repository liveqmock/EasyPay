package com.inter.trade.ui.fragment.cridet.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;


/**
 * 获取交易的手续费和其他信息
 * @author apple
 *
 */
public class ReadCreditCardfeeParser extends NetParser{

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
					if(data.mKey.equals("paytype")){
						serializer.startTag("", "paytype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paytype");
						
					}else if(data.mKey.equals("paymoney")){
						serializer.startTag("", "paymoney");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paymoney");
						
					}else if(data.mKey.equals("paybankid")){
						serializer.startTag("", "paybankid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paybankid");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardid");
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
				}else if (parser.getName().compareTo("fee") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("fee", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("feemoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("feemoney", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("arrivetime") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("arrivetime", temp);
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
