package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;


/**
 * 注册资料登记
 * @author apple
 *
 */
public class AuthorRegParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//注册帐号
					if(data.mKey.equals("aumobile")){
						serializer.startTag("", "aumobile");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aumobile");
						//登录密码
					}else if(data.mKey.equals("aupassword")){
						serializer.startTag("", "aupassword");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aupassword");
					}
					//真实姓名
					else if(data.mKey.equals("autruename")){
						serializer.startTag("", "autruename");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "autruename");
						//身份证号
					}else if(data.mKey.equals("auidcard")){
						serializer.startTag("", "auidcard");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auidcard");
						//邮件地址
					}else if(data.mKey.equals("auemail")){
						serializer.startTag("", "auemail");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auemail");
					}
				}
			}
		}
	}

	@Override
	public void requestheaderToXml(ProtocolData header, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (header.mChildren != null && header.mChildren.size() > 0) {
			Set<String> keys = header.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = header.mChildren.get(key);
				for(ProtocolData data: rs){
					//密钥
					if(data.mKey.equals("req_key")){
						serializer.startTag("", "req_key");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_key");
					}
					//授权码
					if(data.mKey.equals("req_token")){
						serializer.startTag("", "req_token");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_token");
					}
					//请求时间
					if(data.mKey.equals("req_time")){
						serializer.startTag("", "req_time");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_time");
					}
					//请求流水
					if(data.mKey.equals("req_seq")){
						serializer.startTag("", "req_seq");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_seq");
					}
					//验证码
					if(data.mKey.equals("auth_ seq")){
						serializer.startTag("", "auth_ seq");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auth_ seq");
					}
					//mac校验码
					if(data.mKey.equals("mac ")){
						serializer.startTag("", "mac ");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "mac ");
					}
					
					if(data.mKey.equals("channelinfo")){
						serializer.startTag("", "channelinfo");
						Set<String> apiSet = data.mChildren.keySet();
						for(String key1 :apiSet){
							List<ProtocolData> apiDatas = data.mChildren.get(key1);
							for(ProtocolData item : apiDatas){
								if(item.mKey.equals("authorid")){
									serializer.startTag("", "authorid ");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "authorid ");
								}
								if(item.mKey.equals("api_name")){
									serializer.startTag("", "api_name");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "api_name");
								}
								if(item.mKey.equals("api_name_func")){
									serializer.startTag("", "api_name_func");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "api_name_func");
								}
							}
						}
						serializer.endTag("", "channelinfo");
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
				}else if (parser.getName().compareTo("smsmobile") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("smsmobile", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("smscode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("smscode", temp);
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
