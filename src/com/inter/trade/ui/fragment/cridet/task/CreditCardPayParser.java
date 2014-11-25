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
 * 
 * @author chenguangchi
 *
 */
public class CreditCardPayParser extends NetParser<SmsCode> {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {


		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("paytype")){
						serializer.startTag("", "paytype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paytype");
						
					}else if(data.mKey.equals("paymoney")){
						serializer.startTag("", "paymoney");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paymoney");
						
					}else if(data.mKey.equals("shoucardno")){
						serializer.startTag("", "shoucardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "shoucardno");
						
					}else if(data.mKey.equals("shoucardmobile")){
						serializer.startTag("", "shoucardmobile");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "shoucardmobile");
						
					}else if(data.mKey.equals("shoucardman")){
						serializer.startTag("", "shoucardman");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "shoucardman");
						
					}else if(data.mKey.equals("shoucardbank")){
						serializer.startTag("", "shoucardbank");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "shoucardbank");
						
					}else if(data.mKey.equals("current")){
						serializer.startTag("", "current");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "current");
						
					}else if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardid");
						
					}else if(data.mKey.equals("merReserved")){
						serializer.startTag("", "merReserved");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "merReserved");
						
					}else if(data.mKey.equals("bkcardbank")){
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
						
					}else if(data.mKey.equals("allmoney")){
						serializer.startTag("", "allmoney");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "allmoney");
						
					}else if(data.mKey.equals("feemoney")){
						serializer.startTag("", "feemoney");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "feemoney");
						
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
				
//				int mTotalCount, mLoadedCount;
//				List<ProtocolData> msgallcount = data.find("/msgallcount");
//				if(msgallcount != null){
//					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
//					if(bundle != null) {
//						bundle.putInt("mTotalCount", mTotalCount);//把订单总条数放到bundle
//					}
//				}
//				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
//				if(msgdiscount != null){
//					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
//					if(bundle != null) {
//						bundle.putInt("mLoadedCount", mLoadedCount);//把当前加载的总条数放到bundle
//					}
//				}
				
				
				
//				List<ProtocolData> aupic = data.find("/msgchild");
//				if(aupic!=null)
//				for(ProtocolData child:aupic){
//					ApiAirticketGetOrderHistoryData recordData = new ApiAirticketGetOrderHistoryData();
//					if (child.mChildren != null && child.mChildren.size() > 0) {
//						Set<String> keys = child.mChildren.keySet();
//						for(String key:keys){
//							List<ProtocolData> rs = child.mChildren.get(key);
//							for(ProtocolData item: rs){
//								if(item.mKey.equals("departCity")){
//									recordData.departCity  = item.mValue;
//									
//								}else if(item.mKey.equals("arriveCity")){
//									recordData.arriveCity  = item.mValue;
//									
//								}else if(item.mKey.equals("createOrderTime")){
//									
//									recordData.createOrderTime  = item.mValue;
//									
//								}else if(item.mKey.equals("takeOffTime")){
//									
//									recordData.takeOffTime  = item.mValue;
//									
//								}else if(item.mKey.equals("flight")){
//									
//									recordData.flight  = item.mValue;
//									
//								}else if(item.mKey.equals("craftType")){
//									
//									recordData.craftType  = item.mValue;
//								}else if(item.mKey.equals("totalPrice")){
//									
//									recordData.totalPrice  = item.mValue;
//								}
//								else if(item.mKey.equals("status")){
//									
//									recordData.status  = item.mValue;
//								}/**else if(item.mKey.equals("huancardbank")){
//									
//									picData.huancardbank  = item.mValue;
//								}else if(item.mKey.equals("fucardbank")){
//									
//									picData.fucardbank  = item.mValue;
//								}*/
//							}
//						}
//					}
//					
//					responseDatas.add(recordData);
//				}
				
			}
		}
		return responseDatas;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}