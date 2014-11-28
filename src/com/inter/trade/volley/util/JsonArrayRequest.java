package com.inter.trade.volley.util;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;

import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.ParseError;
import com.inter.trade.volley.Response;
import com.inter.trade.volley.Response.ErrorListener;
import com.inter.trade.volley.Response.Listener;


public class JsonArrayRequest extends JsonRequest<JSONArray>
{

	public JsonArrayRequest(String url, Listener<JSONArray> listener,
			ErrorListener errorListener)
	{
		super(Method.GET, url, null, listener, errorListener);
	}

	/**
	 * �����ص����ת����JSON����
	 */
	@Override
	protected Response<JSONArray> parseNetworkResponse(NetworkResponse response)
	{
		try
		{
			String jsonString = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			return Response.success(new JSONArray(jsonString),
					HttpHeaderParser.parseCacheHeaders(response));
		}
		catch (UnsupportedEncodingException e)
		{
			return Response.error(new ParseError(e));
		}
		catch (JSONException je)
		{
			return Response.error(new ParseError(je));
		}
	}
}
