package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.trade.log.Logger;


/**
 * 版本更新接口
 * @author apple
 *
 */
public class VersionParser extends NetParser{

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
					if(data.mKey.equals("apptype")){
						serializer.startTag("", "apptype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "apptype");
						
					}else if(data.mKey.equals("appversion")){
						serializer.startTag("", "appversion");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "appversion");
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
				}else if (parser.getName().compareTo("apptype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("apptype", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("appnewversion") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("appnewversion", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("appisnew") == 0) {
					String temp = parser.nextText();
					Logger.d("VersionTask", "parser appisnew"+temp);
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("appisnew", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("clearoldinfo") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("clearoldinfo", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("appdownurl") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("appdownurl", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("appnewcontent") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("appnewcontent", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("appstrupdate") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("appstrupdate", temp);
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
