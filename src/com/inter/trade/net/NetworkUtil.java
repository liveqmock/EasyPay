package com.inter.trade.net;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.inter.trade.ui.PayApp;
import com.inter.trade.util.EncryptUtil;
import com.inter.trade.util.VersionUtil;
import com.inter.trade.util.XmlUtil;
import com.inter.trade.volley.AuthFailureError;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.util.StringRequest;

import android.content.Context;

public class NetworkUtil
{
	public static void checkUpdate(Context context,
			Response.Listener<String> listener,
			Response.ErrorListener errorListener)
	{
		String url = Constant.RUQUESTURL;

		final int index = EncryptUtil.createRandomkeySort();

		String msgHeaderValue = XmlUtil.parseXmlToNodeStr(initHeader(index));

		LinkedHashMap<String, String> msgBody = new LinkedHashMap<String, String>();
		msgBody.put("apptype", "1");
		msgBody.put("appversion", "3.3.3");
		String msgBodyValue = XmlUtil.parseXmlToNodeStr(msgBody);

		LinkedHashMap<String, String> param = new LinkedHashMap<String, String>();
		param.put("msgheader", msgHeaderValue);
		param.put("msgbody", msgBodyValue);
		String paramValue = XmlUtil.parseXmlToStr("operation_request", param);

		final String requestStr = EncryptUtil.encrypt(paramValue, index);

		StringRequest request = new StringRequest(Request.Method.POST, url,
				listener, errorListener)
		{
			@Override
			protected Map<String, String> getParams() throws AuthFailureError
			{
				Map<String, String> params = new HashMap<String, String>();
				params.put(String.valueOf(index), requestStr);
				return params;
			}
		};
		PayApp.getInstance().addToRequestQueue(request);
	}
	
	private static LinkedHashMap<String,String> initHeader(int index)
	{
		LinkedHashMap<String, String> channelInfo = new LinkedHashMap<String, String>();
		channelInfo.put("api_name_func", "checkAppVersion");
		channelInfo.put("api_name", "ApiAppInfo");

		String channelValue = XmlUtil.parseXmlToNodeStr(channelInfo);

		LinkedHashMap<String, String> msgHeader = new LinkedHashMap<String, String>();
		msgHeader.put("req_token", "");
		msgHeader.put("req_time", new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()));
		msgHeader.put("au_token", "zHBBUseQrqzJlA7l/L911GYA");
		msgHeader.put("req_version", VersionUtil.getVersionName(PayApp.pay));
		msgHeader.put("req_appenv", "00");
		msgHeader.put("req_bkenv", "1");
		msgHeader.put("channelinfo", channelValue);
		return msgHeader;
	}
}
