package com.inter.trade.volley.util;

import java.io.UnsupportedEncodingException;

import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.Response.ErrorListener;
import com.inter.trade.volley.Response.Listener;


public class StringRequest extends Request<String>
{
	private final Listener<String> mListener;

	public StringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener)
	{
		super(method, url, errorListener);
		mListener = listener;
	}

	public StringRequest(String url, Listener<String> listener,
			ErrorListener errorListener)
	{
		this(Method.GET, url, listener, errorListener);
	}

	@Override
	protected void deliverResponse(String response)
	{
		mListener.onResponse(response);
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response)
	{
		String parsed;
		try
		{
			parsed = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		}
		catch (UnsupportedEncodingException e)
		{
			parsed = new String(response.data);
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}
}
