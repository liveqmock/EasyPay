package com.inter.protocol.body;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.BankRecordData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.util.LoginUtil;


/**
 * 银行记录列表
 * @author apple
 *
 */
public class BankRecordParser extends NetParser<BankRecordData>{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//开始读取记录
					if(data.mKey.equals("authorid")){
						serializer.startTag("", "authorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "authorid");
						
					}else if(data.mKey.equals("paytype")){
						serializer.startTag("", "paytype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paytype");
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
				
				}else if (parser.getName().compareTo("msgchild") == 0) {
						ProtocolData r = new ProtocolData("msgchild", null);
						parserHelpItem(parser, r);
						res.addChild(r);
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
				if (parser.getName().compareTo("shoucardid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("shoucardid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("shoucardno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("shoucardno", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("shoucardbank") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("shoucardbank", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("shoucardman") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("shoucardman", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("shoucardmobile") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("shoucardmobile", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("paytype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("paytype", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bankid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bankid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bkcardbanklogo") == 0) {//银行logo链接
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bkcardbanklogo", temp);
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
	
	@Override
	public ArrayList<BankRecordData> parserResponseDatas(
			List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<BankRecordData> responseDatas = new ArrayList<BankRecordData>();
		ResponseData response = new ResponseData();
		LoginUtil.mLoginStatus.mResponseData = response;
		for (ProtocolData data : mDatas) {
			if(data.mKey.equals(ProtocolUtil.msgheader)){
				ProtocolUtil.parserResponse(response, data);
				
			}else if(data.mKey.equals(ProtocolUtil.msgbody)){
				List<ProtocolData> result1 = data.find("/result");
				if(result1 != null){
					LoginUtil.mLoginStatus.result = result1.get(0).mValue;
				}
				List<ProtocolData> message = data.find("/message");
				if(message != null){
					LoginUtil.mLoginStatus.message = message.get(0).mValue;
				}
				
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					BankRecordData picData = new BankRecordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("shoucardid")){
									picData.shoucardid  = item.mValue;
									
								}else if(item.mKey.equals("shoucardno")){
									picData.shoucardno  = item.mValue;
									
								}else if(item.mKey.equals("shoucardbank")){
									
									picData.shoucardbank  = item.mValue;
								}else if(item.mKey.equals("shoucardman")){
									
									picData.shoucardman  = item.mValue;
								}else if(item.mKey.equals("shoucardmobile")){
									
									picData.shoucardmobile  = item.mValue;
								}else if(item.mKey.equals("paytype")){
									
									picData.paytype  = item.mValue;
								}else if(item.mKey.equals("bankid")){
									
									picData.bankid  = item.mValue;
								}else if(item.mKey.equals("bkcardbanklogo")){
									
									picData.bkcardbanklogo  = item.mValue;
								}
							}
						}
					}
					
					responseDatas.add(picData);
				}
				
			}
		}
		return responseDatas;
	}
	
}
