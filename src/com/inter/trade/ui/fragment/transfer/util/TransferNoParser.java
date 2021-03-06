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
 * 获取转账还款流水号
 * @author apple
 *
 */
public class TransferNoParser extends NetParser{

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
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "paytype");
						
					}else if(data.mKey.equals("paymoney")){
						serializer.startTag("", "paymoney");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "paymoney");
						
					}else if(data.mKey.equals("money")){
						serializer.startTag("", "money");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "money");
						
					}
					else if(data.mKey.equals("shoucardno")){
						serializer.startTag("", "shoucardno");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "shoucardno");
						
					}else if(data.mKey.equals("shoucardmobile")){
						serializer.startTag("", "shoucardmobile");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "shoucardmobile");
						
					}else if(data.mKey.equals("shoucardman")){
						serializer.startTag("", "shoucardman");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "shoucardman");
						
					}else if(data.mKey.equals("shoucardbank")){
						serializer.startTag("", "shoucardbank");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "shoucardbank");
						
					}else if(data.mKey.equals("fucardno")){
						serializer.startTag("", "fucardno");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "fucardno");
					}
					else if(data.mKey.equals("fucardbank")){
						serializer.startTag("", "fucardbank");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "fucardbank");
					}
					
					else if(data.mKey.equals("fucardmobile")){
						serializer.startTag("", "fucardmobile");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "fucardmobile");
					}
					
					else if(data.mKey.equals("fucardman")){
						serializer.startTag("", "fucardman");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "fucardman");
					}
					
					else if(data.mKey.equals("current")){
						serializer.startTag("", "current");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "current");
					}
					
					else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "paycardid");
					}
					else if(data.mKey.equals("payfee")){
						serializer.startTag("", "payfee");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "payfee");
					}
					else if(data.mKey.equals("arriveid")){
						serializer.startTag("", "arriveid");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "arriveid");
					}
					else if(data.mKey.equals("shoucardmemo")){
						serializer.startTag("", "shoucardmemo");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "shoucardmemo");
					}
					else if(data.mKey.equals("sendsms")){
						serializer.startTag("", "sendsms");
						if(data.mValue==null){
							serializer.text("");
						}else{
							serializer.text(data.mValue.trim());
						}
						serializer.endTag("", "sendsms");
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
