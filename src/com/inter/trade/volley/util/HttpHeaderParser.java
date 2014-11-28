package com.inter.trade.volley.util;

import java.util.Map;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;
import org.apache.http.protocol.HTTP;

import com.inter.trade.volley.Cache;
import com.inter.trade.volley.NetworkResponse;


public class HttpHeaderParser
{

	public static Cache.Entry parseCacheHeaders(NetworkResponse response)
	{
		long now = System.currentTimeMillis();

		Map<String, String> headers = response.headers;

		long serverDate = 0;
		long serverExpires = 0;
		long softExpire = 0;
		long maxAge = 0;
		boolean hasCacheControl = false;

		String serverEtag = null;
		String headerValue;

		headerValue = headers.get("Date");
		if (headerValue != null)
		{
			serverDate = parseDateAsEpoch(headerValue);
		}

		headerValue = headers.get("Cache-Control");
		if (headerValue != null)
		{
			hasCacheControl = true;
			String[] tokens = headerValue.split(",");
			for (int i = 0; i < tokens.length; i++)
			{
				String token = tokens[i].trim();
				if (token.equals("no-cache") || token.equals("no-store"))
				{
					return null;
				}
				else if (token.startsWith("max-age="))
				{
					try
					{
						maxAge = Long.parseLong(token.substring(8));
					}
					catch (Exception e)
					{
					}
				}
				else if (token.equals("must-revalidate")
						|| token.equals("proxy-revalidate"))
				{
					maxAge = 0;
				}
			}
		}

		headerValue = headers.get("Expires");
		if (headerValue != null)
		{
			serverExpires = parseDateAsEpoch(headerValue);
		}

		serverEtag = headers.get("ETag");

		if (hasCacheControl)
		{
			softExpire = now + maxAge * 1000;
		}
		else if (serverDate > 0 && serverExpires >= serverDate)
		{
			softExpire = now + (serverExpires - serverDate);
		}

		Cache.Entry entry = new Cache.Entry();
		entry.data = response.data;
		entry.etag = serverEtag;
		entry.softTtl = softExpire;
		entry.ttl = entry.softTtl;
		entry.serverDate = serverDate;
		entry.responseHeaders = headers;

		return entry;
	}

	public static long parseDateAsEpoch(String dateStr)
	{
		try
		{
			return DateUtils.parseDate(dateStr).getTime();
		}
		catch (DateParseException e)
		{
			return 0;
		}
	}

	public static String parseCharset(Map<String, String> headers)
	{
		String contentType = headers.get(HTTP.CONTENT_TYPE);
		if (contentType != null)
		{
			String[] params = contentType.split(";");
			for (int i = 1; i < params.length; i++)
			{
				String[] pair = params[i].trim().split("=");
				if (pair.length == 2)
				{
					if (pair[0].equals("charset"))
					{
						return pair[1];
					}
				}
			}
		}

		return HTTP.DEFAULT_CONTENT_CHARSET;
	}
}
