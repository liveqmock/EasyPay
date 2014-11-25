package com.inter.trade.ui.fragment.cridet.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;
import com.inter.protocol.util.ProtocolUtil;
import com.inter.trade.data.ResponseData;
import com.inter.trade.ui.creditcard.data.SmsCode;
import com.inter.trade.ui.fragment.airticket.util.ApiAirticketGetOrderHistoryData;
import com.inter.trade.util.LoginUtil;

/**
 * 信用卡 获取手续费
 * 
 * @author zhichao.huang
 *
 */
public class CreditCardFeeParser extends NetParser<CreditCardfeeData2> {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("bankid")){
						serializer.startTag("", "bankid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bankid");
						
					}else if(data.mKey.equals("money")){
						serializer.startTag("", "money");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "money");
						
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
				}else if (parser.getName().compareTo("paymoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("paymoney", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("feemoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("feemoney", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("allmoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("allmoney", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("bkntno") == 0) {
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
	
	
	@Override
	public ArrayList<CreditCardfeeData2> parserResponseDatas(
			List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<CreditCardfeeData2> responseDatas = new ArrayList<CreditCardfeeData2>();
		CreditCardfeeData2 creditCardfeeData2=new CreditCardfeeData2();
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
				
				List<ProtocolData> paymoney = data.find("/paymoney");
				if (paymoney != null) {
					creditCardfeeData2.setPaymoney(paymoney.get(0).getmValue());
				}
				
				List<ProtocolData> feemoney = data.find("/feemoney");
				if (feemoney != null) {
					creditCardfeeData2.setFeemoney(feemoney.get(0).getmValue());
				}
				
				List<ProtocolData> allmoney = data.find("/allmoney");
				if (allmoney != null) {
					creditCardfeeData2.setAllmoney(allmoney.get(0).getmValue());
				}
				
				List<ProtocolData> bkntno = data.find("/bkntno");
				if (bkntno != null) {
					creditCardfeeData2.setBkntno(bkntno.get(0).getmValue());
				}
				
				responseDatas.add(creditCardfeeData2);
			}
		}
		return responseDatas;
	}
	
}