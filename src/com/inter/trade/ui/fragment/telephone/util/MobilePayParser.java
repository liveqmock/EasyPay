package com.inter.trade.ui.fragment.telephone.util;

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
public class MobilePayParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("rechargeMoney")){
						serializer.startTag("", "rechargeMoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "rechargeMoney");
						
					}else if(data.mKey.equals("payMoney")){
						serializer.startTag("", "payMoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "payMoney");
						
					}else if(data.mKey.equals("rechargePhone")){
						serializer.startTag("", "rechargePhone");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "rechargePhone");
						
					}else if(data.mKey.equals("bankCardId")){
						serializer.startTag("", "bankCardId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "bankCardId");
						
					}else if(data.mKey.equals("bankId")){
						serializer.startTag("", "bankId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "bankId");
						
					}else if(data.mKey.equals("manCardId")){
						serializer.startTag("", "manCardId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "manCardId");
						
					}else if(data.mKey.equals("payPhone")){
						serializer.startTag("", "payPhone");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "payPhone");
						
					}else if(data.mKey.equals("manName")){
						serializer.startTag("", "manName");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "manName");
						
					}else if(data.mKey.equals("expireYear")){
						serializer.startTag("", "expireYear");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "expireYear");
						
					}else if(data.mKey.equals("expireMonth")){
						serializer.startTag("", "expireMonth");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "expireMonth");
						
					}else if(data.mKey.equals("cvv")){
						serializer.startTag("", "cvv");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "cvv");
						
					}else if(data.mKey.equals("mobileProvince")){
						serializer.startTag("", "mobileProvince");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "mobileProvince");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "paycardid");
						
					}else if(data.mKey.equals("bankname")){
						serializer.startTag("", "bankname");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "bankname");
						
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
				}else if (parser.getName().compareTo("orderId") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("orderId", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("verifyCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("verifyCode", temp);
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