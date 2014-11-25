package com.inter.trade.ui.fragment.checking.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;


/**
 * 设置密保问题
 * @author zhichao.huang
 *
 */
public class PwdSafetySettingParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//旧密码
					if(data.mKey.equals("auoldpwd")){
						serializer.startTag("", "auoldpwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auoldpwd");
					}
					//新密码
					else if(data.mKey.equals("aunewpwd")){
						serializer.startTag("", "aunewpwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aunewpwd");
					}
					//修改支付类型;1：密码 2 支付密码
					else if(data.mKey.equals("aumoditype")){
						serializer.startTag("", "aumoditype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aumoditype");
						
					}
					//reset 是否重置密码;如果为1，则无视旧密码，直接设置新密码
					else if(data.mKey.equals("reset")){
						
						serializer.startTag("", "reset");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "reset");
					}
					
					
					else if(data.mKey.equals("queid")){//问题id

						serializer.startTag("", "queid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "queid");
					}else if(data.mKey.equals("answer")){//答案

						serializer.startTag("", "answer");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "answer");
					}
					
					else if(data.mKey.equals("msgchild1")) {
						serializer.startTag("", "msgchild1");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "msgchild1");
					}else if(data.mKey.equals("msgchild2")) {
						serializer.startTag("", "msgchild2");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "msgchild2");
					}else if(data.mKey.equals("msgchild3")) {
						serializer.startTag("", "msgchild3");
						requestBodyToXml(data, serializer);
						serializer.endTag("", "msgchild3");
					}
				}
			}
		}
	}

	
//	public void requestBodyToXml() {
//		
//	}

	
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
				}else if (parser.getName().compareTo("authorid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("authorid", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("ispaypwd") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("ispaypwd", temp);
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
