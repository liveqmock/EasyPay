package com.inter.protocol.body;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.inter.protocol.ProtocolData;


/**
 * 读取用户信息
 * @author apple
 *
 */
public class AuthorInfoParser extends NetParser{

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
				if (parser.getName().compareTo("autruename") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("autruename", temp);
						res.addChild(r);
					}
					//身份证
				}else if (parser.getName().compareTo("autrueidcard") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("autrueidcard", temp);
						res.addChild(r);
					}
				}
				//邮件地址
				else if (parser.getName().compareTo("auemail") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("auemail", temp);
						res.addChild(r);
					}
				}
				//手机号码
				else if (parser.getName().compareTo("aumobile") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("aumobile", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("result") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("result", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("message") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("message", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("msgchild") == 0) {
						ProtocolData r = new ProtocolData("msgchild", null);
						r.addChild(parserPic(parser,r));
						res.addChild(r);
				}
				//代理商基本用户信息
				//代理商公司名
				else if (parser.getName().compareTo("agentcompany") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentcompany", temp);
						res.addChild(r);
					}
				}
				//归属地
				else if (parser.getName().compareTo("agentarea") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentarea", temp);
						res.addChild(r);
					}
				}
				//地址
				else if (parser.getName().compareTo("agentaddress") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentaddress", temp);
						res.addChild(r);
					}
				}
				
				//联系电话
				else if (parser.getName().compareTo("agentmanphone") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentmanphone", temp);
						res.addChild(r);
					}
				}
				//传真
				else if (parser.getName().compareTo("agentfax") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentfax", temp);
						res.addChild(r);
					}
				}
				//合同时间
				else if (parser.getName().compareTo("agenthttime") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agenthttime", temp);
						res.addChild(r);
					}
				}
				//保证金金额
				else if (parser.getName().compareTo("agentbzmoney") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("agentbzmoney", temp);
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
				if (parser.getName().compareTo("msgchild") == 0) {
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
