package com.inter.protocol.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.inter.protocol.ProtocolData;
import com.inter.trade.data.AirTicketCreateOrderData;
import com.inter.trade.data.BaseData;
import com.inter.trade.data.ResponseData;
import com.inter.trade.data.SunType;
import com.inter.trade.log.Logger;
import com.inter.trade.ui.PayApp;
import com.inter.trade.ui.fragment.agent.util.AgentApplyInfoData;
import com.inter.trade.ui.fragment.checking.util.PwdSafetySettingData;
import com.inter.trade.util.Constants;
import com.inter.trade.util.EncryptUtil;
import com.inter.trade.util.LoginUtil;
import com.inter.trade.util.PreferenceConfig;
import com.inter.trade.util.VersionUtil;
import com.unionpay.uppayplugin.demo.UnionpayUtil;


public class ProtocolUtil {
	public static final String TAG="ProtocolUtil";
	public static final String operation_request ="operation_request";
	public static final String msgheader ="msgheader";
	public static final String channelinfo ="channelinfo";
	public static final String api_name ="api_name";
	public static final String api_name_func ="api_name_func";
	public static final String msgbody ="msgbody";
	public static final String smsmobile ="smsmobile";
	
	public static final String SUCCESS ="success";
	public static final String FAIL ="failure";
	
	public static final String HEADER_SUCCESS="0";//返回正常
	public static final String REAL_ERROR="300";//时效错误码,'登录失效，请重新登录',
	public static final String ERROR_EXCUTE=	"500";//'执行代码错误',
	public static final String ERROR_CLIENT="600";// => '客户端调用错误',
	public static final String ERROR_DATABASE="700";//=> '数据库出错',
	public static final String ERROR_DELAY="900" ;// => '引用延迟',
	public static final String ERROR_PAY="800";// => '请求支付接口错误',
	public static final String ERROR_NO_POWER="400";// => '商户权限不足',
	public static final String ERROR_NO_FIND="404";// => '请求功能不存在',
	public static final String ERROR_USER_DEFINE	="200";// => '自定义错误'
	public static final String ERROR_LOGINED="301";//在异设备登录错误
	//error类型
	
	/**
	 * 创建请求头
	 * @return
	 */
	public static synchronized ProtocolData headerData(){
		ProtocolData headerData = new ProtocolData(ProtocolUtil.msgheader, null);
		
		return headerData;
	}
	
	
	/**
	 * 解析响应头
	 * @param response
	 * @param data
	 */
	public static synchronized void parserResponse(ResponseData response,ProtocolData data){
		List<ProtocolData> req_seq = data.find("/req_seq");
		if(req_seq!=null){
			response.setReq_seq(req_seq.get(0).mValue);
		}
		
		List<ProtocolData> ope_seq = data.find("/ope_seq");
		if(ope_seq!=null){
			response.setOpe_seq(ope_seq.get(0).mValue);
		}
		
		List<ProtocolData> au_token = data.find("/au_token");
		if(au_token!=null){
			response.setAu_token(au_token.get(0).mValue);
			//保存请求网络的动态码 au_token
			if(PayApp.pay != null){
				PreferenceConfig.instance(PayApp.pay).putString(Constants.AUTO_TOKEN, au_token.get(0).mValue);
			}
		}
		
		List<ProtocolData> req_bkenv = data.find("/req_bkenv");
		if(req_bkenv!=null){
			response.setReq_bkenv(req_bkenv.get(0).mValue);
			UnionpayUtil.mMode=req_bkenv.get(0).mValue;
		}
		
		List<ProtocolData> req_token = data.find("/req_token");
		if(req_token!=null){
			response.setReq_token(req_token.get(0).mValue);
			Logger.d("login", "返回的：req_token");
			printString("login",req_token.get(0).mValue);
			
			//保存请求网络的动态码 au_token
			if(PayApp.pay != null){
				PreferenceConfig.instance(PayApp.pay).putString(Constants.REQ_TOKEN, req_token.get(0).mValue);
			}
		}
		
		List<ProtocolData> rettype = data.find("/retinfo/rettype");
		if(rettype!=null){
			response.setRettype(rettype.get(0).mValue);
		}
		
		List<ProtocolData> retcode = data.find("/retinfo/retcode");
		if(retcode!=null){
			response.setRetcode(retcode.get(0).mValue);
		}
	
		
		List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
		if(retmsg!=null){
			response.setRetmsg(retmsg.get(0).mValue);
		}
	}
	
	
	/**
	 * 解析响应头
	 * @param response
	 * @param data
	 */
	public static synchronized void parserNotoken(ResponseData response,ProtocolData data){
		List<ProtocolData> req_seq = data.find("/req_seq");
		if(req_seq!=null){
			response.setReq_seq(req_seq.get(0).mValue);
		}
		
		List<ProtocolData> ope_seq = data.find("/ope_seq");
		if(ope_seq!=null){
			response.setOpe_seq(ope_seq.get(0).mValue);
		}
		
		List<ProtocolData> au_token = data.find("/au_token");
		if(au_token!=null){
			response.setAu_token(au_token.get(0).mValue);
			//保存请求网络的动态码 au_token
			if(PayApp.pay != null){
				PreferenceConfig.instance(PayApp.pay).putString(Constants.AUTO_TOKEN, au_token.get(0).mValue);
			}
		}
		
		List<ProtocolData> req_bkenv = data.find("/req_bkenv");
		if(req_bkenv!=null){
			response.setReq_bkenv(req_bkenv.get(0).mValue);
			UnionpayUtil.mMode=req_bkenv.get(0).mValue;
		}
		
//		List<ProtocolData> req_token = data.find("/req_token");
//		if(req_token!=null){
//			response.setReq_token(req_token.get(0).mValue);
//		}
		
		List<ProtocolData> rettype = data.find("/retinfo/rettype");
		if(rettype!=null){
			response.setRettype(rettype.get(0).mValue);
		}
		
		List<ProtocolData> retcode = data.find("/retinfo/retcode");
		if(retcode!=null){
			response.setRetcode(retcode.get(0).mValue);
		}
	
		
		List<ProtocolData> retmsg = data.find("/retinfo/retmsg");
		if(retmsg!=null){
			response.setRetmsg(retmsg.get(0).mValue);
		}
	}
	
