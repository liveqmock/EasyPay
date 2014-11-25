package com.inter.trade.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class IOUtil {
	
	public static String fromInputToString(InputStream inputStream){
		String content = "";
		
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len ;
			byte[] buffer = new byte[1024];
			while((len = inputStream.read(buffer))>0){
				outputStream.write(buffer, 0, len);
			}
			
			inputStream.close();
			content = new String(outputStream.toByteArray());
			outputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
		}
		
		
		return content;
	}
	
	public static byte[] fromInputTobyte(InputStream inputStream){
		
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			int len ;
			byte[] buffer = new byte[1024];
			while((len = inputStream.read(buffer))>0){
				outputStream.write(buffer, 0, len);
			}
			
			inputStream.close();
//			content = new String(outputStream.toByteArray());
//			outputStream.close();
			return outputStream.toByteArray();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
		}
		
		
		return null;
	}
}
