package com.inter.trade.volley;

import java.util.Collections;
import java.util.Map;

import org.apache.http.HttpStatus;

public class NetworkResponse
{

	public NetworkResponse(int statusCode, byte[] data,
			Map<String, String> headers, boolean notModified)
	{
		this.statusCode = statusCode;
		this.data = data;
		this.headers = headers;
		this.notModified = notModified;
	}
	public NetworkResponse(byte[] data)
	{
		this(HttpStatus.SC_OK, data, Collections.<String, String> emptyMap(),
				false);
	}

	public NetworkResponse(byte[] data, Map<String, String> headers)
	{
		this(HttpStatus.SC_OK, data, headers, false);
	}

	public final int statusCode;

	public final byte[] data;

	public final Map<String, String> headers;

	public final boolean notModified;
}