	/**
	 * 构造网络请求数据
	 * @param apiName
	 * @param apiFunc
	 * @param data
	 * @return
	 */
	public static synchronized List<ProtocolData> getRequestDatas(String apiName,String apiFunc,SunType data){
		BaseData temp =null;
		if(data instanceof BaseData){
			temp = (BaseData)data;
		}
		
		if(temp == null)
		{
			return null;
		}
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, apiName);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, apiFunc);
		if(LoginUtil.mLoginStatus.authorid != null){
			ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
			chinalData.addChild(authorid);
		}
		if(LoginUtil.mLoginStatus.agentid != null){
			ProtocolData authorid= new ProtocolData("agentid", LoginUtil.mLoginStatus.agentid);
			chinalData.addChild(authorid);
		}
		
		/*
		 * 代理商类型agenttypeid, 0普通用户，1正式代理商，2虚拟代理商
		 */
		if(LoginUtil.mLoginStatus.agenttypeid != null){
			ProtocolData agenttypeid= new ProtocolData("agenttypeid", LoginUtil.mLoginStatus.agenttypeid);
			chinalData.addChild(agenttypeid);
		}
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		Set<String> keysSet = temp.sunMap.keySet();
		for(String key : keysSet){
			ProtocolData item = new ProtocolData(key, temp.getValue(key));
			bodyData.addChild(item);
//			if(key.equals("authorid")){
//				chinalData.addChild(item);
//			}
		}
		
		headerData.addChild(chinalData);
		
		/**
		 * 构造请求头
		 */
		for(ProtocolData data2 : structParams()){
			headerData.addChild(data2);
		}
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	/**
	 * 构造空的req_token网络请求数据
	 * @param apiName
	 * @param apiFunc
	 * @param data
	 * @return
	 */
	public static synchronized List<ProtocolData> getNullReqTokenRequestDatas(String apiName,String apiFunc,SunType data){
		BaseData temp =null;
		if(data instanceof BaseData){
			temp = (BaseData)data;
		}
		
		if(temp == null)
		{
			return null;
		}
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, apiName);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, apiFunc);
		if(LoginUtil.mLoginStatus.authorid != null){
			ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
			chinalData.addChild(authorid);
		}
		if(LoginUtil.mLoginStatus.agentid != null){
			ProtocolData authorid= new ProtocolData("agentid", LoginUtil.mLoginStatus.agentid);
			chinalData.addChild(authorid);
		}
		
		/*
		 * 代理商类型agenttypeid, 0普通用户，1正式代理商，2虚拟代理商
		 */
		if(LoginUtil.mLoginStatus.agenttypeid != null){
			ProtocolData agenttypeid= new ProtocolData("agenttypeid", LoginUtil.mLoginStatus.agenttypeid);
			chinalData.addChild(agenttypeid);
		}
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		Set<String> keysSet = temp.sunMap.keySet();
		for(String key : keysSet){
			ProtocolData item = new ProtocolData(key, temp.getValue(key));
			bodyData.addChild(item);
//			if(key.equals("authorid")){
//				chinalData.addChild(item);
//			}
		}
		
		headerData.addChild(chinalData);
		
		/**
		 * 构造请求头
		 */
		for(ProtocolData data2 : structNullReqTokenParams()){
			headerData.addChild(data2);
		}
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	/**
	 * 构造代理商申请提交信息的网络请求数据
	 * @param apiName
	 * @param apiFunc
	 * @param data
	 * @return
	 */
	public static synchronized List<ProtocolData> getAgentApplyRequestDatas(String apiName,String apiFunc,SunType data,AgentApplyInfoData applyData){
		BaseData temp =null;
		if(data instanceof BaseData){
			temp = (BaseData)data;
		}
		
		if(temp == null)
		{
			return null;
		}
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, apiName);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, apiFunc);
		if(LoginUtil.mLoginStatus.authorid != null){
			ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
			chinalData.addChild(authorid);
		}
		if(LoginUtil.mLoginStatus.agentid != null){
			ProtocolData authorid= new ProtocolData("agentid", LoginUtil.mLoginStatus.agentid);
			chinalData.addChild(authorid);
		}
		
		/*
		 * 代理商类型agenttypeid, 0普通用户，1正式代理商，2虚拟代理商
		 */
		if(LoginUtil.mLoginStatus.agenttypeid != null){
			ProtocolData agenttypeid= new ProtocolData("agenttypeid", LoginUtil.mLoginStatus.agenttypeid);
			chinalData.addChild(agenttypeid);
		}
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		Set<String> keysSet = temp.sunMap.keySet();
		for(String key : keysSet){
			ProtocolData item = new ProtocolData(key, temp.getValue(key));
			bodyData.addChild(item);
//			if(key.equals("authorid")){
//				chinalData.addChild(item);
//			}
		}
		for(int i = 0; i < applyData.infoDataList.size(); i ++) {
			
			ProtocolData upfileinfoData= new ProtocolData("upfileinfo", null);
			ProtocolData picpath= new ProtocolData("picpath", applyData.infoDataList.get(i).getValue("picuploadurl"));
			ProtocolData pictypeid= new ProtocolData("pictypeid", applyData.infoDataList.get(i).getValue("pictypeid"));
			ProtocolData pictypename= new ProtocolData("pictypename", applyData.infoDataList.get(i).getValue("pictypename"));
			upfileinfoData.addChild(picpath);
			upfileinfoData.addChild(pictypeid);
			upfileinfoData.addChild(pictypename);
			bodyData.addChild(upfileinfoData);
		}
		
		headerData.addChild(chinalData);
		
		/**
		 * 构造请求头
		 */
		for(ProtocolData data2 : structParams()){
			headerData.addChild(data2);
		}
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	/**
	 * 构造设置密保的网络请求数据
	 * @param apiName
	 * @param apiFunc
	 * @param data
	 * @param pwdSafetyData 密保问题id、问题答案的集合
	 * @return
	 */
	public static synchronized List<ProtocolData> getPwdSafetyRequestDatas(String apiName,String apiFunc,SunType data, ArrayList<PwdSafetySettingData> pwdSafetyData){
		BaseData temp =null;
		if(data instanceof BaseData){
			temp = (BaseData)data;
		}
		
		if(temp == null)
		{
			return null;
		}
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, apiName);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, apiFunc);
		if(LoginUtil.mLoginStatus.authorid != null){
			ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
			chinalData.addChild(authorid);
		}
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		Set<String> keysSet = temp.sunMap.keySet();
		for(String key : keysSet){
			ProtocolData item = new ProtocolData(key, temp.getValue(key));
			bodyData.addChild(item);
//			if(key.equals("authorid")){
//				chinalData.addChild(item);
//			}
		}

		//构造设置密保的数据
		for(int i = 0; i < pwdSafetyData.size(); i ++) {
			String pwdKey;
			if(i == 0) {
				pwdKey = "msgchild1";
			}else if(i == 1) {
				pwdKey = "msgchild2";
			}else{
				pwdKey = "msgchild3";
			}
			ProtocolData msgchildSafetyData= new ProtocolData(pwdKey, null);
			ProtocolData queid= new ProtocolData("queid", pwdSafetyData.get(i).queid);
			ProtocolData answer= new ProtocolData("answer", pwdSafetyData.get(i).answer);
			msgchildSafetyData.addChild(queid);
			msgchildSafetyData.addChild(answer);
			bodyData.addChild(msgchildSafetyData);
		}
		
		headerData.addChild(chinalData);
		
		/**
		 * 构造请求头
		 */
		for(ProtocolData data2 : structParams()){
			headerData.addChild(data2);
		}
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	/**
	 * 构造自定义的网络请求数据
	 * @param apiName
	 * @param apiFunc
	 * @param data
	 * @return
	 */
	public static synchronized List<ProtocolData> getCustomRequestDatas(String apiName,String apiFunc,SunType data){
		BaseData temp =null;
		if(data instanceof BaseData){
			temp = (BaseData)data;
		}
		
		if(temp == null)
		{
			return null;
		}
		List<ProtocolData> mDatas = new ArrayList<ProtocolData>();
		ProtocolData headerData = ProtocolUtil.headerData();
		ProtocolData chinalData= new ProtocolData(ProtocolUtil.channelinfo, null);
		ProtocolData name= new ProtocolData(ProtocolUtil.api_name, apiName);
		ProtocolData name_func= new ProtocolData(ProtocolUtil.api_name_func, apiFunc);
		if(LoginUtil.mLoginStatus.authorid != null){
			ProtocolData authorid= new ProtocolData("authorid", LoginUtil.mLoginStatus.authorid);
			chinalData.addChild(authorid);
		}
		if(LoginUtil.mLoginStatus.agentid != null){
			ProtocolData authorid= new ProtocolData("agentid", LoginUtil.mLoginStatus.agentid);
			chinalData.addChild(authorid);
		}
		
		/*
		 * 代理商类型agenttypeid, 0普通用户，1正式代理商，2虚拟代理商
		 */
		if(LoginUtil.mLoginStatus.agenttypeid != null){
			ProtocolData agenttypeid= new ProtocolData("agenttypeid", LoginUtil.mLoginStatus.agenttypeid);
			chinalData.addChild(agenttypeid);
		}
		chinalData.addChild(name);
		chinalData.addChild(name_func);
		
		
		
		ProtocolData bodyData = new ProtocolData(ProtocolUtil.msgbody, null);
		
		Set<String> keysSet = temp.sunMap.keySet();
		for(String key : keysSet){
			ProtocolData item = new ProtocolData(key, temp.getValue(key));
			bodyData.addChild(item);
//			if(key.equals("authorid")){
//				chinalData.addChild(item);
//			}
		}
		
		//机票订单请求信息XML构建
		if(temp instanceof AirTicketCreateOrderData) {
			AirTicketCreateOrderData createOrderData = (AirTicketCreateOrderData)temp;
			
//			//飞机票信息
//			Set<String> ticketKeysSet = createOrderData.ticketMap.keySet();
//			
//			ProtocolData ticketData= new ProtocolData("ticket", null);
//			
//			for(String key : ticketKeysSet){
//				ProtocolData item = new ProtocolData(key, createOrderData.ticketMap.get(key));
//				ticketData.addChild(item);
//			}
//			bodyData.addChild(ticketData);
			
			
			//飞机票信息
			ProtocolData ticketListData= new ProtocolData("ticketList", null);
			
			for(HashMap<String, String> ticketMap : createOrderData.ticketListMap) {
				
//				ProtocolData ticketData= new ProtocolData("ticketId", null);
				
				Set<String> ticketSet = ticketMap.keySet();
				for(String key : ticketSet) {
					ProtocolData item = new ProtocolData("ticketId", ticketMap.get(key));
					ticketListData.addChild(item);
				}
				
//				ticketListData.addChild(ticketData);
				
			}
			bodyData.addChild(ticketListData);
			
			
			
			//乘机人信息
			ProtocolData passengerListData= new ProtocolData("passengerList", null);
			
			for(HashMap<String, String> passengerMap : createOrderData.passengerList) {
				
//				ProtocolData passengerData= new ProtocolData("passengerId", null);
				
				Set<String> passengerSet = passengerMap.keySet();
				for(String key : passengerSet) {
					ProtocolData item = new ProtocolData("passengerId", passengerMap.get(key));
					passengerListData.addChild(item);
				}
				
//				passengerListData.addChild(passengerData);
				
			}
			bodyData.addChild(passengerListData);
			
			//联系人信息
			ProtocolData contacterListData= new ProtocolData("contacterList", null);
			
			for(HashMap<String, String> contacterMap : createOrderData.contacterList) {
				
//				ProtocolData contacterData= new ProtocolData("contacterId", null);
				
				Set<String> contacterSet = contacterMap.keySet();
				for(String key : contacterSet) {
					ProtocolData item = new ProtocolData("contacterId", contacterMap.get(key));
					contacterListData.addChild(item);
				}
				
//				contacterListData.addChild(contacterData);
				
			}
			bodyData.addChild(contacterListData);
			
			
			//支付信息
			Set<String> payinfoKeysSet = createOrderData.payinfoMap.keySet();
			
			ProtocolData payinfoData= new ProtocolData("payinfo", null);
			
			for(String key : payinfoKeysSet){
				ProtocolData item = new ProtocolData(key, createOrderData.payinfoMap.get(key));
				payinfoData.addChild(item);
			}
			bodyData.addChild(payinfoData);
		}
		
		headerData.addChild(chinalData);
		
		/**
		 * 构造请求头
		 */
		for(ProtocolData data2 : structParams()){
			headerData.addChild(data2);
		}
		
		mDatas.add(headerData);
		mDatas.add(bodyData);
		
		return mDatas;
	}
	
	public static synchronized void printString(String tag,String test){
		String ecryptString = test.substring(1,test.length());
		byte[] decrypt = ecryptString.getBytes();
		String indexByte=test.substring(0,1);
		String decryptString = EncryptUtil.decrypt(new String(decrypt), 
				Integer.parseInt(indexByte));
		Logger.d(tag, decryptString);
	}
	
	private static synchronized ArrayList<ProtocolData> structParams(){
		ArrayList<ProtocolData> datas = new ArrayList<ProtocolData>();
		int index = EncryptUtil.createRandomkeySort();
		Logger.d(TAG,"index: "+index);
		String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String token="";
		String auto_token=null;
		if(LoginUtil.isLogin && LoginUtil.mLoginStatus.mResponseData != null &&LoginUtil.mLoginStatus.mResponseData.getReq_token() !=null){
			token = LoginUtil.mLoginStatus.mResponseData.getReq_token();
			Logger.d("login","从缓存获取"+token);
			printString("login",token);
		}else if(!"".equals(PreferenceConfig.instance(PayApp.pay).getString(Constants.REQ_TOKEN, ""))){
			token=PreferenceConfig.instance(PayApp.pay).getString(Constants.REQ_TOKEN, "");
		}else{
			StringBuffer req_token= new StringBuffer();
			//添加mac地址
			req_token.append(getLocalMacAddress(PayApp.pay));
			req_token.append("@@");
			req_token.append(time);
			req_token.append("@@");
			req_token.append(LoginUtil.mLoginStatus.login_name);
			req_token.append("@@");
			req_token.append(LoginUtil.mLoginStatus.login_pwd);
			
			Logger.d(TAG, req_token.toString());
			String encryptData = EncryptUtil.encrypt(req_token.toString(), index);
			token = index+encryptData;
			Logger.d(TAG,"token: "+token);
			
			Logger.d(TAG,"decryptdata: "+EncryptUtil.decrypt(encryptData, index));
		}
		Log.i("req_token.cn", token);
		
		ProtocolData tokenData = new ProtocolData("req_token", token);
		ProtocolData req_time = new ProtocolData("req_time", time);
		ProtocolData req_appenv = new ProtocolData("req_appenv", "1");
		ProtocolData au_token = null;
		
		//读取请求网络的动态码 au_token
		if(PayApp.pay != null){
			auto_token = PreferenceConfig.instance(PayApp.pay).getString(Constants.AUTO_TOKEN, null);
		}
		
		if(auto_token != null){
			//因LoginUtil状态会丢失，暂用PreferenceConfig保存的动态码,每次请求都更新，作为优先判断依据
			au_token  = new ProtocolData("au_token",auto_token);
		}else if(LoginUtil.mLoginStatus.mResponseData != null &&LoginUtil.mLoginStatus.mResponseData.getAu_token() !=null){
			au_token  = new ProtocolData("au_token",LoginUtil.mLoginStatus.mResponseData.getAu_token());
		}else{
			au_token  = new ProtocolData("au_token","");
		}
		ProtocolData req_version = new ProtocolData("req_version", VersionUtil.getVersionName(PayApp.pay));
//		ProtocolData req_auth =new ProtocolData("req_auth", UnionpayUtil.mMode);
		ProtocolData req_bkenv = null;
		if(LoginUtil.mLoginStatus.mResponseData != null &&LoginUtil.mLoginStatus.mResponseData.getReq_bkenv() !=null){
			req_bkenv  = new ProtocolData("req_bkenv",LoginUtil.mLoginStatus.mResponseData.getReq_bkenv());
		}else{
			req_bkenv = new ProtocolData("req_bkenv", UnionpayUtil.mMode);
		}
		datas.add(tokenData);
		datas.add(req_time);
		if(au_token!=null){
			datas.add(au_token);
		}
		datas.add(req_version);
//		datas.add(req_auth);
		datas.add(req_appenv);
		datas.add(req_bkenv);
		return datas;
	}
	
	
	private static synchronized ArrayList<ProtocolData> structNullReqTokenParams(){
		ArrayList<ProtocolData> datas = new ArrayList<ProtocolData>();
		int index = EncryptUtil.createRandomkeySort();
		Logger.d(TAG,"index: "+index);
		String time = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date());
		String token="";
		String auto_token=null;
		ProtocolData tokenData = new ProtocolData("req_token", token);
		ProtocolData req_time = new ProtocolData("req_time", time);
		ProtocolData req_appenv = new ProtocolData("req_appenv", "1");
		ProtocolData au_token = null;
		
		//读取请求网络的动态码 au_token
		if(PayApp.pay != null){
			auto_token = PreferenceConfig.instance(PayApp.pay).getString(Constants.AUTO_TOKEN, null);
		}
		
		if(auto_token != null){
			//因LoginUtil状态会丢失，暂用PreferenceConfig保存的动态码,每次请求都更新，作为优先判断依据
			au_token  = new ProtocolData("au_token",auto_token);
		}else if(LoginUtil.mLoginStatus.mResponseData != null &&LoginUtil.mLoginStatus.mResponseData.getAu_token() !=null){
			au_token  = new ProtocolData("au_token",LoginUtil.mLoginStatus.mResponseData.getAu_token());
		}else{
			au_token  = new ProtocolData("au_token","");
		}
		ProtocolData req_version = new ProtocolData("req_version", VersionUtil.getVersionName(PayApp.pay));
//		ProtocolData req_auth =new ProtocolData("req_auth", UnionpayUtil.mMode);
		ProtocolData req_bkenv = null;
		if(LoginUtil.mLoginStatus.mResponseData != null &&LoginUtil.mLoginStatus.mResponseData.getReq_bkenv() !=null){
			req_bkenv  = new ProtocolData("req_bkenv",LoginUtil.mLoginStatus.mResponseData.getReq_bkenv());
		}else{
			req_bkenv = new ProtocolData("req_bkenv", UnionpayUtil.mMode);
		}
		datas.add(tokenData);
		datas.add(req_time);
		if(au_token!=null){
			datas.add(au_token);
		}
		datas.add(req_version);
//		datas.add(req_auth);
		datas.add(req_appenv);
		datas.add(req_bkenv);
		return datas;
	}
	
   public static synchronized String getLocalMacAddress(Context context) { 

        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE); 

        WifiInfo info = wifi.getConnectionInfo(); 

//        Log.i("MAC address:", info.getMacAddress());
        return info.getMacAddress(); 

   }  
	
}
