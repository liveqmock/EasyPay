package com.inter.trade.ui.fragment.buylicensekey.util;

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
import com.inter.trade.ui.fragment.buylicensekey.data.BuyLicenseKeyData;
import com.inter.trade.util.LoginUtil;

/**
 * 购买授权码 
 * @author haifengli
 *
 */
public class BuyLicenseKeyParser extends NetParser<BuyLicenseKeyData>{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("paycardtype")){
						serializer.startTag("", "paycardtype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardtype");
						
					}else if(data.mKey.equals("IDtype")){
						serializer.startTag("", "IDtype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "IDtype");
						
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
				}else if (parser.getName().compareTo("msgallcount") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("msgallcount", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("msgdiscount") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("msgdiscount", temp);
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
				if (parser.getName().compareTo("paycardIMEI") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("paycardIMEI", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("paycardid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("paycardid", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("paycardmachineno") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("paycardmachineno", temp);
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
	public ArrayList<BuyLicenseKeyData> parserResponseDatas(
			List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<BuyLicenseKeyData> responseDatas = new ArrayList<BuyLicenseKeyData>();
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
				
				int mTotalCount, mLoadedCount;
				List<ProtocolData> msgallcount = data.find("/msgallcount");
				if(msgallcount != null){
					mTotalCount = Integer.parseInt(msgallcount.get(0).mValue.trim());
					if(bundle != null) {
						bundle.putInt("mTotalCount", mTotalCount);//把订单总条数放到bundle
					}
				}
				List<ProtocolData> msgdiscount = data.find("/msgdiscount");
				if(msgdiscount != null){
					mLoadedCount  = Integer.parseInt(msgdiscount.get(0).mValue.trim());
					if(bundle != null) {
						bundle.putInt("mLoadedCount", mLoadedCount);//把当前加载的总条数放到bundle
					}
				}
				
				List<ProtocolData> aupic = data.find("/msgchild");
				if(aupic!=null)
				for(ProtocolData child:aupic){
					BuyLicenseKeyData recordData = new BuyLicenseKeyData();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("paycardIMEI")){
									recordData.setPaycardIMEI(item.mValue);
									
								}
								else if(item.mKey.equals("paycardid")){
									recordData.setLicenseKey(item.mValue);
									
								}
								else if(item.mKey.equals("paycardmachineno")){
									recordData.setPaycardmachineno(item.mValue);
									
								}
//								else if(item.mKey.equals("hotelName")){
//									
//									recordData.hotelName  = item.mValue;
//									
//								}else if(item.mKey.equals("imageUrl")){
//									
//									recordData.imageUrl  = item.mValue;
//									
//								}else if(item.mKey.equals("address")){
//									
//									recordData.address  = item.mValue;
//									
//								}else if(item.mKey.equals("starRate")){
//									
//									recordData.starRate  = item.mValue;
//								}else if(item.mKey.equals("ctripRate")){
//									
//									recordData.ctripRate  = item.mValue;
//								}
//								else if(item.mKey.equals("minPrice")){
//									
//									recordData.minPrice  = item.mValue;
//								}
//								else if(item.mKey.equals("description")){
//									
//									recordData.description  = item.mValue;
//								}
							}
						}
					}
					
					responseDatas.add(recordData);
				}
				
			}
		}
		return responseDatas;
	}
	
}
