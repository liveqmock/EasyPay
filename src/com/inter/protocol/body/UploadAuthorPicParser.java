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
public class UploadAuthorPicParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					//自增长id
					if(data.mKey.equals("picid")){
						serializer.startTag("", "picid");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "picid");
					}
					
					//图片地址
					else if(data.mKey.equals("picpath")){
						serializer.startTag("", "picpath");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "picpath");
					}
					
					//上传动作
					else if(data.mKey.equals("uploadmethod")){
						serializer.startTag("", "uploadmethod");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "uploadmethod");
					}
					//管理图片类型
					else if(data.mKey.equals("uploadpictype")){
						serializer.startTag("", "uploadpictype");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "uploadpictype");
					}
					//上传成功标识
					else if(data.mKey.equals("uploadmark")){
						serializer.startTag("", "uploadmark");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "uploadmark");
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
