package com.inter.trade.ui.fragment.qmoney.util;

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
 * @author Lihaifeng
 *
 */
public class QMoneyCreditCardParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("payMoney")){
						serializer.startTag("", "payMoney");
						
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						
						serializer.endTag("", "payMoney");
						
					}else if(data.mKey.equals("rechargeMoney")){
						serializer.startTag("", "rechargeMoney");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "rechargeMoney");
						
					}else if(data.mKey.equals("RechargeQQ")){
						serializer.startTag("", "RechargeQQ");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "RechargeQQ");
						
					}else if(data.mKey.equals("bkCardno")){
						serializer.startTag("", "bkCardno");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkCardno");
						
					}else if(data.mKey.equals("bkcardman")){
						serializer.startTag("", "bkcardman");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardman");
						
					}else if(data.mKey.equals("bkcardexpireMonth")){
						serializer.startTag("", "bkcardexpireMonth");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireMonth");
						
					}else if(data.mKey.equals("bkcardmanidcard")){
						serializer.startTag("", "bkcardmanidcard");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardmanidcard");
						
					}else if(data.mKey.equals("bankid")){
						serializer.startTag("", "bankid");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bankid");
						
					}else if(data.mKey.equals("bankno")){
						serializer.startTag("", "bankno");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bankno");
						
					}else if(data.mKey.equals("bankname")){
						serializer.startTag("", "bankname");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bankname");
						
					}else if(data.mKey.equals("bkcardexpireYear")){
						serializer.startTag("", "bkcardexpireYear");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireYear");
						
					}else if(data.mKey.equals("bkcardPhone")){
						serializer.startTag("", "bkcardPhone");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardPhone");
						
					}else if(data.mKey.equals("bkcardcvv")){
						serializer.startTag("", "bkcardcvv");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardcvv");
						
					}else if(data.mKey.equals("paytype")){
						serializer.startTag("", "paytype");
						if(data.mValue == null)
							serializer.text("");
						else
							serializer.text(data.mValue.trim());
						serializer.endTag("", "paytype");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						if(data.mValue == null)
							serializer.text("");
						else
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
				}else if (parser.getName().compareTo("bkordernumber") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkordernumber", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("verifyCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("verifyCode", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkntno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkntno", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("verifytoken") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("verifytoken", temp);
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