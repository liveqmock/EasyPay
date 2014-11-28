package com.inter.trade.volley.util;

import com.inter.trade.volley.Cache;
import com.inter.trade.volley.NetworkResponse;
import com.inter.trade.volley.Request;
import com.inter.trade.volley.Response;

import android.os.Handler;
import android.os.Looper;


public class ClearCacheRequest extends Request<Object>
{
	private final Cache mCache;
	private final Runnable mCallback;

	public ClearCacheRequest(Cache cache, Runnable callback)
	{
		super(Method.GET, null, null);
		mCache = cache;
		mCallback = callback;
	}

	@Override
	public boolean isCanceled()
	{
		mCache.clear();
		if (mCallback != null)
		{
			Handler handler = new Handler(Looper.getMainLooper());
			handler.postAtFrontOfQueue(mCallback);
		}
		return true;
	}

	@Override
	public Priority getPriority()
	{
		return Priority.IMMEDIATE;
	}

	@Override
	protected Response<Object> parseNetworkResponse(NetworkResponse response)
	{
		return null;
	}

	@Override
	protected void deliverResponse(Object response)
	{
	}
}
