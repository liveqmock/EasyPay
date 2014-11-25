package com.inter.protocol.body;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.Bundle;

import com.inter.protocol.ProtocolData;

public abstract class NetParser <T>{
	//请求body体转化为xml
	public  abstract void requestBodyToXml(ProtocolData body, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException;
	//解析xml，转化为ProtocolData
	public abstract ProtocolData parserBody(XmlPullParser parser)
			throws XmlPullParserException, IOException;
	/**
	 * 解析响应体的 List<ProtocolData> 集合,@param bundle 用于存储附带信息，如（下拉加载）加载更多的字段，或数据的总条数
	 */
	public /**abstract*/ ArrayList<T> parserResponseDatas(List<ProtocolData> mDatas, Bundle bundle){return null;}
	//请求header转化为xml
	public  void requestheaderToXml(ProtocolData header, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException{
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
					if(data.mKey.equals("au_token")){
						serializer.startTag("", "au_token");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "au_token");
					}
					//验证码
					if(data.mKey.equals("req_version")){
						serializer.startTag("", "req_version");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_version");
					}
					//mac校验码
					if(data.mKey.equals("req_auth")){
						serializer.startTag("", "req_auth");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_auth");
					}
					
					if(data.mKey.equals("req_appenv")){
						serializer.startTag("", "req_appenv");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_appenv");
					}
					
					if(data.mKey.equals("req_bkenv")){
						serializer.startTag("", "req_bkenv");
						serializer.text(data.mValue.trim());
						serializer.endTag("", "req_bkenv");
					}
					
					if(data.mKey.equals("channelinfo")){
						serializer.startTag("", "channelinfo");
						Set<String> apiSet = data.mChildren.keySet();
						for(String key1 :apiSet){
							List<ProtocolData> apiDatas = data.mChildren.get(key1);
							for(ProtocolData item : apiDatas){
								if(item.mKey.equals("authorid")){
									serializer.startTag("", "authorid");
									serializer.text(item.mValue.trim());
									serializer.endTag("", "authorid");
								}
								if(item.mKey.equals("agentid")){//代理商ID
									serializer.startTag("", "agentid");
									serializer.text(item.mValue==null?"":item.mValue.trim());
									serializer.endTag("", "agentid");
								}
								if(item.mKey.equals("agenttypeid")){//代理商类型ID
									serializer.startTag("", "agenttypeid");
									serializer.text(item.mValue==null?"":item.mValue.trim());
									serializer.endTag("", "agenttypeid");
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
	
	/**
	 * 解析header头部信息，转化为ProtocolData
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	public ProtocolData parserHeader(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		ProtocolData res = new ProtocolData("msgheader", null);
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("req_seq") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("req_seq", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("ope_seq") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("ope_seq", temp);
						res.addChild(r);
					}
				}else if (parser.getName().compareTo("mac") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("mac", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("req_bkenv") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("req_bkenv", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("au_token") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("au_token", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("req_token") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("req_token", temp);
						res.addChild(r);
					}
				}
				else if (parser.getName().compareTo("retinfo") == 0) {
//					String temp = parser.nextText();
//					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData("retinfo", null);
						parserRetinfo(parser, r);
						res.addChild(r);
//					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("msgheader") == 0) {
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
	private  void parserRetinfo(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("rettype") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("rettype", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("retcode") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("retcode", temp);
						parent.addChild(data);
					}
				}else if (parser.getName().compareTo("retmsg") == 0) {
					String temp = parser.nextText();
					if (temp != null && temp.length() > 0) {
						ProtocolData data = new ProtocolData("retmsg", temp);
						parent.addChild(data);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("retinfo") == 0) {
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
