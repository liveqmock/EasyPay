package com.inter.trade.ui.fragment.agent.util;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;

/**
 * 代理商在线申请，提交信息,数据解析
 * @author Lihaifeng
 *
 */
public class AgentApplySumitParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("custypeid")){
						serializer.startTag("", "custypeid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "custypeid");
							
					}else if(data.mKey.equals("name")){
						serializer.startTag("", "name");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "name");
							
					}else if(data.mKey.equals("phone")){
						serializer.startTag("", "phone");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "phone");
						
					}else if(data.mKey.equals("agentcode")){
						serializer.startTag("", "agentcode");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "agentcode");
						
					}else if(data.mKey.equals("address")){
						serializer.startTag("", "address");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "address");
						
					}else if(data.mKey.equals("prov")){
						serializer.startTag("", "prov");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "prov");
						
					}else if(data.mKey.equals("city")){
						serializer.startTag("", "city");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "city");
							
					}else if(data.mKey.equals("town")){
						serializer.startTag("", "town");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "town");
						
					}else if(data.mKey.equals("upfileinfo")){
						serializer.startTag("", "upfileinfo");
						
						Set<String> apiSet = data.mChildren.keySet();
						for(String key1 :apiSet){
							List<ProtocolData> apiDatas = data.mChildren.get(key1);
							for(ProtocolData item : apiDatas){
								if(item.mKey.equals("picpath")){
									
									serializer.startTag("", "picpath");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "picpath");
									
								}else if(item.mKey.equals("pictypeid")){
									
									serializer.startTag("", "pictypeid");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "pictypeid");
									
								}else if(item.mKey.equals("pictypename")){
									
									serializer.startTag("", "pictypename");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "pictypename");
								}
							}
						}
						serializer.endTag("", "upfileinfo");
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
