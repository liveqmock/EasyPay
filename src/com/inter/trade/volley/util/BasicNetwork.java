package com.inter.trade.volley.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.cookie.DateUtils;

import com.inter.trade.volley.AuthFailureError;
import com.inter.trade.volley.Cache;
import com.inter.trade.volley.Network;
import com.inter.trade.volley.NetworkError;
import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.NoConnectionError;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.RetryPolicy;
import com.inter.trade.volley.ServerError;
import com.inter.trade.volley.TimeoutError;
import com.inter.trade.volley.VolleyError;
import com.inter.trade.volley.VolleyLog;

import android.os.SystemClock;


public class BasicNetwork implements Network
{
	protected static final boolean DEBUG = VolleyLog.DEBUG;

	private static int SLOW_REQUEST_THRESHOLD_MS = 3000;

	private static int DEFAULT_POOL_SIZE = 4096;

	protected final HttpStack mHttpStack;

	protected final ByteArrayPool mPool;

	public BasicNetwork(HttpStack httpStack)
	{
		this(httpStack, new ByteArrayPool(DEFAULT_POOL_SIZE));
	}

	public BasicNetwork(HttpStack httpStack, ByteArrayPool pool)
	{
		mHttpStack = httpStack;
		mPool = pool;
	}

	@Override
	public NetworkResponse performRequest(Request<?> request)
			throws VolleyError
	{
		long requestStart = SystemClock.elapsedRealtime();
		while (true)
		{
			HttpResponse httpResponse = null;
			byte[] responseContents = null;
			Map<String, String> responseHeaders = new HashMap<String, String>();
			try
			{
				Map<String, String> headers = new HashMap<String, String>();
				addCacheHeaders(headers, request.getCacheEntry());
				httpResponse = mHttpStack.performRequest(request, headers);
				StatusLine statusLine = httpResponse.getStatusLine();
				int statusCode = statusLine.getStatusCode();

				responseHeaders = convertHeaders(httpResponse.getAllHeaders());
				if (statusCode == HttpStatus.SC_NOT_MODIFIED)
				{
					return new NetworkResponse(HttpStatus.SC_NOT_MODIFIED,
							request.getCacheEntry() == null ? null : request
									.getCacheEntry().data,
							responseHeaders, true);
				}

				if (httpResponse.getEntity() != null)
				{
					responseContents = entityToBytes(httpResponse.getEntity());
				}
				else
				{
					responseContents = new byte[0];
				}

				long requestLifetime = SystemClock.elapsedRealtime()
						- requestStart;
				logSlowRequests(requestLifetime, request, responseContents,
						statusLine);

				if (statusCode < 200 || statusCode > 299)
				{
					throw new IOException();
				}
				return new NetworkResponse(statusCode, responseContents,
						responseHeaders, false);
			}
			catch (SocketTimeoutException e)
			{
				attemptRetryOnException("socket", request, new TimeoutError());
			}
			catch (ConnectTimeoutException e)
			{
				attemptRetryOnException("connection", request,
						new TimeoutError());
			}
			catch (MalformedURLException e)
			{
				throw new RuntimeException("Bad URL " + request.getUrl(), e);
			}
			catch (IOException e)
			{
				int statusCode = 0;
				NetworkResponse networkResponse = null;
				if (httpResponse != null)
				{
					statusCode = httpResponse.getStatusLine().getStatusCode();
				}
				else
				{
					throw new NoConnectionError(e);
				}
				VolleyLog.e("Unexpected response code %d for %s", statusCode,
						request.getUrl());
				if (responseContents != null)
				{
					networkResponse = new NetworkResponse(statusCode,
							responseContents, responseHeaders, false);
					if (statusCode == HttpStatus.SC_UNAUTHORIZED
							|| statusCode == HttpStatus.SC_FORBIDDEN)
					{
						attemptRetryOnException("auth", request,
								new AuthFailureError(networkResponse));
					}
					else
					{
						throw new ServerError(networkResponse);
					}
				}
				else
				{
					throw new NetworkError(networkResponse);
				}
			}
		}
	}

	private void logSlowRequests(long requestLifetime, Request<?> request,
			byte[] responseContents, StatusLine statusLine)
	{
		if (DEBUG || requestLifetime > SLOW_REQUEST_THRESHOLD_MS)
		{
			VolleyLog
					.d("HTTP response for request=<%s> [lifetime=%d], [size=%s], "
							+ "[rc=%d], [retryCount=%s]", request,
							requestLifetime,
							responseContents != null ? responseContents.length
									: "null", statusLine.getStatusCode(),
							request.getRetryPolicy().getCurrentRetryCount());
		}
	}

	private static void attemptRetryOnException(String logPrefix,
			Request<?> request, VolleyError exception) throws VolleyError
	{
		RetryPolicy retryPolicy = request.getRetryPolicy();
		int oldTimeout = request.getTimeoutMs();

		try
		{
			retryPolicy.retry(exception);
		}
		catch (VolleyError e)
		{
			request.addMarker(String.format("%s-timeout-giveup [timeout=%s]",
					logPrefix, oldTimeout));
			throw e;
		}
		request.addMarker(String.format("%s-retry [timeout=%s]", logPrefix,
				oldTimeout));
	}

	private void addCacheHeaders(Map<String, String> headers, Cache.Entry entry)
	{
		if (entry == null)
		{
			return;
		}

		if (entry.etag != null)
		{
			headers.put("If-None-Match", entry.etag);
		}

		if (entry.serverDate > 0)
		{
			Date refTime = new Date(entry.serverDate);
			headers.put("If-Modified-Since", DateUtils.formatDate(refTime));
		}
	}

	protected void logError(String what, String url, long start)
	{
		long now = SystemClock.elapsedRealtime();
		VolleyLog.v("HTTP ERROR(%s) %d ms to fetch %s", what, (now - start),
				url);
	}

	private byte[] entityToBytes(HttpEntity entity) throws IOException,
			ServerError
	{
		PoolingByteArrayOutputStream bytes = new PoolingByteArrayOutputStream(
				mPool, (int) entity.getContentLength());
		byte[] buffer = null;
		try
		{
			InputStream in = entity.getContent();
			if (in == null)
			{
				throw new ServerError();
			}
			buffer = mPool.getBuf(1024);
			int count;
			while ((count = in.read(buffer)) != -1)
			{
				bytes.write(buffer, 0, count);
			}
			return bytes.toByteArray();
		}
		finally
		{
			try
			{

				entity.consumeContent();
			}
			catch (IOException e)
			{

				VolleyLog.v("Error occured when calling consumingContent");
			}
			mPool.returnBuf(buffer);
			bytes.close();
		}
	}

	private static Map<String, String> convertHeaders(Header[] headers)
	{
		Map<String, String> result = new HashMap<String, String>();
		for (int i = 0; i < headers.length; i++)
		{
			result.put(headers[i].getName(), headers[i].getValue());
		}
		return result;
	}
}
