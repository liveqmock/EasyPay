package com.inter.trade.ui.fragment.transfer.util;

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
public class TransferCreditCardParser extends NetParser {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("transferMoney")){
						serializer.startTag("", "transferMoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "transferMoney");
						
					}else if(data.mKey.equals("payMoney")){
						serializer.startTag("", "payMoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "payMoney");
						
					}else if(data.mKey.equals("cardReaderId")){
						serializer.startTag("", "cardReaderId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "cardReaderId");
						
					}else if(data.mKey.equals("receiveBankCardId")){
						serializer.startTag("", "receiveBankCardId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "receiveBankCardId");
						
					}else if(data.mKey.equals("receiveBankName")){
						serializer.startTag("", "receiveBankName");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "receiveBankName");
						
					}else if(data.mKey.equals("receivePhone")){
						serializer.startTag("", "receivePhone");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "receivePhone");
						
					}else if(data.mKey.equals("receivePersonName")){
						serializer.startTag("", "receivePersonName");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "receivePersonName");
						
					}else if(data.mKey.equals("sendBankCardId")){
						serializer.startTag("", "sendBankCardId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendBankCardId");
						
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
						
					}else if(data.mKey.equals("sendBankCode")){
						serializer.startTag("", "sendBankCode");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendBankCode");
						
					}else if(data.mKey.equals("personCardId")){
						serializer.startTag("", "personCardId");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "personCardId");
						
					}else if(data.mKey.equals("sendPhone")){
						serializer.startTag("", "sendPhone");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendPhone");
						
					}else if(data.mKey.equals("sendPersonName")){
						serializer.startTag("", "sendPersonName");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendPersonName");
						
					}else if(data.mKey.equals("sendBankCode")){
						serializer.startTag("", "sendBankCode");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendBankCode");
						
					}else if(data.mKey.equals("transferType")){
						serializer.startTag("", "transferType");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "transferType");
						
					}else if(data.mKey.equals("payType")){
						serializer.startTag("", "payType");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "payType");
						
					}else if(data.mKey.equals("arriveid")){
						serializer.startTag("", "arriveid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "arriveid");
						
					}else if(data.mKey.equals("sendBankName")){
						serializer.startTag("", "sendBankName");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendBankName");
						
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