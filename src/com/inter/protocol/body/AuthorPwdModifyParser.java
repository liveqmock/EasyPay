package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;


/**
 * 修改用户密码
 * @author apple
 *
 */
public class AuthorPwdModifyParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//内容
					if(data.mKey.equals("authorid")){
						serializer.startTag("", "authorid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "authorid");
						
					}else if(data.mKey.equals("auoldpwd")){
						serializer.startTag("", "auoldpwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auoldpwd");
						
					}else if(data.mKey.equals("aunewpwd")){
						serializer.startTag("", "aunewpwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aunewpwd");
						
					}else if(data.mKey.equals("aurenewpwd")){
						serializer.startTag("", "aurenewpwd");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aurenewpwd");
						
					}else if(data.mKey.equals("aumoditype")){
						serializer.startTag("", "aumoditype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aumoditype");
					}
					else if(data.mKey.equals("aumobile")){
						serializer.startTag("", "aumobile");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "aumobile");
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
				//姓名
				if (parser.getName().compareTo("result") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("result", temp);
						res.addChild(r);
					}
					//身份证
				}else if (parser.getName().compareTo("message") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("message", temp);
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
	
	
	/**
	 * 解析图片
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ProtocolData parserPic(XmlPullParser parser, ProtocolData res)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
			//自增长ID
			if (parser.getName().compareTo("picid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("picid", temp);
						res.addChild(r);
					}
				}
			//图片类型
				else if (parser.getName().compareTo("pictype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("pictype", temp);
						res.addChild(r);
					}
				}
			//图片地址
				else if (parser.getName().compareTo("picpath") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("picpath", temp);
						res.addChild(r);
					}
				}
			//管理图片类型
				else if (parser.getName().compareTo("uploadpictype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("uploadpictype", temp);
						res.addChild(r);
					}
				}
			//存放地址
				else if (parser.getName().compareTo("uploadurl") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("uploadurl", temp);
						res.addChild(r);
					}
				}
			//上传方式
				else if (parser.getName().compareTo("uploadmethod") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("uploadmethod", temp);
						res.addChild(r);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("aupic") == 0) {
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
