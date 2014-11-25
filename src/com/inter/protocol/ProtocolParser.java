package com.inter.protocol;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

import com.inter.protocol.body.NetParser;
import com.inter.trade.log.Logger;

/**
 * 
 * @author WuWangchun
 * @create 2012-2-28 下午2:34:42
 * @last_modify
 * @last_modify_time
 */
public class ProtocolParser {
	private   NetParser mParser = null;
	public static String news = "<?xml version="+"'1.0'"+"encoding="+"'UTF-8'"+"?>"+
			"<operation_response>" +
			"<msgheader version="+"'1.0'"+">" +
			"<req_seq>21</req_seq>" +
			"<ope_seq>21</ope_seq>" +
			"<retinfo><rettype>0</rettype><retcode>0</retcode><retmsg>成功</retmsg></retinfo>" +
			"</msgheader>" +
			"<msgbody>" +
			"<result>success</result>" +
			"<message>短信发送成功</message>" +
			"<smsmobile></smsmobile>" +
			"<smscode>295104</smscode>" +
			"</msgbody>" +
			"</operation_response>";
	private static  ProtocolParser mProtocolParser=null;
	public void setParser(NetParser params){
		mParser = params;
	}
	private  ProtocolParser(){
	}
	public static ProtocolParser instance(){
		if(mProtocolParser==null){
			mProtocolParser = new ProtocolParser();
		}
		return mProtocolParser;
	}
	 public ProtocolRsp parserProtocol(InputStream in) {
		if (in == null) {
			return null;
		}
		in = printInputStream(in);
		 
//		 in = new ByteArrayInputStream(news.getBytes());
		ProtocolRsp rep = new ProtocolRsp();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(in, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT: {

				}
					break;
				case XmlPullParser.START_TAG: {
					if(parser.getName().compareTo("msgheader") == 0){
						ProtocolData header = mParser.parserHeader(parser);
						rep.mActions.add(header);
						
					}else if(parser.getName().compareTo("msgbody") == 0){
						
						ProtocolData body = mParser.parserBody(parser);
						rep.mActions.add(body);
					}
					
				}
					break;
				case XmlPullParser.END_DOCUMENT: {

				}
					break;
				case XmlPullParser.END_TAG: {

				}
					break;
				case XmlPullParser.TEXT: {

				}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Logger.d("parserProtocol 1:" + e.toString());
			e.printStackTrace();
		}

		return rep;
	}
	/**
	 * 
	 * @param xml
	 *            要解析的xml
	 * @param type
	 *            对应的增加类型，o ：小编推荐点击次数+1，m:mm专区点击次数+1
	 * @return
	 */

