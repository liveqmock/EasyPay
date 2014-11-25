package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;


/**
 * 修改用户信息
 * @author apple
 *
 */
public class ModifyAuthorInfoParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//姓名
					if(data.mKey.equals("autruename")){
						serializer.startTag("", "autruename");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "autruename");
					}
					
					//身份证
					else if(data.mKey.equals("auidcard")){
						serializer.startTag("", "auidcard");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auidcard");
					}
					
					//电子邮件
					else if(data.mKey.equals("auemail")){
						serializer.startTag("", "auemail");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "auemail");
					}
					
					else if(data.mKey.equals("agentcompany")){
						serializer.startTag("", "agentcompany");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentcompany");
					}
					else if(data.mKey.equals("agentarea")){
						serializer.startTag("", "agentarea");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentarea");
					}
					else if(data.mKey.equals("agentaddress")){
						serializer.startTag("", "agentaddress");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentaddress");
					}
					else if(data.mKey.equals("agentmanphone")){
						serializer.startTag("", "agentmanphone");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentmanphone");
					}
					else if(data.mKey.equals("agentfax")){
						serializer.startTag("", "agentfax");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentfax");
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
