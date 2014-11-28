package com.inter.trade.volley.util;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;

import com.inter.trade.volley.AuthFailureError;
import com.inter.trade.volley.Request;


public interface HttpStack
{

	public HttpResponse performRequest(Request<?> request,
			Map<String, String> additionalHeaders) throws IOException,
			AuthFailureError;

}
