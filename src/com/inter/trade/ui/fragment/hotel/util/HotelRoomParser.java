package com.inter.trade.ui.fragment.hotel.util;

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
import com.inter.trade.data.BaseData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.ui.fragment.hotel.data.HotelRoomData;
import com.inter.trade.util.LoginUtil;

/**
 * 酒店房型 数据解析
 * @author haifengli
 *
 */
public class HotelRoomParser extends NetParser<HotelRoomData>{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("authorid")){
						serializer.startTag("", "authorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "authorid");
						
					}else if(data.mKey.equals("msgstart")){
						serializer.startTag("", "msgstart");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "msgstart");
						
					}else if(data.mKey.equals("msgdisplay")){
						serializer.startTag("", "msgdisplay");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "msgdisplay");
						
					}
					//酒店代码
					else if(data.mKey.equals("hotelCode")){
						serializer.startTag("", "hotelCode");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "hotelCode");
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
				if (parser.getName().compareTo("code") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("code", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("name") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("name", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("resident") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("resident", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("bedSize") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("bedSize", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("price") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("price", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("priceCode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("priceCode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("roomImage") == 0) {
					ProtocolData r = new ProtocolData("roomImage", null);
					parserHelpItem2(parser, r);
					parent.addChild(r);
				}
				break;
			}
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
		
	private  void parserHelpItem2(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("msgchild") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("msgchild", temp);
						parent.addChild(data);
					}
				}
				break;
			}
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("roomImage") == 0) {
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
	public ArrayList<HotelRoomData> parserResponseDatas(
			List<ProtocolData> mDatas, Bundle bundle) {
		ArrayList<HotelRoomData> responseDatas = new ArrayList<HotelRoomData>();
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
					HotelRoomData recordData = new HotelRoomData();
					recordData.imageUrls = new HotelRoomData().imageUrls;
					List<String> imageList = new ArrayList<String>();
					if (child.mChildren != null && child.mChildren.size() > 0) {
						Set<String> keys = child.mChildren.keySet();
						for(String key:keys){
							List<ProtocolData> rs = child.mChildren.get(key);
							for(ProtocolData item: rs){
								if(item.mKey.equals("code")){
									recordData.code  = item.mValue;
									
								}else if(item.mKey.equals("name")){
									recordData.name  = item.mValue;
									
								}else if(item.mKey.equals("resident")){
									recordData.resident  = item.mValue;
									
								}else if(item.mKey.equals("bedSize")){
									recordData.bedSize  = item.mValue;
									
								}else if(item.mKey.equals("price")){
									recordData.price  = item.mValue;
									
								}else if(item.mKey.equals("priceCode")){
									recordData.priceCode  = item.mValue;
									
								}else if(item.mKey.equals("roomImage")){
//									recordData.bedSize  = item.mValue;
									
									if (item.mChildren != null && item.mChildren.size() > 0) {
										Set<String> keys2 = item.mChildren.keySet();
										for(String key2:keys2){
											List<ProtocolData> rs2 = item.mChildren.get(key2);
											for(ProtocolData item2: rs2){
												if(item2.mKey.equals("msgchild")){
													imageList.add(item2.mValue);
												}
											}
										}
									}
									
								}
							}
						}
					}
					if(imageList != null ){
						final int size = imageList.size();
						recordData.imageUrls = (String[])imageList.toArray(new String[size]);
					}
					responseDatas.add(recordData);
				}
				
			}
		}
		return responseDatas;
	}
	
}
