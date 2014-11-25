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
import com.inter.trade.ui.fragment.telephone.util.MoblieRechangeRecordData;
import com.inter.trade.util.LoginUtil;

/**
 * 购买汇通卡
 * 银联交易-获取订单交易流水号解析
 * 
 * @author zhichao.huang
 *
 */
public class OrderRequestParser extends NetParser<String> {

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					
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
					
					else if(data.mKey.equals("orderfucardno")){//银行卡号
						serializer.startTag("", "orderfucardno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "orderfucardno");
					}
					else if(data.mKey.equals("orderfucardbank")){//发卡银行
						serializer.startTag("", "orderfucardbank");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "orderfucardbank");
					}
					else if(data.mKey.equals("ordermemo")){//订单备注
						serializer.startTag("", "ordermemo");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "ordermemo");
						
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

	/**
	 * 解析响应体的 List<ProtocolData> 集合
	 */
	@Override
	public ArrayList<String> parserResponseDatas(List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<String> responseDatas = new ArrayList<String>();
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
				
				List<ProtocolData> orderId = data.find("/bkntno");
				if(orderId != null){
					responseDatas.add(orderId.get(0).mValue);
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					MoblieRechangeRecordData recordData = new MoblieRechangeRecordData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("rechamoney")){
									recordData.rechamoney  = item.mValue;
									
								}else if(item.mKey.equals("rechapaymoney")){
									recordData.rechapaymoney  = item.mValue;
									
								}else if(item.mKey.equals("rechamobile")){
									
									recordData.rechamobile  = item.mValue;
									
								}else if(item.mKey.equals("rechamobileprov")){
									
									recordData.rechamobileprov  = item.mValue;
									
								}else if(item.mKey.equals("rechabkcardno")){
									
									recordData.rechabkcardno  = item.mValue;
									
								}else if(item.mKey.equals("rechadatetime")){
									
									recordData.rechadatetime  = item.mValue;
								}else if(item.mKey.equals("rechastate")){
									
									recordData.rechastate  = item.mValue;
								}
								/**else if(item.mKey.equals("allmoney")){
									
									picData.allmoney  = item.mValue;
								}else if(item.mKey.equals("huancardbank")){
									
									picData.huancardbank  = item.mValue;
								}else if(item.mKey.equals("fucardbank")){
									
									picData.fucardbank  = item.mValue;
								}*/
							}
						}
					}
					
//					responseDatas.add(recordData);
				}
				
			}
		}
		return responseDatas;
	}
	
}
