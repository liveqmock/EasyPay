package com.inter.trade.ui.fragment.buyhtbcard.util;

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
import com.inter.trade.util.LoginUtil;

/**
 * 购买汇通卡
 * 易宝交易-获取订单交易流水号解析
 * 
 * @author zhichao.huang
 *
 */
public class YiBaoOrderRequestParser extends NetParser<SmsCode> {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					
					//业务信息
					if(data.mKey.equals("orderprodureid")){//购买产品id
						serializer.startTag("", "orderprodureid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "orderprodureid");
					}
					else if(data.mKey.equals("ordernum")){//购买数量
						serializer.startTag("", "ordernum");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "ordernum");
					}
					else if(data.mKey.equals("orderprice")){//单个价格
						serializer.startTag("", "orderprice");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "orderprice");
					}
					
					else if(data.mKey.equals("ordermoney")){//订单总额
						serializer.startTag("", "ordermoney");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "ordermoney");
					}
					else if(data.mKey.equals("ordermemo")){//订单备注
						serializer.startTag("", "ordermemo");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "ordermemo");
						
					}
					
					//硬件信息
					else if(data.mKey.equals("paycardid")){//刷卡器ID
						serializer.startTag("", "paycardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardid");
					}
					
					//统一付款信用卡信息
					else if(data.mKey.equals("bkcardbank")){
						serializer.startTag("", "bkcardbank");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardbank");
						
					}else if(data.mKey.equals("bkCardno")){
						serializer.startTag("", "bkCardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkCardno");
						
					}else if(data.mKey.equals("bkcardman")){
						serializer.startTag("", "bkcardman");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardman");
						
					}else if(data.mKey.equals("bkcardexpireMonth")){
						serializer.startTag("", "bkcardexpireMonth");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireMonth");
						
					}else if(data.mKey.equals("bkcardmanidcard")){
						serializer.startTag("", "bkcardmanidcard");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardmanidcard");
						
					}else if(data.mKey.equals("bankid")){
						serializer.startTag("", "bankid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bankid");
						
					}else if(data.mKey.equals("bkcardexpireYear")){
						serializer.startTag("", "bkcardexpireYear");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardexpireYear");
						
					}else if(data.mKey.equals("bkcardPhone")){
						serializer.startTag("", "bkcardPhone");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardPhone");
						
					}else if(data.mKey.equals("bkcardcvv")){
						serializer.startTag("", "bkcardcvv");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "bkcardcvv");
						
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
				}else if (parser.getName().compareTo("verifyCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("verifyCode", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("bkordernumber") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("bkordernumber", temp);
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

	@Override
	public ArrayList<SmsCode> parserResponseDatas(
			List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<SmsCode> responseDatas = new ArrayList<SmsCode>();
		SmsCode smscode=new SmsCode();
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
					smscode.setMessage(message.get(0).getmValue());
				}
				
				List<ProtocolData> orderId = data.find("/orderId");
				if (orderId != null) {
					smscode.setOrderId(orderId.get(0).getmValue());
				}
				
				List<ProtocolData> code = data.find("/verifyCode");
				if(code!=null){
					if("1".equals(code.get(0).getmValue())){//是否需要验证码
						smscode.setNeed(true);
					}else{
						smscode.setNeed(false);
					}
				}
				List<ProtocolData> bkordernumber = data.find("/bkordernumber");
				if(bkordernumber!=null){
					smscode.setBkordernumber(bkordernumber.get(0).mValue);
				}
				List<ProtocolData> bkntno = data.find("/bkntno");
				if(bkntno!=null){
					smscode.setBkntno(bkntno.get(0).mValue);
				}
				List<ProtocolData> token = data.find("/verifytoken");
				if(token!=null){
					smscode.setVerifytoken(token.get(0).mValue);
				}
				responseDatas.add(smscode);
			}
		}
		return responseDatas;
	}
	
}
