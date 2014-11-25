package com.inter.trade.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 手机号码归属地SAX解析
 * @see http://life.tenpay.com/cgi-bin/mobile/MobileQueryAttribution.cgi?chgmobile=18102786610
 * @author Administrator
 *
 */
public class MobileQueryAttributionHandler extends DefaultHandler {
	
	private String tag = null;
	
	private MobileQueryAttributionHandler mobileHandler;
	
	private MobileAttribution mobileAttribution;
	
	public MobileQueryAttributionHandler () {
		mobileHandler = this;
	}
	
	public MobileAttribution getMobileAttribution() {
		return mobileAttribution;
	}
	
	public MobileAttribution parser(InputStream inStream, String charsetName) {
		try {
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
    		SAXParser parser = factory.newSAXParser();
    		XMLReader xmlReader = parser.getXMLReader();
    		
    		xmlReader.setContentHandler(mobileHandler);
    		InputStreamReader streamReader = null ;
    		InputSource is = null ;
//    		if("GBK".equals(EncodingUtil.checkEncoding(url)))
//    		{
//    			//设置编码格式
//    			streamReader = new InputStreamReader(url.openStream(),"GBK");
//        		is = new InputSource(streamReader);
//    		}
//    		else
//    		{
//    			is = new InputSource(url.openStream());
//    			//is.setEncoding("utf-8");
//    		}
    		streamReader = new InputStreamReader(inStream,charsetName);
    		is = new InputSource(streamReader);
    		xmlReader.parse(is);
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mobileHandler.getMobileAttribution();
	}
	
	@Override
	public void startDocument() throws SAXException {
		mobileAttribution = new MobileAttribution();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		tag = qName ;
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		
		if(tag != null)
		{
			String data = new String(ch,start,length);
			
			if("chgmobile".equals(tag))
			{
				mobileAttribution.setChgmobile(data);
				//return;
			}
			
			if("city".equals(tag))
			{
				mobileAttribution.setCity(data);
				//return;
			}
			
			if("province".equals(tag))
			{
				mobileAttribution.setProvince(data);
				//return;
			}
			
			if("retcode".equals(tag))
			{
				mobileAttribution.setRetcode(data);
				//return;
			}
			
			if("retmsg".equals(tag))
			{
				mobileAttribution.setRetmsg(data);
				//return;
			}
			
			if("supplier".equals(tag))
			{
				mobileAttribution.setSupplier(data);
				//return;
			}
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		tag = null ;//目的是防止把空格读进来
	}

//	@Override
//	public void endDocument() throws SAXException {
//		
//	}

}
