package com.inter.trade.ui.func;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.body.NetParser;


/**
 * APP功能模块菜单读取
 * @author apple
 *
 */
public class AppResponseParser extends NetParser{

	@Override
	public void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
		if (body.mChildren != null && body.mChildren.size() > 0) {
			Set<String> keys = body.mChildren.keySet();
			for(String key:keys){
				List<ProtocolData> rs = body.mChildren.get(key);
				for(ProtocolData data: rs){
					if(data.mKey.equals("msgchild")){
						serializer.startTag("", "msgchild");
						Set<String> apiSet = data.mChildren.keySet();
						for(String key1 :apiSet){
							List<ProtocolData> apiDatas = data.mChildren.get(key1);
							for(ProtocolData item : apiDatas){
								if(item.mKey.equals("appruleid")){
									serializer.startTag("", "appruleid");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "appruleid");
								}
								if(item.mKey.equals("mnuname")){
									serializer.startTag("", "mnuname");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "mnuname");
								}
								if(item.mKey.equals("mnupic")){
									serializer.startTag("", "mnupic");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "mnupic");
								}
								if(item.mKey.equals("mnuorder")){
									serializer.startTag("", "mnuorder");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "mnuorder");
								}
								if(item.mKey.equals("mnuurl")){
									serializer.startTag("", "mnuurl");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "mnuurl");
								}
								if(item.mKey.equals("mnuid")){
									serializer.startTag("", "mnuid");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "mnuid");
								}
							}
						}
						serializer.endTag("", "msgchild");
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
				}else if (parser.getName().compareTo("isnew") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("isnew", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("version") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("version", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("msgchild") == 0) {
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
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("mnuname") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mnuname", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("mnupic") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mnupic", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("mnuorder") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mnuorder", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("mnuurl") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mnuurl", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("mnuid") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("mnuid", temp);
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
}
