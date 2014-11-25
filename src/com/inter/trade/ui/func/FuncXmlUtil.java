package com.inter.trade.ui.func;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.PayApp;

public class FuncXmlUtil {
	public static final String FUNC_XML="easypay.xml";
	
	public static String getFuncXml(){
		return PayApp.pay.getFilesDir().getPath()+"/"+FUNC_XML;
	}
	/**
	 * 将string写到指定目录中
	 * @param path
	 * @param content
	 */
	public static void writeToXml(String path,String content){
		if(null == content){
			return;
		}
		Logger.d("parser",content);
		File file = new File(path);
		if(file.exists()){
			file.delete();
		}
		FileOutputStream fileOutputStream=null;
		InputStream inputStream = null;
		try {
			fileOutputStream= new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			inputStream = new ByteArrayInputStream(content.getBytes());
			int len ;
			while((len = inputStream.read(buffer)) >0){
				fileOutputStream.write(buffer,0,len);
			}
			fileOutputStream.flush();
			
		} catch (Exception e) {
			// TODO: handle exception
			Logger.e(e);
		}finally{
			try {
				if(fileOutputStream != null)
				{
					fileOutputStream.close();
				}
				if(inputStream != null){
					inputStream.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
				Logger.e(e2);
			}
		}
	}
	
	/***
	 * 读取本地xml,该xml必须符合网络协议规范
	 * 
	 * @param f
	 * @return
	 */
	public static ProtocolRsp readXML(String path) {
		File f = new File(path);
		ProtocolRsp rsp = null;
		if (f == null || !f.exists()) {
			return null;
		}
		try {
			InputStream inp = new FileInputStream(f);
			if (inp != null) {
				ProtocolParser parser = ProtocolParser.instance();
				ExtendListParser authorRegParser = new ExtendListParser();
				parser.setParser(authorRegParser);
				rsp = parser.parserProtocol(inp);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rsp;
	}
}
