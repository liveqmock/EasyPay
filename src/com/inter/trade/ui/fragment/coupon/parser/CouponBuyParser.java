package com.inter.trade.ui.fragment.coupon.parser;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;


/**
 * 获取抵用券流水号
 * @author apple
 *
 */
public class CouponBuyParser extends NetParser{

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
					if(data.mKey.equals("couponid")){
						serializer.startTag("", "couponid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "couponid");
						
					}else if(data.mKey.equals("couponmoney")){
						serializer.startTag("", "couponmoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "couponmoney");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "paycardid");
						
					}else if(data.mKey.equals("creditcardno")){
						serializer.startTag("", "creditcardno");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "creditcardno");
						
					}else if(data.mKey.equals("creditbank")){
						serializer.startTag("", "creditbank");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "creditbank");
						
					}else if(data.mKey.equals("creditcardman")){
						serializer.startTag("", "creditcardman");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "creditcardman");
						
					}else if(data.mKey.equals("creditcardphone")){
						serializer.startTag("", "creditcardphone");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "creditcardphone");
						
					}else if(data.mKey.equals("merReserved")){
						serializer.startTag("", "merReserved");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "merReserved");
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
				if (parser.getName().compareTo("couponid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("couponid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("couponmoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("couponmoney", temp);
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
