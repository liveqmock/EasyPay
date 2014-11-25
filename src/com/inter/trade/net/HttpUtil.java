package com.inter.trade.net;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;

import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.protocol.ProtocolParser;
import com.inter.protocol.ProtocolRsp;
import com.inter.protocol.body.NetParser;
import com.inter.trade.log.Logger;
import com.inter.trade.util.EncryptUtil;

public class HttpUtil {
//	public static final String SERVER_ADDRESS = "http://14.18.207.56/tfb_test/sever/androidapi.php";//测试环境
//	public static final String SERVER_ADDRESS = "http://14.18.207.56/mobilepay/sever/androidapi.php";//正式环境
//	public static final String SERVER_ADDRESS = "http://14.18.205.153/tfb_test/sever/getapi.php";//测试环境(IP地址)
	
	public static final String SERVER_ADDRESS = "http://www.tfbpay.cn/tfb_test/sever/getapi.php";//测试环境
//	public static final String SERVER_ADDRESS = "http://www.tfbpay.cn/mobilepay/sever/getapi.php";///正式环境
	
//	public static final String SERVER_ADDRESS = "http://192.168.1.135/mobilepay/sever/androidapi.php";///demo环境
	
//	public static final String SERVER_ADDRESS = "http://182.92.194.76/mobilepay/sever/getapi.php";//阿里云服务器
//	public static final String SERVER_ADDRESS = "http://www.tfbpay.cn/mobilepay2/sever/getapi.php";///正式版临时接口地址
	
//	public static final String SERVER_ADDRESS = "http://192.168.0.112:8080/test/servlet/HelloWorldServlet";//本地服务器,需要将下面的isDecode改为false以及将端口号改为8080
	
//	public  static ProtocolRsp  getRequest(String content,NetParser parser){
//		ProtocolRsp rsp = null;
//		try {
//			
//			byte[] data =content.getBytes();
//			SunHttpApi httpApi = new SunHttpApi(SunHttpApi.createHttpClient(), "1");
//			HttpPost post = httpApi.createHttpPost(SERVER_ADDRESS);
//			post.setEntity(new InputStreamEntity(new ByteArrayInputStream(data), data.length));
////			post.setEntity(new StringEntity(news));
//			HttpResponse response = httpApi.executeHttpRequest(post);
//			InputStream inputStream = response.getEntity().getContent();
//			
//			ProtocolParser.instance().setParser(parser);
//			rsp = ProtocolParser.instance().parserProtocol(inputStream);
//		} catch (Exception e) {
//			// TODO: handle exception
//			e.printStackTrace();
//		}
//		return rsp;
//	}
	
	/***
	 * 是否加解密
	 */
	private static boolean isDecode=true;
	
	/**
	 * 加解密的网络请求
	 * @param content
	 * @param parser
	 * @return
	 */
	public  static synchronized ProtocolRsp  getRequest(String content,NetParser parser){
		ProtocolRsp rsp = null;
		try {
//			byte[] data =content.getBytes();
//			content = "<?xml version='1.0' encoding='UTF-8' standalone='yes' ?><operation_request><msgheader version="+"1.0><channelinfo><api_name_func>checkAuthorLogin</api_name_func><api_name>ApiAuthorInfo</api_name></channelinfo></msgheader><msgbody><aumobile>18664676691</aumobile><aupwd>111111</aupwd></msgbody></operation_request>";
//		Logger.d("HttpUtil","加密前："+ content);
//			Log.i("LogHttpUtilTEST","请求报文(加密前)：\n"+ content);
			int index = EncryptUtil.createRandomkeySort();
//			Logger.d("HttpUtil","密钥索引："+ index);
//			Log.i("LogHttpUtilTEST","密钥索引："+ index);
			String data="";
			if(isDecode){
				data = EncryptUtil.encrypt(content, index);
			}else{
				data=content;
			}
//			Logger.d("HttpUtil","加密后："+ data);
//			Logger.d("HttpUtil","解密后："+ EncryptUtil.decrypt(data, index));
//			Log.i("LogHttpUtilTEST","加密后："+ data);
//			Log.i("LogHttpUtilTEST","解密后："+ EncryptUtil.decrypt(data, index));
			byte[] data1 =data.getBytes();
			byte[] secur = new byte[data1.length+1];
			secur[0]=String.valueOf(index).getBytes()[0];
			System.arraycopy(data1, 0, secur, 1, data1.length);
//			Logger.d("HttpUtil","所有报文："+ new String(secur));
//			Log.i("LogHttpUtilTEST","所有报文："+ new String(secur));
			
			SunHttpApi httpApi = new SunHttpApi(SunHttpApi.createHttpClient(), "1");
			HttpPost post = httpApi.createHttpPost(SERVER_ADDRESS);
			post.setEntity(new InputStreamEntity(new ByteArrayInputStream(secur), secur.length));
//			post.setEntity(new StringEntity(news));
			HttpResponse response = httpApi.executeHttpRequest(post);
			InputStream inputStream = response.getEntity().getContent();
			
			byte[] result = IOUtil.fromInputTobyte(inputStream);
			
			byte u  = -17;
			byte t = -69;
			byte f = -65;
			
			
			byte[] parms=null;
			if(result[0]==u && result[1]==t && result[2]==f){
				parms= new byte[result.length-3];
				System.arraycopy(result,3, parms, 0, parms.length);
			}
			String test;
			if(null != parms && parms.length>0){
				test = new String(parms);
			}else{
				test = new String (result);
			}
			
//			Logger.d("HttpUtil", "response\n"+test);
//			Log.i("LogHttpUtilTEST", "response\n"+test);
//			Logger.d("HttpUtil", "response\n"+test);
//			String indexByte=new String(new byte[]{result[0]});
//			byte[] decrypt = new byte[result.length-1];
//			System.arraycopy(result, 1, decrypt, 0, decrypt.length);
//			
//			Logger.d("HttpUtil", "indexByte\n"+indexByte);
			String ecryptString = test.substring(1,test.length());
			byte[] decrypt = ecryptString.getBytes();
			String indexByte=test.substring(0,1);
			
			
			String decryptString="";
			if(isDecode){
				decryptString = EncryptUtil.decrypt(new String(decrypt), 
						Integer.parseInt(indexByte));
			}else{
				decryptString = new String(test);
			}
			
			
			
//			Logger.d("HttpUtil", "decrypt\n"+decryptString);
//			Log.i("LogHttpUtilTEST", "响应报文(解密后)：\n"+decryptString);
			ProtocolParser.instance().setParser(parser);
			rsp = ProtocolParser.instance().parserProtocol(new ByteArrayInputStream(decryptString.getBytes()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return rsp;
	}
	

	/**
	 * 网络请求方法
	 * @param parser
	 * @param datas
	 * @return
	 */
	public static synchronized ProtocolRsp doRequest(NetParser parser,List<ProtocolData> datas){
		ProtocolParser.instance().setParser(parser);
		String content = ProtocolParser.instance().aToXml(datas);
		Logger.d("HttpApi", "request\n"+content);
		return  getRequest(content, parser);
	}
	
}
