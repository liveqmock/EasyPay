package com.inter.trade.volley.util;

import java.io.UnsupportedEncodingException;

import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.Response.ErrorListener;
import com.inter.trade.volley.Response.Listener;
import com.inter.trade.volley.VolleyLog;


public abstract class JsonRequest<T> extends Request<T>
{
	private static final String PROTOCOL_CHARSET = "utf-8";

	private static final String PROTOCOL_CONTENT_TYPE = String.format(
			"application/json; charset=%s", PROTOCOL_CHARSET);

	private final Listener<T> mListener;
	private final String mRequestBody;

	public JsonRequest(String url, String requestBody, Listener<T> listener,
			ErrorListener errorListener)
	{
		this(Method.DEPRECATED_GET_OR_POST, url, requestBody, listener,
				errorListener);
	}

	public JsonRequest(int method, String url, String requestBody,
			Listener<T> listener, ErrorListener errorListener)
	{
		super(method, url, errorListener);
		mListener = listener;
		mRequestBody = requestBody;
	}

	@Override
	protected void deliverResponse(T response)
	{
		mListener.onResponse(response);
	}

	@Override
	abstract protected Response<T> parseNetworkResponse(NetworkResponse response);

	@Override
	public String getPostBodyContentType()
	{
		return getBodyContentType();
	}

	@Override
	public byte[] getPostBody()
	{
		return getBody();
	}

	@Override
	public String getBodyContentType()
	{
		return PROTOCOL_CONTENT_TYPE;
	}

	@Override
	public byte[] getBody()
	{
		try
		{
			return mRequestBody == null ? null : mRequestBody
					.getBytes(PROTOCOL_CHARSET);
		}
		catch (UnsupportedEncodingException uee)
		{
			VolleyLog
					.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
							mRequestBody, PROTOCOL_CHARSET);
			return null;
		}
	}
}
