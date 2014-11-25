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
public class BuyLicenseKeyModifyParser extends NetParser<BuyLicenseKeyData>{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("paycardid")){
						serializer.startTag("", "paycardid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "paycardid");
						
					}else if(data.mKey.equals("IMEI")){
						serializer.startTag("", "IMEI");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "IMEI");
						
					}else if(data.mKey.equals("aupwd")){
						serializer.startTag("", "aupwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aupwd");
						
					}else if(data.mKey.equals("machineno")){
						serializer.startTag("", "machineno");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "machineno");
						
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
				if (parser.getName().compareTo("paycardIMEI") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("paycardIMEI", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("hotelCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("hotelCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("hotelName") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("hotelName", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("imageUrl") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("imageUrl", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("address") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("address", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("starRate") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("starRate", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("ctripRate") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("ctripRate", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("minPrice") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("minPrice", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("description") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("description", temp);
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