	static public String parserProtocol(String xml, String type) {
		if (xml == null || xml.equals("")) {
			return null;
		}
		ProtocolRsp rep = new ProtocolRsp();
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(xml));
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT: {

				}
					break;
				case XmlPullParser.START_TAG: {
					if (parser.getName().compareTo("a") == 0) {
						try {
							ProtocolData cell = parserCell(parser);
							if (cell != null) {
								rep.mActions.add(cell);
							}
						} catch (Exception e) {
							// TODO: handle exception
							Logger.d("parserProtocol 0:" + e.toString());
						}

					}
				}
					break;
				case XmlPullParser.END_DOCUMENT: {

				}
					break;
				case XmlPullParser.END_TAG: {

				}
					break;
				case XmlPullParser.TEXT: {

				}
					break;
				}
				eventType = parser.next();
			}
		} catch (Exception e) {
			// TODO: handle exception
			Logger.d("parserProtocol 1:" + e.toString());
		}
		List<ProtocolData> datas = ProtocolRsp.find("info", rep);
		ProtocolData rData = new ProtocolData(P.normal_r, null);
		for (ProtocolData data : datas) {
			ProtocolData oData = data.getFirstChild("o");
			if(oData != null){
				if ( type.equals("o")) {
					oData.setmValue(String.valueOf(Integer.parseInt(oData
							.getmValue()) + 1));
					rData.addChild(oData);
				} else {
					rData.addChild(oData);
				}
			}else{
				oData = new ProtocolData("o", "1");
				rData.addChild(oData);
			}
			ProtocolData mData = data.getFirstChild("m");
			if (mData != null) {
				if (type.equals("m")) {
					mData.setmValue(String.valueOf(Integer.parseInt(mData
							.getmValue()) + 1));
					rData.addChild(mData);
				}else{
					rData.addChild(mData);
				}
			} else {
				mData = new ProtocolData("m", "1");
				rData.addChild(mData);
			}

		}
		ProtocolData aData = new ProtocolData(P.a_name.lml, null);
		aData.addChild(rData);
		String temp = aToXml(aData);
		Logger.d("temp" + temp);
		return temp;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static InputStream printInputStream(InputStream in) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		try {
			while (true) {
				int len = in.read(buffer);
				if (len > 0) {
					outputStream.write(buffer, 0, len);
				} else {
					break;
				}
			}
			outputStream.flush();
			byte[] bx = outputStream.toByteArray();
			String bString = new String(bx, "UTF-8");
			Logger.d("HttpApi", "response \n"+bString);
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bx);
			return inputStream;
		} catch (Exception e) {
			// TODO: handle exception
			Logger.d("************PrintXml Error***********");
			Logger.e(e);
			return null;
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	
	
	
	/**
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ProtocolData parserCell(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		String name = parser.getAttributeValue("", "n");
		if (name == null || name.length() == 0) {
			return null;
		}
		ProtocolData res = new ProtocolData(name, null);
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("r") == 0) {
					String temp = parser.getAttributeValue("", "n");
					if (temp != null && temp.length() > 0) {
						ProtocolData r = new ProtocolData(temp, null);
						parserC(parser, r);
						res.addChild(r);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("a") == 0) {
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
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private static void parserC(XmlPullParser parser, ProtocolData parent)
			throws XmlPullParserException, IOException {
		// TODO Auto-generated method stub
		// HashMap<String, List<ProtocolData>> res = new HashMap<String,
		// List<ProtocolData>>();
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_TAG: {
				if (parser.getName().compareTo("c") == 0) {
					String temp = parser.getAttributeValue("", "n");
					String value = parser.nextText();
					if (temp != null && temp.length() > 0 && value != null) {
						ProtocolData data = new ProtocolData(temp, value);
						parent.addChild(data);
					}
				}
			}
				break;
			case XmlPullParser.END_TAG: {
				if (parser.getName().compareTo("r") == 0) {
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

	static public String aToXml(ProtocolData pc) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "req");
//			aToXML(pc, serializer);
			serializer.endTag("", "req");
			serializer.endDocument();
			serializer.flush();
			writer.flush();
			String ret = writer.toString();
			writer.close();
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	
	static public String requstToXml(ProtocolData pc) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "operation_request");
//			aToXML(pc, serializer);
			serializer.endTag("", "operation_request");
			serializer.endDocument();
			serializer.flush();
			writer.flush();
			String ret = writer.toString();
			writer.close();
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}
	

	static public String aToXml(ProtocolData[] pc) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "req");
			for (ProtocolData protocolCell : pc) {
//				aToXML(protocolCell, serializer);
			}
			serializer.endTag("", "req");
			serializer.endDocument();
			serializer.flush();
			writer.flush();
			String ret = writer.toString();
			writer.close();
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}

	}

	 public String aToXml(List<ProtocolData> pc) {
		try {
			XmlSerializer serializer = Xml.newSerializer();
			StringWriter writer = new StringWriter();
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			serializer.startTag("", "operation_request");
			for (ProtocolData protocolCell : pc) {
				aToXML(protocolCell, serializer);
			}
			serializer.endTag("", "operation_request");
			serializer.endDocument();
			serializer.flush();
			writer.flush();
			String ret = writer.toString();
			writer.close();
			return ret;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}

	}

	 private void aToXML(ProtocolData pc, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		
		if(pc.mKey.equals("msgheader")){
			serializer.startTag("", "msgheader");
			serializer.attribute("", "version", "1.0");
			mParser.requestheaderToXml(pc, serializer);
			serializer.endTag("", "msgheader");
		}else if(pc.mKey.equals("msgbody")){
			serializer.startTag("", "msgbody");
//			requestBodyToXml(pc, serializer);
//			ProtocolDispatch.dispatchBody(apiName, pc, serializer);
			mParser.requestBodyToXml(pc, serializer);
			serializer.endTag("", "msgbody");
		}
			
		

	}
	

	static private void rToXml(ProtocolData r, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "r");
		serializer.attribute("", "n", r.mKey);
		if (r.mChildren != null && r.mChildren.size() > 0) {
			Set<String> keys = r.mChildren.keySet();
			for (String key : keys) {
				List<ProtocolData> rs = r.mChildren.get(key);
				for (ProtocolData protocolData : rs) {
					cToXml(protocolData, serializer);
				}
			}
		}
		serializer.endTag("", "r");
	}

	static private void cToXml(ProtocolData c, XmlSerializer serializer)
			throws IllegalArgumentException, IllegalStateException, IOException {
		serializer.startTag("", "c");
		serializer.attribute("", "n", c.mKey);
		serializer.text(c.mValue.trim());
		serializer.endTag("", "c");
	}

	
}
